package civilisation.individu.plan.action;

import java.util.ArrayList;
import java.util.List;

import civilisation.Configuration;
import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;
import madkit.kernel.Message;
import madkit.message.StringMessage;

public class I_TakeItemsFromChef extends IAction{
	
	String itemToTake;
	Integer variationToTake;
	MCIntegerParameter valToTake;

	@Override
	public Action effectuer(Human h){
		boolean foundAmessage = false;
		List<Message> ms= h.nextMessages(null);
		StringMessage rep = null;
		h.setCibleInteraction(h.chef);
		
		if(verifParticipant(h) && participant1.isHere(participant2)){
			if(participant2.getInventaire().possede(Configuration.getObjetByName(itemToTake)) > valToTake.getValue()){
				
				participant1.getInventaire().addObjets(Configuration.getObjetByName(itemToTake),valToTake.getValue());
				participant2.getInventaire().deleteObjets(Configuration.getObjetByName(itemToTake),valToTake.getValue());
				
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
			if (option.getName().equals("nObjectToTake")) {
				variationToTake = (Integer) option.getParametres().get(0);
				valToTake= loadIntegerParam(option);
			}
		}
		else if (option.getParametres().get(0).getClass().equals(Objet.class)) {
			if (option.getName().equals("objectToTake")) {
				itemToTake = ((Objet) option.getParametres().get(0)).getNom();
			}
		}

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){

		if (schemaParametres == null){
		schemaParametres = new ArrayList<String[]>();
		
		
		String[] itemToTake = {"**Objet**" , "objectToTake"};
		String[] nItemToTake = {"**Integer**" , "nObjectToTake", "0" , "100" , "1"};

		schemaParametres.add(itemToTake);
		schemaParametres.add(nItemToTake);

		}
		return schemaParametres;	
	}
	
	
	public String getInfo() {
		return super.getInfo() + "The agent takes X items from his chef. <html>";
	}
	
}