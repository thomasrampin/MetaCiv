package renderEngine.shaders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import renderEngine.utils.Matrix;

public class TerrainShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 4;
	
	private static final String VERTEX_FILE = "src/renderEngine/shaders/terrainVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/shaders/terrainFragmentShader.glsl";
	private static final String TESS_CONTROL_FILE = "src/renderEngine/shaders/terrainTesstcsShader.glsl";
	private static final String TESS_EVALUATION_FILE = "src/renderEngine/shaders/terrainTesstesShader.glsl";
	private static final String GEOMETRY_FILE = "src/renderEngine/shaders/terrainTessGeometryShader.glsl";

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
	private int location_reciveShadow;
	
	private int location_heights[];
	private int location_heights_size;
	private int location_distanceFog;
	
	private int location_cameraPos;
	
	private int location_dmap_depth;

	
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
		location_reciveShadow = super.getUniformLocation("reciveShadow");
		location_dmap_depth = super.getUniformLocation("dmap_depth");

		
		location_distanceFog = super.getUniformLocation("distanceFog");



		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		
		
		location_heights_size = super.getUniformLocation("heights_size");
		location_cameraPos = super.getUniformLocation("cameraPos");

		
		location_heights = new int[MAX_TERRAIN_TYPE];
		for(int i=0;i<MAX_TERRAIN_TYPE;i++){
			location_heights[i] = super.getUniformLocation("heights["+i+"]");
		}
		
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_diffuseMap, 0);
		//super.loadInt(location_shadowMap, 1);
	}
	
	public void loadCameraPos(Vector3f cameraPos){
		super.loadVector(location_cameraPos, cameraPos);
	}
	
	public void loadDiffuse(Vector3f diffuse){
		super.loadVector(location_diffuse, diffuse);
	}
	
	public void loadLights(Light light){

		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());


	}
	
	public void loadHeights(ArrayList<Vector4f> heights){
		super.loadInt(location_heights_size, heights.size());
		for(int i=0;i<heights.size();i++){
			int color = Color.HSBtoRGB(heights.get(i).x, heights.get(i).y, heights.get(i).z);
			float r = (color >> 16) & 0x000000FF;
			float g = (color >>8 ) & 0x000000FF;
			float b = (color) & 0x000000FF;
			Vector4f colorV = new Vector4f(r/255,g/255,b/255,heights.get(i).w/10);
			super.loadVector4(location_heights[i], colorV);
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

	public void loadMvp(Matrix4f mvp) {
		
		
	}

	public void loadDistanceFog(float distanceFog){
		super.loadFloat(location_distanceFog, distanceFog);
	}
	
	public void conectTextureDiff(int id) {
		String name = "gSampler["+id+"]";
		int location = super.getUniformLocation(name);
		super.loadInt(location, id+1);
		
	}
}