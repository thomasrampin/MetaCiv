package civilisation.inspecteur.simulation.constants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import civilisation.inspecteur.simulation.attributes.PanelAttributes;

public class ActionPanelConstants implements ActionListener{

	PanelConstants p;
	
	public ActionPanelConstants(PanelConstants p) {
		this.p = p;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Perf? : " + p.isSetuping);
		System.out.println(e.getSource().toString());
		if (!p.isSetuping) {
			p.performChange();
		}
	}

	
}
