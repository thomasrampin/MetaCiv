package renderEngine.sea;

import org.lwjgl.util.vector.Matrix4f;

import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import renderEngine.shaders.ShaderProgram;
import renderEngine.utils.Matrix;

public class SeaShader extends ShaderProgram  {
	
	private final static String VERTEX_FILE = "/renderEngine/sea/seaVertex.glsl";
	private final static String FRAGMENT_FILE = "/renderEngine/sea/seaFragment.glsl";

	
	
	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_diffuseMap;
	private int location_refractionMap;
	private int location_dudvMap;
	private int location_moveFactor;
	private int location_normalMap;
	private int location_dispMap;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_cameraPosition;
	private int location_tessLevel;
	private int location_waveStrenght;
	private int location_distanceFog;
	private int location_depthMap;
	
	
	public SeaShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		//bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_diffuseMap = getUniformLocation("diffuseMap");
		location_refractionMap = getUniformLocation("refractionMap");
		location_dudvMap = getUniformLocation("dudvMap");
		location_moveFactor = getUniformLocation("moveFactor");
		location_normalMap = getUniformLocation("normalMap");
		location_dispMap = getUniformLocation("dispMap");
		location_lightPosition = getUniformLocation("lightPosition");
		location_lightColour = getUniformLocation("lightColour");
		location_cameraPosition = getUniformLocation("cameraPosition");
		location_tessLevel = getUniformLocation("tessLevel");
		location_waveStrenght = getUniformLocation("waveStrenght");
		location_distanceFog = super.getUniformLocation("distanceFog");
		location_depthMap = super.getUniformLocation("depthMap");
	}

	public void conectTexture(){
		super.loadInt(location_diffuseMap, 0);
		super.loadInt(location_refractionMap, 1);
		super.loadInt(location_dudvMap, 2);
		super.loadInt(location_normalMap, 3);
		super.loadInt(location_dispMap, 4);
		super.loadInt(location_depthMap, 5);
	}

	public void loadDistanceFog(float distanceFog){
		super.loadFloat(location_distanceFog, distanceFog);
	}
	
	public void loadWaveStrenght(float waveStrenght){
		super.loadFloat(location_waveStrenght, waveStrenght);
	}
	
	public void loadCamera(Camera camera){
		super.loadVector(location_cameraPosition, camera.getPosition());
	}
	
	public void loadlight(Light light){
		super.loadVector(location_lightColour, light.getColour());
		super.loadVector(location_lightPosition, light.getPosition());
	}
	
	public void loadMoveFactor(float moveFactor){
		super.loadFloat(location_moveFactor, moveFactor);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Matrix.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

	public void loadTessLevel(float tessLevel) {
		super.loadFloat(location_tessLevel, tessLevel);
	}

}
