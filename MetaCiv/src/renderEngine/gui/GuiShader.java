package renderEngine.gui;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.shaders.ShaderProgram;
 
public class GuiShader extends ShaderProgram{
     
    private static final String VERTEX_FILE = "/renderEngine/gui/guiVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/renderEngine/gui/guiFragmentShader.glsl";
     
    private int location_transformationMatrix;
    private int location_color;
    private int location_isTextured;
	private int location_isHover;
 
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_color = super.getUniformLocation("color");
        location_isTextured = super.getUniformLocation("isTextured");
        location_isHover = super.getUniformLocation("isHover");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
     
     public void loadColor(Vector3f color){
    	 super.loadVector(location_color, color);
     }
     
     public void loadIsTextured(boolean isTextured){
    	 super.loadBoolean(location_isTextured, isTextured);
     }

	public void loadIsHover(boolean hover) {
		super.loadBoolean(location_isHover, hover);
	}
     
 
}