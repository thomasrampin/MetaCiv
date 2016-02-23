package civilisation.inspecteur.simulation.amenagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionPanelAmenagement implements ActionListener{

	PanelAmenagement p;
	
	public ActionPanelAmenagement(PanelAmenagement p) {
		this.p = p;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println(e.getActionCommand());
		if(e.getActionCommand() == "addEffect")
		{
			p.addEffects();
		}
		else
		{
			
		}
		p.performChange();
	}

	
	
}
