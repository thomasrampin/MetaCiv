package civilisation.inspecteur.simulation.dialogues;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import I18n.I18nList;

import civilisation.inspecteur.simulation.environnement.PanelEnvironnement;
import civilisation.inspecteur.simulation.groupManager.GroupToolBar;
import civilisation.inspecteur.simulation.groupManager.GroupTreeToolBar;

public class DialogCreateRole extends JDialog implements ActionListener, PropertyChangeListener{
	
	JTextField nom;
    JOptionPane optionPane;
    GroupToolBar source;
    
	public DialogCreateRole(Frame f , boolean modal , GroupToolBar source){
		super(f,modal);

		this.source = source;
		nom = new JTextField(20);
		nom.setText(I18nList.CheckLang("role_name"));

		
		this.setTitle(I18nList.CheckLang("Create a role"));

		
	    Object[] array = {nom};
	       

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
				source.createRole(nom.getText());
			}		
	        setVisible(false);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
