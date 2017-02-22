package renderEngine;
 
import java.util.ArrayList;
import java.util.List;

import renderEngine.loaders.Loader;
import renderEngine.materials.Material;
import renderEngine.models.Mesh;
import renderEngine.models.Model;
import renderEngine.shaders.TerrainShader;
import renderEngine.terrains.Terrain;
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
    private int diffuseArray[];

    private int diffuseArraySize;
    
    private boolean wireframe;
    private boolean press;
    
    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix,ArrayList<TerrainTexture> textures) {
        this.shader = shader;
        this.wireframe = false;
        this.press = false;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        diffuseArray = Loader.loadTextureAtlas(textures); 
    }
 
    public void render(Terrain terrain,ArrayList<Vector4f> heights, float distanceFog) {
            prepareTerrain(terrain,heights,distanceFog);
            loadModelMatrix(terrain);
            if(Keyboard.isKeyDown(Keyboard.KEY_W) && !press){
            	wireframe = !wireframe;
            	press = true;
            }
            if(!Keyboard.isKeyDown(Keyboard.KEY_W))
            	press = false;
            if(wireframe)
            	GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            else
            	GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT, 0);
            unbindTexturedModel();
    }
 
    private void prepareTerrain(Terrain terrain,ArrayList<Vector4f> heights, float distanceFog) {
        Mesh rawModel = terrain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        Material texture = terrain.getTexture();
        shader.loadDiffuse(texture.getDiffuse());
        shader.loadShineVariable(15f, new Vector3f(0.5f,0.5f,0.5f));
        shader.loadTextured(texture.getIsTextured());
        shader.loadHeights(heights);
        shader.loadDistanceFog(distanceFog);
        
        		
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
        shader.connectTextureUnits();
        for(int i=0;i<diffuseArray.length;i++){
        	GL13.glActiveTexture(GL13.GL_TEXTURE0+i+1);
        	GL11.glBindTexture(GL11.GL_TEXTURE_2D, diffuseArray[i]);
        	shader.conectTextureDiff(i);
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