/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * @author prafull_kadam
 */
public interface IOutputEntity extends IQueryEntity
{

	//	public List<String> getUrls();
	//
	//	/**
	//	 * @param urls
	//	 * 
	//	 */
	//	public void setUrls(List<String> urls);

	/**
	 * all the Attributes in this list belong to the entity obtained from
	 * getDynamicExtensionsEntity()
	 * @return the list of Dynamic Extentsion attributes.
	 */
	List<AttributeInterface> getSelectedAttributes();

	/**
	 * @param selectedAttributesIndices The selectedAttributesIndices to set
	 * 
	 */
	void setSelectedAttributes(List<AttributeInterface> selectedAttributesIndices);

}
