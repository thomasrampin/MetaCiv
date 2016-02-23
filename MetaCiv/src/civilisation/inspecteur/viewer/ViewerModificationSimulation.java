
package civilisation.inspecteur.viewer;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import turtlekit.viewer.AbstractObserver;
import turtlekit.viewer.TKDefaultViewer;
import civilisation.inspecteur.PanelInspecteur;
import civilisation.inspecteur.PanelOptions;
import civilisation.inspecteur.simulation.PanelModificationSimulation;


/** 
 * Viewer contenat l'inspecteur
 * @author DTEAM
 * @version 1.0 - 2/2013
*/

public class ViewerModificationSimulation extends TKDefaultViewer{

	PanelModificationSimulation contentPane;


		@Override
		public void setupFrame(JFrame frame) {
			super.setupFrame(frame);
			contentPane = new PanelModificationSimulation();
		    frame.setContentPane(contentPane);
			frame.setLocation(50, 0);
		}

		@Override
		public void observe(){
			//contentPane.actualiser();	
		}

}
