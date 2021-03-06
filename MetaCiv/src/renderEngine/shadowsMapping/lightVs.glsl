#version 330 core

in vec3 position;

uniform mat4 mvp;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;



void main(void)
{
	vec4 worldPosition =  transformationMatrix *  vec4(position,1.0);

	vec4 Peye =  mvp *  worldPosition;


	gl_Position = Peye;
   // gl_Position = projectionMatrix  * viewMatrix * transformationMatrix * vec4(position,1.0);
}
