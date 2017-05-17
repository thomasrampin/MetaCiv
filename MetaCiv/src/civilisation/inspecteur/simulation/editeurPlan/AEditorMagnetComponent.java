package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.Point;

public interface AEditorMagnetComponent {
	
	public static final int MAGNET_EFFECT = 20;

	boolean isInsideMagnetEffectBottom(Point loc);
	boolean isInsideMagnetEffectTop(Point loc);

}
