package edu.wustl.cab2b.admin.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.admin.util.Cab2bConstants;
import edu.wustl.common.querysuite.queryobject.ICondition;

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

    public String showAttributeInformation(EntityInterface entity, List<ICondition> condition) {

        String availableAttributes = "";
        String selectedAttributes = "";

        String entityName = entity.getName();

        Iterator<EntityGroupInterface> iter = entity.getEntityGroupCollection().iterator();

        String[] entityNameArray = entityName.split("\\.");
        String entityId = entity.getId().toString();
        Collection<AttributeInterface> attributeCollection = entity.getAttributeCollection();
        List<String> addlist = new ArrayList<String>();
        for (AttributeInterface attributes : attributeCollection) {
            addlist.add(";" + attributes.getName() + ":" + attributes.getId());
        }

        entityName = entityNameArray[entityNameArray.length - 1] + " (" + iter.next().getLongName() + ")";

        String operation = "edit";
        List<String> editlist = new ArrayList<String>();
        for (ICondition cond : condition) {
            AttributeInterface attribute = cond.getAttribute();
            editlist.add(";" + attribute.getName() + ":" + attribute.getId());
        }
        addlist.removeAll(editlist);
        selectedAttributes = getAttributes(editlist);
        availableAttributes = getAttributes(addlist);

        String allAttributeInfo = entityName + Cab2bConstants.ENTITY_NAME_SEPARATOR + entityId
                + Cab2bConstants.ENTITY_ID_SEPARATOR + operation + Cab2bConstants.OPERATION + availableAttributes
                + Cab2bConstants.ADD_EDIT + selectedAttributes;

        return allAttributeInfo;

    }

}
