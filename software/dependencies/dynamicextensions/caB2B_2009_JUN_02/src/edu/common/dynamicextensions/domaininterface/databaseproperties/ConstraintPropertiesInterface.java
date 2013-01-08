/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface.databaseproperties;


/**
 * These are the data base properties for an association.
 * @author geetika_bangard
 */
public interface ConstraintPropertiesInterface extends DatabasePropertiesInterface
{
    
    /**
     * This method returns the key of the source Entity. 
     * @return the key of the source Entity. 
     */
    String getSourceEntityKey();
    
    /**
     * This method sets the key of the source Entity. 
     * @param sourceEntityKey the key of the source Entity to be set. 
     */
    void setSourceEntityKey(String sourceEntityKey);
    
    /**
     * This method returns the key of the target Entity. 
     * @return the key of the source Entity. 
     */
    String getTargetEntityKey();
    
    /**
     * This method sets the key of the target Entity. 
     * @param targetEntityKey the key of the source Entity to be set.
     */
    void setTargetEntityKey(String targetEntityKey);

}
