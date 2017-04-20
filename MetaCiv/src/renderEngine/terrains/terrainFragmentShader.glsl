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
uniform sampler2D blurMap;
uniform sampler2D shadowMap;

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




int getIndice(float red, float green, float blue);

int foundCloseOne(float red,float green,float blue){
	float redClose=heights[0].r,greenClose=heights[0].g,blueClose=heights[0].b;
	int indice = -1;
	for(int i=1;i<heights_size;i++){
		if(abs(redClose-red)>abs(heights[i].r - red) && abs(greenClose-green)>abs(heights[i].g - green) && abs(blueClose-blue)>abs(heights[i].b - blue)){
			redClose = heights[i].r;
			greenClose = heights[i].g;
			blueClose = heights[i].b;
			indice = i;
		}
	}
	return indice;
}

int getIndice(float red, float green, float blue){

	for(int i=0;i<heights_size;i++){
		if(heights[i].r == red && heights[i].g == green && heights[i].b == blue )
			return i;
	}
	return -1;
}

vec4 fog(vec4 c)
{
    float z = length(fs_in.eye_coord)/distanceFog;

    float de = 0.025 * smoothstep(0.0, 6.0, 10.0 - fs_in.world_coord.y);
    float di = 0.045 * (smoothstep(0.0, 20.0, 20.0 - fs_in.world_coord.y));

    float extinction   = exp(-z * de);
    float inscattering = exp(-z * di);

    return c * extinction + fog_color * (1.0 - inscattering);
}

float f(float x,float y){
	return x - y * floor(x/y);
}


vec2 ParallaxMapping(int indice, vec2 texCoords, vec3 viewDir)
{
	   // get depth for this fragment
	   float initialHeight = texture(gSampler[indice], texCoords).r;

	   // calculate amount of offset for Parallax Mapping
	   vec2 texCoordOffset = 0.1 * viewDir.xy / viewDir.z * initialHeight;

	   // calculate amount of offset for Parallax Mapping With Offset Limiting
	   texCoordOffset = 0.1 * viewDir.xy * initialHeight;

	   // retunr modified texture coordinates
	   return texCoords - texCoordOffset;
}

void main(void){


	vec4 landscape;

	float red = texture(blendMap,fs_in.tc).r;
	float blue = texture(blendMap,fs_in.tc).b;
	float green = texture(blendMap,fs_in.tc).g;
	int indice = -1;



	vec2 offset = vec2(0.006,0.006);
	for(int i=0;i<9;i++) // loop to fix ignore point
	{
		indice = getIndice(red,green,blue);
		if(indice == -1)
		{
			red = texture(blendMap,fs_in.tc-offset).r;
			blue = texture(blendMap,fs_in.tc-offset).b;
			green = texture(blendMap,fs_in.tc-offset).g;
			offset += offset;
		}
		else
			break;
	}

	int indice2 = -1;
	float red2 = texture(blendMap,fs_in.tc).r;
	float blue2 = texture(blendMap,fs_in.tc).b;
	float green2 = texture(blendMap,fs_in.tc).g;
	offset = vec2(0.006,0.006);
		for(int i=0;i<2;i++) // loop to fix ignore point
		{
			indice2 = getIndice(red2,green2,blue2);
			if(indice2 == -1 || (red == red && green == green2 && blue == blue2))
			{
				red2 = texture(blendMap,fs_in.tc-offset).r;
				blue2 = texture(blendMap,fs_in.tc-offset).b;
				green2 = texture(blendMap,fs_in.tc-offset).g;
				offset += offset;
			}
			else
				break;
		}



	vec3 viewDir = normalize(fs_in.TangentViewPos - fs_in.TangentFragPos);

	vec3 unitVectorToCamera = normalize(fs_in.toCameraVector);


	float x = f(fs_in.tc.x*15,0.5);
	vec2 parseTc = vec2(x,fs_in.tc.y*15 );


	vec2 parseTcNrm = vec2(x,fs_in.tc.y*15 )/2.0+(0.5);


	vec2 parseTcDisp = vec2(x,fs_in.tc.y*15 )/2.0+(0.666);


	vec2 texCoords = ParallaxMapping(indice,parseTcDisp,fs_in.viewDir);



	vec3 unitNormal = normalize((texture(gSampler[indice],parseTcNrm).rbg * 2.0 - 1.0) );


	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	vec3 lightDir = normalize(fs_in.TangentLightPos - fs_in.TangentFragPos);
	vec3 unitLightVector = normalize(fs_in.toLightVector);

	float nDotl = dot(unitNormal,unitLightVector);
	float nDotl2 = dot(normalize(fs_in.normal),unitLightVector);
	float brightness = max(nDotl,0.0);
	float brightness2 = max(nDotl2,0.0);

	vec3 lightDirection = -unitLightVector;





	totalDiffuse = brightness * brightness2 * lightColour;


	vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);


	vec3 specular = max(pow(dot(reflectedLightDirection,unitVectorToCamera),shineDamper),0.0) * reflectivity;

	// Brighter
	totalDiffuse = totalDiffuse * diffuseColour;

	vec2 blurTextureCoords[11];
	for(int i=-5;i<5;i++){
			blurTextureCoords[i+5] = parseTc + vec2(0.0,0.006 * i);

	}


	landscape = textureProj(shadowMap, fs_in.shadow_coord)* texture(gSampler[indice],parseTc);







	landscape *= vec4(totalDiffuse,1.0)  + vec4(specular,1.0);

	FragmentColor0 = fog(landscape);

	//out_Color = vec4(fs_in.tangent,1.0);
	FragmentColor1 = vec4(0,0,0,1);

}
