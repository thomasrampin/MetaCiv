#version 150

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;
uniform sampler2D highlightTexture;

void main(void){

	vec4 scneColour = texture(colourTexture,textureCoords);
	vec4 hightlightColour = texture(highlightTexture,textureCoords);
	out_Colour = scneColour + hightlightColour * 2.0; 
}