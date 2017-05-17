#version 410 core



layout (quads, fractional_odd_spacing) in;

uniform sampler2D dudvMap;
uniform sampler2D dispMap;
uniform sampler2D normalMap;

uniform float moveFactor;


uniform float waveStrenght;

//uniform mat4 mvp;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPosition;
//uniform float dmap_depth;

in TCS_OUT
{
	vec2 tc;

}tes_in[];

out TES_OUT
{
	vec2 tc;
	float height;
	vec3 world_coord;
	vec3 fromLightVector;
	vec3 toLightVector;
	vec3 toCameraVector;
	vec3 eye_coord;
	vec4 clipSpace;
}tess_out;

void main(void){
	vec2 tc1 = mix(tes_in[0].tc, tes_in[1].tc, gl_TessCoord.x);
	vec2 tc2 = mix(tes_in[2].tc, tes_in[3].tc, gl_TessCoord.x);
	vec2 tc = mix(tc2, tc1, gl_TessCoord.y);

	vec4 p1 = mix(gl_in[0].gl_Position,gl_in[1].gl_Position,gl_TessCoord.x);
	vec4 p2 = mix(gl_in[2].gl_Position,gl_in[3].gl_Position,gl_TessCoord.x);

	vec4 p = mix(p2,p1,gl_TessCoord.y);

	vec2 modifyTexCoords = tc;

	vec2 distortion1 = vec2(tc.x*5.0 + moveFactor,tc.y*5.0 + moveFactor*2.0);
	vec2 distortion2 = vec2(tc.x*5.0,tc.y*5.0 - moveFactor*2.0);
	//distortion1 += (texture(dudvMap, vec2(tc.x + moveFactor,tc.y + moveFactor)).rg * 2.0 -1.0) * waveStrenght;
	modifyTexCoords += distortion1;



	p.y += (texture(dispMap, modifyTexCoords).r)*waveStrenght*2.0 ;
	p.y += (texture(dispMap, distortion2).r)*waveStrenght*2.0 ;

	distortion1 = (texture(dudvMap, vec2(tc.x*10.0 + moveFactor * 2.0,tc.y*10.0)).rg * 2.0 -1.0) * waveStrenght;
	distortion1 += (texture(dudvMap, vec2(tc.x*10.0 + moveFactor* 2.0,tc.y*10.0 + moveFactor* 2.0)).rg * 2.0 -1.0) * waveStrenght;

	vec2 distortion3 = vec2(tc.x*5 + moveFactor ,tc.y*5.0 - moveFactor );

	p.y += (texture(dispMap, distortion1).r)/30.0;
	p.y += (texture(dispMap, distortion3).r)/30.0;

	float height = p.y-1.0;
	height += (texture(dispMap, modifyTexCoords).r)*waveStrenght*5.0 ;
	height += (texture(dispMap, distortion2).r)*waveStrenght*5.0 ;
	height += (texture(dispMap, distortion1).r)/5.0;
	height += (texture(dispMap, distortion3).r)/5.0;


	tess_out.height = height;



	vec4 worldPos = modelMatrix * p;
	tess_out.world_coord =  vec3(projectionMatrix) * vec3(viewMatrix) * worldPos.xyz;
	tess_out.clipSpace = projectionMatrix * viewMatrix * modelMatrix * p;
	gl_Position =   projectionMatrix * viewMatrix * worldPos;




	tess_out.toCameraVector = cameraPosition- worldPos.xyz;

	vec4 P_eye = projectionMatrix * viewMatrix * worldPos;


	tess_out.eye_coord = P_eye.xyz;
	tess_out.toLightVector = lightPosition - worldPos.xyz;

	tess_out.fromLightVector =  worldPos.xyz - lightPosition;
	tess_out.tc = tc;

}
