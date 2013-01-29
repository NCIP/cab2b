/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.admin.flex.DAGNode;
import edu.wustl.cab2b.admin.util.AdminConstants;

/**
 * This class is responsible for sending attributes in Available Attributes and
 * Selected Attributes back to CreatCategory.jsp while editing DAG Node
 * 
 * @author lalit_chand
 */
public class SearchCategoryBizLogic {

	/**
	 * 
	 * @param list
	 *            List of attributes
	 * @return attributes separated by ";" in a String
	 */
	private String getAttributes(List<String> list) {
		StringBuffer string = new StringBuffer();
		for (String temp : list) {
			string.append(temp);
		}

		return string.toString();
	}

	/**
	 * 
	 * @param entity
	 * @param dagNode
	 * @return
	 */
	public String showAttributeInformation(final EntityInterface entity,
			final DAGNode dagNode) {
		Collection<AttributeInterface> attributeCollection = entity
				.getAttributeCollection();
		List<String> addlist = new ArrayList<String>();

		for (AttributeInterface attributes : attributeCollection) {
			addlist.add(";" + attributes.getName() + ":" + attributes.getId());
		}

		List<String> editlist = new ArrayList<String>();
		for (String attribute : dagNode.getAttributeList()) {
			editlist.add(";" + attribute);
		}

		addlist.removeAll(editlist);
		String selectedAttributes = getAttributes(editlist);
		String availableAttributes = getAttributes(addlist);

		String nodeName = edu.wustl.cab2b.common.util.Utility
				.getDisplayName(entity);
		String entityId = entity.getId().toString();
		String allAttributeInfo = nodeName
				+ AdminConstants.ENTITY_NAME_SEPARATOR + entityId
				+ AdminConstants.ENTITY_ID_SEPARATOR + "edit"
				+ AdminConstants.OPERATION + availableAttributes
				+ AdminConstants.ADD_EDIT + selectedAttributes;

		return allAttributeInfo;
	}

}
