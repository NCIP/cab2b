package edu.wustl.cab2b.client.ui.experiment;

import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

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
import static edu.wustl.cab2b.client.ui.util.ClientConstants.TREE_OPEN_FOLDER;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.TREE_CLOSE_FOLDER;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.EXPT_LEAF_NODE;

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
        this.setLayout(new RiverLayout(5,5));
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
        expTree.setRolloverEnabled(true);
        expTree.setHighlighters(new HighlighterPipeline());

        ClassLoader loader = this.getClass().getClassLoader();
        expTree.setOpenIcon(new ImageIcon(loader.getResource(TREE_OPEN_FOLDER)));
        expTree.setClosedIcon(new ImageIcon(loader.getResource(TREE_CLOSE_FOLDER)));
        expTree.setLeafIcon(new ImageIcon(loader.getResource(EXPT_LEAF_NODE)));

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
        this.add("br",addNewButton);

        /*JButton searchButton = new Cab2bButton("Search");
         searchButton.addActionListener(new ActionListener()
         {
         public void actionPerformed(ActionEvent e)
         {
         expTree.getActionMap().get("find").actionPerformed(null);
         }
         });
         this.add("tab",searchButton);*/
        JScrollPane js = new JScrollPane(expTree);
        js.setBorder(null);
        this.add("br hfill vfill", js);
    }
}