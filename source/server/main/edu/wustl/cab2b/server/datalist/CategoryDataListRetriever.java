package edu.wustl.cab2b.server.datalist;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;

public class CategoryDataListRetriever extends AbstractDataListRetriever<ICategorialClassRecord> {
    private CategoryEntityTreeParser parser;

    @Override
    public void initialize(EntityInterface entity) {
        super.initialize(entity);
        parser = new CategoryEntityTreeParser(newEntity);
    }

    @Override
    protected List<AbstractAttributeInterface> getAttributesList(EntityInterface entity) {
        List<AbstractAttributeInterface> allAttributes = super.getAttributesList(entity);
        allAttributes.addAll(parser.getAssociationsForEntity().get(entity));
        return allAttributes;
    }

    @Override
    protected void copyOtherFields(ICategorialClassRecord record, EntityRecordInterface recordInterface,
                                   List<? extends AbstractAttributeInterface> attributesList,
                                   EntityInterface entity) {
        for (CategorialClass categorialClass : record.getCategorialClass().getChildren()) {
            EntityInterface childEntity = parser.getEntityForCategorialClassId(categorialClass.getId());
            AssociationInterface association = parser.getAssociation(entity, childEntity);

            EntityRecordResultInterface value = (EntityRecordResultInterface) recordInterface.getRecordValueList().get(
                                                                                                                       attributesList.indexOf(association));
            List<Long> childRecordIds = getRecordIds(value);
            List<ICategorialClassRecord> childRecords = super.getEntityRecords(childEntity, childRecordIds);
            record.addCategorialClassRecords(categorialClass, childRecords);
        }
    }

    private List<Long> getRecordIds(EntityRecordResultInterface results) {
        List<Long> recordIds = new ArrayList<Long>();
        for (EntityRecordInterface record : results.getEntityRecordList()) {
            recordIds.add(record.getRecordId());
        }
        return recordIds;
    }

    @Override
    protected ICategorialClassRecord createRecord(EntityInterface entity, Set<AttributeInterface> attributes,
                                                  RecordId id) {
        return QueryResultFactory.createCategorialClassRecord(parser.getOriginCategorialClassForEntity(entity),
                                                              attributes, id);
    }
}
