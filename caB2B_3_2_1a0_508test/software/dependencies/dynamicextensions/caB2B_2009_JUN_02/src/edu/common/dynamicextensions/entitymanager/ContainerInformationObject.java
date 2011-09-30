package edu.common.dynamicextensions.entitymanager;


public class ContainerInformationObject
{
	/**
	 * 
	 */
	String name;
	/**
	 * 
	 */
	String value;
	/**
	 * 
	 */
	String description;
	
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @param description
	 */
	public ContainerInformationObject(String name, String value, String description)
	{
		super();
		this.name = name;
		this.value = value;
		this.description = description;
	}
	

}
