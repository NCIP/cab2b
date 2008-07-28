package edu.wustl.cab2b.client.ui.treetable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;

public class B2BTreeNode {

    private Vector<B2BTreeNode> children = new Vector<B2BTreeNode>();

    private String strDisplayName;

    private Object value;

    public Vector<B2BTreeNode> getChildren() {

        return children;
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

    public String getDisplayName() {
        return strDisplayName;

    }

    public void setValue(Object value) {
        this.value = value;
    }

    public JDialog showInDialog() {
        Dimension dimension = MainFrame.getScreenDimesion();

        B2BNewModel b2BNewModel = new B2BNewModel(this);
        JTreeTable treeTable = new JTreeTable(b2BNewModel);
        JScrollPane jScrollPane = new JScrollPane(treeTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JDialog dialog = WindowUtilities.setInDialog(NewWelcomePanel.getMainFrame(), jScrollPane, "Test Tree",
                                                     new Dimension((int) (dimension.width * 0.45),
                                                             (int) (dimension.height * 0.60)), true, false);
        dialog.setVisible(true);
        return dialog;
    }

    public Cab2bPanel getCategoryResultPanel() {
        //creating tree 
        JTreeTable treeTable = new JTreeTable(new B2BNewModel(this));

        //setting header null
        treeTable.setTableHeader(null);

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
        B2BTreeNode rootNode = new B2BTreeNode();
        rootNode.setDisplayName("GENE");

        B2BTreeNode rootAttribute1 = new B2BTreeNode();
        rootAttribute1.setDisplayName("Cytogenetic Position");
        rootAttribute1.setValue("5q31.1");
        rootAttribute1.setChildren(null);

        B2BTreeNode rootAttribute2 = new B2BTreeNode();
        rootAttribute2.setDisplayName("Entrez Gene ID");
        rootAttribute2.setValue("1958");
        rootAttribute2.setChildren(null);

        B2BTreeNode rootAttribute3 = new B2BTreeNode();
        rootAttribute3.setDisplayName("Gene Name early  growth response");
        rootAttribute3.setValue("1");
        rootAttribute3.setChildren(null);

        B2BTreeNode rootAttribute4 = new B2BTreeNode();
        rootAttribute4.setDisplayName("Number of Pub Med Articles");
        rootAttribute4.setValue("1850");
        rootAttribute4.setChildren(null);

        B2BTreeNode rootAttribute5 = new B2BTreeNode();
        rootAttribute5.setDisplayName("Chromosome");
        rootAttribute5.setValue("5");
        rootAttribute5.setChildren(null);

        B2BTreeNode uniGene = new B2BTreeNode();
        uniGene.setDisplayName("UniGene");

        B2BTreeNode uniGene1 = new B2BTreeNode();
        uniGene1.setDisplayName("UniGene1");

        B2BTreeNode uniGeneAttribute1 = new B2BTreeNode();
        uniGeneAttribute1.setDisplayName("Uni Gene Cluster ID");
        uniGeneAttribute1.setValue("Hs.326035");
        uniGeneAttribute1.setChildren(null);

        B2BTreeNode geneBankAccession1 = new B2BTreeNode();
        geneBankAccession1.setDisplayName("GeneBankAccession1");

        B2BTreeNode geneBankAccessionAttribute1 = new B2BTreeNode();
        geneBankAccessionAttribute1.setDisplayName("Gen Bank Accession Number");
        geneBankAccessionAttribute1.setValue("NM_001964");
        geneBankAccessionAttribute1.setChildren(null);

        B2BTreeNode geneBankAccession2 = new B2BTreeNode();
        geneBankAccession2.setDisplayName("GeneBankAccession2");

        B2BTreeNode geneBankAccessionAttribute2 = new B2BTreeNode();
        geneBankAccessionAttribute2.setDisplayName("Gen Bank Accession Number");
        geneBankAccessionAttribute2.setValue("NM_001964");
        geneBankAccessionAttribute2.setChildren(null);

        B2BTreeNode uniGene2 = new B2BTreeNode();
        uniGene2.setDisplayName("UniGene2");

        B2BTreeNode uniGeneAttribute2 = new B2BTreeNode();
        uniGeneAttribute2.setDisplayName("Uni Gene Cluster ID");
        uniGeneAttribute2.setValue(" Hs.592987");
        uniGeneAttribute2.setChildren(null);

        B2BTreeNode geneBankAccession3 = new B2BTreeNode();
        geneBankAccession3.setDisplayName("GeneBankAccession1");

        B2BTreeNode geneBankAccession2Attribute1 = new B2BTreeNode();
        geneBankAccession2Attribute1.setDisplayName("Gen Bank Accession Number");
        geneBankAccession2Attribute1.setValue("CD522629");
        geneBankAccession2Attribute1.setChildren(null);

        B2BTreeNode geneBankAccession4 = new B2BTreeNode();
        geneBankAccession4.setDisplayName("GeneBankAccession2");

        B2BTreeNode geneBankAccession2Attribute2 = new B2BTreeNode();
        geneBankAccession2Attribute2.setDisplayName("Gen Bank Accession Number");
        geneBankAccession2Attribute2.setValue("CD522629");
        geneBankAccession2Attribute2.setChildren(null);

        B2BTreeNode sts = new B2BTreeNode();
        sts.setDisplayName("STS");

        B2BTreeNode sts1 = new B2BTreeNode();
        sts1.setDisplayName("STS1");

        B2BTreeNode stsAttribute1 = new B2BTreeNode();
        stsAttribute1.setDisplayName("STS ID ");
        stsAttribute1.setValue("34569");
        stsAttribute1.setChildren(null);

        B2BTreeNode sts2 = new B2BTreeNode();
        sts2.setDisplayName("STS2");

        B2BTreeNode stsAttribute2 = new B2BTreeNode();
        stsAttribute2.setDisplayName("STS ID ");
        stsAttribute2.setValue("48525");
        stsAttribute2.setChildren(null);

        uniGene1.addChild(uniGeneAttribute1);
        geneBankAccession1.addChild(geneBankAccessionAttribute1);
        geneBankAccession2.addChild(geneBankAccessionAttribute2);
        uniGene1.addChild(geneBankAccession1);
        uniGene1.addChild(geneBankAccession2);

        uniGene2.addChild(uniGeneAttribute2);
        geneBankAccession3.addChild(geneBankAccession2Attribute1);
        geneBankAccession4.addChild(geneBankAccession2Attribute2);
        uniGene2.addChild(geneBankAccession3);
        uniGene2.addChild(geneBankAccession4);

        uniGene.addChild(uniGene1);
        uniGene.addChild(uniGene2);

        sts1.addChild(stsAttribute1);
        sts2.addChild(stsAttribute2);

        sts.addChild(sts1);
        sts.addChild(sts2);

        rootNode.addChild(rootAttribute1);
        rootNode.addChild(rootAttribute2);
        rootNode.addChild(rootAttribute3);
        rootNode.addChild(rootAttribute4);
        rootNode.addChild(rootAttribute5);
        rootNode.addChild(uniGene);
        rootNode.addChild(sts);

        B2BNewModel b2BNewModel = new B2BNewModel(rootNode);
        JTreeTable myTestTable = new JTreeTable(b2BNewModel);
        myTestTable.setBorder(BorderFactory.createBevelBorder(3));
        Cab2bPanel myTestPanel = new Cab2bPanel();
        myTestPanel.add(myTestTable);
        WindowUtilities.showWithScrollingInFrame(myTestTable, "testMe");
    }
}
