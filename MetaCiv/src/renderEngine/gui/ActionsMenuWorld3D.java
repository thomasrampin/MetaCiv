package renderEngine.gui;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import civilisation.DefineConstants;
import civilisation.individu.Human;
import civilisation.world.WorldViewer;
import renderEngine.fontMeshCreator.FontType;
import renderEngine.fontRendering.TextMaster;
import renderEngine.gui.Button;
import turtlekit.kernel.Turtle;

public class ActionsMenuWorld3D extends Button {

	String message;
	int index;
	WorldViewer w;
	Turtle t;
	
	public ActionsMenuWorld3D(Vector3f color, Vector2f position, Vector2f dimension, String label,FontType font, int i,WorldViewer w) {
		super(color, position, dimension,label,font);
		this.message = label;
		index=i;
		this.w=w ;
	}

	public void setSelectedAgent(Turtle t){
		this.t = t;
	}
	
	@Override
	protected void action() {
		
		if(index==1){//Observe agent

			Human h = (Human)t;
			//System.out.println(h.getName());
			w.observeHuman(h);
			
		}else if(index==2){//Active debug string
			w.activDebug = !w.activDebug;
			if(w.activDebug )
				TextMaster.showDebugString();
			else
				TextMaster.hideDebugString();
		}
			
	}




	
}
