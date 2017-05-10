#version 330 core
#define N_LIGHTS 4



layout (location = 0) out vec4 FragmentColor0;
layout (location = 1) out vec4 FragmentColor1;




void main(void){

	
	FragmentColor0 = vec4(0.5,0.5,0.5,1.0);

	FragmentColor1 = vec4(0);

}
