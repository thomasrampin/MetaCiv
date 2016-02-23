package civilisation.individu.plan.action;

import java.util.ArrayList;
import java.util.HashMap;

import civilisation.Configuration;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

/**
 *  Retire X objet a l'inventaire de l'agent et change un attribut 
 *  @param Changed object , l'objet a d�truire
 *  @param Integer , le nombre d'objets a d�truire
 *  @param Changed attribute, l'attribut à changer
 *  @param Integer , la variation de l'attributv
 *  @return l'action suivante dans la liste d'actions de l'agent
 */

public class A_DropObjectAndChangeAttribute extends Action{

	Objet objet;
	int variation;
	Double variationattribute;
	String attributeName;

	@Override
	public Action effectuer(Human h) {

		//On enlève des éléments
		if(h.getInventaire().getListeObjets().get(objet.getNom()) != null && h.getInventaire().getListeObjets().get(objet.getNom()).get(1) >= variation)
		{
			int nombreObjet = h.getInventaire().getListeObjets().get(objet.getNom()).get(1);

			//Il faut enlever les objets de l'inventaire de l'agent
			HashMap<String, HashMap<Integer, Integer>> Bag = h.getInventaire().getListeObjets();
			HashMap<Integer, Integer> temp= new HashMap<Integer, Integer>();
			temp.put(1,nombreObjet-variation);
			Bag.put(objet.getNom(), temp);
			h.getInventaire().setListeObjets(Bag);

			//On commence par changer les attributs
			h.putAttr(attributeName, h.getAttr().get(attributeName) + variationattribute );


		}

		return nextAction;
	}

	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == Objet.class) {
			objet = (Objet) option.getParametres().get(0);
		} else if (option.getParametres().get(0).getClass() == Integer.class) {
			variation = (Integer) option.getParametres().get(0);
		}else if (option.getParametres().get(0).getClass() == String.class) {
			attributeName = (String) option.getParametres().get(0);
		} else if (option.getParametres().get(0).getClass() == Double.class) {
			variationattribute = (Double) option.getParametres().get(0);
		}

	}

	/**
	 * Donne des infos sur l'action
	 */
	@Override
	public String getInfo() {
		return super.getInfo() + "Drop X objects and change an attribute by N. If there's less than X objects, the changes in attributes are not done.<html>";
	}

	/**
	 * Retourne la structure des param_tres.
	 * Permet de d_terminer la pr_sentation de la fen_tre de r_glages.
	 */
	@Override
	public ArrayList<String[]> getSchemaParametres(){

		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] attrName = {"**Objet**" , "Dropped object"};
			String[] n = {"**Integer**" , "n", "0" , "100" , "1"};

			String[] attrName1 = {"**Attribute**" , "Modified attribute"};
			String[] n1 = {"**Double**" , "n", "-10.0" , "10.0" , "1","100"};

			schemaParametres.add(attrName);
			schemaParametres.add(n);
			schemaParametres.add(attrName1);
			schemaParametres.add(n1);

		}
		return schemaParametres;	
	}


}
