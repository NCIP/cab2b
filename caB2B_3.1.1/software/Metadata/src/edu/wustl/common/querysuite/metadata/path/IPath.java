
package edu.wustl.common.querysuite.metadata.path;

import java.io.Serializable;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;

/**
 * @version 1.0
 * @created 22-Dec-2006 2:49:27 PM
 */
public interface IPath extends Serializable
{

	/**
	 * @return the source entity of the path.
	 */
	EntityInterface getSourceEntity();

	/**
	 * @return the target entity of the path.
	 */
	EntityInterface getTargetEntity();

	/**
	 * If the source and target entities refer to classes, then<br>
	 * srcEntity of 1st assoc = getSourceEntity() and <br>
	 * targetEntity of last assoc = getTargetEntity().<br>
	 * But if the source entity and/or target entity refer to a category, 
	 * then the above will not hold...<br> e.g. getSourceEntity() 
	 * will return the category's entity, whereas the srcEntity of the 
	 * 1st association will be the entity corresponding to a class within that category.
	 */
	List<IAssociation> getIntermediateAssociations();

	/**
	 * @return true - if all intermediate associations are bidirectional; false otherwise.
	 */
	boolean isBidirectional();

	/**
	 * Call iff isBidirectional() = true.
	 * @return if bidirectional, returns a reverse path.
	 * @throws java.lang.IllegalArgumentException if the path is not bidirectional.
	 */
	IPath reverse();
    
    /**
     * This method is for Flex UI. It returns the database identifier of Path.
     * @return
     */
    // By Chandrakant on 10 Sep, 2007
    long getPathId();
}
