package civilisation.inspecteur.simulation.environnement;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import I18n.I18nList;
import civilisation.Configuration;
import civilisation.inspecteur.animations.JJPanel;
import civilisation.world.Terrain;

public class PanelTerrains extends JJPanel{

	JList listeTerrains;
	JPopupMenu popup;  
	DefaultListModel listModel;
	  
	public PanelTerrains(){

			this.setLayout(new BorderLayout());
		
			definirListe();
			
			this.add(listeTerrains , BorderLayout.CENTER);
	}


	public JList getListeTerrains() {
		return listeTerrains;
	}


	public void afficherPopup(MouseEvent e) {
		

		popup = new JPopupMenu(I18nList.CheckLang("Terrain"));
		
		JMenuItem editerPhero = new JMenuItem(I18nList.CheckLang("Edit terrain"));

		editerPhero.addActionListener(new ActionsMenuTerrain(this,0));
		editerPhero.setIcon(Configuration.getIcon("pencil.png"));
		popup.add(editerPhero);
		
		JMenuItem editerCouleur = new JMenuItem(I18nList.CheckLang("Change colour"));

		editerCouleur.addActionListener(new ActionsMenuTerrain(this,1));
		editerCouleur.setIcon(Configuration.getIcon("color--pencil.png"));
		popup.add(editerCouleur);
		
		JMenuItem supprimer = new JMenuItem(I18nList.CheckLang("Remove"));

		supprimer.addActionListener(new ActionsMenuTerrain(this,2));
		supprimer.setIcon(Configuration.getIcon("cross.png"));
		popup.add(supprimer);
		
		popup.show(this, e.getX(), e.getY());
	}
	
	public void definirListe(){
		
		listModel = new DefaultListModel();
		listeTerrains = new JList(listModel);

		for (int i = 0; i < Configuration.terrains.size(); i++){
			listModel.addElement(Configuration.terrains.get(i));
		}
		
		listeTerrains.addMouseListener(new MouseListeTerrains(this));
		listeTerrains.setCellRenderer(new ListeTerrainsRenderer());
	}
	
	public void addTerrain(Terrain t){
		((javax.swing.DefaultListModel)listeTerrains.getModel()).addElement(t);	
	}

	
	
}
	



	
	
	
