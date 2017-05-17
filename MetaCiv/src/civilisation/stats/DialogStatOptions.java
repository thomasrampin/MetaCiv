package civilisation.stats;

import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class DialogStatOptions extends JDialog implements PropertyChangeListener {
    AbstractProbeWidget cont;
    JOptionPane optionPane;
    AdvancedStatsWindows advWin = null;

    public DialogStatOptions(JFrame inadvWin, boolean modal, AbstractProbeWidget p) {
        super(inadvWin, modal);
        cont = p;
        buildDialog();
    }

    public DialogStatOptions(AdvancedStatsWindows inadvWin, boolean modal, AbstractProbeWidget p) {
        super((Frame) inadvWin.getFrame(), modal);
        advWin = inadvWin;
        cont = p;
        buildDialog();
    }

    public void buildDialog() {
        Object[] array = {cont.getOptionTab()};


        Object[] options = {"OK", "Cancel"};

        //Create the JOptionPane.
        optionPane = new JOptionPane(array,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);

        optionPane.addPropertyChangeListener(this);
        //Make this dialog display it.
        this.setContentPane(optionPane);
        pack();
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        //System.out.println(optionPane.getValue());
        if (isVisible() && (optionPane.getValue().equals("OK") || optionPane.getValue().equals("Cancel"))) {
            if (optionPane.getValue().equals("OK")) {
                cont.applyOptions();
                if (advWin != null)
                    advWin.addWidgetToCurrentTab(cont);
            }
            setVisible(false);
        }

    }
}
