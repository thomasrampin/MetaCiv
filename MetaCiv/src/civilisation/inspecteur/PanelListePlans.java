package civilisation.inspecteur;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import I18n.I18nList;

import turtlekit.kernel.Turtle;
import civilisation.individu.Human;


/** 
 * Un sous panel pour repr___senter les croyances d'un agent
 * @author DTEAM
 * @version 1.0 - 3/2013
*/

public class PanelListePlans extends JPanel{

	JLabel titre = new JLabel(I18nList.CheckLang("Plans"));

	JTable tableau;
	Object[][] donnees = new Object[1000][2];
	JTableRendererPlans renderer;

	public PanelListePlans()
	{
		this.setLayout(new BorderLayout());
		this.add(titre, BorderLayout.NORTH);
		

        String[] entetes = {I18nList.CheckLang("Plans") ,I18nList.CheckLang("poids")};

        tableau = new JTable(donnees, entetes);
        renderer = new JTableRendererPlans();
        tableau.setDefaultRenderer(Object.class, renderer);

        
		this.add(tableau, BorderLayout.CENTER);

		
		this.setVisible(true);
	}


	
	public void actualiser(Turtle t)
	{
		for (int i=0; i < donnees.length;i++)
		{
			donnees[i][0] = null;
			donnees[i][1] = null;
		}
		for (int i = 0; i < ((Human) t).getEsprit().getPlans().size();i++)
		{
			donnees[i][0] = ((Human) t).getEsprit().getPlans().get(i);
			donnees[i][1] = ((Human) t).getEsprit().getPlans().get(i);
		}
		this.updateUI();
		
	}
}
	


