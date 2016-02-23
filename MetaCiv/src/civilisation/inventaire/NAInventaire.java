package civilisation.inventaire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;

/**
 * Représente un inventaire pour un amménagement
 * 
 * @author Bruno VISSE
 * @author Auteur(s) de NInventaire
 */
public class NAInventaire implements Serializable
{
	HashMap<String,HashMap<Integer, Integer>> listeObjets; //String -> Type d'objet , Int1 -> Qualité, Int2 -> nombre
	Amenagement a;
	int weight;

	public NAInventaire(Amenagement a)
	{
		listeObjets = new HashMap<String, HashMap<Integer, Integer>>();
		this.a = a;
		this.weight = 0;
	}
	
	public int possede(Objet o) // retourne le nombre d'objets possédés d'un même type, quelque soit sa qualité
	{
		if(listeObjets.containsKey(o))
		{
			int retour = 0;
			for(int i : listeObjets.get(o).values())
			{
				retour+= i;
			}
			
			return retour;
			/*
			Set cles = listeObjets.get(o).keySet();
			Iterator it = cles.iterator();
			int i = 0;
			while(it.hasNext())
			{
				i+= listeObjets.get(o).get(it.next());
			}

			return i;
			*/
			//return 1;
		}
		else
		{
			return 0;
		}
	}
	
	public int possede(Objet o, Integer qualite) // retourne le nombre d'objets possédés d'un m�me type, de même qualité
	{
		if(listeObjets.containsKey(o))
		{

			if(listeObjets.get(o).containsKey(qualite))
			{
				
				return listeObjets.get(0).get(qualite);
			}
			else
			{
				return 0;
			}	
		}
		else
		{
			return 0;
		}
	}
	
	public void addObjets(Objet o , int i){ //ajoute i objets 
		if (!listeObjets.containsKey(o.getNom())){
			listeObjets.put(o.getNom(), new HashMap<Integer,Integer>());
			listeObjets.get(o.getNom()).put(o.getQuality(), i);
		}
		else{
			if(o != null && listeObjets != null && listeObjets.containsKey(o.getNom()))
			{
				if(listeObjets.get(o.getNom()) != null)
				{
					if(listeObjets.get(o.getNom()).containsKey(o.getQuality()))
					{
						int nbObjets = listeObjets.get(o.getNom()).get(o.getQuality());
						listeObjets.get(o.getNom()).remove(o.getQuality());
						listeObjets.get(o.getNom()).put(o.getQuality(), nbObjets + i);
					}
					else
					{
						listeObjets.get(o.getNom()).put(o.getQuality(),i);
					}
				}
				else
				{
					listeObjets.get(o.getNom()).put(o.getQuality(),i);
				}
				
			}			
		}
		//TODO : problem if many item??
		for(int k = 0; k < i;k++)
		{
			for(int j = 0; j < o.getEffets().size();++j)
			{
				if(o.getEffets().get(j).getActivation() == 0)
				{
					o.getEffets().get(j).Activer(a);
			//		if (listeObjets.get(o) > 1) {
			//			System.out.println(listeObjets.get(o));
			//		}
				}
			}
		}
		
		
	}
	
	public void useObjets(Objet o,int i) //utilise i objets
	{
		if(this.listeObjets.containsKey(o.getNom()) && this.listeObjets.get(o.getNom()) != null)
		{
			if(this.possede(o) >= i)
			{
			//	System.out.println("test");
				for(int j = 0; j < i;j++)
				{
					for(int k = 0; k < o.getEffets().size();++k)
					{
						if(o.getEffets().get(k).getActivation() == 1)
						{
							o.getEffets().get(k).Activer(a);
						}
					}
					
				}
			}
			else
			{
				for(int j = 0; j < this.possede(o);j++)
				{
					for(int k = 0; k < o.getEffets().size();++k)
					{
						if(o.getEffets().get(k).getActivation() == 1)
						{
							o.getEffets().get(k).Activer(a);
						}
					}
					
				}
			}
			this.deleteObjets(o, i);
		}
	}
	
	public void deleteObjets(Objet o, int i) //supprime i objets
	{
		int val;
		if (listeObjets.containsKey(o.getNom()) && this.possede(o) > 0){
			val = this.possede(o);
			
			if(val - i < 0)
			{
				listeObjets.remove(o.getNom());
				listeObjets.put(o.getNom(), new HashMap<Integer,Integer>());
				for(int j = 0; j < val; ++j)
				{
					for(int k = 0; k < o.getEffets().size();++k)
					{
						if(!o.getEffets().get(k).isPermanent())
						{
							o.getEffets().get(k).Desactiver(a);
						}
					}
				}
			}
			else
			{
				int index = 0;
				Set cles = listeObjets.get(o.getNom()).keySet();
				Iterator it = cles.iterator();
				while(it.hasNext() && index < i)
				{
					Integer a = (Integer) it.next();
					index += listeObjets.get(o.getNom()).get(a);
					if(index <= i)
					{
						listeObjets.get(o.getNom()).remove(a);
					}
					else
					{
						int temp = listeObjets.get(o.getNom()).get(a);
						listeObjets.get(o.getNom()).remove(a);
						listeObjets.get(o.getNom()).put(a, temp - (index - i));
					}
				}
				
				for(int j = 0; j < i; ++j)
				{
					for(int k = 0; k < o.getEffets().size();++k)
					{
						if(!o.getEffets().get(k).isPermanent())
						{
							o.getEffets().get(k).Desactiver(a);
						}
					}
				}
			}
			
		}
	}
	
	public ArrayList<Objet> getAll()
	{
		ArrayList<Objet> retour = new ArrayList<Objet>();
		Set cles = listeObjets.keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
		   String cle = (String) it.next();
		   
		   
		   Set cles2 = listeObjets.get(cle).keySet();
		   Iterator it2 = cles2.iterator();
		   
			while(it2.hasNext())
			{
				Integer cle2 = (Integer) it2.next();
				Integer valeur = listeObjets.get(cle).get(cle2);
				for(int i = 0; i < valeur;++i)
				{
					   retour.add(new Objet(cle,cle2,Configuration.getObjetByName(cle).getEffets()));
				}
			}
		   
		}
		return retour;
	}

	public HashMap<String, HashMap<Integer, Integer>> getListeObjets() {
		return listeObjets;
	}

	public void setListeObjets(HashMap<String, HashMap<Integer, Integer>> listeObjets) {
		this.listeObjets = listeObjets;
	}
}
