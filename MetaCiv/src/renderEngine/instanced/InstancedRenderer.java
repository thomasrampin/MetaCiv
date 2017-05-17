package renderEngine.instanced;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.entities.Camera;
import renderEngine.entities.Object3D;
import renderEngine.loaders.Loader;
import renderEngine.models.Model;
import renderEngine.utils.Helper;
import renderEngine.utils.Matrix;

public class InstancedRenderer {

	private static final int MAX_INSTANCES = 100;
	private static final int INSTANCE_DATA_LENGTH = 32;
	
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(INSTANCE_DATA_LENGTH * MAX_INSTANCES); 
	
	private InstancedShader shader;
	private Loader loader;
	private int vbo;
	private Object3D object;
	
	private int pointer = 0;
	
	public InstancedRenderer(Loader loader,Object3D object,Matrix4f projectionMatrix){
		this.loader = loader;
		shader = new InstancedShader();
		this.vbo = loader.createEmptyVbo(INSTANCE_DATA_LENGTH * MAX_INSTANCES); 
		this.object = object;
		loader.addAttribute(object.getModel().getRawModel().getVaoID(), vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);
		loader.addAttribute(object.getModel().getRawModel().getVaoID(), vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);
		loader.addAttribute(object.getModel().getRawModel().getVaoID(), vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);
		loader.addAttribute(object.getModel().getRawModel().getVaoID(), vbo, 4, 4, INSTANCE_DATA_LENGTH, 12);

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();		
	}
	
	public void render(Map<Model, List<Object3D>> entities,Camera camera){
		Matrix4f viewMatrix = Matrix.createViewMatrix(camera);
		begin();
        for (Model model : entities.keySet()) {
            List<Object3D> batch = entities.get(model);
            pointer = 0;
            float[] vboData = new float[batch.size() * INSTANCE_DATA_LENGTH];
            for (Object3D entity : batch) {
            	computeModelViewMatrix(entity.getPosition(),entity.getRotX(),entity.getRotY(),entity.getRotZ(),entity.getScale(),viewMatrix,vboData);

            	

            }
            loader.updateVbo(vbo, vboData, buffer);
            GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT, 0,batch.size());
        }		
		end();
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
	
	private void computeModelViewMatrix(Vector3f position,float rotationX,float rotationY,float rotationZ, float scale, Matrix4f viewMatrix, float[] vboData){
		Matrix4f modelmatrix = new Matrix4f();
		//Matrix4f.translate(position, modelmatrix, modelmatrix);

		Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelmatrix, null);
		Matrix4f.rotate((float) Math.toRadians(rotationX),new Vector3f(1,0,0),modelViewMatrix,modelViewMatrix);
		Matrix4f.rotate((float) Math.toRadians(rotationY),new Vector3f(0,1,0),modelViewMatrix,modelViewMatrix);
		Matrix4f.rotate((float) Math.toRadians(rotationZ),new Vector3f(0,0,1),modelViewMatrix,modelViewMatrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), modelViewMatrix, modelViewMatrix);
		pointer = Helper.storeMatrixData(modelViewMatrix, vboData, pointer);
	}
	
	private void begin(){
		shader.start();
		GL30.glBindVertexArray(object.getModel().getRawModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);

	}
	
	private void end(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);


		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
}
