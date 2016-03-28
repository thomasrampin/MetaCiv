package civilisation.message;

import civilisation.individu.Human;

public class Message {

	private Human sender;
	
	public Message (){
		sender = null;
	}
	
	public Human getSender(){
		return sender;
	}
	public void setSender(Human sender){
		this.sender = sender;
	}
}
