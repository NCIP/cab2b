/**
 * <p>Title: DataRow Class>
 * <p>Description:  Class which represents a row in the data list or the attribute names in the
 * data list.</p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.common.datalist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.tree.TreeNodeImpl;

/**
 * Class which represents a row in the data list or the attribute names in the
 * data list. 
 * @author gautam_shetty
 */
public class DataRow extends TreeNodeImpl implements IDataRow, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Name of the class to which this data
     * belongs.
     */
    private String className;

    /**
     * parent of this node.
     */
    private IDataRow parent;

    /**
     * Children data list rows. 
     */
    private List<IDataRow> children = new ArrayList<IDataRow>();

    /**
     * 
     */
    private EntityInterface entityInterface = null;

    /**
     * 
     */
    private IAssociation parentAssociation = null;

    /**
     * Is true if the dataRow contains data else it is false i.e. if 
     * the dataRow contains attribute names. 
     */
    boolean isData = true;

    /**
     * 
     */
    private IRecord record;
    
    private DataRow() {
        
    }
    
    /**
     * @param record
     * @param entity
     * @param displayName
     */
    private DataRow(IRecord record, EntityInterface entity,String displayName) {
        this.record = record;
        this.entityInterface = entity;
        this.className = displayName;
    }

    public DataRow(IRecord record, EntityInterface entity) {
        this(record,entity,"");

        if (entity != null) {
            this.className = Utility.getDisplayName(entity);
        }
    }

    public String getId() {
        String id = null;
        if (this.record != null) {
            id = this.record.getRecordId().getId();
        }
        return id;
    }

    /**
     * @return Returns the attributes.
     */
    public EntityInterface getEntity() {
        return this.entityInterface;
    }

    /**
     * @return Returns the childNodes.
     */
    public Vector getChildNodes() {
        return new Vector(children);
    }

    /**
     * @return Returns the parent.
     */
    public IDataRow getParent() {
        return parent;
    }

    /**
     * @param parent The parent to set.
     */
    public void setParent(IDataRow parent) {
        this.parent = parent;
    }

    /**
     * @return Returns the children.
     */
    public List<IDataRow> getChildren() {
        return children;
    }

    /**
     * @param children The children to set.
     */
    public void setChildren(List<IDataRow> children) {
        this.children = children;
    }

    /**
     * Returns the class name.
     * @return Returns the className.
     */
    public String getClassName() {
        return className;
    }

    /**
     * @see edu.wustl.cab2b.common.datalist.IDataRow#getURL()
     */
    public String getURL() {
        String url = null;
        if (this.record != null) {
            url = this.record.getRecordId().getUrl();
        }
        return url;
    }

    /**
     * Returns true if the row contains data else returns false i.e. if the 
     * row contains attribute names.
     * @return Returns the isData.
     */
    public boolean isData() {
        return isData;
    }

    /**
     * Set true if the row contains data else set it as false. 
     * @param isData The isData to set.(true/false)
     */
    public void setData(boolean isData) {
        this.isData = isData;
    }

    public boolean equals(Object obj) {
        boolean flag = false;
        if (obj instanceof IDataRow) {
            IDataRow dataRow = (IDataRow) obj;
            if ((getId() == null && dataRow.getId() == null) && this.getClassName().equals(dataRow.getClassName())
                    && (this.getURL().compareToIgnoreCase(dataRow.getURL()) == 0)) {
                flag = true;
            } else if ((getId() == null && dataRow.getId() != null)
                    || (getId() != null && dataRow.getId() == null)) {
                flag = false;
            } else {
                /**
                 * Data rows are equal if their identifiers match and they belong to same
                 * entity and same instance of the data service
                 */
                if (this.getId().equals(dataRow.getId()) && this.getClassName().equals(dataRow.getClassName())
                        && (this.getURL().compareToIgnoreCase(dataRow.getURL()) == 0)) {
                    flag = true;
                }
            }
        }

        return flag;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int i = 0;
        if (getId() != null)
            i += getId().hashCode();

        if (getClassName() != null)
            i += getClassName().hashCode();
        return i;
    }

    @Override
    public String toString() {
        String label = this.className;

        if (getId() != null)
            label = label + getId();
        else
            label += "s";
        return label;
    }

    public IAssociation getAssociation() {
        return parentAssociation;
    }

    public void setAssociation(IAssociation association) {
        this.parentAssociation = association;
    }

    public IRecord getRecord() {
        return record;
    }
   
    /**
     * @see edu.wustl.cab2b.common.datalist.IDataRow#getTitleNode()
     */
    public IDataRow getTitleNode() {
        IDataRow titleDataRow = new DataRow(null,this.entityInterface,this.className);
        titleDataRow.setData(false);
        return titleDataRow;

    }
    
    /**
     * @see edu.wustl.cab2b.common.datalist.IDataRow#getCopy()
     */
    public IDataRow getCopy() {
        IDataRow copiedDataRow = new DataRow(this.record,this.entityInterface,this.className);
        copiedDataRow.setData(this.isData);
        copiedDataRow.setAssociation(this.parentAssociation);
        return copiedDataRow;
    }
    
    /**
     * @see edu.wustl.cab2b.common.datalist.IDataRow#addChild(edu.wustl.cab2b.common.datalist.IDataRow)
     */
    public void addChild(IDataRow childRow) {
        this.children.add(childRow);
        childRow.setParent(this);
    }
}