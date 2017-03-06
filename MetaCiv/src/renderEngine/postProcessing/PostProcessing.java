package renderEngine.postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import renderEngine.models.Mesh;
import renderEngine.bloom.BrightFilter;
import renderEngine.bloom.CombineFilter;
import renderEngine.depthOfField.DepthOfFieldFilter;
import renderEngine.gaussianBlur.HorizontalBlur;
import renderEngine.gaussianBlur.VerticalBlur;
import renderEngine.loaders.Loader;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static Mesh quad;
	
	private static ContrastBalance contrastChanger;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static HorizontalBlur hBlur2;
	private static VerticalBlur vBlur2;
	private static HorizontalBlur hBlur3;
	private static VerticalBlur vBlur3;
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;
	private static DepthOfFieldFilter dofFilter;

	public static void init(Loader loader){
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastBalance();
		brightFilter = new BrightFilter(Display.getWidth()/2,Display.getHeight()/2);
		hBlur = new HorizontalBlur(Display.getWidth()/16,Display.getHeight()/16);
		vBlur = new VerticalBlur(Display.getWidth()/16,Display.getHeight()/16);
		hBlur2 = new HorizontalBlur(Display.getWidth()/4,Display.getHeight()/4);
		vBlur2 = new VerticalBlur(Display.getWidth()/4,Display.getHeight()/4);
		hBlur3 = new HorizontalBlur(Display.getWidth(),Display.getHeight());
		vBlur3 = new VerticalBlur(Display.getWidth(),Display.getHeight());
		dofFilter = new DepthOfFieldFilter();
		combineFilter = new CombineFilter(Display.getWidth(),Display.getHeight());
	}
	
	public static void doPostProcessing(int colourTexture,int depthBuffer){
		start();
		brightFilter.render(colourTexture);
		hBlur.render(brightFilter.getOutputTexture());
		vBlur.render(hBlur.getOutputTexture());
		hBlur2.render(hBlur.getOutputTexture());
		vBlur2.render(hBlur2.getOutputTexture());
	
		combineFilter.render(colourTexture,vBlur2.getOutputTexture());
		hBlur3.render(combineFilter.getOutputTexture());
		vBlur3.render(hBlur3.getOutputTexture());	
		dofFilter.render(combineFilter.getOutputTexture(), vBlur3.getOutputTexture(),depthBuffer);
		end();
	}
	
	public static void cleanUp(){
		contrastChanger.cleanUp();
		brightFilter.cleanUp();
		combineFilter.cleanUp();
		dofFilter.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		hBlur2.cleanUp();
		vBlur2.cleanUp();
		hBlur3.cleanUp();
		vBlur3.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
