
package edu.common.dynamicextensions.domaininterface.userinterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AssociationDisplayAttributeInterface;

/**
 * This interface is implemented by any control that is used to show for association.
 *  
 * @author Rahul Ner
 *
 */
public interface AssociationControlInterface
{

	/**
	 * @param associationDisplayAttributeCollection
	 */
	void setAssociationDisplayAttributeCollection(Collection<AssociationDisplayAttributeInterface> associationDisplayAttributeCollection);

	/**
	 * @return
	 */
	Collection<AssociationDisplayAttributeInterface> getAssociationDisplayAttributeCollection();

	/**
	 * @param associationDisplayAttribute
	 */
	void addAssociationDisplayAttribute(AssociationDisplayAttributeInterface associationDisplayAttribute);

	/**
	 * @param associationDisplayAttribute
	 */
	void removeAssociationDisplayAttribute(AssociationDisplayAttributeInterface associationDisplayAttribute);
	
	/**
	 * @return
	 */
	String getSeparator();

	/**
	 * @param separator
	 */
	void setSeparator(String separator);
	
	/**
	 * Remove all association display attributes from collection
	 */
	void removeAllAssociationDisplayAttributes();
}
