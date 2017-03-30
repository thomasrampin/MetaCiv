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
}vs_out;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform vec3 cameraPos;
uniform mat4 shadow_matrix;

const mat4 scale_bias_matrix = mat4(0.5f, 0.0f, 0.0f, 0.0f,
                                    0.0f, 0.5f, 0.0f, 0.0f,
                                    0.0f, 0.0f, 0.5f, 0.0f,
									0.5f, 0.5f, 0.5f, 1.0f);

void main(void){


	vs_out.tangent = tangent;
	vec4 plane=vec4(vec3(vec3(projectionMatrix) * vec3(viewMatrix)),10);

	vec4 worldPosition = transformationMatrix * vec4(position,1.0);

	vec4 Peye = projectionMatrix * viewMatrix * worldPosition;

	//gl_ClipDistance[0] = dot(worldPosition,plane);
	gl_Position = Peye;
	vs_out.tc = textureCoords;

	vs_out.normal = (transformationMatrix * vec4(normal,0.0)).xyz;

	vec3 norm = normalize(normal);
	vec3 tang = normalize(vec3(viewMatrix * transformationMatrix * vec4(tangent,0.0)));
	vec3 bitang = normalize(cross(norm,tang));

	/*mat3 toTangentSpace = mat3(
		tang.x, bitang.x, norm.x,
		tang.y, bitang.y, norm.y,
		tang.z, bitang.z, norm.z
	);*/

    mat3 TBN = transpose(mat3(tang, bitang, norm));


	vs_out.toLightVector = lightPosition - worldPosition.xyz;

	vs_out.toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;


	vs_out.world_coord =  vec3(projectionMatrix) * vec3(viewMatrix) * worldPosition.xyz;
	vs_out.eye_coord = Peye.xyz ;
	vs_out.viewDir =  ( cameraPos -  worldPosition.xyz);
	vs_out.shadow_coord = shadow_matrix * transformationMatrix * vec4(position,1.0);

}
