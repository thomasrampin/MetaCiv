#version 330

out vec4 out_Color;

in VS_OUT
{
	vec2 tc;
	float height;
	vec3 world_coord;
	vec3 fromLightVector;
	vec3 toLightVector;
	vec3 toCameraVector;
	vec3 eye_coord;
	vec4 clipSpace;
}fs_in;




uniform sampler2D diffuseMap;
uniform sampler2D refractionMap;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;
uniform vec3 lightColour;

uniform float moveFactor;

uniform vec4 fog_color = vec4(0.4, 0.6, 0.9, 0);
uniform float distanceFog;

uniform float waveStrenght;
const float shineDamper = 20.0;
const float reflectivity = 0.6;
const vec4 coastlineColor = vec4(0.039,0.719,0.925,1);

vec4 fog(vec4 c)
{
    float z = length(fs_in.eye_coord)/distanceFog;

    float de = 0.025 * smoothstep(0.0, 6.0, 10.0 - fs_in.world_coord.y);
    float di = 0.045 * (smoothstep(0.0, 40.0, 20.0 - fs_in.world_coord.y));

    float extinction   = exp(-z * de);
    float inscattering = exp(-z * di);

    return c * extinction + fog_color * (1.0 - inscattering);
}


void main(void) {
	vec2 ndc = (fs_in.clipSpace.xy/fs_in.clipSpace.w) /2.0 + 0.5;
	vec2 coords = vec2(ndc.x,-ndc.y);


	vec2 coords2 = vec2(ndc.x,ndc.y);

	float near = 0.1;
	float far = 1000.0;
	float depth = texture(depthMap,coords2).r;
	float depthDistance = 2.0 * near * far / (far + near - (2.0 * depth -1.0) * (far - near));

	depth = gl_FragCoord.z;
	float seaDistance = 2.0 * near * far / (far + near - (2.0 * depth -1.0) * (far - near));
	float seaDepth = depthDistance - seaDistance;

	vec2 modifyTexCoords = fs_in.tc;


	vec2 distortion1 = (texture(dudvMap, vec2(fs_in.tc.x + moveFactor * 2.5,fs_in.tc.y)).rg * 2.5 -1.0) * 0.01;
	distortion1 += (texture(dudvMap, vec2(fs_in.tc.x + moveFactor* 2.5,fs_in.tc.y + moveFactor* 2.5)).rg * 2.0 -1.0) * 0.01 * clamp(seaDepth/20.0,0.0,1.0);








	modifyTexCoords += distortion1;

	vec3 viewVector = normalize(fs_in.toCameraVector);

	vec4 normalMapColour = texture(normalMap, modifyTexCoords*2.5);
	vec3 normal = vec3(normalMapColour.r *2.0 -1.0, normalMapColour.b, normalMapColour.g *2.0 -1.0);
	normal = normalize(normal);


	vec3 reflectedLight = reflect(normalize(fs_in.fromLightVector), normal);
	float specular = max(dot(reflectedLight, viewVector), 0.0);
	specular = pow(specular, shineDamper);
	vec3 specularHighlights = lightColour * specular * reflectivity *3.0 * clamp(seaDepth/5.0,0.0,1.0);


	//compute reflection coords

	coords += distortion1;
	coords.x = clamp(coords.x, 0.001, 0.999);
	coords.y = clamp(coords.y, -0.999, -0.001);

	coords2 += distortion1;
	coords2 = clamp(coords2, 0.001, 0.999);



	float nDotl = dot(vec3(0,1,0),normalize(fs_in.toLightVector));
	float brightness = max(nDotl,0.2);


	float fresnelFactor = min(max(dot(viewVector,normal),0.3),1.0);

	vec4 lighting =  vec4(brightness * lightColour ,1.0);



	vec4 depthTexture = mix(vec4(0.302, 0.439, 1.0,1.0),texture(refractionMap,coords2),(1-texture(depthMap,coords2).r));
	//out_Color = texture(diffuseMap,modifyTexCoords);
	out_Color = mix(texture(diffuseMap,coords), depthTexture,fresnelFactor);

	out_Color *= lighting;

	out_Color = mix(coastlineColor,out_Color,clamp(seaDepth/15.0,0.0,1.0)),
	out_Color += vec4(specularHighlights,0.0);
	out_Color.a = clamp(seaDepth/5.0,0.0,1.0);
	out_Color = fog(out_Color);



}
