/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@author Mandar Deshmukh
 *@version 1.0
 */

package edu.wustl.common.beans;

import java.io.Serializable;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class NameValueBean implements Comparable,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 861314614541823827L;
	private Object name; 
	private Object value;
	/*
	 * we are using this field to improve performance
	 */
	private String nameLowerCase;
	
	//Relevence counter field is for sorting according to relevance
	private Long relevanceCounter;

	public NameValueBean()
	{

	}

	public NameValueBean(Object name, Object value)
	{
		this.name = name;
		
		if(this.name instanceof String)
		{
			this.nameLowerCase = ((String)this.name).toLowerCase();
		}
		
		this.value = value;
	}

	
	public NameValueBean(Object name, Object value,Long relevanceCounter)
	{
		this.name = name;
		
		if(this.name instanceof String)
		{
			this.nameLowerCase = ((String)this.name).toLowerCase();
		}
		
		this.value = value;
		this.relevanceCounter = relevanceCounter;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name.toString();
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(Object name)
	{
		this.name = name;
		
		if(this.name instanceof String)
		{
			this.nameLowerCase = ((String)this.name).toLowerCase();
		}
		
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue()
	{
		return value.toString();
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}

	
	/**
	 * @return Returns the relevanceCounter.
	 */
	public Long getRelevanceCounter()
	{
		return relevanceCounter;
	}

	/**
	 * @param relevanceCounter The relevanceCounter to set.
	 */
	public void setRelevanceCounter(Long relevanceCounter)
	{
		this.relevanceCounter = relevanceCounter;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new String("name:" + name.toString() + " value:" + value.toString());
	}

	public int compareTo(Object obj)
	{

		if (obj instanceof NameValueBean)
		{
			NameValueBean nameValueBean = (NameValueBean) obj;
			if (nameValueBean.name instanceof String && name instanceof String)
			{
				return nameLowerCase.compareTo(nameValueBean.nameLowerCase);
			}
			else
			{
				return compareObject(obj);
			}
		}
		return 0;
	}

	private int compareObject(Object tmpobj)
	{
		NameValueBean obj = (NameValueBean) tmpobj;
		//Logger.out.debug(name.getClass()+" : " +  obj.name.getClass() );

		if (name.getClass() == obj.name.getClass())
		{

			if (name.getClass().getName().equalsIgnoreCase("java.lang.Long"))
			{
				Long numOne = (Long) name;
				Long numTwo = (Long) obj.name;
				return numOne.compareTo(numTwo);
			}
			else if (name.getClass().getName().equalsIgnoreCase("java.lang.Double"))
			{
				Double numOne = (Double) name;
				Double numTwo = (Double) (obj.name);
				return numOne.compareTo(numTwo);
			}
			else if (name.getClass().getName().equalsIgnoreCase("java.lang.Float"))
			{
				Float numOne = (Float) name;
				Float numTwo = (Float) (obj.name);
				return numOne.compareTo(numTwo);
			}
			else if (name.getClass().getName().equalsIgnoreCase("java.lang.Integer"))
			{
				Integer numOne = (Integer) name;
				Integer numTwo = (Integer) (obj.name);
				return numOne.compareTo(numTwo);
			}
		}
		//		Logger.out.debug("Number type didnot match");
		return 0;
	}

	//-----------
	public boolean equals(Object object)

	{

		if (this.getClass().getName().equals(object.getClass().getName()))

		{

			NameValueBean nvb = (NameValueBean) object;

			if (this.getName().equals(nvb.getName()) && this.getValue().equals(nvb.getValue()))

				return true;

		}

		return false;

	}

	public int hashCode()

	{

		if (this.getName() != null && this.getValue() != null)

			return this.getName().hashCode() * this.getValue().hashCode();

		return super.hashCode();

	}

}
