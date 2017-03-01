package renderEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.loaders.Loader;

public class Turtle3D {

	private float x,y;
	private float lastX,lastY;
	private Object3D object3d;
	
	public Turtle3D(Loader loader){
		this.object3d = new Object3D("1","king", loader,new Vector3f(150,8,150),0,0,0,0.03f);
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getLastX() {
		return lastX;
	}
	public void setLastX(float lastX) {
		this.lastX = lastX;
	}
	public float getLastY() {
		return lastY;
	}
	public void setLastY(float lastY) {
		this.lastY = lastY;
	}
	public Object3D getObject3d() {
		return object3d;
	}
	public void setObject3d(Object3D object3d) {
		this.object3d = object3d;
	}
	
	
	
	
}
