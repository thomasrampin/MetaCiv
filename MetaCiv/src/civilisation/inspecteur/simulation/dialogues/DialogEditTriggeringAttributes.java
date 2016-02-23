package civilisation.inspecteur.simulation.dialogues;

import java.awt.Frame;

/**
 * Dialog to edit the triggering attributes of a cogniton 
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import I18n.I18nList;

import civilisation.Configuration;
import civilisation.DefineConstants;

import civilisation.inspecteur.simulation.GCogniton;
import civilisation.inspecteur.simulation.PanelStructureCognitive;

public class DialogEditTriggeringAttributes extends JDialog implements ActionListener, PropertyChangeListener{
	
	ArrayList<JComboBox> triggeringAttributes;
	ArrayList<JComboBox> comparator;
	ArrayList<JComboBox> constant;
	ArrayList<JRadioButton> selectors;

	ArrayList<JSpinner> triggeringValues;

	GCogniton gCogniton;
    JOptionPane optionPane;
    ArrayList<Object> array;
    
	public DialogEditTriggeringAttributes(Frame f , boolean modal, GCogniton gCogniton){
		super(f,modal);
		this.gCogniton = gCogniton;
		
		ButtonGroup grp;
		JRadioButton sel;
		
		triggeringAttributes = new ArrayList<JComboBox>();
		triggeringValues = new ArrayList<JSpinner>();
		comparator = new ArrayList<JComboBox>();
		constant = new ArrayList<JComboBox>();
		selectors = new ArrayList<JRadioButton>();
		
		for (int i = 0; i < gCogniton.getCogniton().getTriggeringAttributes().size(); i++){
			JComboBox box = new JComboBox();
			box.addItem("--NONE--");
			System.out.println(gCogniton.getCogniton().getTriggeringAttributes().get(i)[0]);
			
			for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size(); j++){
				box.addItem(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().get(j));
				if (gCogniton.getCogniton().getTriggeringAttributes().get(i)[0].equals(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().get(j))){
					box.setSelectedIndex(j+1);
				}
			}
			triggeringAttributes.add(box);
			
			box = new JComboBox();
			box.addItem("> ");  //+2
			box.addItem(">=");  //+1
			box.addItem("==");  // 0
			box.addItem("<= ");  //-1
			box.addItem("< ");  //-2
			comparator.add(box);
			box.setSelectedIndex(-1 * ((int)gCogniton.getCogniton().getTriggeringAttributes().get(i)[2] - 2));
			
			SpinnerNumberModel spinModel = new SpinnerNumberModel(0.0, -1000, 1000, 0.1);
			JSpinner spin = new JSpinner(spinModel);

			spin.setValue((double)gCogniton.getCogniton().getTriggeringAttributes().get(i)[1]);
			
			//box.setSelectedIndex(gCogniton.getCogniton().getLiensPlans().get(i).getPoids()+20);
			triggeringValues.add(spin);
			
			box = new JComboBox();
			box.addItem(DefineConstants.__MC_NULL_CONSTANT);
			box.setSelectedIndex(0);
			for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size(); j++){
				box.addItem(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().get(j));
				if (gCogniton.getCogniton().getTriggeringAttributes().get(i)[3].equals(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().get(j))){
					box.setSelectedIndex(j+1);
				}
			}
			constant.add(box);
			
			grp = new ButtonGroup();

			sel = new JRadioButton(I18nList.CheckLang("Value"));
			sel.setName(Integer.toString(i));
			sel.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					constant.get(Integer.parseInt(((JRadioButton)e.getSource()).getName())).setSelectedIndex(0);
				}
			});
			grp.add(sel);
			sel.setSelected(true);
			selectors.add(sel);
			sel = new JRadioButton(I18nList.CheckLang("Constant"));

			grp.add(sel);
			if(!constant.get(constant.size()-1).getSelectedItem().equals(DefineConstants.__MC_NULL_CONSTANT))
				sel.setSelected(true);
			selectors.add(sel);
			
		}
		ajouterBox();
		
		this.setTitle(I18nList.CheckLang("Edit triggering attributes"));
		
		
		array = new ArrayList<Object>();
	    for (int i = 0; i < triggeringAttributes.size(); i++){
	    	array.add(new JLabel("========== Trigger no "+(i+1)+" =========="));
	    	array.add(triggeringAttributes.get(i));
	    	array.add(comparator.get(i)); 
	    	array.add(selectors.get(i*2));
	    	array.add(triggeringValues.get(i));
	    	array.add(selectors.get((i*2)+1));
	    	array.add(constant.get(i)); 
	    }

	    Object[] options = {"OK" , "Cancel"};
	 
	    optionPane = new JOptionPane(array.toArray(),
	                                    JOptionPane.QUESTION_MESSAGE,
	                                    JOptionPane.YES_NO_OPTION,
	                                    null,
	                                    options,
	                                    options[0]); 
	    setContentPane(optionPane);
	        
	    optionPane.addPropertyChangeListener(this);
	        
		ImageIcon icone = new ImageIcon(System.getProperty("user.dir")+"/civilisation/graphismes/LogoMedium.png");
		optionPane.setIcon(icone);
		this.pack();
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		System.out.println(optionPane.getValue());
		if (isVisible() && (optionPane.getValue().equals("OK") || optionPane.getValue().equals("Cancel"))){
			if (optionPane.getValue().equals("OK")){
				//gCogniton.getCogniton().getTriggeringAttributes().clear();
				for (int i = 0; i < triggeringAttributes.size();i++){
					if (!triggeringAttributes.get(i).getSelectedItem().equals("--NONE--") && i < gCogniton.getCogniton().getTriggeringAttributes().size()){
						//System.out.println(triggeringAttributes.get(i).getSelectedIndex()-1 +" : "+ Configuration.plans.size());
						gCogniton.getCogniton().getTriggeringAttributes().get(i)[0] = triggeringAttributes.get(i).getSelectedItem();
						gCogniton.getCogniton().getTriggeringAttributes().get(i)[1] = triggeringValues.get(i).getValue();
						gCogniton.getCogniton().getTriggeringAttributes().get(i)[2] = ((Integer) comparator.get(i).getSelectedIndex() * (-1)) + 2;
						gCogniton.getCogniton().getTriggeringAttributes().get(i)[3] = (constant.get(i).getSelectedItem());
						gCogniton.getCogniton().getTriggeringAttributes().get(i)[4] = gCogniton.getCogniton();
						
						/*
						Object[] tab = new Object[4];
						tab[0] = triggeringAttributes.get(i).getSelectedItem();
						tab[1] = triggeringValues.get(i).getValue();
						tab[2] = ((Integer) comparator.get(i).getSelectedIndex() * (-1)) + 2;
						tab[3] = (constant.get(i).getSelectedItem());
						System.out.println("inde" + tab[2]);
						gCogniton.getCogniton().getTriggeringAttributes().add(tab);
*/						
					}
					else if(!triggeringAttributes.get(i).getSelectedItem().equals("--NONE--"))
					{
						Object[] tab = new Object[5];
						tab[0] = triggeringAttributes.get(i).getSelectedItem();
						tab[1] = triggeringValues.get(i).getValue();
						tab[2] = ((Integer) comparator.get(i).getSelectedIndex() * (-1)) + 2;
						tab[3] = (constant.get(i).getSelectedItem());
						tab[4] = gCogniton.getCogniton();
						System.out.println("inde" + tab[2]);
						gCogniton.getCogniton().getTriggeringAttributes().add(tab);						
					}
					else if(i < gCogniton.getCogniton().getTriggeringAttributes().size())
					{
						gCogniton.getCogniton().getTriggeringAttributes().remove(i);
						i--;
					}
				}
				((PanelStructureCognitive) gCogniton.getParent()).showTrigger(gCogniton);
				((PanelStructureCognitive) gCogniton.getParent()).clearTriggerLink();
				((PanelStructureCognitive) gCogniton.getParent()).createTriggerLink();
			}		
	        setVisible(false);
	        
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {


		if (!triggeringAttributes.isEmpty() && e.getSource().equals(triggeringAttributes.get(triggeringAttributes.size()-1)) && !((JComboBox) e.getSource()).getSelectedItem().equals("--NONE--")){
			System.out.println("add");
			ajouterBox();
	    	array.add(new JLabel("========== Trigger no "+triggeringAttributes.size()+" =========="));

			array.add(triggeringAttributes.get(triggeringAttributes.size()-1));
			array.add(comparator.get(triggeringAttributes.size()-1));
	    	array.add(selectors.get((triggeringAttributes.size()-1)*2));
			array.add(triggeringValues.get(triggeringAttributes.size()-1));
	    	array.add(selectors.get(((triggeringAttributes.size()-1)*2)+1));
	    	array.add(constant.get(triggeringAttributes.size()-1)); 
			
			optionPane.setMessage(array.toArray());
			this.pack();
		}
		
	}
	
	public void ajouterBox(){
		ButtonGroup grp;
		JRadioButton sel;
		JComboBox box = new JComboBox();
		box.addActionListener(this);
		box.addItem("--NONE--");
		for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size(); j++){
			box.addItem(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().get(j));
		}
		triggeringAttributes.add(box);
		
		box = new JComboBox();
		box.addItem("> ");  //+2
		box.addItem(">=");  //+1
		box.addItem("==");  // 0
		box.addItem("<= ");  //-1
		box.addItem("< ");  //-2
		comparator.add(box);
		
		SpinnerNumberModel spinModel = new SpinnerNumberModel(0.0, -1000, 1000, 0.1);
		JSpinner spin = new JSpinner(spinModel);
		
		//box.setSelectedIndex(gCogniton.getCogniton().getLiensPlans().get(i).getPoids()+20);
		triggeringValues.add(spin);

		box = new JComboBox();
		box.addItem(DefineConstants.__MC_NULL_CONSTANT);
		box.setSelectedIndex(0);
		for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size(); j++){
			box.addItem(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().get(j));
		}
		constant.add(box);
		
		grp = new ButtonGroup();

		sel = new JRadioButton(I18nList.CheckLang("Value"));
		sel.setName(Integer.toString(constant.size()-1));
		sel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				constant.get(Integer.parseInt(((JRadioButton)e.getSource()).getName())).setSelectedIndex(0);
			}
		});
		grp.add(sel);
		selectors.add(sel);

		sel = new JRadioButton(I18nList.CheckLang("Constant"));
		grp.add(sel);
		selectors.add(sel);
	}

	
	
}
	


