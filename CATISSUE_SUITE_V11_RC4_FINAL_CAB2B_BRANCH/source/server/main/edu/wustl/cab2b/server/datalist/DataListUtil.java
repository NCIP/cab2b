/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.datalist;

import static edu.wustl.cab2b.common.util.DataListUtil.ORIGIN_ENTITY_ID_KEY;
import static edu.wustl.cab2b.common.util.DataListUtil.SOURCE_ENTITY_ID_KEY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;

/**
 * Utility class for data list operations.
 * <p>
 * A virtual attribute is an attribute that is present in an entity, but not in
 * the attribute-values map of an {@link IRecord}. Thus, the fields of a
 * {@link RecordId}, viz. service-url and id, are virtual attributes. Whenever
 * a record has extra info (outside of the values map) to be stored, the extra
 * info can be represented as a virtual attribute of the newly created entity.
 * 
 * @author srinath_k
 */
public class DataListUtil {
    /**
     * Cache the entity group that contains all data lists. This entity group is
     * identified by its name
     * {@link edu.wustl.cab2b.common.util.Constants.DATALIST_ENTITY_GROUP_NAME}.
     */ //default scope for testing purpose 
    static EntityGroupInterface dataListEntityGroup = null;

    /**
     * A virtual attribute is identified by tagging it with this key.
     */
    static final String VIRTUAL_ATTRIBUTE_IND = "virtual";

    /**
     * The attribute name for the service-url of {@link RecordId}.
     */
    static final String URL_ATTRIBUTE_NAME = "virtual_url_attr";

    /**
     * The attribute name for the id of {@link RecordId}.
     */
    static final String ID_ATTRIBUTE_NAME = "virtual_id_attr";

    /**
     * Primarily casts the value in the input map to the return type. If the
     * given association is not present as key in the input map, then an entry
     * for this association is added. The primary motivation for this method was
     * to localize the unchecked cast.
     * 
     * @param map the map with associated records.
     * @param association the association that is the key in the input map.
     * @return if given association existed in the map, the corresponding value;
     *         otherwise the empty list corresponding to the newly added entry
     *         for the association.
     */
    public static List<Map<AbstractAttributeInterface, Object>> getAssociatedRecordsList(
                                                                                         Map<AbstractAttributeInterface, Object> map,
                                                                                         AssociationInterface association) {
        Object existingRecords = map.get(association);
        if (existingRecords == null) {
            existingRecords = new ArrayList<Map<AbstractAttributeInterface, Object>>();
            map.put(association, existingRecords);
        }
        List<Map<AbstractAttributeInterface, Object>> existingRecordsList = (List<Map<AbstractAttributeInterface, Object>>) existingRecords;
        return existingRecordsList;
    }

    /**
     * @return the entity group that contains the data lists.
     */
    public static EntityGroupInterface getDatalistEntityGroup() {
        if (dataListEntityGroup == null) {
            dataListEntityGroup = DynamicExtensionUtility.getEntityGroupByName(edu.wustl.cab2b.common.util.Constants.DATALIST_ENTITY_GROUP_NAME);
        }
        return dataListEntityGroup;
    }

    /**
     * Create a new association from srcEntity to targetEntity. Currently
     * creates a one-many association.
     * 
     * @param srcEntity
     * @param targetEntity
     * @return the newly created association obtained using
     *         DynamicExtensionUtility#createNewOneToManyAsso(EntityInterface,
     *         EntityInterface)
     */
    public static AssociationInterface createAssociation(EntityInterface srcEntity, EntityInterface targetEntity) {
        return DynamicExtensionUtility.createNewOneToManyAsso(srcEntity, targetEntity);
    }

    /**
     * Returns the virtual id attribute of given entity.
     * 
     * @param entity
     * @return the id attribute
     */
    public static AttributeInterface getVirtualIdAttribute(EntityInterface entity) {
        return getAttributeByName(entity, ID_ATTRIBUTE_NAME);
    }

    /**
     * Returns the virtual url attribute of given entity.
     * 
     * @param entity
     * @return the id attribute
     */
    public static AttributeInterface getVirtualUrlAttribute(EntityInterface entity) {
        return getAttributeByName(entity, URL_ATTRIBUTE_NAME);
    }

    /**
     * Determines if given attribute is a virtual attribute.
     * 
     * @param attribute
     * @return true if the attribute is tagged with
     *         {@link #VIRTUAL_ATTRIBUTE_IND}; false otherwise.
     */
    public static boolean isVirtualAttribute(AttributeInterface attribute) {
        return Utility.getTaggedValue(attribute.getTaggedValueCollection(), VIRTUAL_ATTRIBUTE_IND) != null;
    }

    /**
     * Returns the {@link AttributeInterface} corresponding to given entity and
     * attribute name.
     * 
     * @param entity
     * @param attributeName
     * @return {@link AttributeInterface} if found; null if no attribute with
     *         given name is present for the entity.
     */
    public static AttributeInterface getAttributeByName(EntityInterface entity, String attributeName) {
        return getAttributeByName(entity.getAttributeCollection(), attributeName);
    }

    /**
     * Locates the {@link AttributeInterface} corresponding to attribute name in
     * the collection.
     * 
     * @param entity
     * @param attributeName
     * @return {@link AttributeInterface} if found; null if no attribute with
     *         given name is present in the collection.
     */
    public static AttributeInterface getAttributeByName(Collection<AttributeInterface> attributes,
                                                        String attributeName) {
        for (AttributeInterface attribute : attributes) {
            if (attribute.getName().equals(attributeName)) {
                return attribute;
            }
        }
        return null;
    }

    /**
     * Marks an attribute as virtual by tagging it with key={@link #VIRTUAL_ATTRIBUTE_IND}
     * (value is immaterial).
     * 
     * @param attribute
     */
    public static void markVirtual(AttributeInterface attribute) {
        DynamicExtensionUtility.addTaggedValue(attribute, DataListUtil.VIRTUAL_ATTRIBUTE_IND, "");
    }

    /**
     * Returns a clone of the old attribute.
     * 
     * @param oldAttribute
     * @return new attribute as obtained from
     *         {@link DynamicExtensionUtility#getAttributeCopy(AttributeInterface)}.
     */
    public static AttributeInterface createNewAttribute(AttributeInterface oldAttribute) {
        return DynamicExtensionUtility.getAttributeCopy(oldAttribute);
    }

    /**
     * Copies all the non-virtual attributes from the old entity to the new
     * entity.
     * 
     * @param newEntity
     * @param oldEntity
     */
    public static void copyNonVirtualAttributes(EntityInterface newEntity, EntityInterface oldEntity) {
        Collection<AttributeInterface> oldAttribs = oldEntity.getAttributeCollection();
        for (AttributeInterface oldAttrib : oldAttribs) {
            if (isVirtualAttribute(oldAttrib)) {
                continue;
            }
            AttributeInterface newAttrib = DataListUtil.createNewAttribute(oldAttrib);
            newEntity.addAttribute(newAttrib);
        }
    }
    
    public static void addVirtualAttributes(EntityInterface entity) {
        AttributeInterface idAttribute = getDomainObjectFactory().createStringAttribute();
        idAttribute.setName(DataListUtil.ID_ATTRIBUTE_NAME);
        DataListUtil.markVirtual(idAttribute);

        AttributeInterface urlAttribute = getDomainObjectFactory().createStringAttribute();
        urlAttribute.setName(DataListUtil.URL_ATTRIBUTE_NAME);
        DataListUtil.markVirtual(urlAttribute);

        entity.addAttribute(idAttribute);
        entity.addAttribute(urlAttribute);
    }
    
    protected static  final DomainObjectFactory getDomainObjectFactory() {
        return DomainObjectFactory.getInstance();
    }
    
    public static  EntityInterface createNewEntity(EntityInterface oldEntity) {
        EntityInterface newEntity = getDomainObjectFactory().createEntity();
        EntityGroupInterface dataListEntityGroup = DataListUtil.getDatalistEntityGroup();
        newEntity.setEntityGroup(dataListEntityGroup);
        dataListEntityGroup.addEntity(newEntity);

        newEntity.setName(oldEntity.getName());

        DynamicExtensionUtility.addTaggedValue(newEntity, ORIGIN_ENTITY_ID_KEY,
                                               getOriginEntityId(oldEntity).toString());
        DynamicExtensionUtility.addTaggedValue(newEntity, SOURCE_ENTITY_ID_KEY, oldEntity.getId().toString());

        DynamicExtensionUtility.addTaggedValue(newEntity,
                                               edu.wustl.cab2b.common.util.Constants.ENTITY_DISPLAY_NAME,
                                               edu.wustl.cab2b.common.util.Utility.getDisplayName(oldEntity));
        addVirtualAttributes(newEntity);

        return newEntity;
    }
    
    private static Long getOriginEntityId(EntityInterface oldEntity) {
        return edu.wustl.cab2b.common.util.DataListUtil.getOriginEntity(oldEntity).getId();
    }
    //for testing purpose
    static void setDataListEntityGroup(EntityGroupInterface group) {
        dataListEntityGroup = group;
    }
}
