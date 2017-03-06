package renderEngine.depthOfField;

import renderEngine.shaders.ShaderProgram;

public class DepthOfFieldShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/renderEngine/depthOfField/dofVertex.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/depthOfField/dofFragment.glsl";
	
	private int location_colourTexture;
	private int location_blurTexture;
	private int location_depthBuffer;
	
	protected DepthOfFieldShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_colourTexture = super.getUniformLocation("colourTexture");
		location_blurTexture = super.getUniformLocation("blurTexture");
		location_depthBuffer = super.getUniformLocation("depthBuffer");
	}
	
	protected void connectTextureUnits(){
		super.loadInt(location_colourTexture, 0);
		super.loadInt(location_blurTexture, 1);
		super.loadInt(location_depthBuffer, 2);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
