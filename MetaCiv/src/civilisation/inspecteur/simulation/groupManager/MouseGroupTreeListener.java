package civilisation.inspecteur.simulation.groupManager;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

public class MouseGroupTreeListener implements MouseListener{

	PanelGroupTree p;
	
	public MouseGroupTreeListener(PanelGroupTree p)
	{
		this.p = p;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	    if(SwingUtilities.isLeftMouseButton(e)){
	    	if(p.getGroupTree().getPathForLocation(e.getX(), e.getY()) != null)
	    		p.changeSelection(((NodeGroupTree)p.getGroupTree().getPathForLocation(e.getX(), e.getY()).getLastPathComponent()).getGm());
	    	else
	    		p.changeSelection(null);	
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
