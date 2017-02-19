package renderEngine;
 
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import renderEngine.loaders.Loader;
import renderEngine.materials.Material;
import renderEngine.models.Mesh;
import renderEngine.models.Model;
import renderEngine.shaders.TerrainShader;
import renderEngine.shaders.TerrainTessShader;
import renderEngine.terrains.Terrain;
import renderEngine.utils.Matrix;
import renderEngine.utils.TerrainTexture;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ARBBindlessTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import java.math.BigInteger;
 
public class TerrainTessRenderer {
 
	private static final int MAX_TERRAIN_TYPE = 50;
    private TerrainTessShader shader;
    private Matrix4f projectionMatrix;
    private int textureDispID;
    private int textureNormID;
    private int textureBlendID;
    private int textureDispSubID;
    private int textureNormSubID;
    private int diffuseArray[];
    private int normalArray[];
    private long handle[];
    private int diffuseArraySize;
    private int normalArraySize;
    private int tessLevel;
    private boolean wireframe;
    private int textureHandleBuffer;
    
    private boolean bindless;
    
    private boolean press;
    
    public TerrainTessRenderer(Loader loader,TerrainTessShader shader, int textureDispID, int textureNormID,int textureDispSubID,int textureNormSubID,Matrix4f projectionMatrix,ArrayList<TerrainTexture> textures) {
        this.shader = shader;
        this.textureDispID = textureDispID;
        this.textureBlendID = textureDispID;
        this.textureNormID = textureNormID;
        this.textureDispSubID = textureDispSubID;
        this.textureNormSubID = textureNormSubID;
        this.projectionMatrix = projectionMatrix;
        this.tessLevel = 16;
        this.wireframe = false;
        this.press = false;
        this.diffuseArray = new int[textures.size()];

        //this.bindless = Window.IsExtensionSupported("GL_ARB_bindless_texture");
        this.bindless = false;
       
        shader.start();
        this.normalArray = new int[MAX_TERRAIN_TYPE];
        this.handle = new long[textures.size()];
        diffuseArraySize=0;
        if(!bindless){
        	diffuseArray = Loader.loadTextureAtlas(textures);       
        	
        }
        else{

        	textureHandleBuffer = GL15.glGenBuffers();
    		
    		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, textureHandleBuffer);
    		GL15.glBufferData(GL31.GL_UNIFORM_BUFFER,
                     textures.size() * 64 * 2,
                     GL30.GL_MAP_WRITE_BIT);


        	
	        for(TerrainTexture t:textures){
	        	
	        	diffuseArray[diffuseArraySize] = loader.loadTexture(t.getTexture()+".png");
	        	handle[diffuseArraySize] = ARBBindlessTexture.glGetTextureHandleARB(diffuseArray[diffuseArraySize]);
	        	ARBBindlessTexture.glMakeTextureHandleResidentARB(handle[diffuseArraySize]);
	        	shader.connectBindless(handle[diffuseArraySize],diffuseArraySize);
				diffuseArraySize++;
	        }
	        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
        }
        
        GL40.glPatchParameteri(GL40.GL_PATCH_VERTICES, 4);
        
        shader.loadProjectionMatrix(projectionMatrix);
        //shader.connectTextureUnits();
        shader.stop();
    }
 
    public void render(Camera camera, Terrain terrain, Light sun, ArrayList<Vector4f> heights, Vector4f clipPlane,float distanceFog,boolean invertPitch,boolean forceLow) {
        //for (Terrain terrain : terrains) {
    		shader.start();
            prepareTerrain();
            shader.loadDmap_depth(8.5f);
            if(invertPitch)
            	camera.invertPitch();
            shader.loadViewMatrix(camera);
            shader.loadLight(sun);
            shader.loadHeights(heights);
            shader.loadTessLevel(tessLevel);
            shader.loadPlane(clipPlane);
            shader.loadforceLow(forceLow);
            shader.loadDistanceFog(distanceFog);
            loadModelMatrix();
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
            GL31.glDrawArraysInstanced(GL40.GL_PATCHES, 0, 4, 64 * 64);
            if(invertPitch)
            	camera.invertPitch();
            shader.stop(); 
            //unbindTexturedModel();
       // }
    }
 
    public void cleanUp(){
    	shader.cleanUp();
    	for(int i=0;i<diffuseArraySize;i++){
    		ARBBindlessTexture.glMakeImageHandleNonResidentARB(handle[i]);
    		GL11.glDeleteTextures(diffuseArray[i]);
    	}
    }
    
    private void prepareTerrain() {
        /*Mesh rawModel = terrain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        Material texture = terrain.getTexture();*/

    	int texture_units;
		
    	
    	
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureBlendID); // BlendMap
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureDispID); //Main displacement
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureNormID); // Man normal
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureDispSubID); // displacement sub
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureNormSubID); // normal sub
        if(!bindless){
	        for(int i=0;i<diffuseArray.length;i++){
	        	GL13.glActiveTexture(GL13.GL_TEXTURE0+i+5);
	        	GL11.glBindTexture(GL11.GL_TEXTURE_2D, diffuseArray[i]);
	        	shader.conectTextureDiff(i);
	        }
        }else{
        	 GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, 1, textureHandleBuffer);
        }
 
       /* for(int i=0;i<normalArraySize;i++){
        	GL13.glActiveTexture(GL13.GL_TEXTURE0+i+5+diffuseArraySize);
        	GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalArray[i]);
        	shader.conectTextureNorm(i,diffuseArraySize);
        }*/
        
    }
 
    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
 
    private void loadModelMatrix() {
        Matrix4f transformationMatrix = Matrix.createTransformationMatrix(
                new Vector3f(0, 0, 0), 0, 0, 0, 3);
        Matrix4f mvp = new Matrix4f();
        Matrix4f.mul(transformationMatrix, projectionMatrix, mvp);
        shader.loadMvp(mvp);
        shader.loadTransformationMatrix(transformationMatrix);
    }

	public void setDispTex(int id, int idDiffuse) {
		textureBlendID = id;
		//textureDiffID = idDiffuse;
	}

	public void setTessLevel(int tessLevel) {
		this.tessLevel = tessLevel;
		
	}
 
}