#version 330





out VS_OUT{
	vec2 tc;
	float height;
	vec3 world_coord;
	vec3 fromLightVector;
	vec3 toLightVector;
	vec3 toCameraVector;
	vec3 eye_coord;
	vec4 clipSpace;
}vs_out;



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
void main(void) {

	const vec4 vertices[] = vec4[](vec4(0,0.0,0,1.0),
										vec4(64,0.0,0,1.0),
										vec4(64,0.0,64,1.0),
										vec4(0,0.0,64,1.0));





		vs_out.tc = vec2(vertices[gl_VertexID].x/2.0 + 0.5, vertices[gl_VertexID].z/2.0 + 0.5);



		vec4 worldPosition =  vertices[gl_VertexID];
		//vs_out.toLightVector = lightPosition -  worldPosition.xyz;
		vec4 p = worldPosition;

		float height = p.y-1.0;



		vs_out.height = height;



		vec4 worldPos = modelMatrix * p;
		vs_out.world_coord =  vec3(projectionMatrix) * vec3(viewMatrix) * worldPos.xyz;
		vs_out.clipSpace = projectionMatrix * viewMatrix * worldPos;
		gl_Position =   vs_out.clipSpace;




		vs_out.toCameraVector = cameraPosition- worldPos.xyz;

		vec4 P_eye = projectionMatrix * viewMatrix * worldPos;


		vs_out.eye_coord = P_eye.xyz;
		vs_out.toLightVector = lightPosition - worldPos.xyz;

		vs_out.fromLightVector =  worldPos.xyz - lightPosition;

}
