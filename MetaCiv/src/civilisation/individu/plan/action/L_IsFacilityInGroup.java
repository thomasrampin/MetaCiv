package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.amenagement.TypeAmenagement;
import civilisation.group.GroupModel;
import civilisation.individu.Human;

public class L_IsFacilityInGroup extends LAction{

	TypeAmenagement amenagements;
	GroupModel grp;

	
	@Override
	public Action effectuer(Human h) {
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		if (h.getEsprit().getConcreteGroup(grp)!=null) {
			if(h.getEsprit().getConcreteGroup(grp).getFacilitiesOfType(amenagements).size()>0){
				if(listeActions.size() > 0){
					a = listeActions.get(0).effectuer(h);
				}else{
					a = new A_DoNothing().effectuer(h);
				}
			} else {
				if(listeActions.size() > 1){
					a = listeActions.get(1).effectuer(h);
				}else{
					a = new A_DoNothing().effectuer(h);
				}
			}
			
		} else {
			if(listeActions.size() > 1){
				a = listeActions.get(1).effectuer(h);
			}else{
				a = new A_DoNothing().effectuer(h);
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
	public String getInfo() {
		return super.getInfo() + " Play first action if comunity owns an amenagement,<br> the second otherwise.<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass() == TypeAmenagement.class) {
			amenagements = (TypeAmenagement) option.getParametres().get(0);
	//		System.out.println(objet.getNom());
		}else if (option.getParametres().get(0).getClass() == GroupModel.class) {
			grp = (GroupModel) option.getParametres().get(0);
	//		System.out.println(objet.getNom());
		}
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			
			
			String[] cog = {"**Amenagement**" , "Facility"};
			String[] group = {"**Group**" , "Group"};
			
			schemaParametres.add(cog);
			schemaParametres.add(group);


		}
		return schemaParametres;	
	}
	
	public boolean internActionsAreLinked() {
		return false;
	}
}
