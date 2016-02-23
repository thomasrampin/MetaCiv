package civilisation.inspecteur.simulation.civilisations;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import civilisation.Civilisation;
import civilisation.Configuration;
import civilisation.SchemaCognitif;
import civilisation.inspecteur.simulation.PanelModificationSimulation;

public class PanelCivilisations extends JPanel{

	PanelModificationSimulation panelParent;
	JTextField nameField;
	JComboBox comboIcon;
	JComboBox comboCogScheme;
	PanelListeCivilisations panelListeCivilisations;
    JSlider startingAgents;
    BoxLayout layout;
	
	public PanelCivilisations (PanelModificationSimulation panelParent , PanelListeCivilisations panelListeCivilisations){
		super();
		this.panelParent = panelParent;
		this.panelListeCivilisations = panelListeCivilisations;
		
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		TitledBorder bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Civilization editor");
		bordure.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(bordure);
		
		this.add(new JLabel("Civilisation name :"));

		nameField = new JTextField();
		nameField.addActionListener(new ActionPanelCivilisation(this));
		this.add(nameField, layout);
		nameField.setPreferredSize(new Dimension(600,20));
		nameField.setMaximumSize(nameField.getPreferredSize());

		/* number of starting agents selection*/
		this.add(new JLabel("Number of agent in this civilisation : "));
		startingAgents = new JSlider(0,500,1);
		startingAgents.setMajorTickSpacing(100);
		startingAgents.setMinorTickSpacing(10);
		startingAgents.setPaintTicks(true);
		startingAgents.setPaintLabels(true);
		startingAgents.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				//performChange();
				System.out.println("slide change");
			}
		});
		this.add(startingAgents,layout);
		
		this.add(new JLabel("cognitive scheme : "));
		SchemaCognitif [] sc = new SchemaCognitif[Configuration.SchemasCognitifs.size()];
		sc = Configuration.SchemasCognitifs.toArray(sc);
		comboCogScheme = new JComboBox<SchemaCognitif>(sc);
		comboCogScheme.addActionListener(new ActionPanelCivilisation(this));

		comboCogScheme.setPreferredSize(new Dimension(600,20));
		comboCogScheme.setMaximumSize(nameField.getPreferredSize());

		this.add(comboCogScheme,layout);
	
	}

	public void refreshCognitiveSchemes()
	{
		comboCogScheme.removeAllItems();
		for(SchemaCognitif s :Configuration.SchemasCognitifs)
			comboCogScheme.addItem(s);
		update();
	}
	
	public void performChange() {
		if (panelListeCivilisations != null){
			if ( panelListeCivilisations.getListeCivilisations().getSelectedValue() != null) {
				Civilisation c = ((Civilisation) panelListeCivilisations.getListeCivilisations().getSelectedValue());
				c.setNom(nameField.getText());
				c.setAgentsInitiaux(startingAgents.getValue());
				c.setCognitiveScheme((SchemaCognitif)comboCogScheme.getSelectedItem());
				System.out.println("Changed civ");
			}
		}
	}
	
	/**
	 * Update data according to selected item in ItemList
	 */
	public void update(){	
		if (panelListeCivilisations != null){
			if ( panelListeCivilisations.getListeCivilisations().getSelectedValue() != null) {
		
				nameField.setText(((Civilisation) panelListeCivilisations.getListeCivilisations().getSelectedValue()).getNom()  );
				startingAgents.setValue((((Civilisation) panelListeCivilisations.getListeCivilisations().getSelectedValue()).getAgentsInitiaux()))  ;
				comboCogScheme.setSelectedItem(((Civilisation) panelListeCivilisations.getListeCivilisations().getSelectedValue()).getCognitiveScheme());
				System.out.println(((Civilisation) panelListeCivilisations.getListeCivilisations().getSelectedValue()).getCognitiveScheme().getNom());
				System.out.println(((Civilisation) panelListeCivilisations.getListeCivilisations().getSelectedValue()).getNom());
				}
			}
		}

	public PanelListeCivilisations getPanelListeCivilisations() {
		return panelListeCivilisations;
	}

	public void setPanelListeCivilisations(
			PanelListeCivilisations panelListeCivilisations) {
		this.panelListeCivilisations = panelListeCivilisations;
	}
	
	
	
}
