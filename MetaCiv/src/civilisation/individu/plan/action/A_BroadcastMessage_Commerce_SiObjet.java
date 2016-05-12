package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.Configuration;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;
import madkit.kernel.AgentAddress;
import madkit.message.StringMessage;

public class A_BroadcastMessage_Commerce_SiObjet extends Action{

	Objet objet;
	Integer radius;
	
	public Action effectuer(Human h) {
		AgentAddress ad = null;
		ArrayList<Human> hinr = h.HumaininRadius(radius);
		StringMessage sm = new StringMessage("commerce");
		for(Human target : hinr){
			ad = target.getAgentAddressIn(target.getCommunity(), "membre", "membre");
			if(ad != null && target.getInventaire().possede(objet) > 0)
				h.sendMessage(ad, sm);
			target.setInitiateur(h);
		}
		return nextAction;
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		if (option.getParametres().get(0).getClass() == Integer.class) {
			radius = (Integer) option.getParametres().get(0);
		} else if (option.getParametres().get(0).getClass().equals(Objet.class)){
			objet = (Objet) option.getParametres().get(0);
		}

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] n = {"**Integer**" , "Radius", "0" , "100" , "1"};
			String[] attr = {"**Objet**" , "objetToCompare"};

			schemaParametres.add(attr);
			schemaParametres.add(n);

		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Ask for a tarde in the specified radius.<html>";
	}

	public boolean isDeprecated()
	{
		return false;
	}
}
