package renderEngine.bloom;

import renderEngine.shaders.ShaderProgram;

public class BrightFilterShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/renderEngine/bloom/bloomVertex.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/bloom/brightFilterFragment.glsl";
	
	public BrightFilterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
