package civilisation.inspecteur.legend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import civilisation.Configuration;

public class PanelLegend extends JPanel {

	PanelLegendPopulation pop;
	PanelLegendFacility fac;
	PanelLegendPatch pat;
	public PanelLegend()
	{
		pop = new PanelLegendPopulation();
		pop.setPreferredSize(new Dimension(150,(int) 800) );
		pop.setMaximumSize(new Dimension(0,800));
		pop.setLocation(0, 0);
		
		fac = new PanelLegendFacility();
		fac.setPreferredSize(new Dimension(150,(int) 800) );
		fac.setMaximumSize(new Dimension(0,800));
		fac.setLocation(150, 0);
		
		pat = new PanelLegendPatch();
		pat.setPreferredSize(new Dimension(150,(int) 800) );
		pat.setMaximumSize(new Dimension(0,800));
		pat.setLocation(300,0);
		
		this.add(pop);
		this.add(fac);
		this.add(pat);
		this.setVisible(true);
	}
	

}
