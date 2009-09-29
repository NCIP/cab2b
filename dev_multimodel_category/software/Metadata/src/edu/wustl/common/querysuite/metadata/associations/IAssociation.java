/**
 * 
 */

package edu.wustl.common.querysuite.metadata.associations;

import java.io.Serializable;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * @author prafull_kadam
 * Interface to represent Association between two classes.
 */
public interface IAssociation extends Serializable
{

	/**
	 * @return true - if association is bidirectional, false otherwise.
	 */
	boolean isBidirectional();

	/**
	 * @return the source entity.
	 */
	EntityInterface getSourceEntity();

	/**
	 * @return the target entity.
	 */
	EntityInterface getTargetEntity();
	
	/**
	 * Call only if isBidirectional is true.
	 * @return an association which is the reverse of this association.
	 * @throws UnsupportedOperationException if association is not bidirectional.
	 */
	IAssociation reverse();
}
