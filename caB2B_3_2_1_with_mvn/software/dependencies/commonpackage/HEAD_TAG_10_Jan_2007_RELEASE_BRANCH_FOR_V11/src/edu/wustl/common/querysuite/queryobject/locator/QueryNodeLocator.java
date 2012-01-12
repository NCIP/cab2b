/**
 * 
 */
package edu.wustl.common.querysuite.queryobject.locator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IJoinGraph;
import edu.wustl.common.querysuite.queryobject.IQuery;


/**
 * To locate the DAG nodes, so that they will appear on the Dag As Sirected routed aCyclic graph.
 * @author prafull_kadam
 *
 */
public class QueryNodeLocator
{
	List<Integer> maxNodeAtLevel;
	List<List<Integer>> visibleExpListLevelWize;
	
	private int maxX;
	private IConstraints constraints;
	private IJoinGraph graph;
	private Map<Integer, Position> positionMap;
	private final static int X_OFFSET = 10;
	private final static int WIDTH_OF_NODE = 220;
	
	/**
	 * Constructor to instanciate the Object.
	 * @param maxX The Max X cordinate.
	 * @param maxY The Max Y coordinate.
	 * @param query The reference to the Query Object.
	 */
	public QueryNodeLocator(int maxX, IQuery query)
	{
		this.maxX = maxX; 
		constraints = query.getConstraints();
		graph = constraints.getJoinGraph();
		int noOfRoots = countNodeAtLevel();
		if (noOfRoots==1)
			createPositionMap();
//		for (int level =0;level < maxNodeAtLevel.size();level++)
//		{
//			List<IExpressionId> list = visibleExpListLevelWize.get(level);
//			System.out.println("Level:"+level+":"+list);
//		}
	}
	
	/**
	 * To get the Map of the visible nodes verses the x & y positions.
	 * @return Map of the visible nodes verses the x & y positions.
	 */
	public Map<Integer, Position>  getPositionMap()
	{
		return positionMap;
	}
	/**
	 * This method will create position Map.
	 *
	 */
	private void createPositionMap()
	{
		positionMap = new HashMap<Integer, Position>();
		int x = X_OFFSET;
		for (int level =0;level < maxNodeAtLevel.size();level++)
		{
			List<Integer> list = visibleExpListLevelWize.get(level);
			int nodesAtThisLevel = list.size() ;
			int yDiff = (maxX)/(nodesAtThisLevel+1);
			int y = yDiff;
			for(Integer expId:list)
			{
				positionMap.put(expId, new Position(x,y));
				y+=yDiff;
			}
			x+=WIDTH_OF_NODE;
		}
	}
	
	/**
	 * This method will count visible node at each level.
	 * @throws MultipleRootsException
	 */
	private int countNodeAtLevel()
	{
		List<IExpression> allRootExprs = graph.getAllRoots();
        List<Integer> allRoots = idsList(allRootExprs);
		maxNodeAtLevel = new ArrayList<Integer>();
		
		int size = allRoots.size();
		if (size==1)
		{
			visibleExpListLevelWize = new ArrayList<List<Integer>>();
			maxNodeAtLevel.add(1);
			addToVisibleMap(allRoots.get(0),1);
			process(allRoots.get(0),1);
		}
		else
		{
			positionMap = new HashMap<Integer, Position>();
			int yIncrement = X_OFFSET*6;
			int y = yIncrement;
			int x = X_OFFSET;
			for(IExpression expression : constraints)
			{
				if (expression.isVisible())
				{
					positionMap.put(expression.getExpressionId(), new Position(x,y));
					x+=WIDTH_OF_NODE/2;
					y+=yIncrement;
				}
			}
		}
		return size;
	}
	
	/**
	 * To add the node in visible node list.
	 * @param expId expression Id
	 * @param level The level of the node in the Graph.
	 */
	private void addToVisibleMap(Integer expId, int level)
	{
		List<Integer> list=null;
		if (level <= visibleExpListLevelWize.size()-1)
		{
			list = visibleExpListLevelWize.get(level);
		}
		else
		{
			list = new ArrayList<Integer>();
			visibleExpListLevelWize.add(list);
		}
		list.add(expId);
	}
	/**
	 * To Process the expression node with the given level.
	 * @param expId expression Id
	 * @param level The level of the node in the Graph.
	 */
	private void process(Integer expId, int level)
	{
		
		List<Integer> childrenList = idsList(graph.getChildrenList(constraints.getExpression(expId)));
		for (Integer child: childrenList)
		{
			IExpression expression = constraints.getExpression(child);
			if (expression.isInView())
			{
				if (maxNodeAtLevel.size()<=level)
				{
					maxNodeAtLevel.add(1);
					addToVisibleMap(child,level+1);
				}
				else
				{
					maxNodeAtLevel.set(level,maxNodeAtLevel.get(level)+1);
					addToVisibleMap(child,level);
				}
				
				process(child,level+1);
			}
			else
			{
				process(child, level);
			}
				
		}
	}

    private List<Integer> idsList(List<IExpression > exprList) {
        List<Integer> res = new ArrayList<Integer>();
        for(IExpression expr : exprList) {
            res.add(expr.getExpressionId());
        }
        return res;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
//		IQuery query = QueryGeneratorMock.createSampleQuery4();
		IQuery query = QueryObjectFactory.createQuery();
		setViewForAll(query);
		IConstraints constraints2 = query.getConstraints();
		Map<Integer, Position> positionMap2 = new QueryNodeLocator(500,query).getPositionMap();
		Set<Integer> keySet = positionMap2.keySet();
		for (Integer expId:keySet)
		{
			Position position = positionMap2.get(expId);
			String name = constraints2.getExpression(expId).getQueryEntity().getDynamicExtensionsEntity().getName();
			System.out.println(expId+"."+name+":"+position.getX()+","+position.getY());
		}
	}
	/**
	 * Method is added to testing purpose, which sets all expression as visible.
	 * @param query reference to the Query.
	 */
	private static void setViewForAll(IQuery query)
	{
		IConstraints constraints2 = query.getConstraints();
		for(IExpression expr : constraints2) {
            expr.setVisible(true);
		}
	}
}
