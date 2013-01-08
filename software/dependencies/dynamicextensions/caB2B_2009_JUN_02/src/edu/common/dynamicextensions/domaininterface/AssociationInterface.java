/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;

/**
 * This interface contains all the information regarding association between the entities.
 * The association may be of different types like one-to-many,many-to-many or from source - destination,
 * bidirectional.Entity object contains association collection.Each object in association collection represents 
 * association of  the entity with other entity.
 *  Using the information of association object different constraints are added in the dynamically created tables.  
 * 
 * @author sujay_narkar
 *
 */
public interface AssociationInterface extends AbstractAttributeInterface
{

	/**
	 * This method returns the direction of the Association. 
	 * It can be Source to Destination or vice versa or bidirectional.
	 * @return the direction of the Association
	 */
	AssociationDirection getAssociationDirection();

	/**
	 * This method sets the direction of the Association.
	 * @param direction the direction of the Association to be set.
	 */
	void setAssociationDirection(AssociationDirection direction);

	/**
	 * This method returns the target Entity of the Association.
	 * @return the target Entity of the Association
	 */
	EntityInterface getTargetEntity();

	/**
	 * This method sets the target Entity of the Association to the given Entity.
	 * @param targetEntity the Entity to be set as target Entity of the Association.
	 */
	void setTargetEntity(EntityInterface targetEntity);

	/**
	 * This method returns the source Role of the Association. Source role represents information 
	 * such as minimum cardinality, maximum cardinality etc., information of source entity. 
	 * @return the source Role of the Association.
	 */
	RoleInterface getSourceRole();

	/**
	 * This method sets the source Role of the Association.
	 * @param sourceRole the Role to be set as source Role.
	 */
	void setSourceRole(RoleInterface sourceRole);

	/**
	 * This method returns the targetRole of the Association. Target role represents information
	 * such as minimum cardinality, maximum cardinality etc., information of target entity. 
	 * @return the targetRole of the Association.
	 */
	RoleInterface getTargetRole();

	/**
	 * This method sets the target Role of the Association.
	 * @param targetRole the Role to be set as targetRole of the Association.
	 */
	void setTargetRole(RoleInterface targetRole);

	/**
	 * This method returns the ConstraintProperties of the Association.
	 * Constraint properties represents the database information of the dynamically created tables 
	 * for the association. e.g. If the association type is many to many we need to store middle table name
	 * and the foreign keys of both the tables. 
	 * @return the ConstraintProperties of the Association.
	 */
	ConstraintPropertiesInterface getConstraintProperties();


	/**
	 * @param constraintProperties constraintProperties
	 */
	void setConstraintProperties(ConstraintPropertiesInterface constraintProperties);


	/**
	 * @return BOOLEAN
	 */
	Boolean getIsSystemGenerated();

	/**
	 * @param isSystemGenerated The isSystemGenerated to set.
	 */
	void setIsSystemGenerated(Boolean isSystemGenerated);

}
