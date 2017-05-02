package renderEngine.entities;

import java.awt.Color;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.loaders.Loader;
import renderEngine.utils.Helper;
import turtlekit.kernel.Turtle;

public class Turtle3D {

	private float x,y,z;
	private float lastX=100,lastY=-1,lastZ=100;
	private Object3D object3d;
	private static Object3D king;
	private static Object3D basic;
	private int id;
	private int colorID;
	private Vector3f colorAction;
	private float interpolation;
	private Turtle t;
	
	public Turtle3D(int id,int card, Turtle t){

		this.object3d = new Object3D(basic);
		this.id = id;
		this.colorID = (255*255*255)-(id+1);

		this.object3d.getModel().setColorID(Helper.IntegerToColor(colorID));//+1 avoid black
		this.interpolation = 0;
		colorAction = new Vector3f(-1,-1,-1);
		this.t = t;
	}
	
	public Turtle getTurlte(){
		return t;
	}
	
	public static void setUp(Loader loader){
		basic = new Object3D("turtle", loader,new Vector3f(150,8,150),0,0,0,0.03f);
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
		return colorAction;
	}

	public void setColorAction(Color colorAction) {
		this.colorAction.x = (colorAction.getRed()<=1.0)?colorAction.getRed():colorAction.getRed()/255.0f;
		this.colorAction.y = (colorAction.getGreen()<=1.0)?colorAction.getGreen():colorAction.getGreen()/255.0f;
		this.colorAction.z = (colorAction.getBlue()<=1.0)?colorAction.getBlue():colorAction.getBlue()/255.0f;
	}
	
	
	
}
