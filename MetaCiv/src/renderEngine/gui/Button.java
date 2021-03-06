package renderEngine.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.renderMain;
import renderEngine.fontMeshCreator.FontType;
import renderEngine.fontMeshCreator.GUIText;

public abstract class Button {

	private Panel gui;
	private Vector2f position;
	private Vector2f dimension;
	private Vector3f color;
	private boolean isPressed;
	private boolean isHover = false;
	private boolean isVisible = true;
	private int width;
	private int height;
	
	public Button(int textureId, Vector2f position, Vector2f dimension){
		this.gui = new Panel(textureId,position,dimension);
		this.position = position;
		this.dimension = dimension;
		this.isPressed = false;
		width = Display.getWidth();
		height = Display.getHeight();
	}
	
	public Button(Vector3f color, Vector2f position, Vector2f dimension){
		this.gui = new Panel(color,position,dimension);
		this.position = position;
		this.dimension = dimension;
		this.isPressed = false;
		this.color=color;
		width = Display.getWidth();
		height = Display.getHeight();
	}
	
	public Button(int textureId, Vector2f position, Vector2f dimension,String text,FontType font){
		this.gui = new Panel(textureId,position,dimension);
		this.position = position;
		this.dimension = dimension;
		this.isPressed = false;
		width = Display.getWidth();
		height = Display.getHeight();
		new GUIText(text, 1, font, new Vector3f(position.x/Display.getWidth() , position.y/Display.getHeight() ,0), 1f, false,-1,true);
	}
	
	public Button(Vector3f color, Vector2f position, Vector2f dimension,String text,FontType font){
		this.gui = new Panel(color,position,dimension);
		this.position = position;
		this.dimension = dimension;
		this.isPressed = false;
		this.color=color;
		width = Display.getWidth();
		height = Display.getHeight();
		new GUIText(text, 1, font, new Vector3f(position.x/Display.getWidth() , position.y/Display.getHeight() ,0), 1f, false,-1,true);
	}
	
	public Panel getTexture(){
		return gui;
	}
	
	protected abstract void action();
	
	public void setVisible() {
		isVisible =!isVisible;
	}
	
	public boolean isVisible(){
		return isVisible;
	}
	
	public void update(){
		float factor =  Display.getWidth()/(float)width;
		int mouseX = (int) (Mouse.getX()/factor);
		int mouseY = (int) (Display.getHeight()/factor - (int)(Mouse.getY()/factor));
		
		if(mouseX>position.x && mouseX<position.x+dimension.x && mouseY>position.y && mouseY<position.y+dimension.y && isVisible){
			isHover = true;
			gui.setHover(true);
		}else{
			isHover = false;
			gui.setHover(false);
		}
		
		if(Mouse.isButtonDown(0)){
			if(mouseX>position.x && mouseX<position.x+dimension.x && mouseY>position.y && mouseY<position.y+dimension.y && !isPressed && isVisible){
				isPressed = true;
				
				action();
			}
		}else
			isPressed = false;
	
	}
	
	public void setHover(boolean hover){
		this.isHover = hover;
	}
	
	public boolean isHover() {
		return isHover;
	}
	
}
