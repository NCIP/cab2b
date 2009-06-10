package edu.wustl.cab2b.common.queryengine.result;

import java.io.Serializable;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * Represents a single record. A record is conceptually a set of attribute-value
 * pairs. The {@link java.util.Set} of attributes for a record is specified
 * while creating the record and cannot be modified. If the record belongs to an
 * {@link edu.wustl.cab2b.common.queryengine.result.IQueryResult}, then the
 * attributes of the record should be a subset of the attributes of the
 * outputEntity of the result; but the API itself does not validate this.
 * 
 * @see edu.wustl.cab2b.common.queryengine.result.QueryResultFactory#createRecord(Set,
 *      String)
 * @author srinath_k
 */
public interface IRecord extends Serializable {
    /**
     * Returns the "identifier" for this record.
     * 
     * @return the id of this record.
     */
    RecordId getRecordId();

    /**
     * Specify the value for an attribute.
     * 
     * @param attribute the attribute.
     * @param value value of the attribute. This is of the proper data type
     */
    void putValueForAttribute(AttributeInterface attribute, Object value);
    
    /**
     * Specify the value for an attribute.
     * 
     * @param attribute the attribute.
     * @param value value of the attribute. This is of the proper data type
     */
    void putStringValueForAttribute(AttributeInterface attribute, String value);
    
    /**
     * Returns the value for the attribute.
     * 
     * @param attribute the attribute
     * @return the value for the attribute.
     */
    Object getValueForAttribute(AttributeInterface attribute);

    /**
     * @return An unmodifiable set view of the attributes.
     */
    Set<AttributeInterface> getAttributes();

    void copyValuesFrom(IRecord record);
}
