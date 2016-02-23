package civilisation.stats;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import civilisation.Configuration;
import civilisation.DefinePath;
import civilisation.individu.Human;
import civilisation.world.World;

public class AttributeProbeWidget extends AbstractXYChartProbeWidget{

    
	public WidgetPanelAttributes AttrPanel;

	public AttributeProbeWidget() {
		super("Attribute Probe");
		createSocialFilter();
		createAttributeFilter();
		createEmptyXYChart();
	}

	public AttributeProbeWidget(String title) {
		super(title);
		createSocialFilter();
		createAttributeFilter();
		createEmptyXYChart();
	}
	
	private void createAttributeFilter() {
		AttrPanel = new WidgetPanelAttributes();
	}

	public JTabbedPane getOptionTab() {
		JTabbedPane t = super.getOptionTab();
		t.add("Social filter", getSocFilter());
		t.add("Attribute filter", getAttrFilter());
		return t;
	}

	private Component getAttrFilter() {
		return AttrPanel;
	}

	public void toFile(File tabFolder)
	{
		super.toFile(tabFolder);
		File widgetFolder = new File(tabFolder.getAbsolutePath()+"/"+this.getTitle());
		AttrPanel.toFile(widgetFolder);
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
		if(AttrPanel.check.get(statsWatcher.civ) != null)
		{
			for(JCheckBox ck : AttrPanel.check.get(statsWatcher.civ))
			{
				if(ck.isSelected())
				{
					XYSeries target = null;
					for(XYSeries x : ((List<XYSeries>)dataset.getSeries()))
					{
						if(x.getKey().equals(statsWatcher.plotName + " - " + ck.getText()))
							target = x;
					}
					if(target == null)
					{
						target = new XYSeries(statsWatcher.plotName+ " - " + ck.getText());
						dataset.addSeries(target);
					}
					double average = 0;
					for(Human h : statsWatcher.getAllHumans())
					{
						average += h.getAttr().get(ck.getText());
					}
					average /= statsWatcher.getAllHumans().size();
					target.add(World.getInstance().getTick(), average);
				}
			}
		}
	}
	@Override
	public void applyOptions()
	{
		super.applyOptions();
		generateProbesFromSocFilter();
	}
	
}