/**
 * 
 */

package edu.wustl.common.querysuite.metadata.associations;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * @author prafull_kadam Interface to represent Inter model Association.
 */
public interface IMetadataInterModelAssociation extends IAssociation {

    /**
     * @return the attribute from source entity.
     */
    AttributeInterface getSourceAttribute();

    /**
     * 
     * @return the attribute from target entity.
     */
    AttributeInterface getTargetAttribute();
}
