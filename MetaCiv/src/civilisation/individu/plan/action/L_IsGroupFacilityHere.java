package civilisation.individu.plan.action;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import turtlekit.kernel.Turtle;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.group.Group;
import civilisation.group.GroupModel;
import civilisation.individu.Human;

public class L_IsGroupFacilityHere extends LAction{
	

		TypeAmenagement objet;
		GroupModel grp;
		
		@Override
		public Action effectuer(Human h)
		{
			if (nextAction != null)
				h.getEsprit().getActions().push(nextAction);
			Action a;
			boolean doAction = false;
			
			for(Turtle t : h.getPatch().getTurtlesWithRole(DefineConstants.Role_Facility))
			{
				if(((Amenagement)t).getType().equals(objet))
				{
						if(((Amenagement)t).getMyGroups(h.getCiv().getNom()).contains(h.getEsprit().getConcreteGroup(grp).getId()))
						{
							doAction = true;break;
						}
				}
			}

			if (doAction)
				a = listeActions.get(0).effectuer(h);
			else
				a = listeActions.get(1).effectuer(h);
			return a;
		}

		@Override
		public ImageIcon getIcon()
		{
			return Configuration.getIcon("processor.png");
		}
		
		@Override
		public int getNumberActionSlot()
		{
			return 2;
		}
		
		@Override
		public String getInfo()
		{
			return super.getInfo() + " Play first action if there is a facility which belongs to the agent's group,<br> the second otherwise.<html>";
		}
		@Override
		public void parametrerOption(OptionsActions option)
		{
			super.parametrerOption(option);
			if (option.getParametres().get(0).getClass() == TypeAmenagement.class)
				objet = (TypeAmenagement) option.getParametres().get(0);
			else if (option.getParametres().get(0).getClass() == GroupModel.class)
				grp = (GroupModel) option.getParametres().get(0);
		}
		
		@Override
		public ArrayList<String[]> getSchemaParametres(){
			
			if (schemaParametres == null)
			{
				schemaParametres = new ArrayList<String[]>();
				String[] attrName = {"**Amenagement**" , "Facility"};
				String[] attrGrp = {"**Group**" , "Group type"};
				
				schemaParametres.add(attrName);
				schemaParametres.add(attrGrp);
			}
			return schemaParametres;	
		}
		
		public boolean internActionsAreLinked()
		{
			return false;
		}
	}
