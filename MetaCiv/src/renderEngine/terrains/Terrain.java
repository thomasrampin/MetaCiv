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

import renderEngine.loaders.Loader;
import renderEngine.materials.Material;
import renderEngine.models.Mesh;
import renderEngine.models.Model;
import renderEngine.utils.Helper;
import turtlekit.kernel.Patch;

public class Terrain {

	private float SIZE_X;
	private float SIZE_Z;
	private static final float MAX_HEIGHT = 10;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 *256;
	
	private Vector2f gridSize;
	
	private float x;
	private float z;
	
	private int VERTEX_COUNT; 
	
	private Mesh model;
	private Material texture;
	
	public Terrain(int gridX, int gridZ, Loader loader,Material texture,ArrayList<Vector4f> heights){
		this.texture = texture;
		SIZE_X = 800;
		SIZE_Z = 800;
		this.x = gridX * SIZE_X;
		this.z = gridZ * SIZE_Z;
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("Assets/Texture/heightmap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		gridSize = new Vector2f(image.getWidth(),image.getHeight());
		this.model = generateTerrain(loader,image,image,heights);
	}
	

	
	private Mesh generateTerrain(Loader loader , BufferedImage image,BufferedImage image2,ArrayList<Vector4f> heights){
		

		
		VERTEX_COUNT = image.getHeight()/5;
		int VERTEX_COUNT_W = image.getHeight()/5;
		SIZE_X = image.getHeight();
		SIZE_Z = image.getHeight();
		
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*3];
        float[] tangents = new float[count*3];
        Vector3f[] pos = new Vector3f[count];
        Vector3f[] Uv = new Vector3f[count];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        int ii=0;
        int jj =0;
        for(int i=0;i<VERTEX_COUNT;i++){
        	jj = 0;
            for(int j=0;j<VERTEX_COUNT_W;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT_W - 1) * SIZE_Z;
                vertices[vertexPointer*3+1] = getHeight(jj,ii,image,heights);
                vertices[vertexPointer*3+1] += getHeight(jj%image2.getHeight(),ii%image2.getWidth(),image2);
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE_X;
                pos[vertexPointer] = new Vector3f(vertices[vertexPointer*3],vertices[vertexPointer*3+1],vertices[vertexPointer*3+2]);
                Vector3f normal = calculateNormal2(jj,ii,image,heights);
               if(normal.equals(new Vector3f(0,1,0))){
	            	Vector3f normal2 = calculateNormal(jj%image2.getHeight(),ii%image2.getWidth(),image2);
	                normals[vertexPointer*3] = normal2.x;
	                normals[vertexPointer*3+1] = normal2.y;
	                normals[vertexPointer*3+2] = normal2.z;
               }
               else{
	                normals[vertexPointer*3] = normal.x;
	                normals[vertexPointer*3+1] = normal.y;
	                normals[vertexPointer*3+2] = normal.z;
                }
       
                textureCoords[vertexPointer*3] = (float)j/((float)VERTEX_COUNT_W - 1);
                textureCoords[vertexPointer*3+1] = (float)i/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*3+2] = 0;
                Uv[vertexPointer] = new Vector3f(textureCoords[vertexPointer*3],textureCoords[vertexPointer*3+1],textureCoords[vertexPointer*3+2]);
                vertexPointer++;
                
                
                
                jj+=5;
            }
            ii +=5;
        }
        
        //Compute tangents
        for(int i=0;i<count-3;i++){
	        Vector3f deltaPos1 = Helper.soustrate(pos[i+1], pos[i]);
	        Vector3f deltaPos2 = Helper.soustrate(pos[i+2], pos[i]);
	
	        
	        Vector3f deltaUv1 = Helper.soustrate(Uv[i+1], Uv[i]);
	        Vector3f deltaUv2 = Helper.soustrate(Uv[i+2], Uv[i]);

	        float r = 1f/(deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
	        
	        Vector3f t = Helper.multiply(Helper.soustrate(Helper.multiply(deltaPos1, deltaUv2.y),Helper.multiply(deltaPos2, deltaUv1.y)),r);
	        
	        tangents[i*3] = r * (deltaUv2.y * deltaPos1.x - deltaUv1.y * deltaPos2.x);
	        tangents[i*3+1] = r * (deltaUv2.y * deltaPos1.y - deltaUv1.y * deltaPos2.y);
	        tangents[i*3+2] =  r * (deltaUv2.y * deltaPos1.z - deltaUv1.y * deltaPos2.z);
        }
        
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT_W-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT_W)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals,tangents, indices);
    }

	private Vector3f calculateNormal(int x, int z, BufferedImage image){
		float heightL = getHeight(x-5, z, image);
		float heightR = getHeight(x+5, z, image);
		float heightD = getHeight(x, z-5, image);
		float heightU = getHeight(x, z+5, image);
		
		Vector3f normal = new Vector3f(heightL - heightR, 2f  , heightD - heightU);
		normal.normalise();
		return normal;
	}

	private Vector3f calculateNormal2(int x, int z, BufferedImage image, ArrayList<Vector4f> heights){
		float heightL = getHeight(x-5, z, image,heights);
		float heightR = getHeight(x+5, z, image,heights);
		float heightD = getHeight(x, z-5, image,heights);
		float heightU = getHeight(x, z+5, image,heights);
		
		Vector3f normal = new Vector3f(heightL - heightR, 2f  , heightD - heightU);
		normal.normalise();
		return normal;
	}
	
	private float getHeight(int x, int z, BufferedImage image){
		if(x<0 || x>= image.getHeight() || z<0 || z>=image.getHeight()){
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOUR /2f;
		height /= MAX_PIXEL_COLOUR /2f;
		height *= MAX_HEIGHT;
		
		return height;
	}
	
	private float getHeight(int x, int z, BufferedImage image,ArrayList<Vector4f> heights){
		if(x<0 || x>= image.getWidth() || z<0 || z>=image.getHeight()){
			return 0;
		}
		int rgb = image.getRGB(x, z);
		float red = (rgb >> 16) & 0x000000FF;
		float green = (rgb >>8 ) & 0x000000FF;
		float blue = (rgb) & 0x000000FF;
		float height = 0.0f;
		
		
		for(Vector4f vector:heights){
			
			int color = Color.HSBtoRGB(vector.x, vector.y, vector.z);
			float red2 = (color >> 16) & 0x000000FF;
			float green2 = (color >>8 ) & 0x000000FF;
			float blue2 = (color) & 0x000000FF;
			if(red2 == red && green2 == green && blue2 == blue){
				height = vector.w*10;
				break;
			}
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

	public void notifyHeightMap(Loader loader, BufferedImage image, Vector2f gridSize,ArrayList<Vector4f> heights) {
		this.gridSize = gridSize;
		BufferedImage image2 = null;
		try {
			image2 = ImageIO.read(new File("Assets/Texture/heightmap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.model = generateTerrain(loader,image,image2,heights);
		this.texture = new Material(Loader.loadTexture(image));
	}
	
	
	
	
}
