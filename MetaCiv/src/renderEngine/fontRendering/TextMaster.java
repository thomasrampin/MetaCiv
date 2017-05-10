package renderEngine.fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.loaders.Loader;
import renderEngine.entities.Camera;
import renderEngine.fontMeshCreator.FontType;
import renderEngine.fontMeshCreator.GUIText;
import renderEngine.fontMeshCreator.TextMeshData;

public class TextMaster {

	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;

	public static void init(Loader theLoader) {
		renderer = new FontRenderer();
		loader = theLoader;
	}

	public static void render(Camera camera) {
		renderer.render(texts,camera);
	}

	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}

	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}

	public static void checkId(int id,String msg, Vector3f position, FontType font2) {
		for (FontType font : texts.keySet()) {
			for (GUIText text : texts.get(font)) {
				
				if(text.getId() == id){
					text.setPosition(position);
					text.setTextString(msg);
					return;
				}
			}
		}
		new GUIText(msg, 100, font2, new Vector3f(position), 200f, false,id,false);
	}

	public static void switchButton(String button){
		for (FontType font : texts.keySet()) {
			for (GUIText text : texts.get(font)) {
				
				if(text.getTextString().equals(button)){
					text.setVisible(!text.isVisible());
					
				}
			}
		}
	}

	public static void hideDebugString() {
		for (FontType font : texts.keySet()) {
			for (GUIText text : texts.get(font)) {
				
				if(text.getId()!=-1){
					text.setVisible(false);
				}
			}
		}
	}
	
	public static void showDebugString() {
		for (FontType font : texts.keySet()) {
			for (GUIText text : texts.get(font)) {
				
				if(text.getId()!=-1){
					text.setVisible(true);
				}
			}
		}
	}

	public static void resetDebugString() {
		for (FontType font : texts.keySet()) {
			for (GUIText text : texts.get(font)) {
				
				if(text.getId()!=-1){
					text.setPosition(new Vector3f(0,-1000,0));
				}
			}
		}
	}
}
