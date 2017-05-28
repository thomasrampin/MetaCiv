package civilisation.inspecteur.simulation.dialogues;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import I18n.I18nList;

import civilisation.Configuration;
import civilisation.world.Terrain;
import renderEngine.renderMain;

public class DialogueEditerTerrain extends JDialog implements ActionListener, PropertyChangeListener{
	
	Terrain terrain;
    JOptionPane optionPane;
    JButton add;
    JTextField nom;
    JLabel name;
    JLabel passability;
    JSpinner passabilite;
    JSpinner jSpinnerHeight;
    JSpinner jSpinnerErosion;
    JSpinner jSpinnerBlurMethod;
    JSpinner jSpinnerTiling;
    JTextField textureFile;
    JCheckBox infranchissable;
    ArrayList<Object> selectors = new ArrayList<Object>();
    ArrayList<JSpinner> startPh = new ArrayList<JSpinner>();
    ArrayList<JSpinner> growthPh = new ArrayList<JSpinner>();
    Frame f;
    boolean modal;
	private JSpinner jSpinnerMerge;
	private JSpinner jSpinnerIH;
	public DialogueEditerTerrain(Frame f , boolean modal, Terrain terrain){
		super(f,modal);
		f = f;
		modal = modal;
		this.terrain = terrain;

		this.setTitle(I18nList.CheckLang("Editer le terrain"));

		
	/*	Container cp = getContentPane(); 
		JScrollPane jspane = new JScrollPane(cp, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 		this.setContentPane(jspane);
*/
		nom = new JTextField();
		nom.setText(terrain.getNom());

		SpinnerModel spinModel = new SpinnerNumberModel(terrain.getPassabilite(), //initial value
		                               1, //min
		                               1000, //max
		                               5);

		passability = new JLabel(I18nList.CheckLang("Passability :"));
		name = new JLabel(I18nList.CheckLang("Name :"));
		passabilite = new JSpinner(spinModel);
		infranchissable = new JCheckBox(I18nList.CheckLang("Impassable?"));

		infranchissable.setSelected(terrain.getInfranchissable());
		
		selectors.add(name);
		selectors.add(nom);
		selectors.add(passability);
		selectors.add(passabilite);
		selectors.add(infranchissable);

		
		for (int i = 0 ; i< Configuration.itemsPheromones.size() ; i++) {
			int var = terrain.getPheromoneIndexByName(Configuration.itemsPheromones.get(i).getNom());
			double start = 0, growth = 0;
			if (var != -1) {
				start = terrain.getPheroInitiales().get(var);
				growth = terrain.getPheroCroissance().get(var);
			}

			
			spinModel = new SpinnerNumberModel(start, -1000, 1000, 0.1);
			JSpinner jSpinner = new JSpinner(spinModel);

			jSpinner.setToolTipText(I18nList.CheckLang("Starting value of ") + Configuration.itemsPheromones.get(i).getNom());
			startPh.add(jSpinner);
			
			Box boxStart = Box.createHorizontalBox();
			boxStart.add(new JLabel(I18nList.CheckLang("Starting value : ")));

			boxStart.add(jSpinner);

			spinModel = new SpinnerNumberModel(growth, -1000, 1000, 0.1);
			JSpinner jSpinner2 = new JSpinner(spinModel);


			jSpinner2.setToolTipText(I18nList.CheckLang("Growth value of ") + Configuration.itemsPheromones.get(i).getNom());
			growthPh.add(jSpinner2);
			
			Box boxGrowth = Box.createHorizontalBox();
			boxGrowth.add(new JLabel(I18nList.CheckLang("Growth value : ")));

			boxGrowth.add(jSpinner2);
			
			Box box = Box.createVerticalBox();
			box.add(boxStart);
			box.add(boxGrowth);
			TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), Configuration.itemsPheromones.get(i).getNom());
			border.setTitleJustification(TitledBorder.LEFT);
			box.setBorder(border);
			selectors.add(box);

			
		}
		
		/*3D Part*/
		Box boxHeight = Box.createHorizontalBox();
		boxHeight.add(new JLabel(I18nList.CheckLang("Height : ")));
		spinModel = new SpinnerNumberModel(terrain.getHeight(), -1000, 1000, 0.1);
		jSpinnerHeight = new JSpinner(spinModel);
		boxHeight.add(jSpinnerHeight);
		
		Box boxErosion = Box.createHorizontalBox();
		spinModel = new SpinnerNumberModel(terrain.getErosion(), 1, 1000, 1);
		boxErosion.add(new JLabel(I18nList.CheckLang("Erosion : ")));
		jSpinnerErosion = new JSpinner(spinModel);
		boxErosion.add(jSpinnerErosion);

		Box boxBlurMethod = Box.createHorizontalBox();
		spinModel = new SpinnerNumberModel(terrain.getBlur(), 0, 1, 1);
		boxBlurMethod.add(new JLabel(I18nList.CheckLang("Ersosion Method : ")));
		jSpinnerBlurMethod = new JSpinner(spinModel);
		boxBlurMethod.add(jSpinnerBlurMethod);
		
		Box boxMerge = Box.createHorizontalBox();
		spinModel = new SpinnerNumberModel(terrain.getMerge(), 0, 100, 1);
		boxMerge.add(new JLabel(I18nList.CheckLang("Merge : ")));
		jSpinnerMerge= new JSpinner(spinModel);
		boxMerge.add(jSpinnerMerge);
		
		Box boxIntensity = Box.createHorizontalBox();
		spinModel = new SpinnerNumberModel(terrain.getIntensityHeightMap(), 0, 1000, 0.1);
		boxIntensity.add(new JLabel(I18nList.CheckLang("Intensity HeightMap : ")));
		jSpinnerIH= new JSpinner(spinModel);
		boxIntensity.add(jSpinnerIH);
		
		Box boxTexture = Box.createHorizontalBox();
		boxTexture.add(new JLabel(I18nList.CheckLang("Textures Folder : ")));
		textureFile = new JTextField(terrain.getTexture());
		boxTexture.add(textureFile);
		
		Box boxTiling = Box.createHorizontalBox();
		boxTiling.add(new JLabel(I18nList.CheckLang("Tiling : ")));
		spinModel = new SpinnerNumberModel(terrain.getTiling(), 0, 1000, 0.1);
		jSpinnerTiling = new JSpinner(spinModel);
		boxTiling.add(jSpinnerTiling);
		
		Box box = Box.createVerticalBox();
		box.add(boxHeight);
		box.add(boxErosion);
		box.add(boxBlurMethod);
		box.add(boxMerge);
		box.add(boxIntensity);
		box.add(boxTexture);
		box.add(boxTiling);
		


		/**/
		
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "3D Part");
		border.setTitleJustification(TitledBorder.LEFT);
		box.setBorder(border);
		selectors.add(box);
	    Object[] array = selectors.toArray();
	    Object[] options = {"OK" , "Cancel"};
	 
	    optionPane = new JOptionPane(array,
	                                    JOptionPane.QUESTION_MESSAGE,
	                                    JOptionPane.YES_NO_OPTION,
	                                    null,
	                                    options,
	                                    options[0]); 
	    setContentPane(new JScrollPane(optionPane));
	        
	    optionPane.addPropertyChangeListener(this);
	        
		ImageIcon icone = new ImageIcon(System.getProperty("user.dir")+"/civilisation/graphismes/LogoMedium.png");
		optionPane.setIcon(icone);
		this.pack();
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (isVisible() && (optionPane.getValue().equals("OK") || optionPane.getValue().equals("Cancel"))){
			if (optionPane.getValue().equals("OK")){
				terrain.setNom(nom.getText());
				terrain.setPassabilite((Integer)passabilite.getValue());
				terrain.setInfranchissable(infranchissable.isSelected());
				terrain.setHeight( (double) jSpinnerHeight.getValue());
				terrain.setErosion((Integer) jSpinnerErosion.getValue());
				terrain.setBlur((Integer) jSpinnerBlurMethod.getValue());
				terrain.setMerge((Integer) jSpinnerMerge.getValue());
				terrain.setTexture(textureFile.getText());
				terrain.setTiling((double) jSpinnerTiling.getValue());
				terrain.setIntensityHeightMap((double) jSpinnerIH.getValue());
				renderMain.updateTerrain();
				terrain.clearPheromones();		
				for (int i = 0 ; i < Configuration.itemsPheromones.size() ; i++) {
					if ((Double)startPh.get(i).getValue() != 0 || (Double)growthPh.get(i).getValue() != 0) {
						System.out.println("ter" + Configuration.itemsPheromones.get(i).getNom()+" "+ (Double)startPh.get(i).getValue()+" "+ (Double)growthPh.get(i).getValue());
						terrain.addPheromoneLiee(Configuration.itemsPheromones.get(i), (Double)startPh.get(i).getValue(), (Double)growthPh.get(i).getValue());
					}
				}
			}		
	        setVisible(false);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DialogCreatePheromon dia = new DialogCreatePheromon(f,modal,terrain);
		dia.setVisible(true);
	}
	
	
}
