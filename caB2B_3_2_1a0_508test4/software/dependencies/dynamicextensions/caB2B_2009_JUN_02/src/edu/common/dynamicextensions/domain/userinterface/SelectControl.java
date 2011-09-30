
package edu.common.dynamicextensions.domain.userinterface;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.AssociationDisplayAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.AssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.SelectInterface;

/**
 * @author rahul_ner
 * @hibernate.joined-subclass table="DYEXTN_SELECT_CONTROL"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public abstract class SelectControl extends Control implements AssociationControlInterface, SelectInterface
{
	String separator = "";

	Collection<AssociationDisplayAttributeInterface> associationDisplayAttributeCollection = new HashSet<AssociationDisplayAttributeInterface>();

	/**
	 * This method Returns the associationDisplayAttributeCollection.
	 * @hibernate.set name="associationDisplayAttributeCollection" table="DYEXTN_ASSO_DISPLAY_ATTR"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="SELECT_CONTROL_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.AssociationDisplayAttribute" 
	 * @return Returns the associationDisplayAttributeCollection.
	 */
	public Collection<AssociationDisplayAttributeInterface> getAssociationDisplayAttributeCollection()
	{
		return associationDisplayAttributeCollection;
	}

	/**
	 * @param associationDisplayAttributeCollection The associationDisplayAttributeCollection to set.
	 */
	public void setAssociationDisplayAttributeCollection(Collection<AssociationDisplayAttributeInterface> associationDisplayAttributeCollection)
	{
		this.associationDisplayAttributeCollection = associationDisplayAttributeCollection;
	}

	/**
	 * @return Returns the separator
	 * @hibernate.property name="separator" type="string" column="SEPARATOR_STRING"
	 */
	public String getSeparator()
	{
		return separator;
	}

	/**
	 * @param separator The separator to set.
	 */
	public void setSeparator(String separator)
	{
		this.separator = separator;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.AssociationControlInterface#addAssociationDisplayAttribute(edu.common.dynamicextensions.domaininterface.AssociationDisplayAttributeInterface)
	 */
	public void addAssociationDisplayAttribute(AssociationDisplayAttributeInterface associationDisplayAttribute)
	{
		associationDisplayAttributeCollection.add(associationDisplayAttribute);
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.AssociationControlInterface#removeAssociationDisplayAttribute(edu.common.dynamicextensions.domaininterface.AssociationDisplayAttributeInterface)
	 */
	public void removeAssociationDisplayAttribute(AssociationDisplayAttributeInterface associationDisplayAttribute)
	{
		associationDisplayAttributeCollection.remove(associationDisplayAttribute);
	}
	/**
	 * 
	 */
	public void removeAllAssociationDisplayAttributes()
	{
		associationDisplayAttributeCollection.clear();
	}
}
