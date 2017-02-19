#version 430 core

layout (vertices = 4) out;

in VS_OUT
{
	vec2 tc;
	vec3 normal;

}tcs_in[];

out TCS_OUT
{
	vec2 tc;
	vec3 normal;

}tcs_out[];


uniform float dmap_depth;
uniform mat4 mvp;
uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform float tessLevel;

void main(void){
	if(gl_InvocationID == 0){
		bool forceNoTess = true;
		vec4 p0 = projectionMatrix * viewMatrix * (transformationMatrix*gl_in[0].gl_Position);
		vec4 p1 = projectionMatrix * viewMatrix * (transformationMatrix*gl_in[1].gl_Position);
		vec4 p2 = projectionMatrix * viewMatrix * (transformationMatrix*gl_in[2].gl_Position);
		vec4 p3 = projectionMatrix * viewMatrix * (transformationMatrix*gl_in[3].gl_Position);
		p0 /= p0.w;
		p1 /= p1.w;
		p2 /= p2.w;
		p3 /= p3.w;


		if(p0.z <= 0.0 || p1.z <= 0.0 || p2.z <= 0.0 || p3.z <= 0.0){
			gl_TessLevelOuter[0] = 0.0;
			gl_TessLevelOuter[1] = 0.0;
			gl_TessLevelOuter[2] = 0.0;
			gl_TessLevelOuter[3] = 0.0;
		}
		else if(forceNoTess){
			gl_TessLevelOuter[0] = 1.0;
			gl_TessLevelOuter[1] = 1.0;
			gl_TessLevelOuter[2] = 1.0;
			gl_TessLevelOuter[3] = 1.0;

			gl_TessLevelInner[0] = 1.0;
			gl_TessLevelInner[1] = 1.0;
		}else
		{
			float t = tessLevel/2;
			float l0 = length(p2.xy - p0.xy) * t + 1.0;
			float l1 = length(p3.xy - p2.xy) * t + 1.0;
			float l2 = length(p3.xy - p1.xy) * t + 1.0;
			float l3 = length(p1.xy - p0.xy) * t + 1.0;

			gl_TessLevelOuter[0] = l0;
			gl_TessLevelOuter[1] = l1;
			gl_TessLevelOuter[2] = l2;
			gl_TessLevelOuter[3] = l3;

			gl_TessLevelInner[0] = min(l1,l3);
			gl_TessLevelInner[1] = min(l0,l2);
		}
	}
	gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;
	tcs_out[gl_InvocationID].normal   = tcs_in[gl_InvocationID].normal;
	tcs_out[gl_InvocationID].tc = tcs_in[gl_InvocationID].tc;
}
