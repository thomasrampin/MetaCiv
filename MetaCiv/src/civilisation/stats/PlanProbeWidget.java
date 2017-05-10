package civilisation.stats;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;

import civilisation.Configuration;
import civilisation.DefinePath;
import civilisation.individu.Human;
import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.NPlanPondere;
import civilisation.world.World;

public class PlanProbeWidget extends AbstractCompositeProbeWidget{

	public PlanProbeWidget(String title) {
		super(title);
		createSocialFilter();
	}		

	public PlanProbeWidget() {
		super("Plan Probe");
		createSocialFilter();
	}		

	public JTabbedPane getOptionTab() {
		JTabbedPane t = super.getOptionTab();
		t.add("Social filter", getSocFilter());
		return t;
	}
	
	public void toFile(File tabFolder)
	{
			super.toFile(tabFolder);
			File widgetFolder = new File(tabFolder.getAbsolutePath()+"/"+this.getTitle());
			PrintWriter out;	
			File header=null;
			try {
				header = new File(widgetFolder.getPath()+"/"+URLEncoder.encode(DefinePath.ADVStatsWidgetHeaderFile,"UTF-8")+Configuration.getExtension());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				out = new PrintWriter(new FileWriter(header.getPath(),true));
				out.println("type : " + this.getClass());
				out.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			
			}
		
		public void updateFromProbe(StatsWatcher statsWatcher) {

		
			for(String ck : chartTypeList)
			{
				if(ck.equals(WidgetPanelChartType.CTYP_Area))
				{
					Double totalWeight = 0.0;
					if(charts.get(statsWatcher.plotName) == null)
						addChart(statsWatcher.plotName);
					if(charts.get(statsWatcher.plotName).get(WidgetPanelChartType.CTYP_Area) == null)
						charts.get(statsWatcher.plotName).put(WidgetPanelChartType.CTYP_Area, allocChartFromType(WidgetPanelChartType.CTYP_Area));
					DefaultCategoryDataset target = (DefaultCategoryDataset) ((CategoryPlot)charts.get(statsWatcher.plotName).get(WidgetPanelChartType.CTYP_Area).getPlot()).getDataset();
					HashMap<String, Double> planWeight = new HashMap<String, Double>();
					for(Human h : statsWatcher.getAllHumans())
					{
						for(NPlanPondere p : h.getEsprit().getPlans())
						{
							if(planWeight.get(p.getPlan().getNom()) != null)
							{
								planWeight.put(p.getPlan().getNom(), planWeight.get(p.getPlan().getNom()) + Math.max(p.getPoids(), 0.0));
							}
							else
							{
								planWeight.put(p.getPlan().getNom(), Math.max(p.getPoids(), 0.0));
							}
						}
					}
					for(Double d : planWeight.values())
						totalWeight += d;
					
					for(String plan : planWeight.keySet())
					{
						target.addValue(
								Math.max(planWeight.get(plan) / totalWeight, 0.0f),
								plan,
								String.valueOf(World.getInstance().getTick()));
					}
				}
				else if(ck.equals(WidgetPanelChartType.CTYP_Pie))
				{
					Double totalWeight = 0.0;
					if(charts.get(statsWatcher.plotName) == null)
						addChart(statsWatcher.plotName);
					if(charts.get(statsWatcher.plotName).get(WidgetPanelChartType.CTYP_Pie) == null)
						charts.get(statsWatcher.plotName).put(WidgetPanelChartType.CTYP_Pie, allocChartFromType(WidgetPanelChartType.CTYP_Pie));

					DefaultPieDataset target = (DefaultPieDataset) ((PiePlot3D)charts.get(statsWatcher.plotName).get(WidgetPanelChartType.CTYP_Pie).getPlot()).getDataset();
					HashMap<String, Double> planWeight = new HashMap<String, Double>();
					for(Human h : statsWatcher.getAllHumans())
					{
						for(NPlanPondere p : h.getEsprit().getPlans())
						{
							if(planWeight.get(p.getPlan().getNom()) != null)
							{
								planWeight.put(p.getPlan().getNom(), planWeight.get(p.getPlan().getNom()) + Math.max(p.getPoids(), 0.0));
							}
							else
							{
								planWeight.put(p.getPlan().getNom(), Math.max(p.getPoids(), 0.0));
							}
						}
					}
					for(Double d : planWeight.values())
						totalWeight += d;
					
					target.clear();
					for(String plan : planWeight.keySet())
					{
						target.setValue(plan,Math.max(planWeight.get(plan) / totalWeight, 0.0f));
					}
				}
			}
		}
		
		@Override
		public void applyOptions()
		{
			super.applyOptions();
			generateProbesFromSocFilter();
			generateChartsForProbes();
		}

}
