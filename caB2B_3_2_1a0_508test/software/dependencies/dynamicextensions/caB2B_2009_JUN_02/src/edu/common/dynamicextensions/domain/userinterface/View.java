
package edu.common.dynamicextensions.domain.userinterface;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ViewInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.class table="DYEXTN_VIEW"
 */
public class View extends DynamicExtensionBaseDomainObject implements Serializable, ViewInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 2285685823617305296L;

	/**
	 * Name of the View.
	 */
	protected String name;

	/**
	 * Collection of container for this view.
	 */
	protected Collection<ContainerInterface> containerCollection = new HashSet<ContainerInterface>();

	/**
	 * Empty Constructor
	 */
	public View()
	{
	}

	/**
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_VIEW_SEQ"
	 * @return Returns the id.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @hibernate.property name="name" type="string" column="NAME" 
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @hibernate.set name="containerCollection" table="DYEXTN_CONTAINER"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="VIEW_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.userinterface.Container"
	 * @return Returns the containerCollection.
	 */
	public Collection<ContainerInterface> getContainerCollection()
	{
		return containerCollection;
	}

	/**
	 * @param containerCollection The containerCollection to set.
	 */
	public void setContainerCollection(Collection<ContainerInterface> containerCollection)
	{
		this.containerCollection = containerCollection;
	}

	/**
	 * This method adds a Container to the Collection of Container of the View.
	 * @param container the Container instance to be added.
	 */
	public void addContainer(ContainerInterface container)
	{
	}
}