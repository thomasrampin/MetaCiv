#version 430 core

#define MAX_TERRAIN_TYPE 11

out VS_OUT{
	vec2 tc;
	vec3 normal;

}vs_out;



layout (binding =0) uniform sampler2D blendMap;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform float dmap_depth;

uniform vec4 heights[MAX_TERRAIN_TYPE];
uniform int heights_size;

uniform vec4 plane;

float getHeight(float red, float green, float blue);



float getHeight(float red, float green, float blue){

	for(int i=0;i<heights_size;i++){
		if(heights[i].r == red && heights[i].g == green && heights[i].b == blue )
			return heights[i].a;
	}
	return -1.0;
}


void main(void){



	const vec4 vertices[] = vec4[](vec4(-0.5,-1.0,-0.5,1.0),
									vec4(0.5,-1.0,-0.5,1.0),
									vec4(-0.5,-1.0,0.5,1.0),
									vec4(0.5,-1.0,0.5,1.0));
	int x = gl_InstanceID & 63;
	int y = gl_InstanceID >> 6;
	vec2 offs = vec2(x,y);




	vs_out.tc = (vertices[gl_VertexID].xz + offs + vec2(0.5)) / 64.0;


	float red = texture(blendMap,vs_out.tc).r;
	float blue = texture(blendMap,vs_out.tc).b;
	float green = texture(blendMap,vs_out.tc).g;
	float height = -1.0;
	vec2 offset = vec2(0.0,0.006);
	for(int i=0;i<9;i++) // loop to fix ignore point
	{
		height = getHeight(red,green,blue);
		if(height == -1)
		{
			red = texture(blendMap,vs_out.tc-offset).r;
			blue = texture(blendMap,vs_out.tc-offset).b;
			green = texture(blendMap,vs_out.tc-offset).g;
			offset += offset;
		}
		else
			break;
	}

	/*float heightL = getHeight(texture(tex_displacement,vs_out.tc+vec2(0.5,0.0)).r,texture(tex_displacement,vs_out.tc+vec2(0.5,0.0)).g,texture(tex_displacement,vs_out.tc+vec2(0.5,0.0)).b);

	float heightR = getHeight(texture(tex_displacement,vs_out.tc+vec2(-0.5,0.0)).r,texture(tex_displacement,vs_out.tc+vec2(-0.5,0.0)).g,texture(tex_displacement,vs_out.tc+vec2(-0.5,0.0)).b);

	float heightD = getHeight(texture(tex_displacement,vs_out.tc+vec2(0.0,-0.5)).r,texture(tex_displacement,vs_out.tc+vec2(0.0,-0.5)).g,texture(tex_displacement,vs_out.tc+vec2(0.0,-0.5)).b);

	float heightU = getHeight(texture(tex_displacement,vs_out.tc+vec2(0.0,0.5)).r,texture(tex_displacement,vs_out.tc+vec2(0.0,0.5)).g,texture(tex_displacement,vs_out.tc+vec2(0.0,0.5)).b);

	vs_out.normal = vec3(heightL - heightR, 2f  , heightD - heightU);*/

	/*vec3 v1 = vertices[gl_VertexID-1].xyz - vertices[gl_VertexID].xyz;
	vec3 v2 = vertices[gl_VertexID+1].xyz - vertices[gl_VertexID].xyz;*/
	//vs_out.normal = cross(v1,v2);

	vec4 worldPosition =  vertices[gl_VertexID] + vec4(float(x), height * dmap_depth,float(y),0.0);
	//vs_out.toLightVector = lightPosition -  worldPosition.xyz;



	gl_Position =  worldPosition;
}
