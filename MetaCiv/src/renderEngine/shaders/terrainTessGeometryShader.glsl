#version 430 core

layout(triangles) in;
layout(line_strip, max_vertices = 3) out;

uniform mat4 ModelView;

in TES_OUT
{
	vec2 tc;
	vec3 normal;
}fs_in[];

out GEO_OUT
{
	vec3 gFacetNormal;
	vec2 gTexCoord;
}geo_out;


void main() {



    for (int i = 0; i < gl_in.length(); i++)
    {
        geo_out.gFacetNormal = fs_in[i].normal;
        geo_out.gTexCoord = fs_in[i].tc;
        gl_Position = gl_in[i].gl_Position;
        EmitVertex();
    }

    EndPrimitive();
}
