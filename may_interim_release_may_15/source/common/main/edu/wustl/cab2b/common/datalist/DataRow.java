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

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.tree.TreeNodeImpl;

/**
 * Class which represents a row in the data list or the attribute names in the
 * data list. 
 * @author gautam_shetty
 */
public class DataRow extends TreeNodeImpl implements IDataRow, Serializable
{
	
	private Object id;
	
    /**
     * Name of the class to which this data
     * belongs.
     */
    private String className;
    
    /**
     * Attribute list.
     */
    private List<AttributeInterface> attributes;
//    private String[] attributes;
    
    /**
     * Row of data/attribute names.
     */
    private Object[] row;
    
    /**
     * parent of this node.
     */
    private IDataRow parent;
    
    /**
     * Children data list rows. 
     */
    private List<IDataRow> children = new ArrayList<IDataRow>();

    
    /**
     * url from where the data is got. 
     */
    private String strURL;
    
    
    private EntityInterface entityInterface = null;
    
    private IAssociation parentAssociation = null;
    
    /**
     * Is true if the dataRow contains data else it is false i.e. if 
     * the dataRow contains attribute names. 
     */
    boolean isData = true;
    
    public String getURL()
	{
		return this.strURL;
	}
    
    public void setURL(String strURL)
	{
		 this.strURL = strURL;
	}

    
    
    
	public Object getId()
	{
		return id;
	}

	
	public void setEntityInterface(EntityInterface entityInterface)
	{
		this.entityInterface = entityInterface;
	}

	/**
     * @return Returns the attributes.
     */
    public EntityInterface getEntityInterface()
    {
        return this.entityInterface;
    }
	
	
	public void setId(Object id)
	{
		this.id = id;
	}

	/**
     * @return Returns the attributes.
     */
    public List<AttributeInterface> getAttributes()
    {
        return attributes;
    }
    
    /**
     * @return Returns the childNodes.
     */
    public Vector getChildNodes()
    {
        return new Vector(children);
    }
    
    /**
     * @param attributes The attributes to set.
     */
    public void setAttributes(List<AttributeInterface> attributes)
    {
        this.attributes = attributes;
    }

    /**
     * @return Returns the parent.
     */
    public IDataRow getParent()
    {
        return parent;
    }

    /**
     * @param parent The parent to set.
     */
    public void setParent(IDataRow parent)
    {
        this.parent = parent;
    }

    /**
     * @return Returns the children.
     */
    public List<IDataRow> getChildren()
    {
        return children;
    }

    /**
     * @param children The children to set.
     */
    public void setChildren(List<IDataRow> children)
    {
        this.children = children;
    }

    /**
     * Returns the class name.
     * @return Returns the className.
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Sets the class name.
     * @param className The className to set.
     */
    public void setClassName(String className)
    {
        this.className = className;
    }

    /**
     * Returns the row of data/attribute names.
     * @return the row of data/attribute names.
     */
    public Object[] getRow()
    {
        return row;
    }

    /**
     * Sets the row of data/attribute names.
     * @param row The row of data/attribute to set.
     */
    public void setRow(Object[] row)
    {
        this.row = row;
    }

    /**
     * Returns true if the row contains data else returns false i.e. if the 
     * row contains attribute names.
     * @return Returns the isData.
     */
    public boolean isData()
    {
        return isData;
    }

    /**
     * Set true if the row contains data else set it as false. 
     * @param isData The isData to set.(true/false)
     */
    public void setData(boolean isData)
    {
        this.isData = isData;
    }
    
    public boolean equals(Object obj)
    {
        boolean flag = false;
        if (obj instanceof IDataRow)
        {
            IDataRow dataRow = (IDataRow) obj;
            if ((getId() == null && dataRow.getId()== null) 
            && 
            this.getClassName().equals(dataRow.getClassName())
            && 
            (this.strURL.compareToIgnoreCase(dataRow.getURL()) == 0))
            {
            	flag = true;
            }
            else if((getId() == null && dataRow.getId()!=null)|| (getId() == null && dataRow.getId()!=null))
            {
            	flag = false;
            }
            else
            /**
             * Data rows are equal if their identifiers match and they belong to same
             * entity and same instance of the data service
             */
            if (this.getId().equals(dataRow.getId()) 
            && 
            this.getClassName().equals(dataRow.getClassName())
            && 
            (this.strURL.compareToIgnoreCase(dataRow.getURL()) == 0))
            {
                flag = true;
            }
        }
        
        return flag;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        int i = 0;
        if(getId() != null)
            i += getId().hashCode();
        
        if (getClassName() != null)
            i +=getClassName().hashCode();
        return i;
    }
    
    @Override
    public String toString()
    {
        String label = this.className;
        
        if (this.id != null)
            label = label + this.id;
        else
            label +="s";
        return label;
    }

	public IAssociation getAssociation() {
		return parentAssociation;
	}

	public void setAssociation(IAssociation association) {
		this.parentAssociation = association;
	}
	
}