package civilisation.individu.cognitons;

import civilisation.Civilisation;
import civilisation.Configuration;
import civilisation.SchemaCognitif;

public class TypeCulturon extends TypeCogniton{

	/** 
	 * Culturons are special cogniton associated to groups instead of humans
	 * @author DTEAM
	 * @version 1.0 - 2/2013
	*/
	
	@Override
	public void creerCognitonLambda(SchemaCognitif c){
		nom = "newCulturon_" + c.getCloudCognitons().size(); /*TODO : faire un nom unique*/
		description = "A new cloud cogniton";
		typeDeCogniton = TypeDeCogniton.CULTURON;
	}
}
