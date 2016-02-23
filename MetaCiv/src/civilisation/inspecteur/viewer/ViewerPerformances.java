
package civilisation.inspecteur.viewer;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

import turtlekit.viewer.AbstractObserver;
import turtlekit.viewer.TKDefaultViewer;

import civilisation.inspecteur.PanelInspecteur;
import civilisation.inspecteur.PanelPerformances;

/** 
 * Viewer contenant les infos de performances
 * @author DTEAM
 * @version 1.0 - 2/2013
*/

public class ViewerPerformances extends TKDefaultViewer{

	PanelPerformances panel;


		@Override
		public void setupFrame(JFrame frame) {
			super.setupFrame(frame);
			panel = new PanelPerformances();
			frame.setContentPane(panel);
			frame.setLocation(50, 0);
		}

		@Override
		public void observe(){
			panel.actualiser();	
		}
}
