package renderEngine.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ContrastBalance {

	private PostProcessingRenderer renderer;
	private ContrastShader shader;
	
	public ContrastBalance(int targetFboWidth, int targetFboHeight){
		shader = new ContrastShader();
		renderer = new PostProcessingRenderer(targetFboWidth,targetFboHeight);
	}
	
	public void render(int texture ){
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanUp(){
		renderer.cleanUp();
		shader.cleanUp();
	}

	public int getTexture() {
		return renderer.getTexture();
	}
	
}
