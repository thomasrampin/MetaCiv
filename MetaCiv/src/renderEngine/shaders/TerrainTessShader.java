package renderEngine.shaders;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.ARBBindlessTexture;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.entities.Camera;
import renderEngine.entities.Light;
import renderEngine.utils.Matrix;

public class TerrainTessShader extends ShaderTessProgram {

	private static final int MAX_TERRAIN_TYPE = 11;
	
	private static final String VERTEX_FILE = "src/renderEngine/shaders/terrainTessVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/shaders/terrainTessFragmentShader.glsl";
	private static final String TESS_CONTROL_FILE = "src/renderEngine/shaders/terrainTesstcsShader.glsl";
	private static final String TESS_EVALUATION_FILE = "src/renderEngine/shaders/terrainTesstesShader.glsl";
	private static final String GEOMETRY_FILE = "src/renderEngine/shaders/terrainTessGeometryShader.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_mvp;
	private int location_dmap_depth;
	private int location_tessLevel;

	private int location_lightPosition;
	private int location_lightColour;
	
	private int location_dispMap;
	private int location_normalMap;
	private int location_dispMap_sub;
	private int location_normalMap_sub;
	private int location_blendMap;
	
	private int location_heights[];
	private int location_heights_size;
	
	private int location_forceLow;
	
	private int location_plane;
	private int location_distanceFog;

	
	public TerrainTessShader() {
		super(VERTEX_FILE, FRAGMENT_FILE,TESS_CONTROL_FILE,TESS_EVALUATION_FILE,GEOMETRY_FILE);
	}

	@Override
	protected void bindAttributes() {
		/*super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normals");*/
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_mvp = super.getUniformLocation("mvp");
		location_dmap_depth = super.getUniformLocation("dmap_depth");
		location_tessLevel = super.getUniformLocation("tessLevel");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_dispMap = super.getUniformLocation("tex_displacement"); 
		location_dispMap_sub = super.getUniformLocation("tex_displacement_sub");
		location_normalMap = super.getUniformLocation("normalMap"); 
		location_blendMap = super.getUniformLocation("blendMap");
		location_normalMap_sub = super.getUniformLocation("normalMap_sub");
		
		location_distanceFog = super.getUniformLocation("distanceFog");
		
		location_forceLow = super.getUniformLocation("forceLow");
		
		location_plane = super.getUniformLocation("plane");
		
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		
		location_heights_size = super.getUniformLocation("heights_size");
		

		
		location_heights = new int[MAX_TERRAIN_TYPE];
		for(int i=0;i<MAX_TERRAIN_TYPE;i++){
			location_heights[i] = super.getUniformLocation("heights["+i+"]");
		}
		
	}
	
	public void conectTextureDiff(int id){
		String name = "gSampler["+id+"]";
		int location = super.getUniformLocation(name);
		super.loadInt(location, id+5);
		
	}
	
	public void conectTextureNorm(int id,int offset){
		String name = "nrmSampler["+id+"]";
		int location = super.getUniformLocation(name);
		super.loadInt(location, id+5+offset);
		
	}
	
	public void loadDistanceFog(float distanceFog){
		super.loadFloat(location_distanceFog, distanceFog);
	}
	
	public void loadforceLow(boolean forceLow){
		super.loadBoolean(location_forceLow, forceLow);
	}
	
	public void loadPlane(Vector4f plane){
		super.loadVector4(location_plane, plane);
	}
	
	public void loadHeights(ArrayList<Vector4f> heights){
		super.loadInt(location_heights_size, heights.size());
		for(int i=0;i<heights.size();i++){
			int color = Color.HSBtoRGB(heights.get(i).x, heights.get(i).y, heights.get(i).z);
			float r = (color >> 16) & 0x000000FF;
			float g = (color >>8 ) & 0x000000FF;
			float b = (color) & 0x000000FF;
			Vector4f colorV = new Vector4f(r/255,g/255,b/255,heights.get(i).w/10);
			super.loadVector4(location_heights[i], colorV);
		}
	}
	
	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_blendMap, 0);
		super.loadInt(location_dispMap, 1);
		super.loadInt(location_normalMap, 2);
		super.loadInt(location_dispMap_sub, 3);
		super.loadInt(location_normalMap_sub, 4);
	}
	
	public void loadDmap_depth(float dmap_depth){
		super.loadFloat(location_dmap_depth, dmap_depth);
	}
	
	public void loadMvp(Matrix4f matrix){
		super.loadMatrix(location_mvp, matrix);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Matrix.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadTessLevel(float tessLevel) {
		super.loadFloat(location_tessLevel, tessLevel);
	}

	public void connectBindless(long handle, int id) {
		String name = "gSampler["+id+"]";
		int location = super.getUniformLocation(name);
		ARBBindlessTexture.glUniformHandleui64ARB(location, handle);  
	}
	

}