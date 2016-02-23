package civilisation;

public class DefinePath {

	// A COMPLETER
	// Penser a supp de Configuration
	public static String pathToRessource = Configuration.pathToRessources;
	
	/* Schemas Cognitifs */

	public static final String schemasCognitifs = "schemasCognitifs";
	public static final String attributes = "attributes";
	public static final String constants = "constants";
	public static final String cognitons = "cognitons";
	public static final String plans = "plans";
	public static final String groups = "groups";
	public static final String cloudCognitons = "cloudCognitons";
	
	public static String pathToSchemas = pathToRessource + "/" + schemasCognitifs;
	public static final String pathToAttributes = "/" + attributes;
	public static final String pathToConstants = "/" + constants;

	public static final String pathToCognitons = "/" + cognitons;
	public static final String pathToPlans = "/" + plans;
	public static final String pathToCloudCognitons = "/" + cloudCognitons;
	public static final String pathToGroups = "/" + groups;
	
	/* Simulation */ 
	public static final String simulation = "simulation";
	public static final String actions = "actions/civilisation/individu/plan/action";
	public static final String amenagements = "amenagements";
	public static final String civilisations = "civilisations";
	public static final String effects = "effects";
	public static final String environnements = "environnements";
	public static final String itemPheromones = "itemPheromones";
	public static final String objets = "objets";
	public static final String terrains = "terrains";
	
	public static String pathToSimulation = pathToRessource + "/" + simulation;
	public static String pathToActions = pathToSimulation + "/" + actions;
	public static String pathToAmenagements = pathToSimulation + "/" + amenagements;
	public static String pathToCivilisations = pathToSimulation + "/" + civilisations;
	public static String pathToEffects = pathToSimulation + "/" + effects;
	public static String pathToEnvironnements = pathToSimulation + "/" + environnements;
	public static String pathToItemPheromones = pathToSimulation + "/" + itemPheromones;
	public static String pathToObjets = pathToSimulation + "/" + objets;
	public static String pathToTerrains = pathToSimulation + "/" + terrains;
	
	/* Advanced stats viewer*/
	public static final String ADVStatsRootDirectory = "ADVStats";
	public static final String ADVStatsWidgetHeaderFile = "Header";
	public static final String ADVStatsWidgetSocialFilterFile = "SocFilter";
	public static final String ADVStatsWidgetObjectFilterFile = "ObjFilter";
	public static final String ADVStatsWidgetAttributeFilterFile = "AttrFilter";
	public static final String ADVStatsWidgetChartTypeFilterFile = "CTypeFilter";
	
	
}
