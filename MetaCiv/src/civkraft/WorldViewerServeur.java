package civkraft;

import java.util.List;
import java.util.logging.Level;

import madkit.kernel.AgentAddress;
import madkit.message.ObjectMessage;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.SchemaCognitif;
import civilisation.world.WorldViewer;
import turtlekit.kernel.TKLauncher;

public class WorldViewerServeur extends TKLauncher {

	private WorldViewer instance;
	private boolean envoi; // Si on est en cours d'envoi
	private List<AgentAddress> clients;
	
	protected void activate() {
		setLogLevel(Level.OFF);
		createGroupIfAbsent(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU_VIEWERS,true);			
		requestRole(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU_VIEWERS,DefineConstants.ROLE_SERVEUR_VIEWERS);
		pause(500);	
		super.activate();
	}
	
	protected void live() 
	 {           
		 List<AgentAddress> others = null;
		 while(true)
		 {
			 List<AgentAddress> othersBis = getAgentsWithRole(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU_VIEWERS,DefineConstants.ROLE_CLIENT_VIEWERS);
			if(othersBis != null && othersBis.size() > 0){
				
			}
				 
				 
				 /*
				 //Envoi config
				 Transfert t = new Transfert();
				 t.envoiConfig();
				 t.envoiWorldViewer();
				 t.envoiWorld();
				 ObjectMessage<Transfert> envoi = new ObjectMessage<Transfert>(t);
				 System.out.println("J'ai envoy� : " + envoi.toString() + " qui correspond � " + t);
				 if(othersBis != null){
					 this.sendMessage(othersBis.get(0), envoi);
				 }
				 this.launchViewers();
				
				 */
				 
				 
						 
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
	
}
