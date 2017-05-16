package renderEngine;
 
import java.util.List;
import java.util.Map;

import renderEngine.entities.Object3D;
import renderEngine.loaders.Loader;
import renderEngine.materials.Material;
import renderEngine.models.Mesh;
import renderEngine.models.Model;
import renderEngine.models.Models;
import renderEngine.sea.SeaFrameBuffers;
import renderEngine.shaders.StaticShader;
import renderEngine.sky.SkyRenderer;
import renderEngine.utils.Matrix;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
 
public class EntityRenderer {
 
    private StaticShader shader;
    private int envTexture_level;
    private float hS = 0.0f;
 
    public EntityRenderer(Loader loader,StaticShader shader,Matrix4f projectionMatrix) {
        this.shader = shader;
        envTexture_level = loader.loadTexture("reflect/level1.png");

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }



    public void render(Map<Model, List<Object3D>> entities,Map<Models, List<Object3D>> entities2,SeaFrameBuffers fbos, float distanceFog) {
        for (Model model : entities.keySet()) {
        	
        	
            prepareModel(model,fbos,distanceFog);
            List<Object3D> batch = entities.get(model);
            for (Object3D entity : batch) {
            	shader.loadColorAction(entity.getColorAction());
            	shader.loadColorID(entity.getColorID());
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            unbindModel();
        }

    }
 
    
    private void prepareModel(Model model,SeaFrameBuffers fbos, float distanceFog) {
        Mesh rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        Material texture = model.getTexture();
        shader.loadDispMapped(false);
        shader.loadSkyAngle(SkyRenderer.angle);
        shader.loadTextured(false);
        shader.loadNormalMapped(false);
        shader.loadReflMapped(false);
        shader.loadMetalMapped(false);
        shader.loadDistanceFog(distanceFog);
        shader.loadDiffuse(texture.getDiffuse());
        shader.loadShineVariable(texture.getShineDamper(), texture.getReflectivity());
        
        //shader.loadColorAction(model.getColorAction());
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
        	hS+=0.1;
        	
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_I)){
        	hS-=0.1;
        	
        }
        
        shader.loadHeightScale(hS/100.0f);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, envTexture_level);
    	if(model.getTexture().IsTextured()){
    		shader.loadTextured(texture.IsTextured());
    		GL13.glActiveTexture(GL13.GL_TEXTURE1);
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
    	}
    	if(model.getTexture().IsNormalMapped()){
    		shader.loadNormalMapped(true);
    		GL13.glActiveTexture(GL13.GL_TEXTURE2);
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getNormalID());
    	}
    	if(model.getTexture().IsDispMapped()){
    		shader.loadDispMapped(true);
    		GL13.glActiveTexture(GL13.GL_TEXTURE3);
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getDispID());
    	}
    	if(model.getTexture().IsReflMapped()){
    		shader.loadReflMapped(true);
    		GL13.glActiveTexture(GL13.GL_TEXTURE4);
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getReflID());
    	}
    	if(model.getTexture().IsMetalMapped()){
    		shader.loadMetalMapped(true);
    		GL13.glActiveTexture(GL13.GL_TEXTURE5);
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getMetalID());
    	}
    }
 
    private void unbindModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
    }
 
    private void prepareInstance(Object3D entity) {
        Matrix4f transformationMatrix = Matrix.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }
 
}