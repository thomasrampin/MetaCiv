#version 330

in vec2 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform bool fix;
uniform vec2 translation;

void main(void) {

	if(fix)
		gl_Position = vec4(position + translation * vec2(2.0, -2.0), 0.0, 1.0);
	else
		gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(0.0, 0.0, 0.0, 1.0) +   vec4(position  , 0.0, 1.0);
	pass_textureCoords = textureCoords;

}
