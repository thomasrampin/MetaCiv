package renderEngine.instanced;


import org.lwjgl.util.vector.Matrix4f;

import renderEngine.shaders.ShaderProgram;


public class InstancedShader extends ShaderProgram{
	

	private static final String VERTEX_FILE = "/renderEngine/instanced/vertexShader.glsl";
	private static final String FRAGMENT_FILE = "/renderEngine/instanced/fragmentShader.glsl";
	private int location_projectionMatrix;
	

	public InstancedShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "roadPos1");
		super.bindAttribute(6, "roadPos2");
		super.bindAttribute(7, "roadPos3");
		super.bindAttribute(8, "roadPos4");

	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}


	
	
}
