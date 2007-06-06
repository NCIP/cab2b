package edu.wustl.cab2b.client.ui.dag.ambiguityresolver;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.query.IPathFinder;


import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.logger.Logger;


/**
 * Class to resolve mabiguity between two categories
 * @author Administrator
 *
 */
public class ResolveAmbiguity 
{

	private Vector<AmbiguityObject> m_ambiguityObjects;
	int m_currentAmbigityIndex = 0;
	int m_totalAmbiguityObjects;
	Map<AmbiguityObject, List<IPath>> m_ambiguityObjectToPathsMap = new HashMap<AmbiguityObject, List<IPath>>();
	private IPathFinder m_pathFinder;
	
	private final int  GENERALPATH = 1, CURATEDPATH=2, SELECTEDPATH=3 ;	 
	public static int pathIdentity = 1; 
	
	public ResolveAmbiguity(Vector<AmbiguityObject> ambiguityObjects, IPathFinder pathFinder)
	{
		m_ambiguityObjects = ambiguityObjects;
		m_totalAmbiguityObjects = m_ambiguityObjects.size();
		m_pathFinder = pathFinder;
	}
	
	public ResolveAmbiguity(AmbiguityObject ambiguityObject, IPathFinder pathFinder)
	{
		m_ambiguityObjects = new Vector<AmbiguityObject>();
		m_ambiguityObjects.add(ambiguityObject);
		m_totalAmbiguityObjects = m_ambiguityObjects.size();
		m_pathFinder = pathFinder;
	}
	
	public Map<AmbiguityObject, List<IPath>> getPathsForAllAmbiguities()
	{
		for(int i=0; i<m_totalAmbiguityObjects; i++)
		{
			AmbiguityObject ambObj = m_ambiguityObjects.get(i);
			List<IPath> list = getPaths(ambObj.getSourceEntity(), ambObj.getTargetEntity());		
				
			if(pathIdentity == SELECTEDPATH)
			{
				//don't show the UI, select the default paths
				m_ambiguityObjectToPathsMap.put(ambObj, list);
			}else
			if((list != null) && (list.size()>1))
			{
				//User interface for selecting paths from a list
				List<IPath> selectedPaths = showAmbiguityResolverUI(ambObj, list);
				m_ambiguityObjectToPathsMap.put(ambObj, selectedPaths);				
			}else
			{
				m_ambiguityObjectToPathsMap.put(ambObj, list);	
			}	
		}
		return m_ambiguityObjectToPathsMap;		
	}
	
	/**
	 * Method to show ambiguity resolver UI
	 *
	 */
	private List<IPath> showAmbiguityResolverUI(AmbiguityObject ambObj, List<IPath> list)
	{
		// Panel to show
		AvailablePathsPanel availablePathsPanel;
		if(pathIdentity == CURATEDPATH)
		{
			availablePathsPanel = new AvailablePathsPanel(ambObj, list, true);
		}
		else
		{
			availablePathsPanel = new  AvailablePathsPanel(ambObj, list);
		}
		
		if(NewWelcomePanel.mainFrame == null)
		{
			Logger.out.debug("Frame object null");
		}
		WindowUtilities.showInDialog(NewWelcomePanel.mainFrame, 
									 availablePathsPanel, "Ambiguity resolver", new Dimension(600, 350), true, false);
		return availablePathsPanel.getUserSelectedpaths();
	}
	
	
	/**
	 * Method to get all possible paths for given source and destination entity
	 * @param sourceEntity
	 * @param destinationEntity
	 * @return
	 */
	private List<IPath> getPaths(EntityInterface sourceEntity, EntityInterface destinationEntity)
	{
		List<IPath> paths = new ArrayList<IPath>();
		List<IPath> selectedPaths =  new ArrayList<IPath>(); 
		List<IPath> curatedPaths =  new ArrayList<IPath>();
		 
		
		Set<ICuratedPath> allCuratedPaths = null;

		//checking curated paths
		allCuratedPaths = m_pathFinder.getCuratedPaths(sourceEntity, destinationEntity);		
		Logger.out.info("  getCuratedPaths() executed : " + allCuratedPaths.size());
		
		Iterator<ICuratedPath> it = allCuratedPaths.iterator();		
		while (it.hasNext()) 
		{			       
			Logger.out.info("Inside curated path While");
		     ICuratedPath iCuratedPaths = it.next();		     	 
		     if( iCuratedPaths.getPaths() != null )
			 {
			   	Set<IPath> path = iCuratedPaths.getPaths();		    	
			   	Iterator<IPath> pathIterator = path.iterator();
			   	while (pathIterator.hasNext())
			   	{
			   		if( iCuratedPaths.isSelected() )
			  	    {
			   			pathIdentity = SELECTEDPATH;
			   			Logger.out.info("Got selected path");
			   			selectedPaths.add(pathIterator.next());	
			   	    }else
					{
			   	    	Logger.out.info("Got curated path");
			   	    	pathIdentity = CURATEDPATH;
			   	    	curatedPaths.add(pathIterator.next());			   	    
					}
			     }	
		      }		    	 
		  }     	     
		
		 if(selectedPaths.size() > 0)
		 {
			return selectedPaths;
		 }else		
		 if( curatedPaths.size() > 0 )
		 {
			return curatedPaths;
		 }else		
		 {
			pathIdentity = GENERALPATH;
			paths = null;
			paths = m_pathFinder.getAllPossiblePaths(sourceEntity, destinationEntity);
			return paths;
		 }	
	 }
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
