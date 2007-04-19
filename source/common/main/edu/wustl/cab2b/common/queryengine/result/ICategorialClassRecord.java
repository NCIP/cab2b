package edu.wustl.cab2b.common.queryengine.result;

import java.util.List;
import java.util.Map;

import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

public interface ICategorialClassRecord {

    String getId();

    Map<CategorialAttribute, String> getAttributesValues();

    Map<CategorialClass, List<ICategorialClassRecord>> getChildrenCategorialClassRecords();

    CategorialClass getCategorialClass();
}