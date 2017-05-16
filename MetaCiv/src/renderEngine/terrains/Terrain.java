package renderEngine.terrains;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import civilisation.Configuration;
import civilisation.world.World;
import renderEngine.loaders.Loader;
import renderEngine.materials.Material;
import renderEngine.models.Mesh;
import renderEngine.models.Model;
import renderEngine.utils.Helper;
import renderEngine.utils.TerrainTexture;
import turtlekit.kernel.Patch;

public class Terrain {

	public class face{
		Vector3f normal;
		int pos[]= new int[3];
	}
	public static float SIZE_X;
	public static float SIZE_Z;
	private static final float MAX_HEIGHT = 10;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 *256;
	private float textureCoordsOffsetX;
	private static  float[] vertices;
	
	
	private static ArrayList<TerrainTexture> effectiveType;
	private static int diffuseArray[];
	
	private float x;
	private float z;
	
	public static int VERTEX_COUNT_H; 
	public static int VERTEX_COUNT_W;
	
	private Mesh model;
	private Material texture;
	private Material blur;
	private int cliffMap;
	private int roadMap;
	
	private static BufferedImage image;
	
	public Terrain(int gridX, int gridZ, Loader loader,Material texture,ArrayList<TerrainTexture> arrayList){
		this.texture = texture;
		SIZE_X = 800;
		SIZE_Z = 800;
		this.x = gridX * SIZE_X;
		this.z = gridZ * SIZE_Z;
		effectiveType = new ArrayList<>();
		image = null;
		try {
			image = ImageIO.read(new File("Assets/Texture/heightmap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.model = generateTerrain(loader,image,image,arrayList);
		this.blur = new Material(Loader.loadTextureBlur(image));
		
		File f = new File(Configuration.pathToRessources+ "/Skin/road");
		if(f.isDirectory())
			this.roadMap = loader.loadTextureAtlas(Configuration.pathToRessources+ "/Skin/road");
		else
			this.roadMap = loader.loadTextureAtlas("Assets/Texture/road");
		
		f = new File(Configuration.pathToRessources+ "/Skin/cliff");
		if(f.isDirectory())
			this.cliffMap = loader.loadTextureAtlas(Configuration.pathToRessources+ "/Skin/cliff");
		else
			this.cliffMap = loader.loadTextureAtlas("Assets/Texture/cliff");
		
	}
	
	public static Vector3f getHeightByTab(int x,int y){
		Vector3f position1 = new Vector3f(vertices[(y*VERTEX_COUNT_W+x)*3],vertices[(y*VERTEX_COUNT_W+x)*3+1],vertices[(y*VERTEX_COUNT_W+x)*3+2]);
	
		return position1;
	}
	
	public static ArrayList<Vector4f> getAllHeightOnePoly(int x,int y){
		Vector3f position1 = new Vector3f(vertices[(y*VERTEX_COUNT_W+x)*3],vertices[(y*VERTEX_COUNT_W+x)*3+1],vertices[(y*VERTEX_COUNT_W+x)*3+2]);
		Vector3f position2 = new Vector3f(vertices[((y-1)*VERTEX_COUNT_W+x)*3],vertices[((y-1)*VERTEX_COUNT_W+x)*3+1],vertices[((y-1)*VERTEX_COUNT_W+x)*3+2]);
		Vector3f position3 = new Vector3f(vertices[(y*VERTEX_COUNT_W+x-1)*3],vertices[(y*VERTEX_COUNT_W+x-1)*3+1],vertices[(y*VERTEX_COUNT_W+x-1)*3+2]);
		Vector3f position4 = new Vector3f(vertices[((y-1)*VERTEX_COUNT_W+x-1)*3],vertices[((y-1)*VERTEX_COUNT_W+x-1)*3+1],vertices[((y-1)*VERTEX_COUNT_W+x-1)*3+2]);
		ArrayList<Vector4f> positions = new ArrayList<>();
		positions.add(new Vector4f(position1.x,position1.y,position1.z,1.0f));
		positions.add(new Vector4f(position2.x,position2.y,position2.z,1.0f));
		positions.add(new Vector4f(position3.x,position3.y,position3.z,1.0f));
		positions.add(new Vector4f(position4.x,position4.y,position4.z,1.0f));
		return positions;
	}
	
	public static float getRoll(int x,int y){
		Vector3f position = new Vector3f(vertices[(y*VERTEX_COUNT_W+x)*3],vertices[(y*VERTEX_COUNT_W+x)*3+1],vertices[(y*VERTEX_COUNT_W+x)*3+2]);
		Vector3f position2 = new Vector3f(vertices[(y*VERTEX_COUNT_W+x-1)*3],vertices[(y*VERTEX_COUNT_W+x)*3+1],vertices[(y*VERTEX_COUNT_W+x-1)*3+2]);
		Vector3f position3 = new Vector3f(vertices[(y*VERTEX_COUNT_W+x-1)*3],vertices[(y*VERTEX_COUNT_W+x-1)*3+1],vertices[(y*VERTEX_COUNT_W+x-1)*3+2]);

		return (position3.y<position.y)?Helper.getAngle(position, position2, position3):360-Helper.getAngle(position, position2, position3);
	}
	
	public static float getPitch(int x,int y){
		Vector3f position = new Vector3f(vertices[(y*VERTEX_COUNT_W+x)*3],vertices[(y*VERTEX_COUNT_W+x)*3+1],vertices[(y*VERTEX_COUNT_W+x)*3+2]);
		Vector3f position2 = new Vector3f(vertices[((y-1)*VERTEX_COUNT_W+x)*3],vertices[(y*VERTEX_COUNT_W+x)*3+1],vertices[((y-1)*VERTEX_COUNT_W+x)*3+2]);
		Vector3f position3 = new Vector3f(vertices[((y-1)*VERTEX_COUNT_W+x)*3],vertices[((y-1)*VERTEX_COUNT_W+x)*3+1],vertices[((y-1)*VERTEX_COUNT_W+x)*3+2]);

		return (position3.y>position.y)?Helper.getAngle(position, position2, position3):360-Helper.getAngle(position, position2, position3);
	}
	
	private Mesh generateTerrain(Loader loader , BufferedImage image,BufferedImage image2,ArrayList<TerrainTexture> textures){
		System.out.println("Generate Terrain...");
		effectiveType = new ArrayList<>();
		//image = Loader.blur(image);
		
		VERTEX_COUNT_H = image.getHeight()/World.getAccuracy();
		VERTEX_COUNT_W = image.getWidth()/World.getAccuracy();
		SIZE_X = World.getY()*World.getSize3D();
		SIZE_Z = World.getX()*World.getSize3D();
		
        int count = VERTEX_COUNT_H * VERTEX_COUNT_W;
        vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*3];
        float[] tangents = new float[count*3];
        ArrayList<Vector3f> ver = new ArrayList<>();
        ArrayList<Vector3f> norm = new ArrayList<>();
        ArrayList<Vector3f> tc = new ArrayList<>();
        
        
        Vector3f[] pos = new Vector3f[count];
        Vector3f[] Uv = new Vector3f[count];
        int[] indices = new int[6*(VERTEX_COUNT_H-1)*(VERTEX_COUNT_W-1)];
        face[] faces  = new face[6*(VERTEX_COUNT_H-1)*(VERTEX_COUNT_W-1)];
        int vertexPointer = 0;
        int ii=0;
        int jj =0;
        
        
        for(int i=0;i<VERTEX_COUNT_H;i++){
        	jj = 0;
            for(int j=0;j<VERTEX_COUNT_W;j++){
            	float h;
            	h = getHeight(jj,ii,image,textures) ;
            	
        		
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT_H - 1) * SIZE_X;
                vertices[vertexPointer*3+1] = h;
                if(!World.getHeightMap().equals(""))
                	vertices[vertexPointer*3+1] += getHeight(j%image2.getHeight(),i%image2.getWidth());
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT_W - 1) * SIZE_Z;
               
                
                
                pos[vertexPointer] = new Vector3f(vertices[vertexPointer*3],vertices[vertexPointer*3+1],vertices[vertexPointer*3+2]);
                ver.add(new Vector3f(vertices[vertexPointer*3],vertices[vertexPointer*3+1],vertices[vertexPointer*3+2]));

				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 0;
				normals[vertexPointer*3+2] = 0;
				tangents[vertexPointer*3] = 0;
				tangents[vertexPointer*3+1] = 0;
				tangents[vertexPointer*3+2] = 0;
               
               
                textureCoords[vertexPointer*3] = (float)j/((float)VERTEX_COUNT_W - 1);
                textureCoords[vertexPointer*3+1] = (float)i/((float)VERTEX_COUNT_H - 1);
                textureCoords[vertexPointer*3+2] = 0;
                Uv[vertexPointer] = new Vector3f(textureCoords[vertexPointer*3],textureCoords[vertexPointer*3+1],textureCoords[vertexPointer*3+2]);
                vertexPointer++;
                
               
                
                jj+=World.getAccuracy();
            }
            ii +=World.getAccuracy();
        }
        
        

        

        
        int pointer = 0;
        int k = 0;
        int[] diviseur =  new int[count * 3];

        for(int z=0;z<VERTEX_COUNT_H-1;z++){
            for(int x=0;x<VERTEX_COUNT_W-1;x++){
                int topLeft = (z*VERTEX_COUNT_W)+x;
                int topRight = topLeft + 1;
                int bottomLeft = ((z+1)*VERTEX_COUNT_W)+x;
                
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
        		Vector3f a = ver.get(topLeft);
        		Vector3f b = ver.get(bottomLeft);
        		Vector3f c = ver.get(topRight);
        		Vector3f vp1 = new Vector3f(b.x-a.x,b.y-a.y,b.z-a.z);
        		Vector3f vp2 = new Vector3f(c.x-a.x,c.y-a.y,c.z-a.z);
        		Vector3f v1 = new Vector3f(vp1.getX(),vp1.getY(),vp1.getZ());
        		Vector3f v2 = new Vector3f(vp2.getX(),vp2.getY(),vp2.getZ());
        		Vector3f normal = Helper.Vectoriel(v1,v2);
        		
        		if(normal.length()>0)
        			normal.normalise();
        		faces[k] = new face();
        		faces[k].normal = normal;
				faces[k].pos[0] =topLeft;
				faces[k].pos[1] =bottomLeft; 
				faces[k].pos[2] =topRight; 
                k++;
				
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
                
        		a = ver.get(topRight);
        		b = ver.get(bottomLeft);
        		c = ver.get(bottomRight);
        		vp1 = new Vector3f(b.x-a.x,b.y-a.y,b.z-a.z);
        		vp2 = new Vector3f(c.x-a.x,c.y-a.y,c.z-a.z);
        		v1 = new Vector3f(vp1.getX(),vp1.getY(),vp1.getZ());
        		v2 = new Vector3f(vp2.getX(),vp2.getY(),vp2.getZ());
        		normal = Helper.Vectoriel(v1,v2);
        		
        		if(normal.length()>0)
        			normal.normalise();
        		faces[k] = new face();
        		faces[k].normal = normal;
				faces[k].pos[0] =topRight;
				faces[k].pos[1] =bottomLeft; 
				faces[k].pos[2] =bottomRight; 
                normals[topLeft*3] += faces[k-1].normal.getX();
                normals[topLeft*3+1] += faces[k-1].normal.getY();
                normals[topLeft*3+2] += faces[k-1].normal.getZ();
                diviseur[topLeft] ++;
                
                normals[bottomLeft*3] += faces[k-1].normal.getX();
                normals[bottomLeft*3+1] += faces[k-1].normal.getY();
                normals[bottomLeft*3+2] += faces[k-1].normal.getZ();
                diviseur[bottomLeft] ++;
                
                normals[topRight*3] += faces[k-1].normal.getX();
                normals[topRight*3+1] += faces[k-1].normal.getY();
                normals[topRight*3+2] += faces[k-1].normal.getZ();
                diviseur[topRight] ++;
                
                
                normals[topRight*3] += faces[k].normal.getX();
                normals[topRight*3+1] += faces[k].normal.getY();
                normals[topRight*3+2] += faces[k].normal.getZ();
                diviseur[topRight] ++;
                
                normals[bottomLeft*3] += faces[k].normal.getX();
                normals[bottomLeft*3+1] += faces[k].normal.getY();
                normals[bottomLeft*3+2] += faces[k].normal.getZ();
                diviseur[bottomLeft] ++;
                
                normals[bottomRight*3] += faces[k].normal.getX();
                normals[bottomRight*3+1] += faces[k].normal.getY();
                normals[bottomRight*3+2] += faces[k].normal.getZ();
                diviseur[bottomRight] ++;
                k++;
            }
        }
        
        //Compute tangents
       
        for(int i=0;i<pointer-3;i+=3){
	        Vector3f deltaPos1 = Vector3f.sub(pos[indices[i+1]], pos[indices[i]],null);
	        Vector3f deltaPos2 = Vector3f.sub(pos[indices[i+2]], pos[indices[i]],null);
	
	        
	        Vector3f deltaUv1 = Vector3f.sub(Uv[indices[i+1]], Uv[indices[i]],null);
	        Vector3f deltaUv2 = Vector3f.sub(Uv[indices[i+2]], Uv[indices[i]],null);

	        float r = 1f/(deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
	        //System.out.println("Pos1 " + deltaPos1.toString() + " Pos2 " + deltaPos2.toString() + " UV1 " + deltaUv1.toString() + " UV2 " + deltaUv2.toString());
	        
	        deltaPos1.scale(deltaUv2.y);
	        deltaPos2.scale(deltaUv1.y);
			Vector3f tangent = Vector3f.sub(deltaPos1, deltaPos2, null);
			tangent.scale(r);
			tangent.normalise();
			
	        tangents[indices[i]*3] = tangent.x;
	        tangents[indices[i]*3+1] =  tangent.y;
	        tangents[indices[i]*3+2] =  tangent.z;
	        
	        tangents[indices[i]*3] = tangent.x;
	        tangents[indices[i+1]*3+1] = tangent.y;
	        tangents[indices[i+2]*3+2] =  tangent.z;
	        
	        tangents[indices[i]*3] = tangent.x;
	        tangents[indices[i+1]*3+1] = tangent.y;
	        tangents[indices[i+2]*3+2] =  tangent.z;
	        //k+=3;
        }
        
       /* for(int i=0;i<count-3;i+=3){
        	Vector3f tangent = new Vector3f(tangents[i],tangents[i+1],tangents[i+2]);
        	if(tangent.length()>0)
        		tangent.normalise();
        	tangents[i] += tangent.x;
 	        tangents[i+1] += tangent.y;
 	        tangents[i+2] +=  tangent.z;
        	
        }*/
        
        /*int[] dividande =  new int[count * 3];
        int kk=0;
        for(int i=0;i<count * 3;i++){
	   		for(int j=0;j<k;j++){
	   			for(int m=0;m<3;m++){
	   				if(faces[j].pos[m] == i){
	                       normals[kk] += faces[j].normal.getX();
	                       normals[kk+1] += faces[j].normal.getY();
	                       normals[kk+2] += faces[j].normal.getZ();
	                       dividande[i] ++;
	                       break;
	   				}
	   			}
	   		}
	   		kk+=3;
        }*/
        
    	k = 0;
    	for(int i=0;i<count*3;i+=3){
    		normals[i] /= diviseur[k];
    		normals[i+1] /= diviseur[k];
    		normals[i+2] /= diviseur[k];
    		k++;
                   
    	}
    	
    	/*for(int i=0;i<count*3;i+=3){
    		float norme = (float) Math.sqrt((normals[i]*normals[i])+(normals[i+1]*normals[i+1])+(normals[i+2]*normals[i+2]));
    		normals[i] /= norme;
    		normals[i+1] /= norme;
    		normals[i+2] /= norme;
    	
    	}*/
    	
    	TerrainTexture.sort(effectiveType);
    	System.out.println("Task done");
        return loader.loadToVAO(vertices, textureCoords, normals,tangents, indices);
    }


	
	public static int getImageHeight(){
		return image.getHeight();
	}
	
	public static int getImageWidth(){
		return image.getWidth();
	}
	
	public static float getHeight(int x, int z){
		if(x<0 || x>= image.getHeight() || z<0 || z>=image.getHeight()){
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOUR /2f;
		height /= MAX_PIXEL_COLOUR /2f;
		height *= World.getIntensityHeight();
		
		return height;
	}
	
	private static float getHeight2(int x, int z, BufferedImage image,ArrayList<TerrainTexture> textures){
		if(x<0 || x>= image.getWidth() || z<0 || z>=image.getHeight()){
			return 0;
		}
		int rgb = image.getRGB(x, z);
		float red = (rgb >> 16) & 0x000000FF;
		float green = (rgb >>8 ) & 0x000000FF;
		float blue = (rgb) & 0x000000FF;
		float height = 0.0f;
		
		
		for(int i=0;i<textures.size();i++){
			Vector4f vector = new Vector4f(textures.get(i).getR(),textures.get(i).getG(),textures.get(i).getB(),textures.get(i).getHeight());
			
			
			
			float red2 = vector.x;
			float green2 = vector.y;
			float blue2 = vector.z;
			
			
			if(red2 == red && green2 == green && blue2 == blue){
				height = vector.w;
				if(!effectiveType.contains(textures.get(i))){
					effectiveType.add(textures.get(i));
				}
				break;
			}
		}
		
		return height;
	}
	
	private static int getErosion(int x, int z, BufferedImage image,ArrayList<TerrainTexture> textures){
		if(x<0 || x>= image.getWidth() || z<0 || z>=image.getHeight()){
			return 0;
		}
		int rgb = image.getRGB(x, z);
		float red = (rgb >> 16) & 0x000000FF;
		float green = (rgb >>8 ) & 0x000000FF;
		float blue = (rgb) & 0x000000FF;
		int erosion = 0;
		
		
		for(int i=0;i<textures.size();i++){
			Vector4f vector = new Vector4f(textures.get(i).getR(),textures.get(i).getG(),textures.get(i).getB(),textures.get(i).getErosion());
			
			
			
			float red2 = vector.x;
			float green2 = vector.y;
			float blue2 = vector.z;
			
			
			if(red2 == red && green2 == green && blue2 == blue){
				erosion = (int) vector.w;
				break;
			}
		}
		return erosion;
	}
	
	private static int getBlur(int x, int z, BufferedImage image,ArrayList<TerrainTexture> textures){
		if(x<0 || x>= image.getWidth() || z<0 || z>=image.getHeight()){
			return 0;
		}
		int rgb = image.getRGB(x, z);
		float red = (rgb >> 16) & 0x000000FF;
		float green = (rgb >>8 ) & 0x000000FF;
		float blue = (rgb) & 0x000000FF;
		int erosion = 0;
		
		
		for(int i=0;i<textures.size();i++){
			Vector4f vector = new Vector4f(textures.get(i).getR(),textures.get(i).getG(),textures.get(i).getB(),textures.get(i).getBlur());
			
			
			
			float red2 = vector.x;
			float green2 = vector.y;
			float blue2 = vector.z;
			
			
			if(red2 == red && green2 == green && blue2 == blue){
				erosion = (int) vector.w;
				break;
			}
		}
		return erosion;
	}
	
	public static float getHeight(int x, int z, BufferedImage image,ArrayList<TerrainTexture> textures){
		
		int N=getErosion(x,z,image,textures);
		
		int dividande=0;
		int merge = 3;
		float height = 0;
		float heightL,heightR,heightD,heightU;
		int algo = getBlur(x,z,image,textures);
		if(algo==0){
			for(int i=0;i<N;i++){

				if(i%2==0){
					heightL = getHeight2(Math.max(x-i*merge,0), z, image,textures);
					heightR = getHeight2(Math.min(x+i*merge,image.getWidth()), z, image,textures);
					heightD = getHeight2(x, Math.max(z-i*merge,0), image,textures);
					heightU = getHeight2(x, Math.min(z+i*merge,image.getHeight()), image,textures);
				}else{
					heightL = getHeight2(Math.max(x-(i-1)*merge,0), Math.max(z-(i-1)*merge,0), image,textures);
					heightR = getHeight2(Math.min(x+(i-1)*merge,image.getWidth()), Math.min(z+(i-1)*merge,image.getHeight()), image,textures);
					heightD = getHeight2(Math.min(x+(i-1)*merge,image.getWidth()), Math.max(z-(i-1)*merge,0), image,textures);
					heightU = getHeight2(Math.max(x-(i-1)*merge,0), Math.min(z+(i-1)*merge,image.getHeight()), image,textures);				
				}
				
				dividande+=4;
				height += heightL+heightR+heightD+heightU;
			}
			height/=dividande;

		}
		else{
			int i=0;
			while(i<N){
				
				int l=-1*(i+1);
				int m=0;
				int h=-1*(i+1);
				while(h<-1*(i+1)*-1){
					
					height += getHeight2(Math.min(Math.max(x+l*merge,0),image.getWidth()), Math.min(Math.max(z+h*merge,0),image.getHeight()), image,textures);
					l++;
					m++;
					if(m>=(2*(i+1)+1) ){
						h++;
						m=0;
						l=-1*(i+1);
					}
					dividande++;
					
				}
				i++;
			}
			height/=dividande;

		}
		
		return height;
	}
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public Mesh getModel() {
		return model;
	}

	public Material getTexture() {
		return texture;
	}

	public void notifyHeightMap(Loader loader, BufferedImage image, Vector2f gridSize,ArrayList<TerrainTexture> textures) {
		
		BufferedImage image2 = null;
		try {
			if(!World.getHeightMap().equals(""))
				image2 = ImageIO.read(new File(Configuration.pathToRessources+ "/Skin/" + World.getHeightMap() + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.model = generateTerrain(loader,image,image2,textures);
		this.texture = new Material(Loader.loadTexture(image));
		
		diffuseArray = Loader.loadTextureAtlas(effectiveType);
	}

	public static ArrayList<TerrainTexture> getEffectiveType() {
		return effectiveType;
	}

	public Material getBlur() {
		return blur;
	}
	
	public static int[] getTextureArray(){
		return diffuseArray;
	}
	
	public int getCliffMap(){
		return cliffMap;
	}
	
	public int getRoadMap(){
		return roadMap;
	}
}
