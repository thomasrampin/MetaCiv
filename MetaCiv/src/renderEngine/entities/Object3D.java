package renderEngine.entities;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.loaders.Loader;
import renderEngine.loaders.OBJLoader;
import renderEngine.models.Model;
import renderEngine.models.Models;
import renderEngine.utils.Matrix;

public class Object3D {
	
	private Model model;
	private Vector3f position;
	private float rotX,rotY,rotZ;
	private float scale;
	private String label;
	private Models models;
	private boolean multiObj;
	
	private ArrayList<Vector4f> positionOverWrite; // Road only

	public Object3D(Object3D object3d){
		if(!object3d.isMultiObj())
			this.model = object3d.getModel();
		else
			this.models = object3d.getModels();
		this.multiObj = object3d.isMultiObj();
		this.position = new Vector3f(object3d.getPosition());
		this.rotX = object3d.getRotX();
		this.rotY = object3d.getRotY();
		this.rotZ = object3d.getRotZ();
		this.scale = object3d.getScale();
		}
	
	private boolean isMultiObj() {
		return multiObj;
	}

	public Object3D(Model model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {

		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public Object3D(String objFilename, Loader loader ) {

		this.model = OBJLoader.loadObjModel(objFilename, loader);
		this.position = new Vector3f(0,0,0);
		this.rotX = 0;
		this.rotY = 0;
		this.rotZ = 0;
		this.scale = 1;
	}
	
	public Object3D(String objFilename, Loader loader,Vector3f position, float rotX, float rotY, float rotZ, float scale ) {

		this.model = OBJLoader.loadObjModel(objFilename, loader);
		this.position =position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public Object3D(String objFilename,String path, Loader loader,boolean multiObj) {
		this.models = OBJLoader.loadObjModels(objFilename,false,path, loader);
		this.multiObj = multiObj;
		this.position = new Vector3f(0,0,0);
		this.rotX = 0;
		this.rotY = 0;
		this.rotZ = 0;
		this.scale = 1;

	}
	
	public Object3D(String objFilename,String path, Loader loader,boolean multiObj,Vector3f position, float rotX, float rotY, float rotZ, float scale ) {
		this.models = OBJLoader.loadObjModels(objFilename,false,path, loader);
		this.multiObj = multiObj;
		this.position = new Vector3f(position);
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;

	}

	
	/*public boolean pick(Vector3f mouseRay){
		Vector3f max = model.getRawModel().getMax();
		Vector3f min = model.getRawModel().getMin();
		Matrix4f transformationMatrix = Matrix.createTransformationMatrix(getPosition(), getRotX(), getRotY(), getRotZ(), getScale());
		
		return mouseRay.x>min.x && mouseRay.x<max.x && mouseRay.y>min.y && mouseRay.y<max.y && mouseRay.z>min.z && mouseRay.z<max.z;
	}*/
	
	public Object3D(String objFilename,String path, Loader loader,boolean multiObj,boolean c,Vector3f position, float rotX, float rotY, float rotZ, float scale ){
		this.models = OBJLoader.loadObjModels(objFilename,true,path, loader);
		this.multiObj = multiObj;
		this.position = new Vector3f(position);
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public void increasePosition(float dx, float dy, float dz){
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz){
		this.rotX+=dx;
		this.rotY+=dy;
		this.rotZ+=dz;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Models getModels() {
		return models;
	}

	public ArrayList<Vector4f> getPositionOverWrite() {
		return positionOverWrite;
	}

	public void setPositionOverWrite(ArrayList<Vector4f> positionOverWrite) {
		this.positionOverWrite = positionOverWrite;
	}

}
