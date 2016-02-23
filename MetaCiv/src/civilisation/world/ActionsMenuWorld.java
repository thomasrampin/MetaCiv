package civilisation.world;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import turtlekit.kernel.Patch;
import turtlekit.kernel.Turtle;
import civilisation.DefineConstants;
import civilisation.individu.Human;
import civilisation.inspecteur.PanelInspecteur;
import civilisation.inspecteur.simulation.dialogues.DialogueChoisirCouleurTerrain;
import civilisation.inspecteur.simulation.dialogues.DialogueEditerTerrain;
import civilisation.world.Terrain;

public class ActionsMenuWorld implements ActionListener{

	WorldViewer w;
	int index;
	Patch p;
	
	public ActionsMenuWorld(WorldViewer w, int i, Patch p)
	{
		this.w = w;
		index = i;
		this.p = p;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (index == 0){			
			List<Turtle> list = p.getTurtlesWithRole(DefineConstants.Role_Human);
			if (list.size() != 0) {
				Human h = (Human)list.get(0);
				//System.out.println(h.getName());
				w.observeHuman(h);
			}
		}
		else if (index == 1){
			List<Turtle> list = p.getTurtlesWithRole(DefineConstants.Role_Human);
			if (list.size() != 0) {
				for (int i = 0 ; i < list.size(); i++) {
					Human h = (Human)list.get(i);
					w.observeHuman(h);
				}
			}
		}		
	}
}
