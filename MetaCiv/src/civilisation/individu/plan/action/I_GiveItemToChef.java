package civilisation.individu.plan.action;

import java.util.ArrayList;
import java.util.List;

import civilisation.Configuration;
import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;
import madkit.kernel.Message;
import madkit.message.StringMessage;

public class I_GiveItemToChef extends IAction{
	String itemToGive;
	Integer variationToGive;
	MCIntegerParameter valToGive;
	

	@Override
	public Action effectuer(Human h){
		boolean foundAmessage = false;
		List<Message> ms= h.nextMessages(null);
		StringMessage rep = null;
		h.setCibleInteraction(h.chef);
		
		if(verifParticipant(h) && participant1.isHere(participant2)){
			if(participant1.getInventaire().possede(Configuration.getObjetByName(itemToGive)) > valToGive.getValue()){
				
				participant1.getInventaire().deleteObjets(Configuration.getObjetByName(itemToGive),valToGive.getValue());
				
				participant2.getInventaire().addObjets(Configuration.getObjetByName(itemToGive),valToGive.getValue());
				
				h.setCibleInteraction(null);
				participant2.setInitiateur(null);
				h.purgeMailbox();
			
			}
		}else{
			if(participant2!=null){
				participant1.setHeadingTowards(participant2);
				participant1.fd(1);
			}
			}
		return nextAction;
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == Integer.class) {
			if (option.getName().equals("nObjectToGive")) {
				variationToGive = (Integer) option.getParametres().get(0);
				valToGive= loadIntegerParam(option);
			} 
		}
		else if (option.getParametres().get(0).getClass().equals(Objet.class)) {
			if (option.getName().equals("objectToGive")) {
				itemToGive = ((Objet) option.getParametres().get(0)).getNom();
			}
		}

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){

		if (schemaParametres == null){
		schemaParametres = new ArrayList<String[]>();
		
		
		String[] itemToGive = {"**Objet**" , "objectToGive"};
		String[] nItemToGive = {"**Integer**" , "nObjectToGive", "0" , "100" , "1"};


		schemaParametres.add(itemToGive);
		schemaParametres.add(nItemToGive);



		}
		return schemaParametres;	
	}
	
	
	public String getInfo() {
		return super.getInfo() + "Interaction where the agent trade items to the person who answered his offer. <html>";
	}
	
}