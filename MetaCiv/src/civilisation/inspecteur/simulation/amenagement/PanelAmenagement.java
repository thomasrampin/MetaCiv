package civilisation.inspecteur.simulation.amenagement;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import I18n.I18nList;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.effects.Effect;
import civilisation.inspecteur.animations.JJIconSelector;
import civilisation.inspecteur.simulation.PanelModificationSimulation;
import civilisation.inspecteur.simulation.dialogues.DialogueChoisirCouleurAmenagement;
import civilisation.inspecteur.simulation.dialogues.DialogueChoisirCouleurTerrain;
import civilisation.inventaire.Objet;
import civilisation.world.Terrain;

public class PanelAmenagement extends JPanel implements ActionListener{

	PanelModificationSimulation panelParent;
	PanelListeAmenagement panelListeAmenagement;
	
	JTextField nameField;
	JTextField descriptionField;
	JComboBox comboIcon;
	JJIconSelector iconSelector;
	JLabel name;
	JLabel descriptionLabel;
	JLabel icon;
	JButton addEffect;
	JButton addRecette;
	ArrayList<PanelEffectA> effects;
	PanelRecetteA recette;
	JButton saveObject;
	JLabel ArdyLabel;
	JComboBox ArdyEffect;
	JButton addArdyEffect;
	Box b1 = Box.createVerticalBox();
	JLabel labelCouleur;
	JButton couleur;
	
	ArrayList<String> recettes;
	ArrayList<Integer> necessaires;
	
	
	public PanelAmenagement (PanelModificationSimulation panelParent , PanelListeAmenagement panelListeObjets){
		super();
		
	
		
		this.panelParent = panelParent;
		this.panelListeAmenagement = panelListeObjets;
		this.effects = new ArrayList<PanelEffectA>();

		TitledBorder bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), I18nList.CheckLang("Facility editor"));

		bordure.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(bordure);
		
		recette = new PanelRecetteA(this);
		recette.setVisible(false);


		ArdyLabel = new JLabel(I18nList.CheckLang("Add effect : "));
		ArdyEffect = new JComboBox();
		ArdyEffect.addActionListener(this);
		for(int i = 0; i < Configuration.effets.size();++i)
		{
			ArdyEffect.addItem(Configuration.effets.get(i).getName());
		}
		
		addArdyEffect = new JButton(I18nList.CheckLang("Add this effect"));

		addArdyEffect.setActionCommand("Ardy");
		addArdyEffect.addActionListener(this);

		this.ArdyLabel.setVisible(false);
		this.ArdyEffect.setVisible(false);
		this.addArdyEffect.setVisible(false);
		
		name = new JLabel(I18nList.CheckLang("Amenagement name : "));
		name.setVisible(false);
		this.add(name);
		nameField = new JTextField(40);
		nameField.addActionListener(new ActionPanelAmenagement(this));
		nameField.setVisible(false);
		this.add(nameField);
		
		//iconSelector = new JJIconSelector(Configuration.pathToIcon);
		//this.add(iconSelector);

		this.icon = new JLabel(I18nList.CheckLang("Icon :"));

		icon.setVisible(false);
		this.add(icon);
		comboIcon = new JComboBox();
		

		this.descriptionLabel = new JLabel(I18nList.CheckLang("Description :"));

		descriptionLabel.setVisible(false);
		this.add(descriptionLabel);
		//this.update();
		
		this.descriptionField = new JTextField(40);
		descriptionField.addActionListener(new ActionPanelAmenagement(this));

		descriptionField.setText(I18nList.CheckLang("Enter facility Description here"));
		this.descriptionField.setVisible(false);
		this.add(descriptionField);
	
		this.labelCouleur = new JLabel(I18nList.CheckLang("Facility color : "));
		this.couleur = new JButton(I18nList.CheckLang("Change color"));

		couleur.addActionListener(this);
		couleur.setActionCommand("Couleur");
		this.labelCouleur.setVisible(false);
		this.couleur.setVisible(false);
		Box b11 = Box.createHorizontalBox();
		b11.add(labelCouleur);
		b11.add(couleur);
		
		
		/*Start chances*/
		SpinnerModel spinModel = new SpinnerNumberModel(0, //initial value
                0, //min
                100, //max
                1); //pas

		
		

		this.addEffect = new JButton(I18nList.CheckLang("New effect"));

		this.addEffect.setVisible(false);
		this.addEffect.addActionListener(new ActionPanelAmenagement(this));
		this.add(addEffect);

		 
	    
		this.saveObject = new JButton(I18nList.CheckLang("save amenagement"));

		saveObject.setVisible(false);
		saveObject.setActionCommand("Save");
		saveObject.addActionListener(this);
		addEffect.setActionCommand("addEffect");
		Box b2 = Box.createHorizontalBox();
		b2.add(name);
		b2.add(nameField);
		Box b3 = Box.createHorizontalBox();
		b3.add(descriptionLabel);
		b3.add(descriptionField);
		
		Box b4 = Box.createHorizontalBox();
	
		b4.add(recette);

		
		Box b5 = Box.createHorizontalBox();
		JLabel rec = new JLabel(I18nList.CheckLang("Recipe"));
		b5.add(rec);		
		
		
		b1.add(b2);
		b1.add(b11);
		b1.add(b3);
		b1.add(b4);
		b1.add(addEffect);
		b1.add(saveObject);
		
		
		b1.add(ArdyLabel);
		b1.add(ArdyEffect);
		b1.add(addArdyEffect);

		
		this.add(b1);
	}
	
	/**
	 * Update data according to selected item in ItemList
	 */
	public void update(){	
		
		name.setVisible(true);
		nameField.setVisible(true);
		nameField.setText(((TypeAmenagement) panelListeAmenagement.getListeObjets().getSelectedValue()).getNom()  );
		
		this.couleur.setBackground(((TypeAmenagement) panelListeAmenagement.getListeObjets().getSelectedValue()).getColor());
		this.labelCouleur.setVisible(true);
		this.couleur.setVisible(true);
		
		this.addEffect.setVisible(true);
		this.ArdyLabel.setVisible(true);
		this.ArdyEffect.setVisible(true);
		this.addArdyEffect.setVisible(true);
		
		for(int i = 0; i < this.effects.size();++i)
		{
			this.effects.get(i).setVisible(false);
		}
		this.effects.clear();
	//	System.out.println("Taille des effets "+((Objet) panelListeObjets.getListeObjets().getSelectedValue()).getEffets().size());
		if(this.effects.size() <  ((TypeAmenagement) panelListeAmenagement.getListeObjets().getSelectedValue()).getEffets().size())
		{
			for(int i = 0; i < ((TypeAmenagement) panelListeAmenagement.getListeObjets().getSelectedValue()).getEffets().size(); ++i )
			{
				Effect ef = ((TypeAmenagement) panelListeAmenagement.getListeObjets().getSelectedValue()).getEffets().get(i);
				this.effects.add( new PanelEffectA(this,ef) );
				this.add(this.effects.get(this.effects.size() - 1));
			}
		}
	
		for(int i =0; i < this.effects.size(); ++i)
		{
			this.effects.get(i).setVisible(true);
		}
		recette.setVisible(true);
		this.recette.update(this);
		saveObject.setVisible(true);
	     
	}

	public PanelListeAmenagement getPanelListeObjets() {
		return panelListeAmenagement;
	}

	public void setPanelListeObjets(PanelListeAmenagement panelListeObjets) {
		this.panelListeAmenagement = panelListeObjets;
	}

	public void performChange() {
		
	}

	public void addEffects() {
		// TODO Auto-generated method stub
	//	System.out.println("New Effect");
		
		this.effects.add(new PanelEffectA(this));
		this.add(this.effects.get(this.effects.size() - 1));
		this.effects.get(this.effects.size()-1).RendreVisible();
		this.ArdyLabel.setVisible(true);
		this.ArdyEffect.setVisible(true);
		addArdyEffect.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	//	System.out.println(e.getActionCommand() );
		if(e.getActionCommand() == "Save")
		{
			if (panelListeAmenagement != null){
				if ( panelListeAmenagement.getListeObjets().getSelectedValue() != null) {
			
					TypeAmenagement a = ((TypeAmenagement) panelListeAmenagement.getListeObjets().getSelectedValue());
					a.setNom(nameField.getText());
					
					for(int i = 0; i < a.getEffets().size();++i)
					{
						a.getEffets().remove(i);
					}
					for(int i = 0; i < this.effects.size();++i)
					{
					//	System.out.println("hey "+i);
						Effect temp = this.effects.get(i).returnEffect();
					//	System.out.println(temp);
						a.addEffect(temp);
						Configuration.addEffectUnique(temp);
					}
					if(this.recettes != null && this.recettes.size() > 0)
					{
						for(int i = 0; i < this.recettes.size();++i)
						{
							a.addItemRecipe(this.recettes.get(i), this.necessaires.get(i));
						}
					}
					

					Configuration.addTypeAmenagementUnique(a);
				}
			}
		//	System.out.println("Nombre d'effets dans config : "+Configuration.effets.size());
		}

		if(e.getActionCommand() == "Ardy")
		{
			Effect ef = Configuration.getEffectByName(ArdyEffect.getSelectedItem().toString());
			this.effects.add( new PanelEffectA(this,ef) );
			this.add(this.effects.get(this.effects.size() - 1));
		}
		
		if(e.getActionCommand() == "item")
		{

			int i = 0;
		}
		if(e.getActionCommand() == "Couleur")
		{
			DialogueChoisirCouleurAmenagement d = new DialogueChoisirCouleurAmenagement((Frame) this.getTopLevelAncestor() , true ,((TypeAmenagement) panelListeAmenagement.getListeObjets().getSelectedValue()));
			d.show();
			this.update();
		}

	}

	public void setPanelListeAmenagement(PanelListeAmenagement panelListeAmenagement2) {
		this.panelListeAmenagement = panelListeAmenagement2;
		
	}
	
	
	
	
}
