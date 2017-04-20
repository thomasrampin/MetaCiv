package renderEngine;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import civilisation.CivLauncher;
import civilisation.DefineConstants;
import renderEngine.fontRendering.TextMaster;
import renderEngine.postProcessing.FrameBufferObject;
import renderEngine.postProcessing.PostProcessing;

import renderEngine.MasterRenderer;
import renderEngine.Window;
import renderEngine.entities.Camera;
import renderEngine.entities.Facility3D;
import renderEngine.entities.Light;
import renderEngine.entities.Object3D;
import renderEngine.entities.Road3D;
import renderEngine.entities.Turtle3D;
import renderEngine.loaders.Loader;

import renderEngine.sea.SeaFrameBuffers;
import renderEngine.sea.SeaRenderer;
import renderEngine.sea.SeaShader;
import renderEngine.shaders.StaticShader;
import renderEngine.shadowsMapping.Shadow;
import renderEngine.shadowsMapping.ShadowFrameBuffer;
import renderEngine.sky.SkyRenderer;
import renderEngine.terrains.Terrain;
import renderEngine.utils.FPS;
import renderEngine.utils.Helper;
import renderEngine.utils.InputHandler;
import renderEngine.utils.InputHandler.inputType;
import renderEngine.fontMeshCreator.FontType;
import renderEngine.fontMeshCreator.GUIText;
import renderEngine.utils.TerrainTexture;

import turtlekit.kernel.Turtle;




public class renderMain implements Runnable {

	private static final float SUN_ROTATE = 0.1f;
	private BufferedImage image;
	private boolean isNotify;
	
	private Vector2f gridSize;
	
	private ArrayList<Vector4f> heights;
	private ArrayList<TerrainTexture> textures;
	private ArrayList<Turtle3D> turtles;
	private ArrayList<Road3D> roads;
	private ArrayList<Debug> debugMessage;
	private FontType font;
	private GUIText text;
	private int tempId;
	private Loader loader;
	float i =0;
	private int focusCamera;
	private static float FOG_INCREASE_SPEED = 0.0001f;

	private ArrayList<Facility3D> facilitys;
	private int free = 1;
	private int refresh_time = 0;
	private boolean stopRefresh = false;
	
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
		focusCamera = -1;
		if(Window.checkGlVersion()){
			
		    boolean pressPlus = false;
		    boolean pressMinus = false;
	        boolean press = false;
	        boolean wireframe = false;
		    int tessLevel = 16;
			
		    turtles = new ArrayList<>();
		    facilitys = new ArrayList<>();
		    roads = new ArrayList<>();
		    debugMessage = new ArrayList<>();
		    
		    float sunRotate = 0f;
		    
			Camera camera = new Camera();
			loader = new Loader();
			font = new FontType(loader.loadFontTextureAtlas("arial"), new File("Assets/Font/arial.fnt"));
			StaticShader shader = new StaticShader();
			
			float distanceFog = 50.0f;
			
			FPS.start();
			
	
			
			Object3D king = new Object3D("sphere", loader, new Vector3f(150, 40, 150), 0, 0, 0, 0.25f);
			//Object3D test = new Object3D("Settlement/settlement","", loader,true, new Vector3f(100, 20, 150), 0, 90, 0, 2.0f);
	
			List<Light> lights = new ArrayList<Light>();
			Light sun = new Light(new Vector3f(-4000,4000,-2000),new Vector3f(1,1,1));

			lights.add(sun);

			Facility3D.setUp(loader);
			Turtle3D.setUp(loader);
			Road3D.setUp(loader);
			
			
			
	
			
			FrameBufferObject multisample = new FrameBufferObject(Display.getWidth(), Display.getHeight());
			ShadowFrameBuffer shadowsTexture = new ShadowFrameBuffer();
			FrameBufferObject outputFbo = new FrameBufferObject(Display.getWidth(), Display.getHeight(),FrameBufferObject.DEPTH_TEXTURE);
			FrameBufferObject colorID = new FrameBufferObject(Display.getWidth(), Display.getHeight(),FrameBufferObject.DEPTH_TEXTURE);
			PostProcessing.init(loader);
			
			
			
			MasterRenderer renderer = new MasterRenderer(loader,textures,Road3D.getRoad());
			SkyRenderer skyboxRenderer = new SkyRenderer(loader,renderer.getProjectionMatrix());
	
			//*************Water Renderer Set-up******************
			
			SeaShader waterShader = new SeaShader();
			SeaRenderer waterRenderer = new SeaRenderer(loader, waterShader,renderer.getProjectionMatrix());
	
			SeaFrameBuffers fbos = new SeaFrameBuffers();
			
			FrameBufferObject sFbo = new FrameBufferObject(Display.getWidth(), Display.getHeight(),FrameBufferObject.DEPTH_TEXTURE);
			Shadow shadow = new Shadow();
			

			Window.showInfo();
			TextMaster.init(loader);
			text = new GUIText("Ceci est un test", 200, font, new Vector3f(150, 50, 150), 200f, false);
			text.setColour(1.0f,1.0f, 10.f);
			
			int lastSize = 0;
			
			while(!Display.isCloseRequested()){
				if(lastSize!=debugMessage.size()){
					for(int i=lastSize;i<debugMessage.size();i++){
						text = new GUIText(debugMessage.get(i).msg, 200, font, new Vector3f(debugMessage.get(i).position), 200f, false);
						text.setColour(1.0f,1.0f, 10.f);
					}
				}
				
				if(refresh_time >0 && !stopRefresh)
					refresh_time  ++;
				
				lastSize = debugMessage.size();
				
				FPS.updateFPS();
				float delta = FPS.getDelta();
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
	            	
	            
	            if(focusCamera != -1)
	            	camera.setTarget(turtles.get(focusCamera).getObject3d().getPosition());
	            /*sunRotate += SUN_ROTATE ;
	            sun.setPosition(Helper.rotateAround(sunRotate, 15000, 0, new Vector3f(0,0,0)));*/
	            //System.out.println(sun.getPosition());
	    		//distanceFog += FOG_INCREASE_SPEED ;
	    		
	    		
	    		
	            /************Render for shadow********************/
	            shadow.update(sun.getPosition());
	            shadowsTexture.bindFrameBuffer();
	            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
	            renderer.render(lights, camera,image,sun,heights,new Vector4f(0,1,0,100000),distanceFog,false,false,true,shadowsTexture,fbos,shadow.getShadowMatrix(),shadow.getLightVpMatrix());
	            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);

	            
	            shadowsTexture.unbindCurrentFrameBuffer();
	           
	            
	            
	    		if(distanceFog>=10.0 || distanceFog<=3.0)
	    			FOG_INCREASE_SPEED = -FOG_INCREASE_SPEED;
	    		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
	    		
	            fbos.bindReflectionFrameBuffer();
	            
	            renderer.render(lights, camera,image,sun,heights,new Vector4f(0,1,0,0),distanceFog,true,true,false,shadowsTexture,fbos,shadow.getShadowMatrix(),shadow.getLightVpMatrix());
	            skyboxRenderer.render(camera,sun,new Vector4f(0,1,0,0),true);
				fbos.unbindCurrentFrameBuffer();
				
	            fbos.bindRefractionFrameBuffer();
	            renderer.render(lights, camera,image,sun,heights,new Vector4f(0,1,0,100000),distanceFog,false,true,false,shadowsTexture,fbos,shadow.getShadowMatrix(),shadow.getLightVpMatrix());
	            
				fbos.unbindCurrentFrameBuffer();



	

			
				if(turtles.size()>0 ){
					free=0;
					for(int i=0;i<turtles.size();i++){

		                renderer.processEntity(turtles.get(i).getObject3d(),turtles.get(i).getColorID(),turtles.get(i).getColorAction());
		            }
					free=1;
				}
				if(facilitys.size()>0){
					
					for(int i=0;i<facilitys.size();i++){
						if(facilitys.get(i).getCountDown()>=0 || facilitys.get(i).isMain()){
							renderer.processMultiEntity(facilitys.get(i).getObject3D(), 0 , facilitys.get(i).getColorToVector());
							facilitys.get(i).setSteelDraw(false);
						}
						if(!facilitys.get(i).getSteelDraw()){
							facilitys.get(i).setCountDown(facilitys.get(i).getCountDown()-1);
						}
					}
				}
				if(roads.size()>0){
					for(int i=0;i<roads.size();i++){
						renderer.processInstancedEntity(roads.get(i).getObject3d());
					}
				}
				
				//renderer.processMultiEntity(test, 0);
				renderer.processEntity(king, 0, new Vector3f(-1,-1,-1));
				//renderer.processEntity(king, 0);
				//sFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, shadowsTexture);
				if(!wireframe){
					multisample.bindFrameBuffer();
					
					renderer.render(lights, camera,image,sun,heights,new Vector4f(0,1,0,100000),distanceFog,false,false,false,shadowsTexture,fbos,shadow.getShadowMatrix(),shadow.getLightVpMatrix());
					
					waterRenderer.render( camera,sun,fbos,distanceFog,delta);
					
					skyboxRenderer.render(camera,sun,new Vector4f(0,1,0,100000),false);
					
					multisample.unbindFrameBuffer();
					multisample.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0,outputFbo);
					multisample.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1,colorID);
					PostProcessing.doPostProcessing(outputFbo.getColourTexture(),outputFbo.getDepthTexture());
				}else{
					renderer.render(lights, camera,image,sun,heights,new Vector4f(0,1,0,100000),distanceFog,false,false,false,shadowsTexture,fbos,shadow.getShadowMatrix(),shadow.getLightVpMatrix());
					
					waterRenderer.render( camera,sun,fbos,distanceFog,delta);
					
					skyboxRenderer.render(camera,sun,new Vector4f(0,1,0,100000),false);
				}

				TextMaster.render(camera);
				Window.updateDisplay();
				
				if(turtles.size()>0){
					
					if(InputHandler.reset(InputHandler.isButtonDown(0) == inputType.INSTANT)){
						for(int id=0;id<turtles.size();id++){
							
							ByteBuffer pixel = colorID.ReadPixel(Mouse.getX(), Mouse.getY());
							if(Helper.isSameColor(turtles.get(id).getColorID(), pixel)){
								focusCamera = id;
								break;
							}else{
								focusCamera = -1;
							}
						}
					}
					for(int id=0;id<turtles.size();id++){
						
						if((double)CivLauncher.sch.getDelay() <= 1.0)
							turtles.get(id).setInterpolation(1);
						else
							turtles.get(id).increaseInterpolation( (float) (1.0/((double)CivLauncher.sch.getDelay()/ (float)delta)));
							
						interpolate(Helper.clamp(turtles.get(id).getInterpolation(), 0,1),id);
					}
				}
				
			}
			
			shadow.cleanUp();
			sFbo.cleanUp();
			PostProcessing.cleanUp();
			outputFbo.cleanUp();
			multisample.cleanUp();
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

	private int containTurtle(int id){
		int iterator = 0;
		for(Turtle3D turtle:turtles){
			if(turtle.getId() == id)
				return iterator;
			iterator++;
		}
		return -1;
	}
	
	private int containFacility(int id){
		int iterator = 0;
		for(Facility3D facility:facilitys){
			if(facility.getID() == id)
				return iterator;
			iterator++;
		}
		return -1;
	}
	
	private void interpolate(float i,int id){
		

		float x ,y,z;
		
		x = Helper.mix(turtles.get(id).getLastX(), turtles.get(id).getX(), i);
		
		y = Helper.mix(turtles.get(id).getLastY(), turtles.get(id).getY(), i);

		z = Helper.mix(turtles.get(id).getLastZ(), turtles.get(id).getZ(), i);
		
		turtles.get(id).getObject3d().setPosition(new Vector3f(x,y,z));

	
	}
	
	synchronized public void paintOneTurtle(Turtle t, int x, int y, Color color, int cellSize) {
		if(free==1){
			Lock l = new ReentrantLock();
			l.lock();

			int cX = (int) ((x/(cellSize/5.0))/5);
			int cY = (int) ((y/(cellSize/5.0))/5);
			try {
				int id = containTurtle(t.getID());
				if(id == -1 && t.isPlayingRole(DefineConstants.Role_Human)){
					
					turtles.add(new Turtle3D(t.getID(),turtles.size()));
					turtles.get(turtles.size()-1).setInterpolation(0);
					turtles.get(turtles.size()-1).setColorAction(color);
					Vector3f position = Terrain.getHeightByTab(cX,cY);
					turtles.get(turtles.size()-1).setX(position.x);
					turtles.get(turtles.size()-1).setY(position.y);
					turtles.get(turtles.size()-1).setZ(position.z);
					turtles.get(turtles.size()-1).setLastX(turtles.get(turtles.size()-1).getX());
					turtles.get(turtles.size()-1).setLastY(turtles.get(turtles.size()-1).getY());
					turtles.get(turtles.size()-1).setLastZ(turtles.get(turtles.size()-1).getZ());
					
					
				}
				else {
					
					turtles.get(id).setInterpolation(0);
					turtles.get(id).setColorAction(color);
					turtles.get(id).setLastX(turtles.get(id).getX());
					turtles.get(id).setLastY(turtles.get(id).getY());
					turtles.get(id).setLastZ(turtles.get(id).getZ());
					//turtles.get(id).getObject3d().setPosition(new Vector3f((float)x,Terrain.getHeight(x%Terrain.getImageHeight(),y%Terrain.getImageWidth()) + Terrain.getHeight(x, y, image, heights),(float)y));
					Vector3f position = Terrain.getHeightByTab(cX,cY);
					turtles.get(id).setX(position.x);
					turtles.get(id).setY(position.y);
					turtles.get(id).setZ(position.z);
				}
			} finally {
			    l.unlock();
			}
		}
	}


	synchronized  public void drawFacility(int x, int y, Color color, int id, int cellSize) {
		stopRefresh  = (refresh_time>0);
		refresh_time  ++;
		
		int ID = containFacility(id);
		int cX = (int) ((x/(cellSize/5.0))/5);
		int cY = (int) ((y/(cellSize/5.0))/5);
		if(ID == -1){
			Vector3f position = Terrain.getHeightByTab(cX,cY);
			facilitys.add(new Facility3D(color,new Vector3f(position.x,position.y,position.z),id,facilitys.size()==0));
		}else{
			facilitys.get(ID).setSteelDraw(true);
		}

		
	}

	private int containRoad(int x,int y){
		int iterator = 0;
		for(Road3D road:roads){
			if(road.checkID(x, y))
				return iterator;
			iterator++;
		}
		return -1;
	}

	synchronized public void drawRoad(int x, int y,int cellSize) {
		int ID = containRoad(x,y);
		if(ID == -1){
			int cX = (int) ((x/(cellSize/5.0))/5);
			int cY = (int) ((y/(cellSize/5.0))/5);
			
			Vector3f position = Terrain.getHeightByTab(cX,cY);

			roads.add(new Road3D(position,x,y,Terrain.getAllHeightOnePoly(cX,cY)));
		}
	}


	public void renderMsg(String msg, int x, int y,int cellSize) {
		int cX = (int) ((x/(cellSize/5.0))/5);
		int cY = (int) ((y/(cellSize/5.0))/5);
		
		Vector3f position = Terrain.getHeightByTab(cX,cY);

		debugMessage.add(new Debug(position,msg));
	}
	
}

class Debug{
	public Vector3f position;
	public String msg;
	public Debug(Vector3f position,String msg){
		this.position = position;
		this.msg = msg;
	}
}
