/**
 * 
 */
package edu.wustl.cab2b.common.queryengine;

import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;

/**
 * @author chetan_patil
 *
 */
public interface KeywordQuery extends CompoundQuery {
    
    /**
     * This method returns the corresponding application group
     * @return
     */
    ModelGroupInterface getApplicationGroup();
    
    /**
     * This method sets the given application group.
     * @param applicationGroup
     */
    void setApplicationGroup(ModelGroupInterface applicationGroup);

}
