package renderEngine.models;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.utils.BoundingBox;

public class Models {
	
	private List<Model> models;
	private BoundingBox box;
	
	public Models(){
		this.models = new ArrayList<Model>();
	}
	
	public Models(List<Model> models){
		this.models = new ArrayList<Model>(models);
	}

	public List<Model> getModels() {
		return models;
	}

	public void add(Model model){
		this.models.add(model);
	}

	public void setBoundingBox(BoundingBox box) {
		this.box = box;
	}
	
	public BoundingBox getBox(){
		return box;
	}




	
}