package renderEngine.fontRendering;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import renderEngine.entities.Camera;
import renderEngine.MasterRenderer;
import renderEngine.fontMeshCreator.FontType;
import renderEngine.fontMeshCreator.GUIText;

public class FontRenderer {

	private FontShader shader;

	public FontRenderer() {
		shader = new FontShader();
		shader.start();
		shader.loadProjectionMatrix(MasterRenderer.projectionMatrix);
		shader.stop();
	}

	public void render(Map<FontType, List<GUIText>> texts,Camera camera) {
		prepare();
		shader.loadViewMatrix(camera);
		for (FontType font : texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for (GUIText text : texts.get(font)) {
				renderText(text);
			}
		}
		endRendering();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	private void prepare() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.start();
	}

	private void renderText(GUIText text) {
		if(text.isVisible()){
			GL30.glBindVertexArray(text.getMesh());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			shader.loadFix(text.isFix());
			shader.loadColour(text.getColour());
			shader.loadTranslation(text.getPosition());
			shader.loadTransformationMatrix(text.getPosition());
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
		}
	}

	private void endRendering() {
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
