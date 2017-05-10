#version 330 core


in vec3 position;

in mat4 modelViewMatrix;

uniform mat4 projectionMatrix;

void main(void){


	gl_Position = projectionMatrix * (modelViewMatrix * vec4(position,1.0));


}
