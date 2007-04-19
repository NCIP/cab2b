package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXTree;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecord;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.StackedBox;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.viewresults.TreePanel;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.datalist.DataListHome;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.common.tree.ExperimentTreeNode;
import edu.wustl.common.util.logger.Logger;

/*
 *  Class used to display left hand side stack panel.
 * */

public class ExperimentStackBox extends Cab2bPanel{

	/**
	 * @param args
	 */
	
	
	/*panel to display data-list (flat structure PPT slide no :44 )*/
	Cab2bPanel dataCategoryPanel = null;
	
	/*panel to display filters on selected data-category*/ 
	Cab2bPanel dataFilterPanel = null;
	
	/*panel to display analysed data on selected data-category*/ 
	Cab2bPanel analyseDataPanel = null;	
	 
	Cab2bPanel visualiseDataPanel = null;	
	
	Cab2bPanel dataViewPanel = null;
	
	/*stack box to add all this panels*/
	StackedBox stackedBox;
	
	JXTree datalistTree; 
	
	ExperimentBusinessInterface m_experimentBusinessInterface;
	Experiment m_selectedExperiment = null; 
	
	public ExperimentStackBox(ExperimentBusinessInterface expBus,Experiment selectedExperiment)
	{
		m_experimentBusinessInterface = expBus;
		m_selectedExperiment = selectedExperiment ;
		initGUI();
	}	
	
	public void initGUI()
	{
		this.setLayout(new BorderLayout());
		stackedBox = new StackedBox();
		stackedBox.setTitleBackgroundColor(new Color(200, 200, 220));
		
		Set<EntityInterface>  entitySet = null;		
		try {
			entitySet = m_experimentBusinessInterface.getDataListEntityNames(m_selectedExperiment);
		} catch (RemoteException e) {
			e.printStackTrace();
		}		
		Iterator iter  = entitySet.iterator();
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("");
		DefaultMutableTreeNode node = null;		    

		while(iter.hasNext())
		{
			EntityInterface entity = (EntityInterface) iter.next();
			node = new DefaultMutableTreeNode(entity);
			rootNode.add(node);						
		}	
		
	/*	//creating datalist tree*/
		datalistTree = new JXTree(rootNode);
		datalistTree.addTreeSelectionListener(new TreeSelectionListener() {			
		public void valueChanged(TreeSelectionEvent e)  {/*
			Logger.out.info("Clicked on datalist");
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
		                           datalistTree.getLastSelectedPathComponent();
		        if (node == null) return;
		        Object nodeInfo = node.getUserObject();
		        if (nodeInfo instanceof EntityInterface) {
		        	EntityInterface entityNode = (EntityInterface)nodeInfo;  	
		        	entityNode.getAttributeCollection();
		        	entityNode.getId();		
		        	
		        	DataListBusinessInterface dataListBI = (DataListBusinessInterface) CommonUtils
					.getBusinessInterface(EjbNamesConstants.DATALIST_BEAN, DataListHome.class);
		        	try {
		        		List<EntityRecord> enityRecords =dataListBI.getEntityRecord(entityNode.getId());
		        		Iterator it = enityRecords.iterator(); 
		        		while(it.hasNext())
		        		{
		        			EntityRecord entityRec = (EntityRecord) it.next();
		        			
		        			Collection<AbstractAttributeInterface>  attributeCollection = entityNode.getAbstractAttributeCollection();
		        			Iterator attributIter = attributeCollection.iterator();
		        			while(attributIter.hasNext())
		        			{
		        				AbstractAttributeInterface attributInterface = (AbstractAttributeInterface) attributIter.next();
		        				Logger.out.info("Attribute :" + attributInterface +"Value :" + entityRec.getValue(attributInterface));
		        			}
		        		}
					} catch (RemoteException e11) {
						CheckedException checkedException = new CheckedException(e11.getMessage());							
						CommonUtils.handleException(checkedException, MainFrame.openExperimentWelcomePanel, true, true,
								true, false);						 
						e11.printStackTrace();						
					}
				}
			  */}				
		});

		/*Adding Select data category pane*/		
		JScrollPane treeView = new JScrollPane(datalistTree);		
		stackedBox.addBox("Select Data Category", treeView,"resources/images/mysearchqueries_icon.gif");		
		
		/*Adding Filter data category panel*/
		dataFilterPanel = new Cab2bPanel();
		dataFilterPanel.setPreferredSize(new Dimension(250, 150));
		dataFilterPanel.setOpaque(false);
		stackedBox.addBox("Filter Data ", dataFilterPanel,"resources/images/mysearchqueries_icon.gif");
				
		/*Adding Analyse data  panel*/
		analyseDataPanel = new Cab2bPanel();
		analyseDataPanel.setPreferredSize(new Dimension(250, 150));
		analyseDataPanel.setOpaque(false);
		stackedBox.addBox("Analyze Data ", analyseDataPanel,"resources/images/mysearchqueries_icon.gif");
		
		stackedBox.setPreferredSize(new Dimension(250,500));
		stackedBox.setMinimumSize(new Dimension(250,500));
		this.add(stackedBox);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
