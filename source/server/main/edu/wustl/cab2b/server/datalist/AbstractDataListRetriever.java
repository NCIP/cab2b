package edu.wustl.cab2b.server.datalist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityRecordInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.RecordId;

public abstract class AbstractDataListRetriever<R extends IRecord> implements DataListRetriever<R> {
    protected EntityInterface newEntity;

    protected AbstractDataListRetriever(EntityInterface newEntity) {
        this.newEntity = newEntity;
    }

    public final List<R> getEntityRecords(List<Long> recordIds) {
        return getEntityRecords(newEntity, recordIds);
    }

    protected List<R> getEntityRecords(EntityInterface entity, List<Long> recordIds) {
        try {
            EntityRecordResultInterface recordResultInterface = EntityManager.getInstance().getEntityRecords(
                                                                                                             entity,
                                                                                                             getAttributesList(entity),
                                                                                                             recordIds);

            return transformToRecords(recordResultInterface, entity);
        } catch (DynamicExtensionsSystemException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DE_0004));
        }
    }

    private List<R> transformToRecords(EntityRecordResultInterface recordResultInterface, EntityInterface entity) {
        List<R> res = new ArrayList<R>();
        List<? extends AbstractAttributeInterface> attributesList = recordResultInterface.getEntityRecordMetadata().getAttributeList();
        int idIndex = attributesList.indexOf(DataListUtil.getVirtualIdAttribute(entity));
        int urlIndex = attributesList.indexOf(DataListUtil.getVirtualUrlAttribute(entity));

        Set<AttributeInterface> recordAttributes = getAttributes(attributesList);

        for (EntityRecordInterface recordInterface : recordResultInterface.getEntityRecordList()) {
            List values = recordInterface.getRecordValueList();
            String id = values.get(idIndex).toString();
            String url = values.get(urlIndex).toString();
            RecordId recordId = new RecordId(id, url);
            R record = createRecord(entity, recordAttributes, recordId);
            res.add(record);
            for (int i = 0; i < attributesList.size(); i++) {
                if (recordAttributes.contains(attributesList.get(i))) {
                    AttributeInterface attribute = (AttributeInterface) attributesList.get(i);
                    record.putValueForAttribute(attribute, values.get(i).toString());
                }
            }
            copyOtherFields(record, recordInterface, attributesList, entity);
        }
        return res;
    }

    protected List<AbstractAttributeInterface> getAttributesList(EntityInterface entity) {
        List<AbstractAttributeInterface> res = new ArrayList<AbstractAttributeInterface>();
        res.addAll(entity.getAttributeCollection());
        return res;
    }

    protected void copyOtherFields(R record, EntityRecordInterface recordInterface,
                                   List<? extends AbstractAttributeInterface> attributesList,
                                   EntityInterface entity) {
        // hook
    }

    protected abstract R createRecord(EntityInterface entity, Set<AttributeInterface> attributes, RecordId id);

    private Set<AttributeInterface> getAttributes(List<? extends AbstractAttributeInterface> attributesList) {
        Set<AttributeInterface> attributesSet = new HashSet<AttributeInterface>();
        for (int i = 0; i < attributesList.size(); i++) {
            AbstractAttributeInterface abstractAttribute = attributesList.get(i);
            if (abstractAttribute instanceof AttributeInterface) {
                AttributeInterface attribute = (AttributeInterface) abstractAttribute;
                if (!DataListUtil.isVirtualAttribute(attribute)) {
                    attributesSet.add(attribute);
                }
            }
        }
        return attributesSet;
    }

    protected final DomainObjectFactory getDomainObjectFactory() {
        return DomainObjectFactory.getInstance();
    }
}
