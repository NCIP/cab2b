package edu.common.dynamicextensions.entitymanager;

public class AssociationInformationObject implements NameInformationInterface
{

	/**
	 * 
	 */
	String name;

	/**
	 * 
	 */
	Long identifier;
	
	/**
	 * 
	 */
	String sourceRoleName;
	
	/**
	 * 
	 */
	String targetRoleName;
	
	/**
	 * 
	 */
	String sourceEntityName;
	/**
	 * 
	 */
	String targetEntityName;
	
	/**
	 * 
	 */
	protected Long sourceEntityId;
	
	/**
	 * 
	 */
	protected Long targetEntityId;

	/**
	 * 
	 * @param associationName
	 * @param associationIdentifier
	 * @param sourceRoleName2
	 * @param targetRoleName2
	 * @param sourceEntityName2
	 * @param targetEntityName2
	 */
	public AssociationInformationObject(String name, Long identifier, String sourceRoleName, String targetRoleName, String sourceEntityName, String targetEntityName, Long sourceEntityIdentifier,  Long targetEntityIdentifier) {
		this.name = name;
		this.identifier = identifier;
		this.sourceRoleName = sourceRoleName;
		this.targetRoleName = targetRoleName;
		this.sourceEntityName = sourceEntityName;
		this.targetEntityName = targetEntityName;
		this.sourceEntityId = sourceEntityIdentifier;
		this.targetEntityId = targetEntityIdentifier;
	}

	/**
	 * 
	 * @return
	 */
	public Long getIdentifier() {
		return identifier;
	}

	/**
	 * 
	 * @param identifier
	 */
	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getSourceEntityName() {
		return sourceEntityName;
	}

	/**
	 * 
	 * @param sourceEntityName
	 */
	public void setSourceEntityName(String sourceEntityName) {
		this.sourceEntityName = sourceEntityName;
	}

	/**
	 * 
	 * @return
	 */
	public String getSourceRoleName() {
		return sourceRoleName;
	}

	/**
	 * 
	 * @param sourceRoleName
	 */
	public void setSourceRoleName(String sourceRoleName) {
		this.sourceRoleName = sourceRoleName;
	}

	/**
	 * 
	 * @return
	 */
	public String getTargetEntityName() {
		return targetEntityName;
	}

	/**
	 * 
	 * @param targetEntityName
	 */
	public void setTargetEntityName(String targetEntityName) {
		this.targetEntityName = targetEntityName;
	}

	/**
	 * 
	 * @return
	 */
	public String getTargetRoleName() {
		return targetRoleName;
	}

	/**
	 * 
	 * @param targetRoleName
	 */
	public void setTargetRoleName(String targetRoleName) {
		this.targetRoleName = targetRoleName;
	}

	/**
	 * @return the sourceEntityId
	 */
	public Long getSourceEntityId()
	{
		return sourceEntityId;
	}

	/**
	 * @param sourceEntityId the sourceEntityId to set
	 */
	public void setSourceEntityId(Long sourceEntityId)
	{
		this.sourceEntityId = sourceEntityId;
	}

	/**
	 * @return the targetEntityId
	 */
	public Long getTargetEntityId()
	{
		return targetEntityId;
	}

	/**
	 * @param targetEntityId the targetEntityId to set
	 */
	public void setTargetEntityId(Long targetEntityId)
	{
		this.targetEntityId = targetEntityId;
	}
}
