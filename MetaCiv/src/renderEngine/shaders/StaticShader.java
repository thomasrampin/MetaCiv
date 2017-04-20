package renderEngine.shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import renderEngine.utils.Matrix;

public class StaticShader extends ShaderProgram{
	
	private static final int MAX_LIGHTS = 4;
	
	private static final String VERTEX_FILE = "src/renderEngine/shaders/vertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/shaders/fragmentShader.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_diffuse;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_textured;
	private int location_shadowMap;
	private int location_diffuseMap;
	private int location_normalMap;
	private int location_dispMap;
	private int location_reciveShadow;
	private int location_colorID;
	private int location_normalMapped;
	private int location_dispMapped;
	private int location_viewPos;
	private int location_heightScale;
	private int location_roughnessMap;
	private int location_reflexion;
	private int location_reflexion_blur;
	private int location_skyAngle;
	
	private int location_reflMapped;

	private int location_metalMapped;

	private int location_colorAction;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normals");
		super.bindAttribute(3, "tangents");
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
		location_shadowMap = super.getUniformLocation("shadowMap"); 
		location_diffuseMap = super.getUniformLocation("diffuseMap"); 
		location_reciveShadow = super.getUniformLocation("reciveShadow");
		location_colorID = super.getUniformLocation("colorID");
		location_normalMap = super.getUniformLocation("normalMap");
		location_normalMapped = super.getUniformLocation("normalMapped");
		location_roughnessMap = super.getUniformLocation("roughnessMap");
		location_dispMap = super.getUniformLocation("dispMap");
		location_dispMapped = super.getUniformLocation("dispMapped");
		location_viewPos = super.getUniformLocation("viewPos");
		location_heightScale = super.getUniformLocation("height_scale");
		location_reflexion = super.getUniformLocation("reflexion");
		location_reflMapped = super.getUniformLocation("reflMapped");
		location_reflexion_blur = super.getUniformLocation("reflexion_blur");
		location_skyAngle = super.getUniformLocation("skyAngle");
		location_metalMapped = super.getUniformLocation("metalMapped");
		location_colorAction = super.getUniformLocation("colorAction");


		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		
	}
	
	public void loadSkyAngle(float skyAngle){
		super.loadFloat(location_skyAngle, skyAngle);
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_reflexion, 0);
		super.loadInt(location_reflexion_blur, 1);
		super.loadInt(location_diffuseMap, 2);
		super.loadInt(location_normalMap, 3);
		super.loadInt(location_dispMap, 4);
		super.loadInt(location_roughnessMap, 5);
		super.loadInt(location_shadowMap, 6);
	}
	
	public void loadHeightScale(float hS){
		super.loadFloat(location_heightScale, hS);
	}
	
	public void loadDiffuse(Vector3f diffuse){
		super.loadVector(location_diffuse, diffuse);
	}
	
	public void loadLights(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
		
	}
	
	public void loadDispMapped(boolean dispMapped){
		super.loadBoolean(location_dispMapped, dispMapped);
	}
	
	public void loadNormalMapped(boolean normalMapped){
		super.loadBoolean(location_normalMapped, normalMapped);
	}
	
	public void loadColorID(Vector4f colorID){
		super.loadVector4(location_colorID, colorID);
	}
	
	public void loadShineVariable(float damper, Vector3f relectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadVector(location_reflectivity, relectivity);
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
		super.loadVector(location_viewPos, camera.getPosition());
	}

	public void loadReflMapped(boolean b) {
		super.loadBoolean(location_reflMapped, b);
	}

	public void loadMetalMapped(boolean b) {
		super.loadBoolean(location_metalMapped, b);
	}

	public void loadColorAction(Vector3f colorAction) {
		super.loadVector(location_colorAction, colorAction);
	}

	
	
}
