package renderEngine.models;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.materials.Material;
import renderEngine.utils.BoundingBox;

// Mesh + texture

public class Model {
	
	private Mesh mesh;
	private Material texture;
	private BoundingBox box;
	private Vector4f colorID;
	
	public Model(Mesh mesh, Material texture){
		this.mesh = mesh;
		this.texture = texture;
		this.colorID =new Vector4f(0,0,0,0);
	}

	public Model( Material texture) {
		this.texture = texture;
	}

	public Model(Model model) {
		this.texture = model.getTexture();
		this.mesh = model.mesh;
		this.box = model.box;
		this.colorID =model.colorID;
	}

	public Mesh getRawModel() {
		return mesh;
	}

	public Material getTexture() {
		return texture;
	}

	public BoundingBox getBox() {
		return box;
	}

	public void setColorID(Vector4f colorID){
		this.colorID = colorID;
	}
	
	public Vector4f getColorID(){
		return colorID;
	}
}
