package civilisation.inspecteur.simulation;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FileChooserUI;
import javax.swing.text.Position;

import I18n.I18nList;

import civilisation.Civilisation;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.FabriqueScheme;
import civilisation.SchemaCognitif;
import civilisation.individu.plan.NPlan;

/** 
 * G_re l'interaction avec la toolBar qui modifie la structure cognitive
 * @author DTEAM
 * @version 1.0 - 2/2013
*/

public class ActionStructureCognitive implements ActionListener{

	PanelModificationSimulation p;
	int index;
	
	public ActionStructureCognitive(PanelModificationSimulation p, int i)
	{
		this.p = p;
		index = i;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(p.tailleSchemaCognitif() != Configuration.SchemasCognitifs.size()){
			p.refreshSelectSchemaCognitif();
		}
		
		if (index == 0) 
		{
			p.getPanelStructureCognitive().creerCogniton();
		}
		else if (index == 1) 
		{
			p.getPanelStructureCognitive().creerPlan();
		}
		else if (index == 2) 
		{
			p.getPanelStructureCognitive().createCloudCogniton();
		}
		else if (index == 3) 
		{
		//	p.getPanelStructureCognitive().createGroup();
		}
		else if (index == 4) 
		{
			System.out.println("Créer schéma cognitif");

			SchemaCognitif[] tabSC = new SchemaCognitif[FabriqueScheme.listeSchemaCognitifs.size()];
			tabSC = FabriqueScheme.listeSchemaCognitifs.toArray(tabSC);			
			JComboBox<SchemaCognitif> comboSC = new JComboBox<>(tabSC);

		    JTextField champNom = new JTextField(I18nList.CheckLang("NewCognitiveScheme"));
		    JPanel panel = new JPanel(new GridLayout(0, 1));
		    JCheckBox choix = new JCheckBox(I18nList.CheckLang("Copy This Cognitive Scheme"));
		    choix.setSelected(false);
		    panel.add(new JLabel(I18nList.CheckLang("Name :")));
	        panel.add(champNom);
	        panel.add(choix);
		    panel.add(comboSC);
		    int result = JOptionPane.showConfirmDialog(p, panel, I18nList.CheckLang("Create Cognitive Scheme"),

		            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		        if (result == JOptionPane.OK_OPTION) {
		        	try{
		        	SchemaCognitif sc;
		        	if(choix.isSelected())
		        	{
						sc = FabriqueScheme.creerSchemaCognitif(champNom.getText(),(SchemaCognitif)comboSC.getSelectedItem());
					}
					else
		        	{
		        		sc = FabriqueScheme.creerSchemaCognitif();
		        		sc.setNom(champNom.getText());		
		        	}
					p.selectSchemaCognitif.addItem(sc);
					Configuration.setSchemaCognitifEnCourEdition(sc);
					p.selectSchemaCognitif.setSelectedItem(sc);	
					p.getPanelStructureCognitive().displayCognitiveScheme((SchemaCognitif)p.selectSchemaCognitif.getSelectedItem());
					p.panelAttributes.setupField();	
					p.panelConstants.setupField();

					p.panelGroupManager.changeSelection(null);
					p.panelGroupTree.refresh();
					p.panelCivilisations.refreshCognitiveSchemes();
		        	} catch (CloneNotSupportedException e1) {
		        		// TODO Auto-generated catch block
		        		e1.printStackTrace();
		        	}
        	
		        	} else {
		            System.out.println("Cancelled create cognitive scheme");
		        }

		
		}
		else if (index == 5) 
		{
			
			System.out.println("importer schéma cognitif "+ FabriqueScheme.listeSchemaCognitifs.size() + " - " + Configuration.SchemasCognitifs);
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "metaciv", "metaciv");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(p);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
				SchemaCognitif sc = FabriqueScheme.creerSchemaCognitif(chooser.getSelectedFile().getParent());
				p.selectSchemaCognitif.addItem(sc);
				Configuration.setSchemaCognitifEnCourEdition(sc);
				p.selectSchemaCognitif.setSelectedItem(sc);	
				p.getPanelStructureCognitive().displayCognitiveScheme((SchemaCognitif)p.selectSchemaCognitif.getSelectedItem());
				p.panelAttributes.setupField();				

				p.panelConstants.setupField();
				
				p.panelGroupManager.changeSelection(null);
				p.panelGroupTree.refresh();
				p.panelCivilisations.refreshCognitiveSchemes();

		    }
			System.out.println("importer schéma cognitif "+ FabriqueScheme.listeSchemaCognitifs.size() + " - " + Configuration.SchemasCognitifs);
			
			
			
			
			
			
		}
		else if (index == 6) 
		{
			System.out.println("Supprimer schéma cognitif");

			if (JOptionPane.showConfirmDialog(null, I18nList.CheckLang("Are you sure you want to delete this cognitive scheme?"),I18nList.CheckLang("DELETE COGNITIVE SCHEME"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)
			{
				Configuration.SchemasCognitifs.remove((SchemaCognitif)p.selectSchemaCognitif.getSelectedItem());
				p.selectSchemaCognitif.removeItem((SchemaCognitif)p.selectSchemaCognitif.getSelectedItem());
				if(Configuration.SchemasCognitifs.size() > 0)
				{
					Configuration.setSchemaCognitifEnCourEdition(Configuration.SchemasCognitifs.get(0));
					p.selectSchemaCognitif.setSelectedItem(Configuration.SchemasCognitifs.get(0));	
					p.getPanelStructureCognitive().displayCognitiveScheme((SchemaCognitif)p.selectSchemaCognitif.getSelectedItem());
					p.panelAttributes.setupField();

					p.panelConstants.setupField();
			
					p.panelGroupManager.changeSelection(null);
					p.panelGroupTree.refresh();
					p.panelCivilisations.refreshCognitiveSchemes();

				}
			}
		}
		else if (index == 7) 
		{
			Configuration.setSchemaCognitifEnCourEdition((SchemaCognitif)p.selectSchemaCognitif.getSelectedItem());
			System.out.println("Cognitive scheme : " + 	Configuration.getSchemaCognitifEnCourEdition().getNom());
			p.getPanelStructureCognitive().displayCognitiveScheme((SchemaCognitif)p.selectSchemaCognitif.getSelectedItem());
			p.panelAttributes.setupField();

			p.panelConstants.setupField();
			
			p.panelGroupManager.changeSelection(null);
			p.panelGroupTree.refresh();			
			p.panelCivilisations.refreshCognitiveSchemes();
		}
		else if (index == 8) //  cognitive scheme options
		{
			final String positive = "Positive";
			final String negative = "Negative";
			final String both = "Both";
			
			System.out.println("cogScheme options");
			
			JComboBox<String> interval = new JComboBox<String>();
			interval.addItem(positive);
			interval.addItem(negative);
			interval.addItem(both);			
			switch (Configuration.SCEnCoursEdition.weightInterval)
			{
				case DefineConstants._WeightIntervalPositive:
					interval.setSelectedItem(positive);break;
				case DefineConstants._WeightIntervalNegative:
					interval.setSelectedItem(negative);break;				
				case DefineConstants._WeightIntervalBoth:
					interval.setSelectedItem(both);break;					
			}
			JSpinner val = new JSpinner();
			val.setValue(Configuration.SCEnCoursEdition.weightsBound);
			
		    JPanel panel = new JPanel(new GridLayout(0, 1));
		    panel.add(new JLabel("Plan weight influence"));
		    panel.add(new JLabel("Bound sign"));
		    panel.add(interval);
		    panel.add(new JLabel("Bound value"));
	        panel.add(val);
		    int result = JOptionPane.showConfirmDialog(p, panel, "Cognitive scheme options",
		            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		        if (result == JOptionPane.OK_OPTION) {
					switch ((String)interval.getSelectedItem())
					{
						case positive:
							Configuration.SCEnCoursEdition.weightInterval = DefineConstants._WeightIntervalPositive;break;
						case negative:
							Configuration.SCEnCoursEdition.weightInterval = DefineConstants._WeightIntervalNegative;break;
						case both:
							Configuration.SCEnCoursEdition.weightInterval = DefineConstants._WeightIntervalBoth;break;
					}
					Configuration.SCEnCoursEdition.weightsBound = Math.abs(Double.parseDouble(val.getValue().toString()));
					
		        	} else {
		            System.out.println("Cancelled cognitive scheme options");
		        }
		}
		
	}




}
