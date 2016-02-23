package civilisation.inspecteur.simulation.dialogues;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import I18n.I18nList;
import civilisation.group.GroupAndRole;
import civilisation.inspecteur.simulation.environnement.PanelEnvironnement;
import civilisation.inspecteur.simulation.groupManager.GroupToolBar;
import civilisation.inspecteur.simulation.groupManager.GroupTreeToolBar;

public class DialogEditGroupOptions extends JDialog implements ActionListener, PropertyChangeListener{
	
	JTextField nom;
    JOptionPane optionPane;
    GroupToolBar source;
    JCheckBox autoJoinCheck;
    JComboBox<String> autoJoinRole;
    
	public DialogEditGroupOptions(Frame f , boolean modal , GroupToolBar source){
		super(f,modal);

		this.source = source;
		nom = new JTextField(20);

		nom.setText(source.groupModel.getName());

		
		this.setTitle("edit group options");

		
		autoJoinCheck = new JCheckBox("All agents join at birth");
		autoJoinCheck.setSelected(source.groupModel.isAutoJoin());
		autoJoinRole = new JComboBox(source.getRoleArray());
		if(autoJoinCheck.isSelected())
			autoJoinRole.setSelectedItem(source.groupModel.getAutoJoinRole());
	    Object[] array = {nom, autoJoinCheck, autoJoinRole};
	       

	    Object[] options = {"OK" , "Cancel"};
	 
	    //Create the JOptionPane.
	    optionPane = new JOptionPane(array,
	                                    JOptionPane.QUESTION_MESSAGE,
	                                    JOptionPane.YES_NO_OPTION,
	                                    null,
	                                    options,
	                                    options[0]); 
	    //Make this dialog display it.
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
				source.changeGroupName(nom.getText());
				if(autoJoinCheck.isSelected())
					source.setAutoJoin(autoJoinCheck.isSelected(),(String) autoJoinRole.getSelectedItem());
				else
					source.setAutoJoin(autoJoinCheck.isSelected(),null);
			}
	        setVisible(false);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
