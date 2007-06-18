package edu.wustl.cab2b.server.datalist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.AttributeInterfaceComparator;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;

public abstract class AbstractDataListSaver<R extends IRecord> implements DataListSaver<R> {
    public static final String OLD_ENTITY_ID_TAG_NAME = "original_entity_id";

    protected EntityInterface newEntity;

    protected AbstractDataListSaver(EntityInterface oldEntity) {
        this.newEntity = createNewEntity(oldEntity);
        populateNewEntity(newEntity);
    }

    // BEGIN SAVER METHODS
    // set of nonVirtual attributes in newEntity equals (by name) set of
    // attributes in
    // record (FOR THE DEFAULT CODE).
    public Map<AbstractAttributeInterface, Object> getRecordAsMap(R record) {
        List<AttributeInterface> recordAttributes = new ArrayList<AttributeInterface>(record.getAttributes());

        List<AttributeInterface> newEntityAttributes = new ArrayList<AttributeInterface>(
                newEntity.getAttributeCollection());

        for (Iterator<AttributeInterface> iter = newEntityAttributes.iterator(); iter.hasNext();) {
            if (DataListUtil.isVirtualAttribute(iter.next())) {
                iter.remove();
            }
        }
        if (!(recordAttributes.size() == newEntityAttributes.size())) {
            throw new IllegalArgumentException();
        }
        Collections.sort(recordAttributes, new AttributeInterfaceComparator());
        Collections.sort(newEntityAttributes, new AttributeInterfaceComparator());

        Map<AbstractAttributeInterface, Object> recordsMap = new HashMap<AbstractAttributeInterface, Object>();
        for (int i = 0; i < newEntityAttributes.size(); i++) {
            recordsMap.put(newEntityAttributes.get(i), record.getValueForAttribute(recordAttributes.get(i)));
        }
        return recordsMap;
    }

    public final EntityInterface getNewEntity() {
        return this.newEntity;
    }

    private EntityInterface createNewEntity(EntityInterface oldEntity) {
        EntityInterface newEntity = getDomainObjectFactory().createEntity();
        EntityGroupInterface dataListEntityGroup = DataListUtil.getDatalistEntityGroup();
        newEntity.addEntityGroupInterface(dataListEntityGroup);
        dataListEntityGroup.addEntity(newEntity);

        newEntity.setName(oldEntity.getName());

        DynamicExtensionUtility.addTaggedValue(newEntity, OLD_ENTITY_ID_TAG_NAME, oldEntity.getId().toString());

        // TODO remove this ??? addding tagged value for display name
        DynamicExtensionUtility.addTaggedValue(newEntity,
                                               edu.wustl.cab2b.common.util.Constants.ENTITY_DISPLAY_NAME,
                                               edu.wustl.cab2b.common.util.Utility.getDisplayName(oldEntity));
        addVirtualAttributes(newEntity);

        return newEntity;
    }

    protected abstract void populateNewEntity(EntityInterface oldEntity);

    private void addVirtualAttributes(EntityInterface entity) {
        AttributeInterface idAttribute = getDomainObjectFactory().createStringAttribute();
        idAttribute.setName(DataListUtil.ID_ATTRIBUTE_NAME);
        DataListUtil.markVirtual(idAttribute);

        AttributeInterface urlAttribute = getDomainObjectFactory().createStringAttribute();
        urlAttribute.setName(DataListUtil.URL_ATTRIBUTE_NAME);
        DataListUtil.markVirtual(urlAttribute);

        entity.addAttribute(idAttribute);
        entity.addAttribute(urlAttribute);
    }

    // END SAVER METHODS
    protected final DomainObjectFactory getDomainObjectFactory() {
        return DomainObjectFactory.getInstance();
    }
}