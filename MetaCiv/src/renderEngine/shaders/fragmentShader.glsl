#version 430 core
#define N_LIGHTS 4

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[N_LIGHTS];
in vec3 toLightVectorTangent[N_LIGHTS];
in vec3 toCameraVector;
in vec3 toCameraVectorTangent;

in vec3 tangent;
in vec3 tangentViewPos;
in vec3 tangentFragPos;

layout (location = 0) out vec4 FragmentColor0;
layout (location = 1) out vec4 FragmentColor1;



uniform sampler2D diffuseMap;
uniform sampler2D normalMap;
uniform sampler2D dispMap;
uniform sampler2D shadowMap;

uniform vec3 lightColour[N_LIGHTS];
uniform float shineDamper;
uniform vec3 reflectivity;
uniform vec3 diffuseColour;
uniform bool textured;
uniform bool normalMapped;
uniform bool dispMapped;
uniform bool reciveShadow;

const float shadowOpacity = 0.4;

uniform vec4 colorID;

uniform float height_scale = 0.05;

vec2 ParallaxMapping(vec2 texCoords, vec3 viewDir)
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
    float currentDepthMapValue = texture(dispMap, currentTexCoords).r;

    while(currentLayerDepth < currentDepthMapValue)
    {
        // shift texture coordinates along direction of P
        currentTexCoords -= deltaTexCoords;
        // get depthmap value at current texture coordinates
        currentDepthMapValue = texture(dispMap, currentTexCoords).r;
        // get depth of next layer
        currentLayerDepth += layerDepth;
    }

    // -- parallax occlusion mapping interpolation from here on
    // get texture coordinates before collision (reverse operations)
    vec2 prevTexCoords = currentTexCoords + deltaTexCoords;

    // get depth after and before collision for linear interpolation
    float afterDepth  = currentDepthMapValue - currentLayerDepth;
    float beforeDepth = texture(dispMap, prevTexCoords).r - currentLayerDepth + layerDepth;

    // interpolation of texture coordinates
    float weight = afterDepth / (afterDepth - beforeDepth);
    vec2 finalTexCoords = prevTexCoords * weight + currentTexCoords * (1.0 - weight);

    return finalTexCoords;
}

void main(void){

	vec3 toLightVectorFinal[N_LIGHTS];
	for(int i=0;i<N_LIGHTS;i++){
		if(normalMapped){
			toLightVectorFinal[i] = toLightVectorTangent[i];
		}else{
			toLightVectorFinal[i] = toLightVector[i];
		}
	}

	vec3 viewDir = normalize(tangentViewPos);

	vec2 texCoords = pass_textureCoords;

	if(dispMapped)
		texCoords = ParallaxMapping(pass_textureCoords,viewDir);

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 toCameraVectorFinal = toCameraVector;
	if(normalMapped){
		vec4 normalValue = 2.0 * texture(normalMap,texCoords) - 1.0;
		toCameraVectorFinal = toCameraVectorTangent;
		unitNormal = normalize(normalValue.rgb);
	}
	vec3 unitVectorToCamera = normalize(toCameraVectorFinal);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i=0;i<N_LIGHTS;i++){
		vec3 unitLightVector = normalize(toLightVectorFinal[i]);
		
		float nDotl = dot(unitNormal,unitLightVector);
		float brightness = max(nDotl,0.0);
		
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor,0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		
		totalSpecular += dampedFactor * reflectivity * lightColour[i];
		totalDiffuse += brightness * lightColour[i];
	}

	// Brighter	
	totalDiffuse = max(totalDiffuse, 0.2) * diffuseColour ;
	

	
	//Check of texture is enable
	if(textured){
		vec4 textureColour = texture(diffuseMap,texCoords);
		if(textureColour.a<0.5) // enable transparency
			discard;
		FragmentColor0 = vec4(totalDiffuse,1.0) * textureColour + vec4(totalSpecular,1.0);
	}
	else
		FragmentColor0 = vec4(totalDiffuse,1.0) + vec4(totalSpecular,1.0);

	FragmentColor1 = colorID;

}
