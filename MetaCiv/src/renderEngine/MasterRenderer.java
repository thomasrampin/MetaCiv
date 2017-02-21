package renderEngine;
 
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import renderEngine.entities.Object3D;
import renderEngine.loaders.Loader;
import renderEngine.materials.Material;
import renderEngine.models.Model;
import renderEngine.shaders.StaticShader;
import renderEngine.shaders.TerrainShader;
import renderEngine.shaders.TerrainTessShader;
import renderEngine.terrains.Terrain;
import renderEngine.utils.TerrainTexture;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
 
public class MasterRenderer {
     
    public static final float FOV = 45;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 650;
     
    private Matrix4f projectionMatrix;
     
    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;
     
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();
 
    private TerrainTessRenderer terrainTessRenderer;
    private TerrainTessShader terrainTessShader = new TerrainTessShader();
     
    private Map<Model,List<Object3D>> entities = new HashMap<Model,List<Object3D>>();
    private Terrain terrain;
    
    private boolean terrainInit;
     
    public MasterRenderer(Loader loader,ArrayList<TerrainTexture> textures){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

        createProjectionMatrix();
        terrainInit = false;
        renderer = new EntityRenderer(shader,projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix,textures);
        terrainTessRenderer = new TerrainTessRenderer(loader,terrainTessShader,loader.loadTexture("heightmap.png"),loader.loadTexture("heightMap_NRM.png"),loader.loadTexture("forest/forest_DISP.png"),loader.loadTexture("forest/forest_NRM.png"),projectionMatrix,textures);
        terrain= new Terrain(0,0, loader,new Material(loader.loadTexture("grass/grass.png"),15f,new Vector3f(0,0,0),new Vector3f(1,1,1)),new ArrayList<Vector4f>());
    }
     
    public void render(List<Light> lights,Camera camera, BufferedImage image,Light sun,ArrayList<Vector4f> heights,Vector4f clipPlane,float distanceFog,boolean invertPitch,boolean forceLow){
        prepare();
        shader.start();
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();

  
            terrainShader.start();
            terrainShader.loadLights(sun);
            if(invertPitch)
            	camera.invertPitch();
            terrainShader.loadViewMatrix(camera);
            terrainShader.loadCameraPos(camera.getPosition());
            
        	terrainRenderer.render(terrain,heights,distanceFog);
        	terrainShader.stop();
            if(invertPitch)
            	camera.invertPitch();
        
       //terrainTessRenderer.render(camera,terrain,sun,heights,clipPlane,distanceFog,invertPitch,forceLow);
        
        
        entities.clear();
    }
     

     
    public void processEntity(Object3D entity){
        Model entityModel = entity.getModel();
        List<Object3D> batch = entities.get(entityModel);
        if(batch!=null){
            batch.add(entity);
        }else{
            List<Object3D> newBatch = new ArrayList<Object3D>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);        
        }
    }
     
    public void cleanUp(){
        shader.cleanUp();
        terrainShader.cleanUp();
        terrainTessRenderer.cleanUp();
    }
     
    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
    }
     
    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
 
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

	public void notifyShaderTerrain(int id, int idDiffuse) {
		terrainTessRenderer.setDispTex(id,idDiffuse);
	}

	public void notifyTerrain(Loader loader,BufferedImage image, Vector2f gridSize,ArrayList<Vector4f> heights) {
		terrain.notifyHeightMap(loader, image,gridSize,heights);
		terrainInit = true;
	}

	public Matrix4f getProjectionMatrix() {
		
		return projectionMatrix;
	}

	public void notifyTessLevel(int tessLevel) {
		terrainTessRenderer.setTessLevel(tessLevel);
		
	}
 
}
