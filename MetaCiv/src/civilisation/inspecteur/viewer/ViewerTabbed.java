
package civilisation.inspecteur.viewer;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import I18n.I18nList;
import turtlekit.viewer.AbstractObserver;
import turtlekit.viewer.TKDefaultViewer;
import civilisation.Configuration;
import civilisation.inspecteur.PanelCharts;
import civilisation.inspecteur.PanelInspecteur;
import civilisation.inspecteur.PanelOptions;
import civilisation.inspecteur.PanelPerformances;
import civilisation.inspecteur.legend.PanelLegend;
import civilisation.inspecteur.legend.PanelLegendPopulation;
import civilisation.inspecteur.simulation.PanelModificationSimulation;
import civilisation.inspecteur.tableauDeBord.PanelInfos;


/** 
 * Main viewer
 * @author DTEAM
 * @version 1.0 - 2/2013
*/

public class ViewerTabbed extends TKDefaultViewer{

		JTabbedPane contentPane;

		PanelInspecteur panelInspecteur;
		PanelPerformances panelPerformances;
		PanelCharts panelCharts;
	  


		@Override
		public void setupFrame(JFrame frame) {
			super.setupFrame(frame);
			Configuration.viewer = this;
			//getFrame().getJMenuBar().setVisible(false);

			panelPerformances = new PanelPerformances();
			panelInspecteur = new PanelInspecteur();
			panelCharts = new PanelCharts();
			
			//Add the different tab
		    contentPane = new JTabbedPane();
		    contentPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		    contentPane.addTab(I18nList.CheckLang("Simulation"), new PanelModificationSimulation());  //simulation editing panel
		    contentPane.addTab(I18nList.CheckLang("Agent"), panelInspecteur); //Agent viewer
		    contentPane.addTab(I18nList.CheckLang("Options"), new PanelOptions()); //Some options
		    contentPane.addTab(I18nList.CheckLang("Performances"), panelPerformances); //Some info about system perf
		    contentPane.addTab(I18nList.CheckLang("Tableau de bord"), new PanelInfos()); //Information panel (need to be improved)
		    contentPane.addTab(I18nList.CheckLang("Charts"), panelCharts); //Information panel
		    contentPane.addTab(I18nList.CheckLang("Legend"),new JScrollPane( new PanelLegend()));

		    frame.setContentPane(contentPane);
			frame.setLocation(50, 0);
		}

		@Override
		public void observe(){
			panelPerformances.actualiser();
			panelInspecteur.actualiser();
			panelCharts.updateData();
		}
}
