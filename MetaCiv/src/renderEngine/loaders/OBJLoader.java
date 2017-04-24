package renderEngine.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.loaders.Vertex;
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
		List<Vertex> vertices = new ArrayList<>();
		List<Vector3f> textures = new ArrayList<Vector3f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Vector3f> tangents = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();

		
		float minX = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;

		float minY = Float.MAX_VALUE;
		float maxY = Float.MIN_VALUE;

		float minZ = Float.MAX_VALUE;
		float maxZ = Float.MIN_VALUE;
		

		try{
			while(true){
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				
				if(line.startsWith("mtllib ")){
					fileMaterial = currentLine[1];
				}
				else if(line.startsWith("v ")){	//Vertices position
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]),Float.parseFloat(currentLine[4]));
					vertices.add(new Vertex(vertices.size(),vertex));
					if(vertex.x < minX)
						minX = vertex.x;
					if(vertex.x > maxX)
						maxX = vertex.x;
					
					if(vertex.y < minY)
						minY = vertex.y;
					if(vertex.y > maxY)
						maxY = vertex.y;
					
					if(vertex.z < minZ)
						minZ = vertex.z;
					if(vertex.z > maxX)
						maxZ = vertex.z;
				}
				else if(line.startsWith("vt ")){  //Texture coord
					Vector3f texture;
					if(currentLine.length==4){
						texture = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
					}else{
						texture = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),0);
					}
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
				Vertex v0 = processVertex(vertex1, vertices, indices,0,0,0);
				Vertex v1 = processVertex(vertex2, vertices, indices,0,0,0);
				Vertex v2 = processVertex(vertex3, vertices, indices,0,0,0);
				calculateTangents(v0, v1, v2, textures);
				
				/*processVertex(vertex1,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
				processVertex(vertex2,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
				processVertex(vertex3,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
				if(!vertex1.equals("") && !vertex2.equals("") && !vertex3.equals(""))
					computeTangents(vertex1,vertex2,vertex3,vertices,textures,tangents);
				if(currentLine.length==5){
					String[] vertex4 = currentLine[4].split("/");
				
					processVertex(vertex4,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
					processVertex(vertex3,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
					processVertex(vertex1,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
				}*/
				
				
				line = reader.readLine();
				
			}
			reader.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		int[] indicesArray = null;
		indicesArray = new int[indices.size()];
		
		removeUnusedVertices(vertices);
		for(int i=0;i<indices.size();i++){
			indicesArray[i] = indices.get(i);
		}
	
		

		/*float[] verticesFinal = new float[verticesArray.size()*3];
		float[] textureFinal = new float[textureArray.size()*3];
		float[] normalsFinal = new float[normalsArray.size()*3];
		float[] tangentsFinal = new float[verticesArray.size()*3];

		
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
		for(Vector3f vertex:verticesArray){		
			for(VertexTangent vT:vertices){
				if(vT.position.equals(vertex)){
					vT.averageTangents();
					tangentsFinal[currentTang++] = vT.averagedTangent.x;
					tangentsFinal[currentTang++] = vT.averagedTangent.y;
					tangentsFinal[currentTang++] = vT.averagedTangent.z;
					break;
				}
			}
		}*/
		
		float[] verticesArray = new float[vertices.size() * 3];
		float[] texturesArray = new float[vertices.size() * 3];
		float[] normalsArray = new float[vertices.size() * 3];
		float[] tangentsArray = new float[vertices.size() * 3];
		convertDataToArrays(vertices, textures, normals, verticesArray,
				texturesArray, normalsArray, tangentsArray);
		
		Material texture = loadMaterial(fileMaterial,fileName,loader);
		Model model = new Model(loader.loadToVAO(verticesArray, texturesArray,normalsArray, indicesArray,tangentsArray),texture);
		BoundingBox box = new BoundingBox(new Vector3f(minX,minY,minZ),new Vector3f(maxX,maxY,maxZ));
		model.setBoundingBox(box);
		return model ;
		
	}

	
	private static void calculateTangents(Vertex v0, Vertex v1, Vertex v2,
			List<Vector3f> textures) {
		Vector3f delatPos1 = Vector3f.sub(v1.position, v0.position, null);
		Vector3f delatPos2 = Vector3f.sub(v2.position, v0.position, null);
		Vector3f uv0 = textures.get(v0.getTextureIndex());
		Vector3f uv1 = textures.get(v1.getTextureIndex());
		Vector3f uv2 = textures.get(v2.getTextureIndex());
		Vector3f deltaUv1 = Vector3f.sub(uv1, uv0, null);
		Vector3f deltaUv2 = Vector3f.sub(uv2, uv0, null);

		float r = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
		delatPos1.scale(deltaUv2.y);
		delatPos2.scale(deltaUv1.y);
		Vector3f tangent = Vector3f.sub(delatPos1, delatPos2, null);
		tangent.scale(r);
		v0.addTangent(tangent);
		v1.addTangent(tangent);
		v2.addTangent(tangent);
	}

	private static Vertex processVertex(String[] vertex, List<Vertex> vertices,
			List<Integer> indices, int lastVertices, int lastTextures, int lastNormals) {
		int index = Integer.parseInt(vertex[0]) - 1 - lastVertices;
		Vertex currentVertex = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1 - lastTextures;
		int normalIndex = Integer.parseInt(vertex[2]) - 1 - lastNormals;
		if (!currentVertex.isSet()) {
			currentVertex.setTextureIndex(textureIndex);
			currentVertex.setNormalIndex(normalIndex);
			indices.add(index);
			return currentVertex;
		} else {
			return dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
					vertices);
		}
	}
	
	private static void convertDataToArrays(List<Vertex> vertices, List<Vector3f> textures,
			List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
			float[] normalsArray, float[] tangentsArray) {

		for (int i = 0; i < vertices.size(); i++) {
			Vertex currentVertex = vertices.get(i);

			Vector3f position = currentVertex.position;
			Vector3f textureCoord = textures.get(currentVertex.getTextureIndex());
			Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
			Vector3f tangent = currentVertex.tangent;
			verticesArray[i * 3] = position.x;
			verticesArray[i * 3 + 1] = position.y;
			verticesArray[i * 3 + 2] = position.z;
			texturesArray[i * 3] = textureCoord.x;
			texturesArray[i * 3 + 1] = 1 - textureCoord.y;
			texturesArray[i * 3 + 2] = textureCoord.z;
			normalsArray[i * 3] = normalVector.x;
			normalsArray[i * 3 + 1] = normalVector.y;
			normalsArray[i * 3 + 2] = normalVector.z;
			tangentsArray[i * 3] = tangent.x;
			tangentsArray[i * 3 + 1] = tangent.y;
			tangentsArray[i * 3 + 2] = tangent.z;

		}
	}

	private static Vertex dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex,
			int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.getIndex());
			return previousVertex;
		} else {
			Vertex anotherVertex = previousVertex.getDuplicateVertex();
			if (anotherVertex != null) {
				return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex,
						newNormalIndex, indices, vertices);
			} else {
				Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.position);
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
				return duplicateVertex;
			}

		}
	}

	private static void removeUnusedVertices(List<Vertex> vertices) {
		for (Vertex vertex : vertices) {
			vertex.computeTangents();
			if (!vertex.isSet()) {
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}
	
	
	/*private static void computeTangents(String[] v0, String[] v1, String[] v2,List<VertexTangent> vertices, List<Vector3f> textures,List<Vector3f> tangents) {
		if(!v0[0].equals("") && !v0[1].equals("") && !v1[0].equals("") && !v1[1].equals("") && !v2[0].equals("") && !v2[1].equals("")){
			Vector3f delatPos1 = Vector3f.sub(vertices.get(Integer.parseInt(v1[0])-1).position, vertices.get(Integer.parseInt(v0[0])-1).position, null);
			Vector3f delatPos2 = Vector3f.sub(vertices.get(Integer.parseInt(v2[0])-1).position, vertices.get(Integer.parseInt(v0[0])-1).position, null);
			Vector3f uv0 = textures.get(Integer.parseInt(v0[1])-1);
			Vector3f uv1 = textures.get(Integer.parseInt(v1[1])-1);
			Vector3f uv2 = textures.get(Integer.parseInt(v2[1])-1);
			Vector3f deltaUv1 = Vector3f.sub(uv1, uv0, null);
			Vector3f deltaUv2 = Vector3f.sub(uv2, uv0, null);
	
			float r = 1.0f / (float)(deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
			delatPos1.scale(deltaUv2.y);
			delatPos2.scale(deltaUv1.y);
			Vector3f tangent = Vector3f.sub(delatPos1, delatPos2, null);
			tangent.scale(r);
			vertices.get(Integer.parseInt(v0[0])-1).addTangent(tangent);
			vertices.get(Integer.parseInt(v1[0])-1).addTangent(tangent);
			vertices.get(Integer.parseInt(v2[0])-1).addTangent(tangent);
			

		}else{
			Vector3f tangent = new Vector3f(0,0,0);
			tangents.add(tangent);
			

		}

	}*/
	
	/*private static void processVertex(String[] vertexData, List<Integer> indices,List<VertexTangent> vertices, List<Vector3f> textures,List<Vector3f> normals,float[] verticesArray,float[] textureArray,float[] normalsArray){
		indices.add(Integer.parseInt(vertexData[0])-1);
		VertexTangent currentVert = vertices.get(Integer.parseInt(vertexData[0])-1);

		verticesArray[(Integer.parseInt(vertexData[0])-1)*3] = currentVert.position.x;
		verticesArray[(Integer.parseInt(vertexData[0])-1)*3+1] = currentVert.position.y;
		verticesArray[(Integer.parseInt(vertexData[0])-1)*3+2] = currentVert.position.z;
		Vector3f currentTex = new Vector3f(0,0,0);
		if(!vertexData[1].equals(""))
			currentTex = textures.get(Integer.parseInt(vertexData[1])-1);

		textureArray[(Integer.parseInt(vertexData[0])-1)*3] = currentTex.x;
		textureArray[(Integer.parseInt(vertexData[0])-1)*3+1] = 1 - currentTex.y;
		textureArray[(Integer.parseInt(vertexData[0])-1)*3+2] = currentTex.z;		
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);

		normalsArray[(Integer.parseInt(vertexData[0])-1)*3] = currentNorm.x;
		normalsArray[(Integer.parseInt(vertexData[0])-1)*3+1] = currentNorm.y;
		normalsArray[(Integer.parseInt(vertexData[0])-1)*3+2] = currentNorm.z;		
		
	}*/
	
	public static Material loadMaterial(String fileNameM,String fileName, Loader loader){
		FileReader fr = null;
		String textureFile="";
		String normalFile = "";
		String dispFile = "";
		String reflFile = "";
		String metalFile = "";
		float damper = 1;
		Vector3f reflectivity = new Vector3f(0,0,0);
		Vector3f diffuse = new Vector3f(1,1,1);
		
		File file = new File("Assets/OBJ/"+fileNameM);
		if(file.exists() && file.isFile()){
			try {
				fr = new FileReader(new File("Assets/OBJ/"+fileNameM));
			} catch (FileNotFoundException e) {
				System.err.println("Couldn't load file!");
				e.printStackTrace();
			}
			
			BufferedReader reader = new BufferedReader(fr);
			String line;

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
					else if(line.startsWith("	map_bump")){
						String[] currentLine = line.split(" ");
						normalFile = currentLine[1];
					}
					else if(line.startsWith("	disp")){
						String[] currentLine = line.split(" ");
						dispFile = currentLine[1];
					}
					else if(line.startsWith("	map_refl")){
						String[] currentLine = line.split(" ");
						reflFile = currentLine[1];
					}
					
					line = reader.readLine();
				}
	
				reader.close();
				
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		//Check out if any folder
		File f = new File("Assets/Texture/" + fileName);
		
		if(f.isDirectory()){
			
			for (final File fileEntry : f.listFiles()) {
				
				if(fileEntry.getName().equals("Diffuse.png") || fileEntry.getName().equals("diffuse.png") || fileEntry.getName().equals("Albedo.png") || fileEntry.getName().equals("albedo.png") || fileEntry.getName().equals("Base_Color.png") || fileEntry.getName().equals("base_color.png") || fileEntry.getName().equals("Base_color.png")
						|| fileEntry.getName().equals("base_Color.png") || fileEntry.getName().equals("BaseColor.png") || fileEntry.getName().equals("Basecolor.png") || fileEntry.getName().equals("baseColor.png") || fileEntry.getName().equals("basecolor.png")){
					textureFile = fileName + "/" + fileEntry.getName();
				}
				else if(fileEntry.getName().equals("normal.png") || fileEntry.getName().equals("Normal.png") || fileEntry.getName().equals("Normal_OpenGL.png")){
					normalFile = fileName + "/" + fileEntry.getName();
				}
				else if(fileEntry.getName().equals("Height.png") || fileEntry.getName().equals("height.png") || fileEntry.getName().equals("DisplacementMap.png") || fileEntry.getName().equals("displacentMap.png") || fileEntry.getName().equals("displacentmap.png") || fileEntry.getName().equals("dispMap.png") || fileEntry.getName().equals("dispmap.png")){
					dispFile = fileName + "/" + fileEntry.getName();
				}
				else if(fileEntry.getName().equals("Roughness.png") || fileEntry.getName().equals("roughness.png")){
					reflFile = fileName + "/" + fileEntry.getName();
				}
				else if(fileEntry.getName().equals("Metallic.png") || fileEntry.getName().equals("metallic.png") || fileEntry.getName().equals("Metal.png") || fileEntry.getName().equals("metal.png")){
					metalFile = fileName + "/" + fileEntry.getName();
				}
			}
		}
		
		Material materialReturn = new Material(damper,reflectivity,diffuse);
		if(!textureFile.equals(""))
			materialReturn.setTextureID(loader.loadTexture(textureFile));
		
		if(!normalFile.equals(""))
			materialReturn.setNormalID(loader.loadTexture(normalFile));
		
		if(!dispFile.equals(""))
			materialReturn.setDispID(loader.loadTexture(dispFile));
		
		if(!reflFile.equals(""))
			materialReturn.setReflID(loader.loadTexture(reflFile));
					
		if(!metalFile.equals(""))
			materialReturn.setMetalID(loader.loadTexture(metalFile));

		
		return materialReturn;
	}
	
	
	private static Material loadMaterial(String fileName,boolean absolute,String path, Loader loader,String material){
		FileReader fr = null;
		try {
			if(absolute)
				fr = new FileReader(new File(path+"/"+fileName));
			else
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
		String reflFile = "";
		String metalFile = "";
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
				else if(line.startsWith("	map_bump")){
					String[] currentLine = line.split(" ");
					normalFile = currentLine[1];
					
				}
				else if(line.startsWith("	disp")){
					String[] currentLine = line.split(" ");
					dispFile = currentLine[1];
				}
				else if(line.startsWith("	map_refl")){
					String[] currentLine = line.split(" ");
					reflFile = currentLine[1];
				}
				
				line = reader.readLine();
			}

			reader.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Check out if any folder
		File f ;
		if(absolute){
			f = new File(path + "/" + fileName);
		}
		else{
			f = new File("Assets/texture/" + fileName);
		}
		if(f.isDirectory()){
			
			for (final File fileEntry : f.listFiles()) {

				if(fileEntry.getName().equals("Diffuse.png") || fileEntry.getName().equals("diffuse.png") || fileEntry.getName().equals("Albedo.png") || fileEntry.getName().equals("albedo.png") || fileEntry.getName().equals("Base_Color.png") || fileEntry.getName().equals("base_color.png") || fileEntry.getName().equals("Base_color.png")
						|| fileEntry.getName().equals("base_Color.png") || fileEntry.getName().equals("BaseColor.png") || fileEntry.getName().equals("Basecolor.png") || fileEntry.getName().equals("baseColor.png") || fileEntry.getName().equals("basecolor.png")){
					textureFile = fileName + "/" + fileEntry.getName();
				}
				else if(fileEntry.getName().equals("normal.png") || fileEntry.getName().equals("Normal.png") || fileEntry.getName().equals("Normal_OpenGL.png")){
					normalFile = fileName + "/" + fileEntry.getName();
				}
				else if(fileEntry.getName().equals("Height.png") || fileEntry.getName().equals("height.png") || fileEntry.getName().equals("DisplacementMap.png") || fileEntry.getName().equals("displacentMap.png") || fileEntry.getName().equals("displacentmap.png") || fileEntry.getName().equals("dispMap.png") || fileEntry.getName().equals("dispmap.png")){
					dispFile = fileName + "/" + fileEntry.getName();
				}
				else if(fileEntry.getName().equals("Roughness.png") || fileEntry.getName().equals("roughness.png")){
					reflFile = fileName + "/" + fileEntry.getName();
				}
				else if(fileEntry.getName().equals("Metallic.png") || fileEntry.getName().equals("metallic.png") || fileEntry.getName().equals("Metal.png") || fileEntry.getName().equals("metal.png")){
					metalFile = fileName + "/" + fileEntry.getName();
				}
			}
		}
		
		Material materialReturn = new Material(damper,reflectivity,diffuse);
		if(!textureFile.equals(""))
			materialReturn.setTextureID(loader.loadTexture(textureFile));
		
		if(!normalFile.equals(""))
			materialReturn.setNormalID(loader.loadTexture(normalFile));
		
		if(!dispFile.equals(""))
			materialReturn.setDispID(loader.loadTexture(dispFile));
		
		if(!reflFile.equals(""))
			materialReturn.setReflID(loader.loadTexture(reflFile));
		
		if(!metalFile.equals(""))
			materialReturn.setMetalID(loader.loadTexture(metalFile));				
		return materialReturn;
	}

	public static Models loadObjModels(String fileName,boolean absolute,String path, Loader loader){
		FileReader fr = null;
		try {
			if(absolute)
				fr = new FileReader(new File(fileName));
			else
				fr = new FileReader(new File("Assets/OBJ/"+fileName+".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace();
		}
		

		
		BufferedReader reader = new BufferedReader(fr);
		String line;
		String fileMaterial="";
		String Material="";

		int lastVertices = 0;
		int lastTextures = 0;
		int lastNormals = 0;

		Models models = new Models();
		
		float minX = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;

		float minY = Float.MAX_VALUE;
		float maxY = Float.MIN_VALUE;

		float minZ = Float.MAX_VALUE;
		float maxZ = Float.MIN_VALUE;
		try{
			line = "";
			while(line!=null){
				List<Vertex> vertices = new ArrayList<>();
				List<Vector3f> textures = new ArrayList<Vector3f>();
				List<Vector3f> normals = new ArrayList<Vector3f>();
				List<Integer> indices = new ArrayList<Integer>();



				int toAddVertices = 0;
				int toAddTextures = 0;
				int toAddNormals = 0;
				

				
				while(true && line!=null){
					
					String[] currentLine = line.split(" ");
					
					if(line.startsWith("mtllib ")){
						fileMaterial = currentLine[1];
					}
					else if(line.startsWith("v ")){	//Vertices position
						Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]),Float.parseFloat(currentLine[4]));
						vertices.add(new Vertex(vertices.size(),vertex));
						if(vertex.x < minX)
							minX = vertex.x;
						if(vertex.x > maxX)
							maxX = vertex.x;
						
						if(vertex.y < minY)
							minY = vertex.y;
						if(vertex.y > maxY)
							maxY = vertex.y;
						
						if(vertex.z < minZ)
							minZ = vertex.z;
						if(vertex.z > maxX)
							maxZ = vertex.z;
						toAddVertices++;
					}
					else if(line.startsWith("vt ")){  //Texture coord
						Vector3f texture = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
						textures.add(texture);
						toAddTextures++;
					}
					else if(line.startsWith("vn ")){  //normals vertex
						Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
						normals.add(normal);
						toAddNormals++;
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
					Vertex v0 = processVertex(vertex1, vertices, indices,lastVertices,lastTextures,lastNormals);
					Vertex v1 = processVertex(vertex2, vertices, indices,lastVertices,lastTextures,lastNormals);
					Vertex v2 = processVertex(vertex3, vertices, indices,lastVertices,lastTextures,lastNormals);
					calculateTangents(v0, v1, v2, textures);
					
					
					/*rocessVertex(vertex1,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
					processVertex(vertex2,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
					processVertex(vertex3,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
					if(!vertex1.equals("") && !vertex2.equals("") && !vertex3.equals(""))
						computeTangents(vertex1,vertex2,vertex3,vertices,textures,tangents);
					sizeIndices+=3;
					if(currentLine.length==5){
						String[] vertex4 = currentLine[4].split("/");
					
						processVertex(vertex4,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
						processVertex(vertex3,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
						processVertex(vertex1,indices,vertices,textures,normals,verticesArray,textureArray,normalsArray);
						sizeIndices+=3;
					}*/
					
					
					line = reader.readLine();
					
				}
				
				lastVertices += toAddVertices;
				lastTextures += toAddTextures;
				lastNormals += toAddNormals;
				
				int[] indicesArray = null;
				indicesArray = new int[indices.size()];
				
				removeUnusedVertices(vertices);
				for(int i=0;i<indices.size();i++){
					indicesArray[i] = indices.get(i);
				}
				
				/*float[] verticesFinal = new float[verticesArray.size()*3];
				float[] textureFinal = new float[textureArray.size()*3];
				float[] normalsFinal = new float[normalsArray.size()*3];
				float[] tangentsFinal = new float[verticesArray.size()*3];

				
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
				for(Vector3f vertex:verticesArray){		
					for(VertexTangent vT:vertices){
						if(vT.position.equals(vertex)){
							vT.averageTangents();
							tangentsFinal[currentTang++] = vT.averagedTangent.x;
							tangentsFinal[currentTang++] = vT.averagedTangent.y;
							tangentsFinal[currentTang++] = vT.averagedTangent.z;
							break;
						}
					}
				}*/
				float[] verticesArray = new float[vertices.size() * 3];
				float[] texturesArray = new float[vertices.size() * 3];
				float[] normalsArray = new float[vertices.size() * 3];
				float[] tangentsArray = new float[vertices.size() * 3];
				convertDataToArrays(vertices, textures, normals, verticesArray,
						texturesArray, normalsArray, tangentsArray);
	
	
				Material texture = loadMaterial(fileMaterial,absolute,path,loader,Material);
				
				models.add(new Model(loader.loadToVAO(verticesArray, texturesArray,normalsArray, indicesArray,tangentsArray),texture));
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		BoundingBox box = new BoundingBox(new Vector3f(minX,minY,minZ),new Vector3f(maxX,maxY,maxZ));
		models.setBoundingBox(box);
		return models;
	}
	
	
}

