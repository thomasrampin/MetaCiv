package renderEngine.sky;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import civilisation.world.World;
import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import renderEngine.loaders.Loader;
import renderEngine.loaders.OBJLoader;
import renderEngine.models.Mesh;
import renderEngine.utils.Matrix;

public class SkyRenderer {

	private static final float SIZE = World.getSize3D();
	
	public static final Vector3f COLOUR = new Vector3f(0.8f,0.8f,0.8f);
	

	
	
	private Mesh cube;
	private int texture;
	private int alpha;
	private SkyShader shader;
	public static float angle = 0;
	private float density = 0.01f;
	private static final float CLOUD_SPEED = 0.00002f;
	private static float DENSITY_VARIATION = 0.000001f;

	
	public SkyRenderer(Loader loader,Matrix4f projectionMatrix){
		cube = OBJLoader.loadObjModel("sky", loader).getRawModel();
		texture = loader.loadTexture("T_Sky_Blue.png",false);
		alpha = loader.loadTexture("T_Sky_Clouds_M.png",false);
		shader = new SkyShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextures();
		shader.stop();
	}
	
	public void render(Camera camera,Light sun, Vector4f plane, boolean invertPitch){
		angle += CLOUD_SPEED;
		density += DENSITY_VARIATION;
		angle %= 1;
		if(density >= 1 || density<=0)
			DENSITY_VARIATION = -DENSITY_VARIATION;

			
		shader.start();
		if(invertPitch)
			camera.invertPitch();
		shader.loadViewMatrix(camera);
		shader.loadAngle(angle);
		shader.loadDensity(density);
		shader.loadPlane(plane);
		shader.loadSunPosition(sun.getPosition());
		Matrix4f transformationMatrix = Matrix.createTransformationMatrix(new Vector3f(0,0,0), 0, 0, 0, SIZE);
		shader.loadTransformationMatrix(transformationMatrix);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, alpha);
        GL11.glDrawElements(GL11.GL_TRIANGLES, cube.getVertexCount(),
                GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		if(invertPitch)
			camera.invertPitch();
		shader.stop();
	}
	
}