/*
 * TurtleKit - An Artificial Life Simulation Platform
 * Copyright (C) 2000-2010 Fabien Michel, Gregory Beurier
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package civilisation;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import civilisation.group.Group;
import civilisation.group.GroupModel;
import civilisation.individu.Human;
import civilisation.world.World;
import civilisation.amenagement.*;
import turtlekit.kernel.Turtle;

/** 
 * Une communaut___, point de rassemblement d'un peuple
 * @author DTEAM
 * @version 1.0 - 2/2013
*/



@SuppressWarnings("serial")
public class Communaute extends Turtle implements Serializable
{

	int visionRadius;
	Civilisation civ;
	HashMap<String,ArrayList<Amenagement>> batiments;
	static int nombreCommunaute = 0;
	int index;
	
	/*Pour des tests*/
	Communaute ennemie;
	

	public Communaute()
	{
		super("start");
		//Pour tester :
		batiments = new HashMap<String,ArrayList<Amenagement>>();
		index = nombreCommunaute;
		nombreCommunaute++;
		this.playRole("communaute");
	}

	public Communaute(Civilisation civ)
	{
		super("start2");
		//Pour tester :
		batiments = new HashMap<String,ArrayList<Amenagement>>();
		index = nombreCommunaute;
		nombreCommunaute++;
		this.civ = civ;
		
	}
	
	
	
	@Override
	public void setup()
	{
		int posX = this.xcor();
		int posY = this.ycor();

		super.setup();
		setColor(Color.orange);
		playRole("Communaute");
		
	//	System.out.println(xcor() + " " + ycor() + " " + posX + " " + posY);
		this.moveTo(posX, posY);


	} 

	/**
	 * Action unique appell___e seulement use seule fois pour initialiser certaines informations
	 * @return Action suivante de la communaute
	 */
	public String start()
	{
		
		civ = new Civilisation();

		for (int i = 0; i < 15; i++)
		{
		//	Turtle h = new Humain(civ,this);
		//	createTurtle(h);
		}
		
		//setColor(civ.getCouleur());
		
		return "neRienFaire";
	}
	
	/**
	 * Action unique appell___e seulement use seule fois pour initialiser certaines informations.
	 * Permet de creer les agents li___s ___ cette communaut___
	 * @return Action suivante de la communaute
	 */
	public String start2()
	{
		System.out.println(civ);
		System.out.println("AGENTS : " + civ.getAgentsInitiaux());
		//init the autojoin groups
		for(GroupModel g : civ.cerveau.groups)
		{
			if(g.isAutoJoin())
			{
				Group gr = new Group(null,g,civ);
				this.launchAgent(gr);
				civ.autoGroups.add(gr);
			}
		}
		
		for (int i = 0; i < civ.getAgentsInitiaux(); i++)
		{
			Human h = new Human(civ,this);
			this.launchAgent(h);
			do {
				if(Configuration.dispersion)
				{
					int temp = civ.getAgentsInitiaux();
					int k = 0;
					int multi = 0;
					while((8*multi)+1 < temp)
					{
						k++;
						multi+= k;
						
					}
					h.moveTo(this.getX() + ((Math.random()*2*k) - k),
							this.getY() + ((Math.random()*2*k) - k));
				}
				else
				{
					h.moveTo(this.getX() + (Math.random()*2*this.civ.scatteredModifier),
								this.getY() + (Math.random()*2*this.civ.scatteredModifier));
						
				}
				
			} while (Configuration.couleurs_terrains.get(h.getPatchColor()).getInfranchissable() && h.getPatch().countTurtles() > 0);
		}
		return "neRienFaire";
	}
	
	public String neRienFaire()
	{
		return "neRienFaire";
	}

	public void construire(Amenagement b)
	{
		if(this.batiments.containsKey(b.getType().getNom()))
		{
			this.batiments.get(b.getType().getNom()).add(b);
		}
		else
		{
			ArrayList<Amenagement> init = new ArrayList<Amenagement>();
			init.add(b);
			//TODO check dependency
			this.batiments.put(b.getType().getNom(), init);
		}
	}
	
	
	
	//----------------GETTERS/SETTERS------------------
		public HashMap<String, ArrayList<Amenagement>> getBatiments() {
		return batiments;
	}
	
		public int getIndex(){
			return index;
		}

		public boolean possedeHutte(Human h) {

			if(this.batiments.containsKey("Hutte"))
			{
				for(int i = 0; i < this.batiments.get("Hutte").size();++i)
				{
					if(this.batiments.get("Hutte").get(i).getPossesseur().equals(h))
					{
						return true;
					}
				}
			}
			return false;
		}

		public Civilisation getCiv() {
			return civ;
		}

		public void detruire(Amenagement a,Human h) {
			// TODO Auto-generated method stub
			if(this.batiments.containsKey(a.getType().getNom()))
			{
				int i = 0;
				while(i < this.batiments.get(a.getType().getNom()).size() && !this.batiments.get(a.getType().getNom()).get(i).getPossesseur().equals(h))
				{
					++i;
				}
				if(i < this.batiments.get(a.getType().getNom()).size())
				{
					for(int k = 0; k < a.getType().getEffets().size();++k)
					{
						if(!a.getType().getEffets().get(k).isPermanent())
						{
							
							a.getType().getEffets().get(k).Desactiver(h);
						}
					}
				}
				if( this.batiments.get(a.getType().getNom()).size() > 0){
					ArrayList<Amenagement> amenagements = this.batiments.get(a.getType().getNom());
					Iterator<Amenagement> iterAmenagement = amenagements.iterator();
					
					while (iterAmenagement.hasNext()) {
						Amenagement amenagement = (Amenagement) iterAmenagement.next();
						if (amenagement.getPosition().equals(a.getPosition()) && amenagement.getType().equals(a.getType())){
							iterAmenagement.remove();
						}
					}
				}
			}

		}
	
		
		
}










