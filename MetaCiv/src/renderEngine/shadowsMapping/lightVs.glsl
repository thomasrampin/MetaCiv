#version 420 core

uniform mat4 mvp;
uniform mat4 projectionMatrix;

layout (location = 0) in vec3 position;

void main(void)
{
    gl_Position = mvp * projectionMatrix * vec4(position,1.0);
}
