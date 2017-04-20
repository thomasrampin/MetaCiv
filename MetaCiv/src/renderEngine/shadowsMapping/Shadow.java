package renderEngine.shadowsMapping;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.utils.Matrix;

public class Shadow {
    public static final float FOV = 45;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000;
	private static final Matrix4f scale_bias_matrix = Matrix.createMatrix(0.5f, 0.0f, 0.0f, 0.0f,
										            0.0f, 0.5f, 0.0f, 0.0f,
										            0.0f, 0.0f, 0.5f, 0.0f,
													0.5f, 0.5f, 0.5f, 1.0f);
	
	private static final Matrix4f light_proj_matrix = Matrix.frustum(-1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 200.0f);
	private static Matrix4f light_view_matrix = new Matrix4f();
	private Matrix4f shadow_sbpv_matrix;
	private Matrix4f light_vp_matrix;
	private ShadowShader shader = new ShadowShader();
	private int quad_vao;
	
	public Shadow(){
		GL30.glGenVertexArrays();
		GL30.glBindVertexArray(quad_vao);
	}

	public void update(Vector3f light_position){
		light_view_matrix = Matrix.lookAt(light_position,
				new Vector3f(0.0f,0.0f,0.0f),new Vector3f(0.0f, 1.0f, 0.0f));
		//updateLightViewMatrix(new Vector3f(-light_position.x,-light_position.y,light_position.z),light_position);
		light_vp_matrix = new Matrix4f();
				Matrix4f.mul(light_proj_matrix, light_view_matrix, light_vp_matrix);
		shadow_sbpv_matrix = new Matrix4f();
				Matrix4f.mul(scale_bias_matrix, light_proj_matrix, shadow_sbpv_matrix) ;
		Matrix4f.mul(shadow_sbpv_matrix, light_view_matrix, shadow_sbpv_matrix);
		
	}
	
    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
 
        
        light_proj_matrix.m00 = x_scale;
        light_proj_matrix.m11 = y_scale;
        light_proj_matrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        light_proj_matrix.m23 = -1;
        light_proj_matrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        light_proj_matrix.m33 = 0;
    }

	private void updateLightViewMatrix(Vector3f direction, Vector3f center) {
		direction.normalise();
		center.negate();
		light_view_matrix.setIdentity();
		float pitch = (float) Math.acos(new Vector2f(direction.x, direction.z).length());
		Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), light_view_matrix, light_view_matrix);
		float yaw = (float) Math.toDegrees(((float) Math.atan(direction.x / direction.z)));
		yaw = direction.z > 0 ? yaw - 180 : yaw;
		Matrix4f.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0), light_view_matrix,
				light_view_matrix);
		Matrix4f.translate(center, light_view_matrix, light_view_matrix);
	}
	
	public Matrix4f getLightVpMatrix(){
		return light_vp_matrix;
	}
	
	public Matrix4f getShadowMatrix(){
		return shadow_sbpv_matrix;
	}
	
	public void render(ShadowFrameBuffer fbo){
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL30.glBindVertexArray(quad_vao);
        shader.start();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getDepthTexture());
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        shader.stop();
        GL30.glBindVertexArray(0);
	}
	
	public void cleanUp(){
		GL30.glDeleteVertexArrays(quad_vao);
	}
}
