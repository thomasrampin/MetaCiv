#version 330 core
#define N_LIGHTS 4

in VS_OUT{
	vec2 pass_textureCoords;
	vec3 surfaceNormal;
	vec3 toLightVector;
	vec3 toLightVectorTangent;
	vec3 toCameraVector;
	vec3 toCameraVectorTangent;
	vec3 tangent;
	vec3 tangentViewPos;
	vec3 tangentFragPos;
	vec3 TangentLightPos;
	vec3 TangentViewPos;
	vec3 TangentFragPos;
	mat3 RM;
	mat3 TBN;
	vec3 eye_coord;
	vec3 world_coord;
}fs_in;

layout (location = 0) out vec4 FragmentColor0;
layout (location = 1) out vec4 FragmentColor1;



uniform sampler2D diffuseMap;
uniform sampler2D normalMap;
uniform sampler2D dispMap;
uniform sampler2D shadowMap;
uniform sampler2D roughnessMap;
uniform sampler2D metalMap;
uniform sampler2D reflexion_map;

uniform vec3 lightColour;
uniform float shineDamper;
uniform vec3 reflectivity;
uniform vec3 diffuseColour;
uniform bool textured;
uniform bool normalMapped;
uniform bool dispMapped;
uniform bool reciveShadow;
uniform bool reflMapped;
uniform bool metalMapped;

uniform vec4 fog_color = vec4(0.4, 0.6, 0.9, 0);
uniform float distanceFog;

const vec4 CloudColor = vec4(0.9,0.8,0.9,0.8);
const vec4 Horizon = vec4(0.2,0.3,0.941,1);
uniform vec4 colorID;
uniform vec3 colorAction;
uniform float skyAngle;
uniform float height_scale;

//size reflect image
const float size_level1 = 1024.0;

vec4 fog(vec4 c)
{
    float z = length(fs_in.eye_coord)/distanceFog;

    float de = 0.025 * smoothstep(0.0, 6.0, 10.0 - fs_in.world_coord.y);
    float di = 0.045 * (smoothstep(0.0, 20.0, 20.0 - fs_in.world_coord.y));

    float extinction   = exp(-z * de);
    float inscattering = exp(-z * di);

    return c * extinction + fog_color * (1.0 - inscattering);
}





void main(void){

	vec3 toLightVectorFinal;
	if(normalMapped){
		vec3 lightDir = normalize(fs_in.TangentLightPos - fs_in.TangentFragPos);
		toLightVectorFinal = lightDir;
	}else{
		toLightVectorFinal = fs_in.toLightVector;
	}


	vec3 viewDir = normalize(fs_in.TangentViewPos - fs_in.TangentFragPos);

	vec2 texCoords = fs_in.pass_textureCoords;

	/*if(dispMapped)
		texCoords = ParallaxMapping(fs_in.pass_textureCoords,viewDir);*/

	vec3 unitNormal = normalize(fs_in.surfaceNormal);
	vec3 toCameraVectorFinal = fs_in.toCameraVector;
	if(normalMapped){
		vec4 normalValue = 2.0 * texture(normalMap,texCoords) - 1.0;

		toCameraVectorFinal = viewDir;
		unitNormal = normalize(normalValue.rgb);
	}
	vec3 unitVectorToCamera = normalize(toCameraVectorFinal);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	

	vec3 unitLightVector = normalize(toLightVectorFinal);

	float nDotl = dot(unitNormal,unitLightVector);
	float brightness = max(nDotl,0.0);

	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);

	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor,0.0);
	float dampedFactor = pow(specularFactor,shineDamper);

	totalSpecular += dampedFactor * reflectivity * lightColour;
	totalDiffuse += brightness * lightColour;


	// Brighter	
	totalDiffuse = max(totalDiffuse, 0.4) ;
	


	//Check of texture is enable
	if(textured){
		vec4 textureColour = texture(diffuseMap,texCoords);
		if(textureColour.a<0.5) // enable transparency
			discard;
		FragmentColor0 = vec4(totalDiffuse,1.0) * textureColour + vec4(totalSpecular,1.0);
	}
	else
		FragmentColor0 = vec4(totalDiffuse,1.0) * vec4(diffuseColour,1.0) + vec4(totalSpecular,1.0);


	if(reflMapped){
		vec4 gloss_color;
		float gloss = texture(roughnessMap,texCoords).r;
		float fresnelFactor = max(1-dot(unitVectorToCamera,unitNormal),0.8);
		vec3 r = reflect(normalize(fs_in.toCameraVector),normalize(fs_in.RM * (unitNormal * fs_in.TBN)));
		//Compute texture coordinate based on direction
		vec2 tc;

		tc.y = r.y;
		r.y = 0.0;
		tc.x = normalize(r).x * 0.5;

		float s = sign(r.z) * 0.5;
		tc.s = 0.75 - s * (0.5 - tc.s);
		tc.t = 0.5 +0.5 * tc.t;
		gloss_color = vec4(0,0,0,1.0);

		float lod = (5.0 + 5.0*sin( gloss ))*step( tc.x, tc.y );
		gloss_color = textureLod(reflexion_map, tc,gloss*10*2.5) ;

		vec4 final_gloss = FragmentColor0 * gloss_color;
		if(metalMapped)
			FragmentColor0 = mix(FragmentColor0,final_gloss,(fresnelFactor)*texture(metalMap,texCoords).r);

		//FragmentColor0 += texture(reflexion_blur,tc);
		//FragmentColor0 += mix(texture(reflexion,  vec2(tc.x+skyAngle/6,tc.y)),texture(reflexion_blur, vec2(tc.x+skyAngle/6,tc.y)),0.0);
		//FragmentColor0 += CloudColor* max(texture(reflexion_blur, vec2(tc.x+skyAngle,tc.y)).r - 0.01*2,0)*gloss;
		//FragmentColor0 = mix(Horizon,FragmentColor0,tc.y);
	}
	if(colorAction.r != -1.0)
		FragmentColor0 *= vec4(colorAction,1.0);

	FragmentColor0 = fog(FragmentColor0);
	FragmentColor1 = colorID;

}
