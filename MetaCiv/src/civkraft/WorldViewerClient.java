package civkraft;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.util.logging.Level;

import turtlekit.kernel.Patch;
import turtlekit.kernel.Turtle;
import turtlekit.viewer.AbstractGridViewer;
import madkit.kernel.AbstractAgent;
import madkit.kernel.AgentAddress;
import madkit.simulation.probe.PropertyProbe;
import madkit.simulation.viewer.SwingViewer;
import civilisation.DefineConstants;

public class WorldViewerClient extends SwingViewer {

	private Patch[] matrice;
	private int largeur;
	private int hauteur;
	private Dimension d;
	private List<Turtle> listeT = null;
	
	
	public WorldViewerClient(int l, int h, Patch[] p){
		largeur = l;
		hauteur = h;
		matrice = p;
		d = new Dimension(largeur, hauteur);
		//createGUIOnStartUp();
		// la drogue est dure...
	}
	
	@Override
	protected void activate() {
		setLogLevel(Level.OFF);
		createGroupIfAbsent(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU_VIEWERS,true);			
		requestRole(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU_VIEWERS,DefineConstants.ROLE_CLIENT_VIEWERS);

		//super.activate();
		
		getDisplayPane().setPreferredSize(d);
		getDisplayPane().setBackground(Color.black);
		getFrame().pack();
		getFrame().setTitle("Client Civkraft");
		setSynchronousPainting(true);
		//getFrame().setVisible(true);	
	}
	
	protected void live() 
	 {           
		//AgentAddress test = this.getDistantAgentWithRole(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU_VIEWERS,DefineConstants.ROLE_SERVEUR_VIEWERS, serveur.getKernelAddress());
		//test.getClass();
	 }
	
	public void setMatrice(Patch[] m){
		matrice = m;
	}
	
	@Override
	protected void render(Graphics g) {
		// TODO Auto-generated method stub	
		// Les patchs
		if(matrice != null){
			System.out.println("Patchs : " + matrice.length);
			int maxifail = matrice[matrice.length-1].y;
			for(int i=0; i<matrice.length; i++){
				listeT = matrice[i].getTurtles();
				if(listeT.size() != 0){
					System.out.println(listeT.size());				
					for(int j=0; j<listeT.size(); j++){
						//if(listeT.get(j).isAlive()){
							g.setColor(listeT.get(j).getColor());
							g.fillRect((matrice[i].x)*3, (maxifail -(matrice[i].y))*3, 3, 3);
							//System.out.println("On vient de paint un turtle de couleur : " + g.getColor().toString());
						//}	
					}
				}
				else{
					g.setColor(matrice[i].getColor());
					g.fillRect((matrice[i].x)*3, (maxifail -(matrice[i].y))*3, 3, 3);
				}
			}
		}
	}
	
	

}
