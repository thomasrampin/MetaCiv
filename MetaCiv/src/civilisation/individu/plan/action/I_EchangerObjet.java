package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;
import madkit.message.StringMessage;

public class I_EchangerObjet extends IAction{
	String attributeName;
	Objet itemToGive;
	Objet itemToTake;
	int nItemToGive;
	int nItemToTake;
	

	@Override
	public Action effectuer(Human h){
		if(verifParticipant(h) && participant1.isHere(participant2)){
			StringMessage message = new StringMessage("Echange");
			h.sendMessage(participant2.getAgentAddressIn(participant2.getCommunity(), "membre", "membre"), message);
			participant2.setInitiateur(participant1);
			
			participant1.getInventaire().addObjets(itemToTake, nItemToTake); 
			participant1.getInventaire().deleteObjets(itemToGive, nItemToGive);
			
			participant2.getInventaire().deleteObjets(itemToTake, nItemToTake); 
			participant2.getInventaire().addObjets(itemToGive, nItemToGive); 
			
		}else{
			if(participant2!=null){
				participant1.setHeadingTowards(participant2);
				StringMessage message = new StringMessage("Echange");
				h.sendMessage(participant2.getAgentAddressIn(participant2.getCommunity(), "membre", "membre"), message);
				participant2.setInitiateur(participant1);
				participant1.fd(1);
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
		/*
		Integer turns;
		
		String itemToGive;
		Integer nItemToGive;
		
		String itemToTake;
		Integer nItemToTake;
		
		String myTag;
		String compatibleTag;
		*/
		
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
		return super.getInfo() + "Interaction where the agent deals his damages to his target's selected attribute. <html>";
	}
	
}