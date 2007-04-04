package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXPanel;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.pagination.PageElement;
import edu.wustl.cab2b.client.ui.pagination.PageElementImpl;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface;
import edu.wustl.cab2b.common.ejb.path.PathFinderHomeInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.util.logger.Logger;

/**
 * This panel is a container panel for all different panel in simple view of search results.
 * This panel has a bread crumb panel for easy navigation. 
 * 
 * @author chetan_bh
 */
public class SimpleSearchResultBreadCrumbPanel extends Cab2bPanel
{
	
	HashMap<String, List<AttributeInterface>> mapResultLabel = new HashMap<String, List<AttributeInterface>>();
	
	public int panelCount = 0;
	
	/** A vector of display strings for bread crumbs hyperlink. */
	Vector<String> m_vBreadCrumbs = new Vector<String>();
	
	int m_iBreadCrumbCount = 0;
	
	/**
	 * The breadcrumPanel common to all cards.
	 */
	JXPanel m_breadCrumbPanel = null;
	
	/**
	 * The panel will house cards, where each card is a panel depicting some details on the results.
	 */
	JXPanel m_resultsPanel = null;
	
	/** Query result object. */
	IQueryResult queryResult;
	
	String currentBreadCrumb = "Patients";
	
	/**
	 * Action listener for bread crumbs hyperlink.
	 */
	ActionListener breadCrumbsAL;
	
	/**
	 * Pagination components hyperlinks action listener.
	 */
	ActionListener hyperlinkAL;
	
	/**
	 * Associated Data hyperlinks action listener;
	 */
	ActionListener associatedDataAL;
	
	ViewSearchResultsPanel viewPanel;
	
	List<AttributeInterface> attributeList = null;
	
	public SimpleSearchResultBreadCrumbPanel(IQueryResult queryResult,ViewSearchResultsPanel viewPanel)
	{
		this.viewPanel  = viewPanel;
		this.queryResult = queryResult;
		initData();
		initGUI(viewPanel);
	}
	
	/** Initialize data. */
	private void initData()
	{
		
	}
	
	public void addPanel(JXPanel panel, String panelName)
	{
		this.m_resultsPanel.add(panel, panelName);
	}
	
	/** Shows a panel represented by a name, by bringing that panel to the top. */
	public void showPanel(String panelName)
	{
		Logger.out.info("panel name : "+panelName);
		CardLayout layout = (CardLayout)this.m_resultsPanel.getLayout();
		layout.show(this.m_resultsPanel, panelName);	
		int totalCardPanels = m_resultsPanel.getComponentCount();
		/**
		 * have to add My Data list summary panel
		 */
		for(int i=0; i<totalCardPanels; i++)
		{
			Component comp = m_resultsPanel.getComponent(i);
			if(true == comp.isVisible())
			{
				if(comp instanceof ViewSearchResultsSimplePanel)
				{
					ViewSearchResultsSimplePanel showingPanel = (ViewSearchResultsSimplePanel)m_resultsPanel.getComponent(i);
					showingPanel.addDataSummaryPanel();
					break;
				}
				else
				if(comp instanceof ResultObjectDetailsPanel)
				{
					ResultObjectDetailsPanel showingPanel = (ResultObjectDetailsPanel)m_resultsPanel.getComponent(i);
					showingPanel.addDataSummaryPanel();
					break;
				}
			}
		}
	}
	
	/** Addes a new panel to the top of the stack of existing breadcrumbs panels. */
	public void addBreadCrumbPanel(JXPanel panel, String panelName)
	{
		this.m_breadCrumbPanel.add(panel, panelName);
	}
	
	/** @see showPanel(String panelName) */
	public void showBreadcrumbPanel(String panelName)
	{		
		CardLayout layout = (CardLayout)this.m_breadCrumbPanel.getLayout();
		layout.show(this.m_breadCrumbPanel, panelName);
	}
	
	
	public List<AttributeInterface> getAttributes()
	{
		return this.attributeList;
	}
	
	
	public IQueryResult getQueryResult()
	{
		return queryResult;
	}
	
	private JXPanel updateGuiIfResultOneOrZero(IAssociation association, final DataRow parentDataRow)
	{
		//Check if you got only one record
		JXPanel simpleSearchResultPanel =  null;
        Map<String, String[][]> allRecords = queryResult.getAllRecords();
		Iterator ittr = allRecords.keySet().iterator();
		if (ittr.hasNext())
		{
				String urlKey = (String) ittr.next();
				Object[][] results = allRecords.get(urlKey);	
				String className = edu.wustl.cab2b.common.util.Utility.getDisplayName(queryResult.getAttributes().get(0).getEntity());				
				Logger.out.info("Result Length :" + results.length);
				
				if( results.length == 0 )
				{
					simpleSearchResultPanel = new Cab2bPanel();  
					Cab2bLabel noResultFoundErroLabel = new Cab2bLabel("No results found for Class " + className);
					simpleSearchResultPanel.add(noResultFoundErroLabel);					
				}
				else if( results.length == 1 )
				{
					//create data row			
					/*if (className == null || className.length() == 0)
					{
							 Get the class name from the attributes, if the above is not set on the server.
							className = this.getClassNameFromIattribute();
					}*/

						/* Initialize the count for number of attributes to be shown in the */
						int attributeSize = queryResult.getAttributes().size();
						int attributeLimitInDescStr = (attributeSize < 5) ? attributeSize : 5;			
						
						Object[] row = results[0];
						PageElement element = new PageElementImpl();
						//element.setDisplayName(className + "_" + (1));
						element.setDisplayName(className );
						String descStr = "";
						for (int j = 0; j < attributeLimitInDescStr; j++)
						{
							if (row[j] != null)
							{
								if (j == attributeLimitInDescStr - 1)
								{
									descStr += row[j];
								}
								else
								{
									descStr += row[j] + ", ";
								}
							}
						}
						element.setDescription(descStr);

						DataRow dataRow = new DataRow();
						List<AttributeInterface> attributes = queryResult.getAttributes();
						
						AttributeInterface attrib = attributes.get(0);
						/*
						 * Get the EntityInterface from the map only if the last parameter is null. 
						 * This should ideally happen only the first time
						 */
						EntityInterface presentEntityInterface = null;						
						presentEntityInterface = attrib.getEntity();
						String strclassName = edu.wustl.cab2b.common.util.Utility.getDisplayName(presentEntityInterface);

						int identifierIndex = CommonUtils.getIdAttributeIndexFromAttributes(attributes);
						Object id = row[identifierIndex];
						dataRow.setRow(row);
						dataRow.setAttributes(attributes);
						Logger.out.info("Class Name if result one or zero:"+strclassName);
						dataRow.setClassName(strclassName);
						dataRow.setParent(parentDataRow);
						dataRow.setId(id);
						dataRow.setAssociation(association);
						dataRow.setEntityInterface(presentEntityInterface);
						dataRow.setURL(urlKey);				
						Vector interIntraAccoClassCol = getInterIntraAssociatedObjectsCollection(dataRow);
						
						simpleSearchResultPanel = new ResultObjectDetailsPanel(dataRow, queryResult.getAttributes(), breadCrumbsAL, associatedDataAL, viewPanel, interIntraAccoClassCol);							
			    	}					
				}
		return simpleSearchResultPanel;
	}
	
	/** Initialize GUI. */
	private void initGUI(ViewSearchResultsPanel viewPanel)
	{
		this.setLayout(new RiverLayout());
		
		breadCrumbsAL = new BreadCrumbActionListener(this);
		hyperlinkAL = new HyperlinlActionListener(this);
		associatedDataAL = new AssociatedDataActionListener(this);
		
		//Check if you got only one record
		JXPanel simpleSearchResultPanel;
		simpleSearchResultPanel = updateGuiIfResultOneOrZero(null, null);	
		
		if(simpleSearchResultPanel == null)		
		{
			simpleSearchResultPanel = new ViewSearchResultsSimplePanel(null, queryResult, breadCrumbsAL, hyperlinkAL, null ,viewPanel,null);									
		 }
		
		/*
		 * The breadcrumb panel should be common to all cards, and hence should not be part of any card.
		 */
		
		String key = this.panelCount+"#"+this.getClassNameFromIattribute();
		this.m_vBreadCrumbs.add(key);
		Logger.out.info("key   :" + key);
		/*
		 * Put the list of attributes into the map and associate a key with it.
		 * The key is the userObject that sits behind the hyperlinks in the
		 * bread-crumbs.
		 */
		this.mapResultLabel.put(key, queryResult.getAttributes());
		/*Set also directly the attribute list*/
		this.attributeList = queryResult.getAttributes();
		
		this.m_breadCrumbPanel = new Cab2bPanel();
		this.m_breadCrumbPanel.setLayout( new CardLayout());		
		BreadcrumbPanel breadCrumbPanel = new BreadcrumbPanel(breadCrumbsAL, m_vBreadCrumbs);
		this.m_breadCrumbPanel.add(breadCrumbPanel, ""+this.panelCount);
		//this.m_breadCrumbPanel.setPreferredSize(new Dimension(1150, 20));
		
		/*
		 * Initialize the panel that will house all cards, and add the first card.
		 */
		this.m_resultsPanel = new Cab2bPanel();
		this.m_resultsPanel.setLayout( new CardLayout());
		this.m_resultsPanel.add(simpleSearchResultPanel,""+this.panelCount );		
		
		/* Add the main result panel to the current panel.*/
		this.add("hfill", this.m_breadCrumbPanel);
		this.add("br vfill hfill", this.m_resultsPanel);
	  }
	
	private String getClassNameFromIattribute()
	{
		String strClassName = null;
		List attributes = queryResult.getAttributes();
		if(attributes != null && attributes.size() >0){
			
            AttributeInterface attribute = (AttributeInterface)attributes.get(0);
			//strClassName = edu.wustl.common.util.Utility.parseClassName(attribute.getEntity().getName());	
			strClassName  = edu.wustl.cab2b.common.util.Utility.getDisplayName(attribute.getEntity());
		}
		return strClassName;
	}
	
	/** Gets bread-crumbs hyperlink action listener. */
	public ActionListener getBreadCrumbsAL()
	{
		return breadCrumbsAL;
	}

	/** Sets bread-crumbs hyperlink action listener. */
	public void setBreadCrumbsAL(ActionListener breadCrumbsAL)
	{
		this.breadCrumbsAL = breadCrumbsAL;
	}

	/** Gets pagination elements hyperlink action listener. */
	public ActionListener getHyperlinkAL()
	{
		return hyperlinkAL;
	}

	/** Sets pagination elements hyperlink action listener. */
	public void setHyperlinkAL(ActionListener hyperlinkAL)
	{
		this.hyperlinkAL = hyperlinkAL;
	}
	
	/** Gets associated data hyperlink action listener. */
	public ActionListener getAssociatedDataAL()
	{
		return associatedDataAL;
	}
	
	/** Sets associated data hyperlinks action listner. */
	public void setAssociatedDataAL(ActionListener associatedDataAL)
	{
		this.associatedDataAL = associatedDataAL;
	}
	
	private Vector getInterIntraAssociatedObjectsCollection(DataRow dataRow)
	{
		/* Get all the incoming intramodel associations. */
	    PathFinderBusinessInterface busInt = (PathFinderBusinessInterface) CommonUtils.getBusinessInterface(
	                                                                               "edu.wustl.cab2b.server.ejb.path.PathFinderBean",
	                                                                                PathFinderHomeInterface.class,
	                                                                                null);
	    
	    Vector interIntraColVector = new Vector();
	    Collection incomingIntraModelAssociationCollection = null;
	    Collection interModelAssociationCollection = null;
	    
		EntityInterface parentEntityInterface = dataRow.getEntityInterface();
		try{
		  
	      incomingIntraModelAssociationCollection = busInt.getIncomingIntramodelAssociations(parentEntityInterface.getId());
	      
	      interModelAssociationCollection = busInt.getInterModelAssociations(parentEntityInterface.getId());
	      
		  }catch(RemoteException re)
		  {
			CommonUtils.handleException(re, viewPanel, true, true, false, false);
		  }
		  
		  interIntraColVector.add(interModelAssociationCollection);
		  interIntraColVector.add(incomingIntraModelAssociationCollection);
		  
		  return interIntraColVector;
	}
	
	/**
	 * This action listener class should take care of creating new/existinf panels in the 
	 * Card layout.
	 * 
	 * @author chetan_bh
	 */
	private class BreadCrumbActionListener implements ActionListener
	{
		SimpleSearchResultBreadCrumbPanel breadCrumbPanel;
		
		public BreadCrumbActionListener(SimpleSearchResultBreadCrumbPanel panel)
		{
			this.breadCrumbPanel = panel;
		}
		
		public void actionPerformed(ActionEvent evt)
		{
			Logger.out.info("bread crumbs ActionListener : "+evt.getActionCommand());
			Cab2bHyperlink hyperlink = (Cab2bHyperlink)evt.getSource();
			/* Get the user object for the hyperlink. This contains the whole string.*/
			String hyperlinkText = (String)hyperlink.getUserObject();
			
			/*Set the attribute list if user clicked on a class.*/
			List<AttributeInterface> attrList = breadCrumbPanel.mapResultLabel.get(hyperlinkText);
			if(attrList != null)
			{
				breadCrumbPanel.attributeList = attrList;
			}
			int i;
			for(i = 0; i < m_vBreadCrumbs.size(); i++)
			{
				String strVectorValue = (String)m_vBreadCrumbs.get(i);				
				boolean blnEval = (strVectorValue.trim().equals(hyperlinkText.trim()));								 				
				if(blnEval)
				{					
					break;
				}
			}
			i++;
			Logger.out.info("i :> "+i);
			
			Logger.out.info("size :> " + m_vBreadCrumbs.size());
			for(int j = i; j< m_vBreadCrumbs.size(); j++)
			{
				m_vBreadCrumbs.remove(j);
				j=j-1;
			}
			
			/*Get the number from the hyperlink text and use that to show the panel.*/
			String strIndex = hyperlinkText.substring(0,hyperlinkText.indexOf("#"));
			Logger.out.info("INDEX FOR PANEL : "+strIndex);
			
		    breadCrumbPanel.showPanel(strIndex);
		    BreadcrumbPanel breadcrumbPanel = new BreadcrumbPanel(breadCrumbPanel.getBreadCrumbsAL(), m_vBreadCrumbs);
	        this.breadCrumbPanel.addBreadCrumbPanel(breadcrumbPanel, strIndex);            
	        this.breadCrumbPanel.showBreadcrumbPanel(strIndex);		    
		}
	}
	
	/**
	 * This action listener class should take care of creating new/existing panels in the 
	 * Card layout.
	 * 
	 * @author chetan_bh
	 */
	private class HyperlinlActionListener implements ActionListener
	{
		SimpleSearchResultBreadCrumbPanel breadCrumbPanel;
		
		//private boolean blnAddingFirstTime = true;
		
		public HyperlinlActionListener(SimpleSearchResultBreadCrumbPanel panel)
		{
			this.breadCrumbPanel = panel;
		}
		
		public void actionPerformed(ActionEvent evt)
		{			
			Cab2bHyperlink  hyperlink = (Cab2bHyperlink) evt.getSource();
			Object userObj = hyperlink.getUserObject();
			Logger.out.info("userObj "+userObj.getClass());
			
			PageElement element = (PageElement)userObj;
			
			/*Get the data row corresponding to the clicked element.*/
			final DataRow dataRow = (DataRow) element.getUserObject();
			
			/*Get the attributes for the last query fired.*/
			final List<AttributeInterface> attrList = breadCrumbPanel.getAttributes();
			Logger.out.info("attributes "+attrList);
			String hyperlinkText = hyperlink.getText();	
				  
            /*
			 * Refresh the breadcrumb vector, and pass that instance onto a new
			 * instance of the breadcrumb panel.
			 */
           //SimpleSearchResultBreadCrumbPanel.m_vBreadCrumbs.add(hyperlinkText);
		   (breadCrumbPanel.panelCount)++;
		   final int currentCount = breadCrumbPanel.panelCount;
		   
		   Logger.out.info("CURRENT COUNT : "+currentCount);
		   m_vBreadCrumbs.add(currentCount+"#"+hyperlinkText);
		   Logger.out.info("VECT SIZE : "+m_vBreadCrumbs.size());
           BreadcrumbPanel breadcrumbPanel1 = new BreadcrumbPanel(breadCrumbPanel.getBreadCrumbsAL(),m_vBreadCrumbs);
           
           //breadcrumbPanel1.setPreferredSize(new Dimension(900,20));           
           this.breadCrumbPanel.addBreadCrumbPanel(breadcrumbPanel1,""+currentCount);            
           this.breadCrumbPanel.showBreadcrumbPanel(""+currentCount);
			
			// ----------- Swing worker thread ------------
           CustomSwingWorker sw = new CustomSwingWorker(viewPanel)
           {

	           	Vector interIntraAcssoClassColl;
	           	
	   			@Override
	   			protected void doNonUILogic() throws RuntimeException
	   			{
	   				interIntraAcssoClassColl = getInterIntraAssociatedObjectsCollection(dataRow);
	   			}

				@Override
				protected void doUIUpdateLogic() throws RuntimeException
				{
					// TODO can also pass queryResult object instead of first two parameters.
					ResultObjectDetailsPanel detailsPanel = new ResultObjectDetailsPanel(dataRow, attrList, 																				  
																						 breadCrumbPanel.getBreadCrumbsAL(), 
																						 breadCrumbPanel.getAssociatedDataAL(),
																						 breadCrumbPanel.viewPanel,interIntraAcssoClassColl
																						 );			
					breadCrumbPanel.addPanel(detailsPanel,""+currentCount);            
		            breadCrumbPanel.showPanel(""+currentCount);
				}
           };
           sw.start();
           
		}
	}
	
	/**
	 * This action listener is to handle actions on associated data hyperlinks.
	 * 
	 * @author chetan_bh
	 */
	private class AssociatedDataActionListener implements ActionListener
	{
		SimpleSearchResultBreadCrumbPanel breadCrumbPanel;
		
		public AssociatedDataActionListener(SimpleSearchResultBreadCrumbPanel panel)
		{
			this.breadCrumbPanel = panel;
		}
		
		// TODO This action listener should take care of Creating QueryObject
		// and firing the query using QueryExecutor.
		public void actionPerformed(ActionEvent ae)
		{
			/* Get the source for the event.*/
			Cab2bHyperlink hyperlink = (Cab2bHyperlink) ae.getSource();			
			Object sourceUserObject = hyperlink.getUserObject();
			/* Get the user object associated with that source.*/
			final Vector userDataVector = (Vector) sourceUserObject;
			
			/* Get the target entity.*/			 
			final EntityInterface targetEntity = (EntityInterface) userDataVector.get(1);
			
			/* The text to be added to the bread-crumb.*/
			final String breadCrumbText = edu.wustl.cab2b.common.util.Utility.getDisplayName(targetEntity);
			
			Vector attributeInfo = (Vector) userDataVector.get(2); // will contain [IAttribute, predicate(String), value(will be id)(String)]
			
			final ClientQueryBuilder queryObject = new ClientQueryBuilder();
			
			
			int size = 1;
			/*Create the objects needed for adding the rule based on the source.*/
			List<AttributeInterface> attributes = new ArrayList<AttributeInterface>(size);
            List<String> operators = new ArrayList<String>(size);
            List<String> firstValues = new ArrayList<String>(size);
            List<String> secondValues = new ArrayList<String>(size);
			
            
            attributes.add((AttributeInterface)attributeInfo.get(0));
            Logger.out.info("0 : " + (AttributeInterface)attributeInfo.get(0));
            operators.add((String)attributeInfo.get(1));
            Logger.out.info("1 : " + (String)attributeInfo.get(1));
            firstValues.add(attributeInfo.get(2).toString());
            Logger.out.info("2 : " + attributeInfo.get(2).toString());
            secondValues.add(null);
            
            Logger.out.info("before adding Rule");
            IExpressionId sourceExpressionID = queryObject.addRule(attributes, operators, firstValues, secondValues);
			Logger.out.info("added Rule "+queryObject);
	
			/* Get the source expression id. Needed to add the path.*/
			IExpressionId targetExpressionID = queryObject.createDummyExpression(targetEntity);
			
			final IAssociation association = (IAssociation)userDataVector.get(4);
			
			final DataRow parentDataRow = (DataRow)userDataVector.get(3);
			
			if(association instanceof IIntraModelAssociation)
			{			
				/*Get the association from the fourth element.*/			
				IIntraModelAssociation intraModelAssociation = (IIntraModelAssociation)userDataVector.get(4);			
				try
				{
					queryObject.addAssociation(targetExpressionID, sourceExpressionID,intraModelAssociation);
				}
				catch(CyclicException exCyclic)
				{
					exCyclic.printStackTrace();
				}
				queryObject.setOutputForQueryForSpecifiedURL( targetEntity,parentDataRow.getURL());
			}
			else if(association instanceof IInterModelAssociation)
			{
				
				IInterModelAssociation interaModelAssociation = (IInterModelAssociation)userDataVector.get(4);
				try
				{
					queryObject.addAssociation(targetExpressionID, sourceExpressionID,interaModelAssociation.reverse());
				}
				catch(CyclicException exCyclic)
				{
					exCyclic.printStackTrace();
				}				
				queryObject.setOutputForQuery(targetEntity);
				
			}
			
		    /* Get result by executing the Query in a worker thread. */
            CustomSwingWorker swingWorker = new CustomSwingWorker((JXPanel)breadCrumbPanel, queryResult)
            {
				@Override
				protected void doNonUILogic() throws RuntimeException
				{
					queryResult = CommonUtils.executeQuery((ICab2bQuery)queryObject.getQuery(),(JComponent)breadCrumbPanel);
					Logger.out.info("queryResult "+queryResult.getAllRecords().values());
				}

				@Override
				protected void doUIUpdateLogic() throws RuntimeException
				{
					breadCrumbPanel.panelCount++;
		            int currentCount = breadCrumbPanel.panelCount;
		            /*Get attributes and set in map, for later when the user is navigating, and for now directly set it.*/
		            breadCrumbPanel.mapResultLabel.put(currentCount+"#"+breadCrumbText,queryResult.getAttributes());
		            breadCrumbPanel.attributeList = queryResult.getAttributes();
		            
		            /*
		             * Add a new instance of ViewSearchResultsSimplePanel.
		             */
		            //Check if you got only one record
		    		JXPanel simpleSearchResultPanelNew;
		    		simpleSearchResultPanelNew = updateGuiIfResultOneOrZero(association, parentDataRow);
		    		if(simpleSearchResultPanelNew == null)		
		    		{
		    			simpleSearchResultPanelNew = new ViewSearchResultsSimplePanel(association, queryResult, breadCrumbsAL, hyperlinkAL, parentDataRow ,viewPanel,targetEntity);									
		    		 }
		             
		            breadCrumbPanel.addPanel(simpleSearchResultPanelNew,""+currentCount);            
		            breadCrumbPanel.showPanel(""+currentCount);
		   
		            /*
		             * Also we need to refresh the breadcrumb panel,so do the same with the breadcrumb panel.
		             */
		           m_vBreadCrumbs.add(currentCount+"#"+breadCrumbText);
		           BreadcrumbPanel breadcrumbPanel = new BreadcrumbPanel(breadCrumbPanel.getBreadCrumbsAL(),m_vBreadCrumbs);
		           
		           breadCrumbPanel.addBreadCrumbPanel(breadcrumbPanel,""+currentCount);            
		           breadCrumbPanel.showBreadcrumbPanel(""+currentCount);
				}
            };
            swingWorker.start();
		}
	}
	
}
