package renderEngine.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.util.vector.Vector4f;

public class TerrainTexture {

	private int r,g,b;
	private String texture;
	private String normalMap;
	private String displacementMap;
	private float height;
	private int erosion;
	private float tiling;
	
	public TerrainTexture(float h, float s, float b,float height, int erosion,String texture,String normalMap,String displacementMap){
		int color = Color.HSBtoRGB(h, s,b);
		this.r = (color >> 16) & 0x000000FF;
		this.g = (color >>8 ) & 0x000000FF;
		this.b = (color) & 0x000000FF;
		this.texture = texture;
		this.normalMap = normalMap;
		this.displacementMap = displacementMap;
		this.height = height;
		this.erosion = erosion;
	}

	public TerrainTexture(Color couleur, float height, int erosion, float tiling, String texture) {
		
		this.r = couleur.getRed();
		this.g = couleur.getGreen();
		this.b = couleur.getBlue();

		this.texture = texture + "/" + texture;
		this.normalMap = texture + "/" + texture + "_NRM";
		this.displacementMap = texture + "/" + texture + "_DISP";
		this.height = height;
		this.erosion = erosion;
		this.tiling = tiling;
	}

	public TerrainTexture(TerrainTexture terrainTexture) {
		this.r = terrainTexture.getR();
		this.g = terrainTexture.getG();
		this.b = terrainTexture.getB();

		this.texture = terrainTexture.texture;
		this.normalMap = terrainTexture.normalMap;
		this.displacementMap = terrainTexture.displacementMap;
		this.height = terrainTexture.height;
		this.erosion = terrainTexture.erosion;
		this.tiling = terrainTexture.tiling;
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

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public int getErosion() {
		return erosion;
	}

	public void setErosion(int erosion) {
		this.erosion = erosion;
	}

	public static void sort(ArrayList<TerrainTexture> textures) {
		Collections.sort(textures,  new Comparator<TerrainTexture>() {
			public int compare(TerrainTexture p1, TerrainTexture p2) {

				return  (p1.getHeight()>p2.getHeight()?-1:1);
			}

		});
	
	}

	public float getTiling() {
		return tiling;
	}

	public void setTiling(float tiling) {
		this.tiling = tiling;
	}
	
}
