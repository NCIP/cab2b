/**
 * <p>Title: DefaultLookupParameters Class>
 * <p>Description:	This is the implementation class of LookupParameters which stores the object which is to be matched and the cutoff value </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author santosh_chandak
 */
package edu.wustl.common.lookup;

import java.util.List;


public class DefaultLookupParameters implements LookupParameters
{
	
	/**
	 * Object
	 */
	Object object;
	/**
	 * List of participants
	 */
	List listOfParticipants;
	
	/**
	 * @return Returns the listOfParticipants.
	 */
	public List getListOfParticipants()
	{
		return listOfParticipants;
	}
	/**
	 * @param listOfParticipants The listOfParticipants to set.
	 */
	public void setListOfParticipants(List listOfParticipants)
	{
		this.listOfParticipants = listOfParticipants;
	}
	/**
	 * @return Returns the object.
	 */
	public Object getObject()
	{
		return object;
	}
	/**
	 * @param object The object to set.
	 */
	public void setObject(Object object)
	{
		this.object = object;
	}
}
