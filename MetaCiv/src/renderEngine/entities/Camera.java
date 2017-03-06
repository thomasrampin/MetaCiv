package renderEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private static final float ZOOM_OUT_MAX = 200;
	private static final float SPEED = 1.5f;
	
	private Vector3f position = new Vector3f(0,0,0);
	private Vector3f lookAt = new Vector3f(0,0,0);
	private float pitch=20;
	private float angleAroundPivot=0;
	private float distanceFromPivot=50;
	private float yangle;
	private float roll;
	
	
	public Camera(){
		
	}

	public void move(){
		float zoomLevel = Mouse.getDWheel() *0.1f;
		
		if (distanceFromPivot - zoomLevel > 5f && distanceFromPivot - zoomLevel < ZOOM_OUT_MAX )
			distanceFromPivot -= zoomLevel;
		
		
		if(Mouse.isButtonDown(0)){
			float pitchChange = Mouse.getDY() * 0.3f;
			if(pitch - pitchChange >10 && pitch - pitchChange <85)
				pitch -= pitchChange;
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPivot -= angleChange;
			
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
			
			lookAt.z += SPEED * Math.cos(Math.toRadians(angleAroundPivot));
		
			lookAt.x += SPEED * Math.sin(Math.toRadians(angleAroundPivot));
		
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			
				lookAt.z -= SPEED * Math.cos(Math.toRadians(angleAroundPivot));
			
				lookAt.x -= SPEED * Math.sin(Math.toRadians(angleAroundPivot));
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			
				lookAt.z -= SPEED * Math.sin(Math.toRadians(angleAroundPivot));
			
				lookAt.x += SPEED * Math.cos(Math.toRadians(angleAroundPivot));
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			
				lookAt.z += SPEED * Math.sin(Math.toRadians(angleAroundPivot));
			
				lookAt.x -= SPEED * Math.cos(Math.toRadians(angleAroundPivot));
	
			}
		
		float horizontalDistance = calculateHorizontal();
		float verticalDistance = calculateVertical();
		calculateCameraPosition(horizontalDistance,verticalDistance);
		
		yangle = 180 - angleAroundPivot;
        /*position.x = (float) ( -Math.sin(cameraX*(Math.PI/180)) * Math.cos((cameraY)*(Math.PI/180)));
        position.y = (float) (  -Math.sin((cameraY)*(Math.PI/180)));
        position.z = (float) ( -Math.cos((cameraX)*(Math.PI/180)) * Math.cos((cameraY)*(Math.PI/180)));*/
	}
	
	public void invertPitch(){
		this.pitch = -this.pitch;
	}
	
	private void calculateCameraPosition(float hD, float vD){
		float offsetX = (float) (hD * Math.sin(Math.toRadians(angleAroundPivot)));
		float offsetZ = (float) (hD * Math.cos(Math.toRadians(angleAroundPivot)));
		position.x = lookAt.x - offsetX;
		position.y = lookAt.y + vD;
		position.z = lookAt.z - offsetZ;
	}
	
	private float calculateHorizontal(){
		return (float) (distanceFromPivot * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVertical(){
		return (float) (distanceFromPivot * Math.sin(Math.toRadians(pitch)));
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYangle() {
		return yangle;
	}

	public float getRoll() {
		return roll;
	}

	public void setTarget(Vector3f position2) {
		this.lookAt = position2;
		
	}
	
	
	
}
