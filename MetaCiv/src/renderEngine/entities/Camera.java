package renderEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import civilisation.world.WorldViewer;
import renderEngine.terrains.Terrain;

public class Camera {
	
	private static final float ZOOM_OUT_MAX = 400;
	private static final float SPEED = 1.5f;
	
	private Vector3f position = new Vector3f(0,0,0);
	private Vector3f lookAt = new Vector3f(180,0,100);
	private float pitch=10;
	private float angleAroundPivot=45;
	private float distanceFromPivot=200;
	private float yangle;
	private float roll;
	
	
	public Camera(){
		
	}

	public void move(){
		

		
		float zoomLevel = Mouse.getDWheel() * distanceFromPivot/4000.0f;
		Vector3f position = new Vector3f(0,0,100000);
		int cX = (int) (this.position.x/(25/WorldViewer.initialCellSize));
		int cY = (int) (this.position.z/(25/WorldViewer.initialCellSize));
		
		if(cX>0 && cY>0 && cX<Terrain.VERTEX_COUNT_W && cY< Terrain.VERTEX_COUNT)
			position = Terrain.getHeightByTab(cX,cY);
		
		if (distanceFromPivot - zoomLevel > 5f && distanceFromPivot - zoomLevel < ZOOM_OUT_MAX && distanceFromPivot - zoomLevel >position.y+8)
			distanceFromPivot -= zoomLevel;
		
		
		if(Mouse.isButtonDown(0)){
			float pitchChange = Mouse.getDY() *0.3f;// * distanceFromPivot/500.0f;
			if(pitch - pitchChange >0 && pitch - pitchChange <85 && simulate(pitchChange))
				pitch -= pitchChange;
			float angleChange = Mouse.getDX() *0.3f;// * distanceFromPivot/500.0f;
			angleAroundPivot -= angleChange;
			
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
			
			lookAt.z += distanceFromPivot/100.0f * Math.cos(Math.toRadians(angleAroundPivot));
		
			lookAt.x += distanceFromPivot/100.0f * Math.sin(Math.toRadians(angleAroundPivot));
		
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			
			lookAt.z -= distanceFromPivot/100.0f * Math.cos(Math.toRadians(angleAroundPivot));
			
			lookAt.x -= distanceFromPivot/100.0f * Math.sin(Math.toRadians(angleAroundPivot));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			
			lookAt.z -= distanceFromPivot/100.0f * Math.sin(Math.toRadians(angleAroundPivot));
			
			lookAt.x += distanceFromPivot/100.0f * Math.cos(Math.toRadians(angleAroundPivot));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			
			lookAt.z += distanceFromPivot/100.0f * Math.sin(Math.toRadians(angleAroundPivot));
			
			lookAt.x -= distanceFromPivot/100.0f * Math.cos(Math.toRadians(angleAroundPivot));
	
			}
		float horizontalDistance = calculateHorizontal();
		float verticalDistance = calculateVertical();
		calculateCameraPosition(horizontalDistance,verticalDistance);
		
		cX = (int) (this.position.x/(25/WorldViewer.initialCellSize));
		cY = (int) (this.position.z/(25/WorldViewer.initialCellSize));
		
		if(cX>0 && cY>0 && cX<Terrain.VERTEX_COUNT_W && cY< Terrain.VERTEX_COUNT)
			position = Terrain.getHeightByTab(cX,cY);
		while(this.position.y<position.y+5){
		
			
			if(pitch>85)
				distanceFromPivot++;
			else
				this.pitch++;
			horizontalDistance = calculateHorizontal();
			verticalDistance = calculateVertical();
			calculateCameraPosition(horizontalDistance,verticalDistance);
		}
		yangle = 180 - angleAroundPivot;
		
		if(this.position.x<0 || this.position.z<0){//RollBack
			
		}


	}
	
	private boolean simulate(float pitchChange) {
		pitch -=pitchChange;
		float horizontalDistance = calculateHorizontal();
		float verticalDistance = calculateVertical();
		
		Vector3f pos = new Vector3f(position);
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(angleAroundPivot)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(angleAroundPivot)));
		pos.x = lookAt.x - offsetX;
		pos.y = lookAt.y + verticalDistance;
		pos.z = lookAt.z - offsetZ;
		
		int cX = (int) (pos.x/(25/WorldViewer.initialCellSize));
		int cY = (int) (pos.z/(25/WorldViewer.initialCellSize));
		Vector3f p = new Vector3f(0,0,100000);
		if(cX>0 && cY>0 && cX<Terrain.VERTEX_COUNT_W && cY< Terrain.VERTEX_COUNT)
			p = Terrain.getHeightByTab(cX,cY);
		pitch += pitchChange;
		return pos.y>p.y+5;
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
