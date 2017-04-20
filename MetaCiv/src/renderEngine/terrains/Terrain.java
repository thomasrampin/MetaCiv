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

	public class face{
		Vector3f normal;
		int pos[]= new int[3];
	}
	private float SIZE_X;
	private float SIZE_Z;
	private static final float MAX_HEIGHT = 10;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 *256;
	private float textureCoordsOffsetX;
	private static  float[] vertices;
	private Vector2f gridSize;
	
	private float x;
	private float z;
	
	private static int VERTEX_COUNT; 
	
	private Mesh model;
	private Material texture;
	private Material blur;
	
	private static BufferedImage image;
	
	public Terrain(int gridX, int gridZ, Loader loader,Material texture,ArrayList<Vector4f> heights){
		this.texture = texture;
		SIZE_X = 800;
		SIZE_Z = 800;
		this.x = gridX * SIZE_X;
		this.z = gridZ * SIZE_Z;
		image = null;
		try {
			image = ImageIO.read(new File("Assets/Texture/heightmap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		gridSize = new Vector2f(image.getWidth(),image.getHeight());
		this.model = generateTerrain(loader,image,image,heights);
		this.blur = new Material(Loader.loadTextureBlur(image));
	}
	
	public static Vector3f getHeightByTab(int x,int y){
		Vector3f position1 = new Vector3f(vertices[(y*VERTEX_COUNT+x)*3],vertices[(y*VERTEX_COUNT+x)*3+1],vertices[(y*VERTEX_COUNT+x)*3+2]);
		Vector3f position2 = new Vector3f(vertices[((y-1)*VERTEX_COUNT+x-1)*3],vertices[((y-1)*VERTEX_COUNT+x-1)*3+1],vertices[((y-1)*VERTEX_COUNT+x-1)*3+2]);
		Vector3f position = new Vector3f((position1.x+position2.x)/2.0f,(position1.y+position2.y)/2.0f,(position1.z+position2.z)/2.0f);
		return position;
	}
	
	public static ArrayList<Vector4f> getAllHeightOnePoly(int x,int y){
		Vector3f position1 = new Vector3f(vertices[(y*VERTEX_COUNT+x)*3],vertices[(y*VERTEX_COUNT+x)*3+1],vertices[(y*VERTEX_COUNT+x)*3+2]);
		Vector3f position2 = new Vector3f(vertices[((y-1)*VERTEX_COUNT+x)*3],vertices[((y-1)*VERTEX_COUNT+x)*3+1],vertices[((y-1)*VERTEX_COUNT+x)*3+2]);
		Vector3f position3 = new Vector3f(vertices[(y*VERTEX_COUNT+x-1)*3],vertices[(y*VERTEX_COUNT+x-1)*3+1],vertices[(y*VERTEX_COUNT+x-1)*3+2]);
		Vector3f position4 = new Vector3f(vertices[((y-1)*VERTEX_COUNT+x-1)*3],vertices[((y-1)*VERTEX_COUNT+x-1)*3+1],vertices[((y-1)*VERTEX_COUNT+x-1)*3+2]);
		ArrayList<Vector4f> positions = new ArrayList<>();
		positions.add(new Vector4f(position1.x,position1.y,position1.z,1.0f));
		positions.add(new Vector4f(position2.x,position2.y,position2.z,1.0f));
		positions.add(new Vector4f(position3.x,position3.y,position3.z,1.0f));
		positions.add(new Vector4f(position4.x,position4.y,position4.z,1.0f));
		return positions;
	}
	
	public static float getRoll(int x,int y){
		Vector3f position = new Vector3f(vertices[(y*VERTEX_COUNT+x)*3],vertices[(y*VERTEX_COUNT+x)*3+1],vertices[(y*VERTEX_COUNT+x)*3+2]);
		Vector3f position2 = new Vector3f(vertices[(y*VERTEX_COUNT+x-1)*3],vertices[(y*VERTEX_COUNT+x)*3+1],vertices[(y*VERTEX_COUNT+x-1)*3+2]);
		Vector3f position3 = new Vector3f(vertices[(y*VERTEX_COUNT+x-1)*3],vertices[(y*VERTEX_COUNT+x-1)*3+1],vertices[(y*VERTEX_COUNT+x-1)*3+2]);

		return (position3.y<position.y)?Helper.getAngle(position, position2, position3):360-Helper.getAngle(position, position2, position3);
	}
	
	public static float getPitch(int x,int y){
		Vector3f position = new Vector3f(vertices[(y*VERTEX_COUNT+x)*3],vertices[(y*VERTEX_COUNT+x)*3+1],vertices[(y*VERTEX_COUNT+x)*3+2]);
		Vector3f position2 = new Vector3f(vertices[((y-1)*VERTEX_COUNT+x)*3],vertices[(y*VERTEX_COUNT+x)*3+1],vertices[((y-1)*VERTEX_COUNT+x)*3+2]);
		Vector3f position3 = new Vector3f(vertices[((y-1)*VERTEX_COUNT+x)*3],vertices[((y-1)*VERTEX_COUNT+x)*3+1],vertices[((y-1)*VERTEX_COUNT+x)*3+2]);

		return (position3.y>position.y)?Helper.getAngle(position, position2, position3):360-Helper.getAngle(position, position2, position3);
	}
	
	private Mesh generateTerrain(Loader loader , BufferedImage image,BufferedImage image2,ArrayList<Vector4f> heights){
		System.out.println("Generate Terrain...");

		//image = Loader.blur(image);
		VERTEX_COUNT = image.getHeight()/5;
		int VERTEX_COUNT_W = image.getHeight()/5;
		SIZE_X = image.getHeight();
		SIZE_Z = image.getHeight();
		
        int count = VERTEX_COUNT * VERTEX_COUNT;
        vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*3];
        float[] tangents = new float[count*3];
        ArrayList<Vector3f> ver = new ArrayList<>();
        ArrayList<Vector3f> norm = new ArrayList<>();
        ArrayList<Vector3f> tc = new ArrayList<>();
        
        
        Vector3f[] pos = new Vector3f[count];
        Vector3f[] Uv = new Vector3f[count];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        face[] faces  = new face[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        int ii=0;
        int jj =0;
        float lastH = 0;
        float max = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
        	jj = 0;
            for(int j=0;j<VERTEX_COUNT_W;j++){
            	float h;
            	h = getHeight(jj,ii,image,heights) ;
            	
        		
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT_W - 1) * SIZE_Z;
                vertices[vertexPointer*3+1] = h;
                vertices[vertexPointer*3+1] += getHeight(jj%image2.getHeight(),ii%image2.getWidth());
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE_X;
               
                
                
                pos[vertexPointer] = new Vector3f(vertices[vertexPointer*3],vertices[vertexPointer*3+1],vertices[vertexPointer*3+2]);
                ver.add(new Vector3f(vertices[vertexPointer*3],vertices[vertexPointer*3+1],vertices[vertexPointer*3+2]));

				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 0;
				normals[vertexPointer*3+2] = 0;

  
       
               
               
                textureCoords[vertexPointer*3] = (float)j/((float)VERTEX_COUNT_W - 1);
                textureCoords[vertexPointer*3+1] = (float)i/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*3+2] = 0;
                Uv[vertexPointer] = new Vector3f(textureCoords[vertexPointer*3],textureCoords[vertexPointer*3+1],textureCoords[vertexPointer*3+2]);
                vertexPointer++;
                
                lastH = h;
                
                jj+=5;
            }
            ii +=5;
        }
        
        

        
        //Compute tangents
        /*int k = 0;
        for(int i=0;i<count-3;i++){
	        Vector3f deltaPos1 = Vector3f.sub(pos[i+1], pos[i],null);
	        Vector3f deltaPos2 = Vector3f.sub(pos[i+2], pos[i],null);
	
	        
	        Vector3f deltaUv1 = Vector3f.sub(Uv[i+1], Uv[i],null);
	        Vector3f deltaUv2 = Vector3f.sub(Uv[i+2], Uv[i],null);

	        float r = 1f/(deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
	        
	        
	        deltaPos1.scale(deltaUv2.y);
	        deltaPos2.scale(deltaUv1.y);
			Vector3f tangent = Vector3f.sub(deltaPos1, deltaPos2, null);
			tangent.scale(r);
	        tangents[k] = 0;
	        tangents[k+1] = 0;
	        tangents[k+2] =  1;
	        k+=3;
        }*/
        
        int pointer = 0;
        int k=0;
        for(int gz=0;gz<VERTEX_COUNT_W-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT_W)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
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
        		
        		normal.normalise();
        		faces[k] = new face();
        		faces[k].normal = normal;
				faces[k].pos[0] =topRight;
				faces[k].pos[1] =bottomLeft; 
				faces[k].pos[2] =bottomRight; 
                k++;
            }
        }
        

        int[] dividande =  new int[count * 3];
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
        }
        
    	k = 0;
    	for(int i=0;i<count*3;i+=3){
    		normals[i] /= dividande[k];
    		normals[i+1] /= dividande[k];
    		normals[i+2] /= dividande[k];
    		k++;
                   
    	}
    	
    	/*for(int i=0;i<count*3;i+=3){
    		float norme = (float) Math.sqrt((normals[i]*normals[i])+(normals[i+1]*normals[i+1])+(normals[i+2]*normals[i+2]));
    		normals[i] /= norme;
    		normals[i+1] /= norme;
    		normals[i+2] /= norme;
    	
    	}*/
    	
    	System.out.println("Task done");
        return loader.loadToVAO(vertices, textureCoords, normals,tangents, indices);
    }

	private Vector3f calculateNormal(int x, int z, BufferedImage image){
		float heightL = getHeight(x-5, z);
		float heightR = getHeight(x+5, z );
		float heightD = getHeight(x, z-5 );
		float heightU = getHeight(x, z+5);
		
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
		height *= MAX_HEIGHT;
		
		return height;
	}
	
	private static float getHeight2(int x, int z, BufferedImage image,ArrayList<Vector4f> heights){
		if(x<0 || x>= image.getWidth() || z<0 || z>=image.getHeight()){
			return 0;
		}
		int rgb = image.getRGB(x, z);
		float red = (rgb >> 16) & 0x000000FF;
		float green = (rgb >>8 ) & 0x000000FF;
		float blue = (rgb) & 0x000000FF;
		float height = 0.0f;
		
		
		for(int i=0;i<heights.size();i++){
			Vector4f vector = heights.get(i);
			
			
			int color = Color.HSBtoRGB(vector.x, vector.y, vector.z);
			float red2 = (color >> 16) & 0x000000FF;
			float green2 = (color >>8 ) & 0x000000FF;
			float blue2 = (color) & 0x000000FF;
			
			
			if(red2 == red && green2 == green && blue2 == blue){
				height = vector.w*10;
				break;
			}/*else{
				for(int j=0;j<heights.size()-1;j++){
					if(true){
						Vector4f vectorS = heights.get(j);
						int colo2 = Color.HSBtoRGB(vectorS.x, vectorS.y, vectorS.z);
						float red3 = (colo2 >> 16) & 0x000000FF;
						float green3 = (colo2 >>8 ) & 0x000000FF;
						float blue3 = (colo2) & 0x000000FF;
						
						
						if((red2 >= red && green2 >= green && blue2 >= blue && red3 <= red && green3 <= green && blue3 <= blue)){
							
							float smooth = Helper.smoothStep(red2,red3,red);
							if(smooth == 0 || smooth == 1)
								smooth = Helper.smoothStep(green2,green3,green);
							if(smooth == 0 || smooth == 1)
								smooth = Helper.smoothStep(blue2,blue3,blue);
							System.out.println(smooth);
							height = (vector.w+ vectorS.w)*smooth*10;
							break;
						}else if(red2 <= red && green2 <= green && blue2 <= blue && red3 >= red && green3 >= green && blue3 >= blue){
							
							float smooth = Helper.smoothStep(red3,red2,red);
							if(smooth == 0 || smooth == 1)
								smooth = Helper.smoothStep(green3,green2,green);
							if(smooth == 0 || smooth == 1)
								smooth = Helper.smoothStep(blue3,blue2,blue);
							System.out.println(smooth);
							height = (vector.w+ vectorS.w)*smooth*10;
							break;							
						}
						
					}
				}
				
			}*/
		}
		return height;
	}
	
	public static float getHeight(int x, int z, BufferedImage image,ArrayList<Vector4f> heights){
		
		float heightL = getHeight2(Math.max(x-3,0), z, image,heights);
		float heightR = getHeight2(Math.min(x+3,image.getHeight()), z, image,heights);
		float heightD = getHeight2(x, Math.max(z-3,0), image,heights);
		float heightU = getHeight2(x, Math.min(z+3,image.getHeight()), image,heights);

		float heightL2 = getHeight2(Math.max(x-3,0), Math.max(z-3,0), image,heights);
		float heightR2 = getHeight2(Math.min(x+3,image.getHeight()), Math.max(z+3,image.getHeight()), image,heights);
		float heightD2 = getHeight2(Math.max(x+3,image.getHeight()), Math.max(z-3,0), image,heights);
		float heightU2 = getHeight2(Math.max(x-3,0), Math.min(z+3,image.getHeight()), image,heights);
		
		float heightL3 = getHeight2(Math.max((x-6),0), z, image,heights);
		float heightR3 = getHeight2(Math.min(x+6,image.getHeight()), z, image,heights);
		float heightD3 = getHeight2(x, Math.max(z-6,0), image,heights);
		float heightU3 = getHeight2(x, Math.min(z+6,image.getHeight()), image,heights);
		
		
		

		float height = (heightL+heightR+heightD+heightU+heightL2+heightR2+heightD2+heightU2+heightL3+heightR3+heightD3+heightU3)/12;
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
		this.blur = new Material(loader.loadTexture("fade.png"));
	}



	public Material getBlur() {
		return blur;
	}
	
	
	
	
}
