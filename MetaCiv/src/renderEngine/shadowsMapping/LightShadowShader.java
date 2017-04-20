package renderEngine.shadowsMapping;

import org.lwjgl.util.vector.Matrix4f;

import renderEngine.entities.Camera;
import renderEngine.shaders.ShaderProgram;
import renderEngine.utils.Matrix;

public class LightShadowShader extends ShaderProgram{
	private static final String VERTEX_FILE = "src/renderEngine/shadowsMapping/lightVs.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/shadowsMapping/lightFs.glsl";
	
	private int location_mvp;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_transformationMatrix;
	
	public LightShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvp = super.getUniformLocation("mvp");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Matrix.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadMvp(Matrix4f mvp){
		super.loadMatrix(location_mvp, mvp);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

}
