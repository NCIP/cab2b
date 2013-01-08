/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ObjectTypeInformationInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;

/**
 * @hibernate.joined-subclass table="DYEXTN_OBJECT_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author Rahul Ner
 *
 */
public class ObjectAttributeTypeInformation extends AttributeTypeInformation implements
        ObjectTypeInformationInterface {

    /**
     * Empty Constructor.
     */
    public ObjectAttributeTypeInformation() {

    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface#getDataType()
     */
    public String getDataType() {
        return EntityManagerConstantsInterface.OBJECT_ATTRIBUTE_TYPE;
    }
}
