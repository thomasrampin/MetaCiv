#version 430 core


layout (quads, fractional_odd_spacing) in;



in TCS_OUT
{
	vec2 tc;
	vec3 normal;

}tes_in[];

out TES_OUT
{
	vec2 tc;
	vec3 world_coord;
	vec3 toLightVector;
	vec3 toCameraVector;
	vec3 eye_coord;
}tess_out;

layout (binding = 0) uniform sampler2D blendMap;
layout (binding = 1) uniform sampler2D tex_displacement;
layout (binding = 4) uniform sampler2D tex_displacement_sub;

uniform mat4 mvp;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform float dmap_depth;

uniform vec4 plane;


void main(void){
	vec2 tc1 = mix(tes_in[0].tc, tes_in[1].tc, gl_TessCoord.x);
	vec2 tc2 = mix(tes_in[2].tc, tes_in[3].tc, gl_TessCoord.x);
	vec2 tc = mix(tc2, tc1, gl_TessCoord.y);

	vec4 p1 = mix(gl_in[0].gl_Position,gl_in[1].gl_Position,gl_TessCoord.x);
	vec4 p2 = mix(gl_in[2].gl_Position,gl_in[3].gl_Position,gl_TessCoord.x);

	vec4 p = mix(p2,p1,gl_TessCoord.y);

	p.y += texture(tex_displacement,tc*2.0).r * 1.0;
	p.y += (texture(tex_displacement_sub,tc*50.0).r+texture(tex_displacement_sub,tc*50.0).g+texture(tex_displacement_sub,tc*50.0).b) / 100.0;

	/*float red = texture(blendMap,tc).r;
	float blue = texture(blendMap,tc).b;
	float green = texture(blendMap,tc).g;
	int indice = -1;

	vec2 offset = vec2(0.0,0.006);
	for(int i=0;i<9;i++) // loop to fix ignore point
	{
		indice = getIndice(red,green,blue);
		if(indice == -1)
		{
			red = texture(blendMap,tc-offset).r;
			blue = texture(blendMap,tc-offset).b;
			green = texture(blendMap,tc-offset).g;
			offset += offset;
		}
		else
			break;
	}

	p.y += texture(gSampler[0], tc*50.0).r/50.0;*/

	vec4 worldPos = transformationMatrix * p;

	gl_ClipDistance[0] = dot(worldPos,plane);

	gl_Position = projectionMatrix * viewMatrix * worldPos;

	tess_out.toCameraVector = vec3(inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)) -  worldPos.xyz;

	vec4 P_eye = projectionMatrix * viewMatrix * worldPos;

	tess_out.world_coord =  vec3(projectionMatrix) * vec3(viewMatrix) * worldPos.xyz;
	tess_out.eye_coord = P_eye.xyz;

	tess_out.toLightVector = lightPosition - worldPos.xyz;
	tess_out.tc = tc;

}
