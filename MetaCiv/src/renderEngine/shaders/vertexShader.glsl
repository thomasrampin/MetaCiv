#version 430 core
#define N_LIGHTS 4

in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec3 tangents;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[N_LIGHTS];
out vec3 toLightVectorTangent[N_LIGHTS];
out vec3 toCameraVector;
out vec3 toCameraVectorTangent;
out vec3 tangent;
out vec3 tangentViewPos;
out vec3 tangentFragPos;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[N_LIGHTS];
uniform vec3 viewPos;

uniform mat4 toShadowMapSpace;


const float shadowDistance = 220.0;
const float transitionDistance = 80.0;

void main(void){
	surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;
	vec4 worldPosition = transformationMatrix * vec4(position,1.0);
	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize(vec3(  (viewMatrix) * transformationMatrix * vec4(tangents,0.0)));
	vec3 bitang = normalize(cross(norm,tang));

	mat3 toTangentSpace = mat3(
		tang.x, bitang.x, norm.x,
		tang.y, bitang.y, norm.y,
		tang.z, bitang.z, norm.z
	);




	
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	pass_textureCoords = textureCoords;

	

	for(int i=0; i<N_LIGHTS;i++){
		toLightVector[i] =  (lightPosition[i] - worldPosition.xyz);
		toLightVectorTangent[i] =  toTangentSpace * (lightPosition[i] - worldPosition.xyz);
	}

	toCameraVector = ((inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz);
	toCameraVectorTangent = toTangentSpace * ((inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz);

	 norm = normalize(surfaceNormal);
	 tang = normalize(vec3(  (viewMatrix) * transformationMatrix * vec4(tangents,0.0)));
	 bitang = normalize(cross(norm,tang));

	 toTangentSpace = mat3(
		tang.x, bitang.x, norm.x,
		tang.y, bitang.y, norm.y,
		tang.z, bitang.z, norm.z
	);

	tangent = tangents;
	tangentViewPos = toTangentSpace * (viewPos - worldPosition.xyz);

	tangentFragPos = toTangentSpace * worldPosition.xyz;
}
