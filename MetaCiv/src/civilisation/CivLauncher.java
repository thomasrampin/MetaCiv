package civilisation;

import java.io.File;

import static turtlekit.kernel.TurtleKit.Option.cuda;
import static turtlekit.kernel.TurtleKit.Option.envDimension;
import static turtlekit.kernel.TurtleKit.Option.envHeight;
import turtlekit.kernel.TurtleKit.Option;
import static turtlekit.kernel.TurtleKit.Option.endTime;
import static turtlekit.kernel.TurtleKit.Option.envDimension;
import static turtlekit.kernel.TurtleKit.Option.environment;
import static turtlekit.kernel.TurtleKit.Option.scheduler;
import static turtlekit.kernel.TurtleKit.Option.startSimu;
import static turtlekit.kernel.TurtleKit.Option.turtles;
import static turtlekit.kernel.TurtleKit.Option.viewers;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import civilisation.group.Group;
import civilisation.inspecteur.viewer.ViewerAgent;
import civilisation.inspecteur.viewer.ViewerInspecteur;
import civilisation.inspecteur.viewer.ViewerModificationSimulation;
import civilisation.inspecteur.viewer.ViewerOptions;
import civilisation.inspecteur.viewer.ViewerPerformances;
import I18n.I18nList;
import civkraft.Client;
import civkraft.InterfaceClient;
import civkraft.InterfaceLauncher;
import civkraft.InterfaceServeur;
import civkraft.Serveur;
import civkraft.Transfert;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.KernelAddress;
import madkit.kernel.Madkit;
import madkit.kernel.Message;
import madkit.kernel.Probe;
import madkit.kernel.Watcher;
import madkit.message.ObjectMessage;
import madkit.simulation.activator.GenericBehaviorActivator;
import renderEngine.renderMain;
import civilisation.inspecteur.viewer.ViewerTabbed;
import civilisation.inspecteur.viewer.ViewerTableauDeBord;
import civilisation.stats.AdvancedStatsWindows;
import civilisation.stats.StatsWatcher;
import civilisation.stats.StatsWatcherBehaviorActivator;
import civilisation.world.World;
import civilisation.world.WorldViewer;
import debugUtils.MultiOutputStream;
import turtlekit.kernel.TKLauncher;
import turtlekit.kernel.TKScheduler;
import turtlekit.kernel.TurtleKit;
import turtlekit.mle.MLEEnvironment;
import turtlekit.viewer.PheromoneViewer;
import turtlekit.viewer.TKDefaultViewer;

public class CivLauncher extends TKLauncher {
	

	private static int choix;
	private static String cuda = "false";
	private static String desktop = "false";
	private static String fastRendering = "true";
	private static String GPU_gradients = "true";
	private static String network = "true"; 
	private static String launcher = CivLauncher.class.getName();
	public static TKScheduler sch;
	private InterfaceServeur iS;
	private InterfaceClient iC;
	
	@Override
	protected void activate() {
		super.activate();
	}

	protected void live() 
	 {   
	 }
	
	
	@Override
	protected void createSimulationInstance() {
		
		printStartMessage();
		
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); //TODO : this correct a strange behaviour of Swing. Must be improved.

		//SwingUtilities.invokeLater(new Runnable() {
		//		public void run() { 
		
		//synchronized(Configuration.pathToRessources) {
			
		
		//TODO : Correct the 1/2 crash chance at start

		//		}
	
		//}
		//
		//		);

		//Configuration.pathToRessources = System.getProperty("user.dir") + "/civilisation/ressources";

		/*Initialiseur.readParameters(); //Load minimum informations about the simulation
		System.out.println(Configuration.pathToRessources + Configuration.environnementACharger);
		int x = Integer.parseInt(Initialiseur.getChamp("Largeur", new File(DefinePath.pathToEnvironnements+"/"+Configuration.environnementACharger+Configuration.getExtension()))[0]);
		int	y = Integer.parseInt(Initialiseur.getChamp("Hauteur", new File(DefinePath.pathToEnvironnements+"/"+Configuration.environnementACharger+Configuration.getExtension()))[0]);

		setMadkitProperty(envDimension, x+","+y);*/
		initProperties();
		/*setMadkitProperty(startSimu, "false");
		setMadkitProperty(viewers, WorldViewer.class.getName() /*+","+ TKDefaultViewer.class.getName()///);
		setMadkitProperty(environment, World.class.getName());
		setMadkitProperty(turtles, TurtleGenerator.class.getName()+","+1);
		//setMadkitProperty(Option.noWrap, "true");*/

		super.createSimulationInstance();

		//this.launchAgent(new ViewerModificationSimulation());
		//this.launchAgent(new ViewerOptions());
		//this.launchAgent(new ViewerPerformances());
		//this.launchAgent(new ViewerTableauDeBord());
		this.launchAgent(new ViewerTabbed());
		
		
		
		//Test que tout est bien chargé
		//Configuration.afficherContenuCivkraft();

		}
	
	public static void main(String[] args) {
		
		
		// sauver les logs pour débug
		/*
		try
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			Date date = new Date();
			
	    	File logsRoot = new File("logs");
	    	logsRoot.mkdir();
			FileOutputStream fout= new FileOutputStream("logs/stdout"+dateFormat.format(date)+".log");
			FileOutputStream ferr= new FileOutputStream("logs/stderr"+dateFormat.format(date)+".log");
			
			MultiOutputStream multiOut= new MultiOutputStream(System.out, fout);
			MultiOutputStream multiErr= new MultiOutputStream(System.err, ferr);
			
			PrintStream stdout= new PrintStream(multiOut);
			PrintStream stderr= new PrintStream(multiErr);
			
			System.setOut(stdout);
			System.setErr(stderr);
		}
		catch (FileNotFoundException ex)
		{
			
		}		
		//*/
		
		
		final Semaphore pathSelected = new Semaphore(1, true);
    	try {
			pathSelected.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
	//	System.out.println(Initialiseur.getChamp("Load_last_model", new File(System.getProperty("user.dir") + "/bin/config"))[0]);
	//	System.out.println(new File(Initialiseur.getChamp("Last_loaded_model_path", new File(System.getProperty("user.dir") + "/bin/config"))[0]));

		if (new File(System.getProperty("user.dir") + "/bin/config").exists() &&
				Initialiseur.getChamp("Load_last_model", new File(System.getProperty("user.dir") + "/bin/config"))[0].equals("true") &&
				new File(Initialiseur.getChamp("Last_loaded_model_path", new File(System.getProperty("user.dir") + "/bin/config"))[0]).exists()) {
			//Config file exist and the user specified to always use latest model
			Configuration.pathToRessources = Initialiseur.getChamp("Last_loaded_model_path", new File(System.getProperty("user.dir") + "/bin/config"))[0];
			pathSelected.release();
		}
		else {

				InterfaceLauncher inter = new InterfaceLauncher(pathSelected, choix);
				inter.setVisible(true);
				
				// Il reste a tester (switch case) le choix renvoy� par l'interface
				// choix = 1 -> Solo
				// choix = 2 -> Heberger partie multijoueurs
				// choix = 3 -> Rejoindre partie multijoueurs
				
				
				try {
					pathSelected.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				choix = inter.getChoix();
				if(choix == 2 || choix == 3){
					network = "true";
					if(choix == 2){
						launcher = Serveur.class.getName();
						
					}else{
						launcher = Client.class.getName();
					}					

				}else{
			       

			        
					network = "false";
					launcher = CivLauncher.class.getName();
				}
//				JPanel pan = new JPanel();
				//	JButton buttonLoadLastModel = new JButton("Load last model");
				//	pan.add(buttonLoadLastModel);
				class MCFileChooser extends JFileChooser{
					public static final int SCRATCH_OPTION = 4;
					private JPanel accessory = new JPanel();
					private JButton scratch = new JButton(I18nList.CheckLang("Scratch"));

					public MCFileChooser()
					{
						super();
						accessory.add(scratch);
						this.setAccessory(accessory);
					}
				}

				JFileChooser chooser = new MCFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("parametres.metaciv","metaciv");
				chooser.setFileFilter(filter);
				//chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				//   chooser.add(pan,BorderLayout.SOUTH);
				int returnVal = chooser.showOpenDialog(null);
	        	    	   
				// chooser.
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
	        	    	if(Convertisseur.checkVersion(file.getParent()))
	        	    	{
		        			Configuration.pathToRessources = file.getParent();
	        	    	}
	        	    	else
	        	    	{
	        	    		if (JOptionPane.showConfirmDialog(null, "Convert to v1.90 ?","Convert to v1.90", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)
	        				{
	        	    			Convertisseur.convertir(file.getPath(), file.getParent()+"(v1.90)/parametres.metaciv");
			        			Configuration.pathToRessources = file.getParent()+"(v1.90)";
	        				}
	        	    		else
	        	    		{
	        	    			System.exit(0);
	        	    		}
	        	    	}
	        	    } else if(returnVal == MCFileChooser.SCRATCH_OPTION)
	        	    {
	        	    	
	        	    }
	        	    else{
	        	    	/*
	        			if (new File(System.getProperty("user.dir") + "/bin/config").exists() &&
	        					new File(Initialiseur.getChamp("Last_loaded_model_path", new File(System.getProperty("user.dir") + "/bin/config"))[0]).exists()) {
	        				Configuration.pathToRessources = Initialiseur.getChamp("Last_loaded_model_path", new File(System.getProperty("user.dir") + "/bin/config"))[0];
	        			} else {
		        	    	Configuration.pathToRessources = System.getProperty("user.dir") + "/civilisation/ressources";
	        			}*/
	        	    	System.exit(0);
	        	    }
	        	    System.out.println(Configuration.pathToRessources);
	        	    setField("Last_loaded_model_path",Configuration.pathToRessources,
	        	    		new File(System.getProperty("user.dir") + "/bin/config"),
	        	    		new File(System.getProperty("user.dir") + "/bin/tempConfig"));

	        	    pathSelected.release();
		}
				 
			 
			try {
				pathSelected.acquire();
			
				 System.out.println("Selected path : " + Configuration.pathToRessources);
				 
				 Initialiseur.readParameters(); //Load minimum informations about the simulation
				 System.out.println(Configuration.pathToRessources + Configuration.environnementACharger);
				 int x = Integer.parseInt(Initialiseur.getChamp("Largeur", new File(DefinePath.pathToEnvironnements+"/"+Configuration.environnementACharger+Configuration.getExtension()))[0]);
				 int y = Integer.parseInt(Initialiseur.getChamp("Hauteur", new File(DefinePath.pathToEnvironnements+"/"+Configuration.environnementACharger+Configuration.getExtension()))[0]);
					
				 new TurtleKit(
							"--desktop " + desktop									//Virer la fenetre de base MadKit
							,"--network " + network									//Active le r�seau m�taciv	
							,"--envDimension " + x + "," + y						//Taille de l'environnement/map
							,"--viewers " + WorldViewer.class.getName()				//Appel du viewer
							,"--environment " + World.class.getName()				//Appel de la map
							,"--turtles " + TurtleGenerator.class.getName()			//Appel des turtles
							,"--fastRendering " + fastRendering						//Doit am�liorer l'affichage
							,"--cuda " + cuda										//Pareil mais pour Nvidia
							,"--GPU_gradients " + GPU_gradients						//?
							,"--popDensity 0"										//?
							,"--launcher ", launcher								//Le launcher a launcher
							,"--startSimu false"									//Pour ne pas lancer direct et attendre qu'on appuie sur le bouton
							);
				 //executeThisLauncher("--popDensity","0");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
	}
	
	public void printStartMessage() {
		//Show version
		System.out.println("\n\t---------------------------------------" +
		"\n\t               MetaCiv" + "\n\t           version: "
				+ Configuration.versionNumber +
				"\n\t      MetaCiv Team (c) 2013-"
				+ Calendar.getInstance().get(Calendar.YEAR) + 
				"\n\t---------------------------------------\n");
	}
	
	
	static public void setField(String field, String newValue,  File f, File temp){
		
		 Scanner scanner;
		 PrintWriter out;
		try {
			scanner = new Scanner(new FileReader(f));
			out = new PrintWriter(new FileWriter(temp));

			 String str = null;
			 while (scanner.hasNextLine()) {
			     str = scanner.nextLine();
			     if(str.split(" : ")[0].equals(field)){
			    	 out.println((field + " : " + newValue));
			     }
			     else {
				     out.println(str);
			     }
			 }
			 out.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	catch (IOException e) {
			e.printStackTrace();
		}
		
		//String name = f.getAbsolutePath();
		f.delete();
		temp.renameTo(f);
	}
	
	@Override
	protected void launchScheduler(){
		 sch = (TKScheduler) launchAgent(getMadkitProperty(scheduler));

		//Group management activator
		sch.addActivator(new GenericBehaviorActivator<Group>(DefineConstants.Comunity_MetacivSoftware, DefineConstants.Group_MetacivSocialStructure, DefineConstants.Role_ConcreteGroup, "processMessages"));
		//Stats Watchers activator
		sch.addActivator(new StatsWatcherBehaviorActivator(DefineConstants.Comunity_MetacivSoftware,  DefineConstants.Group_StatWatcher, DefineConstants.Role_StatWatcher, "updateChart"));

		try {
			sch.setSimulationDuration(Integer.parseInt(getMadkitProperty(endTime)));
		} catch (NumberFormatException | NullPointerException e) {
		}
	}
}

