package edu.wustl.cab2b.client.ui.viewresults;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;

public class HyperLinkUserObject {
    
    IAssociation association;
    
    EntityInterface targetEntity;
    
    DataRow parentDataRow;

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
    public DataRow getParentDataRow() {
        return parentDataRow;
    }

    /**
     * @param parentDataRow The parentDataRow to set.
     */
    public void setParentDataRow(DataRow parentDataRow) {
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
