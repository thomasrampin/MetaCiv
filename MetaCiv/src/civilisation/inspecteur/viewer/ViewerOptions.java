
package civilisation.inspecteur.viewer;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

import turtlekit.viewer.AbstractObserver;

import turtlekit.viewer.TKDefaultViewer;

import civilisation.inspecteur.PanelOptions;

/** 
 * Viewer contenant les options
 * @author DTEAM
 * @version 1.0 - 2/2013
*/

public class ViewerOptions extends TKDefaultViewer{

	PanelOptions agents;
		



		@Override
		public void setupFrame(JFrame frame) {
			super.setupFrame(frame);
			agents = new PanelOptions();
			frame.setContentPane(agents);
			frame.setLocation(50, 0);
		}

		@Override
		public void observe(){
		}
}
