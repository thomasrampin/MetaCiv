
#version 430
#define N_LIGHTS 4
#define N 3600

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[N_LIGHTS];
out vec3 toCameraVector;
out vec4 shadowCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[N_LIGHTS];



uniform mat4 toShadowMapSpace;


const float shadowDistance = 220.0;
const float transitionDistance = 80.0;

void main(void){






	vec4 worldPosition = transformationMatrix * vec4(position,1.0);

	shadowCoords = toShadowMapSpace * worldPosition;

	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	pass_textureCoords = textureCoords * 30.0;

	surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;

	for(int i=0; i<N_LIGHTS;i++){
		toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;

	//shadow glitch
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	float distance = length(positionRelativeToCam.xyz);
	distance -= (shadowDistance - transitionDistance);
	distance /= transitionDistance;
	shadowCoords.w = clamp(1.0-distance,0.0,1.0);

	
}
