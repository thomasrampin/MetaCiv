package renderEngine.utils;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.entities.Camera;
import renderEngine.entities.Object3D;

public class MousePicker {

	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;
	
	private Vector3f currentRay;
	private Vector3f ray;
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	private Vector3f direction;
	

	private Vector3f currentObjectPoint;
	
	public MousePicker(Camera camera, Matrix4f projection){
		this.camera = camera;
		this.projectionMatrix = projection;
		this.viewMatrix = Matrix.createViewMatrix(camera);

	}
	
	public Vector3f getCurrentObjectPoint(){
		return currentObjectPoint;
	}
	
	public Vector3f getCurrentRay(){
		return currentRay;
	}
	
	public Vector3f getRay(){
		return ray;
	}
	
	public Vector3f getDirection(){
		return (Vector3f) direction.normalise();
	}
	
	public void update(){
		viewMatrix = Matrix.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		ray = getPointOnRay(currentRay, RAY_RANGE);
		direction = new Vector3f(ray.x-currentRay.x,ray.y-currentRay.y,ray.z-currentRay.z);

	}
	
	private Vector3f calculateMouseRay(){
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX,mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x,normalizedCoords.y ,-1f ,1f); // Pointing to the screen
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords){
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x,rayWorld.y,rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords){
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f ,0f);
	}
	
	// Change mouseCoords to Opengl Coords
	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY){
		float x = (2f * mouseX) / Display.getWidth() - 1;
		float y = (2f * mouseY) / Display.getHeight() -1;
		return new Vector2f(x,y);
	}
	
	
	
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	




	

}
