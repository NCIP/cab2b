/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author 
 *@version 1.0
 */ 
package edu.common.dynamicextensions.domaininterface;



public interface TaggedValueInterface extends DynamicExtensionBaseDomainObjectInterface
{

    /**
     * Getter method for key
     * @return
     */
     String getKey();
    
     /**
      *Setter method for key 
      * @param key
      */
     void setKey(String key);
    /**
     * Getter method for value
     * @return
     */
     String getValue();
    
     /**
      * Setter method for value
      * @param value
      */
     void setValue(String value);
   

}
