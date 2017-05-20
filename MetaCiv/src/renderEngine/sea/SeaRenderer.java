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

import civilisation.world.World;
import renderEngine.Window;
import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import renderEngine.loaders.Loader;
import renderEngine.models.Mesh;
import renderEngine.terrains.Terrain;
import renderEngine.utils.FPS;
import renderEngine.utils.Matrix;

public class SeaRenderer {

    private static final String DIFFUSE_MAP = "water/water_NRM.png";
    private static final String DUDV_MAP = "water/waterDUDV.png";
    private static final String NORMAL_MAP = "water/water_NRM.png";
    private static final String DISP_MAP = "water/water_DISP.png";
	private static final float WAVE_SPEED = 0.01f;
	private static float WAVE_INCREASE_SPEED = 0.001f;
    
	private Mesh quad;
	private SeaTessShader shaderTess;
	private SeaShader shader;
	
	private int textureID;
	private int dudvID;
	private int normalID;
	private int dispID;
	private float moveFactor = 0;
	private float waveStrenght = 1.1f;
	private int tessLevel = 16;

	public SeaRenderer(Loader loader, Matrix4f projectionMatrix) {
		
		
		if(Window.is4){
			this.shaderTess = new SeaTessShader();
			shaderTess.start();
			shaderTess.loadProjectionMatrix(projectionMatrix);
			shaderTess.stop();
		}
			
		this.shader = new SeaShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.conectTexture();
		shader.stop();
		
		//setUpVAO(loader);
		//textureID = loader.loadTexture(DIFFUSE_MAP);
		dudvID = loader.loadTexture(DUDV_MAP);
		normalID = loader.loadTexture(NORMAL_MAP);
		dispID = loader.loadTexture(DISP_MAP);
		GL40.glPatchParameteri(GL40.GL_PATCH_VERTICES, 4);
	}

	public void render(Camera camera, Light light,SeaFrameBuffers fbos,float distanceFog, float delta) {
		prepareRender(camera,light,delta);	
		
		Matrix4f modelMatrix;/* = Matrix.createTransformationMatrix(
				new Vector3f(-128,0,-128), 0, 0, 0,
				13,13,13);*/
		if(Window.is4 && World.getSea()==2){
			
			shaderTess.conectTexture();
			shaderTess.loadTessLevel(tessLevel);
			shaderTess.loadDistanceFog(distanceFog);
		}else{
			
			
			shader.loadDistanceFog(distanceFog);
		}
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,dudvID);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,normalID);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,dispID);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,fbos.getRefractionDepthTexture());
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
	
		
		
		int l=0;
		int m=0;
		
		while(Terrain.SIZE_Z>-128+64*13*l){
			m=0;
			while(Terrain.SIZE_X>-128+64*13*m){
				modelMatrix = Matrix.createTransformationMatrix(
						new Vector3f(-128+(64*13*l),0,-128+(64*13*m)), 0, 0, 0,
						13,13,13);
				if(Window.is4 && World.getSea()==2 ){
					
					shaderTess.loadModelMatrix(modelMatrix);
				}else{
				
					shader.loadModelMatrix(modelMatrix);
				}
				if(Window.is4 && World.getSea()==2){
					if(-128+64*13*(l+1) + 64*13>camera.getPosition().x && -128+64*13*(l-1)<camera.getPosition().x && -128+(64*13*(m+1)) + 64*13>camera.getPosition().z && -128+(64*13*(m-1))<camera.getPosition().z)
						GL31.glDrawArraysInstanced(GL40.GL_PATCHES, 0, 4, 64*64);
					else{
						shaderTess.stop();
						
						shader.start();
						shader.loadDistanceFog(distanceFog);
						shader.loadlight(light);
						shader.loadViewMatrix(camera);
						shader.loadCamera(camera);
						shader.loadModelMatrix(modelMatrix);
						GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
						shader.stop();
					
						
						shaderTess.start();
					}
				}else{
					GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
				}
				m++;
			}
			l++;
		}
		
		
		unbind();
	}
	
	private void prepareRender(Camera camera, Light light, float delta){
		if(Window.is4 && World.getSea()==2){
			shaderTess.start();
			shaderTess.loadlight(light);
			shaderTess.loadViewMatrix(camera);
			shaderTess.loadCamera(camera);
		}else{
			shader.start();
			shader.loadlight(light);
			shader.loadViewMatrix(camera);
			shader.loadCamera(camera);
		}
		//GL30.glBindVertexArray(quad.getVaoID());
		//GL20.glEnableVertexAttribArray(0);
		moveFactor += WAVE_SPEED * delta/1000;
		moveFactor %= 1;
		waveStrenght += WAVE_INCREASE_SPEED ;
		
		if(waveStrenght>=1.5 || waveStrenght<=0.001)
			WAVE_INCREASE_SPEED = -WAVE_INCREASE_SPEED;

		if(Window.is4 && World.getSea()==2){
			shaderTess.loadMoveFactor(moveFactor);
			shaderTess.loadWaveStrenght(waveStrenght/10f);
		}else{
			shader.loadMoveFactor(moveFactor);
			shader.loadWaveStrenght(waveStrenght/10f);
		}
	}
	
	private void unbind(){
		GL11.glDisable(GL11.GL_BLEND);
		//GL20.glDisableVertexAttribArray(0);
		//GL30.glBindVertexArray(0);
		if(Window.is4 && World.getSea()==2){
			shaderTess.stop();
		}else{
			shader.stop();
		}
	}


	public void notifyTessLevel(int tessLevel) {
		this.tessLevel = tessLevel;
	}

}
