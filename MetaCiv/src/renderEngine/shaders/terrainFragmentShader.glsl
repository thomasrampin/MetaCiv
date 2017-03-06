#version 430 core


#define GL_ARB_bindless_texture_is_supported 0


// Enable bindless textures
#if GL_ARB_bindless_texture_is_supported
	#extension GL_ARB_bindless_texture : require
#endif

#define MAX_TERRAIN_TYPE 11

out vec4 color;



uniform sampler2D blendMap;
uniform sampler2D blurMap;

#if GL_ARB_bindless_texture_is_supported
	layout(binding = 5, std140) uniform TEXTURE_BLOCK
	{
		sampler2D gSampler[MAX_TERRAIN_TYPE];
	};
#else
	uniform sampler2D gSampler[MAX_TERRAIN_TYPE];
#endif

uniform vec4 fog_color = vec4(0.4f, 0.6f, 0.9f, 0f);

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

vec4 blend(vec4 color1, vec4 color2, float alpha){
	return color1*(1.0-alpha)+b*t;
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

	/*int indice2 =-1;
	red = texture(blendMap,fs_in.tc).r;
	blue = texture(blendMap,fs_in.tc).b;
	green = texture(blendMap,fs_in.tc).g;
	vec2 offset2 = vec2(0.01,0.00);
	int i;
	for( i=0;i<9;i++) // loop to fix ignore point
	{
		indice2 = getIndice(red,green,blue);
		if(indice2 == -1 || indice==indice2)
		{
			red = texture(blendMap,fs_in.tc-offset2).r;
			blue = texture(blendMap,fs_in.tc-offset2).b;
			green = texture(blendMap,fs_in.tc-offset2).g;
			offset2 += offset2;
		}
		else
			break;
	}*/




	vec3 unitVectorToCamera = normalize(fs_in.toCameraVector);


	float x = f(fs_in.tc.x*40,0.5);
	vec2 parseTc = vec2(x,fs_in.tc.y*40 );


	vec2 parseTcNrm = vec2(x,fs_in.tc.y*40 )/2.0+(0.5);


	vec2 parseTcDisp = vec2(x,fs_in.tc.y*40 )/2.0+(0.666);


	vec2 texCoords = ParallaxMapping(indice,parseTcDisp,fs_in.viewDir);



	vec3 unitNormal2 = normalize((texture(gSampler[indice],parseTcNrm).rbg * 2.0) - vec3(1.0));
	vec3 unitNormal = normalize(fs_in.normal);

	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);


	vec3 unitLightVector = normalize(fs_in.toLightVector);

	float nDotl = dot(unitNormal,unitLightVector);
	float brightness = max(nDotl,0.0);

	vec3 lightDirection = -unitLightVector;





	totalDiffuse = brightness * lightColour;


	vec3 reflectedLightDirection = reflect(lightDirection,unitNormal2);


	vec3 specular = max(pow(dot(reflectedLightDirection,unitVectorToCamera),shineDamper),0.0) * reflectivity;

	// Brighter
	totalDiffuse = totalDiffuse * diffuseColour;

	vec2 blurTextureCoords[11];
	for(int i=-5;i<5;i++){
			blurTextureCoords[i+5] = parseTc + vec2(0.0,0.006 * i);

	}

	/*if(indice2!=-1 )
		landscape = mix(texture(gSampler[indice2],parseTc),texture(gSampler[indice], parseTc),i/9.0);

	else*/
		landscape = texture(gSampler[indice],parseTc);






	landscape *= vec4(totalDiffuse,1.0)  + vec4(specular,1.0);

	out_Color = fog(landscape);
	//out_Color = vec4(fs_in.tangent,1.0);


}
