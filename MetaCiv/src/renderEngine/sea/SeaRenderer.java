package renderEngine.sea;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL40;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.Window;
import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import renderEngine.loaders.Loader;
import renderEngine.models.Mesh;
import renderEngine.utils.FPS;
import renderEngine.utils.Matrix;

public class SeaRenderer {

    private static final String DIFFUSE_MAP = "water/water_NRM.png";
    private static final String DUDV_MAP = "water/waterDUDV.png";
    private static final String NORMAL_MAP = "water/water_NRM.png";
    private static final String DISP_MAP = "water/water_DISP.png";
	private static final float WAVE_SPEED = 0.01f;
	private static float WAVE_INCREASE_SPEED = 0.0001f;
    
	private Mesh quad;
	private SeaShader shader;
	private int textureID;
	private int dudvID;
	private int normalID;
	private int dispID;
	private float moveFactor = 0;
	private float waveStrenght = 1.1f;
	private int tessLevel = 16;

	public SeaRenderer(Loader loader, SeaShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		//setUpVAO(loader);
		textureID = loader.loadTexture(DIFFUSE_MAP);
		dudvID = loader.loadTexture(DUDV_MAP);
		normalID = loader.loadTexture(NORMAL_MAP);
		dispID = loader.loadTexture(DISP_MAP);
		GL40.glPatchParameteri(GL40.GL_PATCH_VERTICES, 4);
	}

	public void render(Camera camera, Light light,SeaFrameBuffers fbos,float distanceFog) {
		prepareRender(camera,light);	
		
			Matrix4f modelMatrix = Matrix.createTransformationMatrix(
					new Vector3f(-128,15,-128), 0, 0, 0,
					13);
			shader.loadModelMatrix(modelMatrix);
			shader.conectTexture();
			shader.loadTessLevel(tessLevel);
			shader.loadDistanceFog(distanceFog);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,fbos.getReflectionTexture());
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,dudvID);
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,normalID);
			GL13.glActiveTexture(GL13.GL_TEXTURE3);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,dispID);
			//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
			//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL31.glDrawArraysInstanced(GL40.GL_PATCHES, 0, 4, 64 * 64);
		
		//unbind();
	}
	
	private void prepareRender(Camera camera, Light light){
		shader.start();
		shader.loadlight(light);
		shader.loadViewMatrix(camera);
		shader.loadCamera(camera);
		//GL30.glBindVertexArray(quad.getVaoID());
		//GL20.glEnableVertexAttribArray(0);
		moveFactor += WAVE_SPEED * (float)FPS.getDelta()/1000;
		moveFactor %= 1;
		waveStrenght += WAVE_INCREASE_SPEED ;
		
		if(waveStrenght>=1.5 || waveStrenght<=0.001)
			WAVE_INCREASE_SPEED = -WAVE_INCREASE_SPEED;

			
		shader.loadMoveFactor(moveFactor);
		shader.loadWaveStrenght(waveStrenght/10f);
	}
	
	private void unbind(){
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = { -100, -100, -100, 100, 100, -100, 100, -100, -100, 100, 100, 100 };
		quad = loader.loadToVAO(vertices,2);
	}

	public void notifyTessLevel(int tessLevel) {
		this.tessLevel = tessLevel;
	}

}
