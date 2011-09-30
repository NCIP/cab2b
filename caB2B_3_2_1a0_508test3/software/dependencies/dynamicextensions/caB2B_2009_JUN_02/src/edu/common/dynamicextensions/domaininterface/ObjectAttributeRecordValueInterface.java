package edu.common.dynamicextensions.domaininterface;

import java.io.IOException;

import edu.common.dynamicextensions.domain.ObjectAttributeRecordValue;

/**
 * @author rahul_ner
 *
 */

public interface ObjectAttributeRecordValueInterface extends DynamicExtensionBaseDomainObjectInterface {

    /**
     * @return
     */
    Long getId();

    /**
     * @return
     */
    String getClassName();

    /**
     * @param className
     */
    void setClassName(String className);

    /**
     * @return
     */
    Object getObject();

    /**
     * @param value
     * @throws IOException 
     */
    void setObject(Object value) throws IOException;

    /**
     * @param objectRecordValue
     */
    void copyValues(ObjectAttributeRecordValue objectRecordValue);

}
