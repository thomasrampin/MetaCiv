package civilisation.individu.plan.action;

import java.util.ArrayList;

import java.util.Random;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.Human;
import civilisation.individu.cognitons.TypeCogniton;

public class A_AskRandomMemberToChangeRoleForAnother extends Action
{
	GroupModel group;
	String role;
	
	@Override
	public Action effectuer(Human h)
	{
		Group gr = h.getEsprit().getConcreteGroup(group);
		if (gr != null) {
			//On s'assure que l'agent est au moins dans le groupe.
			
			int tailleGroupe = gr.getMembers().size();
			if(tailleGroupe != 1)
			{
				int random=0;
				Random randomGenerator = new Random();
				//On essaye de trouver un autre membre
				while (gr.getMembers().get(random).getID() == h.getID())
				{
					random=randomGenerator.nextInt(tailleGroupe);
				}
				
				
				Human cible= gr.getMembers().get(random);
				if(! cible.getEsprit().getGroups().get(gr).equals(role))
				{	
					//System.out.println("Quelqu'un est devenu un agriculteur");
					gr.changeRole(cible.getEsprit(), role);
//					cible.getEsprit().joinRestrictiveGroup(gr, role);
				}
			}
			
		}
		
		return nextAction;
	}

	@Override
	public String getInfo()
	{
		return super.getInfo() + " Change the role of a random agent in the group for another (Does not work if there's only one member in the group)<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option)
	{
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass() == GroupAndRole.class) {
			group = ((GroupAndRole) option.getParametres().get(0)).getGroupModel();
			role = ((GroupAndRole) option.getParametres().get(0)).getRole();
		}
		
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres()
	{	
		if (schemaParametres == null)
		{
			schemaParametres = new ArrayList<String[]>();
			
			String[] role1 = {"**GroupAndRole**" , "Change to that role :"};
			schemaParametres.add(role1);
		}
		return schemaParametres;	
	}
}
