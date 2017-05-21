package renderEngine;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import civilisation.CivLauncher;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.amenagement.Amenagement;
import civilisation.world.World;
import civilisation.world.WorldViewer;
import renderEngine.gui.ActionsMenuWorld3D;
import renderEngine.gui.Drawable;
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
import renderEngine.sea.SeaTessShader;
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
import turtlekit.kernel.Patch;
import turtlekit.kernel.Turtle;




public class renderMain implements Runnable {

	private static final float SUN_ROTATE = 0.1f;
	private BufferedImage image;
	private static boolean isNotify;
	
	private Vector2f gridSize;
	

	private static ArrayList<TerrainTexture> textures;
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
	
	private int refresh_time = 0;
	private boolean stopRefresh = false;
	private boolean menuActionIsVisible = false;
	private WorldViewer worldViewer;
	final Patch[] grid;
	public boolean init = true;
	
	public static int wBuffer;
	public static int hBuffer;
	
	public renderMain(BufferedImage bufferedView, WorldViewer worldViewer, Patch[] patchs) {
		grid = patchs;
		this.image = bufferedView;
		isNotify = false;
		gridSize = new Vector2f();

		
		textures = new ArrayList<>();
		for(civilisation.world.Terrain terrain: Configuration.terrains){
			TerrainTexture type = new TerrainTexture(terrain.getCouleur(),terrain.getHeight(),terrain.getErosion(),terrain.getBlur(),terrain.getTiling(),terrain.getTexture());
			textures.add(type);
		}
		this.worldViewer = worldViewer;
	}


	@Override
	public void run() {
		java.awt.Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int)dimension.getHeight();
		int width = (int)dimension.getWidth();
		wBuffer = width;
		hBuffer = height-8;
		try {
			Window.createDislay();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Window.showInfo();
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
			
	
			List<Light> lights = new ArrayList<Light>();
			Light sun = new Light(new Vector3f(-4000,4000,-2000),new Vector3f(1,1,1));

			lights.add(sun);

			Facility3D.setUp(loader);
			Turtle3D.setUp(loader);

			
			
			
	
			
			FrameBufferObject multisample = new FrameBufferObject(width, height);
			ShadowFrameBuffer shadowsTexture = new ShadowFrameBuffer();
			FrameBufferObject outputFbo = new FrameBufferObject(width, height,FrameBufferObject.DEPTH_TEXTURE);
			FrameBufferObject colorID = new FrameBufferObject(width, height,FrameBufferObject.DEPTH_TEXTURE);
			PostProcessing.init(loader,width, height);
			
			
			
			MasterRenderer renderer = new MasterRenderer(loader,textures,Road3D.getRoad());
			SkyRenderer skyboxRenderer = new SkyRenderer(loader,renderer.getProjectionMatrix());
	
			//*************Water Renderer Set-up******************
			
			
			SeaRenderer waterRenderer = new SeaRenderer(loader,renderer.getProjectionMatrix());
	
			SeaFrameBuffers fbos = new SeaFrameBuffers();
			
			
			Shadow shadow = new Shadow();
			

			
			TextMaster.init(loader);
			ActionsMenuWorld3D buttonObserve = new ActionsMenuWorld3D(new Vector3f(0,0,0),new Vector2f(Display.getWidth()-200,50), new Vector2f(180,32),"Observe one Agent",font ,1,worldViewer);
			ActionsMenuWorld3D buttonDebug = new ActionsMenuWorld3D(new Vector3f(0,0,0),new Vector2f(Display.getWidth()-200,50+32+5), new Vector2f(180,32),"Switch Debug String",font,2,worldViewer);
			TextMaster.switchButton("Observe one Agent");
			TextMaster.switchButton("Switch Debug String");
			Drawable drawable = new Drawable(loader);
			drawable.addPanel(buttonObserve.getTexture());
			drawable.addPanel(buttonDebug.getTexture());
			
			
			
			
			while(!Display.isCloseRequested()){
				if(!menuActionIsVisible && InputHandler.reset(InputHandler.isButtonDown(1) == inputType.INSTANT)){
					menuActionIsVisible = true;
					TextMaster.switchButton("Observe one Agent");
					TextMaster.switchButton("Switch Debug String");
				}else if(menuActionIsVisible && Mouse.isButtonDown(0) && !buttonObserve.isHover() && !buttonDebug.isHover()){
					menuActionIsVisible = false;
					TextMaster.switchButton("Observe one Agent");
					TextMaster.switchButton("Switch Debug String");
				}
				if(turtles.size()>0)
					buttonObserve.setSelectedAgent(turtles.get((focusCamera!=-1)?focusCamera:0).getTurlte());
				if(menuActionIsVisible){
					buttonObserve.update();
					buttonDebug.update();
				}
				
				if(!worldViewer.activDebug){
					debugMessage.clear();
					TextMaster.resetDebugString();
				}
				
				for(int i=0;i<debugMessage.size();i++){
					if(debugMessage.get(i).modify){
						TextMaster.checkId(debugMessage.get(i).id,debugMessage.get(i).msg,debugMessage.get(i).position,font);
						debugMessage.get(i).modify = false;
					}
						
				}
				
				
				if(refresh_time >0 && !stopRefresh)
					refresh_time  ++;
				
		
				
				FPS.updateFPS();
				float delta = FPS.getDelta();
				/*Notify Terrain Shader handle*/
				if(isNotify){
					int id = Loader.loadTexture(image);
					
					int idDiffuse = 0;
					//renderer.notifyShaderTerrain(id,idDiffuse);
					renderer.notifyTerrain(loader, image,gridSize,textures);
					
					isNotify = false;
				}

				Loader.updateData(image);

				camera.move();

				/*Change resolution*/
				if(Keyboard.isKeyDown(Keyboard.KEY_F1 )){//High
					wBuffer = width;
					hBuffer = height-8;
					renderer.createProjectionMatrix();
					multisample = new FrameBufferObject(width, height);
					
					outputFbo = new FrameBufferObject(width,height,FrameBufferObject.DEPTH_TEXTURE);
					colorID = new FrameBufferObject(width, height,FrameBufferObject.DEPTH_TEXTURE);
					PostProcessing.update(width,height);
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_F2 )){//Medium
					wBuffer = (int) (width/1.5);
					hBuffer = (int) (height/1.5-8);
					renderer.createProjectionMatrix();
					multisample = new FrameBufferObject((int)(width/1.5), (int)(height/1.5));
					
					outputFbo = new FrameBufferObject((int)(width/1.5),(int)(height/1.5),FrameBufferObject.DEPTH_TEXTURE);
					colorID = new FrameBufferObject((int)(width/1.5), (int)(height/1.5),FrameBufferObject.DEPTH_TEXTURE);
					PostProcessing.update((int)(width/1.5),(int)(height/1.5));
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_F3 )){//Low
					wBuffer = width/3;
					hBuffer = height/3-8;
					renderer.createProjectionMatrix();
					multisample = new FrameBufferObject(width/3, height/3);
					
					outputFbo = new FrameBufferObject(width/3,height/3,FrameBufferObject.DEPTH_TEXTURE);
					colorID = new FrameBufferObject(width/3, height/3,FrameBufferObject.DEPTH_TEXTURE);
					PostProcessing.update(width/3,height/3);
				}
				
				if((Keyboard.isKeyDown(56) ||Keyboard.isKeyDown(184)) && Keyboard.isKeyDown(28) && !pressPlus){//Switch fullScreen
					
					if(Display.getWidth() == width ){
						try {
							Display.setDisplayMode(new DisplayMode((int)(width/1.5),(int)(height/1.5)));
						} catch (LWJGLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						try {
							Display.setDisplayMode(new DisplayMode(width,height));
						} catch (LWJGLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					pressPlus = true;
				}else{
					pressPlus = false;
				}
				
				if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4  )){
					CivLauncher.sch.setDelay(CivLauncher.sch.getDelay()+10);
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5  )){
					CivLauncher.sch.setDelay(CivLauncher.sch.getDelay()-10);
				}

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
	            	
	            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
	            	if(turtles.size()>0 ){
	            		focusCamera = 0;
						camera.setDistance();
	            	}
	            }
	            
	            if(focusCamera != -1)
	            	camera.setTarget(turtles.get(focusCamera).getObject3d().getPosition());
	            /*sunRotate += SUN_ROTATE ;
	            sun.setPosition(Helper.rotateAround(sunRotate, 15000, 0, new Vector3f(0,0,0)));*/
	            //System.out.println(sun.getPosition());
	    		//distanceFog += FOG_INCREASE_SPEED ;
	    		
	    		
	    		

	           
	            
	            
	    		if(distanceFog>=10.0 || distanceFog<=3.0)
	    			FOG_INCREASE_SPEED = -FOG_INCREASE_SPEED;
	    		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
	    		
	    		
	    		/*Sea Pass*/
	    		if(World.getSea()!=0){
		            fbos.bindReflectionFrameBuffer();
		            
		            renderer.render(lights, camera,image,sun,textures,new Vector4f(0,1,0,0),distanceFog,true,true,false,shadowsTexture,fbos,shadow.getShadowMatrix(),shadow.getLightVpMatrix(),false);
		            skyboxRenderer.render(camera,sun,new Vector4f(0,1,0,0),true);
					fbos.unbindCurrentFrameBuffer();
					
		            fbos.bindRefractionFrameBuffer();
		            renderer.render(lights, camera,image,sun,textures,new Vector4f(0,1,0,100000),distanceFog,false,true,false,shadowsTexture,fbos,shadow.getShadowMatrix(),shadow.getLightVpMatrix(),false);
		            
					fbos.unbindCurrentFrameBuffer();
	    		}


	

			
				if(turtles.size()>0 ){
					
					for(int i=0;i<turtles.size();i++){
						
						if(turtles.get(i).getTurlte().isAlive()){
							renderer.processMultiEntity(turtles.get(i).getObject3d(),turtles.get(i).getColorID(),turtles.get(i).getColorAction());
						}
		            }
					
				}
				if(facilitys.size()>0){
					
					for(int i=0;i<facilitys.size();i++){
						if(facilitys.get(i).isMain() || facilitys.get(i).getA().isAlive() ){
							renderer.processMultiEntity(facilitys.get(i).getObject3D(), 0 , facilitys.get(i).getColorToVector());
						}
						
					}
				}

				

				if(!wireframe){
					multisample.bindFrameBuffer();
					
					renderer.render(lights, camera,image,sun,textures,new Vector4f(0,1,0,100000),distanceFog,false,false,false,shadowsTexture,fbos,shadow.getShadowMatrix(),shadow.getLightVpMatrix(),true);
					
					if(World.getSea()!=0)
						waterRenderer.render( camera,sun,fbos,distanceFog,delta);
					
					skyboxRenderer.render(camera,sun,new Vector4f(0,1,0,100000),false);
					
					multisample.unbindFrameBuffer();
					multisample.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0,outputFbo);
					multisample.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1,colorID);
					PostProcessing.doPostProcessing(outputFbo.getColourTexture(),outputFbo.getDepthTexture());
				}else{
					renderer.render(lights, camera,image,sun,textures,new Vector4f(0,1,0,100000),distanceFog,false,false,false,shadowsTexture,fbos,shadow.getShadowMatrix(),shadow.getLightVpMatrix(),true);
					if(World.getSea()!=0)
						waterRenderer.render( camera,sun,fbos,distanceFog,delta);
					
					skyboxRenderer.render(camera,sun,new Vector4f(0,1,0,100000),false);
				}
				
				if(menuActionIsVisible)
					drawable.draw();
				TextMaster.render(camera);
				Window.updateDisplay();
				
				if(turtles.size()>0){
					
					if(InputHandler.reset(InputHandler.isButtonDown(0) == inputType.INSTANT)){
						for(int id=0;id<turtles.size();id++){
							
							ByteBuffer pixel = colorID.ReadPixel((int)(Mouse.getX()/(float)(Display.getWidth()/(float)wBuffer)), (int)(Mouse.getY()/(float)(Display.getHeight()/(float)hBuffer)));
							if(Helper.isSameColor(turtles.get(id).getColorID(), pixel)){
								focusCamera = id;
								camera.setDistance();
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
			
			drawable.cleanUp();
			shadow.cleanUp();
		
			PostProcessing.cleanUp();
			outputFbo.cleanUp();
			multisample.cleanUp();
			renderer.cleanUp();
			shader.cleanUp();
			loader.cleanUp();
		}
		
		Window.closeDisplay();
		
	}
	

	
	public static void updateTerrain(){
		isNotify = true;
		textures = new ArrayList<>();
		for(civilisation.world.Terrain terrain: Configuration.terrains){
			TerrainTexture type = new TerrainTexture(terrain.getCouleur(),terrain.getHeight(),terrain.getErosion(),terrain.getBlur(),terrain.getTiling(),terrain.getTexture());
			textures.add(type);
		}
		
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
		
		Lock l = new ReentrantLock();
		l.lock();

		int cX = (int) ((x/(cellSize/(float)WorldViewer.initialCellSize))/World.getAccuracy());
		int cY = (int) ((y/(cellSize/(float)WorldViewer.initialCellSize))/World.getAccuracy());
		try {
			int id = containTurtle(t.getID());
			if(id == -1 && t.isPlayingRole(DefineConstants.Role_Human)){
				
				turtles.add(new Turtle3D(t.getID(),turtles.size(),t));
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

	synchronized public void initMainFacilitys(){
		init = false;
		for (int i = 0; i < Configuration.civilisations.size(); i++){
			for(int j=0;j<grid.length;j++){
				if(grid[j].x == Configuration.civilisations.get(i).getStartX() && grid[j].y == Configuration.civilisations.get(i).getStartY()){
					
					int cX = (int) ((grid[j].x*5)/World.getAccuracy());
					int cY = (int) ((grid[j].y*5)/World.getAccuracy());

					Vector3f position = Terrain.getHeightByTab(cX,cY);
					facilitys.add(new Facility3D(new Vector3f(position.x,position.y,position.z),-1,true));
					//drawFacility(grid[j].x,grid[j].y,( (Amenagement) grid[j].getTurtles().get(0)).getColorType(),grid[j].getTurtles().get(0).getID(),WorldViewer.initialCellSize,( (Amenagement) grid[j].getTurtles().get(0)),true);
					break;
				}
			}
		}
	}
	
	synchronized  public void drawFacility(int x, int y, Color color, int id, int cellSize, Amenagement a, boolean Capitale) {
		int i=-1;
		int civPos[] = new int[Configuration.civilisations.size()];
		boolean found = false;
		if(init){
			init = false;
			
			for(int j=0;j<grid.length;j++){
				for (i = 0; i < Configuration.civilisations.size(); i++){
					
					if(grid[j].x == Configuration.civilisations.get(i).getStartX() && grid[j].y == Configuration.civilisations.get(i).getStartY()){
						
						
						/*Vector3f position = Terrain.getHeightByTab(cX,cY);
						facilitys.add(new Facility3D( ( (Amenagement) grid[j].getTurtles().get(0)).getColorType(),new Vector3f(0,0,0),grid[j].getTurtles().get(0).getID(),true,( (Amenagement) grid[j].getTurtles().get(0))));
						*///drawFacility(grid[j].x,grid[j].y,( (Amenagement) grid[j].getTurtles().get(0)).getColorType(),grid[j].getTurtles().get(0).getID(),WorldViewer.initialCellSize,( (Amenagement) grid[j].getTurtles().get(0)),true);
						found = true;
						civPos[i] = j;
						break;
					}
				}
				
			}
			
			
		}
		
		/*if(found){
			for (i = 0; i < Configuration.civilisations.size(); i++){
				int cX = (int) (((grid[civPos[i]].x*5))/World.getAccuracy());
				int cY = (int) (((grid[civPos[i]].y*5))/World.getAccuracy());
				
				Vector3f position = Terrain.getHeightByTab(cX,cY);
				facilitys.add(new Facility3D(color,new Vector3f(position.x,position.y,position.z),-1,true,a));
			}
		}*/
		int ID = containFacility(id);
		int cX = (int) ((x/(cellSize/(float)WorldViewer.initialCellSize))/World.getAccuracy());
		int cY = (int) ((y/(cellSize/(float)WorldViewer.initialCellSize))/World.getAccuracy());
		if(ID == -1){
			Vector3f position = Terrain.getHeightByTab(cX,cY);

			
			facilitys.add(new Facility3D(color,new Vector3f(position.x,position.y,position.z),id,Capitale,a));

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


	private int containMsg(int id){
		int iterator = 0;
		for(Debug d:debugMessage){
			if(d.id == id)
				return iterator;
			iterator++;
		}
		return -1;
	}

	public void renderMsg(String msg, int x, int y,int cellSize, int id) {
		int cX = (int) ((x/(cellSize/(float)WorldViewer.initialCellSize))/World.getAccuracy());
		int cY = (int) ((y/(cellSize/(float)WorldViewer.initialCellSize))/World.getAccuracy());
		
		Vector3f position = Terrain.getHeightByTab(cX,cY);
		position.y += 5;
		int ID = containMsg(id);
		if(ID == -1){
			
			debugMessage.add(new Debug(position,msg,id,true));
		}
		else{
			debugMessage.get(ID).position = new Vector3f(position);
			debugMessage.get(ID).msg = msg;
			debugMessage.get(ID).modify = true;
		}
			
	}
	
}

class Debug{
	public boolean modify;
	public Vector3f position;
	public String msg;
	public int id;
	public Debug(Vector3f position,String msg,int id,boolean modify){
		this.position = position;
		this.msg = msg;
		this.id = id;
		this.modify = modify;
	}
}
