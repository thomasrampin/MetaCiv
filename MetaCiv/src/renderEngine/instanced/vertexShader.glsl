#version 430 core


in vec3 position;

in mat4 modelViewMatrix;
in vec4 roadPos1;
in vec4 roadPos2;
in vec4 roadPos3;
in vec4 roadPos4;

uniform mat4 projectionMatrix;

void main(void){

	if(gl_VertexID == 1)
		gl_Position = projectionMatrix * (modelViewMatrix * roadPos1);
	else if(gl_VertexID == 2)
		gl_Position = projectionMatrix * (modelViewMatrix * roadPos2);
	else if(gl_VertexID == 3)
		gl_Position = projectionMatrix * (modelViewMatrix * roadPos3);
	else
		gl_Position = projectionMatrix * (modelViewMatrix * roadPos4);


}
