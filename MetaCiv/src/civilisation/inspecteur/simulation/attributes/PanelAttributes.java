package civilisation.inspecteur.simulation.attributes;

import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import civilisation.Civilisation;
import civilisation.Configuration;
import civilisation.constant.MCConstant;
import civilisation.inspecteur.simulation.PanelModificationSimulation;

public class PanelAttributes extends JPanel{

	PanelModificationSimulation panelParent;
	ArrayList<JTextField> attributesName = new ArrayList<JTextField>();
	ArrayList<JSpinner> attributesStartingValue = new ArrayList<JSpinner>();
	JComboBox comboIcon;
	Dimension minSize = new Dimension(50,30);
	public boolean isSetuping = false;
	
	public PanelAttributes (PanelModificationSimulation panelParent){
		super();
		this.panelParent = panelParent;
		this.setLayout(new GridLayout(1,2));
		
		TitledBorder bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Attributes");
		bordure.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(bordure);
		
		setupField();
		revalidate();

	}

	public void performChange() {
	
		//Configuration.getSchemaCognitifEnCourEdition().setAttributesNames(new ArrayList<String>());
		//Configuration.getSchemaCognitifEnCourEdition().setAttributesStartingValues(new ArrayList<Double>());
		int temp;
		//System.out.println("change");
		
		for (int i = 0 ; i < attributesName.size() ; i++) {
			if (!attributesName.get(i).getText().equals("") ) {
			temp = Configuration.getSchemaCognitifEnCourEdition().getAttributeIndexByName((attributesName.get(i).getText()));
			if(temp == -1)
			{
				Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().add(attributesName.get(i).getText());
				Configuration.getSchemaCognitifEnCourEdition().getAttributesStartingValues().add(Configuration.getSchemaCognitifEnCourEdition().getAttributeIndexByName((attributesName.get(i).getText())),(double)(attributesStartingValue.get(i).getValue()));
			}else
			{
				Configuration.getSchemaCognitifEnCourEdition().getAttributesStartingValues().set(temp,(double)(attributesStartingValue.get(i).getValue()));
			}
				
			}
		}
		if (attributesName.size() == Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size()) {
			//System.out.println(attributesName.size() + " " + Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size());
			setupField();
			revalidate();
		}
		
	}

	public void setupField() {
		attributesName = new ArrayList<JTextField>();
		attributesStartingValue = new ArrayList<JSpinner>();
		
		this.removeAll();
		
		isSetuping = true;
		
		this.setLayout(new GridLayout(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size()+1,2));

		for (int i = 0 ; i < Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size() ; i++) {
			attributesName.add(new JTextField(""));
			attributesName.get(i).setMinimumSize(minSize);

			attributesName.get(i).setName("Field " + i);
			attributesName.get(i).setText(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().get(i));
			attributesName.get(i).addActionListener(new ActionPanelAttributes(this));
			this.add(attributesName.get(i),i,0);
			/*Create JComboBox to select starting value*/
			SpinnerModel spinModel = new SpinnerNumberModel((Double)Configuration.getSchemaCognitifEnCourEdition().getAttributesStartingValues().get(i), (Double)0.0,(Double)1000.0,(Double)0.1);
			JSpinner spin = new JSpinner(spinModel);

			spin.setMinimumSize(minSize);
			spin.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
				performChange();
				}
			});
			attributesStartingValue.add(spin);
			this.add(spin,i,1);
		}

		attributesName.add(new JTextField(""));
		attributesName.get(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size()).setMinimumSize(minSize);

		attributesName.get(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size()).setName("Field " + Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size());
		attributesName.get(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size()).addActionListener(new ActionPanelAttributes(this));
		this.add(attributesName.get(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size()),Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size(),0);

		SpinnerModel spinModel = new SpinnerNumberModel((Double)0.0, (Double)(-10000.0),(Double)10000.0,(Double)0.1);
		JSpinner spin = new JSpinner(spinModel);
		spin.setMinimumSize(minSize);
		attributesStartingValue.add(spin);
		spin.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
			performChange();
			}
		});
		this.add(spin, Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size(),1);
		
		isSetuping = false;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		//this.performChange();
	}

}
