package edu.wustl.cab2b.common.category;

import java.io.Serializable;

import edu.wustl.common.querysuite.metadata.category.AbstractCategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;

/**
 * 
 * @author atul_jawale
 * @hibernate.joined-subclass table="DATA_CATEGORIAL_ATTRIBUTE"
 * @hibernate.joined-subclass-key column="ID" 
 */
public class DataCategorialAttribute extends AbstractCategorialAttribute implements Serializable {

    public DataCategorialAttribute() {
        // TODO Auto-generated method stub

    }

    /**
     * This constructor takes categorialatrribute and copies all the values from
     * categorialatrribute to a new Datacategorialatrribute .
     * Mainly used to create DataCategory object from Category
     * @param categorialAttribute
     */
    public DataCategorialAttribute(CategorialAttribute categorialAttribute) {
        super();
        this.setDeSourceClassAttributeId(categorialAttribute.getDeSourceClassAttributeId());
        this.setSourceClassAttribute(categorialAttribute.getCategoryAttribute());
    }

}
