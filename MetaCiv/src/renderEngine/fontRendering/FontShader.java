package renderEngine.fontRendering;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.entities.Camera;
import renderEngine.shaders.ShaderProgram;
import renderEngine.utils.Matrix;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/renderEngine/fontRendering/fontVertex.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/fontRendering/fontFragment.glsl";

	private int location_colour;
	private int location_transformationMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_Fix;
	private int location_translation;


	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_Fix = super.getUniformLocation("fix");
		location_translation = super.getUniformLocation("translation");
	}

	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Matrix.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	protected void loadColour(Vector3f colour) {
		super.loadVector(location_colour, colour);
	}

	protected void loadTranslation(Vector3f translation) {
		super.load2DVector(location_translation, new Vector2f(translation.x,translation.y));
}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix){
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

	public void loadTransformationMatrix(Vector3f position) {
        Matrix4f transformationMatrix = Matrix.createTransformationMatrix(position,
                0,0,0, 1);
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
		
	}

	public void loadFix(boolean fix) {
		super.loadBoolean(location_Fix, fix);
	}

}
