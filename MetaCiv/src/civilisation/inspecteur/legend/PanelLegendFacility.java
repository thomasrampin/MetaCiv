package civilisation.inspecteur.legend;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import civilisation.Configuration;

public class PanelLegendFacility extends JPanel{

	public PanelLegendFacility()
	{
		this.setVisible(true);
		this.repaint();
	}
	
	public void paint(Graphics g)
	{
		Font fonte = new Font("TimesRoman ",Font.BOLD,14);
		g.setFont(fonte);
		g.drawString("Facilities", 35, 20);
		Font fontes = new Font("Dialog",10,10);
		g.setFont(fontes);
		for(int i = 0; i < Configuration.amenagements.size();++i)
		{
			g.setColor(Configuration.amenagements.get(i).getColor());
			for (int j = 0; j < 10; j++)
			{
				int x = 10;
				int y = 50 + i *20;
				if (j%2 == 1)
				{
					g.drawLine(x, y+j, x+10-1, y+j);
				}

			}
			//g.fillRect(10, 50 + i *20, 15, 15);
			g.setColor(Color.black);
			g.drawString(" : "+Configuration.amenagements.get(i).getNom(), 30, 60 + i *20);
			
		}
	}
}

