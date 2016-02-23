package civkraft;

import static turtlekit.kernel.TurtleKit.Option.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import turtlekit.kernel.TKLauncher;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.SchemaCognitif;
import civilisation.inspecteur.viewer.ViewerTabbed;
import civilisation.world.World;
import civilisation.world.WorldViewer;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.KernelAddress;
import madkit.kernel.Message;
import madkit.kernel.Probe;
import madkit.message.ObjectMessage;

public class Serveur extends TKLauncher implements Serializable{
	
	
	private InterfaceServeur iS;
	private boolean envoiContinuViewer;
	
	protected void activate() {
		setLogLevel(Level.OFF);
		createGroupIfAbsent(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU,true);			
		requestRole(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU,DefineConstants.ROLE_SERVEUR);
		pause(500);	
		super.activate();
	}
	
	 protected void live() 
	 {           
		 //System.out.println("Dans le serveur ma prop est : " + this.getMadkitProperty("viewers"));
		 envoiContinuViewer = false;
		 iS = new InterfaceServeur(null,512,256, this);
		 iS.setVisible(true);
		 List<AgentAddress> others = null;
		 //List<AgentAddress> clientsViewers = null;
		 while(true)
		 {
			 List<AgentAddress> othersBis = getAgentsWithRole(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU,DefineConstants.ROLE_CLIENT);
			 if(!sames(others,othersBis)){
				 others = othersBis;
				 iS.actualiser(othersBis);
				 if(others == null || others.size() == 0){
					 envoiContinuViewer = false;
				 }
			 }			 

			if(envoiContinuViewer){
				Transfert t = new Transfert();
				t.setChoix(DefineConstants.ENVOI_CONTINU);
				t.setHauteurSimu(WorldViewer.getInstance().getDisplayPane().getHeight());
				t.setLargeurSimu(WorldViewer.getInstance().getDisplayPane().getWidth());
				//WorldViewer.getInstance().getDisplayPane().getPreferredSize();
				t.setGrille(WorldViewer.getInstance().getPatchGrid());				
				
				ObjectMessage<Transfert> om = new ObjectMessage<Transfert>(t);
				broadcastMessage(DefineConstants.RESEAU, DefineConstants.GROUPE_RESEAU, DefineConstants.ROLE_CLIENT, om);	
			}
			 
			 
			 
			
			 
			 //TODO sécuriser
			 //SchémaCognitif
			 if(!this.isMessageBoxEmpty()){
				// System.out.println("Boite non vide");
				 ObjectMessage<SchemaCognitif> om = (ObjectMessage<SchemaCognitif>) this.nextMessage();
				 if(om != null){
					 //System.out.println("J'ai reçu " + om.getContent().plans);
					 boolean presentDansConfiguration = false;
					 for(SchemaCognitif sc :  Configuration.SchemasCognitifs){
						 if(sc.getNom().equals(om.getContent().getNom())){
							 presentDansConfiguration = true;
						 }
					 }
					 if(!presentDansConfiguration){
						 //System.out.println("Je l'ajoute donc car je ne l'ai pas");
						 Configuration.SchemasCognitifs.add(om.getContent());
					 }
				 }
			 }
			 
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
			
			WorldViewer.getInstance().setARole(DefineConstants.RESEAU,DefineConstants.GROUPE_RESEAU_VIEWERS,DefineConstants.ROLE_SERVEUR_VIEWERS);
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
	
		
		public void setEnvoiContinuViewer(boolean t){
			envoiContinuViewer = t;
		}

}