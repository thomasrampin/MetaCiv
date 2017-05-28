package renderEngine.utils;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.util.vector.Vector4f;

import civilisation.Configuration;

public class TerrainTexture {

	private int r,g,b;
	private String texture;
	private String normalMap;
	private String displacementMap;
	private float height;
	private int erosion;
	private float tiling;
	private int merge;
	private int blur;
	private float isTextured;
	private float intensityHeightMap;
	
	public TerrainTexture(float h, float s, float b,float height, int erosion,int blur,int merge,float intensityHeightMap,String texture,String normalMap,String displacementMap){
		int color = Color.HSBtoRGB(h, s,b);
		this.r = (color >> 16) & 0x000000FF;
		this.g = (color >>8 ) & 0x000000FF;
		this.b = (color) & 0x000000FF;
		this.texture = texture;
		this.normalMap = normalMap;
		this.displacementMap = displacementMap;
		this.height = height;
		this.erosion = erosion;
		this.blur = blur;
		this.merge = merge;
		this.intensityHeightMap = intensityHeightMap;
	}

	public TerrainTexture(Color couleur, float height, int erosion,int blur,int merge,float intensityHeightMap, float tiling, String texture) {
		this.isTextured = 1.0f;
		this.r = couleur.getRed();
		this.g = couleur.getGreen();
		this.b = couleur.getBlue();
		File f = new File(Configuration.pathToRessources+ "/Skin/" + texture);
		this.texture = "";
		this.normalMap = "";
		this.displacementMap = "";
		if(f.isDirectory()){
			for (final File fileEntry : f.listFiles()) {
				if(fileEntry.getName().equals("Diffuse.png") || fileEntry.getName().equals("diffuse.png") || fileEntry.getName().equals("Albedo.png") || fileEntry.getName().equals("albedo.png") || fileEntry.getName().equals("Base_Color.png") || fileEntry.getName().equals("base_color.png") || fileEntry.getName().equals("Base_color.png")
						|| fileEntry.getName().equals("base_Color.png") || fileEntry.getName().equals("BaseColor.png") || fileEntry.getName().equals("Basecolor.png") || fileEntry.getName().equals("baseColor.png") || fileEntry.getName().equals("basecolor.png")){
					this.texture = Configuration.pathToRessources+ "/Skin/" + texture + "/" + fileEntry.getName();
				}
				else if(fileEntry.getName().equals("normal.png") || fileEntry.getName().equals("Normal.png") || fileEntry.getName().equals("Normal_OpenGL.png")){
					this.normalMap = Configuration.pathToRessources+ "/Skin/" + texture + "/" + fileEntry.getName();
				}
				else if(fileEntry.getName().equals("Height.png") || fileEntry.getName().equals("height.png") || fileEntry.getName().equals("DisplacementMap.png") || fileEntry.getName().equals("displacentMap.png") || fileEntry.getName().equals("displacentmap.png") || fileEntry.getName().equals("dispMap.png") || fileEntry.getName().equals("dispmap.png")){
					this.displacementMap = Configuration.pathToRessources+ "/Skin/" + texture + "/" + fileEntry.getName();
				}

			}
		}
		if(this.texture.equals(""))
			this.isTextured = 0.0f;
		this.height = height;
		this.erosion = erosion;
		this.tiling = tiling;
		this.blur = blur;
		this.merge=merge;
		this.intensityHeightMap = intensityHeightMap;
	}

	public TerrainTexture(TerrainTexture terrainTexture) {
		this.r = terrainTexture.getR();
		this.g = terrainTexture.getG();
		this.b = terrainTexture.getB();
		this.isTextured = terrainTexture.isTextured;
		this.texture = terrainTexture.texture;
		this.normalMap = terrainTexture.normalMap;
		this.displacementMap = terrainTexture.displacementMap;
		this.height = terrainTexture.height;
		this.erosion = terrainTexture.erosion;
		this.tiling = terrainTexture.tiling;
		this.blur = terrainTexture.blur;
		this.merge = terrainTexture.merge;
		this.intensityHeightMap = intensityHeightMap;
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

	public float getBlur() {
		return this.blur;
	}

	public float isTextured() {
		return isTextured;
	}

	public void setTextured(float isTextured) {
		this.isTextured = isTextured;
	}

	public int getMerge() {
		return merge;
	}

	public void setMerge(int merge) {
		this.merge = merge;
	}

	public float getIntensityHeightMap() {
		return intensityHeightMap;
	}

	public void setIntensityHeightMap(float intensityHeightMap) {
		this.intensityHeightMap = intensityHeightMap;
	}
	
	
	
}
