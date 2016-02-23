package civilisation.group;

import madkit.kernel.Message;

public class MessageString extends Message{
	public String content;
	
	public MessageString(String msg)
	{
		super();
		content = msg;
	}
}
