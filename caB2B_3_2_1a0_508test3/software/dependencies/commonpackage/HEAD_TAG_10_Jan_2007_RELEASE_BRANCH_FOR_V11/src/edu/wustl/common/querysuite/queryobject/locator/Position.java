package edu.wustl.common.querysuite.queryobject.locator;


/**
 * Class which contains x,y coordinates & its getters & setters.
 * Objects of this class is used to specify node position of the query nodes in the DAG.
 * @author prafull_kadam
 */
public class Position
{
	private int x,y;

	/**
	 * Default constructor.
	 * @param x The X coordinate value 
	 * @param y The Y coordinate value
	 */
	public Position(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	/**
	 * To get X coordinate value
	 * @return The X coordinate value
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * To get Y coordinate value
	 * @return The Y coordinate value
	 */
	public int getY()
	{
		return y;
	}
}
