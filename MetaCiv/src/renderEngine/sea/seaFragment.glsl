#version 430

out vec4 out_Color;

in TES_OUT
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
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform vec3 lightColour;

uniform float moveFactor;

uniform vec4 fog_color = vec4(0.4f, 0.6f, 0.9f, 0f);
uniform float distanceFog;

uniform float waveStrenght;
const float shineDamper = 20.0;
const float reflectivity = 0.6;

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

	vec2 modifyTexCoords = fs_in.tc;
	vec2 modifyTexCoords2 = fs_in.tc;

	vec2 distortion1 = (texture(dudvMap, vec2(fs_in.tc.x*10.0 + moveFactor * 2.0,fs_in.tc.y*10.0)).rg * 2.0 -1.0) * waveStrenght;
	distortion1 += (texture(dudvMap, vec2(fs_in.tc.x*10.0 + moveFactor* 2.0,fs_in.tc.y*10.0 + moveFactor* 2.0)).rg * 2.0 -1.0) * waveStrenght;


	modifyTexCoords += distortion1;
	
	vec3 viewVector = normalize(fs_in.toCameraVector);
	
	vec4 normalMapColour = texture(normalMap, modifyTexCoords);
	vec3 normal = vec3(normalMapColour.r *2.0 -1.0, normalMapColour.b, normalMapColour.g *2.0 -1.0);
	normal = normalize(normal);


	vec2 distortion2 = vec2(fs_in.tc.x*5 + moveFactor ,fs_in.tc.y*5.0 - moveFactor );
	modifyTexCoords2 += distortion2;

	distortion1 = (texture(dudvMap, vec2(fs_in.tc.x*50.0 + moveFactor * 2.0,fs_in.tc.y*50.0)).rg * 2.0 -1.0) * waveStrenght;
	distortion1 += (texture(dudvMap, vec2(fs_in.tc.x*50.0 + moveFactor* 2.0,fs_in.tc.y*50.0 + moveFactor* 2.0)).rg * 2.0 -1.0) * waveStrenght;
	vec3 normal2 =  normalize((texture(normalMap, distortion1).rbg* fs_in.height*10 ) - vec3(1.0));

	vec2 distortion3 = vec2(fs_in.tc.x*5 + moveFactor ,fs_in.tc.y*5.0 - moveFactor );



	vec3 normal3 =  normalize((texture(normalMap, distortion3).rbg* fs_in.height*10) - vec3(1.0));

	
	vec3 reflectedLight = reflect(normalize(fs_in.fromLightVector), normal);
	float specular = max(dot(reflectedLight, viewVector), 0.3);
	specular = pow(specular, shineDamper);
	vec3 specularHighlights = lightColour * specular * reflectivity;




	float nDotl = dot(normal2,normalize(vec3(0,1,0)));
	float brightness = max(nDotl,0.5);
	nDotl = dot(normal3,normalize(vec3(0,1,0)));
	float brightness2 = max(nDotl,0.5);

	float fresnelFactor = dot(viewVector,vec3(0.0,1.0,0.0));
	
	vec4 lighting =  vec4(brightness * lightColour ,1.0);
	vec4 lighting2 =  vec4(brightness2 * lightColour ,1.0);

	//compute reflection coords
	vec2 ndc = (fs_in.clipSpace.xy/fs_in.clipSpace.w) /2.0 + 0.5;
	vec2 coords = vec2(ndc.x,-ndc.y);
	coords += distortion1;
	coords.x = clamp(coords.x, 0.001, 0.999);
	coords.y = clamp(coords.y, -0.999, -0.001);
	//out_Color = texture(diffuseMap,modifyTexCoords);
	out_Color = texture(diffuseMap,coords);


	out_Color = (mix(out_Color,vec4(1.0,1.0,1.0,1.0),fs_in.height));
	out_Color = mix(out_Color,vec4(0.0,0.3,1.0,1.0),fresnelFactor);
	out_Color *= (lighting + lighting2  );
	out_Color += vec4(specularHighlights,0.0);
	out_Color = fog(out_Color);



}
