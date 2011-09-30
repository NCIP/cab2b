package edu.wustl.common.jms;


/**
 * 
 * @author kalpana_thakur
 * TODO This interface is used to initiate the connection and to 
 * publish the message or data   
 */
public interface MessagePublisher
{
	/**
	 * This method is used to initialize the connection 
	 * */
	public void initialize();
	
	/**
	 * @param obj
	 * This method is used to publish message or data 
	 */
	public void update(Object obj);
}
