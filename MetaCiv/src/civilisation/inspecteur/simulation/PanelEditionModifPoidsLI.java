package civilisation.inspecteur.simulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.individu.cognitons.LienPlan;
import civilisation.individu.plan.NPlan;

public class PanelEditionModifPoidsLI extends JPanel implements PropertyChangeListener, ChangeListener{

	private JPanel panelCenter;
	
	private PanelModifPoidsLI parent;
	private GCogniton gC;
	private GPlan gP;
	private LienPlan lien;
	
	private JSpinner jspinPas;
	private JSpinner jspinValeur;
	
	private JKnob jknob;
	
	// test
	private JOptionPane optionPane;
	private ArrayList<JSpinner> poids;
	private ArrayList<Object> array;
	
	// Constantes
	public static final Dimension dimensionPanelEditionModifPoidsLI = new Dimension(180,245);
	// to do
	// A mettre je sais pas ou
	public static final int pasJSpinner = 1;
	
	public PanelEditionModifPoidsLI(PanelModifPoidsLI parent, GCogniton gC, GPlan gP, LienPlan lien)
	{

		this.parent = parent;
		this.gC = gC;
		this.gP = gP;
		this.lien = lien;
		
		
		
		
		
		System.out.println(" parent.valeurMax = " +  parent.valeurMax + "\n== ? == " + (int)Configuration.SCEnCoursEdition.weightsBound);
				
		SpinnerNumberModel model = new SpinnerNumberModel(lien.getPoids(), parent.valeurMin, parent.valeurMax, pasJSpinner);
		jspinValeur = new JSpinner(model);
		model = new SpinnerNumberModel(parent.pasModifPoidsLI, pasJSpinner, (int)Configuration.SCEnCoursEdition.weightsBound , pasJSpinner);
		jspinPas = new JSpinner(model);
		System.out.println("out");
		
		this.setLayout(new BorderLayout());
		
		this.setBackground(Color.black);
		this.setSize(dimensionPanelEditionModifPoidsLI);
		
		// Initialise le panel du centre qui contiendra le knob
		panelCenter = new JPanel(new FlowLayout());
		/*panelCenter = new JPanel();
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));*/
		panelCenter.setBackground(Color.black);
		// Ajoute le panel du centre au panel principal
		this.add(panelCenter, BorderLayout.CENTER);
	
		poids = new ArrayList<JSpinner>();
		array = new ArrayList<Object>();
		
	ajouterBox("Valeur : ",jspinValeur);
	ajouterBox("Pas : ", jspinPas);
	
       
    //Create an array specifying the number of dialog buttons
    //and their text.
    Object[] options = {"OK" , "Cancel"};
 
    //Create the JOptionPane.
    optionPane = new JOptionPane(array.toArray(),
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]); 
    this.add(optionPane, BorderLayout.SOUTH);
    optionPane.setBackground(Color.black);
   // this.add(new JScrollPane(optionPane));
        
    optionPane.addPropertyChangeListener(this);
    jspinValeur.addChangeListener(this);
    
	ImageIcon icone = new ImageIcon(System.getProperty("user.dir")+"/civilisation/graphismes/LogoMedium.png");
	optionPane.setIcon(icone);
	
	
	// On ajoute le knob
	jknob = new JKnob(this, lien.getPoids()*100/(int)Configuration.SCEnCoursEdition.weightsBound);
	panelCenter.add(jknob, BorderLayout.CENTER);
	
	this.setFocusable(true);
	this.requestFocus();
}

@Override
public void propertyChange(PropertyChangeEvent e) {

	//System.out.println("evenement : " + e.getSource().getClass().getName());
	//System.out.println(optionPane.getValue());
	if (isVisible() && (optionPane.getValue().equals("OK") || optionPane.getValue().equals("Cancel"))){
		if (optionPane.getValue().equals("OK")){
			//((PanelStructureCognitive) gC.getParent()).modifierLienInfluence(gC, gP, lien.getPoids());
			parent.modifierPoids((int)jspinValeur.getValue());
			parent.pasModifPoidsLI = (int)jspinPas.getValue();
		}		
		//System.out.println(this.parent.getParent().getClass().getName());
		((PanelStructureCognitive)this.parent.getParent()).setAnimeIndicateurDeLiens(true);
        setVisible(false);
	}
	// Si on modifie la valeur
	else if(e.getSource().equals(jspinValeur))
	{
		int valeur = (int)jspinValeur.getValue();
		if(valeur>=0)
			jknob.valeurModifiee((valeur*100/parent.valeurMax));
		else{
			jknob.valeurModifiee((valeur*100/parent.valeurMin*(-1)));
		}
	}
	
}


public void ajouterBox(String texte, JSpinner jspin){
	
	Box b = Box.createHorizontalBox();
	JLabel label = new JLabel(texte);
	label.setBackground(Color.GRAY);
	b.add(label);
	// to do
	// a modifier...
	
	b.add(jspin);
	this.poids.add(jspin);
	array.add(b);
}

// Methode calculant la position du panel, au centre des 2 GItem
public void actualiserPosition()
{
	this.setLocation((int)(gC.getCentreX()+gP.getCentreX())/2-this.getWidth()/2,(int)(gC.getCentreY()+gP.getCentreY())/2-this.getHeight()/2);


	// On regarde si le panel d�passe sur les cot�s, on ajuste en consequence
	if(this.getX()<0) 
		this.setLocation(0, this.getY());
	if(this.getX()>this.getParent().getWidth()-this.getWidth()) 
		this.setLocation(this.getParent().getWidth()-this.getWidth(), this.getY());
	if(this.getY()<0) 
		this.setLocation(this.getX(), 0);
	if(this.getY()>this.getParent().getHeight()-this.getHeight())
		this.setLocation(this.getX(), this.getParent().getHeight()-this.getHeight());
}

public int modifierPoids(int pourcent) {
	// TODO Auto-generated method stub
	float p = (float)pourcent/100;
	int resultat = 0;
	//System.out.println("pourcent : " + pourcent + "\np = "+p);
	if(pourcent>=0){
		jspinValeur.setValue(Math.round((parent.valeurMax*p)));
		resultat = (int)jspinValeur.getValue()*100/parent.valeurMax;
	}
	else{
		jspinValeur.setValue(Math.round(parent.valeurMin*p*(-1)));
		resultat = (int)jspinValeur.getValue()*100/parent.valeurMin*(-1);
	}
	
	return resultat;
}

@Override
public void stateChanged(ChangeEvent event) {
	// TODO Auto-generated method stub
	if(event.getSource().equals(jspinValeur))
	{
		int valeur = (int)jspinValeur.getValue();
		if(valeur>=0)
			jknob.valeurModifiee((int)jspinValeur.getValue()*100/parent.valeurMax);
		else
			jknob.valeurModifiee((int)jspinValeur.getValue()*100/parent.valeurMin*(-1));
	}
}

}
