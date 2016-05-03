package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.Configuration;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;
import madkit.message.StringMessage;

public class I_Commercer_Repondu extends IAction{
	Objet itemToGive;
	Integer nItemToGive;
	
	Objet itemToTake;
	Integer nItemToTake;

	@Override
	public Action effectuer(Human h){

		StringMessage m = (StringMessage) h.nextMessage();
		if(m.getContent().equals("commerceOk")){
			if(verifParticipant(h) && participant1.isHere(participant2)){
					participant1.getInventaire().addObjets(itemToTake,nItemToTake);
					participant1.getInventaire().deleteObjets(itemToGive, nItemToGive);
	
					participant2.getInventaire().addObjets(itemToGive,nItemToGive);
					participant2.getInventaire().deleteObjets(itemToTake, nItemToTake);
					
					participant1.setCibleInteraction(null);
					System.out.println("A trade happened !");
			}else{
				if(participant2!=null){
					participant1.setHeadingTowards(participant2);
					participant1.fd(1);
				}
			}
		}
		return nextAction;
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == Integer.class) {
			if (option.getName().equals("nItemToGive")) {
				nItemToGive = (Integer) option.getParametres().get(0);
			} else if (option.getName().equals("nItemToTake")) {
				nItemToTake = (Integer) option.getParametres().get(0);
			}
		}
		else if (option.getParametres().get(0).getClass().equals(Objet.class)) {
			if (option.getName().equals("itemToGive")) {
				itemToGive = (Objet) option.getParametres().get(0);
			} else if (option.getName().equals("itemToTake")) {
				itemToTake = (Objet) option.getParametres().get(0);
			}
		}

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){

		if (schemaParametres == null){
		schemaParametres = new ArrayList<String[]>();
		
		
		String[] itemToGive = {"**Objet**" , "objectToGive"};
		String[] nItemToGive = {"**Integer**" , "nObjectToGive", "0" , "100" , "1"};

		String[] itemToTake = {"**Objet**" , "objectToTake"};
		String[] nItemToTake = {"**Integer**" , "nObjectToTake", "0" , "100" , "1"};

		schemaParametres.add(itemToGive);
		schemaParametres.add(nItemToGive);
		schemaParametres.add(itemToTake);
		schemaParametres.add(nItemToTake);



		}
		return schemaParametres;	
	}
	
	
	public String getInfo() {
		return super.getInfo() + "Interaction where the agent trade items to the person who answered his offer. <html>";
	}
	
}