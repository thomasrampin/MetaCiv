#version 430 core



in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec3 tangent;


out VS_OUT
{
	vec2 tc;
	vec3 world_coord;
	vec3 toLightVector;
	vec3 toCameraVector;
	vec3 eye_coord;
	vec3 normal;
	vec3 tangent;
	vec3 viewDir;
	vec4 shadow_coord;
	vec3 TangentLightPos;
	vec3 TangentViewPos;
	vec3 TangentFragPos;
	float height;
	mat3 RM;
	mat3 TBN;
	vec3 Pos;
}vs_out;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform vec3 viewPos;
uniform mat4 shadow_matrix;

const mat4 scale_bias_matrix = mat4(0.5, 0.0, 0.0, 0.0,
                                    0.0, 0.5, 0.0, 0.0,
                                    0.0, 0.0, 0.5, 0.0,
									0.5, 0.5, 0.5, 1.0);

void main(void){


	vs_out.tangent = tangent;
	vec4 plane=vec4(vec3(vec3(projectionMatrix) * vec3(viewMatrix)),10);

	vec4 worldPosition = transformationMatrix * vec4(position,1.0);

	vec4 Peye = projectionMatrix * viewMatrix * worldPosition;

	//gl_ClipDistance[0] = dot(worldPosition,plane);
	gl_Position = Peye;
	vs_out.tc = textureCoords;

	vs_out.normal = (transformationMatrix * vec4(normal,0.0)).xyz;

    mat3 normalMatrix = transpose(inverse(mat3(transformationMatrix)));
    vec3 T = normalize(mat3(transformationMatrix) * tangent);
    vec3 N = normalize(mat3(transformationMatrix) * normal);
    vec3 B = normalize(cross(N,T));
    vs_out.TBN = transpose(mat3(T, B, N));

    vs_out.TangentLightPos = vs_out.TBN * lightPosition;
    vs_out.TangentViewPos  = vs_out.TBN * viewPos;
    vs_out.TangentFragPos  = vs_out.TBN * worldPosition.xyz;

	vs_out.RM = mat3( transformationMatrix[0].xyz,
			transformationMatrix[1].xyz,
			transformationMatrix[2].xyz );
	vs_out.toLightVector =  (lightPosition - worldPosition.xyz);

	vs_out.toCameraVector =  ((inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz);
	vs_out.height = position.y;

	vs_out.world_coord =  vec3(projectionMatrix) * vec3(viewMatrix) * worldPosition.xyz;
	vs_out.eye_coord = Peye.xyz ;
	vs_out.viewDir =  ( viewPos -  worldPosition.xyz);
	vs_out.shadow_coord = shadow_matrix * transformationMatrix * vec4(position,1.0);

	vs_out.Pos = worldPosition.xyz;

}
