package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.common.tree.GenerateTree;
import edu.wustl.common.tree.TreeNodeImpl;

/**
 * Panel to show the tree.
 * @author gautam_shetty
 */
public class TreePanel extends Cab2bPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Tree to be shown.
     */
    private JXTree tree;

    /**
     * Default constructor.
     */
    public TreePanel() {
    }

    /**
     * Constructor.
     */
    public TreePanel(IDataRow rootDataRow) {
        if (rootDataRow != null) {
            initGUI(rootDataRow);
        }
    }

    /**
     * Initializes the GUI.
     */
    public void initGUI(IDataRow rootDataRow) {
        this.setLayout(new RiverLayout());

        //Generate the tree.
        GenerateTree generateTree = new GenerateTree();
        tree = (JXTree) generateTree.createTree((TreeNodeImpl) rootDataRow, true);
        tree.setRolloverEnabled(true);
        tree.setHighlighters(new HighlighterPipeline());

        //Add the selection listener.
        //Show the details for that object in the right hand panel.
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent event) {
                Object source = event.getSource();
                if (source instanceof JTree) {
                    tree = (JXTree) source;
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                    if (node != null) {
                        IDataRow dataRow = (IDataRow) node.getUserObject();

                        List<IRecord> records = new ArrayList<IRecord>();
                        Cab2bPanel dataListDetailedPanel;

                        //If title node is selected show details of all the children.
                        if (!dataRow.isData()) {
                            for (int i = 0; i < node.getChildCount(); i++) {
                                DefaultMutableTreeNode childDefaultNode = (DefaultMutableTreeNode) node.getChildAt(i);
                                IDataRow childDataRow = (IDataRow) childDefaultNode.getUserObject();
                                records.add(childDataRow.getRecord());
                            }

                            dataListDetailedPanel = new DefaultDataListDetailedPanel(records);
                            dataListDetailedPanel.doInitialization();
                        } else {
                            //show details of the selected node only.
                            dataListDetailedPanel = ResultPanelFactory.getDataListDetailedPanel(dataRow.getRecord());
                        }

                        JSplitPane splitPane = (JSplitPane) getParent();
                        DataListDetailedPanelContainer detailsPanel = (DataListDetailedPanelContainer) splitPane.getRightComponent();
                        detailsPanel = new DataListDetailedPanelContainer(dataListDetailedPanel);
                        splitPane.setRightComponent(detailsPanel);
                    }
                }
            }
        });

        this.add("br hfill vfill", new JScrollPane(tree));
        this.setBorder(new CustomizableBorder(new Insets(1, 1, 1, 1), true, true));
    }

    /**
     * Method to set the default selection for the my data list tree
     * view if the tree contains valid data
     */
    public void selectTreeRoot() {
        tree.setRootVisible(false);
        // Finally set the appropriate values
        Object[] path = new Object[2];
        path[0] = tree.getModel().getRoot();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) (tree.getModel().getRoot());
        if (node.getChildCount() > 0) {
            path[1] = node.getFirstChild();
            tree.setSelectionPath(new TreePath(path));
            tree.expandPath(tree.getSelectionPath());
        }
    }

    /**
     * Method to set the selection for the my data list tree
     * view if the tree contains valid data
     */
    public void selectTreeRoot(final IDataRow row) {
        DefaultMutableTreeNode nodeFound = CommonUtils.searchNode(
                                                                  (DefaultMutableTreeNode) tree.getModel().getRoot(),
                                                                  row);
        if (nodeFound != null) {
            tree.setSelectionPath(new TreePath(nodeFound.getPath()));
            tree.expandPath(tree.getSelectionPath());
        }
    }
}