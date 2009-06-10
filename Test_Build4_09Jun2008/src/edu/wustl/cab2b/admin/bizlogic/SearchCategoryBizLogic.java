package edu.wustl.cab2b.admin.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.admin.flex.DAGNode;
import edu.wustl.cab2b.admin.util.AdminConstants;

/**
 * 
 * @author lalit_chand
 * 
 * This class is resposible for sending attributes in  Available Attributes and Selected Attributes  back to CreatCategory.jsp when editting DAG Node
 *
 */

public class SearchCategoryBizLogic {

    /**
     * 
     * 
     * @param list List of attributes
     * @return attributes separated by ";" in a String
     */

    private String getAttributes(List<String> list) {
        String str = "";
        for (String temp : list) {
            str = str + temp;
        }

        return str;
    }

    /**
     * 
     * @param entity 
     * @param condition
     * @return
     */

    public String showAttributeInformation(EntityInterface entity, DAGNode dagNode) {

        String availableAttributes = "";
        String selectedAttributes = "";

        String entityId = entity.getId().toString();
        Collection<AttributeInterface> attributeCollection = entity.getAttributeCollection();
        List<String> addlist = new ArrayList<String>();

        for (AttributeInterface attributes : attributeCollection) {
            addlist.add(";" + attributes.getName() + ":" + attributes.getId());
        }

        String nodeName = edu.wustl.cab2b.common.util.Utility.getDisplayName(entity);

        String operation = "edit";
        List<String> editlist = new ArrayList<String>();

        for (String attribute : dagNode.getAttributeList()) {
            editlist.add(";" + attribute);
        }

        addlist.removeAll(editlist);
        selectedAttributes = getAttributes(editlist);
        availableAttributes = getAttributes(addlist);

        String allAttributeInfo = nodeName + AdminConstants.ENTITY_NAME_SEPARATOR + entityId
                + AdminConstants.ENTITY_ID_SEPARATOR + operation + AdminConstants.OPERATION + availableAttributes
                + AdminConstants.ADD_EDIT + selectedAttributes;

        return allAttributeInfo;

    }

}
