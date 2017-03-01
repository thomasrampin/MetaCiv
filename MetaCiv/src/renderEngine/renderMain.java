package renderEngine;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import civilisation.DefineConstants;
import renderEngine.postProcessing.FrameBufferObject;
import renderEngine.postProcessing.PostProcessing;
import renderEngine.EntityRenderer;
import renderEngine.MasterRenderer;
import renderEngine.Window;
import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import renderEngine.entities.Object3D;
import renderEngine.entities.Turtle3D;
import renderEngine.loaders.Loader;
import renderEngine.materials.Material;
import renderEngine.models.Model;
import renderEngine.sea.SeaFrameBuffers;
import renderEngine.sea.SeaRenderer;
import renderEngine.sea.SeaShader;
import renderEngine.shaders.StaticShader;
import renderEngine.sky.SkyRenderer;
import renderEngine.terrains.Terrain;
import renderEngine.utils.FPS;
import renderEngine.utils.MousePicker;
import renderEngine.utils.TerrainTexture;
import turtlekit.kernel.Turtle;



public class renderMain implements Runnable {

	private static final float SUN_ROTATE = 0.1f;
	private BufferedImage image;
	private boolean isNotify;
	
	private Vector2f gridSize;
	
	private ArrayList<Vector4f> heights;
	private ArrayList<TerrainTexture> textures;
	private Object3D king;
	private List<Object3D> objects;
	private ArrayList<Turtle3D> turtles;
	private int tempId;
	private Loader loader;
	
	private static float FOG_INCREASE_SPEED = 0.0001f;
	
	public renderMain(BufferedImage bufferedView) {
		this.image = bufferedView;
		isNotify = false;
		gridSize = new Vector2f();
		heights = new ArrayList<>();
		heights.add(new Vector4f(0.16666667f,1.0f,0.4f, 4f)); //collines
		heights.add(new Vector4f(0.16666667f,0.4f,1.0f, 1f)); //desert profond
		heights.add(new Vector4f(0.16666667f,0.2f,1.0f, 1f)); //desert
		heights.add(new Vector4f(0.33333334f,1.0f,0.4f, 1.5f)); //foret
		heights.add(new Vector4f(0.5333333f,1.0f,1.0f,0.8f)); //litorral
		heights.add(new Vector4f(0.6111111f,1.0f,0.6f,0f)); //mer
		heights.add(new Vector4f(0.0f,0.0f,0.4f,6f)); //montagne
		heights.add(new Vector4f(0.16666667f,1.0f,0.6f,1.3f)); //plaine aride
		heights.add(new Vector4f(0.38888887f,1.0f,0.6f,1.3f)); //prairie
		
		textures = new ArrayList<>();
		textures.add(new TerrainTexture(0.16666667f,1.0f,0.4f, "roche/roche", "roche/roche_NRM","roche/roche_DISP")); //collines
		textures.add(new TerrainTexture(0.16666667f,0.4f,1.0f,"sand/sand","sand/sand_NRM","sand/sand_DISP")); //desert profond
		textures.add(new TerrainTexture(0.16666667f,0.2f,1.0f, "sand/sand","sand/sand_NRM","sand/sand_DISP")); //desert
		textures.add(new TerrainTexture(0.33333334f,1.0f,0.4f, "forest/forest", "forest/forest_NRM","forest/forest_DISP")); //foret
		textures.add(new TerrainTexture(0.5333333f,1.0f,1.0f,"sand/sand","sand/sand_NRM","sand/sand_DISP")); //litorral
		textures.add(new TerrainTexture(0.6111111f,1.0f,0.6f,"sand/sand","sand/sand_NRM","sand/sand_DISP")); //mer
		textures.add(new TerrainTexture(0.0f,0.0f,0.4f,"roche/roche","roche/roche_NRM","roche/roche_DISP")); //montagne
		textures.add(new TerrainTexture(0.16666667f,1.0f,0.6f,"grass/grass","grass/grass_NRM","grass/grass_DISP")); //plaine aride
		textures.add(new TerrainTexture(0.38888887f,1.0f,0.6f,"grass/grass","grass/grass_NRM","grass/grass_DISP")); //prairie
	}


	@Override
	public void run() {

		Window.createDislay();
		tempId = -1;
		if(Window.checkGlVersion()){
		    boolean pressPlus = false;
		    boolean pressMinus = false;
		    int tessLevel = 16;
			
		    turtles = new ArrayList<>();
		    
		    float sunRotate = 0f;
		    
			Camera camera = new Camera();
			loader = new Loader();
			StaticShader shader = new StaticShader();
			
			float distanceFog = 50.0f;
			
			FPS.start();
			
	
			
			king = new Object3D("1","king", loader,new Vector3f(150,8,150),0,0,0,0.03f);
			/*Object3D plateau = new Object3D("plateau","plateau",loader, new Vector3f(0,0,0),0,0,0,0.1f);
			Object3D king2 = new Object3D("3","king_tex",loader, new Vector3f(0,0,20),0,0,0,0.1f);*/
	
			List<Light> lights = new ArrayList<Light>();
			Light sun = new Light(new Vector3f(-4000,1000,-2000),new Vector3f(1,1,1));
			Light light = new Light(new Vector3f(-20,10,10),new Vector3f(1,1,1));
			Light light2 = new Light(new Vector3f(20,10,-10),new Vector3f(0,1,1));
			Light light3 = new Light(new Vector3f(80,20,-100),new Vector3f(1,0,1));
			Light light4 = new Light(new Vector3f(50,10,80),new Vector3f(1,1,0));
			lights.add(sun);
			/*lights.add(light);
			lights.add(light2);
			lights.add(light3);
			lights.add(light4);*/
			
		
			
			objects = new ArrayList<Object3D>();
			objects.add(king);
			//objects.add(king2);
			
			//MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix(),plateau);
			
			FrameBufferObject fbo = new FrameBufferObject(Display.getWidth(), Display.getHeight(),FrameBufferObject.DEPTH_RENDER_BUFFER);
			PostProcessing.init(loader);
			
			
			
			MasterRenderer renderer = new MasterRenderer(loader,textures);
			SkyRenderer skyboxRenderer = new SkyRenderer(loader,renderer.getProjectionMatrix());
	
			//*************Water Renderer Set-up******************
			
			SeaShader waterShader = new SeaShader();
			SeaRenderer waterRenderer = new SeaRenderer(loader, waterShader,renderer.getProjectionMatrix());
	
			SeaFrameBuffers fbos = new SeaFrameBuffers();
			
			turtles.add(new Turtle3D(loader));
			Window.showInfo();
			
			while(!Display.isCloseRequested()){
				FPS.updateFPS();
				
				/*Notify Terrain Shader handle*/
				if(isNotify){
					int id = Loader.loadTexture(image);
					
					//int idDiffuse = Loader.loadMultiTexture(image, textures);
					int idDiffuse = 0;
					renderer.notifyShaderTerrain(id,idDiffuse);
					renderer.notifyTerrain(loader, image,gridSize,heights);
					
					isNotify = false;
				}
				
				//entity.increaseRotation(1, 1, 0);
				camera.move();
				//terrain.updateChunk(loader,-30000,50,50);
				//picker.update();
				//Vector3f point = picker.getCurrentObjectPoint();
				/*if(point!=null){
					king.setPosition(point);
				}*/
	
				/*buttonA.update();
				buttonB.update();*/
				
	       
				if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2  ) && !pressPlus && tessLevel<16){
	            	
					tessLevel ++;
					renderer.notifyTessLevel(tessLevel);
					waterRenderer.notifyTessLevel(tessLevel);
					pressPlus = true;
	            }
	            if(!Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2))
	            	pressPlus = false;
	            
				if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1) && !pressMinus && tessLevel>1){
	            	
					tessLevel --;
					renderer.notifyTessLevel(tessLevel);
					waterRenderer.notifyTessLevel(tessLevel);
					pressMinus = true;
	            }
	            if(!Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1))
	            	pressMinus = false;			
	            

	            camera.setTarget(objects.get(0).getPosition());
	            /*sunRotate += SUN_ROTATE ;
	            sun.setPosition(Helper.rotateAround(sunRotate, 15000, 0, new Vector3f(0,0,0)));
	            System.out.println(sun.getPosition());*/
	    		//distanceFog += FOG_INCREASE_SPEED ;
	    		
	    		
	    		
	    		if(distanceFog>=10.0 || distanceFog<=3.0)
	    			FOG_INCREASE_SPEED = -FOG_INCREASE_SPEED;
	    		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
	    		
	            fbos.bindReflectionFrameBuffer();
	            
	            renderer.render(lights, camera,image,sun,heights,new Vector4f(0,1,0,0),distanceFog,true,true);
	            skyboxRenderer.render(camera,sun,new Vector4f(0,1,0,0),true);
				fbos.unbindCurrentFrameBuffer();
				if(turtles.size()>0)
		            for(Turtle3D entity:turtles){
		                renderer.processEntity(entity.getObject3d());
		            }
				fbo.bindFrameBuffer();
				renderer.render(lights, camera,image,sun,heights,new Vector4f(0,1,0,100000),distanceFog,false,false);
				
				waterRenderer.render( camera,sun,fbos,distanceFog);
				
				skyboxRenderer.render(camera,sun,new Vector4f(0,1,0,100000),false);
				fbo.unbindFrameBuffer();
				
				PostProcessing.doPostProcessing(fbo.getColourTexture());
				Window.updateDisplay();
				
			}
			
			PostProcessing.cleanUp();
			fbo.cleanUp();
			renderer.cleanUp();
			shader.cleanUp();
			loader.cleanUp();
		}
		
		Window.closeDisplay();
		
	}
	
	public void notifyShaderTerrain(int i, int j){
		gridSize = new Vector2f(i,j);
		isNotify = true;
	}
	
	public void setBufferedImage(BufferedImage bI){
		image = bI;
	}


	public void paintOneTurtle(Turtle t, int x, int y) {
		//objects.add(king);
		//objects.get(objects.size()-1).setPosition(new Vector3f((float)x/10.0f,0.0f,(float)y/10.0f));
		if(tempId == -1 && t.isPlayingRole(DefineConstants.Role_Human)){
			tempId = t.getID();
			
		}
		if(t.getID() == tempId){
			
			turtles.get(0).getObject3d().setPosition(new Vector3f((float)x,Terrain.getHeight(x%Terrain.getImageHeight(),y%Terrain.getImageWidth()) + Terrain.getHeight(x, y, image, heights),(float)y));
		}
	}
	
}
