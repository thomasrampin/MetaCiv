package renderEngine.shadowsMapping;

import org.lwjgl.util.vector.Matrix4f;

import renderEngine.shaders.ShaderProgram;

public class LightShadowShader extends ShaderProgram{
	private static final String VERTEX_FILE = "src/renderEngine/shadowsMapping/lightVs.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/shadowsMapping/lightFs.glsl";
	
	private int location_mvp;
	private int location_projectionMatrix;
	
	public LightShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvp = super.getUniformLocation("mvp");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	public void loadMvp(Matrix4f mvp){
		super.loadMatrix(location_mvp, mvp);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
