package civilisation.world;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import madkit.action.KernelAction;
import madkit.kernel.AgentAddress;
import madkit.kernel.KernelAddress;
import madkit.simulation.probe.SingleAgentProbe;
import madkit.simulation.viewer.SwingViewer;
import civilisation.Civilisation;
import civilisation.Configuration;
import civilisation.DefineConstants;

import civilisation.ItemPheromone;
import civilisation.TurtleGenerator;
import civilisation.amenagement.Amenagement;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.inspecteur.FenetreInspecteur;
import civilisation.inspecteur.simulation.ActionsMenuActions;
import civilisation.inspecteur.simulation.NodeArbreActions;
import civilisation.inspecteur.viewer.ViewerHuman;
import civilisation.individu.Esprit;
import civilisation.individu.Human;
import civilisation.individu.cognitons.Cogniton;
import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.action.Action;
import turtlekit.agr.TKOrganization;
import turtlekit.gui.CudaMenu;
import turtlekit.gui.toolbar.TKToolBar;
import turtlekit.kernel.TKScheduler;
import turtlekit.kernel.Turtle;
import turtlekit.viewer.TKDefaultViewer;
import turtlekit.kernel.Patch;
import turtlekit.pheromone.Pheromone;

/** 
 * Viewer of the environment
 * @author DTEAM
 * @version 1.0 - 2/2013
*/
public class WorldViewer extends TKDefaultViewer implements Serializable
{
	
	private static WorldViewer instance;
	private static final long serialVersionUID = -6736823013883615452L;
	FenetreInspecteur inspecteur;
	Boolean GraphismesAmeliores;
	NPlan planVisible;
	Boolean frontieresVisibles = true;
	ItemPheromone pheroToMap;
	Pheromone pheromoneToMap;
	GroupAndRole groupAndRoleToMap;
	Group groupToObserve;
	Turtle selectedAgent;
	private boolean endRendering;
	private int sizeForAccurateView = 8;
	JPopupMenu popup;
	BufferedImage bufferedView;

	public WorldViewer()
	{
		super();
		
		cellSize = 5;
		instance = this;
		this.getDisplayPane().addMouseListener(new WorldMouseListener(this));
	}
	
	protected void activate() {	
		super.activate();
		//System.out.println("WV a fini activate");
		//this.setDisplayPane(new JScrollPane(new PanelWorldViewer((JScrollPane) this.getDisplayPane())));
	}

	public void setARole(String com, String grp, String role){
		createGroupIfAbsent(com,grp,true);			
		requestRole(com,grp,role);
		//System.out.println("TEST DANS LE WV : " + this.getMyRoles(com, grp));
		String roleInverse;
		if(role.equals(DefineConstants.ROLE_CLIENT_VIEWERS)){
			roleInverse = DefineConstants.ROLE_SERVEUR_VIEWERS;
		}else{
			roleInverse = DefineConstants.ROLE_CLIENT_VIEWERS;
		}		
	}
	
	public void live(){
		//System.out.println("JE SUIS DANS LE WV : " + this.getMyRoles(this.getCommunity(), this.getMyGroups(this.getCommunity()).first()));
	}

	static public WorldViewer getInstance()
	{
		return instance;
	}
	
	static public void setInstance(WorldViewer i)
	{
		instance = i;
	}


	public void afficherPopup(MouseEvent e, Patch p){
		
		popup = new JPopupMenu();
		JMenuItem observeOne = new JMenuItem("Observe one agent");
		observeOne.addActionListener(new ActionsMenuWorld(this,0,p));
		observeOne.setIcon(Configuration.getIcon("pencil.png"));
		popup.add(observeOne);
		
		JMenuItem observeAll = new JMenuItem("Observe all agents");
		observeAll.addActionListener(new ActionsMenuWorld(this,1,p));
		observeAll.setIcon(Configuration.getIcon("cross.png"));
		popup.add(observeAll);
		
		popup.show((Component) e.getSource(), e.getX(), (e.getY()));
	}
	
	@Override
	public void paintPatch(Graphics g, Patch p,int x,int y,int cellS){
		//super.paintPatch(g, p, x, y, cellS);

		
			if (pheroToMap == null) {
				g.setColor(p.getColor());
				} else {
					double v = pheromoneToMap.get(cellS);
					if (v > 255) v = 255;
					else if (v < 0) v = 0;
					g.setColor(new Color (255 - (int)v, 255 - (int)v, 255));
				}
				g.fillRect(x,y,this.getCellSize(),this.getCellSize());

			/*	if (this.frontieresVisibles)
				{
					int controleur = getControleurPatch(p);
					List<Patch> neighbors = p.getNeighbors(1, true);

					if (controleur != -1)
					{
						Color c = Civilisation.getListeCiv().get(controleur).getCouleur();
						g.setColor(c);
						
						if (getControleurPatch(neighbors.get(0))!=controleur)
						{
							g.drawLine(x+cellS-1, y, x+cellS-1, y+cellS-1);
						}
						if (getControleurPatch(neighbors.get(6))!=controleur)
						{
							g.drawLine(x, y+cellS-1, x+cellS-1, y+cellS-1);
						}
						if (getControleurPatch(neighbors.get(4))!=controleur)
						{
							g.drawLine(x, y, x, y+cellS-1);
						}
						if (getControleurPatch(neighbors.get(2))!=controleur)
						{
							g.drawLine(x, y, x+cellS-1, y);
						}			
					}*/
				
			 	/*
				if (p.isMarkPresent("Champ"))
				{
					Amenagement mark = (Amenagement) p.getMark("Champ");
					mark.dessiner(g, x, y,  this.getCellSize());
					p.dropMark("Champ", mark);
				}*/
				if (p.isMarkPresent("Route"))
				{
					Amenagement mark = (Amenagement) p.getMark("Route");
					mark.dessiner(g, x, y, this.getCellSize());
					p.dropMark("Route", mark);
				}
		/*
				for(int i = 0; i < Configuration.amenagements.size();i++)
				{
					if(p == null)
						System.out.println("null");
					if(Configuration.amenagements.get(i) == null)
						System.out.println("amenagements null");
							
					if (p.isMarkPresent(Configuration.amenagements.get(i).getNom().toLowerCase().toString()))
					{
					//	System.out.println("Ca marche youpi tout ca tout ca");
						Amenagement mark = (Amenagement) p.getMark(Configuration.amenagements.get(i).getNom().toLowerCase().toString());
						mark.dessiner(g, x, y, this.getCellSize());
						p.dropMark(Configuration.amenagements.get(i).getNom().toLowerCase().toString(), mark);
					}
				}*/
				
		/*		if (p.isMarkPresent(Amenagement.class.getName()))
				{
					System.out.println("Ca marche youpi tout ca tout ca");
					Amenagement mark = (Amenagement) p.getMark(Amenagement.class.getName());
					mark.dessiner(g, x, y, this.getCellSize());
					p.dropMark(Amenagement.class.getName(), mark);
				}*/
	}
	
	@Override
	public void paintTurtle(Graphics g,Turtle t,int x,int y) {
		paintOneTurtle( g, t, x, y, true);
	}

	
	/**
	 * paint every turtle on the viewer
	 */
	private void paintOneTurtle(Graphics g,Turtle t,int x,int y, boolean first)
    {
		int size;
		int dx , dy;
		
		if(t.isPlayingRole(DefineConstants.Role_Human)){
				
			
			if (this.getCellSize() > 20) {
				size = this.sizeForAccurateView + (this.getCellSize() - 20)/10;
				dx = x + (int)(this.getCellSize() * (t.getX()%1));
				dy = y + (int)(this.getCellSize() * (t.getY()%1));
				//System.out.println(size+" "+dx+" "+dy+" "+this.getCellSize()+" "+x+" "+y);
				if (first) {
					paintPatch(g, t.getPatch(),x,y,World.getInstance().get1DIndex(t.xcor(), t.ycor()));
					List<Turtle> turtles = t.getOtherTurtles(0, true);
					for (int i = 0 ; i < turtles.size() ; i++) {
						paintOneTurtle(g,turtles.get(i),x,y,false);
						
					}
				}
			}
			else {
				size = this.getCellSize();
				dx = x;
				dy = y;
			}
			Human h = (Human)t;
			Esprit e = h.getEsprit();
			Color humain = e.getCognitonMaxWeight().getCogniton().getType().getCouleur();
			
			// Les dessins sous le carre de couleur
			
			if ((planVisible == null || planVisible == e.getPlanEnCours().getPlan() ))
			{	
				//System.out.println(getSelectedAgent().getName());
				if(t==selectedAgent){
					g.setColor(Color.BLUE);
					g.fillRect(dx-2,dy-2,size +4,size +4);
				}
				//Color square
				if(getCellSize() > 4) {
					g.setColor(t.getPatch().getColor());
					g.fillRect(dx,dy,size,size);
					g.setColor(humain);
					g.fillRect(dx+1,dy+1,size - 2,size - 2);
					paintDebugMessage(g, h, dx+size, dy+size);
				}
				else 
				{
					g.setColor(t.getColor());
					g.fillRect(dx,dy,size,size);
				}

				
				if (e.getHumain().isShowGroup)
				{	
					g.setColor(Color.DARK_GRAY);
					g.drawLine(dx+size -1, dy, dx+size -1, dy+size -1);
					g.drawLine(dx, dy+size -1, dx+size -1, dy+size -1);
					g.drawLine(dx, dy, dx, dy+size -1);
					g.drawLine(dx, dy, dx+size -1, dy);
					
					g.drawLine(dx+size -2, dy, dx+size -2, dy+size -1);
					g.drawLine(dx, dy+size -2, dx+size -1, dy+size -2);
					g.drawLine(dx+1, dy, dx+1, dy+size -1);
					g.drawLine(dx, dy+1, dx+size -1, dy+1);
				}
			}
			else
			{
				paintPatch(g, t.getPatch(),x,y,World.getInstance().get1DIndex(t.xcor(), t.ycor()));
				g.setColor(t.getColor());
				g.fillRect(dx+3,dy+3,size - 6,size - 6);
			}

		}


		// Les dessins sur le carr___ de couleur
		else if(t.isPlayingRole("Communaute")){
			
			//Le carr___ de couleur
			g.setColor(t.getColor());
			g.fillRect(x+1,y+1,this.getCellSize() -1,this.getCellSize() -1);
			
			g.setColor(Color.DARK_GRAY);
			g.drawLine(x+this.getCellSize() -1, y, x+this.getCellSize() -1, y+this.getCellSize() -1);
			g.drawLine(x, y+this.getCellSize() -1, x+this.getCellSize() -1, y+this.getCellSize() -1);
			g.drawLine(x, y, x, y+this.getCellSize() -1);
			g.drawLine(x, y, x+this.getCellSize() -1, y);
		}	

		
		else if(t.isPlayingRole("Group")){
			/*
			//Groups are not visible, so we paint the patch instead
			paintPatch(g, t.getPatch(),x,y,World.getInstance().get1DIndex(t.xcor(), t.ycor()));
			
			//If it's the observed group
			if(this.groupToObserve == t && endRendering){ //Observed Group is drawn over other draw
				Group group = (Group)t;
				size = this.getCellSize();
				dx = x;
				dy = y;
				


				g.setColor(Color.ORANGE);

				for (int i = 0 ; i < group.getMembers().size() ; i++) {
					Turtle target = group.getMembers().get(i);
					System.out.println("world height : " + target.getWorldHeight() + "  getY() : " + Math.round(target.getY()));
					g.drawLine(dx + size/2, dy + size/2, (target.xcor() + 1) * size - (size/2), (target.getWorldHeight()-target.ycor()) * size - (size/2));
				}
				
				g.setColor(Color.RED);
				g.fillRect(dx,dy,size,size);

			}
			*/	
		}
		else if(t.isPlayingRole(DefineConstants.Role_Facility)){
			paintPatch(g, t.getPatch(),x,y,World.getInstance().get1DIndex(t.xcor(), t.ycor()));
			((Amenagement)t).dessiner(g, x,y, this.getCellSize());
			
		}
		else {
			//All other turtles used in simulation are invisible
			paintPatch(g, t.getPatch(),x,y,World.getInstance().get1DIndex(t.xcor(), t.ycor()));
		}
	}
	
	private void paintDebugMessage(Graphics g, Human agent, int x, int y) {
		agent.setDebugString(agent.getEsprit().getPlanEnCours().planToString());
		if(!agent.getDebugString().equals("")){
			String msg = agent.getDebugString();
			//System.out.println(msg);
			int distanceBubbleFromAgent = 20;
			int padding = 2;

			Font font = new Font("Arial", Font.PLAIN, 10);
			FontMetrics metrics = g.getFontMetrics(font);
			Dimension speechBubbleSize = new Dimension(metrics.stringWidth(msg) + (2 * padding), metrics.getHeight() + (2 * padding));
			
			//le probleme !
			int posX = (int) x;//((agent.getX()) * cellSize - (5 / cellSize) - speechBubbleSize.width - distanceBubbleFromAgent);
			int posY = (int) y;//((agent.getY()) * cellSize - (5 / cellSize) - speechBubbleSize.height - distanceBubbleFromAgent);
			
			//System.out.println("posX = "+posX+" posY = "+posY);
			//System.out.println("agentX = "+agent.getX()+" agentY = "+agent.getY());
			g.setColor(Color.BLACK);
			g.drawLine(posX, posY, posX+10, posY+10);
			g.setColor(Color.WHITE);
			g.fillRect(posX+10, posY+10, speechBubbleSize.width, speechBubbleSize.height);
			g.setColor(Color.BLACK);
			g.drawRect(posX+10, posY+10, speechBubbleSize.width, speechBubbleSize.height);
			g.setColor(Color.BLUE);
			g.setFont(font);
			g.drawString(msg, posX+10 + padding, posY+10 + speechBubbleSize.height - padding);
		}
	}
	
	public int getControleurPatch(Patch p)
	{
		int controlleur = -1;
		double smellControlleur = 0;
		Turtle t = TurtleGenerator.getInstance();

		for (int i = 0; i <Configuration.civilisations.size();i++)
		{
			if (smellControlleur < t.smellAt("civ" + i , p.x ,p.y))
			{
				controlleur = i;
				smellControlleur = t.smellAt("civ" + i , p.x ,p.y);
			}
		}
		
		return controlleur;
		
	}
	
	public void setPlanVisible(NPlan p)
	{
		planVisible = p;
	}

	public void setFrontieresVisibles(Boolean frontieresVisibles) {
		this.frontieresVisibles = frontieresVisibles;
	}

	public void setPheroToMap(ItemPheromone itemPheromone) {
		pheroToMap = itemPheromone;
		pheromoneToMap = World.getInstance().getPheromone(itemPheromone.getNom());
	}

	public GroupAndRole getGroupAndRoleToMap() {
		return groupAndRoleToMap;
	}

	public void setGroupAndRoleToMap(GroupAndRole groupAndRoleToMap) {
		this.groupAndRoleToMap = groupAndRoleToMap;
	}


	public Group getGroupToObserve() {
		return groupToObserve;
	}


	public void setGroupToObserve(Group groupToObserve) {
		System.out.println("Now watching group : " + groupToObserve.getGroupModel().getName() + " ID:" + groupToObserve.hashCode());
		this.groupToObserve = groupToObserve;
	}


	public Turtle getSelectedAgent() {
		return selectedAgent;
	}


	public void setSelectedAgent(Turtle sAgent) {
		selectedAgent = sAgent;
	}

	protected void render(Graphics g) {
		super.render(g);
		endRendering = true;
		if (this.groupToObserve != null) {
			//paintOneTurtle( g, groupToObserve, groupToObserve.xcor()*this.cellSize, (groupToObserve.getWorldHeight() - groupToObserve.ycor())*this.cellSize, true);
		}
		endRendering = false;
	}
	

	
  /*  public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
    	super.paintComponents(g);
    	BufferedImage bufImage = new BufferedImage(100, 100,BufferedImage.TYPE_INT_RGB);  
        panel.paint(bufImage.createGraphics()); 
        
    	g2d.drawImage(bufImage, 10, 10, null);

    }*/

	public void observeHuman(Human h) {
		this.launchAgent(new ViewerHuman(h));
	}
	
	public void setupFrame(final JFrame frame) {
		super.setupFrame(frame);
		getFrame().getJMenuBar().setVisible(false);
		
		((TKToolBar)(getFrame().getContentPane().getComponent(0))).remove(0);
		/*((TKToolBar)(getFrame().getContentPane().getComponent(0))).remove(2);
		JButton restart = new JButton("Restart");
		restart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		((TKToolBar)(getFrame().getContentPane().getComponent(0))).add(restart, 1);
*/
	}
}
