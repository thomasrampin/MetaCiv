package civilisation.inspecteur.simulation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MousePanelStructureCognitiveListener  implements MouseListener, KeyListener{

	private PanelStructureCognitive panelStructureCognitive;
	
	public MousePanelStructureCognitiveListener(PanelStructureCognitive p){
		this.panelStructureCognitive = p;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		panelStructureCognitive.enleverPanelsModifPoidsLI();
		panelStructureCognitive.enleverPanelEdit();
		panelStructureCognitive.requestFocus();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		panelStructureCognitive.enleverBoutonsModifRapide();
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
        
		 if ((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			 panelStructureCognitive.afficherPanelsModifPoidsLI();
         }
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
