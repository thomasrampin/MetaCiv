package renderEngine.loaders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import renderEngine.models.Mesh;
import renderEngine.models.TextureData;
import renderEngine.utils.TerrainTexture;

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	private static int textureTerrainID = -1;
	private static int textureTerrainDiffID = -1;
	private static int texturesAtlas[];
	
	/*********************************
	 * VAO
	 *  -------------------------------
	 * | 0 | vertex
	 * | 1 | textureCoord
	 * | 2 | normals
	 * |   |
	 * |   |
	 *  -------------------------------
	 */
	
	
	public Mesh loadToVAO(float[] positions, float[] textureCoords,float[] normals,int[] indices){
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,3,textureCoords);
		storeDataInAttributeList(2,3,normals);
		unbindVAO();
		return new Mesh(vaoID,indices.length);
	}
	
	public Mesh loadToVAO(float[] positions ,int dimensions){
		int vaoID = createVAO();
		storeDataInAttributeList(0,dimensions,positions);
		unbindVAO();
		return new Mesh(vaoID,positions.length/dimensions);
	}
	
	public int loadTexture(String fileName){
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("Assets/Texture/"+fileName));
		} catch (FileNotFoundException e) {
			System.out.println("file :"+fileName+" not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	  private static BufferedImage blur(BufferedImage image) {

		  	float value =  1f/2f;
		    Kernel kernel = new Kernel(3, 3, new float[] { value, value, value,
		    		value,value,value,value, value,value });
		    BufferedImageOp op = new ConvolveOp(kernel,ConvolveOp.EDGE_NO_OP, null);
		    image = op.filter(image, null);
		    return image;
		  }
	
	public static int loadTexture(BufferedImage image){
		/*BufferedImage bufferedView = new BufferedImage(60,60,BufferedImage.TYPE_INT_ARGB);
        Graphics g2d = bufferedView.createGraphics();
        g2d .setColor(Color.BLUE);    //draw the things you want
        g2d .fillOval(0,0,30,30);   //draw the things you want*/
		
		//image = Loader.blur(image);
		
		if(textureTerrainID!=-1)
			GL11.glDeleteTextures(textureTerrainID);
		
	    int pixels[] = new int[image.getWidth() * image.getHeight()];
	    image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
	    ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); // <-- 4 for RGBA, 3 for RGB

	    for(int y = 0; y < image.getHeight(); y++){
	        for(int x = 0; x < image.getWidth(); x++){
	      
	            int pixel = pixels[y*image.getHeight()+x];
	          
	            /*buffer.put((byte) (pixel & 0x00FF0000));     // Red component
	            buffer.put((byte) (pixel & 0x0000FF00));      // Green component
	            buffer.put((byte) (pixel & 0x000000FF));               // Blue component
	            buffer.put((byte) ((pixel & 0xFF000000) >>> 24));    // Alpha component. Only for RGBA*/
	            
	            
	            
	            buffer.put((byte) ((pixel >>16 ) & 0xFF));
	            buffer.put((byte)  ((pixel >>8) & 0xFF));
	            buffer.put((byte)  ((pixel) & 0xFF));
	            
	            buffer.put((byte)  ((pixel >>24 ) & 0xFF));
	        }
	    }

	    buffer.flip();

	    textureTerrainID = GL11.glGenTextures(); //Generate texture ID
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureTerrainID);

	    // Setup wrap mode
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

	    //Setup texture scaling filtering
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

	    //Send texel data to OpenGL
	    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

	    return textureTerrainID;
	 }

	
    private static BufferedImage scale(BufferedImage bImage, double factor) {
        int destWidth=(int) (bImage.getWidth() * factor);
        int destHeight=(int) (bImage.getHeight() * factor);

        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage bImageNew = configuration.createCompatibleImage(destWidth, destHeight);
        Graphics2D graphics = bImageNew.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.drawImage(bImage, 0, 0, destWidth, destHeight, 0, 0, bImage.getWidth(), bImage.getHeight(), null);
        graphics.dispose();
 
        return bImageNew;
    }
	
	public static int loadMultiTexture(BufferedImage image,ArrayList<TerrainTexture> textures){

		
		if(textureTerrainDiffID!=-1)
			GL11.glDeleteTextures(textureTerrainDiffID);
		
	    int pixels[] = new int[image.getWidth() * image.getHeight()];
	    image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
	    ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); // <-- 4 for RGBA, 3 for RGB
	    
	    int pixelsTextures[][] = new int[textures.size()][];
	    Vector4f comp[] = new Vector4f[textures.size()];
	    
	    int iterator = 0;
	    for(TerrainTexture t : textures){
			BufferedImage temp = null;
			try {
				temp = ImageIO.read(new File("Assets/Texture/"+t.getTexture()+".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			 
			  BufferedImage tiledtemp = temp;
			pixelsTextures[iterator] = new int[tiledtemp.getWidth() * tiledtemp.getHeight()];
			tiledtemp.getRGB(0, 0, tiledtemp.getWidth(), tiledtemp.getHeight(), pixelsTextures[iterator], 0, tiledtemp.getWidth());
			comp[iterator] = new Vector4f(0,0,tiledtemp.getWidth(), tiledtemp.getHeight());
			iterator++;
	    }
	    boolean f = false;
	    boolean found[] = new boolean[textures.size()];
	    for(int y = 0; y < image.getHeight(); y++){
	    	for(int i=0 ;i<textures.size();i++){
	    		found[i] = false;
	    	}
	        for(int x = 0; x < image.getWidth(); x++){
	      
	            int pixel = pixels[y*image.getHeight()+x];
	            iterator = 0;
	            f = false;
            	int red = (pixel >>16 ) & 0xFF;
            	int green = (pixel >>8) & 0xFF;
            	int blue = (pixel ) & 0xFF;
            	int alpha = (pixel >>24 ) & 0xFF;
            	
	            for(TerrainTexture t : textures){
            		comp[iterator].setX(comp[iterator].getX()+1);

            		if(comp[iterator].getX() == comp[iterator].getZ()){
            			comp[iterator].setX(0);
            			//comp[iterator].setY((comp[iterator].getY()+1)%comp[iterator].getW());
            		}
            		
	            	//System.out.println(red + " " + t.getR() + " green: " + green + " "+ t.getG() + " blue: " + blue + " "+ t.getB());
	            	if(t.getR() == red && t.getG() == green && t.getB() == blue){
	            		
	            		int pixelTexture = pixelsTextures[iterator][(int) ((comp[iterator].getY()*comp[iterator].getW()+comp[iterator].getX()))];
	            		

	    	            buffer.put((byte) ((pixelTexture >>16 ) & 0xFF));
	    	            buffer.put((byte)  ((pixelTexture >>8) & 0xFF));
	    	            buffer.put((byte)  ((pixelTexture) & 0xFF));
	    	            
	    	            buffer.put((byte)  ((pixelTexture >>24 ) & 0xFF));
	    	            found[iterator] = true;
	    	          
	    	            f = true;
	    	            
	    	            break;
	            	}
	            	iterator++;
	            }
	            
	            if(!f){
    	            buffer.put((byte) red);
    	            buffer.put((byte)  green);
    	            buffer.put((byte)  blue);
    	            
    	            buffer.put((byte)  alpha);	            	
	            }
	            
	        }
	        for(int i=0 ;i<textures.size();i++){
	        	if(found[i]){
	        		comp[i].setY(comp[i].getY()+1);
	        		
	        		if(comp[i].getY() == comp[i].getW()){
	        			comp[i].setY(0);
	        			
	        			//comp[iterator].setY((comp[iterator].getY()+1)%comp[iterator].getW());
	        		}
	        	}
	        	comp[i].setX(0);
	        }
	    }

	    buffer.flip();

	    textureTerrainDiffID = GL11.glGenTextures(); //Generate texture ID
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureTerrainDiffID);

	    // Setup wrap mode
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

	    //Setup texture scaling filtering
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

	    //Send texel data to OpenGL
	    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

	    return textureTerrainDiffID;
	 }	

	public static int[] loadTextureAtlas(ArrayList<TerrainTexture> textures){
	    int iterator = 0;
	    texturesAtlas = new int[textures.size()];
		for(TerrainTexture t : textures){

			BufferedImage imageDiff = null;
			try {
				imageDiff = ImageIO.read(new File("Assets/Texture/"+t.getTexture()+".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
				 
			BufferedImage imageNrm = null;
			try {
				imageNrm = ImageIO.read(new File("Assets/Texture/"+t.getNormalMap()+".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
				
			int pixelsDiff[] = new int[imageDiff.getWidth() * imageDiff.getHeight()];
			imageDiff.getRGB(0, 0, imageDiff.getWidth(), imageDiff.getHeight(), pixelsDiff, 0, imageDiff.getWidth());
		    ByteBuffer buffer = BufferUtils.createByteBuffer((imageDiff.getWidth()+imageNrm.getWidth()) * (imageDiff.getHeight()) * 4); // <-- 4 for RGBA, 3 for RGB

		    int pixelsNrm[] = new int[imageNrm.getWidth() * imageNrm.getHeight()];
		    imageNrm.getRGB(0, 0, imageNrm.getWidth(), imageNrm.getHeight(), pixelsNrm, 0, imageNrm.getWidth());
		    
		    for(int y = 0; y < imageDiff.getHeight(); y++){
		        for(int x = 0; x < imageDiff.getWidth()+imageNrm.getWidth(); x++){
		        	int pixel;
		        	if(x<imageDiff.getWidth())
		        		pixel = pixelsDiff[y*imageDiff.getHeight()+x];
		        	else
		        		pixel = pixelsNrm[y*imageNrm.getHeight()+(x- imageDiff.getWidth())];
		            buffer.put((byte) ((pixel >>16 ) & 0xFF));
		            buffer.put((byte)  ((pixel >>8) & 0xFF));
		            buffer.put((byte)  ((pixel) & 0xFF));
		            
		            buffer.put((byte)  ((pixel >>24 ) & 0xFF));
		        }
		    }
		    
		    buffer.flip();
		    
		    textureTerrainDiffID = GL11.glGenTextures(); //Generate texture ID
		    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureTerrainDiffID);
	
		    // Setup wrap mode
		    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
	
		    //Setup texture scaling filtering
		    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	
		    //Send texel data to OpenGL
		    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, imageDiff.getWidth()+imageNrm.getWidth(), imageDiff.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		    texturesAtlas[iterator] = textureTerrainDiffID;
		   
		    iterator++;
		}
	    return texturesAtlas;		
	}
	
	public int loadCubeMap(String[] textureFiles){
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		for(int i=0;i<textureFiles.length;i++){
			TextureData data = decodeTextureFile("Assets/Skybox/" + textureFiles[i] + ".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(texID);
		return texID;
	}
	
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	public void cleanUp(){
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture:textures){
			GL11.glDeleteTextures(texture);
		}
		
		GL11.glDeleteTextures(textureTerrainID);
	}
	
	private void storeDataInAttributeList(int attributeNumber,int coordinateSize, float[] data){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); // Cannot be modified
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false,0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STREAM_DRAW); // Cannot be modified
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();// prepare for reading
		return buffer;
	}

	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();// prepare for reading
		return buffer;
	}

}
