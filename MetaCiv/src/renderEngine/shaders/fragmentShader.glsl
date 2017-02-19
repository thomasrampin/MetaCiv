#version 430 core
#define N_LIGHTS 4

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[N_LIGHTS];
in vec3 toCameraVector;
in vec4 shadowCoords;

out vec4 out_Color;

uniform sampler2D diffuseMap;
uniform sampler2D shadowMap;

uniform vec3 lightColour[N_LIGHTS];
uniform float shineDamper;
uniform vec3 reflectivity;
uniform vec3 diffuseColour;
uniform bool textured;
uniform bool reciveShadow;

const float shadowOpacity = 0.4;

void main(void){

	// make some magic stuff about shadow
	float objectNearestLight = texture(shadowMap, shadowCoords.xy).r;
	float lightFactor = 1.0;
	if(shadowCoords.z > objectNearestLight && reciveShadow)
		lightFactor -= (shadowCoords.w * shadowOpacity);

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i=0;i<N_LIGHTS;i++){
		vec3 unitLightVector = normalize(toLightVector[i]);
		
		float nDotl = dot(unitNormal,unitLightVector);
		float brightness = max(nDotl,0.0);
		
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor,0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		
		totalSpecular += dampedFactor * reflectivity * lightColour[i] * lightFactor;
		totalDiffuse += brightness * lightColour[i];
	}

	// Brighter	
	totalDiffuse = max(totalDiffuse, 0.2) * diffuseColour * lightFactor;
	

	
	//Check of texture is enable
	if(textured){
		vec4 textureColour = texture(diffuseMap,pass_textureCoords);
		if(textureColour.a<0.5) // enable transparency
			discard;
		out_Color = vec4(totalDiffuse,1.0) * textureColour + vec4(totalSpecular,1.0);
	}
	else
		out_Color = vec4(totalDiffuse,1.0) + vec4(totalSpecular,1.0);

}
