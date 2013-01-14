/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.viewresults;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;

public class HyperLinkUserObject {
    
    IAssociation association;
    
    EntityInterface targetEntity;
    
    IDataRow parentDataRow;

    /**
     * @return Returns the association.
     */
    public IAssociation getAssociation() {
        return association;
    }

    /**
     * @param association The association to set.
     */
    public void setAssociation(IAssociation association) {
        this.association = association;
    }

    /**
     * @return Returns the parentDataRow.
     */
    public IDataRow getParentDataRow() {
        return parentDataRow;
    }

    /**
     * @param parentDataRow The parentDataRow to set.
     */
    public void setParentDataRow(IDataRow parentDataRow) {
        this.parentDataRow = parentDataRow;
    }

    /**
     * @return Returns the targetEntity.
     */
    public EntityInterface getTargetEntity() {
        return targetEntity;
    }

    /**
     * @param targetEntity The targetEntity to set.
     */
    public void setTargetEntity(EntityInterface targetEntity) {
        this.targetEntity = targetEntity;
    }

}
