package renderEngine;
 
import java.util.List;
import java.util.Map;

import renderEngine.entities.Object3D;
import renderEngine.materials.Material;
import renderEngine.models.Mesh;
import renderEngine.models.Model;
import renderEngine.shaders.StaticShader;
import renderEngine.utils.Matrix;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
 
public class EntityRenderer {
 
    private StaticShader shader;
 
    public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }



    public void render(Map<Model, List<Object3D>> entities) {
        for (Model model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Object3D> batch = entities.get(model);
            for (Object3D entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }
 
    private void prepareTexturedModel(Model model) {
        Mesh rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        Material texture = model.getTexture();
        shader.loadDiffuse(texture.getDiffuse());
        shader.loadShineVariable(texture.getShineDamper(), texture.getReflectivity());
    	if(model.getTexture().getIsTextured()){
    		shader.loadTextured(texture.getIsTextured());
    		GL13.glActiveTexture(GL13.GL_TEXTURE0);
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
    	}
    }
 
    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
 
    private void prepareInstance(Object3D entity) {
        Matrix4f transformationMatrix = Matrix.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }
 
}