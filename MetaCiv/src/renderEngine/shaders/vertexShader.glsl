#version 330 core
#define N_LIGHTS 4

in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec3 tangents;

out VS_OUT{
	vec2 pass_textureCoords;
	vec3 surfaceNormal;
	vec3 toLightVector;
	vec3 toLightVectorTangent;
	vec3 toCameraVector;
	vec3 toCameraVectorTangent;
	vec3 tangent;
	vec3 tangentViewPos;
	vec3 tangentFragPos;
	vec3 TangentLightPos;
	vec3 TangentViewPos;
	vec3 TangentFragPos;
	mat3 RM;
	mat3 TBN;
}vs_out;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform vec3 viewPos;




void main(void){
	vs_out.surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;
	vec4 worldPosition = transformationMatrix * vec4(position,1.0);



    mat3 normalMatrix = transpose(inverse(mat3(transformationMatrix)));
    vec3 T = normalize(normalMatrix * tangents);
    vec3 N = normalize(normalMatrix * normal);
    vec3 B = normalize(cross(N,T));
    vs_out.TBN = transpose(mat3(T, B, N));

    vs_out.TangentLightPos =  vs_out.TBN * lightPosition;
    vs_out.TangentViewPos  =  vs_out.TBN * viewPos;
    vs_out.TangentFragPos  =  vs_out.TBN * worldPosition.xyz;


	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	vs_out.pass_textureCoords = textureCoords;


	vs_out.RM = mat3( transformationMatrix[0].xyz,
			transformationMatrix[1].xyz,
			transformationMatrix[2].xyz );

	vs_out.toLightVector =  (lightPosition - worldPosition.xyz);
	vs_out.toCameraVector = ((inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz);




	vs_out.tangent = tangents;

}
