#version 420 core

out vec4 color;

void main(void)
{
    color = vec4(gl_FragCoord.z);
}
