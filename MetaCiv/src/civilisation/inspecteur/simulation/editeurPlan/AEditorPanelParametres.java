package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.ItemPheromone;
import civilisation.amenagement.TypeAmenagement;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.cognitons.TypeCogniton;
import civilisation.individu.plan.action.Action;
import civilisation.individu.plan.action.Comparator;
import civilisation.individu.plan.action.OptionsActions;
import civilisation.inventaire.Objet;
import civilisation.world.Terrain;

@SuppressWarnings("serial")
/**
 * Classe qui fabrique le panel contenant le nom de l'action du block et les parametres de l'action 
 * @author Arnau
 *
 */
public class AEditorPanelParametres extends JPanel {
	
	JLabel lname; // label du nom de l'action
	ArrayList<JComponent> boxs = new ArrayList<JComponent>();
	ArrayList<String[]> schema;
	Action a;
    JComboBox extraBox=null;

    
    public AEditorPanelParametres(String name) {
    	super();
		lname = new JLabel(name, SwingConstants.CENTER);
		this.add(lname);
    }
	
	public AEditorPanelParametres(Action action) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		a = action;
		lname = new JLabel(action.getSimpleName(), SwingConstants.CENTER);
		this.add(lname);
		schema = a.getSchemaParametres();
		addParameters();
	}
	
	/**
	 * 
	 * @return le label du titre de l'action
	 */
	public JLabel getLName() {
		return lname;
	}
	
	/**
	 * Ajout des parametres dans le panel
	 * Tiré de la fonction du créer la boite de dialogue d'edition
	 */
	private void addParameters() {
		boolean extraAttribute = false;
		String extraTitle="extra Attribute";
		JRadioButton selector;
		ButtonGroup grpSelector=null;
				
		if (schema != null){
			for (int i = 0; i < schema.size(); i++){
				JComboBox box = new JComboBox();
				boolean isComboBox = true;
				System.out.println(schema.get(i)[0]);
				if (schema.get(i)[0].equals("**Objet**")){
					for (int j = 0; j < Configuration.objets.size(); j++){
						box.addItem(Configuration.objets.get(j).getNom());
						
					}
					if(a != null && a.getOptions().size() > 0 && a.getOptions().get(i) != null)
					{
						box.setSelectedItem(((Objet)a.getOptions().get(i).getParametres().get(0)).getNom());
					}
				}
				else if (schema.get(i)[0].equals("**Cogniton**")){
					for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getCognitons().size(); j++){
						box.addItem(Configuration.getSchemaCognitifEnCourEdition().getCognitons().get(j).getNom());
					}
					if(a != null && a.getOptions().size() > 0 && a.getOptions().get(i) != null)
					{
						box.setSelectedItem(((TypeCogniton)a.getOptions().get(i).getParametres().get(0)).getNom());
					}
				}
				else if (schema.get(i)[0].equals("**Pheromone**")){
					for (int j = 0; j < Configuration.itemsPheromones.size(); j++){
						box.addItem(Configuration.itemsPheromones.get(j).getNom());
					}
					if(a != null && a.getOptions().size() > 0 && a.getOptions().get(i) != null)
					{
						box.setSelectedItem(((ItemPheromone)a.getOptions().get(i).getParametres().get(0)).getNom());
					}
				}
				else if (schema.get(i)[0].equals("**Integer**")){
					for (int j = Integer.parseInt(schema.get(i)[2]); j < Integer.parseInt(schema.get(i)[3]); j++){
						box.addItem(j);
					}
					if(a != null &&   a.getOptions().size() > 0 && a.getOptions().get(i) != null)
					{
						if(a.getOptions().get(i).getParametres().get(0).getClass().equals(Integer.class))
						{
							box.setSelectedItem(a.getOptions().get(i).getParametres().get(0));
						}
						else
						{
							box.setSelectedItem(Integer.parseInt((String) a.getOptions().get(i).getParametres().get(0)));
						}
						
					}
					extraAttribute = true;
					extraTitle = "Linked Constant";
					extraBox = new JComboBox();
					extraBox.addItem(DefineConstants.__MC_NULL_CONSTANT);
					for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size() ; j++){
						extraBox.addItem(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().get(j));
					}
					if(a != null &&  a.getOptions().size() > 0 && a.getOptions().get(i) != null && a.getOptions().get(i).getParametres().size() >= 2)
					{
						extraBox.setSelectedItem(((String)a.getOptions().get(i).getParametres().get(1)));
					}
					else
					{
						extraBox.setSelectedItem(DefineConstants.__MC_NULL_CONSTANT);
					}
				}
				else if (schema.get(i)[0].equals("**Double**")){
					for (double j = Double.parseDouble(schema.get(i)[2]); j < Double.parseDouble(schema.get(i)[3]); j+=Double.parseDouble(schema.get(i)[4])){
						box.addItem(j);
					}
					if(a != null &&   a.getOptions().size() > 0 && a.getOptions().get(i) != null)
					{
						if(a.getOptions().get(i).getParametres().get(0).getClass().equals(Double.class))
						{
							box.setSelectedItem(a.getOptions().get(i).getParametres().get(0));
						}
						else
						{
							box.setSelectedItem(Double.parseDouble((String) a.getOptions().get(i).getParametres().get(0)));
						}
						
					}
					extraAttribute = true;
					extraTitle = "Linked Constant";
					extraBox = new JComboBox();
					extraBox.addItem(DefineConstants.__MC_NULL_CONSTANT);
					for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size() ; j++){
						extraBox.addItem(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().get(j));
					}
					if(a != null &&  a.getOptions().size() > 0 && a.getOptions().get(i) != null && a.getOptions().get(i).getParametres().size() >= 2)
					{
						extraBox.setSelectedItem(((String)a.getOptions().get(i).getParametres().get(1)));
					}
					else
					{
						extraBox.setSelectedItem(DefineConstants.__MC_NULL_CONSTANT);
					}
				}
				else if (schema.get(i)[0].equals("**Attribute**")){
					for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size() ; j++){
						box.addItem(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().get(j));
					}
					if(a != null &&  a.getOptions().size() > 0 && a.getOptions().get(i) != null)
					{
						box.setSelectedItem(((String)a.getOptions().get(i).getParametres().get(0)));
					}
				}
				else if (schema.get(i)[0].equals("**Constant**")){
					for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().size() ; j++){
						box.addItem(Configuration.getSchemaCognitifEnCourEdition().getConstantsNames().get(j));
					}
					if(a != null &&  a.getOptions().size() > 0 && a.getOptions().get(i) != null)
					{
						box.setSelectedItem(((String)a.getOptions().get(i).getParametres().get(0)));
					}
				}
				else if (schema.get(i)[0].equals("**Group**")){
					for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getGroups().size(); j++){
						box.addItem(Configuration.getSchemaCognitifEnCourEdition().getGroups().get(j).getName());
					}
					if(a != null &&  a.getOptions().size() > 0 && a.getOptions().get(i) != null)
					{
						box.setSelectedItem(((GroupModel)a.getOptions().get(i).getParametres().get(0)).getName());
					}
				}
				else if (schema.get(i)[0].equals("**GroupAndRole**")){
					for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getGroups().size(); j++){
						System.out.println(Configuration.getSchemaCognitifEnCourEdition().getGroups().size() + " j : " + j + "keyset size : " + Configuration.getSchemaCognitifEnCourEdition().getGroups().get(j).getCulturons().keySet().size());
						Object[] keys = (Object[]) Configuration.getSchemaCognitifEnCourEdition().getGroups().get(j).getCulturons().keySet().toArray();
						for (int k = 0 ; k < Configuration.getSchemaCognitifEnCourEdition().getGroups().get(j).getCulturons().size() ; k++) {
							box.addItem(Configuration.getSchemaCognitifEnCourEdition().getGroups().get(j).getName()+":"+(String)keys[k]);	
						}
					}

				}
				else if (schema.get(i)[0].equals("**Amenagement**"))
				{
						for (int j = 0; j < Configuration.amenagements.size(); j++){
							box.addItem(Configuration.amenagements.get(j).getNom());
						}
						System.out.println(a + " "+a.getOptions());
						if(a != null &&  a.getOptions().size() > 0 && a.getOptions().get(i) != null)
						{
							box.setSelectedItem(((TypeAmenagement)a.getOptions().get(i).getParametres().get(0)).getNom());
						}
				}
				else if (schema.get(i)[0].equals("**Terrain**"))
				{
					for (int j = 0; j < Configuration.terrains.size(); j++){
						box.addItem(Configuration.terrains.get(j).getNom());
					}
					System.out.println(a + " "+a.getOptions());
					if(a != null &&  a.getOptions().size() > 0 && a.getOptions().get(i) != null)
					{
						box.setSelectedItem(((Terrain)a.getOptions().get(i).getParametres().get(0)).getNom());
					}
				}
				else if (schema.get(i)[0].equals("**Comparator**")){
					for (int j = 0; j < Comparator.values().length ; j++){
						box.addItem(Comparator.values()[j].toSymbol());
					}
					if(a != null &&  a.getOptions().size() > 0 && a.getOptions().get(i) != null)
					{
						box.setSelectedItem(((Comparator)a.getOptions().get(i).getParametres().get(0)).toSymbol());
					}
				}
				else if (schema.get(i)[0].equals("**String**")){
					boxs.add(new JTextField());
					isComboBox = false;
				}
				else{
					for (int j = 0; j < schema.get(i).length; j++){
						box.addItem(schema.get(i)[j]);
					}
				}
				if (isComboBox){
					if(extraAttribute)
					{
						grpSelector = new ButtonGroup();
						selector = new JRadioButton();
						//selector.setName(Integer.toString(boxs.size()+3));
						selector.setOpaque(false);
						grpSelector.add(selector);
						selector.setSelected(true);
						selector.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								((JComboBox)boxs.get(Integer.parseInt(((JRadioButton)(e.getSource())).getName()))).setSelectedItem(DefineConstants.__MC_NULL_CONSTANT);
							}
						});
						selector.addActionListener(new AEditorParameterSelectorChangeListener()); // listener de changement
						boxs.add(selector);
					}
					box.setMaximumSize(box.getPreferredSize());
					boxs.add(box);					
				}
				box.addItemListener(new AEditoComboBoxParameterChangeListener()); // listenr de changement
				Box hb = Box.createHorizontalBox();
				hb.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
				
				JLabel nom = new JLabel(schema.get(i)[1]);
				nom.setForeground(AEditorColors.WHITE.color);
				if(extraAttribute)
				{
					selector = new JRadioButton();
					selector.setOpaque(false);
					grpSelector.add(selector);
					if(!extraBox.getSelectedItem().equals(DefineConstants.__MC_NULL_CONSTANT))
					{
						selector.setSelected(true);
					}
					selector.addActionListener(new AEditorParameterSelectorChangeListener()); // listenr de changment
					extraBox.addItemListener(new AEditoComboBoxParameterChangeListener()); // listener de changement
					boxs.add(selector);
					boxs.add(extraBox);
					hb.add(boxs.get(boxs.size()-4));
					//hb.add(nom);
					hb.add(boxs.get(boxs.size()-3));
					hb.add(boxs.get(boxs.size()-2));
					/*JLabel eT = new JLabel(extraTitle);
					eT.setForeground(AEditorColors.WHITE.color);
					hb.add(eT);*/
					hb.add(boxs.get(boxs.size()-1));	
				}
				else
				{
					//hb.add(nom);
					hb.add(boxs.get(boxs.size()-1));
				}
				this.add(hb);
				extraAttribute = false;
			}
		}
	}
	
	/**
	 * Mets a jour les paramaetres de l'action suivants les valeurs saisies dans les blocks
	 * tiré de la classe qui créer la boite de dialogue d'edition
	 */
	private void appliquerChangementsParametres() {
		a.clearOptions(); //Suppression des anciennes options
		
		int schIndex = 0;
		for (int i = 0; i < boxs.size(); i++){	
			OptionsActions opt = null;

			if (schema.get(schIndex)[0].equals("**Objet**")){
				opt = new OptionsActions(schema.get(schIndex)[1]); /*Le deuxi___me terme est toujours le nom du param___tre pour les param___tres complexes*/
				opt.addParametre(Configuration.getObjetByName((String)((JComboBox) boxs.get(i)).getSelectedItem()));
			}
			else if (schema.get(schIndex)[0].equals("**Pheromone**")){
				opt = new OptionsActions(schema.get(schIndex)[1]); /*Le deuxi___me terme est toujours le nom du param___tre pour les param___tres complexes*/
				opt.addParametre(Configuration.getPheromoneByName((String)((JComboBox) boxs.get(i)).getSelectedItem()));
			}
			else if (schema.get(schIndex)[0].equals("**Cogniton**")){
				opt = new OptionsActions(schema.get(schIndex)[1]);
				opt.addParametre(Configuration.getSchemaCognitifEnCourEdition().getCognitonByName((String)((JComboBox) boxs.get(i)).getSelectedItem()));
			}
			else if (schema.get(schIndex)[0].equals("**Group**")){
				opt = new OptionsActions(schema.get(schIndex)[1]);
				opt.addParametre(Configuration.getSchemaCognitifEnCourEdition().getGroupModelByName((String)((JComboBox) boxs.get(i)).getSelectedItem()));
			}
			else if (schema.get(schIndex)[0].equals("**GroupAndRole**")){
				opt = new OptionsActions(schema.get(schIndex)[1]);
				opt.addParametre(new GroupAndRole((String)((JComboBox) boxs.get(i)).getSelectedItem(), Configuration.getSchemaCognitifEnCourEdition()));
			}
			else if (schema.get(schIndex)[0].equals("**Integer**")){
				opt = new OptionsActions(schema.get(schIndex)[1]); /*Le deuxi___me terme est toujours le nom du param___tre pour les param___tres complexes*/
				opt.addParametre((Integer)((JComboBox) boxs.get(i+1)).getSelectedItem());
				i+=3;
				opt.addParametre(((JComboBox) boxs.get(i)).getSelectedItem(),"Constant");
			}
			else if (schema.get(schIndex)[0].equals("**Double**")){
				opt = new OptionsActions(schema.get(schIndex)[1]); /*Le deuxi___me terme est toujours le nom du param___tre pour les param___tres complexes*/
				opt.addParametre((Double)((JComboBox) boxs.get(i+1)).getSelectedItem());
				i+=3;
				opt.addParametre(((JComboBox) boxs.get(i)).getSelectedItem(),"Constant");
			}
			else if (schema.get(schIndex)[0].equals("**Attribute**")){
				opt = new OptionsActions(schema.get(schIndex)[1]);
				opt.addParametre(((JComboBox) boxs.get(i)).getSelectedItem());
			}
			else if (schema.get(schIndex)[0].equals("**Constant**")){
				opt = new OptionsActions(schema.get(schIndex)[1]);
				opt.addParametre(((JComboBox) boxs.get(i)).getSelectedItem(),"Constant");
			}
			else if (schema.get(schIndex)[0].equals("**Comparator**")){
				opt = new OptionsActions(schema.get(schIndex)[1]);
				opt.addParametre(Comparator.toComparator((String)((JComboBox) boxs.get(i)).getSelectedItem()));

			}
			else if (schema.get(schIndex)[0].equals("**String**")){
				opt = new OptionsActions(schema.get(schIndex)[1]);
				opt.addParametre(((JTextField) boxs.get(i)).getText(), "String");

			}
			else if (schema.get(schIndex)[0].equals("**Amenagement**"))
			{
				opt = new OptionsActions(schema.get(schIndex)[1]); /*Le deuxi___me terme est toujours le nom du param___tre pour les param___tres complexes*/
				opt.addParametre(Configuration.getAmenagementsByName((String)((JComboBox) boxs.get(i)).getSelectedItem()));
			}
			else if (schema.get(schIndex)[0].equals("**Terrain**"))
			{
				opt = new OptionsActions(schema.get(schIndex)[1]); /*Le deuxi___me terme est toujours le nom du param___tre pour les param___tres complexes*/
				opt.addParametre(Configuration.getTerrainByName((String)((JComboBox) boxs.get(i)).getSelectedItem()));
			}
			else if (schema.get(schIndex)[0] != null){
				System.out.println(schema.get(schIndex)[0]);
				opt = new OptionsActions((String)((JComboBox) boxs.get(i)).getSelectedItem());
			}
			a.parametrerOption(opt);
			schIndex++;
		}
	}
	
	/**
	 * Listeners de changement de parametres pour les combobox
	 * @author Arnau
	 *
	 */
	private class AEditoComboBoxParameterChangeListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				AEditorPanelParametres.this.appliquerChangementsParametres();
			}
		}
	}
	
	/**
	 * Listeners de changement de selectors
	 * @author Arnau
	 *
	 */
	private class AEditorParameterSelectorChangeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			AEditorPanelParametres.this.appliquerChangementsParametres();
		}
		
	}

}
