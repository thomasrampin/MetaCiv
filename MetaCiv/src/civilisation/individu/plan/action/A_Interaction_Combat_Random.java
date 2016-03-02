package civilisation.individu.plan.action;

import civilisation.individu.*;
import java.util.Random;

public class A_Interaction_Combat_Random extends A_Interaction {

	@Override
	public Action effectuer(Human h){
		if(verifParticipant(h) && participant1.isHere(participant2)){
			/*Random generator = new Random();
			int nombreTest = generator.nextInt(10);
			if(nombreTest % 2 == 0){
				participant1.die();
			}else{*/
				participant2.die();
			//}
		}else{
			if(participant2!=null){
				participant1.setHeadingTowards(participant2);
				participant1.fd(1);
			}
		}
		return nextAction;
	}
	
	public String getInfo() {
		return super.getInfo() + "Type of action where two agents fights. <html>";
	}
	
}
