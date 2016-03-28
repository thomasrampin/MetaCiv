package civilisation.message;

public class StringMessage extends Message{

	private String contenu;
	
	public StringMessage(String contenu){
		this.contenu = contenu;
	}
	
	public String getContenu(){
		return contenu;
	}
	public void setContenu(String contenu){
		this.contenu = contenu;
	}
}
