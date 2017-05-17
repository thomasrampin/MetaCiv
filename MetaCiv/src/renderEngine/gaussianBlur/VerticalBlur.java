package renderEngine.gaussianBlur;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import renderEngine.postProcessing.PostProcessingRenderer;

public class VerticalBlur {
	
	private PostProcessingRenderer renderer;
	private VerticalBlurShader shader;
	
	public VerticalBlur(int targetFboWidth, int targetFboHeight){
		shader = new VerticalBlurShader();
		renderer = new PostProcessingRenderer(targetFboWidth, targetFboHeight);
		shader.start();
		shader.loadTargetHeight(targetFboHeight);
		shader.stop();
	}

	
	public void render(int texture){
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public int getTexture(){
		return renderer.getTexture();
	}
	
	public void cleanUp(){
		renderer.cleanUp();
		shader.cleanUp();
	}
}
