package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.ItemPheromone;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class L_CompareRoleMembers extends LAction{

    GroupModel group;
    Comparator comp;
    Double d;
    String role;

    @Override
    public Action effectuer(Human h) {

        if (nextAction != null) h.getEsprit().getActions().push(nextAction);
        Action a;

        int nombrePersonneRole=0;
        Group gr = h.getEsprit().getConcreteGroup(group);

        if(gr != null)
        {
            nombrePersonneRole = gr.getMembersWithRole(role).size();

			/*
			if (!h.getEsprit().getGroups().isEmpty())
			{
				for (Group grp : h.getEsprit().getGroups().keySet())
				{

					for (Human grpH : grp.getMembers())
					{
						if(grpH != null && grpH.getEsprit().getGroups() != null && grpH.getEsprit().getGroups().get(gr)!= null)
						{
							if(grpH.getEsprit().getGroups().get(gr).equals(role))
								nombrePersonneRole++;
						}

					}
				}
			}*/
        }

        if (comp.compare(new Double(nombrePersonneRole),d)){
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
        return super.getInfo() + " Compare the numbers of members for a role.<html>";
    }

    @Override
    public void parametrerOption(OptionsActions option){
        super.parametrerOption(option);

        if (option.getParametres().get(0).getClass() == GroupAndRole.class) {
            group = ((GroupAndRole) option.getParametres().get(0)).getGroupModel();
            role = ((GroupAndRole) option.getParametres().get(0)).getRole();
        } else
        if (option.getParametres().get(0).getClass().equals(Comparator.class)){
            comp = (Comparator) option.getParametres().get(0);
        } else
        if (option.getParametres().get(0).getClass().equals(Double.class)){
            d = (Double) option.getParametres().get(0);
        }
    }

    @Override
    public ArrayList<String[]> getSchemaParametres(){

        if (schemaParametres == null){
            schemaParametres = new ArrayList<String[]>();


            String[] group = {"**GroupAndRole**" , "GroupToCreate"};
            String[] comp  = {"**Comparator**" , "comparator"};
            String[] val   = {"**Double**" , "n", "-100.0" , "100.0" , "1.0", "100"};

            schemaParametres.add(group);
            schemaParametres.add(comp);
            schemaParametres.add(val);

        }
        return schemaParametres;
    }

    public boolean internActionsAreLinked() {
        return false;
    }
}
