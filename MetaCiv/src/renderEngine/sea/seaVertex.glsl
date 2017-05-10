#version 430





out VS_OUT{
	vec2 tc;

}vs_out;



uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 lightPosition;
uniform vec3 cameraPosition;

void main(void) {

	const vec4 vertices[] = vec4[](vec4(-0.5,0.0,-0.5,1.0),
										vec4(0.5,0.0,-0.5,1.0),
										vec4(-0.5,0.0,0.5,1.0),
										vec4(0.5,0.0,0.5,1.0));
		int x = gl_InstanceID & 63;
		int y = gl_InstanceID >> 6;
		vec2 offs = vec2(x,y);




		vs_out.tc = (vertices[gl_VertexID].xz + offs + vec2(0.5)) / 64.0;



		vec4 worldPosition =  vertices[gl_VertexID] + vec4(float(x), 0.0,float(y),0.0);
		//vs_out.toLightVector = lightPosition -  worldPosition.xyz;


		gl_Position =  worldPosition;


}
