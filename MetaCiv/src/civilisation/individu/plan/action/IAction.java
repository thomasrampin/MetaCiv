package civilisation.individu.plan.action;
import civilisation.individu.*;

public class IAction extends Action{
		Human participant1;
		Human participant2;
		
		public String getInfo() {
			return super.getInfo() + "Type of action where two agents interact. <html>";
		}
		
		public void setAttributes(Human h){
			participant1 = h;
			participant2 = h.getCibleInteraction();
		}
		
		public Boolean verifParticipant(Human h){
			this.setAttributes(h);
			return (participant1 != null && participant2 != null && participant1 != participant2);
		}
		
		@Override
		public Action effectuer(Human h){
			if(verifParticipant(h)){
				
			}
			return nextAction;
		}
	
}
