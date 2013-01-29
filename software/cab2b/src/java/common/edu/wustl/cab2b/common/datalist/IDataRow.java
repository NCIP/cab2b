/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.datalist;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.tree.TreeNode;


public interface IDataRow extends TreeNode
{
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
    
    /**
     * @return
     */
    public String getURL();
    
    
    /**
     * @param association
     */
    public void setAssociation(IAssociation association);

    /**
     * @return
     */
    public IAssociation getAssociation();
    
    /**
     * @return
     */
    public EntityInterface getEntity();
    
    /**
     * @return
     */
    IRecord getRecord();
    
    /**
     * @return
     */
    String getId();
    
    /**
     * @return
     */
    IDataRow getTitleNode(); 
    
    /**
     * returns copy of this node, which does not have any parent or child set
     * @return
     */
    IDataRow getCopy();
    
    void addChild(IDataRow childRow);
}
