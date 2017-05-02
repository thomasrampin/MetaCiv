package renderEngine.entities;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
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
		
		/*Color color = Configuration.getAmenagementsByNameFor3D("Setlement").getColor();
		if(color.getBlue() == c.getBlue() &&color.getGreen() == c.getGreen() && color.getRed() == c.getRed()){
			this.object3d = new Object3D(settlement);
			this.c = c;
		}
		
		color = Configuration.getAmenagementsByNameFor3D("Ferme").getColor();
		//System.out.println("Ferme color: " + color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " Path color: " + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
		if(color.getBlue() == c.getBlue() &&color.getGreen() == c.getGreen() && color.getRed() == c.getRed()){
			//System.out.println("true");
			this.object3d = new Object3D(ferme);
			this.c = c;
		}*/
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
	
	
	public static void setUp(Loader loader){

		
		ArrayList<TypeAmenagement> Amenagements = Configuration.getAllAmenagementFor3D();
		for(TypeAmenagement Amenagement : Amenagements){
			
			File f = new File(Configuration.pathToRessources+"/Skin/Amenagement/" + Amenagement.getNom() + "/" + Amenagement.getNom() + ".OBJ");
			
			if(f.isFile()){
				objects3d.add(new ObjectFacility3D(new Object3D(f.getAbsolutePath(),f.getParent(), loader,true,true, new Vector3f(0, 0, 0), 0, 0, 0, 1.0f),Amenagement.getColor(),true));
				BoundingBox box = objects3d.get(objects3d.size()-1).object3d.getModels().getBox();
				float distX = box.getMax().x - box.getMin().x;
				float distY = box.getMax().y - box.getMin().y;
				float distZ = box.getMax().z - box.getMin().z;
				float distMax = Math.max(Math.max(distX, distY),distZ);
				if(distMax>5){
					objects3d.get(objects3d.size()-1).object3d.setScale(5.0f/distMax);
				}
			}else{
				objects3d.add(new ObjectFacility3D(new Object3D("Default_facility","", loader,true, new Vector3f(100, 20, 150), 0, 90, 0, 1.0f),Amenagement.getColor(),false));
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
	
	public ObjectFacility3D(Object3D object,Color color,boolean doNotOverRightColor){
		this.object3d = object;
		this.color = color;
		this.doNotOverRightColor = doNotOverRightColor;
	}
}
