package civilisation.stats;

import java.awt.BorderLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.*;

import civilisation.Civilisation;
import civilisation.Configuration;


import civilisation.DefinePath;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

// tree management code from oracle website

public class WidgetPanelSocialFilter extends JPanel {
    JTree tree;
    CheckNode rootNode;
    CheckNode firstNode;
    public static final String totalInWorld = "Total in world";
    public static final String totalInCiv = "Total in civilisation";
    public static final String total = "Total";
    public static final String totalForGroupType = "Total for group type";
    public static final String separateCount = "Separate count for instances";

    public WidgetPanelSocialFilter() {

        this.setLayout(new BorderLayout());
        rootNode = new CheckNode("Civilisations", true, false, CheckNode.LABEL_MODE);
        firstNode = new CheckNode(totalInWorld);
        rootNode.add(firstNode);
        for (Civilisation c : Configuration.civilisations) {
            CheckNode civNode = new CheckNode(c.getNom(), true, false, CheckNode.LABEL_MODE);
            civNode.add(new CheckNode(totalInCiv));
            CheckNode grpTitle = new CheckNode("Groups", true, false, CheckNode.LABEL_MODE);
            civNode.add(grpTitle);
            for (GroupModel g : c.getCognitiveScheme().getGroups()) {
                CheckNode grpNode = new CheckNode(g.getName(), true, false, CheckNode.LABEL_MODE);
                grpNode.add(new CheckNode(totalForGroupType));
                grpNode.add(new CheckNode(separateCount));
                CheckNode rleTitle = new CheckNode("Roles", true, false, CheckNode.LABEL_MODE);
                grpNode.add(rleTitle);
                for (String r : g.getCulturons().keySet()) {
                    CheckNode rleNode = new CheckNode(r, true, false, CheckNode.LABEL_MODE);
                    rleNode.add(new CheckNode(totalForGroupType));
                    rleNode.add(new CheckNode(separateCount));
                    rleTitle.add(rleNode);
                }
                grpTitle.add(grpNode);
            }
            rootNode.add(civNode);
        }
        tree = new JTree(rootNode);

        tree.setCellRenderer(new TreeRenderer());
        tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION
        );
        //tree.putClientProperty("JTree.lineStyle", "Angled");
        tree.addMouseListener(new NodeSelectionListener(tree));
        JScrollPane sp = new JScrollPane(tree);
        add(sp, BorderLayout.CENTER);

    }

    public void checkTotalInWorld(){
        this.firstNode.setSelected(true);
    }

    class NodeSelectionListener extends MouseAdapter {
        JTree tree;

        NodeSelectionListener(JTree tree) {
            this.tree = tree;
        }

        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int row = tree.getRowForLocation(x, y);
            TreePath path = tree.getPathForRow(row);
            //TreePath  path = tree.getSelectionPath();
            if (path != null) {
                CheckNode node = (CheckNode) path.getLastPathComponent();
                if (node.selectionMode == CheckNode.CHECK_MODE) {
                    boolean isSelected = !(node.isSelected());
                    node.setSelected(isSelected);
                    ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
                    // I need revalidate if node is root.  but why?
                    if (row == 0) {
                        tree.revalidate();
                        tree.repaint();
                    }
                }
            }
        }
    }

    class TreeRenderer extends JPanel implements TreeCellRenderer {
        protected JCheckBox check;

        protected TreeLabel label;

        public TreeRenderer() {
            setLayout(null);
            add(check = new JCheckBox());
            add(label = new TreeLabel());
            check.setBackground(UIManager.getColor("Tree.textBackground"));
            label.setForeground(UIManager.getColor("Tree.textForeground"));
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean isSelected, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            String stringValue = tree.convertValueToText(value, isSelected,
                    expanded, leaf, row, hasFocus);
            setEnabled(tree.isEnabled());
            if (((CheckNode) value).selectionMode == CheckNode.CHECK_MODE) {
                check.setSelected(((CheckNode) value).isSelected());
                check.setVisible(true);
            } else
                check.setVisible(false);
            label.setFont(tree.getFont());
            label.setText(stringValue);
            label.setSelected(isSelected);
            label.setFocus(hasFocus);
            if (leaf) {
                label.setIcon(UIManager.getIcon("Tree.leafIcon"));
            } else if (expanded) {
                label.setIcon(UIManager.getIcon("Tree.openIcon"));
            } else {
                label.setIcon(UIManager.getIcon("Tree.closedIcon"));
            }
            return this;
        }

        public Dimension getPreferredSize() {
            Dimension d_check = check.getPreferredSize();
            Dimension d_label = label.getPreferredSize();
            return new Dimension(d_check.width + d_label.width,
                    (d_check.height < d_label.height ? d_label.height
                            : d_check.height));
        }

        public void doLayout() {
            Dimension d_check = check.getPreferredSize();
            Dimension d_label = label.getPreferredSize();
            int y_check = 0;
            int y_label = 0;
            if (d_check.height < d_label.height) {
                y_check = (d_label.height - d_check.height) / 2;
            } else {
                y_label = (d_check.height - d_label.height) / 2;
            }
            check.setLocation(0, y_check);
            check.setBounds(0, y_check, d_check.width, d_check.height);
            if (check.isVisible()) {
                label.setLocation(d_check.width, y_label);
                label.setBounds(d_check.width, y_label, d_label.width, d_label.height);
            } else {
                label.setLocation(0, y_label);
                label.setBounds(0, y_label, d_label.width, d_label.height);
            }
        }

        public void setBackground(Color color) {
            if (color instanceof ColorUIResource)
                color = null;
            super.setBackground(color);
        }

        public class TreeLabel extends JLabel {
            boolean isSelected;

            boolean hasFocus;

            public TreeLabel() {
            }

            public void setBackground(Color color) {
                if (color instanceof ColorUIResource)
                    color = null;
                super.setBackground(color);
            }

            public void paint(Graphics g) {
                String str;
                if ((str = getText()) != null) {
                    if (0 < str.length()) {
                        if (isSelected) {
                            g.setColor(UIManager
                                    .getColor("Tree.selectionBackground"));
                        } else {
                            g.setColor(UIManager.getColor("Tree.textBackground"));
                        }
                        Dimension d = getPreferredSize();
                        int imageOffset = 0;
                        Icon currentI = getIcon();
                        if (currentI != null) {
                            imageOffset = currentI.getIconWidth()
                                    + Math.max(0, getIconTextGap() - 1);
                        }
                        g.fillRect(imageOffset, 0, d.width - 1 - imageOffset,
                                d.height);
                        if (hasFocus) {
                            g.setColor(UIManager
                                    .getColor("Tree.selectionBorderColor"));
                            g.drawRect(imageOffset, 0, d.width - 1 - imageOffset,
                                    d.height - 1);
                        }
                    }
                }
                super.paint(g);
            }

            public Dimension getPreferredSize() {
                Dimension retDimension = super.getPreferredSize();
                if (retDimension != null) {
                    retDimension = new Dimension(retDimension.width + 3,
                            retDimension.height);
                }
                return retDimension;
            }

            public void setSelected(boolean isSelected) {
                this.isSelected = isSelected;
            }

            public void setFocus(boolean hasFocus) {
                this.hasFocus = hasFocus;
            }
        }
    }

    class CheckNode extends DefaultMutableTreeNode {

        public static final int LABEL_MODE = 0;
        public static final int CHECK_MODE = 1;

        protected int selectionMode;

        protected boolean isSelected;

        public CheckNode() {
            this(null);
        }

        public CheckNode(Object userObject) {
            this(userObject, true, false, CHECK_MODE);
        }

        public CheckNode(Object userObject, boolean allowsChildren,
                         boolean isSelected, int selMode) {
            super(userObject, allowsChildren);
            this.isSelected = isSelected;
            this.selectionMode = selMode;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public boolean isSelected() {
            return isSelected;
        }
    }

    public void toFile(File folder) {

        if (folder.exists() && folder.isDirectory()) {
            PrintWriter out;
            File socfilter = null;
            try {
                socfilter = new File(folder.getPath() + "/" + URLEncoder.encode(DefinePath.ADVStatsWidgetSocialFilterFile, "UTF-8") + Configuration.getExtension());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                out = new PrintWriter(new FileWriter(socfilter.getPath()));

                outputSelectionToFileRec(out, (CheckNode) tree.getModel().getRoot(), "");
                out.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void outputSelectionToFileRec(PrintWriter out, CheckNode current, String path) {
        path += "/" + (String) current.getUserObject();
        if (current.selectionMode == CheckNode.CHECK_MODE && current.isSelected)
            out.println(path);
        for (int i = 0; i < current.getChildCount(); i++) {
            outputSelectionToFileRec(out, (CheckNode) current.getChildAt(i), path);
        }
    }

    public void loadFromFile(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            File socfilter = null;
            try {
                socfilter = new File(folder.getPath() + "/" + URLEncoder.encode(DefinePath.ADVStatsWidgetSocialFilterFile, "UTF-8") + Configuration.getExtension());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (socfilter.exists() && socfilter.isFile()) {
                Scanner scanner = null;
                try {
                    scanner = new Scanner(new FileReader(socfilter));
                    String str;
                    while (scanner.hasNextLine()) {
                        str = scanner.nextLine();
                        StringTokenizer stok = new StringTokenizer(str, "/");
                        stok.nextToken();
                        CheckNode current = (CheckNode) tree.getModel().getRoot();
                        CheckNode next = null;
                        Boolean valid = true;
                        while (stok.hasMoreElements() && valid) {
                            int i = 0;
                            String tokName = stok.nextToken();
                            while (i < current.getChildCount()) {
                                if (((String) ((CheckNode) current.getChildAt(i)).getUserObject()).equals(tokName)) {
                                    next = (CheckNode) current.getChildAt(i);
                                    break;
                                }
                                i++;
                            }
                            if (i == current.getChildCount())
                                valid = false;
                            current = next;
                        }
                        if (valid)
                            current.setSelected(true);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    scanner.close();
                }
            }
        }
    }
}
