package edu.wustl.cab2b.client.ui.experiment;

import java.awt.Component;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.common.experiment.ExperimentHome;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.cab2b.common.locator.LocatorException;
import edu.wustl.common.tree.ExperimentTreeNode;
import edu.wustl.common.tree.GenerateTree;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * A panel to display experiment and experiment group hierarchies 
 * in a tree format, and an action button to create new experiment groups.
 *  
 * @author chetan_bh
 */
public class ExperimentHierarchyPanel extends Cab2bPanel {

    JXTree expTree;

    JButton addNewButton;

    //private Icon customLeafIcon = new ImageIcon("images/experiment.ico");
    ExperimentDetailsPanel expDetailsPanel;

    public ExperimentHierarchyPanel(ExperimentDetailsPanel newExpDetailsPanel) {
        expDetailsPanel = newExpDetailsPanel;
        initGUI();
    }

    private void initGUI() {
        Logger.out.debug("Inside experiment hirarchy model");
        this.setLayout(new RiverLayout());
        Vector dataVector = null;

        // EJB code start
        BusinessInterface bus = null;
        try {
            bus = Locator.getInstance().locate(edu.wustl.cab2b.common.ejb.EjbNamesConstants.EXPERIMENT,
                                               ExperimentHome.class);
        } catch (LocatorException e1) {
            CommonUtils.handleException(e1, this, true, true, false, false);
        }

        ExperimentBusinessInterface expBus = (ExperimentBusinessInterface) bus;
        try {
            dataVector = expBus.getExperimentHierarchy();
        } catch (RemoteException e1) {
            CommonUtils.handleException(e1, this, true, true, false, false);
        } catch (ClassNotFoundException e1) {
            CommonUtils.handleException(e1, this, true, true, false, false);
        } catch (DAOException e1) {
            CommonUtils.handleException(e1, this, true, true, false, false);
        }

        // EJB code end		
        GenerateTree treeGenerator = new GenerateTree();
        expTree = (JXTree) treeGenerator.createTree(dataVector,
                                                    edu.wustl.common.util.global.Constants.EXPERIMETN_TREE_ID,
                                                    true);
        
        
        expTree.setCellRenderer(new MyRenderer());
        expTree.setRolloverEnabled(true);
        expTree.setHighlighters(new HighlighterPipeline());
        
        
        
        //setting tree node name
        expTree.setSelectionRow(0);
        ExperimentTreeNode treeNodeUserObj = (ExperimentTreeNode)((DefaultMutableTreeNode) expTree.getSelectionPath().getPathComponent(0)).getUserObject();
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
                Logger.out.debug("ExperimentTreeNode id :" + selectedExpTreeNode.getIdentifier());
            }
        }
        expTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse) {
                TreePath[] treePaths = tse.getPaths();
                JXTree tree = (JXTree) tse.getSource();

                Object userObject = ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject();
                Logger.out.info("userObject ==>> " + userObject.getClass());
                if (userObject instanceof ExperimentTreeNode) {
                    ExperimentTreeNode selectedExpTreeNode = (ExperimentTreeNode) userObject;
                    if (selectedExpTreeNode != null) {
                        expDetailsPanel.refreshDetails(selectedExpTreeNode);
                    }
                    Logger.out.info("ExperimentTreeNode id :" + selectedExpTreeNode.getIdentifier());
                } else if (userObject instanceof Experiment) {
                    Logger.out.info("Experiment clicked, do some action");
                    if (expDetailsPanel != null)
                        expDetailsPanel.refreshDetails((Experiment) userObject);
                } else if (userObject instanceof ExperimentGroup) {
                    Logger.out.info("ExperimentGroup clicked, do some action");
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

        Icon expIcon = new ImageIcon("resources/images/experiment_small.gif");

        Icon expEmpty = new ImageIcon("resources/images/empty_folder.ico");

        Icon expGrpOpenIcon = new ImageIcon("resources/images/folder_open.ico");

        Icon expGrpClosedIcon = new ImageIcon("resources/images/folder_closed.ico");

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
                } else
                // this condition to take care of newly created experiment group node.
                {
                    setIcon(UIManager.getIcon("Tree.closedIcon"));
                }
            }
            return this;
        }
    }

    public static void main(String[] args) {
        ExperimentHierarchyPanel expHiePanel = new ExperimentHierarchyPanel(null);

        JFrame frame = new JFrame("Experiment");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(expHiePanel);
        frame.setVisible(true);
    }
}
