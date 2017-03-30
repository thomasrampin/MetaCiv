package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;


public class Window {
	
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	public static void createDislay(){
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create();
			Display.setTitle("MetaCiv 3D view");
			GL11.glEnable(GL13.GL_MULTISAMPLE);
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
	}

	public static boolean checkGlVersion() {
		if(GL11.glGetString(GL11.GL_VERSION).charAt(0) == '4'){
			return Character.getNumericValue(GL11.glGetString(GL11.GL_VERSION).charAt(2)) >= 2;
		}
		return false;
	}

}
