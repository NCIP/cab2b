/**
 * 
 */
package titli.controller;

/**
 * This class represents a case-insensitive name that is usually required for platform independent working of JDBC related functionality.
 * The equals() and hashCode() methods are overridden for proper functioning.
 * It can be safely used in Collections.
 *   
 * @author Juber Patel
 *
 */
public class Name
{
	private String name;
	private Integer hashCode;
	
	/**
	 * 
	 * @param name the name this object represents
	 */
	public Name(String name)
	{
		this.name=name.trim();
	}
	
	/**
	 * overriding equals() in Objetc.
	 * Two Names are equal if and only if their underlynig Strings return true for equlasIgnoreCase()
	 * @param anObject another Name
	 * @return true if and only if their underlynig Strings return true for equlasIgnoreCase(), otherwise false
	 * 
	 */
	public boolean equals(Object anObject)
	{
		Name aName = (Name) anObject;
		if(name.equalsIgnoreCase(aName.name))
		{
			return true;
		}
		
		return false;
		
	}
	
	
	
	/**
	 * overriding hashCode(). the hashcode is computed on the upper case form of the underlying String 
	 * @return the hashcode for this object
	 */
	public int hashCode()
	{
		if(hashCode==null)
		{
			hashCode = name.toUpperCase().hashCode(); 
		}
		
		return hashCode;
	}
	
	/**
	 * decides whether this name starts with the given prefix. The test is case-insesitive
	 * @param prefix the prefix to test
	 * @return true if this name starts with the given prefix, otherwise false
	 */
	public boolean startsWith(Name prefix)
	{
		if(name.toUpperCase().startsWith(prefix.name.toUpperCase()))
		{
			return true;
		}
	
		return false;
	}
	
	/**
	 * @return the String representation 
	 */
	public String toString()
	{
		return name;
	}

}
