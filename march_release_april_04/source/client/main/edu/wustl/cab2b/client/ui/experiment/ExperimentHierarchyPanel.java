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
	
	private TreeModel getDummyExperimentHierarchy()
	{
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Experiments");
		TreeModel returner = new DefaultTreeModel(root);
		DefaultMutableTreeNode mouseExp = new DefaultMutableTreeNode("Mouse Experiments");
		DefaultMutableTreeNode humanExp = new DefaultMutableTreeNode("Human Experiments");
		
		DefaultMutableTreeNode mouseExp1 = new DefaultMutableTreeNode("Zometa Primary Tumor");
		DefaultMutableTreeNode mouseExp2 = new DefaultMutableTreeNode("NFKB1 vs. NFKB2");
		DefaultMutableTreeNode humanSNPExp = new DefaultMutableTreeNode("SNP Experiments");
		DefaultMutableTreeNode humanExp2 = new DefaultMutableTreeNode("Normal vs. Diseased Lever");
		DefaultMutableTreeNode humanExp3 = new DefaultMutableTreeNode("Tissue Experiment");
		
		mouseExp.add(mouseExp1);
		mouseExp.add(mouseExp2);
		
		humanExp.add(humanSNPExp);
		humanExp.add(humanExp2);
		humanExp.add(humanExp3);
		
		DefaultMutableTreeNode humanSNPExp1 = new DefaultMutableTreeNode("SNP Experiment 1");
		DefaultMutableTreeNode humanSNPExp2 = new DefaultMutableTreeNode("SNP Experiment 2");
		
		humanSNPExp.add(humanSNPExp1);
		humanSNPExp.add(humanSNPExp2);
		
		root.add(mouseExp);
		root.add(humanExp);		
		
		return returner;
	}
	
	private TreeModel getExperimentHierarchy()
	{
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Experiments");
		TreeModel returner = new DefaultTreeModel(root);
		
		Experiment exp1 = new Experiment()     { public String toString() {return getName();} };
			exp1.setName("Mouse SNP Experiment");
			exp1.setCreatedOn(new Date());
		Experiment exp2 = new Experiment()     { public String toString() {return getName();} };
			exp2.setName("Mouse Microarray Experiment");
			exp2.setCreatedOn(new Date());
			exp2.setDescription("Mu218Av mouse experiment on C11W cell lines");
			
		ExperimentGroup mouseExpGrp = new ExperimentGroup()   { public String toString() {return getName();} };
			mouseExpGrp.setName("Mouse Experiments");
		
		mouseExpGrp.getExperimentCollection().add(exp1);
		mouseExpGrp.getExperimentCollection().add(exp2);
		exp1.getExperimentGroupCollection().add(mouseExpGrp);
		exp2.getExperimentGroupCollection().add(mouseExpGrp);
		
		DefaultMutableTreeNode mouseExpGrpNode = new DefaultMutableTreeNode(mouseExpGrp);
		DefaultMutableTreeNode snpExpNode = new DefaultMutableTreeNode(exp1);
		DefaultMutableTreeNode microarrayExpNode = new DefaultMutableTreeNode(exp2);
		
		mouseExpGrpNode.add(snpExpNode);
		mouseExpGrpNode.add(microarrayExpNode);
		
		root.add(mouseExpGrpNode);
		
		return returner;
	}
	
	private void initGUI()
	{
		this.setLayout(new RiverLayout());
		//expTree = new JXTree(getDummyExperimentHierarchy());
		//ExperimentBizLogic expBizLogic = new ExperimentBizLogic();
		Vector dataVector = null;
//		try
//		{
//			dataVector = expBizLogic.getExperimentHierarchy();
//		}
//		catch (ClassNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//		catch (DAOException e)
//		{
//			e.printStackTrace();
//		}
		
		// EJB code start
		BusinessInterface bus = null;
		try
		{
			bus = Locator.getInstance().locate("Experiment", ExperimentHome.class);
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
		
//		expTree.setSearchable(new Searchable(){
//			
//		});
		//expTree = new JXTree(getExperimentHierarchy());
		
		expTree.addTreeSelectionListener(new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent tse)
					{
						TreePath[] treePaths = tse.getPaths();
						JXTree tree = (JXTree)tse.getSource();
						//for (int i = 0; i < treePaths.length; i++)
						//{
						//	Logger.out.info(treePaths[i].toString());
						//}
						//Logger.out.info(tree.getLastSelectedPathComponent().getClass());
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
		
		//Icon customLeafIcon = new ImageIcon("images/experiment.ico");
		//Logger.out.info("icon "+customLeafIcon.getIconHeight());
		//DefaultTreeCellRenderer renderer3 = new DefaultTreeCellRenderer();
	    //renderer3.setOpenIcon(customOpenIcon);
	    //renderer3.setClosedIcon(customClosedIcon);
	    //renderer3.setLeafIcon(customLeafIcon);
	    //expTree.setCellRenderer(renderer3);
		
		
		addNewButton = new Cab2bButton("Add New");
		this.add("br",addNewButton);
		
		JButton searchButton = new Cab2bButton("Search");
		searchButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						expTree.getActionMap().get("find").actionPerformed(null);
					}
				});
		this.add("tab",searchButton);
		
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
