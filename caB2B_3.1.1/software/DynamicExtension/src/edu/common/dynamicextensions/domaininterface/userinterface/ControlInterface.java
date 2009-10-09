
package edu.common.dynamicextensions.domaininterface.userinterface;

import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This interface stores necessary informations about the control that gets added 
 * to the Container on dynamically generated User Interface.
 * @author geetika_bangard
 */
public interface ControlInterface extends DynamicExtensionBaseDomainObjectInterface,  Comparable
{

	/**
	 * Id of the control
	 * @return id 
	 */
	Long getId();

	/**
	 * This can be a primitive type or derived type.
	 * @return Returns the attribute.
	 */
	AbstractAttributeInterface getAbstractAttribute();

	/**
	 * @param abstractAttributeInterface The attribute to set.
	 */
	void setAbstractAttribute(AbstractAttributeInterface abstractAttributeInterface);

	/**
	 * Caption/Title for the control.
	 * @return Returns the caption.
	 */
	String getCaption();

	/**
	 * @param caption The caption to set.
	 */
	void setCaption(String caption);

	/**
	 * The css specified for the control by user.
	 * @return Returns the cssClass.
	 */
	String getCssClass();

	/**
	 * @param cssClass The cssClass to set.
	 */
	void setCssClass(String cssClass);

	/**
	 * If user has chosen it to be kept hidden.
	 * @return Returns the isHidden.
	 */
	Boolean getIsHidden();

	/**
	 * @param isHidden The isHidden to set.
	 */
	void setIsHidden(Boolean isHidden);

	/**
	 * Name of the control.
	 * @return Returns the name.
	 */
	String getName();

	/**
	 * @param name The name to set.
	 */
	void setName(String name);

	/**
	 * The sequence Number for setting it at the desired place in the tree and so in the UI.
	 * @return Returns the sequenceNumber.
	 */
	Integer getSequenceNumber();

	/**
	 * @param sequenceNumber The sequenceNumber to set.
	 */
	void setSequenceNumber(Integer sequenceNumber);

	/**
	 * Tool tip for the control.
	 * @return Returns the tooltip.
	 */
	String getTooltip();

	/**
	 * @param tooltip The tooltip to set.
	 */
	void setTooltip(String tooltip);

	/**
	 * @return return the HTML string for this type of a object
	 * @throws DynamicExtensionsSystemException 
	 */
	String generateHTML() throws DynamicExtensionsSystemException;

	/**
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	String getHTMLComponentName() throws DynamicExtensionsSystemException;

	/**
	 * @return
	 */
	Object getValue();

	/**
	 * @param value
	 */
	void setValue(Object value);

	/**
	 * @return
	 */
	Boolean getSequenceNumberChanged();

	/**
	 * @param sequenceNumberChanged
	 */
	void setSequenceNumberChanged(Boolean sequenceNumberChanged);
	
	/**
	 * @return parentContainer
	 */
	Container getParentContainer();


	/**
	 * @param parentContainer parentContainer
	 */
	void setParentContainer(Container parentContainer);
	
	/**
	 * @return the isSubControl
	 */
	boolean getIsSubControl();

	/**
	 * @param isSubControl the isSubControl to set
	 */
	void setIsSubControl(boolean isSubControl);

}