package renderEngine.depthOfField;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import renderEngine.postProcessing.PostProcessingRenderer;

public class DepthOfFieldFilter {
	
	private PostProcessingRenderer renderer;
	private DepthOfFieldShader shader;
	
	public DepthOfFieldFilter(){
		shader = new DepthOfFieldShader();
		shader.start();
		shader.connectTextureUnits();
		shader.stop();
		renderer = new PostProcessingRenderer();
	}
	
	public void render(int colourTexture, int blurTexture,int depthBuffer){
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colourTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, blurTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthBuffer);
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
