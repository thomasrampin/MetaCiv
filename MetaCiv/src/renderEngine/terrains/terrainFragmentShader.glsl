#version 430 core


#define GL_ARB_bindless_texture_is_supported 0


// Enable bindless textures
#if GL_ARB_bindless_texture_is_supported
	#extension GL_ARB_bindless_texture : require
#endif

#define MAX_TERRAIN_TYPE 11

layout (location = 0) out vec4 FragmentColor0;
layout (location = 1) out vec4 FragmentColor1;



uniform sampler2D blendMap;
uniform sampler2D reflexion;
uniform sampler2D shadowMap;
uniform sampler2D cliffMap;
uniform sampler2D roadMap;

#if GL_ARB_bindless_texture_is_supported
	layout(binding = 5, std140) uniform TEXTURE_BLOCK
	{
		sampler2D gSampler[MAX_TERRAIN_TYPE];
	};
#else
	uniform sampler2D gSampler[MAX_TERRAIN_TYPE];
#endif

uniform vec4 fog_color = vec4(0.4, 0.6, 0.9, 0);

uniform bool forceLow;

in VS_OUT
{
	vec2 tc;
	vec3 world_coord;
	vec3 toLightVector;
	vec3 toCameraVector;
	vec3 eye_coord;
	vec3 normal;
	vec3 tangent;
	vec3 viewDir;
	vec4 shadow_coord;
	vec3 TangentLightPos;
	vec3 TangentViewPos;
	vec3 TangentFragPos;
	float height;
	mat3 RM;
	mat3 TBN;
	vec3 Pos;
}fs_in;


uniform vec4 heights[MAX_TERRAIN_TYPE];
uniform int heights_size;

uniform float distanceFog;



out vec4 out_Color;




uniform vec3 lightColour;
uniform float shineDamper;
uniform vec3 reflectivity;
uniform vec3 diffuseColour;
uniform bool textured;
uniform float snow;
uniform float snowAttenuation;
uniform float snowDensity;
uniform float roadTiling;
uniform float cliffTiling;

vec4 fog(vec4 c)
{
    float z = length(fs_in.eye_coord)/distanceFog;

    float de = 0.025 * smoothstep(0.0, 6.0, 10.0 - fs_in.world_coord.y);
    float di = 0.045 * (smoothstep(0.0, 20.0, 20.0 - fs_in.world_coord.y));

    float extinction   = exp(-z * de);
    float inscattering = exp(-z * di);

    return c * extinction + fog_color * (1.0 - inscattering);
}



uniform float height_scale=0.1;

vec2 ParallaxMapping(int indice,vec2 texCoords, vec3 viewDir)
{
    // number of depth layers
    const float minLayers = 10;
    const float maxLayers = 20;
    float numLayers = mix(maxLayers, minLayers, abs(dot(vec3(0.0, 0.0, 1.0), viewDir)));
    // calculate the size of each layer
    float layerDepth = 1.0 / numLayers;
    // depth of current layer
    float currentLayerDepth = 0.0;
    // the amount to shift the texture coordinates per layer (from vector P)
    vec2 P = viewDir.xy / viewDir.z * height_scale;
    vec2 deltaTexCoords = P / numLayers;

    // get initial values
    vec2  currentTexCoords     = texCoords;
    float currentDepthMapValue = texture(gSampler[indice], currentTexCoords).r;

    while(currentLayerDepth < currentDepthMapValue)
    {
        // shift texture coordinates along direction of P
        currentTexCoords -= deltaTexCoords;
        // get depthmap value at current texture coordinates
        currentDepthMapValue = texture(gSampler[indice], currentTexCoords).r;
        // get depth of next layer
        currentLayerDepth += layerDepth;
    }

    // -- parallax occlusion mapping interpolation from here on
    // get texture coordinates before collision (reverse operations)
    vec2 prevTexCoords = currentTexCoords + deltaTexCoords;

    // get depth after and before collision for linear interpolation
    float afterDepth  = currentDepthMapValue - currentLayerDepth;
    float beforeDepth = texture(gSampler[indice], prevTexCoords).r - currentLayerDepth + layerDepth;

    // interpolation of texture coordinates
    float weight = afterDepth / (afterDepth - beforeDepth);
    vec2 finalTexCoords = prevTexCoords * weight + currentTexCoords * (1.0 - weight);

    return finalTexCoords;
}

vec3 blend(vec4 texture1, float a1, vec4 texture2, float a2)
{
    float depth = 0.2;
    float ma = max(texture1.a + a1, texture2.a + a2) - depth;

    float b1 = max(texture1.a + a1 - ma, 0);
    float b2 = max(texture2.a + a2 - ma, 0);

    return (texture1.rgb * b1 + texture2.rgb * b2) / (b1 + b2);
}

void main(void){


	vec4 landscape;

	float red = texture(blendMap,fs_in.tc).r;
	float blue = texture(blendMap,fs_in.tc).b;
	float green = texture(blendMap,fs_in.tc).g;
	int indice = -1;
	int indice2 = -1;
	bool isRoad=(red==0.0 && green==0.0 && blue==0.0);


	vec3 viewDir = normalize(fs_in.TangentViewPos - fs_in.TangentFragPos);

	vec3 unitVectorToCamera = normalize(fs_in.toCameraVector);


	//cliff detector
	//Get face normal
	vec3 X = dFdx(fs_in.Pos);
	vec3 Y = dFdy(fs_in.Pos);
	vec3 normal=normalize(cross(X,Y));
	bool isCliff = normal.y<0.6 && fs_in.height>1.0;


	//vec2 texCoords = ParallaxMapping(indice,parseTcDisp,fs_in.viewDir);
	float blendValue=0;
	if(fs_in.height>=heights[0].w){
		indice = 0;
	}else{
		for(int i=1;i<=heights_size;i++){
			if(fs_in.height>=heights[i].w){
				indice = i-1;
				indice2 = i;

				blendValue = ((fs_in.height-heights[i].w)/(heights[i-1].w-heights[i].w));
				break;
			}
		}
	}
	if(indice==-1)
		indice = heights_size-1;

	float tiling = heights[indice].x;
	if(isRoad)
		tiling = roadTiling;
	if(isCliff)
		tiling = cliffTiling;

	float x = mod(fs_in.tc.x*tiling/2.0,0.5);
	vec2 parseTc = vec2(x,fs_in.tc.y*tiling)/4.0+0.25;


	vec2 parseTcNrm = vec2(x,fs_in.tc.y*tiling)/4.0+(0.75);


	vec2 parseTcDisp = vec2(x,fs_in.tc.y*15 )/2.0+(0.666);

	x = mod(fs_in.tc.x*heights[indice2].x/2.0,0.5);
	vec2 parseTc2 = vec2(x,fs_in.tc.y*heights[indice2].x )/4.0+0.25;


	vec2 parseTcNrm2 = vec2(x,fs_in.tc.y*heights[indice2].x )/4.0+(0.75);





	blendValue = 1-blendValue;


	vec3 unitNormal;

	if(!isCliff){
		if(isRoad){
			unitNormal = normalize((texture(roadMap,parseTcNrm).rbg * 2.0 - 1.0) );
		}else{
		if(indice2==-1)
			unitNormal = normalize((texture(gSampler[indice],parseTcNrm).rbg * 2.0 - 1.0) );
		else
			unitNormal = normalize((mix(texture(gSampler[indice],parseTcNrm),texture(gSampler[indice2],parseTcNrm2),blendValue)).rbg * 2.0 - 1.0);
		}
	}else
		unitNormal = normalize((texture(cliffMap,parseTcNrm).rbg * 2.0 - 1.0) );


	vec3 unitNormalBasic = normalize(fs_in.normal);

	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	vec3 lightDir = normalize(fs_in.TangentLightPos - fs_in.TangentFragPos);
	vec3 unitLightVector = normalize(fs_in.toLightVector);

	float nDotl = dot(unitNormal,unitLightVector);
	float nDotl2 = dot(unitNormalBasic,unitLightVector);
	float brightness = max(nDotl,0.0);
	float brightness2 = max(nDotl2,0.3);

	vec3 lightDirection = -unitLightVector;





	totalDiffuse = brightness * brightness2 * lightColour;


	vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);


	vec3 specular = max(pow(dot(reflectedLightDirection,unitVectorToCamera),shineDamper),0.0) * reflectivity;

	// Brighter
	totalDiffuse = totalDiffuse * diffuseColour;




	if(!isCliff){
		if(isRoad)
			landscape = texture(roadMap,parseTc)+ vec4(specular,1.0);
		else{
		if(indice2==-1)
			landscape = texture(gSampler[indice],parseTc) ;
		else
			landscape = mix(texture(gSampler[indice],parseTc),texture(gSampler[indice2],parseTc2),blendValue);
		}
	}else{
		landscape = texture(cliffMap,parseTc);
	}








	landscape *= vec4(totalDiffuse,1.0)  ;//+ vec4(specular,1.0);
	/*if(fs_in.height>50 && unitNormalBasic.y>0.9){
		landscape = (vec4(0.9 ) + vec4(specular,1.0)) * brightness2;
	}*/
	if(fs_in.height>snow && unitNormal.y>=0 && unitNormal.y<=(min((fs_in.height)/(snow+0.01+snowAttenuation),snowDensity))){
		landscape =(vec4(1.0) + vec4(specular,1.0)) * brightness2;
	}
	vec3 normalReflect = normalize(fs_in.RM * (unitNormal * fs_in.TBN));
	float fresnelFactor = max(dot(normalize(fs_in.toCameraVector),unitNormal),0.8);
	vec3 r = reflect(normalize(fs_in.toCameraVector),unitNormal);
	//Compute texture coordinate based on direction
	vec2 tc;

	tc.y = r.y;
	r.y = 0.0;
	tc.x = normalize(r).x * 0.5;

	float s = sign(r.z) * 0.5;
	tc.s = 0.75 - s * (0.5 - tc.s);
	tc.t = 0.5 +0.5 * tc.t;

	FragmentColor0 = fog(landscape);
	//FragmentColor0 = mix(textureLod(reflexion,tc,8),FragmentColor0,fresnelFactor);
	//out_Color = vec4(fs_in.tangent,1.0);
	FragmentColor1 = vec4(0,0,0,1);

}
