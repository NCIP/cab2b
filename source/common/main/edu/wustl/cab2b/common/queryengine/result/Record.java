package edu.wustl.cab2b.common.queryengine.result;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

public class Record implements IRecord {
    private static final long serialVersionUID = 4657684475538003175L;

    private Map<AttributeInterface, String> attributesValues;

    private String id;

    protected Record(Set<AttributeInterface> attributes, String id) {
        if (attributes == null || attributes.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.attributesValues = new HashMap<AttributeInterface, String>(attributes.size());
        for (AttributeInterface attribute : attributes) {
            attributesValues.put(attribute, "");
        }
        this.id = id;
    }

    public String getId() {
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
}
