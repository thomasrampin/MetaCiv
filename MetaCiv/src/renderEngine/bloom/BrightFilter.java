package renderEngine.bloom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import renderEngine.postProcessing.PostProcessingRenderer;

public class BrightFilter {

	private PostProcessingRenderer renderer;
	private BrightFilterShader shader;
	
	public BrightFilter(int width, int height){
		shader = new BrightFilterShader();
		renderer = new PostProcessingRenderer(width, height);
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
