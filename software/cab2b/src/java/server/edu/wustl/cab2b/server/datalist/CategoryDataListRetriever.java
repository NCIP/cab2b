/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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

/**
 * 
 * @author srinath_k
 *
 */
public class CategoryDataListRetriever extends AbstractDataListRetriever<ICategorialClassRecord> {
    private CategoryEntityTreeParser parser;

    /**
     * Initializes field CategoryEntityTreeParser
     * @param entity 
     */
    @Override
    public void initialize(EntityInterface entity) {
        super.initialize(entity);
        parser = new CategoryEntityTreeParser(getNewEntity());
    }

    /**
     * Returns all attributes and associations of the given entity. Attributes
     * are obtained from
     * {@link AbstractDataListRetriever#getAttributesList(EntityInterface)}.
     * Associations are obtained from
     * {@link CategoryEntityTreeParser#getAssociationsForEntity()}.
     * 
     * @param entity 
     * @see edu.wustl.cab2b.server.datalist.AbstractDataListRetriever#getAttributesList(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    @Override
    protected List<AbstractAttributeInterface> getAttributesList(EntityInterface entity) {
        List<AbstractAttributeInterface> allAttributes = super.getAttributesList(entity);
        allAttributes.addAll(parser.getAssociationsForEntity().get(entity));
        return allAttributes;
    }

    /**
     * Overrriden to additionally copy the values of the associated children
     * entities as children {@link ICategorialClassRecord}s. This is achieved
     * by invoking
     * {@link AbstractDataListRetriever#getEntityRecords(EntityInterface, List)}
     * for the child entity. <br>
     * Note: The record ids of the child entity are part of the given DE record,
     * since we also fetched associations by overriding
     * {@link #getAttributesList(EntityInterface)}.<br>
     * This is essentially a depth first strategy, where for each record, all
     * its children records are fetched. The methods
     * {@link AbstractDataListRetriever#getEntityRecords(EntityInterface, List)},
     * {@link #copyOtherFields(ICategorialClassRecord, EntityRecordInterface, List, EntityInterface)}
     * form the recursive chain for this depth first fetch; the second calls the
     * first.
     * 
     * @param record
     * @param recordInterface
     * @param attributesList
     * @param entity
     * 
     * @see edu.wustl.cab2b.server.datalist.AbstractDataListRetriever#copyOtherFields(edu.wustl.cab2b.common.queryengine.result.IRecord,
     *      edu.common.dynamicextensions.entitymanager.EntityRecordInterface,
     *      java.util.List,
     *      edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    @Override
    protected void copyOtherFields(ICategorialClassRecord record, EntityRecordInterface recordInterface,
                                   List<? extends AbstractAttributeInterface> attributesList,
                                   EntityInterface entity) {
        for (CategorialClass categorialClass : record.getCategorialClass().getChildren()) {
            EntityInterface childEntity = parser.getEntityForCategorialClassId(categorialClass.getId());
            AssociationInterface association = parser.getAssociation(entity, childEntity);

            EntityRecordResultInterface value = (EntityRecordResultInterface) recordInterface.getRecordValueList().get(
                                                                                                                       attributesList.indexOf(association));
            List<ICategorialClassRecord> childRecords;
            if (value == null) {
                childRecords = new ArrayList<ICategorialClassRecord>();
            } else {
                List<Long> childRecordIds = getRecordIds(value);
                childRecords = super.getEntityRecords(childEntity, childRecordIds);
            }
            record.addCategorialClassRecords(categorialClass, childRecords);
        }
    }

    /**
     * Returns the record ids of all the records.
     * 
     * @param results
     * @return
     */
    private List<Long> getRecordIds(EntityRecordResultInterface results) {
        List<Long> recordIds = new ArrayList<Long>();
        for (EntityRecordInterface record : results.getEntityRecordList()) {
            recordIds.add(record.getRecordId());
        }
        return recordIds;
    }

    /**
     * Creates the basic {@link ICategorialClassRecord} using
     * {@link QueryResultFactory#createCategorialClassRecord(CategorialClass, Set, RecordId).
     * 
     * @param entity 
     * @param id 
     * @param attributes
     * @see edu.wustl.cab2b.server.datalist.AbstractDataListRetriever#createRecord(edu.common.dynamicextensions.domaininterface.EntityInterface,
     *      java.util.Set, edu.wustl.cab2b.common.queryengine.result.RecordId)
     */
    @Override
    protected ICategorialClassRecord createRecord(EntityInterface entity, Set<AttributeInterface> attributes,
                                                  RecordId id) {
        return QueryResultFactory.createCategorialClassRecord(parser.getOriginCategorialClassForEntity(entity),
                                                              attributes, id);
    }
}
