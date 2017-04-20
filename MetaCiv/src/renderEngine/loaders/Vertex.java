package renderEngine.loaders;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {
	

	
	public Vector3f position;
	private int textureIndex = -1;
	private int normalIndex = -1;
	private Vertex duplicateVertex = null;
	private int index;

	private List<Vector3f> tangents = new ArrayList<Vector3f>();
	public Vector3f tangent = new Vector3f(0, 0, 0);
	
	public Vertex(int index,Vector3f position){
		this.index = index;
		this.position = position;

	}
	
	public void addTangent(Vector3f tangent){
		tangents.add(tangent);
	}
	
	public void computeTangents(){
		if(tangents.isEmpty()){
			return;
		}
		for(Vector3f t : tangents){
			Vector3f.add(tangent, t, tangent);
		}
		if(tangent.length()>0)
			tangent.normalise();
	}
	

	
	public int getIndex(){
		return index;
	}
	

	public boolean isSet(){
		return textureIndex!=-1 && normalIndex!=-1;
	}
	
	public boolean hasSameTextureAndNormal(int textureIndexOther,int normalIndexOther){
		return textureIndexOther==textureIndex && normalIndexOther==normalIndex;
	}
	
	public void setTextureIndex(int textureIndex){
		this.textureIndex = textureIndex;
	}
	
	public void setNormalIndex(int normalIndex){
		this.normalIndex = normalIndex;
	}


	public int getTextureIndex() {
		return textureIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public Vertex getDuplicateVertex() {
		return duplicateVertex;
	}

	public void setDuplicateVertex(Vertex duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}
