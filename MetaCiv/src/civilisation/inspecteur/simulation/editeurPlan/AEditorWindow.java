package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import civilisation.individu.plan.NPlan;

@SuppressWarnings("serial")
public class AEditorWindow extends JFrame{
	
	private AEditorPanelActions panelActions;
	private AEditorPanelEditor panelEditor;
	private JSplitPane splitPane;
	private NPlan plan;

	public AEditorWindow(String title, NPlan plan) throws HeadlessException {
		super(title);
		init(plan);
	}

	/**
	 * Fonction d'initialisation de la JFrame
	 */
	private void init(NPlan plan) {
		panelActions = new AEditorPanelActions();
		panelEditor = new AEditorPanelEditor(plan);
		this.plan = plan;
		setSize(1024, 768);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		addSaveOnClose();
		setLocationRelativeTo(null);
		JScrollPane scrollP = new JScrollPane(panelEditor);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollP, new JScrollPane(panelActions));
		splitPane.setResizeWeight(0.8);
		add(splitPane);
		setVisible(true);
	}
	
	private void addSaveOnClose() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//panelEditor.applyPlanEdition();
				System.out.println(plan);
			}
		});
	}

}
