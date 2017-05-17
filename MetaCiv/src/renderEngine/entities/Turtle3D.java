package renderEngine.entities;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import civilisation.Configuration;
import civilisation.world.World;
import renderEngine.loaders.Loader;
import renderEngine.utils.BoundingBox;
import renderEngine.utils.Helper;
import turtlekit.kernel.Turtle;

public class Turtle3D {

	private float x,y,z;
	private float lastX=100,lastY=-1,lastZ=100;
	private Object3D object3d;
	private static ArrayList<Object3D> objects3ds = new ArrayList<>();
	
	private int id;
	private int colorID;
	private Vector3f colorAction;
	private float interpolation;
	private Turtle t;
	
	public Turtle3D(int id,int card, Turtle t){
		for (int i = 0; i < Configuration.civilisations.size(); i++){
			if(t.getColor().equals(Configuration.civilisations.get(i).getCouleur()))
				this.object3d = new Object3D(objects3ds.get(i));
		}
		
		this.id = id;
		this.colorID = (255*255*255)-(id+1);
		
		//this.object3d.getModels().setColorID(Helper.IntegerToColor(colorID));//+1 avoid black
		this.interpolation = 0;
		colorAction = new Vector3f(-1,-1,-1);
		this.t = t;
		
	}
	
	public Turtle getTurlte(){
		return t;
	}
	
	public static void setUp(Loader loader){
		for (int i = 0; i < Configuration.civilisations.size(); i++){
			File f = new File(Configuration.pathToRessources+"/Skin/Civilisations/" + Configuration.civilisations.get(i).getNom() + "/" + Configuration.civilisations.get(i).getNom() + ".OBJ");
			
			if(f.isFile()){
				objects3ds.add(new Object3D(f.getAbsolutePath(),f.getParent(), loader,true,true, new Vector3f(0, 0, 0), 0, 0, 0, 1.0f));
				BoundingBox box = objects3ds.get(objects3ds.size()-1).getModels().getBox();
				float distX = box.getMax().x - box.getMin().x;
				float distY = box.getMax().y - box.getMin().y;
				float distZ = box.getMax().z - box.getMin().z;
				float distMax = Math.max(Math.max(distX, distY),distZ);
				if(distMax>World.getSize3D()){
					objects3ds.get(objects3ds.size()-1).setScale(World.getSize3D()/distMax);
				}
			}else{
				objects3ds.add(new Object3D("turtle","", loader,true,new Vector3f(150,8,150),0,0,0,0.03f));
			}
		}
		
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public float getLastX() {
		return lastX;
	}
	public void setLastX(float lastX) {
		this.lastX = lastX;
	}
	public float getLastZ() {
		return lastZ;
	}
	public void setLastZ(float lastZ) {
		this.lastZ = lastZ;
	}
	public Object3D getObject3d() {
		return object3d;
	}
	public void setObject3d(Object3D object3d) {
		this.object3d = object3d;
	}

	public int getId() {
		return id;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getLastY() {
		return lastY;
	}

	public void setLastY(float lastY) {
		this.lastY = lastY;
	}

	public float getInterpolation() {
		return interpolation;
	}

	public void setInterpolation(float interpolation) {
		this.interpolation = interpolation;
	}
	
	public void increaseInterpolation(float increase){
		this.interpolation += increase;
	}
	
	public int getColorID(){
		return colorID;
	}

	public Vector3f getColorAction() {
		return new Vector3f(colorAction);
	}

	public void setColorAction(Color colorAction) {
		this.colorAction.x = colorAction.getRed()/255.0f;
		this.colorAction.y = colorAction.getGreen()/255.0f;
		this.colorAction.z = colorAction.getBlue()/255.0f;
	}
	
	
	
}

class ObjectTurtle3D {
	public Object3D object3d;
	public Color color;

	
	public ObjectTurtle3D(Object3D object,Color color,boolean doNotOverRightColor){
		this.object3d = object;
		this.color = color;

	}
}
