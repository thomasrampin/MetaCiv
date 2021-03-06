
package civilisation.inspecteur.viewer;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;



import turtlekit.viewer.AbstractObserver;
import turtlekit.viewer.TKDefaultViewer;

import civilisation.individu.Human;
import civilisation.inspecteur.PanelAgentData;
import civilisation.inspecteur.PanelAgentLog;
import civilisation.inspecteur.PanelInspecteur;
import civilisation.inspecteur.PanelMind;
import civilisation.inspecteur.PanelMindData;
import civilisation.inspecteur.PanelMiniMap;
import civilisation.inspecteur.PanelOptions;
import civilisation.inspecteur.PanelPerformances;
import civilisation.inspecteur.groupPanel.PanelGroupOfAnAgent;
import civilisation.inspecteur.simulation.PanelModificationSimulation;
import civilisation.inspecteur.tableauDeBord.PanelInfos;
import civilisation.world.WorldViewer;


/** 
 * Viewer contenat l'inspecteur
 * @author DTEAM
 * @version 1.0 - 2/2013
*/

public class ViewerHuman extends TKDefaultViewer{

		JTabbedPane contentPane;

		PanelInspecteur panelInspecteur;
		PanelMind panelMind;
		PanelGroupOfAnAgent panelGroupOfAnAgent;
		PanelMiniMap miniMap;
		PanelMindData panelMindData;
		PanelAgentData panelAgentData;
		PanelAgentLog panelAgentLog;
		
		Human h;
	  
		public ViewerHuman(Human h) {
			super();
			this.h = h;
		}
		


		@Override
		public void setupFrame(JFrame frame) {
			super.setupFrame(frame);
			
			panelInspecteur = new PanelInspecteur(h);
			panelMind = new PanelMind(h);
			panelGroupOfAnAgent = new PanelGroupOfAnAgent(h);
			miniMap = new PanelMiniMap(h);
			panelMindData = new PanelMindData(h);
			panelAgentData = new PanelAgentData(h);
			panelAgentLog = new PanelAgentLog(h);

		    contentPane = new JTabbedPane();
		    contentPane.addTab("Mind",new JSplitPane(JSplitPane.VERTICAL_SPLIT,panelMindData,new JScrollPane(panelMind)) );
		    contentPane.addTab("Data",panelInspecteur);
		    contentPane.addTab("Charts",panelAgentData);
		    contentPane.addTab("Groups", new JScrollPane(panelGroupOfAnAgent));
		    contentPane.addTab("MiniMap", miniMap);
		    contentPane.addTab("Log", new JScrollPane(panelAgentLog));

		    //contentPane.setPreferredSize(new Dimension(400,400));

		    frame.setContentPane(contentPane);
		   // frame.setContentPane(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,contentPane,miniMap));
			frame.setLocation(50, 0);
		}

		@Override
		public void observe(){
			if (h.isAlive()) {
				//contentPane.setPreferredSize(new Dimension(400,400));
				panelInspecteur.actualiser();
				miniMap.updatePosition();
				panelMind.updateData();
				panelMindData.updateData();
				panelAgentData.updateData();
				panelAgentLog.updateData();
			}
			else {
				this.killAgent(this);
			}

		}
}
