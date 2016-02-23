package civilisation.inspecteur.simulation.amenagement;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.border.TitledBorder;

import I18n.I18nList;

import civilisation.Configuration;
import civilisation.amenagement.TypeAmenagement;
import civilisation.inspecteur.animations.JJPanel;
import civilisation.inventaire.Objet;

public class PanelListeAmenagement extends JJPanel{

	JList listeAmenagement;
	JPopupMenu popup;  
	DefaultListModel listModel;
	PanelAmenagement panelAmenagement;
	
	
	public PanelListeAmenagement (PanelAmenagement panelObjets) {
		
		this.panelAmenagement = panelObjets;
		
		this.setLayout(new BorderLayout());
		
		setupList();
		
		this.add(listeAmenagement , BorderLayout.CENTER);


		TitledBorder bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), I18nList.CheckLang("Amenagement list"));

		bordure.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(bordure);
		
	}


	private void setupList() {
		
		listModel = new DefaultListModel();
		listeAmenagement = new JList(listModel);

		for (int i = 0; i < Configuration.amenagements.size(); i++){
			listModel.addElement(Configuration.amenagements.get(i));
		}
		
		listeAmenagement.addMouseListener(new MouseListeAmenagement(this));
		listeAmenagement.setCellRenderer(new ListeAmenagementRenderer());
	}
	

	public JList getListeObjets() {
		return listeAmenagement;
	}


	public PanelAmenagement getPanelAmenagement() {
		return panelAmenagement;
	}


	public void addAmenagement(TypeAmenagement a) {
		((javax.swing.DefaultListModel)listeAmenagement.getModel()).addElement(a);	
		
	}

	
	
	
}
