/**
 * 
 */
package edu.wustl.common.querysuite.security.utility;

import java.util.HashMap;
import java.util.Map;


/**
 * This class maintains cache related to Read and Identified Data Privileges for a particular user on CP ids to 
 * which objects in Query are related.
 * @author supriya_dankh
 *
 */
public class CsmCache
{

	/*A map that stores collection protocol id vs a boolean value that a user is authorized to read data related to that CP.*/ 
	private Map<Long, Boolean> ReadPrivilegeMap ;
	/*A map that stores collection protocol id vs a boolean value that a user is authorized to see Identified
	  data related to that CP.*/
	private Map<Long, Boolean> IdentifiedDataAccsessMap ;
	
	
	public CsmCache()
	{     
		ReadPrivilegeMap = new HashMap<Long, Boolean>();
		IdentifiedDataAccsessMap = new HashMap<Long, Boolean>();
	}
	
	/**
	 * Returns true or false depending on user privileges.
	 * @param id
	 * @return
	 */
	public Boolean isReadDenied(Long id)
	{  
		Boolean isReadDenied = ReadPrivilegeMap.get(id);
		return isReadDenied;
	} 
	
	/**
	 * Returns true or false depending on user privileges.
	 * @param id
	 * @return
	 */
	public Boolean isIdentifedDataAccess(Long id)
	{ 
		Boolean isIdentified = this.IdentifiedDataAccsessMap.get(id);
		return isIdentified;
	}
	
	/**
	 * Adds record in ReadPrivilegeMap.
	 * @param id
	 * @param isAuthorized
	 */
	public void addNewObjectInReadPrivilegeMap(Long id,Boolean isAuthorized)
	{
		ReadPrivilegeMap.put(id, isAuthorized);
	}
	/**
	 * Remove record from ReadPrivilegeMap
	 * @param id
	 */
	public void removeObjectFromReadPrivilegeMap(Long id)
	{
		ReadPrivilegeMap.remove(id);
	}
	/**
	 * Adds record in IdentifiedDataAccsessMap.
	 * @param id
	 * @param isAuthorized
	 */
	public void addNewObjectInIdentifiedDataAccsessMap(Long id,Boolean isAuthorized)
	{
		IdentifiedDataAccsessMap.put(id, isAuthorized);
	}
	
	/**
	 * Remove record from IdentifiedDataAccsessMap
	 * @param id
	 */
	public void removeObjectFromIdentifiedDataAccsessMap(Long id)
	{
		IdentifiedDataAccsessMap.remove(id);
	}

}
