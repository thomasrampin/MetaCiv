package renderEngine.postProcessing;

import org.lwjgl.opengl.GL11;

public class PostProcessingRenderer {

	private FrameBufferObject fbo;

	public PostProcessingRenderer(int width, int height) {
		this.fbo = new FrameBufferObject(width, height, FrameBufferObject.NONE);
	}

	public PostProcessingRenderer() {}

	public void renderQuad() {
		if (fbo != null) {
			fbo.bindFrameBuffer();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		if (fbo != null) {
			fbo.unbindFrameBuffer();
		}
	}

	public int getTexture() {
		return fbo.getColourTexture();
	}

	public  void cleanUp() {
		if (fbo != null) {
			fbo.cleanUp();
		}
	}

}
