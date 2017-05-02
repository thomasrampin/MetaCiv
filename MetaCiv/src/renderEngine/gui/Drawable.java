package renderEngine.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import renderEngine.loaders.Loader;
import renderEngine.models.Mesh;
import renderEngine.utils.Matrix;

public class Drawable {

	public final Mesh quad;
	private GuiShader shader;
	
	private List<Panel> guis;
	
	public Drawable(Loader loader){
		float[] positions = {-1,1,
							-1,-1,
							1,1,
							1,-1};
		quad = loader.loadToVAO(positions,2);
		shader = new GuiShader();
		guis = new ArrayList<Panel>();
	}
	
	public void addPanel(Panel gui){
		guis.add(gui);
	}
	
	public void draw(){
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(Panel gui:guis){
			if(gui.isTextured()){
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,gui.getTextureId());
			}
			shader.loadIsTextured(gui.isTextured());
			shader.loadColor(gui.getColor());
			shader.loadIsHover(gui.isHover());
			Matrix4f matrix = Matrix.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
	
}
