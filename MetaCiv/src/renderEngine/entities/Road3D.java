package renderEngine.entities;

import java.io.File;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import civilisation.Configuration;
import civilisation.amenagement.TypeAmenagement;
import renderEngine.loaders.Loader;
import renderEngine.utils.BoundingBox;

public class Road3D {

	private static Object3D road;
	private Object3D object3d;
	private Vector2f id;
	
	public Road3D(Vector3f position,int x, int y, ArrayList<Vector4f> positionRoad){
		this.object3d = new Object3D(road);
		Vector3f position_reajust = new Vector3f(position.x,position.y+0.01f,position.z);
		this.object3d.setPosition(position_reajust);
		this.object3d.setPositionOverWrite(positionRoad);
		this.id = new Vector2f(x,y);
	}

	public static void setUp(Loader loader){

			
			File f = new File(Configuration.pathToRessources+"/Skin/Amenagement/Road/Road.OBJ");
			
			if(f.isFile()){
				//road = new Object3D(f.getAbsolutePath(),f.getParent(), loader, loader,new Vector3f(150,8,150),0,0,0,0.03f);
				BoundingBox box = road.getModels().getBox();
				float distX = box.getMax().x - box.getMin().x;
				float distY = box.getMax().y - box.getMin().y;
				float distZ = box.getMax().z - box.getMin().z;
				float distMax = Math.max(Math.max(distX, distY),distZ);
				if(distMax>5){
					road.setScale(5.0f/distMax);
				}
			}else{
				road = new Object3D("Default_road", loader,new Vector3f(150,8,150),0,0,0,1.0f);
			}
		
	}
	
	public static Object3D getRoad() {
		return road;
	}

	public Object3D getObject3d() {
		return object3d;
	}

	public void setObject3d(Object3D object3d) {
		this.object3d = object3d;
	}
	
	public boolean checkID(int x,int y){
		return (id.x == x && id.y == y);
	}
	
	
	
}
