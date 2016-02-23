package civilisation.inspecteur.simulation.dialogues;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import I18n.I18nList;
import civilisation.amenagement.TypeAmenagement;
import civilisation.world.Terrain;

public class DialogueChoisirCouleurAmenagement extends JDialog implements ActionListener, PropertyChangeListener{
	
	TypeAmenagement amenagement;
    JOptionPane optionPane;
    JColorChooser choixCouleur;

	public DialogueChoisirCouleurAmenagement(Frame f , boolean modal, TypeAmenagement amenagement){
		super(f,modal);
		this.amenagement = amenagement;

		

		choixCouleur = new JColorChooser();

		this.setTitle(I18nList.CheckLang("Choisir la couleur"));

		
	    Object[] array = {choixCouleur};
	    Object[] options = {"OK" , "Cancel"};
	 
	    optionPane = new JOptionPane(array,
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
		if (isVisible() && (optionPane.getValue().equals("OK") || optionPane.getValue().equals("Cancel"))){
			if (optionPane.getValue().equals("OK")){
				amenagement.setColor(choixCouleur.getColor());
			}		
	        setVisible(false);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
