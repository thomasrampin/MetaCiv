package civilisation.inspecteur.simulation.amenagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import civilisation.Configuration;
import civilisation.amenagement.TypeAmenagement;
import civilisation.inspecteur.simulation.objets.PanelListeObjets;
import civilisation.inventaire.Objet;

public class ActionsToolBarListeAmenagement implements ActionListener{


	int index;
	PanelListeAmenagement p;
	
	
	public ActionsToolBarListeAmenagement(PanelListeAmenagement p, int i)
	{
		this.p = p;
		index = i;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (index == 0){
			System.out.println("--New amenagement--");
			TypeAmenagement a = new TypeAmenagement("Default_" + Configuration.amenagements.size());
			Configuration.amenagements.add(a);
			p.addAmenagement(a);
			
		}
	}

}
