package edu.wustl.common.util.dbManager;

/**This class contains all the relationship related data i.e className, relatedClass,
 * relationshipType.
 * 
 * @author vaishali_khandelwal
 *
 */
public class ClassRelationshipData 
{
	/*
	 Explanation of variables
	 consider Following block from Specimen.hbm.xml
	
	 <set
     name="biohazardCollection" -------------------------> Attribute Name
     table="CATISSUE_SPECIMEN_BIOHZ_REL" ----------------> Table Name
     lazy="false"
     inverse="false"
     cascade="none"
     sort="unsorted">
    <key
           column="SPECIMEN_ID" --------------------------> Key Id
     />
     <many-to-many
         class="edu.wustl.catissuecore.domain.Biohazard" --> Related Class
         column="BIOHAZARD_ID" ----------------------------> Role Id
         outer-join="auto"
      />

	 </set>
	 */
	private String className;
	private String relatedClassName;
	private String roleAttribute;
	private String relationType;
	private String relationTable;
	private String keyId;
	private String roleId;
	
	/* Default Constructor */
	public ClassRelationshipData()
	{
		
	}
	/* Full Constructor */
	public ClassRelationshipData(String className,String relatedClassName,String roleAttribute,String relationType,
										String relationTable,String keyId,String roleId)
	{
		this.className=className;
		this.relatedClassName=relatedClassName;
		this.roleAttribute=roleAttribute;
		this.relationType=relationType;
		this.relationTable=relationTable;
		this.keyId=keyId;
		this.roleId=roleId;
	}
	/* Minimal Constructor */
	public ClassRelationshipData(String className,String relatedClassName,String roleAttribute)
	{
		this.className=className;
		this.relatedClassName=relatedClassName;
		this.roleAttribute=roleAttribute;
		
	}
	/* Returns the Class Name */
	public String getClassName() 
	{
		return className;
	}
	
	public void setClassName(String className) 
	{
		this.className = className;
	}
	/* Returns the key Id */
	public String getKeyId() 
	{
		return keyId;
	}
	
	public void setKeyId(String keyId) 
	{
		this.keyId = keyId;
	}
	/* Returns the Related Class Name which has relation with class */
	public String getRelatedClassName() 
	{
		return relatedClassName;
	}
	public void setRelatedClass(String relatedClassName) 
	{
		this.relatedClassName = relatedClassName;
	}
	/* Returns the Table Name */
	public String getRelationTable() 
	{
		return relationTable;
	}
	public void setRelationTable(String relationTable) 
	{
		this.relationTable = relationTable;
	}
	/* Returns the relation type i.e Many-To-Many or  One-To-Many*/
	public String getRelationType() 
	{
		return relationType;
	}
	public void setRelationType(String relationType) 
	{
		this.relationType = relationType;
	}
	/* Returns the role attribute */
	public String getRoleAttribute() 
	{
		return roleAttribute;
	}
	public void setRoleAttribute(String roleAttribute) 
	{
		this.roleAttribute = roleAttribute;
	}
	
	/* Returns the role Id */
	public String getRoleId() 
	{
		return roleId;
	}
	public void setRoleId(String roleId) 
	{
		this.roleId = roleId;
	}

	/** This function checks weather obj is equal to the obj which invoked this method.
	 * For equality checking we are using Class Name, Related Class Name and Role Attribute Name because 
	 * these three variables will be unique for any relationship.
	 * @param obj the object for which equality is checked.
	 */
	public boolean equals(Object obj)
	{
		if( obj!=null && obj instanceof ClassRelationshipData)
		{
			ClassRelationshipData crd = (ClassRelationshipData)obj;
			if(this.getClassName().equals(crd.getClassName()) &&
					this.getRelatedClassName().equals(crd.getRelatedClassName()) && 
					this.getRoleAttribute().equals(crd.getRoleAttribute()))
		
			{
				return true;
			}
		}
		return false;
	}
	
	/** This function returns the hashCode value for the object which invoked this method
	 * 	@return int - hash code value
	 */ 
	public int hashCode()
	{
		int hashCode = 0;
	
		if(className!=null)
			hashCode += className.hashCode(); 
		if(relatedClassName!=null)	
			hashCode += relatedClassName.hashCode(); 
		if(roleAttribute!=null)		
			hashCode +=  roleAttribute.hashCode();
		
		return hashCode;
	}		
}