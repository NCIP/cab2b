/**
 * 
 */
package edu.wustl.cab2b.common.modelgroup;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;

/**
 * This is an Interface of Model Group. This interface is to be used ant not the concrete class.
 * @author gaurav_mehta
 *
 */
public interface ModelGroupInterface {

    /** Returns the Model Name for a particular Model Group */
    String getModelGroupName();

    /** Returns whether that Model Group is secured or not */
    boolean isSecured();
    
    /** Returns the Model Group Description */ 
    String getModelDescription();

    /** Returns the list of Entity Group for a particular Model Group */
    List<EntityGroupInterface> getEntityGroupList();
    
    /** sets the ModelGroup Name for a particular Model Group */ 
    void setModelGroupName(String modelName);
    
    /** Sets whether the Model Group is secured or not */
    void setSecured(boolean isSecured);
    
    /** Sets the Model Description for given Model */
    void setModelDescription(String modelDescription);
    
    /** Sets the EntityGroupList for a particular Model Group */
    void setEntityGroupList(List<EntityGroupInterface> entityGroupList);

}
