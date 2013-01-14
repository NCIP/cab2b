/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.queryengine.result;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.queryobject.DataType;

public class Record implements IRecord {
    private static final long serialVersionUID = 4657684475538003175L;

    private transient Map<AttributeInterface, Object> attributesValues;

    private RecordId id;

    protected Record(Set<AttributeInterface> attributes, RecordId id) {
        if (attributes == null || attributes.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (id == null) {
            throw new IllegalArgumentException();
        }
        this.attributesValues = new HashMap<AttributeInterface, Object>(attributes.size());
        for (AttributeInterface attribute : attributes) {
            attributesValues.put(attribute, "");
        }
        this.id = id;
    }

    public RecordId getRecordId() {
        return id;
    }

    public void putValueForAttribute(AttributeInterface attribute, Object value) {
        if (!attributesValues.containsKey(attribute)) {
            throw new IllegalArgumentException("The attribute is invalid for this record.");
        }
        this.attributesValues.put(attribute, value);
    }

    public void putStringValueForAttribute(AttributeInterface attribute, String value) {
        if (!attributesValues.containsKey(attribute)) {
            throw new IllegalArgumentException("The attribute is invalid for this record.");
        }
        this.attributesValues.put(attribute, convertValueToSpecificType(attribute, value));

    }

    private Object convertValueToSpecificType(AttributeInterface attribute, String value) {
        DataType dataType = Utility.getDataType(attribute.getAttributeTypeInformation());
        return dataType.convertValue(value);
    }

    public Object getValueForAttribute(AttributeInterface attribute) {
        return attributesValues.get(attribute);
    }

    public Set<AttributeInterface> getAttributes() {
        return Collections.unmodifiableSet(attributesValues.keySet());
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        AbstractEntityCache cache = AbstractEntityCache.getCache();

        Map<Object, Object> idValueMap = new HashMap<Object, Object>();

        for (Map.Entry<AttributeInterface, Object> entry : attributesValues.entrySet()) {
            AttributeInterface attribute = entry.getKey();
            EntityInterface entity = attribute.getEntity();

            if (entity == null || !cache.isEntityPresent(entity.getId())) {
                idValueMap.put(attribute, entry.getValue());
            } else {
                Long attributeId = attribute.getId();
                idValueMap.put(entity.getId() + "_" + attributeId, entry.getValue());
            }
        }
        s.writeObject(idValueMap);
        //        s.writeBoolean(true);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        Map<?, ?> values = (Map<?, ?>) s.readObject();

        //        Map<String, String> idValues = (Map<String, String>) values;
        AbstractEntityCache cache = AbstractEntityCache.getCache();
        attributesValues = new HashMap<AttributeInterface, Object>();

        for (Map.Entry<?, ?> entry : values.entrySet()) {
            Object keyObj = entry.getKey();
            if (keyObj instanceof String) {
                String[] key = ((String) keyObj).split("_");
                Long entityId = Long.parseLong(key[0]);
                Long attributeId = Long.parseLong(key[1]);
                AttributeInterface attribute = cache.getEntityById(entityId).getAttributeByIdentifier(attributeId);
                attributesValues.put(attribute, entry.getValue());
            } else {
                attributesValues.put((AttributeInterface) keyObj, entry.getValue());
            }
        }
    }

    public void copyValuesFrom(IRecord record) {
        if (!record.getAttributes().equals(getAttributes())) {
            throw new IllegalArgumentException();
        }
        for (AttributeInterface attribute : record.getAttributes()) {
            putValueForAttribute(attribute, record.getValueForAttribute(attribute));
        }
    }

}
