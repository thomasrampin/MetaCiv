package civilisation.inspecteur.legend;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import civilisation.Configuration;

public class PanelLegendPatch extends JPanel {

	public PanelLegendPatch()
	{
		this.setVisible(true);
		this.repaint();
	}
	
	public void paint(Graphics g)
	{
		Font fonte = new Font("TimesRoman ",Font.BOLD,14);
		g.setFont(fonte);
		g.drawString("Patches", 35, 20);
		Font fontes = new Font("Dialog",10,10);
		g.setFont(fontes);
		for(int i = 0; i < Configuration.terrains.size();++i)
		{
			g.setColor(Configuration.terrains.get(i).getCouleur());
			g.fillRect(10, 50 + i *20, 15, 15);
			g.setColor(Color.black);
			g.drawString(" : "+Configuration.terrains.get(i).getNom(), 30, 60 + i *20);
			
		}
	}
}

