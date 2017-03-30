package renderEngine.materials;

import org.lwjgl.util.vector.Vector3f;

public class Material {
	
	private int textureID;
	private int normalID;
	private int dispID;
	private boolean normalMapped=false;
	private boolean textured=false;
	private boolean dispMapped=false;
	private float shineDamper = 1;
	private Vector3f reflectivity = new Vector3f(0,0,0);
	private Vector3f diffuse = new Vector3f(1,1,1);
	
	public Material(int id){
		textureID = id;
		textured = true;
	}

	public Material(int tid,int nid){
		textureID = tid;
		textured = true;
		normalID = nid;
		normalMapped = true;
	}

	public Material(int tid,int nid,int did){
		textureID = tid;
		textured = true;
		normalID = nid;
		normalMapped = true;
		dispID = did;
		dispMapped = true;
	}
	
	public Material(int id,float shineDamper, Vector3f reflectivity,Vector3f diffuse){
		textureID = id;
		textured = true;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.diffuse = diffuse;
	}
	
	public Material(int id,int nid,float shineDamper, Vector3f reflectivity,Vector3f diffuse){
		textureID = id;
		textured = true;
		normalID = nid;
		normalMapped = true;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.diffuse = diffuse;
	}
	
	public Material(int id,int nid,int did,float shineDamper, Vector3f reflectivity,Vector3f diffuse){
		textureID = id;
		textured = true;
		normalID = nid;
		normalMapped = true;
		dispID = did;
		dispMapped = true;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.diffuse = diffuse;
	}
	
	public Material(float shineDamper, Vector3f reflectivity,Vector3f diffuse){
		textured = false;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.diffuse = diffuse;
	}
	
	public int getTextureID(){
		return textureID;
	}
	
	public int getNormalID(){
		return normalID;
	}
	
	public int getDispID(){
		return dispID;
	}
	
	public boolean IsNormalMapped(){
		return normalMapped;
	}
	
	public boolean IsTextured(){
		return textured;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public Vector3f getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(Vector3f reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	public Vector3f getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(Vector3f diffuse) {
		this.diffuse = diffuse;
	}

	public boolean IsDispMapped() {
		return dispMapped;
	}
	
	
}
