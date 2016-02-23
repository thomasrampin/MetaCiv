package civilisation.inspecteur.simulation.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import civilisation.Configuration;
import civilisation.inspecteur.simulation.PanelModificationSimulation;

public class PanelConfiguration extends JPanel implements ActionListener{
	
	PanelModificationSimulation panelParent;
	
	JLabel labelPassages;
	JSpinner passages;
	
	JLabel labelEffacement;
	JSpinner effacement;
	
	JLabel labelVision;
	JSpinner Vision;
	
	JLabel labelDispersion;
	JCheckBox boxDispersion;
	
	JButton save;
	
	public PanelConfiguration(PanelModificationSimulation panelParent )
	{
		super();
		this.panelParent = panelParent;
		
		int temp = Configuration.passagesPourCreerRoute;
		int temp1 = Configuration.EffacementRoute;
		int temp2 = Configuration.VisionRadius;
		
		SpinnerModel modele =
		        new SpinnerNumberModel(temp, //initial value
		                               0, //min
		                              Integer.MAX_VALUE, //max
		                               1);    
		this.passages = new JSpinner(modele);
		this.passages.setVisible(true);
		this.labelPassages = new JLabel("Nombre de passages nécessaires pour créer une route : ");
		
		SpinnerModel modele1 =
		        new SpinnerNumberModel(temp1, //initial value
		                               0, //min
		                              Integer.MAX_VALUE, //max
		                               1);    
		this.effacement = new JSpinner(modele1);
		this.effacement.setVisible(true);
		this.labelEffacement = new JLabel("Nombre de passages a enlever du décompte tout les 75 ticks");
		
		SpinnerModel modele2 =
		        new SpinnerNumberModel(temp2, //initial value
		                               0, //min
		                              Integer.MAX_VALUE, //max
		                               1);    
		this.Vision = new JSpinner(modele2);
		this.Vision.setVisible(true);
		this.labelVision = new JLabel("Nombre de patch visibles par un agent");
		
		this.labelDispersion = new JLabel("Les agents sont dispersés autour du point de création ");
		this.boxDispersion = new JCheckBox("",false);

		
		save = new JButton("Save Configuration");
		save.addActionListener(this);
		save.setActionCommand("sauve");
		
		Box b1 = Box.createHorizontalBox();
		b1.add(this.labelPassages);
		b1.add(this.passages);
		
		Box b2 = Box.createHorizontalBox();
		b2.add(this.labelEffacement);
		b2.add(this.effacement);
		
		Box b3 = Box.createHorizontalBox();
		b3.add(this.labelVision);
		b3.add(this.Vision);
		
		Box b5 = Box.createHorizontalBox();
		b5.add(this.labelDispersion);
		b5.add(this.boxDispersion);
		
		Box b4 = Box.createVerticalBox();
		b4.add(b1);
		b4.add(b2);
		b4.add(b3);
		b4.add(b5);
		b4.add(save);
		
		this.add(b4);
		
		b4.setVisible(true);
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getActionCommand() == "sauve")
		{
			Configuration.VisionRadius = (int) this.Vision.getValue();
			Configuration.passagesPourCreerRoute = (int) this.passages.getValue();
			Configuration.EffacementRoute = (int) this.effacement.getValue();
			Configuration.dispersion = this.boxDispersion.isSelected();
		}
	}

}
