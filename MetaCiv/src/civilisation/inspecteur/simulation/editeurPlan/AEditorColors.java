package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.Color;

public enum AEditorColors {

	WHITE(236, 240, 241),
	PURPLE(155, 89, 182),
	ORANGE(243, 156, 18),
	GREEN(46, 204, 113),
	GRAY(127, 140, 141);

	public final Color color;
	
	private AEditorColors(int r, int g, int b) {
		color = new Color(r, g, b);
	}
	
}
