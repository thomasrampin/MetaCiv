package civilisation.individu.plan.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import civilisation.ItemPheromone;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.Human;
import civilisation.individu.cognitons.Cogniton;
import civilisation.individu.cognitons.TypeCogniton;
import civilisation.individu.cognitons.TypeDeCogniton;
import turtlekit.kernel.Turtle;

public class A_CreateElectedGroup extends Action{

	GroupModel group;
	String roleLeader;
	String roleFollower;
	TypeCogniton cogniton;
	Integer radius;

	@Override
	public Action effectuer(Human h) {
		
		
		int nbVote = 0;
		
		List<Turtle> turtles = h.getOtherTurtles(radius.intValue(), true);
		
		LinkedList<Human> humans = new LinkedList<Human>();
		for (Turtle t : turtles) {
			if (t instanceof Human && (t.getColor().equals(h.getColor()))) {
				humans.add((Human) t);
			}
		}
		for ( Human human : humans){
			int voteBonus = 0;
			int cognitonBonus = 0;
			if (h.getCommunaute().getIndex() == human.getCommunaute().getIndex()){
				List<Cogniton> cognitonHumains = h.getEsprit().getCognitons();
				Cogniton highestSkillCogniton = new Cogniton(cogniton);
					for (Cogniton cog : cognitonHumains){
						if (cog.getCogniton().getType().equals(TypeDeCogniton.SKILL)){
							if(!highestSkillCogniton.getCogniton().getType().equals(TypeDeCogniton.SKILL)){
								highestSkillCogniton = cog;
							}
							if( cog.getWeigth() > highestSkillCogniton.getWeigth()){
								highestSkillCogniton = cog;
							}
						}
						if (cog.getCogniton().equals(cogniton)){
							cognitonBonus = (int) cog.getWeigth();
						}
					}
				if (h.getEsprit().getCognitonOfType(highestSkillCogniton.getCogniton()).getWeigth() > highestSkillCogniton.getWeigth()){
					voteBonus++;
				}
				//System.out.println(" cogniton current : " + cognitonBonus);

				//System.out.println(" cogniton bonus : " + highestSkillCogniton.getWeigth());
				int randomNumber = (int) (Math.random() * 10);
				if(randomNumber < (voteBonus + cognitonBonus)){
					nbVote++;
				}
					
			}
		}
		//System.out.println("nb of votes : " + nbVote);
		//System.out.println("size : " + humans.size());
		if ( nbVote >= (int)(humans.size()/2)){
			Group g = new Group(null, group, h.getCiv());
			h.launchAgent(g);
			//System.out.println("vote success!!");
			//h.getEsprit().joinRestrictiveGroup(g, roleLeader);
			g.joinGroup(h.getEsprit(), roleLeader);
			for ( Human human : humans){
				g.joinGroup(human.getEsprit(), roleFollower);
				//human.getEsprit().joinRestrictiveGroup(g, roleFollower);
			}
		}
		
		
		return nextAction;
	}

	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass() == GroupAndRole.class && roleLeader == null) {
			group = ((GroupAndRole) option.getParametres().get(0)).getGroupModel();
			roleLeader = ((GroupAndRole) option.getParametres().get(0)).getRole();

		}else if (option.getParametres().get(0).getClass() == GroupAndRole.class && roleFollower == null){
				group = ((GroupAndRole) option.getParametres().get(0)).getGroupModel();
				roleFollower = ((GroupAndRole) option.getParametres().get(0)).getRole();
		} else if (option.getParametres().get(0).getClass().equals(TypeCogniton.class)){
				cogniton = (TypeCogniton) option.getParametres().get(0);
		}else if (option.getParametres().get(0).getClass() == Integer.class) {
				radius = (Integer) option.getParametres().get(0);
			}

		

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] group = {"**GroupAndRole**" , "GroupToCreate"};
			String[] role = {"**GroupAndRole**" , "RoleOfVoter"};
			String[] cog = {"**Cogniton**" , "Cogniton"};
			String[] n = {"**Integer**" , "n", "0" , "200" , "200"};
			
			
			schemaParametres.add(group);
			schemaParametres.add(role);
			schemaParametres.add(cog);
			schemaParametres.add(n);
			
		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Create a new group if agent is elected to role, voters join group as set role. Agent is elected based on a Cogniton weight<html>";
	}


	
}
