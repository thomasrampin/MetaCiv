package renderEngine.utils;

import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Helper {

	public static Vector4f IntegerToColor(int i)
	{
		int r = (i & 0x000000FF) >> 0;
		int g = (i & 0x0000FF00) >> 8;
		int b = (i & 0x00FF0000) >> 16;
		int a = (i & 0xFF000000) >> 24;

		return new Vector4f(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
	}
	
	public static boolean isSameColor(int i,ByteBuffer pixel){
		
		int r = (i & 0x000000FF) >> 0;
		int g = (i & 0x0000FF00) >> 8;
		int b = (i & 0x00FF0000) >> 16;
		int a = (i & 0xFF000000) >> 24;
		int r2 = pixel.get(0) & 0xFF;
		int g2 = pixel.get(1) & 0xFF;
		int b2 = pixel.get(2) & 0xFF;
		

		return (r == r2 && g == g2 && b == b2 );
	}
	
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
	
	public static float clamp(float val, float d,float e){
	    return Math.max(d, Math.min(e, val));
	}

	public static float mix(float a,float b, float x){
		float t = a*(1-x)+b*x;
		
		return t;
	}
	
	public static float smoothStep(float a, float b,float x){
		float  t = (float) clamp((x - a) / (b - a), 0.0f, 1.0f);
		
	    return (float) (t * t * (3.0 - 2.0 * t));
	}
	
	public static double Norme(Vector3f vector){
	    return Math.sqrt((vector.getX()*vector.getX())+(vector.getY()*vector.getY())+(vector.getZ()*vector.getZ()));
	}

	public static Vector3f Normalize(Vector3f vector){
	    double n = Norme(vector);
	    vector.setX((float) (vector.getY()/n));
	    vector.setY((float) (vector.getY()/n));
	    vector.setZ((float) (vector.getZ()/n));
	    return new Vector3f(vector);
	}

	public static double Scalar(Vector3f Vector,Vector3f Vector2){
	    return (Vector.getX()*Vector2.getX()+Vector.getY()*Vector2.getY()+Vector.getZ()*Vector2.getZ());
	}
	
	public static Vector3f Vectoriel(Vector3f Vector,Vector3f Vector2){
	    Vector3f vec = new Vector3f((Vector.y * Vector2.getZ()) - ( Vector2.getY() * Vector.z),(Vector.z * Vector2.getX()) - ( Vector2.getZ() * Vector.x),(Vector.x * Vector2.getY()) - ( Vector2.getX() * Vector.y));
	    return vec;
	}

	
	public static Vector3f projectOnPlan(Vector3f Point,Vector3f PointOnPlane,Vector3f NormalOfPlane){
	    Vector3f MA = new Vector3f(Point.getX()-PointOnPlane.getX(),Point.getY()-PointOnPlane.getY(),Point.getZ()-PointOnPlane.getZ());
	    float lenght = (float) (Scalar(MA,NormalOfPlane)/Norme(NormalOfPlane));

	    Vector3f Project = new Vector3f(Point.getX()-NormalOfPlane.getX()*lenght,Point.getY()-NormalOfPlane.getY()*lenght,Point.getZ()-NormalOfPlane.getZ()*lenght);

	    return Project;
	}
	
	
}
