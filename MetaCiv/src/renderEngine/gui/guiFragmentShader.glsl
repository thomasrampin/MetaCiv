#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform vec3 color;
uniform bool isTextured;
uniform bool isHover;

const vec4 hoverColor = vec4(0.3,0.4,0.8,1.0);

void main(void){


	out_Color = vec4(color,1.0);
	if(isTextured)
		out_Color = texture(guiTexture,textureCoords);
	if(isHover)
		out_Color = mix(out_Color,hoverColor,0.5);

}
