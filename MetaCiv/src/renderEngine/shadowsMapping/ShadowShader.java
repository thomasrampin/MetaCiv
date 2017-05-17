package renderEngine.shadowsMapping;

import renderEngine.shaders.ShaderProgram;

public class ShadowShader extends ShaderProgram{
	private static final String VERTEX_FILE = "/renderEngine/shadowsMapping/vertexShader.glsl";
	private static final String FRAGMENT_FILE = "/renderEngine/shadowsMapping/fragmentShader.glsl";
	
	public ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {

	}

	@Override
	protected void bindAttributes() {

	}

}
