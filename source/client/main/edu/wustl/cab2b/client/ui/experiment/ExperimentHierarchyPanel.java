package edu.wustl.cab2b.client.ui.experiment;

import java.awt.Component;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.common.experiment.ExperimentHome;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.common.tree.ExperimentTreeNode;
import edu.wustl.common.tree.GenerateTree;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;

/**
 * A panel to display experiment and experiment group hierarchies 
 * in a tree format, and an action button to create new experiment groups.
 *  
 * @author chetan_bh
 */
public class ExperimentHierarchyPanel extends Cab2bPanel {

    private static final long serialVersionUID = 1L;

    private JXTree expTree;

    private JButton addNewButton;

    private ExperimentDetailsPanel expDetailsPanel;

    public ExperimentHierarchyPanel(ExperimentDetailsPanel newExpDetailsPanel) {
        expDetailsPanel = newExpDetailsPanel;
        initGUI();
    }

    private void initGUI() {
        this.setLayout(new RiverLayout());
        Vector dataVector = null;
        try {
            ExperimentBusinessInterface expBus = (ExperimentBusinessInterface) Locator.getInstance().locate(
                                                                                                            edu.wustl.cab2b.common.ejb.EjbNamesConstants.EXPERIMENT,
                                                                                                            ExperimentHome.class);
            dataVector = expBus.getExperimentHierarchy();
        } catch (RemoteException e1) {
            CommonUtils.handleException(e1, this, true, true, false, false);
        } catch (ClassNotFoundException e1) {
            CommonUtils.handleException(e1, this, true, true, false, false);
        } catch (DAOException e1) {
            CommonUtils.handleException(e1, this, true, true, false, false);
        }

        GenerateTree treeGenerator = new GenerateTree();
        expTree = (JXTree) treeGenerator.createTree(dataVector, Constants.EXPERIMETN_TREE_ID, true);
        expTree.setCellRenderer(new MyRenderer());
        expTree.setRolloverEnabled(true);
        expTree.setHighlighters(new HighlighterPipeline());

        //setting tree node name
        expTree.setSelectionRow(0);
        ExperimentTreeNode treeNodeUserObj = (ExperimentTreeNode) ((DefaultMutableTreeNode) expTree.getSelectionPath().getPathComponent(
                                                                                                                                        0)).getUserObject();
        treeNodeUserObj.setName("My Projects");
        expTree.setSelectionRow(-1);
        expTree.updateUI();

        if (expTree.getRowCount() >= 2) {
            expTree.setSelectionRow(1);
            expTree.expandRow(1);
            Object userObject = ((DefaultMutableTreeNode) expTree.getLastSelectedPathComponent()).getUserObject();
            if (userObject instanceof ExperimentTreeNode) {
                ExperimentTreeNode selectedExpTreeNode = (ExperimentTreeNode) userObject;
                if (selectedExpTreeNode != null) {
                    expDetailsPanel.refreshDetails(selectedExpTreeNode);
                }
            }
        }
        expTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse) {
                JXTree tree = (JXTree) tse.getSource();

                Object userObject = ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject();

                if (userObject instanceof ExperimentTreeNode) {
                    ExperimentTreeNode selectedExpTreeNode = (ExperimentTreeNode) userObject;
                    if (selectedExpTreeNode != null) {
                        expDetailsPanel.refreshDetails(selectedExpTreeNode);
                    }
                } else if (userObject instanceof Experiment) {
                    if (expDetailsPanel != null)
                        expDetailsPanel.refreshDetails((Experiment) userObject);
                } else if (userObject instanceof ExperimentGroup) {
                    if (expDetailsPanel != null)
                        expDetailsPanel.refreshDetails((ExperimentGroup) userObject);
                }
            }
        });

        addNewButton = new Cab2bButton("Add New");
        addNewButton.setEnabled(false);
        this.add("br", addNewButton);

        /*JButton searchButton = new Cab2bButton("Search");
         searchButton.addActionListener(new ActionListener()
         {
         public void actionPerformed(ActionEvent e)
         {
         expTree.getActionMap().get("find").actionPerformed(null);
         }
         });
         this.add("tab",searchButton);*/

        this.add("br hfill vfill", new JScrollPane(expTree));
    }

    class MyRenderer extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = 8552472456633962811L;

        ClassLoader loader = this.getClass().getClassLoader();

        Icon expIcon = new ImageIcon(loader.getResource("experiment_small.gif"));

        Icon expEmpty = new ImageIcon(loader.getResource("empty_folder.ico"));

        Icon expGrpOpenIcon = new ImageIcon(loader.getResource("folder_opened.ico"));

        Icon expGrpClosedIcon = new ImageIcon(loader.getResource("folder_closed.ico"));

        public MyRenderer() {
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
                Object userObj = treeNode.getUserObject();

                ExperimentTreeNode expTreeNode = null;
                if (userObj instanceof ExperimentTreeNode)
                    expTreeNode = (ExperimentTreeNode) userObj;

                if (expTreeNode != null) {
                    if (expTreeNode.isExperimentGroup()) {
                        if (tree.isExpanded(row)) {
                            setIcon(UIManager.getIcon("Tree.openIcon"));
                        } else {
                            setIcon(UIManager.getIcon("Tree.closedIcon"));
                        }
                    } else {
                        setIcon(expIcon);
                    }
                } else {
                    //this condition to take care of newly created experiment group node.
                    setIcon(UIManager.getIcon("Tree.closedIcon"));
                }
            }
            return this;
        }
    }
}