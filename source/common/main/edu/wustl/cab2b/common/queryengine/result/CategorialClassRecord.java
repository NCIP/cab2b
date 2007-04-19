package edu.wustl.cab2b.common.queryengine.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

public class CategorialClassRecord implements ICategorialClassRecord {
    private Map<CategorialClass, List<ICategorialClassRecord>> childrenCategorialClassRecords;

    private Map<CategorialAttribute, String> attributesValues;

    private String id;

    private CategorialClass categorialClass;

    public CategorialClassRecord() {
        attributesValues = new HashMap<CategorialAttribute, String>();
        childrenCategorialClassRecords = new HashMap<CategorialClass, List<ICategorialClassRecord>>();
    }

    public CategorialClass getCategorialClass() {
        return categorialClass;
    }

    public void setCategorialClass(CategorialClass categorialClass) {
        this.categorialClass = categorialClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAttributesValues(
                                    Map<CategorialAttribute, String> attributesValues) {
        this.attributesValues = attributesValues;
    }

    public Map<CategorialClass, List<ICategorialClassRecord>> getChildrenCategorialClassRecords() {
        return childrenCategorialClassRecords;
    }

    public void setChildrenCategorialClassRecords(
                                                  Map<CategorialClass, List<ICategorialClassRecord>> childrenCategorialClassRecords) {
        this.childrenCategorialClassRecords = childrenCategorialClassRecords;
    }

    public Map<CategorialAttribute, String> getAttributesValues() {
        return attributesValues;
    }

    public void addCategorialClassRecords(
                                          CategorialClass catClass,
                                          List<? extends ICategorialClassRecord> catClassRecs) {
        List<ICategorialClassRecord> existingRecs = getChildrenCategorialClassRecords().get(
                                                                                            catClass);
        if (existingRecs == null) {
            getChildrenCategorialClassRecords().put(
                                                    catClass,
                                                    new ArrayList<ICategorialClassRecord>(
                                                            catClassRecs));
        } else {
            existingRecs.addAll(catClassRecs);
        }
    }
}
