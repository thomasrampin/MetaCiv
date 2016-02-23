package civilisation.inspecteur.simulation.amenagement;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import civilisation.inspecteur.simulation.objets.PanelListeObjets;

public class MouseListeAmenagement implements MouseListener{

	PanelListeAmenagement p;
	
	public MouseListeAmenagement(PanelListeAmenagement p)
	{
		this.p = p;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		

	    if(SwingUtilities.isLeftMouseButton(e)){
			p.getPanelAmenagement().update();
			//p.afficherPopup(e);
	    }	
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	


}
