/**
 * 
 */
package edu.wustl.cab2b.server.datalist;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * @author deepak_shingan
 *
 */
public class DataCategorySaver extends AbstractDataListSaver {

    List<AttributeInterface> newAttributes;

    List<AttributeInterface> oldAttributes;

    public DataCategorySaver(List<AttributeInterface> newOnes, List<AttributeInterface> oldOnes) {
        newAttributes = newOnes;
        oldAttributes = oldOnes;
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.datalist.AbstractDataListSaver#populateNewEntity(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    @Override
    protected void populateNewEntity(EntityInterface oldEntity) {
        DataListUtil.copyNonVirtualAttributes(newEntity, oldEntity);
        if (oldAttributes != null)
            for (AttributeInterface oldAttribute : oldAttributes)
                newEntity.removeAttribute(oldAttribute);

        if (newAttributes != null)
            for (AttributeInterface newAttribute : newAttributes) {
                newEntity.addAttribute(newAttribute);
                newAttribute.setEntity(newEntity);
            }
    }
}
