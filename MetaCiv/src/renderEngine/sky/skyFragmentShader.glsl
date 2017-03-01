#version 430 core

in vec2 textureCoords;
in vec3 cameraVector;
in vec3 lightVector;
out vec4 out_Color;

uniform sampler2D diffuseMap;
uniform sampler2D alphaMap;
uniform vec4 fog_color = vec4(0.4f, 0.6f, 0.9f, 0f);
uniform float angle;
uniform float cloudDensity;

uniform vec3 sunPosition;

const vec4 Zenith = vec4(0.0952,0.154,0.35,1);
const vec4 Horizon = vec4(0.2,0.3,0.941,1);
const vec4 CloudColor = vec4(0.9,0.8,0.9,0.8);




void main(void){


	vec2 uv = textureCoords;
	vec3 vector = vec3(0,0,-1);
	float dott = clamp(0,1,pow(3,(1-clamp(0,1,dot(vector,normalize(cameraVector))))));


	vec3 light = vec3(-2,-1,1);
	 uv -= vec2(sunPosition.xy/15000);
	float dist =  sqrt(dot(uv, uv));

	//out_Color = mix(Zenith,Horizon,textureCoords.y*2-0.6)*1.2;
    out_Color += texture(diffuseMap, vec2(textureCoords.x+angle/6,textureCoords.y*1.8)) ;

    out_Color += CloudColor* max(texture(alphaMap, vec2(textureCoords.x+angle,textureCoords.y*1.8)).r - cloudDensity*2,0);

    //out_Color += mix(vec4(1,1,1,1),out_Color,textureCoords.y/30);
    out_Color = mix(Horizon,out_Color,textureCoords.y);
    //out_Color = mix(fog_color,out_Color,textureCoords.y);
   /* if ( (dist < (0.005)) )
    	 out_Color = vec4(1,0.8,0.4,1);*/
}
