package edu.wustl.cab2b.common.datalist;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.tree.TreeNode;


public interface IDataRow extends TreeNode
{
    /**
     * @return Returns the attributes.
     */
    public List<AttributeInterface> getAttributes();

    /**
     * @param attributes The attributes to set.
     */
    public void setAttributes(List<AttributeInterface> attributes);

    /**
     * @return Returns the parent.
     */
    public IDataRow getParent();
    
    /**
     * @param parent The parent to set.
     */
    public void setParent(IDataRow parent);

    /**
     * @return Returns the children.
     */
    public List<IDataRow> getChildren();

    /**
     * @param children The children to set.
     */
    public void setChildren(List<IDataRow> children);

    /**
     * Returns the class name.
     * @return Returns the className.
     */
    public String getClassName();

    /**
     * Sets the class name.
     * @param className The className to set.
     */
    public void setClassName(String className);

    /**
     * Returns the row of data/attribute names.
     * @return the row of data/attribute names.
     */
    public Object[] getRow();

    /**
     * Sets the row of data/attribute names.
     * @param row The row of data/attribute to set.
     */
    public void setRow(Object[] row);

    /**
     * Returns true if the row contains data else returns false i.e. if the 
     * row contains attribute names.
     * @return Returns the isData.
     */
    public boolean isData();

    /**
     * Set true if the row contains data else set it as false. 
     * @param isData The isData to set.(true/false)
     */
    public void setData(boolean isData);
    
    public Object getId();
    
    public void setId(Object id);
    
    
    public String getURL();
    
    public void setURL(String strURL);
    
    public void setAssociation(IAssociation association);

    public IAssociation getAssociation();
    
    public EntityInterface getEntityInterface();
    
    public void setEntityInterface(EntityInterface entityInterface);
    
    IRecord getRecord();
    
    void setRecord(IRecord record);
    
    
}
