package renderEngine;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.ImageIOImageData;

import civilisation.Configuration;


public class Window {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 400;
	private static final int FPS_CAP = 120;
	public static boolean is4 = false;
	
	public static void createDislay() throws IOException{
		
		String ICON_PATHS[] = new String[3];
		ICON_PATHS[0] = "src/civilisation/graphismes/Logo_16.png";
		ICON_PATHS[1] = "src/civilisation/graphismes/Logo_32.png";
		ICON_PATHS[2] = "src/civilisation/graphismes/Logo_128.png";
		ContextAttribs attribs = new ContextAttribs(4, 2).withForwardCompatible(true).withProfileCore(true);
		java.awt.Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int)dimension.getHeight();
		int width = (int)dimension.getWidth();
		try {
			Display.setDisplayMode(new DisplayMode((int)(width/1.5),(int)(height/1.5)));
			
			Display.create();
			Display.setTitle("MetaCiv 3D view");
			GL11.glEnable(GL13.GL_MULTISAMPLE);

			try {
				String OS = System.getProperty("os.name").toUpperCase();

				if (OS.contains("WIN")) {
					Display.setIcon(new ByteBuffer[] {
							new ImageIOImageData().imageToByteBuffer(ImageIO
									.read(new File(ICON_PATHS[0])),
									false, false, null),
							new ImageIOImageData().imageToByteBuffer(ImageIO
									.read(new File(ICON_PATHS[1])),
									false, false, null) });
				}

				else if (OS.contains("MAC")) {
					Display.setIcon(new ByteBuffer[] { new ImageIOImageData()
							.imageToByteBuffer(ImageIO.read(new File(
									ICON_PATHS[2])), false, false,
									null) });
				}

				else {
					Display.setIcon(new ByteBuffer[] { new ImageIOImageData().imageToByteBuffer(
							ImageIO.read(new File(ICON_PATHS[1])),
							false, false, null) });
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
	}
	
	public static void updateDisplay(){
		
		Display.sync(FPS_CAP);
		Display.update();
		
	}
	
	public static void closeDisplay(){
		
		Display.destroy();
		
	}
	
	
	public static boolean IsExtensionSupported(String extname)
	{


	    int numExtensions = GL11.glGetInteger(GL30.GL_NUM_EXTENSIONS);

	    for (int i = 0; i < numExtensions; i++)
	    {
	        String e = GL30.glGetStringi(GL11.GL_EXTENSIONS, i);
	        if (e.equals(extname))
	        {
	            return true;
	        }
	    }

	    return false;
	}
	
	public static void showInfo(){
		System.out.println("******   OPENGl context   ******");
		System.out.println("OpneGL version: " + GL11.glGetString(GL11.GL_VERSION));
		System.out.println("GLSL version: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
		System.out.println("Is ARB bindless texture is supported : " + Window.IsExtensionSupported("GL_ARB_bindless_texture"));
		/*IntBuffer o = BufferUtils.createIntBuffer(16);
		GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS, o);
		System.err.println("GL_MAX_TEXTURE_SIZE: " + o.get());*/
	}

	public static boolean checkGlVersion() {
		if(Character.getNumericValue(GL11.glGetString(GL11.GL_VERSION).charAt(0)) >= 4){
			is4 = Character.getNumericValue(GL11.glGetString(GL11.GL_VERSION).charAt(2)) >= 1;
			return is4;
		}
		else if(GL11.glGetString(GL11.GL_VERSION).charAt(0) == '3'){
			return Character.getNumericValue(GL11.glGetString(GL11.GL_VERSION).charAt(2)) >= 3;
		}
		return false;
	}

}
