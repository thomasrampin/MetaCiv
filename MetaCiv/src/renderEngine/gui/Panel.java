package renderEngine.gui;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Panel {
	
	private int textureId;
	private Vector2f position;
	private Vector2f scale;
	private Vector3f color;
	private boolean isTextured;
	private boolean hover = false;
	
	
	public Panel(int textureId, Vector2f position, Vector2f dimension) {
		this.textureId = textureId;
		float x = (2f * (position.x + dimension.x/2)) / Display.getWidth() - 1;
		float y = (2f * (Display.getHeight() - position.y - dimension.y/2)) / Display.getHeight() -1;
		this.position = new Vector2f(x,y);
		this.scale = new Vector2f(dimension.x/Display.getWidth(),dimension.y/Display.getHeight());
		isTextured = true;
	}

	public Panel(Vector3f color, Vector2f position, Vector2f dimension) {
		this.color = color;
		float x = (2f * (position.x + dimension.x/2)) / Display.getWidth() - 1;
		float y = (2f * (Display.getHeight() - position.y - dimension.y/2)) / Display.getHeight() -1;
		this.position = new Vector2f(x,y);
		this.scale = new Vector2f(dimension.x/Display.getWidth(),dimension.y/Display.getHeight());
		isTextured = false;
	}

	public int getTextureId() {
		return textureId;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public boolean isTextured() {
		return isTextured;
	}

	public void setTextured(boolean isTextured) {
		this.isTextured = isTextured;
	}

	public void setHover(boolean hover) {
		this.hover  = hover;
	}
	
	public boolean isHover(){
		return hover;
	}

}
