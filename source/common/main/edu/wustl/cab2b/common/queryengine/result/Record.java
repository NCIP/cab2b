package edu.wustl.cab2b.common.queryengine.result;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;

public class Record implements IRecord {
    private static final long serialVersionUID = 4657684475538003175L;

    private transient Map<AttributeInterface, String> attributesValues;

    private RecordId id;

    protected Record(Set<AttributeInterface> attributes, RecordId id) {
        if (attributes == null || attributes.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.attributesValues = new HashMap<AttributeInterface, String>(attributes.size());
        for (AttributeInterface attribute : attributes) {
            attributesValues.put(attribute, "");
        }
        this.id = id;
    }

    public RecordId getRecordId() {
        return id;
    }

    public void putValueForAttribute(AttributeInterface attribute, String value) {
        if (!attributesValues.containsKey(attribute)) {
            throw new IllegalArgumentException("The attribute is invalid for this record.");
        }
        this.attributesValues.put(attribute, value);
    }

    public String getValueForAttribute(AttributeInterface attribute) {
        return attributesValues.get(attribute);
    }

    public Set<AttributeInterface> getAttributes() {
        return Collections.unmodifiableSet(attributesValues.keySet());
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(serializeValues());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        Map<String, String> values = (Map<String, String>) s.readObject();
        deserializeValues(values);
    }

    private Map<String, String> serializeValues() {
        Map<String, String> res = new HashMap<String, String>();
        for (Map.Entry<AttributeInterface, String> entry : attributesValues.entrySet()) {
            AttributeInterface attribute = entry.getKey();
            res.put(attribute.getEntity().getId() + "_" + attribute.getId(), entry.getValue());
        }
        return res;
    }

    private void deserializeValues(Map<String, String> values) {
        attributesValues = new HashMap<AttributeInterface, String>();
        AbstractEntityCache cache = AbstractEntityCache.getCache();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String[] key = entry.getKey().split("_");
            Long entityId = Long.parseLong(key[0]);
            Long attributeId = Long.parseLong(key[1]);
            AttributeInterface attribute = cache.getEntityById(entityId).getAttributeByIdentifier(attributeId);

            attributesValues.put(attribute, entry.getValue());
        }
    }
}
