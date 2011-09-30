
package edu.common.dynamicextensions.ui.webui.actionform;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author sujay_narkar
 *
 */
public class PreviewForm extends AbstractActionForm
{
	private static final long serialVersionUID = -7828307676065035418L;

	String entitySaved;
	/**
	 * 
	 */
	ContainerInterface containerInterface;

	/**
	 * @return The Form identifier
	 */
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param abstractDomainObject AbstractDomainObject instance
	 */
	public void setAllValues(AbstractDomainObject abstractDomainObject)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * This method resets all values.
	 */
	protected void reset()
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @return Returns the containerInterface.
	 */
	public ContainerInterface getContainerInterface()
	{
		return containerInterface;
	}

	/**
	 * @param containerInterface The containerInterface to set.
	 */
	public void setContainerInterface(ContainerInterface containerInterface)
	{
		this.containerInterface = containerInterface;
	}

	/**
	 * 
	 * @return entitySaved
	 */
	public String getEntitySaved()
	{
		return this.entitySaved;
	}

	/**
	 * 
	 * @param entitySaved entitySaved
	 */
	public void setEntitySaved(String entitySaved)
	{
		this.entitySaved = entitySaved;
	}
}
