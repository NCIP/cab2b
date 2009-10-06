
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * This Class represents the pre-defined values of the Attribute.
 * @author sujay_narkar
 * @hibernate.class table="DYEXTN_DATA_ELEMENT"
 */
public abstract class DataElement extends DynamicExtensionBaseDomainObject implements DataElementInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	protected static final long serialVersionUID = 1234567890L;


	/**
	 * This method returns the unique identifier of DataElement.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_DATA_ELEMENT_SEQ" 
	 * @return the unique identifier of DataElement.
	 */
	public Long getId()
	{
		return id;
	}
}
