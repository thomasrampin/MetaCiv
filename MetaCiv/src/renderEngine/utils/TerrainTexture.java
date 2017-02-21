package renderEngine.utils;

import java.awt.Color;

import org.lwjgl.util.vector.Vector4f;

public class TerrainTexture {

	private int r,g,b;
	private String texture;
	private String normalMap;
	private String displacementMap;
	
	public TerrainTexture(float r, float g, float b,String texture,String normalMap,String displacementMap){
		int color = Color.HSBtoRGB(r, g,b);
		this.r = (color >> 16) & 0x000000FF;
		this.g = (color >>8 ) & 0x000000FF;
		this.b = (color) & 0x000000FF;
		this.texture = texture;
		this.normalMap = normalMap;
		this.displacementMap = displacementMap;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public String getNormalMap() {
		return normalMap;
	}
	
	public String getDisplacementMap(){
		return displacementMap;
	}
	
}
