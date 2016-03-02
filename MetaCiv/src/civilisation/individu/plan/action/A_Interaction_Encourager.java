package civilisation.individu.plan.action;

import java.util.ArrayList;
import java.util.Random;

import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;

public class A_Interaction_Encourager extends A_Interaction {

	String attributeName;
	Integer variation;
	MCIntegerParameter val;
	
	@Override
	public Action effectuer(Human h){
		if(verifParticipant(h)){
			if(participant1.isHere(participant2)){
				participant2.putAttr(attributeName, participant2.getAttr().get(attributeName) + val.getValue() );
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
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] attrName = {"**Attribute**" , "Modified attribute"};
			String[] n = {"**Integer**" , "n", "-100" , "100" , "100"};

			schemaParametres.add(attrName);
			schemaParametres.add(n);

		}
		return schemaParametres;	
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == String.class) {
			attributeName = (String) option.getParametres().get(0);
		} else if (option.getParametres().get(0).getClass() == Integer.class) {
			variation = (Integer) option.getParametres().get(0);
			val = loadIntegerParam(option);
		}

	}
	
	public String getInfo() {
		return super.getInfo() + "An agent increase the moral of his friend. <html>";
	}
}
