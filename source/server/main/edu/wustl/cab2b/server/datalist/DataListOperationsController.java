package edu.wustl.cab2b.server.datalist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.cache.DatalistCache;

public class DataListOperationsController {

    private DataListOperationsController() {

    }

    public static Long save(DataList dataListToSave) {
        Map<EntityPair, AssociationInterface> associationForEntities = new HashMap<EntityPair, AssociationInterface>();
        Map<EntityInterface, DataListSaver<IRecord>> oldEntityToSaver = new HashMap<EntityInterface, DataListSaver<IRecord>>();
        Map<IDataRow, Map<AbstractAttributeInterface, Object>> dataRowToRecordsMap = new HashMap<IDataRow, Map<AbstractAttributeInterface, Object>>();

        EntityGroupInterface dataListEntityGroup = DataListUtil.getDatalistEntityGroup();
        // root stuff
        final EntityInterface rootEntity = getDomainObjectFactory().createEntity();
        rootEntity.setName("DataList_" + System.currentTimeMillis());
        rootEntity.addEntityGroupInterface(dataListEntityGroup);
        dataListEntityGroup.addEntity(rootEntity);

        oldEntityToSaver.put(null, new DataListSaver<IRecord>() {

            public EntityInterface getNewEntity() {
                return rootEntity;
            }

            public Map<AbstractAttributeInterface, Object> getRecordAsMap(IRecord record) {
                return null;
            }

        });
        IDataRow rootRecordDataRow = dataListToSave.getRootDataRow();
        dataRowToRecordsMap.put(rootRecordDataRow, new HashMap<AbstractAttributeInterface, Object>());
        // end root stuff.

        // init to first class level.
        List<IDataRow> currDataRows = new ArrayList<IDataRow>();
        currDataRows.add(rootRecordDataRow);

        while (!currDataRows.isEmpty()) {
            List<IDataRow> nextDataRows = new ArrayList<IDataRow>();
            for (IDataRow classDataRow : currDataRows) {
                EntityInterface oldEntity = classDataRow.getEntityInterface();
                DataListSaver<IRecord> saver = oldEntityToSaver.get(oldEntity);
                if (saver == null) {
                    saver = (DataListSaver<IRecord>) DataListOperationsFactory.createDataListSaver(oldEntity);
                    oldEntityToSaver.put(oldEntity, saver);
                }
                EntityInterface newEntity = saver.getNewEntity();
                IDataRow parentRecordDataRow = classDataRow.getParent();

                EntityInterface oldParentEntity = parentRecordDataRow.getEntityInterface();
                EntityInterface newParentEntity = oldEntityToSaver.get(oldParentEntity).getNewEntity();
                EntityPair entityPair = new EntityPair(newParentEntity, newEntity);
                AssociationInterface association = associationForEntities.get(entityPair);
                if (association == null) {
                    association = DataListUtil.createAssociation(newParentEntity, newEntity);
                    associationForEntities.put(entityPair, association);
                }

                for (IDataRow recordDataRow : classDataRow.getChildren()) {
                    Map<AbstractAttributeInterface, Object> recordsMap = saver.getRecordAsMap(recordDataRow.getRecord());
                    dataRowToRecordsMap.put(recordDataRow, recordsMap);

                    Map<AbstractAttributeInterface, Object> parentRecordsMap = dataRowToRecordsMap.get(parentRecordDataRow);
                    DataListUtil.getAssociatedRecordsList(parentRecordsMap, association).add(recordsMap);

                    nextDataRows.addAll(recordDataRow.getChildren());
                }

            }
            currDataRows = nextDataRows;
        }

        // remove the sentinel.
        oldEntityToSaver.remove(null);
        EntityManagerInterface entityManager = EntityManager.getInstance();
        try {
            entityManager.persistEntity(rootEntity, false).getId();
            entityManager.insertData(rootEntity, dataRowToRecordsMap.get(rootRecordDataRow));
            DataListMetadata dataListMetadata = dataListToSave.getDataListAnnotation();
            for (DataListSaver<IRecord> saver : oldEntityToSaver.values()) {
                EntityInterface newEntity = saver.getNewEntity();
                dataListMetadata.addEntityId(newEntity.getId());
                addToCache(newEntity);
            }
            Long dataListId = saveDataListMetadata(dataListMetadata);
            return dataListId;
        } catch (DynamicExtensionsSystemException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_SAVE_ERROR));
        } catch (DynamicExtensionsApplicationException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_SAVE_ERROR));
        }
    }

    /**
     * Returns entity record object
     * 
     * @param Long entityId
     * @return a EntityRecordResultInterface obj
     */
    public static List<IRecord> getEntityRecords(Long entityId) {

        // DE's Entity manager instance.
        EntityManagerInterface entityManager = EntityManager.getInstance();

        try {
            EntityInterface entity = entityManager.getEntityByIdentifier(entityId);
            DataListRetriever<IRecord> retriever = (DataListRetriever<IRecord>) DataListOperationsFactory.createDataListRetriever(entity);
            return retriever.getEntityRecords(null);
        } catch (DynamicExtensionsSystemException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DE_0004));
        } catch (DynamicExtensionsApplicationException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DE_0004));
        }
    }

    private static DomainObjectFactory getDomainObjectFactory() {
        return DomainObjectFactory.getInstance();
    }

    /**
     * Saves data list metadata.
     * 
     * @see DataListBusinessInterface#saveDataListMetadata(DataListMetadata)
     */
    private static Long saveDataListMetadata(DataListMetadata datalistMetadata) {
        return new DataListMetadataOperations().saveDataListMetadata(datalistMetadata);
    }

    private static void addToCache(EntityInterface newEntity) {
        DatalistCache.getInstance().addEntity(newEntity);
    }
}
