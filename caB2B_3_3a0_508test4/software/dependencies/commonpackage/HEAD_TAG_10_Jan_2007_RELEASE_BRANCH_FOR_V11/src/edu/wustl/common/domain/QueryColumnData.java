/*
 * Created on Aug 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author gautam_shetty
 * @hibernate.class table="CATISSUE_INTERFACE_COLUMN_DATA"
 */
public class QueryColumnData extends AbstractDomainObject implements Serializable
{
    private long identifier;

    private QueryTableData tableData = new QueryTableData();

    private String columnName;

    private String displayName;

    private String columnType;

    /**
     * Returns the id.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_INTF_COLUMN_DATA_SEQ"
     * @return Returns the identifier.
     */
    public long getIdentifier()
    {
        return identifier;
    }

    /**
     * @param identifier The identifier to set.
     */
    public void setIdentifier(long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * @hibernate.many-to-one column="TABLE_ID" class="edu.wustl.common.domain.QueryTableData"
     * constrained="true"
     * @return Returns the tableData.
     */
    public QueryTableData getTableData()
    {
        return tableData;
    }

    /**
     * @return Returns the columnType.
     */
    public String getColumnType()
    {
        return columnType;
    }

    /**
     * @param columnType The columnType to set.
     */
    public void setColumnType(String columnType)
    {
        this.columnType = columnType;
    }

    /**
     * @param tableData The tableData to set.
     */
    public void setTableData(QueryTableData tableData)
    {
        this.tableData = tableData;
    }

    /**
     * @hibernate.property name="columnName" type="string" column="COLUMN_NAME" length="50"
     * @return Returns the tableName.
     */
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * @param tableName The tableName to set.
     */
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    /**
     * @hibernate.property name="displayName" type="string" column="DISPLAY_NAME" length="50"
     * @return Returns the displayName.
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * @param displayName The displayName to set.
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(IValueObject abstractForm)
            throws AssignDataException
    {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#getId()
     */
    public Long getId()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setId(java.lang.Long)
     */
    public void setId(Long id)
    {
    }   
}