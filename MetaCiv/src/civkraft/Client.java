package civkraft;
import static turtlekit.kernel.TurtleKit.Option.viewers;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;

import turtlekit.kernel.TKLauncher;
import turtlekit.kernel.TKScheduler;
import turtlekit.kernel.TurtleKit;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.SchemaCognitif;
import civilisation.inspecteur.viewer.ViewerTabbed;
import civilisation.world.World;
import civilisation.world.WorldViewer;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Madkit;
import madkit.kernel.Message;
import madkit.kernel.Probe;
import madkit.message.ObjectMessage;
import madkit.simulation.probe.SingleAgentProbe;

public class Client extends TKLauncher{
	
	
	private InterfaceClient iC;
	private WorldViewerClient wVC;
		
	protected void activate() {
		setLogLevel(Level.OFF);
		createGroupIfAbsent(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU,true);			
		requestRole(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU,DefineConstants.ROLE_CLIENT);
		pause(500);
		wVC = null;
		super.activate();
	}
	
	protected void live() 
	 {           		
		iC = new InterfaceClient(null,512,256, this);
		iC.setVisible(true);
		 List<AgentAddress> others = null;
		 while(true)
		 {
			 List<AgentAddress> othersBis = getAgentsWithRole(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU,DefineConstants.ROLE_SERVEUR);
			 if(!sames(others,othersBis)){
				
				 others = othersBis;
				 iC.actualiser(othersBis);
			 }
			 
			 //Reception World
			 if(!this.isMessageBoxEmpty()){			
 
				// System.out.println("Boite non vide");

				 ObjectMessage<Transfert> om = (ObjectMessage<Transfert>) this.nextMessage();
				this.purgeMailbox();
				 if(om != null){
					 Transfert t = om.getContent();
					 System.out.println("J'ai reçu " + om.getContent());
					 String choix = t.getChoix();
					 if(choix.equals(DefineConstants.LANCER_VIEWER) && wVC == null){
						 System.out.println("lancement wvc");

						 wVC = new WorldViewerClient(t.getLargeurSimu(), t.getHauteurSimu(), t.getGrille());
						 this.launchAgent(wVC);
					 }else if(choix.equals(DefineConstants.ENVOI_CONTINU)){
						 wVC.setMatrice(t.getGrille());
						// wVC.render(wVC.getFrame().getGraphics());
					 }
					
					// this.launchAgent(om.getContent());
					// System.out.println(om.getContent() + " dans le client");
					 //System.out.println("Test client");
				 }
			 }
			 
			 
			 
			 //Config
			 /*
			 ObjectMessage<Transfert> recept = (ObjectMessage<Transfert>) this.nextMessage();// new ObjectMessage<Transfert>(new Transfert());
			 if(recept != null){
				 Transfert t = recept.getContent();
				
				 System.out.println("J'ai reï¿½u : " + recept.toString() + " qui correspond ï¿½ " + t);
				 System.out.println("Ma config prï¿½dï¿½dente ï¿½tait : " + Configuration.pathToRessources);
				 System.out.println(WorldViewer.getInstance());
				 
				 t.receptionConfig();					 
				 t.receptionWorldViewer();
				 t.receptionWorld();
				 System.out.println("Maintenant c'est : " + Configuration.pathToRessources);
				 
			 }
			 */
			 
			 
			
			 
			 pause(1000);
		 }   
	 }
	
	public boolean sames(List<AgentAddress> un, List<AgentAddress> deux){
		if(un == null && deux == null){
			return true;
		}
		if((un == null && deux != null) || (un != null && deux == null)){
			return false;
		}
		if(un.size() == deux.size()){
			boolean rechercheActuelle =false;
			for(AgentAddress ad : un){
				for(AgentAddress adDeux : deux){
					if(ad.equals(adDeux)){
						rechercheActuelle = true;
					}
				}
				if(!rechercheActuelle){
					return false;
				}
			}
			return true;
		}		
		return false;
	}
	
	
	@Override
	protected void createSimulationInstance() {
		
		printStartMessage();
		
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); //TODO : this correct a strange behaviour of Swing. Must be improved.

		initProperties();
		
		super.createSimulationInstance();

		this.launchAgent(new ViewerTabbed());
		
		WorldViewer.getInstance().setARole(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU_VIEWERS,DefineConstants.ROLE_CLIENT_VIEWERS);
		
		//Test que tout est bien chargÃ©
		//Configuration.afficherContenuCivkraft();

		}
	
	public void printStartMessage() {
		//Show version
		System.out.println("\n\t---------------------------------------" +
		"\n\t               Civkraft" + "\n\t           version: "
				+ Configuration.versionNumber +
				"\n\t      MetaCiv Team (c) 2015-"
				+ Calendar.getInstance().get(Calendar.YEAR) + 
				"\n\t---------------------------------------\n");
	}
}
