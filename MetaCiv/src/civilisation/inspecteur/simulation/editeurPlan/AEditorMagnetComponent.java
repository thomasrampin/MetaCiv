package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.Point;

/**
 * Interface pour les fonctions pour savoir si un point est dans la zone magnetique d'un block
 * @author Arnau
 *
 */
public interface AEditorMagnetComponent {
	
	public static final int MAGNET_EFFECT = 20;

	boolean isInsideMagnetEffectBottom(Point loc);
	boolean isInsideMagnetEffectTop(Point loc);

}
