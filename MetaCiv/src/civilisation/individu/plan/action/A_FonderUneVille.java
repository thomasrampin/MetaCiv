package civilisation.individu.plan.action;

import civilisation.Civilisation;
import civilisation.Communaute;
import civilisation.Configuration;
import civilisation.individu.Human;

public class A_FonderUneVille extends Action{

	@Override
	public Action effectuer(Human h) {
		h.setHeading(Math.random()*360.);
		h.fd(1);
		
		try {
			Communaute com = h.getNearestTurtle(16, Communaute.class);
			if (com == null) {
				Civilisation civ = h.getCiv().createDaugtherCivilization();
				h.setCiv(civ);
				Communaute c = new Communaute(civ);
				
				h.setCommunaute(c);
				h.launchAgent(c);
				c.moveTo(h.getX(), h.getY());
				
				//New city start with two agents for test purpose
				Human h2 = new Human(civ,c);
				h.launchAgent(h2);
				h2.moveTo(h.getX(), h.getY());
				Human h3 = new Human(civ,c);
				h.launchAgent(h3);
				h3.moveTo(h.getX(), h.getY());
				
				return nextAction;
			} else {
				return this;
			
			}
		} catch (Exception e) {
			System.out.println("Concurrent!");
		}
		return this;
			
		
		
	}

	
	
	
}
