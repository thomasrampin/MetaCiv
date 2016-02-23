package civilisation.stats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import civilisation.Configuration;
import civilisation.DefinePath;
import civilisation.Initialiseur;

public class ADVSWidgetFactory {
	
	public static AbstractProbeWidget loadWidgetFromFile(File wFolder)
	{
		AbstractProbeWidget result = null;
		if(wFolder.exists())
		{
			File header=null;
			try {
				header = new File(wFolder.getPath()+"/"+URLEncoder.encode(DefinePath.ADVStatsWidgetHeaderFile,"UTF-8")+Configuration.getExtension());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}	
			if(header != null && header.exists() && header.isFile()){
				
				// here call init(private) for each widget type
				String tmp = Initialiseur.getChamp("type" , header)[0];
				if(tmp.equals(PopulationProbeWidget.class.toString()))
				{
					result = new PopulationProbeWidget(wFolder.getName());
					initPopulationProbe(result, wFolder);
				}
				else if(tmp.equals(ObjectProbeWidget.class.toString()))
				{
					result = new ObjectProbeWidget(wFolder.getName());
					initObjectProbe(result, wFolder);
				}
				else if(tmp.equals(AttributeProbeWidget.class.toString()))
				{
					result = new AttributeProbeWidget(wFolder.getName());
					initAttributeProbe(result, wFolder);
				}
				else if(tmp.equals(PlanProbeWidget.class.toString()))
				{
					result = new PlanProbeWidget(wFolder.getName());
					initPlanProbe(result, wFolder);
				}
				else if(tmp.equals(CognitonProbeWidget.class.toString()))
				{
					result = new CognitonProbeWidget(wFolder.getName());
					initCognitonProbe(result, wFolder);
				}
				
				if(result != null)
				{
					result.setSize(Integer.parseInt(Initialiseur.getChamp("size" , header)[0]), Integer.parseInt(Initialiseur.getChamp("size" , header)[1]));
					result.setLocation(Integer.parseInt(Initialiseur.getChamp("position" , header)[0]), Integer.parseInt(Initialiseur.getChamp("position" , header)[1]));					
					System.out.println(Initialiseur.getChamp("legend" , header)[0]);
					if(!Initialiseur.getChamp("legend" , header)[0].equals("null"))
					{
						while(result.showLegend != Boolean.parseBoolean(Initialiseur.getChamp("legend" , header)[0]))
						{
							result.changeLegendState();
						}
					}
					result.applyOptions();
				}				    	
			}
		}
		return result;
	}

	private static void initPlanProbe(AbstractProbeWidget result, File wFolder) {
		if(result.SocFilter != null)
		{
			result.SocFilter.loadFromFile(wFolder);
		}
		if(((PlanProbeWidget)result).chartTypeFilter != null)
		{
			((PlanProbeWidget)result).chartTypeFilter.loadFromFile(wFolder);
		}
	}
	
	private static void initCognitonProbe(AbstractProbeWidget result, File wFolder) {
		if(result.SocFilter != null)
		{
			result.SocFilter.loadFromFile(wFolder);
		}
		if(((CognitonProbeWidget)result).chartTypeFilter != null)
		{
			((CognitonProbeWidget)result).chartTypeFilter.loadFromFile(wFolder);
		}
	}

	private static void initPopulationProbe(AbstractProbeWidget result,File wFolder) {

		if(result.SocFilter != null)
		{
			result.SocFilter.loadFromFile(wFolder);
		}
		
	}
	
	private static void initObjectProbe(AbstractProbeWidget result,File wFolder) {

		if(result.SocFilter != null)
		{
			result.SocFilter.loadFromFile(wFolder);
		}
		if(((ObjectProbeWidget)result).objPanel != null)
		{
			((ObjectProbeWidget)result).objPanel.loadFromFile(wFolder);
		}
	}
	
	private static void initAttributeProbe(AbstractProbeWidget result,File wFolder) {

		if(result.SocFilter != null)
		{
			result.SocFilter.loadFromFile(wFolder);
		}
		if(((AttributeProbeWidget)result).AttrPanel != null)
		{
			((AttributeProbeWidget)result).AttrPanel.loadFromFile(wFolder);
		}
	}

}
