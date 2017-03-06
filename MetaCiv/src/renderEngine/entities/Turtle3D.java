package renderEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.loaders.Loader;

public class Turtle3D {

	private float x,y,z;
	private float lastX=100,lastY=-1,lastZ=100;
	private Object3D object3d;
	private static Object3D king;
	private static Object3D basic;
	private int id;
	private float interpolation;
	
	public Turtle3D(int id, boolean b){
		if(b)
			this.object3d = new Object3D(king);
		else
			this.object3d = new Object3D(basic);
		this.id = id;
		this.interpolation = 0;
	}
	
	public static void setUp(Loader loader){
		basic = new Object3D("1","pion", loader,new Vector3f(150,8,150),0,0,0,0.03f);
		king = new Object3D("1","king", loader,new Vector3f(150,8,150),0,0,0,0.03f);
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
	
	
	
}
