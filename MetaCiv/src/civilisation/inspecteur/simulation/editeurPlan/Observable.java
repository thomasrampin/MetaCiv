package civilisation.inspecteur.simulation.editeurPlan;


public interface Observable {
	public void addObserver(Observer obs);
	public void updateObserver();
	public void clearObserver();
}
