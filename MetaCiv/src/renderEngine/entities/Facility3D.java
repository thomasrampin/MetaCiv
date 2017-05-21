package renderEngine.entities;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.world.World;
import renderEngine.loaders.Loader;
import renderEngine.utils.BoundingBox;
import renderEngine.utils.Helper;




public class Facility3D {
	
	private static final int INTERVAL = 10;


	private static ArrayList<ObjectFacility3D> objects3d = new ArrayList<>();
	private Object3D object3d;
	private Color c;//Amenagement type
	private int ID;
	private boolean doNotOverRightColor;
	private boolean steelDraw;
	private int countDown;
	private boolean main;
	private Amenagement a;
	
	public Facility3D(Color c, Vector3f position,int ID,boolean main, Amenagement a){
		
		this.a = a;
		for(ObjectFacility3D object:objects3d){
			if(c.equals(object.color)){
				this.object3d = new Object3D(object.object3d);
				this.c = c;
				this.doNotOverRightColor = object.doNotOverRightColor;
			}
		}
		this.main = main;
		this.steelDraw = true;
		this.countDown = INTERVAL;
		this.object3d.setPosition(position);
		this.ID=ID;
	}
	
	
	public Facility3D(Vector3f position,int ID,boolean main){
		
		
		for(ObjectFacility3D object:objects3d){
			if(object.name.equals("Setlement")){
				this.object3d = new Object3D(object.object3d);
				this.c = object.color;
				this.doNotOverRightColor = object.doNotOverRightColor;
			}
		}
		this.main = main;
		this.steelDraw = true;
		this.countDown = INTERVAL;
		this.object3d.setPosition(position);
		this.ID=ID;
	}
	
	
	public static void setUp(Loader loader){

		
		ArrayList<TypeAmenagement> Amenagements = Configuration.getAllAmenagementFor3D();
		for(TypeAmenagement Amenagement : Amenagements){
			
			File f = new File(Configuration.pathToRessources+"/Skin/Amenagement/" + Amenagement.getNom() + "/" + Amenagement.getNom() + ".obj");
			
			if(f.isFile()){
				objects3d.add(new ObjectFacility3D(Amenagement.getNom(),new Object3D(f.getAbsolutePath(),f.getParent(), loader,true,true, new Vector3f(0, 0, 0), 0, 0, 0, 1.0f),Amenagement.getColor(),true));
				BoundingBox box = objects3d.get(objects3d.size()-1).object3d.getModels().getBox();
				float distX = box.getMax().x - box.getMin().x;
				float distY = box.getMax().y - box.getMin().y;
				float distZ = box.getMax().z - box.getMin().z;
				float distMax = Math.max(Math.max(distX, distY),distZ);
				if(distMax>World.getSize3D()){
					objects3d.get(objects3d.size()-1).object3d.setScale(World.getSize3D()/distMax);
				}
			}else{
				objects3d.add(new ObjectFacility3D(Amenagement.getNom(),new Object3D("Default_facility","", loader,true, new Vector3f(100, 20, 150), 0, 90, 0, 1.0f),Amenagement.getColor(),false));
			}
		}
		
	}


	public Object3D getObject3D() {
		return object3d;
	}
	
	public int getID(){
		return ID;
	}

	public Vector3f getColorToVector(){
		if(!doNotOverRightColor)
			return new Vector3f(c.getRed()/255.0f,c.getGreen()/255.0f,c.getBlue()/255.0f);
		else
			return new Vector3f(-1,-1,-1);
	}


	public void setSteelDraw(boolean b) {
		steelDraw = b;
		if(b)
			countDown=INTERVAL;
	}
	
	public int getCountDown() {
		return countDown;
	}


	public void setCountDown(int countDown) {
		this.countDown = countDown;
	}


	public boolean getSteelDraw(){
		return steelDraw;
	}
	
	public boolean isMain(){
		return main;
	}


	public Amenagement getA() {
		return a;
	}


	public void setA(Amenagement a) {
		this.a = a;
	}
	
	
	
}

class ObjectFacility3D {
	public Object3D object3d;
	public Color color;
	public boolean doNotOverRightColor;
	public String name;
	
	public ObjectFacility3D(String name, Object3D object,Color color,boolean doNotOverRightColor){
		this.name = name;
		this.object3d = object;
		this.color = color;
		this.doNotOverRightColor = doNotOverRightColor;
	}
}
