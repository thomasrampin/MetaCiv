package civilisation.inspecteur;

import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import I18n.I18nList;

/** 
 * Fen___tre contenat l'inspecteur
 * @author DTEAM
 * @version 1.0 - 2/2013
*/

public class FenetreInspecteur extends JFrame{

	JTabbedPane contentPane;
	  


	  public FenetreInspecteur(String nomFenetre){
		    super(nomFenetre);
		    this.setSize(350,550);


		    contentPane = new JTabbedPane();

		    contentPane.addTab(I18nList.CheckLang("Agents"), new PanelInspecteur());
		    contentPane.addTab(I18nList.CheckLang("Options"), new PanelOptions());

		    this.setTitle(nomFenetre);
		    this.setVisible(true);
		    this.setContentPane(contentPane);
		    }
	  
	  
	  public void actualiser(MouseEvent e)
	  {
		 // contentPane.actualiser();
	  }
		    
}
