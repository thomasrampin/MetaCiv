package civilisation.stats;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeriesCollection;

import turtlekit.agr.TKOrganization;
import turtlekit.kernel.TurtleKit;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Probe;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.DefinePath;
import civilisation.Initialiseur;
import civilisation.individu.Human;
import civilisation.stats.WidgetPanelSocialFilter.CheckNode;

public abstract class AbstractProbeWidget extends JInternalFrame{

	JFreeChart chart;
	LegendTitle legend;
	WidgetPanelSocialFilter SocFilter = null;
	WidgetPanelGeneralProperties genProp = null;
	JToolBar optionsBar;
	HashMap<String, StatsWatcher> humanProbes; 
	boolean showLegend = true;
	
	public AbstractProbeWidget(String title)
	{
		super(title,true,true,true,true);
		this.setSize(200, 200);
		this.setLocation(100, 100);
		this.setLayout(new BorderLayout());
		genProp = new WidgetPanelGeneralProperties();
		genProp.widgetName.setText(title);
		optionsBar = new JToolBar();
		
		JButton optionBTN =new JButton("options");
		optionBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogStatOptions d = new DialogStatOptions((JFrame)getThis().getTopLevelAncestor(), true, getThis());
				d.setVisible(true);
			}
		});
		
		JButton legendBTN =new JButton("Show/hide legend");
		legendBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLegendState();
			}
		});
		Dimension d = optionsBar.getPreferredSize();
		d.height = 20;
		optionsBar.setMinimumSize(d);
		optionsBar.setMaximumSize(d);
		optionsBar.add(optionBTN);
		optionsBar.add(legendBTN);
		this.add(optionsBar,BorderLayout.SOUTH);
	}
	
	protected void changeLegendState() {
		if(chart != null)
		{
			if(chart.getLegend() == null)
			{
				chart.addLegend(legend);
				showLegend = true;
			}
			else
			{
				chart.removeLegend();
				showLegend = false;
			}
		}
	}

	public WidgetPanelSocialFilter getSocFilter()
	{
		return SocFilter;
	}
	
	public JTabbedPane getOptionTab()
	{
		JTabbedPane res = new JTabbedPane();
		genProp.setWidgetName(this.getTitle());
		res.add("general properties", genProp);
		return res;
	}

	public void applyOptions()
	{
		this.setTitle(genProp.getWidgetName());
	}
	
	public AbstractProbeWidget getThis()
	{
		return this;
	}
	
	public void createSocialFilter(File infile)
	{
		SocFilter = new WidgetPanelSocialFilter();
		humanProbes = new HashMap<String,StatsWatcher>();
	}

	public void createSocialFilter()
	{
		createSocialFilter(null);
	}

	public void loadFromFile(File folder)
	{
		
	}
	
	public void toFile(File tabFolder) {
		File widgetFolder = new File(tabFolder.getAbsolutePath()+"/"+this.getTitle());
		if(widgetFolder.exists())
			widgetFolder.delete();
		widgetFolder.mkdirs();
		
		System.out.println(this.getClass());
		if(SocFilter != null)
		{
			SocFilter.toFile(widgetFolder);
		}
		
		PrintWriter out;	
		File header=null;
		try {
			header = new File(widgetFolder.getPath()+"/"+URLEncoder.encode(DefinePath.ADVStatsWidgetHeaderFile,"UTF-8")+Configuration.getExtension());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			out = new PrintWriter(new FileWriter(header.getPath()));
			out.println("size : " + this.getWidth() + "," + this.getHeight());
			out.println("position : " + this.getX() + "," + this.getY());
			out.println("legend : " + this.showLegend);
			out.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	
	public void generateProbesFromSocFilter()
	{
		if(SocFilter!= null)
		{
			CheckNode root = (CheckNode) SocFilter.tree.getModel().getRoot();
			for(int i = 0 ; i < root.getChildCount() ; i++)
			{
				if(((CheckNode)root.getChildAt(i)).getUserObject().equals(WidgetPanelSocialFilter.totalInWorld))
				{
					if(((CheckNode)root.getChildAt(i)).isSelected)
					{
						if(!humanProbes.containsKey(WidgetPanelSocialFilter.totalInWorld))
						{
							StatsWatcher p = new StatsWatcher(this);
							p.setPlotName(WidgetPanelSocialFilter.totalInWorld);
							Configuration.viewer.launchAgent(p);
							p.addProbe(new Probe<Human>(p.getMadkitProperty(TurtleKit.Option.community), TKOrganization.TURTLES_GROUP, DefineConstants.Role_Human));
							humanProbes.put(WidgetPanelSocialFilter.totalInWorld, p);
						}
					}
					else
					{
						humanProbes.remove(WidgetPanelSocialFilter.totalInWorld);
					}
				}
				else
					generateCivProbes((CheckNode)root.getChildAt(i));
			}
		}
	}
	
	private void generateCivProbes(CheckNode civ)
	{
		for(int i = 0 ; i < civ.getChildCount() ; i++)
		{
			if(((CheckNode)civ.getChildAt(i)).getUserObject().equals(WidgetPanelSocialFilter.totalInCiv))
			{
				if(((CheckNode)civ.getChildAt(i)).isSelected)
				{
					if(!humanProbes.containsKey(civ.getUserObject() + " "+ WidgetPanelSocialFilter.total))
					{
						StatsWatcher p = new StatsWatcher(this);
						p.setPlotName(civ.getUserObject() + " "+ WidgetPanelSocialFilter.total);
						p.setciv(((String)civ.getUserObject()));
						Configuration.viewer.launchAgent(p);
						p.addProbe(new Probe<Human>(DefineConstants.Comunity_Civilisations, (String)civ.getUserObject(), DefineConstants.Role_Human));
						humanProbes.put(civ.getUserObject() + " "+ WidgetPanelSocialFilter.total, p);
					}
				}
				else
				{
					humanProbes.remove(civ.getUserObject() + " "+ WidgetPanelSocialFilter.total);
				}
			}
			else
			{
				for(int j = 0 ; j < civ.getChildAt(i).getChildCount() ; j++)
				{
					generateGroupProbes((String)civ.getUserObject(),(CheckNode)civ.getChildAt(i).getChildAt(j));
				}
			}
		}
	}

	private void generateGroupProbes(String civ,CheckNode grp)
	{
		for(int i = 0 ; i < grp.getChildCount() ; i++)
		{
			if(((CheckNode)grp.getChildAt(i)).getUserObject().equals(WidgetPanelSocialFilter.totalForGroupType))
			{
				if(((CheckNode)grp.getChildAt(i)).isSelected)
				{
					if(!humanProbes.containsKey(civ + " " + grp.getUserObject() + " "+ WidgetPanelSocialFilter.total))
					{
						GroupStatsWatcher p = new GroupStatsWatcher(this);
						p.setPlotName(civ + " " + grp.getUserObject() + " "+ WidgetPanelSocialFilter.total);
						p.setciv((civ));
						p.setGrp((String)grp.getUserObject());
						p.setTotal(true);
						Configuration.viewer.launchAgent(p);
						humanProbes.put(civ + " " + grp.getUserObject() + " "+ WidgetPanelSocialFilter.total, p);
					}
				}
				else
				{
					humanProbes.remove(civ + " " +grp.getUserObject() + " "+ WidgetPanelSocialFilter.total);
				}
			}
			else if(((CheckNode)grp.getChildAt(i)).getUserObject().equals(WidgetPanelSocialFilter.separateCount))
			{
				if(((CheckNode)grp.getChildAt(i)).isSelected)
				{
					if(!humanProbes.containsKey(civ + " " + grp.getUserObject() + " "+ WidgetPanelSocialFilter.separateCount))
					{
						GroupStatsWatcher p = new GroupStatsWatcher(this);
						p.setPlotName(civ + " " + grp.getUserObject() + " "+ WidgetPanelSocialFilter.separateCount);
						p.setciv((civ));
						p.setGrp((String)grp.getUserObject());
						p.setSeparate(true);
						Configuration.viewer.launchAgent(p);
						humanProbes.put(civ + " " + grp.getUserObject() + " "+ WidgetPanelSocialFilter.separateCount, p);
					}
				}
				else
				{
					humanProbes.remove(civ + " " +grp.getUserObject() + " "+ WidgetPanelSocialFilter.separateCount);
				}
			}
			else
			{
				for(int j = 0 ; j < grp.getChildAt(i).getChildCount() ; j++)
				{
					generateRoleProbes(civ,(String)grp.getUserObject(),(CheckNode)grp.getChildAt(i).getChildAt(j));
				}
			}
		}
	}

	
	private void generateRoleProbes(String civ, String grp,CheckNode role) {
		for(int i = 0 ; i < role.getChildCount() ; i++)
		{
			if(((CheckNode)role.getChildAt(i)).getUserObject().equals(WidgetPanelSocialFilter.totalForGroupType))
			{
				if(((CheckNode)role.getChildAt(i)).isSelected)
				{
					if(!humanProbes.containsKey(civ + " " + grp + " "+ role.getUserObject() + " "+ WidgetPanelSocialFilter.total))
					{
						RoleStatsWatcher p = new RoleStatsWatcher(this);
						p.setPlotName(civ + " " + grp + " "+ role.getUserObject() + " "+ WidgetPanelSocialFilter.total);
						p.setciv((civ));
						p.setGrp((String)grp);
						p.setRole((String)role.getUserObject());
						p.setTotal(true);
						Configuration.viewer.launchAgent(p);
						humanProbes.put(civ + " " + grp + " "+ role.getUserObject() + " "+ WidgetPanelSocialFilter.total, p);
					}
				}
				else
				{
					humanProbes.remove(civ + " " + grp + " "+ role.getUserObject() + " "+ WidgetPanelSocialFilter.total);
				}
			}
			else if(((CheckNode)role.getChildAt(i)).getUserObject().equals(WidgetPanelSocialFilter.separateCount))
			{
				if(((CheckNode)role.getChildAt(i)).isSelected)
				{
					if(!humanProbes.containsKey(civ + " " + grp + " "+ role.getUserObject()+ " "+ WidgetPanelSocialFilter.separateCount))
					{
						RoleStatsWatcher p = new RoleStatsWatcher(this);
						p.setPlotName(civ + " " + grp + " "+ role.getUserObject()+ " "+ WidgetPanelSocialFilter.separateCount);
						p.setciv((civ));
						p.setGrp((String)grp);
						p.setRole((String)role.getUserObject());
						p.setSeparate(true);
						Configuration.viewer.launchAgent(p);
						humanProbes.put(civ + " " + grp + " "+ role.getUserObject()+ " "+ WidgetPanelSocialFilter.separateCount, p);
					}
				}
				else
				{
					humanProbes.remove(civ + " " + grp + " "+ role.getUserObject() + " "+ WidgetPanelSocialFilter.separateCount);
				}
			}
		}
	}

	public void updateFromProbe(StatsWatcher statsWatcher) {
		
	}
}
