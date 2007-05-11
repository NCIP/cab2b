package edu.wustl.cab2b.client.ui.treetable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;

public class B2BTreeNode {

    Vector<B2BTreeNode> children = new Vector<B2BTreeNode>();

    String strDisplayName;

    Object value;

    public Object[] getChildren() {

        if (children == null)
            return null;

        return children.toArray();
    }

    public String toString() {
        return strDisplayName;
    }

    public Object getValue() {
        return value;
    }

    public void addChild(B2BTreeNode child) {
        this.children.add(child);
    }

    public void setChildren(Vector<B2BTreeNode> children) {
        this.children = children;

    }

    public void setDisplayName(String strDisplayName) {
        this.strDisplayName = strDisplayName;

    }

    public void setValue(Object value) {
        this.value = value;
    }

    public JDialog showInDialog() {
        Dimension dimension = MainFrame.mainframeScreenDimesion;

        B2BNewModel b2BNewModel = new B2BNewModel(this);
        JTreeTable treeTable = new JTreeTable(b2BNewModel);
        JScrollPane jScrollPane = new JScrollPane(treeTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JDialog dialog = WindowUtilities.setInDialog(NewWelcomePanel.mainFrame, jScrollPane, "Test Tree",
                                                     new Dimension((int) (dimension.width * 0.45),
                                                             (int) (dimension.height * 0.60)), true, false);
        dialog.setVisible(true);
        return dialog;
    }
    
    public Cab2bPanel getCategoryResultPanel()
    {
        //creating tree 
        JTreeTable treeTable = new JTreeTable(new B2BNewModel(this));
        
        //adding to scrollpane
        JScrollPane jScrollPane = new JScrollPane(treeTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBackground(Color.WHITE);       
        Cab2bPanel trePanel = new Cab2bPanel();
        trePanel.setLayout(new BorderLayout());        
        trePanel.add(jScrollPane);        
        return trePanel;   
    }

    public static void main(String arr[]) {
        B2BTreeNode node1 = new B2BTreeNode();
        node1.setDisplayName("Root1");
        node1.setValue("1");

        B2BTreeNode node[] = new B2BTreeNode[500];

        for (int i = 0; i < 500; i++) {
            node[i] = new B2BTreeNode();
            node[i].setDisplayName("" + i);
            node[i].setValue(i);
            node1.addChild(node[i]);
        }

        B2BNewModel b2BNewModel = new B2BNewModel(node1);
        JTreeTable myTestTable = new JTreeTable(b2BNewModel);
        Cab2bPanel myTestPanel = new Cab2bPanel();
        myTestPanel.add(myTestTable);
        WindowUtilities.showWithScrollingInFrame(myTestTable, "testMe");
    }
}
