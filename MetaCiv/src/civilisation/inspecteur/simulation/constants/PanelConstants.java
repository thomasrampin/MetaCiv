package civilisation.inspecteur.simulation.constants;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import civilisation.Configuration;
import civilisation.constant.MCConstant;
import civilisation.inspecteur.simulation.PanelModificationSimulation;
import civilisation.inspecteur.simulation.attributes.ActionPanelAttributes;

public class PanelConstants extends JPanel{

	PanelModificationSimulation panelParent;
	ArrayList<JTextField> constantsName = new ArrayList<JTextField>();
	ArrayList<JSpinner> constantsValue = new ArrayList<JSpinner>();
	JComboBox comboIcon;
	public boolean isSetuping = false;
	Dimension minSize = new Dimension(50,30);
	
	public PanelConstants (PanelModificationSimulation panelParent){
		super();
		this.panelParent = panelParent;
		this.setLayout(new GridLayout(1,2));
		
		TitledBorder bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Constants");
		bordure.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(bordure);
		
		setupField();
		revalidate();

	}

	public void performChange() {
		
		//Configuration.getSchemaCognitifEnCourEdition().setConstantsNames(new ArrayList<String>());
		//Configuration.getSchemaCognitifEnCourEdition().setConstantsValues(new ArrayList<MCConstant>());
		MCConstant temp;
		
		for (int i = 0 ; i < constantsName.size() ; i++) {

			
			if (!constantsName.get(i).getText().equals("") ) {
				temp = Configuration.getSchemaCognitifEnCourEdition().getConstantByName(constantsName.get(i).getText());
				if(temp == null)
				{
					Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().add(constantsName.get(i).getText());
					Configuration.getSchemaCognitifEnCourEdition().getConstantsValues().add(new MCConstant((Double) constantsValue.get(i).getValue()));
				}else
				{
					
					temp.setValue((Double) constantsValue.get(i).getValue());
				}
			}
			
			//System.out.println(Configuration.attributesNames.get(i));
		}
		if (constantsName.size() == Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size()) {
			//System.out.println(constantsName.size() + " " + Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size());
			setupField();
			revalidate();
		}
		
	}

	public void setupField() {
		constantsName = new ArrayList<JTextField>();
		constantsValue = new ArrayList<JSpinner>();
		
		this.removeAll();
		
		isSetuping = true;
		if(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames() != null && Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size() > 0){
			this.setLayout(new GridLayout(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size()+1,2));
			for (int i = 0 ; i < Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size() ; i++) {
				constantsName.add(new JTextField(""));
				constantsName.get(i).setMinimumSize(minSize);
				constantsName.get(i).setName("Field " + i);
				constantsName.get(i).setText(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().get(i));
				constantsName.get(i).addActionListener(new ActionPanelConstants(this));
				this.add(constantsName.get(i),i,0);
				/*Create JComboBox to select starting value*/
				SpinnerModel spinModel = new SpinnerNumberModel((Double)Configuration.getSchemaCognitifEnCourEdition().getConstantsValues().get(i).getValue(), (Double)(-10000.0),(Double)10000.0,(Double)0.1);
				JSpinner spin = new JSpinner(spinModel);
				spin.setMinimumSize(minSize);
				spin.addChangeListener(new ChangeListener() {
					
					@Override
					public void stateChanged(ChangeEvent e) {
						performChange();
					}
				});
				constantsValue.add(spin);
				this.add(spin,i,1);
			}
			constantsName.add(new JTextField(""));
			constantsName.get(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size()).setMinimumSize(minSize);;
			constantsName.get(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size()).setName("Field " + Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size());
			constantsName.get(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size()).addActionListener(new ActionPanelConstants(this));
			this.add(constantsName.get(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size()),Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size(),0);

			SpinnerModel spinModel = new SpinnerNumberModel((Double)0.0, (Double)0.0,(Double)1000.0,(Double)0.1);
			JSpinner spin = new JSpinner(spinModel);
			spin.setMinimumSize(minSize);
			constantsValue.add(spin);
			this.add(spin, Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size(),1);
			
			isSetuping = false;
			
		}else{
			this.setLayout(new GridLayout(1,2));
			constantsName.add(new JTextField(""));
			constantsName.get(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size()).setMinimumSize(minSize);;
			constantsName.get(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size()).setName("Field " + Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size());
			constantsName.get(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size()).addActionListener(new ActionPanelConstants(this));
			this.add(constantsName.get(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size()),Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size(),0);

			SpinnerModel spinModel = new SpinnerNumberModel((Double)0.0, (Double)0.0,(Double)1000.0,(Double)0.1);
			JSpinner spin = new JSpinner(spinModel);
			spin.setMinimumSize(minSize);
			constantsValue.add(spin);
			this.add(spin, Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size(),1);
			
			isSetuping = false;
			
		}
		

		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		//this.performChange();
	}

}
