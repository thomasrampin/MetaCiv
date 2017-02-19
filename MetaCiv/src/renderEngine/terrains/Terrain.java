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
		

		
		VERTEX_COUNT = image.getHeight();
		SIZE_X = image.getHeight();
		SIZE_Z = image.getHeight();
		
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*3];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        int ii=0;
        int jj =0;
        for(int i=0;i<VERTEX_COUNT;i++){
        	jj = 0;
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE_Z;
                vertices[vertexPointer*3+1] = getHeight(jj,ii,image,heights);
                vertices[vertexPointer*3+1] += getHeight(j,i,image2);
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE_X;
                Vector3f normal = calculateNormal(j,i,image2);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*3+1] = (float)i/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*3+2] = 0;
                vertexPointer++;
                jj++;
            }
            ii ++;
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomRight;
                indices[pointer++] = bottomLeft;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

	private Vector3f calculateNormal(int x, int z, BufferedImage image){
		float heightL = getHeight(x-1, z, image);
		float heightR = getHeight(x+1, z, image);
		float heightD = getHeight(x, z-1, image);
		float heightU = getHeight(x, z+1, image);
		
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
