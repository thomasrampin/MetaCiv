package civkraft;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.concurrent.Semaphore;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import I18n.I18nList;
import civilisation.CivLauncher;
import civilisation.Configuration;
import madkit.kernel.Madkit;
import turtlekit.kernel.TurtleKit;

public class InterfaceLauncher extends JFrame implements MouseListener, ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JPanel civkraftPanelSolo;
	private JPanel civkraftPanelMulti;
	private JPanel choixLang;
	private JButton civkraftBoutonSolo;
	private JButton civkraftBoutonSoloWith3D;
	private JButton civkraftBoutonHeberger;
	private JButton civkraftBoutonRejoindre;
	private JLabel civkraftTextSolo;
	private JLabel civkraftTextMulti;
	private JPanel civkraftSuperPanel;
	private Semaphore sema;
	private int choix;
	private int choix3D;

	
	protected JComboBox langBox;
	protected JButton valider;
	String[] elemLang = new String[]{"Eng","Fr","Rus"};
	
	public final static Dimension dimenionInterfaceLauncher = new Dimension(360,400);
	
	public InterfaceLauncher(Semaphore s, int c) throws HeadlessException {
		super();
		
		sema = s;
		choix = c;
	
		civkraftPanelSolo = new JPanel(new BorderLayout());
		civkraftPanelMulti = new JPanel(new BorderLayout());
		choixLang =  new JPanel(new BorderLayout());
		civkraftSuperPanel = new JPanel(new GridBagLayout());
		
		
		civkraftBoutonSolo = new JButton(I18nList.CheckLang("Solo / Editeur"));
		civkraftBoutonSoloWith3D = new JButton(I18nList.CheckLang("Solo 3D/ Editeur"));
		civkraftBoutonHeberger = new JButton(I18nList.CheckLang("Heberger une partie"));
		civkraftBoutonRejoindre = new JButton(I18nList.CheckLang("Rejoindre une partie"));
		//valider = new JButton("Valider");
		
		civkraftTextSolo = new JLabel("Metaciv", JLabel.CENTER);
		civkraftTextSolo.setOpaque(true);
		civkraftTextSolo.setForeground(new Color(255,255,255));
		civkraftTextSolo.setBackground(Color.black);
		civkraftTextMulti = new JLabel(I18nList.CheckLang("Civkraft : Jeu multijoueur"), JLabel.CENTER);
		civkraftTextMulti.setOpaque(true);
		civkraftTextMulti.setForeground(new Color(255,255,255));
		civkraftTextMulti.setBackground(Color.black);
		
		
		civkraftSuperPanel.setBackground(Color.white);
		langBox = new JComboBox(elemLang);
		langBox.addActionListener(this);
		
		JLayeredPane panel0 =new JLayeredPane();
		JPanel panel1=new JPanel();
		JPanel panel2=new JPanel();
		JPanel panFond1=new JPanel(new GridLayout(0,1));

		panel0.setPreferredSize(new Dimension(360, 360)); // permet d'empiler le Panelles	
		panel1.setBounds(0, 0, 360, 360);
		panel2.setBounds(0, 0, 360, 260); // setBounds permet de choisir la position et la taille d'objet en question par rapport � la frame 
	
		Configuration.getIcon("disk-black.png");

		JLabel im = new JLabel(new ImageIcon(Configuration.getImage("200.gif")));
		JLabel im2 = new JLabel(new ImageIcon(Configuration.getImage("mc.png")));
		
		panel1.add(im);
		panel2.add(im2);
		panel1.setOpaque(false);
		panel2.setOpaque(false);

		
		civkraftBoutonSolo.addMouseListener(this);
		civkraftBoutonSoloWith3D.addMouseListener(this);
		civkraftBoutonHeberger.addMouseListener(this);
		civkraftBoutonRejoindre.addMouseListener(this);
		//valider.addMouseListener(this);
		
		civkraftPanelSolo.add(civkraftTextSolo, BorderLayout.NORTH);
		civkraftPanelSolo.add(civkraftBoutonSolo, BorderLayout.WEST);
		civkraftPanelSolo.add(civkraftBoutonSoloWith3D, BorderLayout.EAST);
		
		civkraftPanelMulti.add(civkraftTextMulti, BorderLayout.NORTH);
		
		civkraftPanelMulti.add(civkraftBoutonHeberger, BorderLayout.WEST);
		civkraftPanelMulti.add(civkraftBoutonRejoindre, BorderLayout.EAST);
		
		//choixLang.add(valider, BorderLayout.WEST);
		choixLang.add(langBox, BorderLayout.CENTER);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);
		 
		 
		civkraftSuperPanel.add(civkraftPanelSolo, gbc);
		civkraftSuperPanel.add(civkraftPanelMulti, gbc);
		civkraftSuperPanel.add(choixLang, gbc);
		
		/*civkraftSuperPanel.add(civkraftPanelSolo);
		civkraftSuperPanel.add(civkraftPanelMulti);
		civkraftSuperPanel.add(choixLang);*/
		
		
	
		panel0.add(panel1, new Integer(1));
		panel0.add(panel2, new Integer(2));
		
		panFond1.add(panel0);
		panFond1.add(civkraftSuperPanel);
		this.add(panFond1);
		this.setTitle("Metaciv");
		this.setSize(dimenionInterfaceLauncher);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource().equals(civkraftBoutonSolo)){
			
			
			this.dispose();			
			//this.setVisible(false);
			setChoix(1);
			choix3D=0;
			sema.release();
		}
		if(arg0.getSource().equals(civkraftBoutonSoloWith3D)){
			
			
			this.dispose();
	
			//this.setVisible(false);
			setChoix(1);
			choix3D=1;
			sema.release();
		}
		if(arg0.getSource().equals(civkraftBoutonHeberger)){
			
			
			this.dispose();
			setChoix(2);
			sema.release();
			// Il faudra passer la semaphore dans le constructeur
			// Ne pas oublier de la release ensuite pour debloquer le main de CivLauncher.
			//InterfaceServeur iS = new InterfaceServeur(sema,512,256);
			//iS.setVisible(true);
			//sema.release();
			/*new Madkit(
					"--network true"
					,"--desktop false"
					,"--launchAgents"
					,Serveur.class.getName() + ",true,1;"
					);*/
		}
		if(arg0.getSource().equals(civkraftBoutonRejoindre)){
			
			
			this.dispose();
			setChoix(3);
			sema.release();
			// Il faudra passer la semaphore dans le constructeur
			// Ne pas oublier de la release ensuite pour debloquer le main de CivLauncher.
			//Client c = new Client(sema, 512,256);
			/*new Madkit 
			( 
					"--network true"
					,"--desktop false"
					,"--launchAgents"
					,Client.class.getName() + ",true,1;" //,
					);*/
			//InterfaceClient iC = new InterfaceClient(sema, 512, 256);
			//iC.setVisible(true);
		}
	}
	
	public String getLang(){
		 String valeur = langBox.getSelectedItem().toString();
	   	return valeur;
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public int getChoix() {
		return choix;
	}
	
	public int getChoix3D() {
		return choix3D;
	}
	
	public void setChoix(int choix) {
		this.choix = choix;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(langBox))
		{
			I18nList.infoLang = langBox.getSelectedItem().toString();
			
			civkraftBoutonSolo.setText(I18nList.CheckLang("Solo / Editeur"));
			civkraftBoutonSoloWith3D.setText(I18nList.CheckLang("Solo 3D/ Editeur"));
			civkraftBoutonHeberger.setText(I18nList.CheckLang("Heberger une partie"));
			civkraftBoutonRejoindre.setText(I18nList.CheckLang("Rejoindre une partie"));
			civkraftTextMulti.setText(I18nList.CheckLang("Civkraft : Jeu multijoueur"));

		}
	}
}
