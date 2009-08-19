package edu.wustl.cab2b.common.multimodelcategory;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;

/**
 * 
 * @author rajesh_vyas
 * @hibernate.class table="CAB2B_MMA"
 * @hibernate.cache usage="read-write"
 */
public class MultiModelAttributeImpl implements MultiModelAttribute {
    private static final long serialVersionUID = 1L;

    private Long id;

    private AttributeInterface attribute;

    private Collection<CategorialAttribute> selectedAttributes;

    private Boolean isVisible;

    /**
     * Default constructor
     */
    public MultiModelAttributeImpl() {

    }

    /**
     * @hibernate.id name="id" column="ID" type="long" length="30"
     *               unsaved-value="null" generator-class="increment"
     * @return
     */
    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public AttributeInterface getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeInterface attribute) {
        this.attribute = attribute;
    }

    public void addCategorialAttribute(CategorialAttribute categorialAttribute) {
        getCategorialAttributes().add(categorialAttribute);
    }

    public Collection<CategorialAttribute> getCategorialAttributes() {
        if (selectedAttributes == null) {
            selectedAttributes = new HashSet<CategorialAttribute>();
        }
        return selectedAttributes;
    }

    public void setCategorialAttributes(Collection<CategorialAttribute> categorialAttributes) {
        selectedAttributes = categorialAttributes;
    }

    /**
     * @hibernate.property name="isVisible" column="ISVISIBLE" type="boolean" 
     *                     not-null="false"
     */
    public Boolean isVisible() {
        return isVisible;
    }

    public void setVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * @hibernate.property name="categorialAttributeIds" column="CATEGORIAL_ATTRIBUTE_IDS" type="string" length="254"  not-null="true"
     */
    private String getCategorialAttributeIds() {
        StringBuffer categorialAttributeIds = new StringBuffer();
        int index = selectedAttributes.size();
        for (CategorialAttribute categorialAttribute : selectedAttributes) {
            categorialAttributeIds.append(categorialAttribute.getId());
            if (--index > 0) {
                categorialAttributeIds.append('_');
            }
        }

        return categorialAttributeIds.toString();
    }

    private void setCategorialAttributeIds(String categorialAttributeIds) {
        if (selectedAttributes.isEmpty()) {
            String[] tokenIds = categorialAttributeIds.split("_");
            for (String categorialAttributeId : tokenIds) {
                if (categorialAttributeId.length() > 0) {
                    CategorialAttribute attribute = new CategorialAttribute();
                    attribute.setId(Long.parseLong(categorialAttributeId));
                    selectedAttributes.add(attribute);
                }
            }
        }
    }

    @Override
    public String toString() {
        return attribute.getName();
    }
}
