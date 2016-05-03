package civilisation.individu.plan.action;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.Amenagement_Efficacite;
import civilisation.individu.Human;

public class L_IsCounterAtZero extends LAction{

	@Override
	public Action effectuer(Human h)
	{
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a = new A_DoNothing().effectuer(h);
	
		for(Amenagement amenagement : h.getFacilitiesHere())
		{
			if(amenagement instanceof Amenagement_Efficacite){
				if(((Amenagement_Efficacite) amenagement).getCompteur() <= 0){
					if(listeActions.size() > 0){
						a = listeActions.get(0).effectuer(h);
					}else{
						a = new A_DoNothing().effectuer(h);
					}
				}else{
					if(listeActions.size() > 1){
						a = listeActions.get(1).effectuer(h);
					}else{
						a = new A_DoNothing().effectuer(h);
					}
					
				}
			}
		}
		return a;
	}
	
	@Override
	public ImageIcon getIcon(){
		return Configuration.getIcon("processor.png");
	}
	
	@Override
	public int getNumberActionSlot(){
		return 2;
	}
	
	@Override
	public String getInfo()
	{
		return super.getInfo() + "Do the first action if the facility on this patch have his counter at 0, the second otherwise.<html>";
	}

}