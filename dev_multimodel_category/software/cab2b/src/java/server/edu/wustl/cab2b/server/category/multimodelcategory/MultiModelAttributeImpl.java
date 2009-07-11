package edu.wustl.cab2b.server.category.multimodelcategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelAttribute;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryCache;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;

/**
 * 
 * @author rajesh_vyas
 * @hibernate.class table="CAB2B_MMA"
 * @hibernate.cache usage="read-write"
 */
public class MultiModelAttributeImpl implements MultiModelAttribute {

    private static final String DELIMETER = "_";

    private Long id;

    private AttributeInterface multiModelAttribute;

    private Long attributeId;

    private Collection<CategorialAttribute> selectedAttributes;

    private String catAttrDelimitedIds;

    private Boolean isVisible;

    public MultiModelAttributeImpl() {
        selectedAttributes = new ArrayList<CategorialAttribute>();
    }

    public void addCategorialAttribute(CategorialAttribute categorialAttribute) {
        selectedAttributes.add(categorialAttribute);
    }

    public AttributeInterface getAttribute() {
        return multiModelAttribute;
    }

    public CategorialAttribute getCategorialAttribute(int index) {
        return ((List<CategorialAttribute>) selectedAttributes).get(index);
    }

    public Collection<CategorialAttribute> getCategorialAttributes() {
        return selectedAttributes;
    }

    /**
     * @hibernate.property name="isVisible" column="ISVISIBLE" type="boolean" 
     *                     not-null="false"
     */
    public Boolean isVisible() {
        return isVisible;
    }

    public void setAttribute(AttributeInterface attribute) {
        multiModelAttribute = attribute;
    }

    public void setCategorialAttributes(Collection<CategorialAttribute> categorialAttributes) {
        selectedAttributes.addAll(categorialAttributes);
    }

    public void setVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * @hibernate.property  name="multiModelAttribute" column="DE_ATTRIBUTE_ID" type="long" length="30" unsaved-value="null"
     * @return
     */
    Long getAttributeId() {
        return multiModelAttribute.getId();
    }

    void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
        this.multiModelAttribute = EntityCache.getInstance().getAttributeById(attributeId);
    }

    /**
     * @hibernate.property name="catAttrDelimitedIds" column="CATEGORIAL_ATTRIBUTE_IDS" type="string" length="254"  not-null="true"
     */
    private String getCatAttrDelimitedIds() {
        StringBuffer delimIds = new StringBuffer();
        for (CategorialAttribute ca : selectedAttributes) {
            delimIds.append(ca.getId()).append(DELIMETER);
        }

        catAttrDelimitedIds = delimIds.toString();
        return catAttrDelimitedIds;
    }

    private void setCatAttrDelimitedIds(String catAttrDelimitedIds) {
        this.catAttrDelimitedIds = catAttrDelimitedIds;

        if (selectedAttributes.isEmpty()) {
            for (String categorialAttrId : catAttrDelimitedIds.split(DELIMETER)) {
                CategorialAttribute attribute = new CategorialAttribute();
                attribute.setId(Long.parseLong(categorialAttrId));
                selectedAttributes.add(attribute);
            }
        }
    }

    /**
     * @hibernate.id name="id" column="ID" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="ID_SEQ"
     * @return
     */
    private Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }
}
