package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecord;
import edu.common.dynamicextensions.entitymanager.EntityRecordInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.StackedBox;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
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
	
	/*ExperimentOpen Panel*/
	ExperimentDataCategoryGridPanel m_experimentDataCategoryGridPanel= null;
	
	JXTree datalistTree; 
	
	
	JScrollPane treeViewScrollPane;
	ExperimentBusinessInterface m_experimentBusinessInterface;
	Experiment m_selectedExperiment = null; 
	
	String columnName[] = null;
	Object recordObject[][] = null;

	
	public ExperimentStackBox(ExperimentBusinessInterface expBus,Experiment selectedExperiment)
	{
		m_experimentBusinessInterface = expBus;
		m_selectedExperiment = selectedExperiment ;
		initGUI();
	}	
	
	public ExperimentStackBox(ExperimentBusinessInterface expBus,Experiment selectedExperiment, ExperimentDataCategoryGridPanel experimentDataCategoryGridPanel)
	{
		m_experimentBusinessInterface = expBus;
		m_selectedExperiment = selectedExperiment ;
		m_experimentDataCategoryGridPanel = experimentDataCategoryGridPanel;
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
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Experiment Data Categories");
		DefaultMutableTreeNode node = null;		    

		while(iter.hasNext())
		{
			EntityInterface entity = (EntityInterface) iter.next();
			ExperimentEntity expEnity = new ExperimentEntity();
			expEnity.setEntityInterface(entity);
			node = new DefaultMutableTreeNode(expEnity);	
			//node.setUserObject(entity);
			rootNode.add(node);						
		}	
		
		//creating datalist tree
		datalistTree = new JXTree(rootNode);	
		datalistTree.addTreeSelectionListener(new TreeSelectionListener() {			
		public void valueChanged(TreeSelectionEvent e)  {
			treeSelectionListenerAction();
		}				
		});

		//Adding Select data category pane		
		treeViewScrollPane = new JScrollPane(datalistTree);
		stackedBox.addBox("Select Data Category", treeViewScrollPane,"resources/images/mysearchqueries_icon.gif");		
		
		//Adding Filter data category panel
		dataFilterPanel = new Cab2bPanel();
		dataFilterPanel.setPreferredSize(new Dimension(250, 150));
		dataFilterPanel.setOpaque(false);
		stackedBox.addBox("Filter Data ", dataFilterPanel,"resources/images/mysearchqueries_icon.gif");
				
		//Adding Analyse data  panel
		analyseDataPanel = new Cab2bPanel();
		analyseDataPanel.setPreferredSize(new Dimension(250, 150));
		analyseDataPanel.setOpaque(false);
		stackedBox.addBox("Analyze Data ", analyseDataPanel,"resources/images/mysearchqueries_icon.gif");
		
		stackedBox.setPreferredSize(new Dimension(250,500));
		stackedBox.setMinimumSize(new Dimension(250,500));
		this.add(stackedBox);
	}
	
	/**
    * Method to perform tree node selection action
    * for currently selected node 
    */

	public void treeSelectionListenerAction(){	
		
		 CustomSwingWorker swingWorker = new CustomSwingWorker(datalistTree)
	     {
				@Override
				protected void doNonUILogic() throws RuntimeException
				{
					 Logger.out.info("Clicked on datalist");
						DefaultMutableTreeNode node = (DefaultMutableTreeNode)datalistTree.getLastSelectedPathComponent();
						if (node == null)		
							return;
						
						//if rot node is selected, clear all table content 
						if(node.isRoot() == true)
						{
							m_experimentDataCategoryGridPanel.refreshTable(null,null); 
							return;
						}	
						
						Object nodeInfo = node.getUserObject();
						if (nodeInfo instanceof ExperimentEntity) {
							EntityInterface entityNode = ((ExperimentEntity)nodeInfo).getEntityInterface();  	
							entityNode.getAttributeCollection();
							Logger.out.info("ID :: "+entityNode.getId());		
				        	
							//getting datalist entity interface
							DataListBusinessInterface dataListBI = (DataListBusinessInterface) CommonUtils
							.getBusinessInterface(EjbNamesConstants.DATALIST_BEAN, DataListHome.class);
				        	try {
				        			//getting list of attributes
									EntityRecordResultInterface  recordResultInterface =  dataListBI.getEntityRecord(entityNode.getId());
				    				List<AbstractAttributeInterface>  headerList = recordResultInterface.getEntityRecordMetadata().getAttributeList();
				    				Iterator it = headerList.iterator();    
				    				columnName = new String[headerList.size()];	     			
				    				int i=0;		     			
				    				while(it.hasNext())
				    				{
				    					AbstractAttributeInterface attribute  = (AbstractAttributeInterface)it.next();		     			
				    					columnName[i++] = CommonUtils.getFormattedString(attribute.getName());		     					
				    					Logger.out.info("Table Header :" +attribute.getName());
				    				}  			
				    				
				        			//getting actual records
				    				List<EntityRecordInterface>  recordList = recordResultInterface.getEntityRecordList();
				    				it = recordList.iterator();
				    				recordObject = new Object[recordList.size()][headerList.size()];
				    				Logger.out.info("Record Size :: " + recordList.size());
				    				i=0;
				    				while(it.hasNext())
				    				{
				    					EntityRecordInterface record = 	(EntityRecordInterface) it.next();			
				    					List recordValueList = record.getRecordValueList();
				    					int j=0;
				    					Iterator iterList = recordValueList.iterator();		     				
				    					while(iterList.hasNext())
				    					{
				    						recordObject[i][j] = new Object();
				    						recordObject[i][j] = iterList.next();		     					
				    						Logger.out.info("Data ["+i+"]"+"["+j+"]"+recordObject[i][j]);
				    						j++;
				    					}
				    					i++;
				    				}  			
    				   			} catch (RemoteException e1) {					
								e1.printStackTrace();
							}	        		
						}			
						
				}
				protected void doUIUpdateLogic() throws RuntimeException
				{
					m_experimentDataCategoryGridPanel.refreshTable(columnName,recordObject);
				}    	
	     };
    swingWorker.start();
    
    /*Logger.out.info("Clicked on datalist");
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)datalistTree.getLastSelectedPathComponent();
			if (node == null)		
				return;
			
			//if rot node is selected, clear all table content 
			if(node.isRoot() == true)
			{
				m_experimentDataCategoryGridPanel.refreshTable(null,null); 
				return;
			}	
			
			Object nodeInfo = node.getUserObject();
			if (nodeInfo instanceof ExperimentEntity) {
				EntityInterface entityNode = ((ExperimentEntity)nodeInfo).getEntityInterface();  	
				entityNode.getAttributeCollection();
				Logger.out.info("ID :: "+entityNode.getId());		
	        	
				//getting datalist entity interface
				DataListBusinessInterface dataListBI = (DataListBusinessInterface) CommonUtils
				.getBusinessInterface(EjbNamesConstants.DATALIST_BEAN, DataListHome.class);
	        	try {
	        			//getting list of attributes
						EntityRecordResultInterface  recordResultInterface =  dataListBI.getEntityRecord(entityNode.getId());
	    				List<AbstractAttributeInterface>  headerList = recordResultInterface.getEntityRecordMetadata().getAttributeList();
	    				Iterator it = headerList.iterator();    
	    				columnName = new String[headerList.size()];	     			
	    				int i=0;		     			
	    				while(it.hasNext())
	    				{
	    					AbstractAttributeInterface attribute  = (AbstractAttributeInterface)it.next();		     			
	    					columnName[i++] = CommonUtils.getFormattedString(attribute.getName());		     					
	    					Logger.out.info("Table Header :" +attribute.getName());
	    				}  			
	    				
	        			//getting actual records
	    				List<EntityRecordInterface>  recordList = recordResultInterface.getEntityRecordList();
	    				it = recordList.iterator();
	    				recordObject = new Object[recordList.size()][headerList.size()];
	    				Logger.out.info("Record Size :: " + recordList.size());
	    				i=0;
	    				while(it.hasNext())
	    				{
	    					EntityRecordInterface record = 	(EntityRecordInterface) it.next();			
	    					List recordValueList = record.getRecordValueList();
	    					int j=0;
	    					Iterator iterList = recordValueList.iterator();		     				
	    					while(iterList.hasNext())
	    					{
	    						recordObject[i][j] = new Object();
	    						recordObject[i][j] = iterList.next();		     					
	    						Logger.out.info("Data ["+i+"]"+"["+j+"]"+recordObject[i][j]);
	    						j++;
	    					}
	    					i++;
	    				}
	    				m_experimentDataCategoryGridPanel.refreshTable(columnName,recordObject);
	    				updateUI();
	     			} catch (RemoteException e1) {					
					e1.printStackTrace();
				}	        		
			}			
		}*/	
	}
}
