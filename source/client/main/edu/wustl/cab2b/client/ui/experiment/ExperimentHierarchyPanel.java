package edu.wustl.cab2b.client.ui.experiment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
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
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
/**
 * A panel to display experiment and experiment group hierarchies 
 * in a tree format, and an action button to create new experiment groups.
 *  
 * @author chetan_bh
 */
public class ExperimentHierarchyPanel extends Cab2bPanel
{
	
	JXTree expTree;
	JButton addNewButton;
	//private Icon customLeafIcon = new ImageIcon("images/experiment.ico");
	ExperimentDetailsPanel expDetailsPanel;
	
	public ExperimentHierarchyPanel(ExperimentDetailsPanel newExpDetailsPanel)
	{
		expDetailsPanel = newExpDetailsPanel;
		initGUI();
	}

	
	private void initGUI()
	{
		Logger.out.info("Inside experiment hirarchy model");
		this.setLayout(new RiverLayout());		
		Vector dataVector = null;
		
		// EJB code start
		BusinessInterface bus = null;
		try
		{
			bus = Locator.getInstance().locate(edu.wustl.cab2b.common.ejb.EjbNamesConstants.EXPERIMENT, ExperimentHome.class);
		}
		catch (LocatorException e1)
		{
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
		expTree = (JXTree) treeGenerator.createTree(dataVector,edu.wustl.common.util.global.Constants.EXPERIMETN_TREE_ID, true);
		expTree.setRolloverEnabled(true);
		expTree.setHighlighters(new HighlighterPipeline());		
		expTree.addTreeSelectionListener(new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent tse)
					{
						TreePath[] treePaths = tse.getPaths();
						JXTree tree = (JXTree)tse.getSource();

						Object userObject = ((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getUserObject();
						Logger.out.info("userObject ==>> "+userObject.getClass());
						if(userObject instanceof ExperimentTreeNode)
						{
							ExperimentTreeNode selectedExpTreeNode = (ExperimentTreeNode) userObject;
							if(selectedExpTreeNode != null)
							{
								expDetailsPanel.refreshDetails(selectedExpTreeNode);
							}
							Logger.out.info("ExperimentTreeNode id :"+selectedExpTreeNode.getIdentifier());
						}else if(userObject instanceof Experiment)
						{
							Logger.out.info("Experiment clicked, do some action");
							if(expDetailsPanel != null)
									expDetailsPanel.refreshDetails((Experiment)userObject);
						}else if(userObject instanceof ExperimentGroup)
						{
							Logger.out.info("ExperimentGroup clicked, do some action");
							if(expDetailsPanel != null)
									expDetailsPanel.refreshDetails((ExperimentGroup)userObject);
						}
					}					
				});		
		
		addNewButton = new Cab2bButton("Add New");
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
		
		this.add("br hfill vfill",new JScrollPane(expTree));
	}
	
	public static void main(String[] args)
	{
		ExperimentHierarchyPanel expHiePanel = new ExperimentHierarchyPanel(null);
		
		JFrame frame = new JFrame("Experiment");
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(expHiePanel);
		frame.setVisible(true);
	}
}
