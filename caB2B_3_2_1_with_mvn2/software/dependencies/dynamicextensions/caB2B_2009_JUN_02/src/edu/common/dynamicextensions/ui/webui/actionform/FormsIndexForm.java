/**
 * 
 */

package edu.common.dynamicextensions.ui.webui.actionform;

import java.util.Collection;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author chetan_patil
 *
 */
public class FormsIndexForm extends AbstractActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * Collection of Entities
	 */
	protected Collection containerCollection = null;

	/**
	 * Values of the Check boxes
	 */
	protected String[] containerCheckBoxes = null;
	/**
	 * mode
	 */
	protected String mode;
	protected String operationMode;

	/**
	 * @return the operationMode
	 */
	public String getMode()
	{
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode)
	{
		this.mode = mode;
	}

	/**
	 * @return the containerList
	 */
	public Collection getContainerCollection()
	{
		return containerCollection;
	}

	/**
	 * @param containerList the containerList to set
	 */
	public void setContainerCollection(Collection containerList)
	{
		this.containerCollection = containerList;
	}

	/**
	 * @return the containerCheckBoxes
	 */
	public String[] getContainerCheckBoxes()
	{
		return containerCheckBoxes;
	}

	/**
	 * @param containerCheckBoxes the containerCheckBoxes to set
	 */
	public void setContainerCheckBoxes(String[] containerCheckBoxes)
	{
		this.containerCheckBoxes = containerCheckBoxes;
	}

	/**
	 * Overrides getFormId() method of ActionForm
	 * @return the form identifier
	 */
	public int getFormId()
	{
		return 0;
	}

	/**
	 * Overrides reset() method of ActionForm
	 */
	protected void reset()
	{
		containerCollection = null;
	}

	/**
	 * Overrides setAllValues() method of ActionForm
	 * @param abstractDomainObject AbstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject abstractDomainObject)
	{
	}

	/**
	 * @return the operationMode
	 */
	public String getOperationMode()
	{
		return operationMode;
	}

	/**
	 * @param operationMode the operationMode to set
	 */
	public void setOperationMode(String operationMode)
	{
		this.operationMode = operationMode;
		if(operationMode==null)
		{
			this.operationMode = "";
		}
	}

}
