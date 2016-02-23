package I18n;

import java.io.File;
import java.util.*; 
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import turtlekit.kernel.TurtleKit;

import civkraft.Client;
import civkraft.InterfaceClient;
import civkraft.InterfaceLauncher;
import civkraft.InterfaceServeur;
import civkraft.Serveur;
import civilisation.CivLauncher;
import civilisation.Configuration;
import civilisation.Convertisseur;
import civilisation.DefinePath;
import civilisation.Initialiseur;

import civilisation.TurtleGenerator;
import civilisation.world.World;
import civilisation.world.WorldViewer;


 public  class I18nList{ 
 
	 String texte;
	 public static String infoLang = "";
	 Locale locale; 
	 ResourceBundle res;
	// final Semaphore pathSelected = new Semaphore(1, true);
	
	

  public I18nList() { 
	  
	 /* InterfaceLauncher inter = new InterfaceLauncher(pathSelected,CivLauncher.getChoix());
	  CivLauncher.setChoix( inter.getChoix());
	  inter.setVisible(true);*/
		 
  }
  

    
  /*
   * Methode cheeking JComboBox for language traduction
   */
   static public String CheckLang(String key){
	  Locale locale = Locale.getDefault();  
	  ResourceBundle res;   
	  res = ResourceBundle.getBundle("I18n.I18nListRessources_fr_FR", locale); 
	 // System.out.println("vous avez choisis la langue FR " + (String)res.getObject(key));
	  
	 
    if( infoLang.equals("Fr")){
	    locale = new Locale("fr","FR"); //Locale.getDefault(); 
	    res = ResourceBundle.getBundle("I18n.I18nListRessources_fr_FR", locale); 
	    //System.out.println("vous avez choisis la langue FR " + (String)res.getObject(key));
	    return  (String)res.getObject(key);     
    }
 
    else if( infoLang.equals("Eng")){
	    locale = new Locale("en","EN"); 
	    res = ResourceBundle.getBundle("I18n.I18nListRessources_en_EN", locale);
	   // System.out.println("vous avez choisis la langue Eng " +(String)res.getObject(key));
	    return  (String)res.getObject(key); 
	   	 
    }
    
    else if( infoLang.equals("Rus")){
	    locale = new Locale("ru","RU"); 
	    res = ResourceBundle.getBundle("I18n.I18nListRessources_ru_RU", locale);
	   // System.out.println("vous avez choisis la langue RUS " +(String)res.getObject(key));
	    return  (String)res.getObject(key); 
   	 }
    
    
    //System.out.println("Vous avez pas choisis la langue");
    return  (String)res.getObject(key);  
   
  }
   

}