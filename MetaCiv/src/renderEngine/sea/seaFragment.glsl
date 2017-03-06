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
uniform sampler2D refractionMap;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;
uniform vec3 lightColour;

uniform float moveFactor;

uniform vec4 fog_color = vec4(0.4f, 0.6f, 0.9f, 0f);
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
	vec2 modifyTexCoords2 = fs_in.tc;

	vec2 distortion1 = (texture(dudvMap, vec2(fs_in.tc.x*10.0 + moveFactor * 2.0,fs_in.tc.y*10.0)).rg * 2.0 -1.0) * 0.01;
	distortion1 += (texture(dudvMap, vec2(fs_in.tc.x*10.0 + moveFactor* 2.0,fs_in.tc.y*10.0 + moveFactor* 2.0)).rg * 2.0 -1.0) * 0.01 * clamp(seaDepth/20.0,0.0,1.0);








	modifyTexCoords += distortion1;

	vec3 viewVector = normalize(fs_in.toCameraVector);
	
	vec4 normalMapColour = texture(normalMap, modifyTexCoords*20);
	vec3 normal = vec3(normalMapColour.r *2.0 -1.0, normalMapColour.b, normalMapColour.g *2.0 -1.0);
	normal = normalize(normal);


	vec2 distortion2 = vec2(fs_in.tc.x*5 + moveFactor ,fs_in.tc.y*5.0 - moveFactor );
	modifyTexCoords2 += distortion2;

	distortion1 = (texture(dudvMap, vec2(fs_in.tc.x*50.0 + moveFactor * 2.0,fs_in.tc.y*50.0)).rg * 2.0 -1.0) * waveStrenght;
	distortion1 += (texture(dudvMap, vec2(fs_in.tc.x*50.0 + moveFactor* 2.0,fs_in.tc.y*50.0 + moveFactor* 2.0)).rg * 2.0 -1.0) * waveStrenght;
	vec3 normal2 =  normalize((texture(normalMap, distortion1).rbg* fs_in.height*10 ) - vec3(1.0));


	vec2 distortion3 = vec2(fs_in.tc.x*5 + moveFactor ,fs_in.tc.y*5.0 - moveFactor );
	vec3 normal3 =  normalize((texture(normalMap, distortion3).rbg* fs_in.height*10) - vec3(1.0));


	vec2 distortion4 = vec2(fs_in.tc.x*5.0 + moveFactor,fs_in.tc.y*5.0 + moveFactor*2.0);
	normalMapColour = texture(normalMap, distortion4);
	vec3 normal4 = vec3(normalMapColour.r *2.0 -1.0, normalMapColour.b, normalMapColour.g *2.0 -1.0)* waveStrenght;
	vec2 distortion5 = vec2(fs_in.tc.x*5.0,fs_in.tc.y*5.0 - moveFactor*2.0);
	normalMapColour = texture(normalMap, distortion5);
	vec3 normal5 = vec3(normalMapColour.r *2.0 -1.0, normalMapColour.b, normalMapColour.g *2.0 -1.0)* waveStrenght;

	vec3 reflectedLight = reflect(normalize(fs_in.fromLightVector), normal);
	float specular = max(dot(reflectedLight, viewVector), 0.3);
	specular = pow(specular, shineDamper);
	vec3 specularHighlights = lightColour * specular * reflectivity *3.0 * clamp(seaDepth/5.0,0.0,1.0);


	//compute reflection coords

	coords += distortion1;
	coords.x = clamp(coords.x, 0.001, 0.999);
	coords.y = clamp(coords.y, -0.999, -0.001);

	coords2 += distortion1;
	coords2 = clamp(coords2, 0.001, 0.999);



	float nDotl = dot(normal2,normalize(vec3(0,1,0)));
	float brightness = max(nDotl,0.5);
	nDotl = dot(normal3,normalize(vec3(0,1,0)));
	float brightness2 = max(nDotl,0.5);
	nDotl = dot(normal4,normalize(fs_in.toLightVector));
	float brightness3 = max(nDotl,0.4);
	nDotl = dot(normal5,normalize(fs_in.toLightVector));
	float brightness4 = max(nDotl,0.4);
	nDotl = dot(normalize(vec3(0,1,0)),normalize(fs_in.toLightVector));
	float brightness5 = max(nDotl,0.0);

	float fresnelFactor = dot(viewVector,vec3(0,1,0));

	vec4 lighting =  vec4(brightness * lightColour ,1.0);
	vec4 lighting2 =  vec4(brightness2 * lightColour ,1.0);
	vec4 lighting3 =  vec4(brightness3 * lightColour ,1.0);
	vec4 lighting4 =  vec4(brightness4 * lightColour ,1.0);
	vec4 lighting5 =  vec4(brightness5 * lightColour ,1.0);

	vec4 depthTexture = mix(vec4(0.0,0.3,1.0,1.0),texture(refractionMap,coords2),(1-texture(depthMap,coords2).r));
	//out_Color = texture(diffuseMap,modifyTexCoords);
	out_Color = mix(texture(diffuseMap,coords), depthTexture,fresnelFactor);
	out_Color = (mix(out_Color,vec4(1.0,1.0,1.0,1.0),fs_in.height));

	out_Color *= (lighting + lighting2  );
	out_Color *= lighting3 + lighting4;
	out_Color *= lighting5;
	out_Color = mix(coastlineColor,out_Color,clamp(seaDepth/15.0,0.0,1.0)),
	out_Color += vec4(specularHighlights,0.0);
	out_Color.a = clamp(seaDepth/5.0,0.0,1.0);
	out_Color = fog(out_Color);



}
