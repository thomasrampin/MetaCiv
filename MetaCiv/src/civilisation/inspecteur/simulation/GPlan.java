package civilisation.inspecteur.simulation;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import civilisation.individu.cognitons.Cogniton;
import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.NPlanPondere;
import civilisation.inspecteur.animations.JJComponent;
import civilisation.inspecteur.animations.JJPanel;
import civilisation.inspecteur.simulation.editeurPlan.AEditorWindow;


public class GPlan extends GItemCognitif{

	NPlan typePlan;
	static float margeEcriture = 2;
	
	/*For individual agent view*/
	NPlanPondere concretePlan = null;
	
	private Boolean DisplayCustomColor;
	private Color CustomColor;
	
	
	public GPlan(JJPanel parent , double xx, double yy, double w, double h, NPlan plan) {
		super(parent, xx, yy, w, h);
		this.typePlan = plan;
		this.setToolTipText(plan.toString());
		this.addMouseListener(new MouseGPlanListener(this));
		this.DisplayCustomColor = false;
		this.CustomColor = plan.getCustomColor();

	}

	
	@Override
	public void paintComponent(Graphics g) 
    {    
        Graphics2D g2d = genererContexte(g);
        Color backgroundColor = Color.GRAY;
    	g2d.setColor(Color.BLACK);
    	int weightedBorder = 0;
    	String displayedString = typePlan.getNom();
    	
    	if (concretePlan != null) {
    		displayedString += " [" + concretePlan.getPoids() + "]";
    		if (concretePlan.getH().getEsprit().getPlanEnCours() == concretePlan) {
        		g2d.setColor(new Color (135, 38, 87));
    		}
    		backgroundColor = new Color(Math.max(1.0f - Math.max(concretePlan.getPoids(),0) * 0.1f, 0), Math.max(1.0f - Math.max(concretePlan.getPoids(),0) * 0.1f, 0), 1.0f);
    	}

    	FontMetrics fm = g2d.getFontMetrics();
    	this.setBounds(   (int)(this.getXx()+margeEcriture),(int)this.getYy()+2,(int) (fm.stringWidth(displayedString) + (2*margeEcriture)),2*fm.getHeight());

    	g2d.fill(new Rectangle2D.Double(0,0,fm.stringWidth(displayedString) + (2*margeEcriture),2*fm.getHeight()));
    	this.setW(fm.stringWidth(displayedString) + (2*margeEcriture));
    	this.setH(2*fm.getHeight());
    	if(DisplayCustomColor){
    		g2d.setColor(CustomColor);
    	}
    	else
    	{
    		g2d.setColor(backgroundColor);
    	}
    	g2d.fill(new Rectangle2D.Double(margeEcriture,2,fm.stringWidth(displayedString),2*fm.getHeight()-4));
    	
    	if (typePlan.getIsSelfPlan()) {
        	g2d.setColor(Color.BLUE);
    	} else if (typePlan.getIsBirthPlan()) {
        	g2d.setColor(Color.RED);
    	} else {
        	g2d.setColor(Color.BLACK);
    	}
    	g2d.drawString(displayedString, margeEcriture, (float) (fm.getHeight()*1.3));

    	
    	//System.out.println("dessin du composant");
    	//validate();
    }

	public void selectionnerPlan(){
		((PanelStructureCognitive) this.getParent()).selectionnerPlan(typePlan);
	}
	
	public void editerPlanAvecEditeur() {
		AEditorWindow editeur = new AEditorWindow(typePlan.getNom(), typePlan);
	}


	public NPlan getPlan() {
		return typePlan;
	}

	public void afficherPopup(MouseEvent e){
		((PanelStructureCognitive) this.getParent()).afficherPopupPlan(e , this);
	}


	public NPlanPondere getConcretePlan() {
		return concretePlan;
	}

	public void setConcretePlan(NPlanPondere concretePlan) {
		this.concretePlan = concretePlan;
	}


	public void setCouleur(Color c)
	{
		this.CustomColor = c;
		typePlan.setCustomColor(c);
	}
	
	public void displayCustomColor()
	{
		this.DisplayCustomColor = true;
	}
	
	public void displayTypeColor()
	{
		this.DisplayCustomColor = false;
	}
	
}