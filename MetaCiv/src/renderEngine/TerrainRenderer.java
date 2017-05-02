package renderEngine;
 
import java.util.ArrayList;
import java.util.List;

import renderEngine.entities.Camera;
import renderEngine.loaders.Loader;
import renderEngine.materials.Material;
import renderEngine.models.Mesh;
import renderEngine.models.Model;
import renderEngine.postProcessing.FrameBufferObject;
import renderEngine.shadowsMapping.LightShadowShader;
import renderEngine.shadowsMapping.ShadowFrameBuffer;
import renderEngine.terrains.Terrain;
import renderEngine.terrains.TerrainShader;
import renderEngine.utils.Matrix;
import renderEngine.utils.TerrainTexture;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
 
public class TerrainRenderer {
 
    private TerrainShader shader;
    private LightShadowShader lShader;


    private int envTexture_level;
    

    
    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix,ArrayList<TerrainTexture> textures,Loader loader) {
        this.shader = shader;
        lShader = new LightShadowShader();
        envTexture_level = loader.loadTexture("reflect/level1.png");

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
        
        lShader.start();
        lShader.loadProjectionMatrix(projectionMatrix);
        lShader.stop();
         
    }
 
    public void render(Terrain terrain,ArrayList<TerrainTexture> textures, float distanceFog,boolean fromLight, ShadowFrameBuffer shadowsTexture,Matrix4f shadowMatrix) {
            prepareTerrain(terrain,textures,distanceFog,shadowsTexture,fromLight,shadowMatrix);
            loadModelMatrix(terrain);

            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT, 0);
            unbindTexturedModel();
    }
    
 
	public void renderL(Terrain terrain, ArrayList<TerrainTexture> textures, float distanceFog, boolean fromLight,
			ShadowFrameBuffer shadowsTexture, Matrix4f shadowMatrix,Matrix4f  light_vp_matrix,Camera camera) {
		
		lShader.start();
        Mesh rawModel = terrain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
		Matrix4f transformationMatrix = Matrix.createTransformationMatrix(
	                new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		//Matrix4f.mul(light_vp_matrix, transformationMatrix, light_vp_matrix);
		lShader.loadMvp(light_vp_matrix);
		lShader.loadViewMatrix(camera);

        lShader.loadTransformationMatrix(transformationMatrix);
		GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
                GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);

        GL30.glBindVertexArray(0);
		lShader.stop();
		
	}

    
    private void prepareTerrain(Terrain terrain,ArrayList<TerrainTexture> textures, float distanceFog,ShadowFrameBuffer shadowsTexture, boolean fromLight,Matrix4f shadowMatrix) {
        Mesh rawModel = terrain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
  
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        Material texture = terrain.getTexture();
        shader.loadDiffuse(texture.getDiffuse());
        shader.loadShineVariable(15f, new Vector3f(0.5f,0.5f,0.5f));
        shader.loadTextured(texture.IsTextured());
       
        shader.loadHeights(textures);
        shader.loadDistanceFog(distanceFog);
        
        //shader.loadShadowMatrix(shadowMatrix);
        
        		
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, envTexture_level);
    
        if(shadowsTexture!=null){
        	GL13.glActiveTexture(GL13.GL_TEXTURE2);
        	GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadowsTexture.getDepthTexture());
        }else{
        	GL13.glActiveTexture(GL13.GL_TEXTURE2);
        	GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());       	
        }
       
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getCliffMap());
 
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getRoadMap());
    	
        int textureArray [] = Terrain.getTextureArray();
        if(textureArray!=null){
	        for(int i=0;i<textureArray.length;i++){
	        	GL13.glActiveTexture(GL13.GL_TEXTURE0+i+5);
	        	GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureArray[i]);
	        	shader.conectTextureDiff(i);
	        }
        }

    }
 
    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
    }
 
    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix = Matrix.createTransformationMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }




 
}