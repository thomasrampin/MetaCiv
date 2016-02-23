package civilisation.stats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import civilisation.Configuration;
import civilisation.DefinePath;
import civilisation.world.World;
import madkit.kernel.Watcher;

public class PopulationProbeWidget extends AbstractXYChartProbeWidget{
	
	
	public PopulationProbeWidget() {
		super("Population Probe");
		createSocialFilter();
		createEmptyXYChart();
	}

	public PopulationProbeWidget(String title) {
		super(title);
		createSocialFilter();
		createEmptyXYChart();
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
		XYSeries target = null;
		
		for(XYSeries x : ((List<XYSeries>)dataset.getSeries()))
		{
			if(x.getKey().equals(statsWatcher.plotName))
				target = x;
		}
		if(target == null)
		{
			target = new XYSeries(statsWatcher.plotName);
			dataset.addSeries(target);
		}
		target.add(World.getInstance().getTick(), statsWatcher.getAgentCount());
	}
	
	@Override
	public void applyOptions()
	{
		super.applyOptions();
		generateProbesFromSocFilter();
	}
	
}
