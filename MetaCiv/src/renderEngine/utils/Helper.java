package renderEngine.utils;

import org.lwjgl.util.vector.Vector3f;

public class Helper {

	public static Vector3f rotateAround(float pitch,float distance,float around,Vector3f lookAt){
		Vector3f position = new Vector3f();
		float hD =  (float) (distance * Math.cos(Math.toRadians(pitch)));
		float vD = (float) (distance * Math.sin(Math.toRadians(pitch)));
		float offsetX = (float) (hD * Math.sin(Math.toRadians(around)));
		float offsetZ = (float) (hD * Math.cos(Math.toRadians(around)));
		position.x = lookAt.x - offsetX;
		position.y = lookAt.y + vD;
		position.z = lookAt.z - offsetZ;
		return position;
	}

	public static Vector3f soustrate(Vector3f a,Vector3f b){
		Vector3f r = new Vector3f();
		r.x = a.x - b.x;
		r.y = a.y - b.y;
		r.z = a.z - b.z;
		return r;
	}

	public static Vector3f multiply(Vector3f a,Vector3f b){
		Vector3f r = new Vector3f();
		r.x = a.x * b.x;
		r.y = a.y * b.y;
		r.z = a.z * b.z;
		return r;
	}

	public static Vector3f multiply(Vector3f a,float b){
		Vector3f r = new Vector3f();
		r.x = a.x * b;
		r.y = a.y * b;
		r.z = a.z * b;
		return r;
	}
	
}
