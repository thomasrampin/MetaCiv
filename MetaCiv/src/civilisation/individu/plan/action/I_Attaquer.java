package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;
import civilisation.message.StringMessage;

public class I_Attaquer extends IAction{
	String attributeName;

	@Override
	public Action effectuer(Human h){
		if(verifParticipant(h) && participant1.isHere(participant2)){
			participant2.putAttr(attributeName, participant2.getAttr().get(attributeName) - participant1.getDegats());
		}else{
			if(participant2!=null){
				participant1.setHeadingTowards(participant2);
				StringMessage message = new StringMessage("Attaque");
				h.sendMessage(participant2, message);
				participant1.fd(1);
			}
		}
		return nextAction;
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == String.class) {
			attributeName = (String) option.getParametres().get(0);
		}

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
	
	
	public String getInfo() {
		return super.getInfo() + "Interaction where the agent deals his damages to his target's selected attribute. <html>";
	}
	
}
