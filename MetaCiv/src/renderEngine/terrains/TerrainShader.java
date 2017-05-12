package renderEngine.terrains;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import renderEngine.shaders.ShaderProgram;
import renderEngine.utils.Matrix;
import renderEngine.utils.TerrainTexture;

public class TerrainShader extends ShaderProgram {


	
	private static final String VERTEX_FILE = "/renderEngine/terrains/terrainVertexShader.glsl";
	private static final String FRAGMENT_FILE = "/renderEngine/terrains/terrainFragmentShader.glsl";


	private static final int MAX_TERRAIN_TYPE = 11;
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_diffuse;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_textured;
	private int location_toShadowMapSpace;
	private int location_shadowMap;
	private int location_diffuseMap;
	private int location_reflexion;
	private int location_reciveShadow;
	private int location_shadowMatrix;
	private int location_viewPos;
	
	private int location_heights[];
	private int location_heights_size;
	private int location_distanceFog;
	
	

	
	private int location_dmap_depth;

	private int location_cliffMap;
	private int location_roadMap;

	private int location_snow;

	private int location_distanceAttSnow;

	private int location_snowDensity;
	private int location_roadTiling;
	private int location_cliffTiling;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "tangent");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_textured = super.getUniformLocation("textured");
		location_diffuse = super.getUniformLocation("diffuseColour");
		location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
		location_shadowMap = super.getUniformLocation("shadowMap"); 
		location_diffuseMap = super.getUniformLocation("blendMap"); 
		location_cliffMap = super.getUniformLocation("cliffMap");
		location_roadMap = super.getUniformLocation("roadMap");
		location_reciveShadow = super.getUniformLocation("reciveShadow");
		location_dmap_depth = super.getUniformLocation("dmap_depth");
		location_reflexion = super.getUniformLocation("reflexion");
		location_shadowMatrix = super.getUniformLocation("shadow_matrix");
		location_distanceFog = super.getUniformLocation("distanceFog");
		location_viewPos = super.getUniformLocation("viewPos");

		location_snow = super.getUniformLocation("snow");
		location_distanceAttSnow = super.getUniformLocation("snowAttenuation");
		location_snowDensity = super.getUniformLocation("snowDensity");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		
		
		location_heights_size = super.getUniformLocation("heights_size");

		location_roadTiling = super.getUniformLocation("roadTiling");
		location_cliffTiling = super.getUniformLocation("cliffTiling");
		location_heights = new int[MAX_TERRAIN_TYPE];
		for(int i=0;i<MAX_TERRAIN_TYPE;i++){
			location_heights[i] = super.getUniformLocation("heights["+i+"]");
		}
		
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_diffuseMap, 0);
		super.loadInt(location_reflexion, 1);
	    super.loadInt(location_shadowMap, 2);
	    super.loadInt(location_cliffMap, 3);
	    super.loadInt(location_roadMap, 4);
	}
	
	public void loadShadowMatrix(Matrix4f matrix){
		super.loadMatrix(location_shadowMatrix, matrix);
	}
	
	public void loadCameraPos(Vector3f cameraPos){
		super.loadVector(location_viewPos, cameraPos);
	}
	
	public void loadDiffuse(Vector3f diffuse){
		super.loadVector(location_diffuse, diffuse);
	}
	
	public void loadLights(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	
	public void loadHeights(ArrayList<TerrainTexture> textures){
		ArrayList<TerrainTexture> texturese = Terrain.getEffectiveType();
		
		super.loadInt(location_heights_size, texturese.size());
		for(int i=0;i<texturese.size();i++){
			
			
			Vector4f h = new Vector4f(texturese.get(i).getTiling(),texturese.get(i).isTextured(),0,texturese.get(i).getHeight());//2 empty slots
			
			super.loadVector4(location_heights[i], h);
		}
	}
	
	public void loadShineVariable(float damper, Vector3f relectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadVector(location_reflectivity, relectivity);
	}

	public void loadDmap_depth(float dmap_depth){
		super.loadFloat(location_dmap_depth, dmap_depth);
	}
	
	public void loadReceiveShadow(boolean reciveShadow){
		super.loadBoolean(location_reciveShadow, reciveShadow);
	}
	
	public void loadTextured(boolean textured){
		super.loadBoolean(location_textured, textured);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Matrix.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadToShadowSpaceMatrix(Matrix4f matrix){
		super.loadMatrix(location_toShadowMapSpace, matrix);
	}

	public void loadDistanceFog(float distanceFog){
		super.loadFloat(location_distanceFog, distanceFog);
	}
	
	public void conectTextureDiff(int id) {
		String name = "gSampler["+id+"]";
		int location = super.getUniformLocation(name);
		super.loadInt(location, id+5);
		
	}

	public void loadSnow(float snow) {
		super.loadFloat(location_snow, snow);
	}

	public void loadDistanceAttSnow(float snowDistanceAtt) {
		super.loadFloat(location_distanceAttSnow, snowDistanceAtt);
	}

	public void loadSnowDensity(float snowDensity) {
		super.loadFloat(location_snowDensity, snowDensity);
	}
	
	public void loadRoadTiling(float roadTiling){
		super.loadFloat(location_roadTiling, roadTiling);
	}
	
	public void loadCliffTiling(float cliffTiling){
		super.loadFloat(location_cliffTiling, cliffTiling);
	}
}