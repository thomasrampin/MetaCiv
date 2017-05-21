#version 150

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;
uniform sampler2D blurTexture;
uniform sampler2D depthBuffer;

void main(void){
	float depthValue = (1-texture(depthBuffer,textureCoords).r)*1800;
	vec4 sceneColour;
	if(depthValue<0.99)
		sceneColour = mix(texture(blurTexture,textureCoords),texture(colourTexture,textureCoords),depthValue);
	else
		sceneColour = texture(colourTexture,textureCoords);

	out_Colour = sceneColour;
}
