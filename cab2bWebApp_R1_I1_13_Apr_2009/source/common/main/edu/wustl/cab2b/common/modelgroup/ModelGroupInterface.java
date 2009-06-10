/**
 * 
 */
package edu.wustl.cab2b.common.modelgroup;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;

/**
 * @author gaurav_mehta
 *
 */
public interface ModelGroupInterface {

    /** Returns the Model Name for a particular Model Group */
    String getModelGroupName();

    /** Returns whether that Model Group is secured or not */
    boolean isSecured();

    /** Returns the list of Entity Group for a particular Model Group */
    List<EntityGroupInterface> getEntityGroupList();
    
    /** sets the ModelGroup Name for a particular Model Group */ 
    void setModelGroupName(String modelName);
    
    /** Sets whether the Model Group is secured or not */
    void setSecured(boolean isSecured);
    
    /** Sets the EntityGroupList for a particular Model Group */
    void setEntityGroupList(List<EntityGroupInterface> entityGroupList);

}
