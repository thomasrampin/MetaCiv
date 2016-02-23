package civilisation.stats;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

public class AbstractXYChartProbeWidget extends AbstractProbeWidget {
	XYSeriesCollection dataset;
    
    ChartPanel chartPanel;
    
	public AbstractXYChartProbeWidget(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	

	protected void createEmptyXYChart() {
		dataset = new XYSeriesCollection();
		chart = ChartFactory.createXYLineChart(
	            null,      // chart title
	            "Ticks",                      // x axis label
	            "Value",                      // y axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );
		legend = chart.getLegend();
	    chartPanel = new ChartPanel(chart);
	    this.add(chartPanel);
	}
	
}
