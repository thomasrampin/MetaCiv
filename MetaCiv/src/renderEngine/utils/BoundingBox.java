package renderEngine.utils;

import org.lwjgl.util.vector.Vector3f;

public class BoundingBox {

	private Vector3f min;
	private Vector3f max;
	private Vector3f[] normal;
	private Vector3f[] points;
	
	public BoundingBox(Vector3f min, Vector3f max) {
		this.min = min;
		this.max = max;
		this.normal = new Vector3f[6];
		this.normal[0] = new Vector3f(0,1,0);
		this.normal[1] = new Vector3f(0,-1,0);
		this.normal[2] = new Vector3f(1,0,0);
		this.normal[3] = new Vector3f(-1,0,0);
		this.normal[4] = new Vector3f(0,0,1);
		this.normal[5] = new Vector3f(0,0,-1);
		
		this.points = new Vector3f[6];
		this.points[0] = new Vector3f((max.x+min.x)/2.0f,max.y,(max.z+min.z)/2.0f);
		this.points[1] = new Vector3f((max.x+min.x)/2.0f,min.y,(max.z+min.z)/2.0f);
		this.points[2] = new Vector3f(max.x,(max.y+min.y)/2.0f,(max.z+min.z)/2.0f);
		this.points[3] = new Vector3f(min.x,(max.y+min.y)/2.0f,(max.z+min.z)/2.0f);
		this.points[4] = new Vector3f((max.x+min.x)/2.0f,(max.y+min.y)/2.0f,max.z);
		this.points[5] = new Vector3f((max.x+min.x)/2.0f,(max.y+min.y)/2.0f,min.z);
		
	}

	public Vector3f getMin() {
		return min;
	}

	public void setMin(Vector3f min) {
		this.min = min;
	}

	public Vector3f getMax() {
		return max;
	}

	public void setMax(Vector3f max) {
		this.max = max;
	}
	
	public boolean containPoint(Vector3f Point, Vector3f Origin, Vector3f direction,Vector3f pos){
		for(int i=0;i<6;i++){
			double lenght = Math.sqrt(Math.pow((points[i].x-Origin.x),2) - Math.pow((points[i].y-Origin.y),2) - Math.pow((points[i].z-Origin.z),2));
			if(Helper.Scalar(direction, normal[i])>0){
				return true;
			}
			/*if(-((Helper.Scalar(Origin, normal[i])+lenght)/Helper.Scalar(direction, normal[i]))>=0)
				return true;*/
		}
		return false;
		
	}
	
}
