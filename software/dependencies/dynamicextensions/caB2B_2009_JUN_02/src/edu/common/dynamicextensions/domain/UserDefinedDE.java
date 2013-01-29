/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * This Class represents the UserDefined DataElements
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_USERDEFINED_DE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class UserDefinedDE extends DataElement implements UserDefinedDEInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 1148563078444213122L;

	/**
	 * Collection of PermissibleValues
	 */
	Collection<PermissibleValueInterface> permissibleValueCollection = new HashSet<PermissibleValueInterface>();

	/**
	 * Set all values from the form
	 * @param abstractActionForm the ActionForm
	 * @throws AssignDataException if data is not in proper format.
	 */
	public void setAllValues(AbstractActionForm abstractActionForm) throws AssignDataException
	{
	}

	/**
	 * This method returns the Collection of PermissibleValues.
	 * @hibernate.set name="permissibleValueCollection" table="DYEXTN_PERMISSIBLE_VALUE"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="USER_DEF_DE_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.PermissibleValue" 
	 * @return the Collection of PermissibleValues.
	 */
	public Collection<PermissibleValueInterface> getPermissibleValueCollection()
	{
		return permissibleValueCollection;
	}

	/**
	 * This method sets the permissibleValueCollection to the given Collection of PermissibleValues.
	 * @param permissibleValueCollection The permissibleValueCollection to set.
	 */
	public void setPermissibleValueCollection(Collection<PermissibleValueInterface> permissibleValueCollection)
	{
		this.permissibleValueCollection = permissibleValueCollection;
	}

	/**
	 * This method adds a PermissibleValue to the Collection of PermissibleValues.
	 * @param permissibleValue the PermissibleValue to be added.
	 */
	public void addPermissibleValue(PermissibleValueInterface permissibleValue)
	{
		if (this.permissibleValueCollection == null)
		{
			this.permissibleValueCollection = new LinkedHashSet<PermissibleValueInterface>();
		}
		this.permissibleValueCollection.add(permissibleValue);
	}

}
