package edu.wustl.cab2b.server.category.multimodelcategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelAttribute;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;

/**
 * 
 * @author rajesh_vyas
 * @hibernate.class table="CAB2B_MMA"
 * @hibernate.cache usage="read-write"
 */
public class MultiModelAttributeImpl implements MultiModelAttribute {

    private Long id;

    private AttributeInterface attribute;

    private Collection<CategorialAttribute> selectedAttributes;

    private Boolean isVisible;

    public MultiModelAttributeImpl() {
        selectedAttributes = new ArrayList<CategorialAttribute>();
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
        selectedAttributes.add(categorialAttribute);
    }

    public CategorialAttribute getCategorialAttribute(int index) {
        return ((List<CategorialAttribute>) selectedAttributes).get(index);
    }

    public Collection<CategorialAttribute> getCategorialAttributes() {
        return selectedAttributes;
    }

    public void setCategorialAttributes(Collection<CategorialAttribute> categorialAttributes) {
        selectedAttributes.addAll(categorialAttributes);
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
     * @hibernate.property  name="attributeId" column="MMA_ATTRIBUTE_ID" type="long" length="30" unsaved-value="null"
     * @return
     */
    private Long getAttributeId() {
        return attribute.getId();
    }

    private void setAttributeId(Long attributeId) {
        this.attribute = EntityCache.getInstance().getAttributeById(attributeId);
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
                CategorialAttribute attribute = new CategorialAttribute();
                attribute.setId(Long.parseLong(categorialAttributeId));
                selectedAttributes.add(attribute);
            }
        }
    }

}
