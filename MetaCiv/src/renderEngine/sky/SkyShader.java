package renderEngine.sky;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.entities.Camera;

import renderEngine.shaders.ShaderProgram;
import renderEngine.utils.Matrix;

public class SkyShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/renderEngine/sky/skyVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/sky/skyFragmentShader.glsl";
	
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_transformationMatrix;
	private int location_angle;
	private int location_cloudDensity;
	private int location_sunPosition;
	private int location_plane;
	
	private int location_diffuseMap;
	private int location_alphaMap;
	
	public SkyShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadAngle(float angle){
		super.loadFloat(location_angle, angle);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Matrix.createViewMatrix(camera);
		//Block translation
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadDensity(float density){
		super.loadFloat(location_cloudDensity, density);
	}
	
	public void loadSunPosition(Vector3f sunPosition){
		super.loadVector(location_sunPosition, sunPosition);
	}
	
	public void loadPlane(Vector4f plane){
		super.loadVector4(location_plane, plane);
	}
	
	public void connectTextures(){
		super.loadInt(location_diffuseMap, 0);
		super.loadInt(location_alphaMap, 1);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_diffuseMap = super.getUniformLocation("diffuseMap");
		location_alphaMap = super.getUniformLocation("alphaMap");
		location_angle = super.getUniformLocation("angle");
		location_cloudDensity = super.getUniformLocation("cloudDensity");
		location_sunPosition = super.getUniformLocation("sunPosition");
		location_plane = super.getUniformLocation("plane");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "tc");
	}
}
