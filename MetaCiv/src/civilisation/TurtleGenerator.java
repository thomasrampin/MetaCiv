package civilisation;

import java.io.Serializable;

import turtlekit.kernel.Turtle;


/** 
 * Special turtle used to create other turtles
 * @author DTEAM
 * @version 1.0 - 09/2013
*/


public class TurtleGenerator extends Turtle implements Serializable{

	static TurtleGenerator turtleGenerator;
	
	public TurtleGenerator(){
		turtleGenerator = this;
		
	}
	
	public String doNothing(){
		//this.playRole("Generator");
		return "doNothing";
	}
	
	public void activate() {
		this.moveTo(0, 0);	
	}
	
	public static TurtleGenerator getInstance(){
		return turtleGenerator;
	}
	
}
