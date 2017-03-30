package renderEngine.shadowsMapping;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.utils.Matrix;

public class Shadow {

	private static final Matrix4f scale_bias_matrix = Matrix.createMatrix(0.5f, 0.0f, 0.0f, 0.0f,
										            0.0f, 0.5f, 0.0f, 0.0f,
										            0.0f, 0.0f, 0.5f, 0.0f,
													0.5f, 0.5f, 0.5f, 1.0f);
	
	private static final Matrix4f light_proj_matrix = Matrix.frustum(-1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 200.0f);
	private static Matrix4f light_view_matrix;
	private Matrix4f shadow_sbpv_matrix;
	private Matrix4f light_vp_matrix;
	private ShadowShader shader = new ShadowShader();
	private int quad_vao;
	
	public Shadow(){
		GL30.glGenVertexArrays();
		GL30.glBindVertexArray(quad_vao);
	}

	public void update(Vector3f light_position){
		light_view_matrix = Matrix.lookat(light_position,
				new Vector3f(0.0f,0.0f,0.0f),new Vector3f(0.0f, 1.0f, 0.0f));
		light_vp_matrix = new Matrix4f();
				Matrix4f.mul(light_proj_matrix, light_view_matrix, light_vp_matrix);
		shadow_sbpv_matrix = new Matrix4f();
				Matrix4f.mul(scale_bias_matrix, light_proj_matrix, shadow_sbpv_matrix) ;
		Matrix4f.mul(shadow_sbpv_matrix, light_view_matrix, shadow_sbpv_matrix);
		
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
