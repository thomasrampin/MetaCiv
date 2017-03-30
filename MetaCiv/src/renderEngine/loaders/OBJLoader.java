package renderEngine.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.materials.Material;
import renderEngine.models.Model;
import renderEngine.models.Models;
import renderEngine.utils.BoundingBox;

public class OBJLoader {
	
	public static Model loadObjModel(String fileName,Loader loader){
		FileReader fr = null;
		try {
			fr = new FileReader(new File("Assets/OBJ/"+fileName+".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line;
		String fileMaterial="";
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector3f> textures = new ArrayList<Vector3f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Vector3f> tangents = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		List<Vector3f> verticesArray = new ArrayList<Vector3f>();
		List<Vector3f> normalsArray = new ArrayList<Vector3f>();
		List<Vector3f> textureArray = new ArrayList<Vector3f>();
		int[] indicesArray = null;
		
		
		try{
			while(true){
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				
				if(line.startsWith("mtllib ")){
					fileMaterial = currentLine[1];
				}
				else if(line.startsWith("v ")){	//Vertices position
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]),Float.parseFloat(currentLine[4]));
					vertices.add(vertex);			
				}
				else if(line.startsWith("vt ")){  //Texture coord
					Vector3f texture = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
					textures.add(texture);					
				}
				else if(line.startsWith("vn ")){  //normals vertex
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
					normals.add(normal);					
				}
				else if(line.startsWith("f ")){  //Faces
					break;
				}
			}
			while(line!=null){
				
				if(!line.startsWith("f ")){
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				
				processVertex(vertex1,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
				processVertex(vertex2,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
				processVertex(vertex3,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
				computeTangents(vertex1,vertex2,vertex3,vertices,textures,tangents);
				if(currentLine.length==5){
					String[] vertex4 = currentLine[4].split("/");
				
					processVertex(vertex4,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
					processVertex(vertex3,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
					processVertex(vertex1,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
				}
				
				
				line = reader.readLine();
				
			}
			reader.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		indicesArray = new int[indices.size()];
		
		
		for(int i=0;i<indices.size();i++){
			indicesArray[i] = indices.get(i);
		}
		
		float[] verticesFinal = new float[verticesArray.size()*3];
		float[] textureFinal = new float[textureArray.size()*3];
		float[] normalsFinal = new float[normalsArray.size()*3];
		float[] tangentsFinal = new float[tangents.size()*3];
		
		//Copy data
		int currentVert = 0;
		for(Vector3f vertex:verticesArray){
			verticesFinal[currentVert++] = vertex.x;
			verticesFinal[currentVert++] = vertex.y;
			verticesFinal[currentVert++] = vertex.z;
		}
		int currentTex = 0;
		for(Vector3f texture:textureArray){
			textureFinal[currentTex++] = texture.x;
			textureFinal[currentTex++] = texture.y;
			textureFinal[currentTex++] = texture.z;
		}
		int currentNorm = 0;
		for(Vector3f normal:normalsArray){
			normalsFinal[currentNorm++] = normal.x;
			normalsFinal[currentNorm++] = normal.y;
			normalsFinal[currentNorm++] = normal.z;
		}
		int currentTang = 0;
		for(Vector3f tangent:tangents){
			tangentsFinal[currentTang++] = tangent.x;
			tangentsFinal[currentTang++] = tangent.y;
			tangentsFinal[currentTang++] = tangent.z;
		}		

		
		
		Material texture = loadMaterial(fileMaterial,loader);
		return new Model(loader.loadToVAO(verticesFinal, textureFinal,normalsFinal, indicesArray,tangentsFinal),texture);
		
	}

	private static void computeTangents(String[] v0, String[] v1, String[] v2,List<Vector3f> vertices, List<Vector3f> textures,List<Vector3f> tangents) {
		Vector3f delatPos1 = Vector3f.sub(vertices.get(Integer.parseInt(v1[0])-1), vertices.get(Integer.parseInt(v0[0])-1), null);
		Vector3f delatPos2 = Vector3f.sub(vertices.get(Integer.parseInt(v2[0])-1), vertices.get(Integer.parseInt(v0[0])-1), null);
		Vector3f uv0 = textures.get(Integer.parseInt(v0[1])-1);
		Vector3f uv1 = textures.get(Integer.parseInt(v1[1])-1);
		Vector3f uv2 = textures.get(Integer.parseInt(v2[1])-1);
		Vector3f deltaUv1 = Vector3f.sub(uv1, uv0, null);
		Vector3f deltaUv2 = Vector3f.sub(uv2, uv0, null);

		float r = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
		delatPos1.scale(deltaUv2.y);
		delatPos2.scale(deltaUv1.y);
		Vector3f tangent = Vector3f.sub(delatPos1, delatPos2, null);
		tangent.scale(r);
		tangents.add(tangent);

}
	
	private static void processVertex(String[] vertexData, List<Integer> indices,List<Vector3f> vertices, List<Vector3f> textures,List<Vector3f> normals,List<Vector3f> verticesArray,List<Vector3f> textureArray,List<Vector3f> normalsArray){
		indices.add(indices.size());
		Vector3f currentVert = vertices.get(Integer.parseInt(vertexData[0])-1);
		verticesArray.add(currentVert);
		Vector3f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
		textureArray.add(new Vector3f(currentTex.x,1-currentTex.y,currentTex.z));// OBJ origin is at upper left, OpenGL origin is at lower left.
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
		normalsArray.add(currentNorm);
	}
	
	public static Material loadMaterial(String fileName,Loader loader){
		FileReader fr = null;
		try {
			fr = new FileReader(new File("Assets/OBJ/"+fileName));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line;
		String textureFile="";
		String normalFile = "";
		String dispFile = "";
		float damper = 1;
		Vector3f reflectivity = new Vector3f(0,0,0);
		Vector3f diffuse = new Vector3f(1,1,1);
		
		try{
			line = reader.readLine();
			while(line!=null){
				
				
				if(line.startsWith("	Ns")){
					String[] currentLine = line.split(" ");
					damper = Float.parseFloat(currentLine[1]);
				}
				else if(line.startsWith("	Kd")){
					String[] currentLine = line.split(" ");
					diffuse = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
				}
				else if(line.startsWith("	Ks")){
					String[] currentLine = line.split(" ");
					reflectivity = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
				}
				else if(line.startsWith("	map_Kd")){
					String[] currentLine = line.split(" ");
					textureFile = currentLine[1];
				}
				else if(line.startsWith("	map_Kn")){
					String[] currentLine = line.split(" ");
					normalFile = currentLine[1];
				}
				else if(line.startsWith("	disp")){
					String[] currentLine = line.split(" ");
					dispFile = currentLine[1];
				}
				
				line = reader.readLine();
			}

			reader.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!textureFile.equals(""))
			if(!normalFile.equals(""))
				if(!dispFile.equals(""))
					return new Material(loader.loadTexture(textureFile),loader.loadTexture(normalFile),loader.loadTexture(dispFile),damper,reflectivity,diffuse);
				else
					return new Material(loader.loadTexture(textureFile),loader.loadTexture(normalFile),damper,reflectivity,diffuse);
			else
				return new Material(loader.loadTexture(textureFile),damper,reflectivity,diffuse);
		else
			return new Material(damper,reflectivity,diffuse);
	}
	
	
	private static Material loadMaterial(String fileName,Loader loader,String material){
		FileReader fr = null;
		try {
			fr = new FileReader(new File("Assets/OBJ/"+fileName));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line;
		String textureFile="";
		String normalFile = "";
		String dispFile = "";
		float damper = 1;
		Vector3f reflectivity = new Vector3f(0,0,0);
		Vector3f diffuse = new Vector3f(1,1,1);
		
		try{
			line = reader.readLine();
			while(line!=null){
				if(line.startsWith("newmtl ")){
					String[] currentLine = line.split(" ");
					if(currentLine[1].equals(material)){
						break;
					}
				}
				line = reader.readLine();
			}
			line = reader.readLine();
			while(line!=null && !line.startsWith("newmtl ") ){
				
				
				if(line.startsWith("	Ns")){
					String[] currentLine = line.split(" ");
					damper = Float.parseFloat(currentLine[1]);
				}
				else if(line.startsWith("	Kd")){
					String[] currentLine = line.split(" ");
					diffuse = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
				}
				else if(line.startsWith("	Ks")){
					String[] currentLine = line.split(" ");
					reflectivity = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
				}
				else if(line.startsWith("	map_Kd")){
					String[] currentLine = line.split(" ");
					textureFile = currentLine[1];
				}
				else if(line.startsWith("	map_Kn")){
					String[] currentLine = line.split(" ");
					normalFile = currentLine[1];
					
				}
				else if(line.startsWith("	disp")){
					String[] currentLine = line.split(" ");
					dispFile = currentLine[1];
				}
				
				line = reader.readLine();
			}

			reader.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		if(!textureFile.equals(""))
			if(!normalFile.equals(""))
				if(!dispFile.equals(""))
					return new Material(loader.loadTexture(textureFile),loader.loadTexture(normalFile),loader.loadTexture(dispFile),damper,reflectivity,diffuse);
				else
					return new Material(loader.loadTexture(textureFile),loader.loadTexture(normalFile),damper,reflectivity,diffuse);
			else
				return new Material(loader.loadTexture(textureFile),damper,reflectivity,diffuse);
		else
			return new Material(damper,reflectivity,diffuse);
	}

	public static Models loadObjModels(String fileName,Loader loader){
		FileReader fr = null;
		try {
			fr = new FileReader(new File("Assets/OBJ/"+fileName+".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace();
		}
		

		
		BufferedReader reader = new BufferedReader(fr);
		String line;
		String fileMaterial="";
		String Material="";
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector3f> textures = new ArrayList<Vector3f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		List<Vector3f> tangents = new ArrayList<Vector3f>();
		
		int last = 0;
		Models models = new Models();
		
		try{
			line = "";
			while(line!=null){
				int sizeIndices = 0;
				List<Vector3f> verticesArray = new ArrayList<Vector3f>();
				List<Vector3f> normalsArray = new ArrayList<Vector3f>();
				List<Vector3f> textureArray = new ArrayList<Vector3f>();
				int[] indicesArray = null;
				
				while(true && line!=null){
					
					String[] currentLine = line.split(" ");
					
					if(line.startsWith("mtllib ")){
						fileMaterial = currentLine[1];
					}
					else if(line.startsWith("v ")){	//Vertices position
						Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]),Float.parseFloat(currentLine[4]));
						vertices.add(vertex);	
					}
					else if(line.startsWith("vt ")){  //Texture coord
						Vector3f texture = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
						textures.add(texture);
					}
					else if(line.startsWith("vn ")){  //normals vertex
						Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
						normals.add(normal);
					}
					else if(line.startsWith("usemtl ")){
						Material = currentLine[1];
					}
					else if(line.startsWith("f ")){  //Faces
						break;
					}
					
					line = reader.readLine();
				}
				while(line!=null && !line.startsWith("# ")){
					if(!line.startsWith("f ")){
						line = reader.readLine();
						continue;
					}
					String[] currentLine = line.split(" ");
					String[] vertex1 = currentLine[1].split("/");
					String[] vertex2 = currentLine[2].split("/");
					String[] vertex3 = currentLine[3].split("/");
					
					
					processVertex(vertex1,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
					processVertex(vertex2,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
					processVertex(vertex3,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
					computeTangents(vertex1,vertex2,vertex3,vertices,textures,tangents);
					sizeIndices+=3;
					if(currentLine.length==5){
						String[] vertex4 = currentLine[4].split("/");
					
						processVertex(vertex4,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
						processVertex(vertex3,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
						processVertex(vertex1,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
						sizeIndices+=3;
					}
					
					
					line = reader.readLine();
					
				}
				if(indices.size()-last<0)
					break;
				indicesArray = new int[sizeIndices];
				
				for(int i=0;i<sizeIndices;i++){
					indicesArray[i] = indices.get(i+last)-last;
				}
				last = indices.size();
				float[] verticesFinal = new float[verticesArray.size()*3];
				float[] textureFinal = new float[textureArray.size()*3];
				float[] normalsFinal = new float[normalsArray.size()*3];
				float[] tangentsFinal = new float[tangents.size()*3];
				
				//Copy data
				int currentVert = 0;
				for(Vector3f vertex:verticesArray){						
					verticesFinal[currentVert++] = vertex.x;
					verticesFinal[currentVert++] = vertex.y;
					verticesFinal[currentVert++] = vertex.z;
				}
				int currentTex = 0;
				for(Vector3f texture:textureArray){
					textureFinal[currentTex++] = texture.x;
					textureFinal[currentTex++] = texture.y;
					textureFinal[currentTex++] = texture.z;
				}
				int currentNorm = 0;
				for(Vector3f normal:normalsArray){
					normalsFinal[currentNorm++] = normal.x;
					normalsFinal[currentNorm++] = normal.y;
					normalsFinal[currentNorm++] = normal.z;
				}
				int currentTang = 0;
				for(Vector3f tangent:tangents){
					tangentsFinal[currentTang++] = tangent.x;
					tangentsFinal[currentTang++] = tangent.y;
					tangentsFinal[currentTang++] = tangent.z;
				}		

				Material texture = loadMaterial(fileMaterial,loader,Material);
				
				models.add(new Model(loader.loadToVAO(verticesFinal, textureFinal,normalsFinal, indicesArray,tangentsFinal),texture));
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return models;
	}
	
	
}

