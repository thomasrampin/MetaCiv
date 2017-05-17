#version 330 core

in vec3 position;
in vec2 tc;
out vec2 textureCoords;
out vec3 cameraVector;
out vec3 lightVector;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

uniform vec3 sunPosition;
uniform vec4 plane;


void main(void){
	
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	gl_ClipDistance[0] = dot(worldPosition,plane);
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	textureCoords = tc;
	cameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - (transformationMatrix * vec4(position, 1.0)).xyz;
	lightVector = sunPosition - (transformationMatrix * vec4(position, 1.0)).xyz;
}
