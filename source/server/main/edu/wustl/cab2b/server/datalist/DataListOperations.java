
package edu.wustl.cab2b.server.datalist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecord;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * Class which performs several Data List operations like 
 * save, retreive data list etc.
 * 
 * @author chetan_bh
 */
public class DataListOperations extends DefaultBizLogic
{

	/**
	 * Hibernate DAO Type to use.
	 */
	private static final int DAO_TYPE = Constants.HIBERNATE_DAO;

	/**
	 * DE's DomainObjectFactory instance.
	 */
	DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();

	/**
	 * DataList entity group. 
	 */
	EntityGroupInterface dataListEntityGroup = DynamicExtensionUtility
			.getEntityGroupByName(edu.wustl.cab2b.common.util.Constants.DATALIST_ENTITY_GROUP_NAME);

	/**
	 * Returns a list of all available data list metadata. 
	 * @return list of data list metadata.
	 */
	public List<DataListMetadata> retrieveAllDataListMetadata()
	{
		List<DataListMetadata> allDataList = null;

		try
		{
			allDataList = (List<DataListMetadata>) retrieve(DataListMetadata.class.getName());
		}
		catch (DAOException e)
		{
			throw (new RuntimeException(e.getMessage(), e,
					ErrorCodeConstants.DATALIST_RETRIEVE_ERROR));
		}
		return allDataList;
	}

	/**
	 * Returns a data list which contains saved data.
	 * @param dataListId
	 * @return a data list.
	 */
	public DataList retrieveDataList(Long dataListId)
	{
		DataList dataList = new DataList();
		return dataList;
	}	
	
	public List<EntityRecord> getEntityRecord(Long entityId)
	{
		
		//		 DE's Entity manager instance.		 
		EntityManagerInterface entityManager = EntityManager.getInstance();
		
		Object entityRecord[][] = null;	
		List<EntityRecord> entityRecords = null;
		try {
			EntityInterface entity = entityManager.getEntityByIdentifier(entityId);
			entityRecords = entityManager.getAllRecords(entity);			
			/*Iterator it = entityRecords.iterator();
			
			while(it.hasNext())
			{
				EntityRecord entityRec = (EntityRecord) it.next();	
			}*/
			
		} catch (DynamicExtensionsSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DynamicExtensionsApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return entityRecords;
	}

	/**
	 * Returns a data list metadata.
	 * @param id
	 * @return
	 */
	public DataListMetadata retrieveDataListMetadata(Long id)
	{
		List<DataListMetadata> results = null;
		try
		{
			results = (new DataListOperations()).retrieve(DataListMetadata.class.getName(), "id",
					id);
		}
		catch (DAOException e)
		{
			throw (new RuntimeException(e.getMessage(), e,
					ErrorCodeConstants.DATALIST_RETRIEVE_ERROR));
		}
		if (results != null && results.size() > 0)
			return results.get(0);
		return null;
	}

	/**
	 * Constructs the recursive map {@link DataListOperations#dataListAttributesMap}} recursively 
	 * and persists it to the database using Dynamic Extension APIs.
	 *  
	 * @param dataListToSave
	 * @return idetifier of the new root level entity(new DataList entity) created.
	 */
	public Long save(DataList dataListToSave)
	{		
		// DE's Entity manager instance.		 
		EntityManagerInterface entityManager = EntityManager.getInstance();

		/* There will always be one root node is the datalist tree. */
		IDataRow rootDataRow = dataListToSave.getRootDataRow();

		Map<AbstractAttributeInterface, Object> dataListAttributesMap = new HashMap<AbstractAttributeInterface, Object>();

		EntityInterface dataListEntity = domainObjectFactory.createEntity();
		dataListEntity.setName("DataList_" + System.currentTimeMillis());
		dataListEntity.addEntityGroupInterface(dataListEntityGroup);
		dataListEntityGroup.addEntity(dataListEntity);

		/*
		 * Map to maintain list of new entities created so that 
		 * we can reuse the existing entiy later.
		 * 
		 * New entities will contain new attributes, corresponding to 
		 * old entitiy's old attributes. 
		 */
		Map<String, EntityInterface> mapOfNewEntities = new HashMap<String, EntityInterface>();

		/*
		 * Map to maintain list of new Associations created between
		 * new entities to reuse later. 
		 */
		Map<String, AssociationInterface> mapOfNewAssociations = new HashMap<String, AssociationInterface>();

		mapOfNewEntities.put(dataListEntity.getName() + "_1", dataListEntity); // Tree level one.

		for (IDataRow firstLevelTypeDataRow : rootDataRow.getChildren())
		{
			if (firstLevelTypeDataRow.isData() == false)
			{
				List<IDataRow> valueDataRowsForThisType = firstLevelTypeDataRow.getChildren();
				for (IDataRow firstLevelDataRow : valueDataRowsForThisType)
				{
					createNewEntityAndAssociation(dataListAttributesMap, firstLevelDataRow,
							dataListEntity, 2, mapOfNewEntities, mapOfNewAssociations);
				}
			}
			else
			{
				createNewEntityAndAssociation(dataListAttributesMap, firstLevelTypeDataRow,
						dataListEntity, 2, mapOfNewEntities, mapOfNewAssociations);
			}
		}

		Long dataListId = null;
		try
		{
			Long entityId = entityManager.persistEntity(dataListEntity).getId();

			Long recordId = entityManager.insertData(dataListEntity, dataListAttributesMap);

			Logger.out.info("DataList saved successfully !!!! (entityId, recordId) :: " + entityId
					+ ", " + recordId);

			DataListMetadata dataListMetadata = dataListToSave.getDataListAnnotation();
			dataListMetadata.setEntityId(entityId);

			dataListId = saveDataListMetadata(dataListMetadata);

		}
		catch (DynamicExtensionsSystemException e)
		{
			throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_SAVE_ERROR));
		}
		catch (DynamicExtensionsApplicationException e)
		{
			throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_SAVE_ERROR));
		}
		return dataListId;
	}

	/**
	 * Creates new entities and associations if they are not already created.
	 * @param mapToConstruct map to construct.
	 * @param dataRow data row which contains values and attributes collection used to construct map.
	 * @param entity parent entity.
	 * @param treeLevel tree level is only to distinguish same entities in different levels in the datalist.
	 */
	private void createNewEntityAndAssociation(
			Map<AbstractAttributeInterface, Object> mapToConstruct, IDataRow dataRow,
			EntityInterface entity, int treeLevel, Map<String, EntityInterface> mapOfNewEntities,
			Map<String, AssociationInterface> mapOfNewAssociations)
	{

		String keyForNewEntitiesMap = dataRow.getEntityInterface().getName() + "_" + treeLevel;

		EntityInterface newEntity = mapOfNewEntities.get(keyForNewEntitiesMap);
		if (newEntity == null)
		{
			newEntity = getNewEntity(dataRow.getEntityInterface());
			mapOfNewEntities.put(keyForNewEntitiesMap, newEntity);
		}

		String keyForNewAssociationsMap = entity.getName() + "_"
				+ dataRow.getEntityInterface().getName();

		AssociationInterface newAssociation = mapOfNewAssociations.get(keyForNewAssociationsMap);
		if (newAssociation == null)
		{
			newAssociation = createNewOneToManyAsso(entity, newEntity);
			mapOfNewAssociations.put(keyForNewAssociationsMap, newAssociation);
			mapToConstruct.put(newAssociation, new ArrayList());
			entity.addAssociation(newAssociation);
		}
		List<Map> listOfSubMaps = (List<Map>) mapToConstruct.get(newAssociation);

		if (listOfSubMaps == null)
		{
			listOfSubMaps = new ArrayList<Map>();
			mapToConstruct.put(newAssociation, listOfSubMaps);
		}

		Map<AbstractAttributeInterface, Object> mapToAdd = new HashMap<AbstractAttributeInterface, Object>();
		constructDataRowMap(dataRow, mapToAdd, newEntity, treeLevel, mapOfNewEntities,
				mapOfNewAssociations);

		listOfSubMaps.add(mapToAdd);
	}

	/**
	 * Creates and returns a new one to many association between source target entities.
	 * @param srcEntity source entity of the new association
	 * @param tarEntity target enetiyt of the new association
	 * @return new association
	 */
	private AssociationInterface createNewOneToManyAsso(EntityInterface srcEntity,
			EntityInterface tarEntity)
	{
		AssociationInterface returner = domainObjectFactory.createAssociation();
		String associationName = "AssociationName_" + srcEntity.getAssociationCollection().size()
				+ 1;
		returner.setName(associationName);
		returner.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		returner.setEntity(srcEntity);
		returner.setTargetEntity(tarEntity);
		returner.setSourceRole(getNewRole(AssociationType.CONTAINTMENT, "source_role_"
				+ associationName, Cardinality.ONE, Cardinality.ONE));
		returner.setTargetRole(getNewRole(AssociationType.CONTAINTMENT, "target_role_"
				+ associationName, Cardinality.ZERO, Cardinality.MANY));

		return returner;
	}

	/**
	 * Creates and returns new Role for an association.
	 * @param associationType associationType
	 * @param name name
	 * @param minCard  minCard
	 * @param maxCard maxCard
	 * @return  RoleInterface
	 */
	private RoleInterface getNewRole(AssociationType associationType, String name,
			Cardinality minCard, Cardinality maxCard)
	{
		RoleInterface role = domainObjectFactory.createRole();
		role.setAssociationsType(associationType);
		role.setName(name);
		role.setMinimumCardinality(minCard);
		role.setMaximumCardinality(maxCard);
		return role;
	}

	/**
	 * A recursive function to construct the recursive map of attribute->value 
	 * and association->list(map).
	 * 
	 * @param dataRowToSave
	 * @param mapToConstruct
	 * @param newEntity
	 * @param treeLevel
	 */
	private void constructDataRowMap(IDataRow dataRowToSave,
			Map<AbstractAttributeInterface, Object> mapToConstruct, EntityInterface newEntity,
			int treeLevel, Map<String, EntityInterface> mapOfNewEntities,
			Map<String, AssociationInterface> mapOfNewAssociations)
	{
		Object[] values = dataRowToSave.getRow();

		/* Find an existing entity in the newEntitiesMap for with same name and same tree level */

		List<AttributeInterface> newAttributes = getOrderedAttributes(
				dataRowToSave.getAttributes(), newEntity.getAttributeCollection());

		for (int i = 0; i < newAttributes.size(); i++)
		{
			AttributeInterface attribute = newAttributes.get(i);
			Object value = values[i];
			mapToConstruct.put(attribute, value);
		}

		List<IDataRow> children = dataRowToSave.getChildren();

		if (children != null && children.size() > 0)
		{
			treeLevel++;
			for (IDataRow dataRow : children)
			{
				if (dataRow.isData() == false) // or if row values == null
				{
					for (IDataRow subDR : dataRow.getChildren())
					{
						createNewEntityAndAssociation(mapToConstruct, subDR, newEntity, treeLevel,
								mapOfNewEntities, mapOfNewAssociations);
					}
				}
				else
				{
					createNewEntityAndAssociation(mapToConstruct, dataRow, newEntity, treeLevel,
							mapOfNewEntities, mapOfNewAssociations);
				}
			}
		}

	}

	/**
	 * Returns the ordered attributes of unordered list in a new list.
	 * @param orderedOldAttribs
	 * @param unOrderedNewAttribs
	 * @return
	 */
	private List<AttributeInterface> getOrderedAttributes(
			List<AttributeInterface> orderedOldAttribs,
			Collection<AttributeInterface> unOrderedNewAttribs)
	{
		List<AttributeInterface> returner = new ArrayList<AttributeInterface>();
		for (AttributeInterface attribute : orderedOldAttribs)
		{
			AttributeInterface foundAttrib = findAttributeByName(unOrderedNewAttribs, attribute
					.getName());
			returner.add(foundAttrib);
		}
		return returner;
	}

	/**
	 * Finds and returns an atttribute by name in the list of attributes.
	 * @param attribs
	 * @param attribName
	 * @return
	 */
	private AttributeInterface findAttributeByName(Collection<AttributeInterface> attribs,
			String attribName)
	{
		AttributeInterface returner = null;
		for (AttributeInterface attrib : attribs)
		{
			if (attrib.getName().startsWith(attribName))
			{
				returner = attrib;
				break;
			}
		}
		return returner;
	}

	/**
	 * Creates and returns a new attribute given old attribute.
	 * @param oldAttribute
	 * @return new attriute with same old attribute name.
	 */
	private AttributeInterface getNewAttribute(AttributeInterface oldAttribute)
	{
		AttributeInterface newAttribute = null;
		String oldAttributeType = oldAttribute.getDataType();
		if (oldAttributeType.equals(EntityManagerConstantsInterface.STRING_ATTRIBUTE_TYPE))
			newAttribute = domainObjectFactory.createStringAttribute();
		else if (oldAttributeType.equals(EntityManagerConstantsInterface.INTEGER_ATTRIBUTE_TYPE))
			newAttribute = domainObjectFactory.createIntegerAttribute();
		else if (oldAttributeType.equals(EntityManagerConstantsInterface.DATE_ATTRIBUTE_TYPE))
			newAttribute = domainObjectFactory.createDateAttribute();
		else if (oldAttributeType.equals(EntityManagerConstantsInterface.DOUBLE_ATTRIBUTE_TYPE))
			newAttribute = domainObjectFactory.createDoubleAttribute();
		else if (oldAttributeType.equals(EntityManagerConstantsInterface.FILE_ATTRIBUTE_TYPE))
			newAttribute = domainObjectFactory.createFileAttribute();
		else if (oldAttributeType.equals(EntityManagerConstantsInterface.SHORT_ATTRIBUTE_TYPE))
			newAttribute = domainObjectFactory.createShortAttribute();
		else if (oldAttributeType.equals(EntityManagerConstantsInterface.LONG_ATTRIBUTE_TYPE))
			newAttribute = domainObjectFactory.createLongAttribute();
		else if (oldAttributeType.equals(EntityManagerConstantsInterface.BOOLEAN_ATTRIBUTE_TYPE))
			newAttribute = domainObjectFactory.createBooleanAttribute();
		else if (oldAttributeType.equals(EntityManagerConstantsInterface.FLOAT_ATTRIBUTE_TYPE))
			newAttribute = domainObjectFactory.createFloatAttribute();
		else if (oldAttributeType.equals(EntityManagerConstantsInterface.DATE_TIME_ATTRIBUTE_TYPE))
			newAttribute = domainObjectFactory.createDateAttribute();
		else
		{
			Logger.out.debug("Extra attribute not mapped");
		}
		return newAttribute;
	}

	/**
	 * Creates and returns a new entity given an old entity.
	 * @param oldEntity
	 * @return new entity.
	 */
	private EntityInterface getNewEntity(EntityInterface oldEntity)
	{
		EntityInterface newEntity = domainObjectFactory.createEntity();
		newEntity.addEntityGroupInterface(dataListEntityGroup);
		dataListEntityGroup.addEntity(newEntity);

		newEntity.setName(oldEntity.getName());

		TaggedValueInterface taggedValue = domainObjectFactory.createTaggedValue();
		taggedValue.setKey(edu.wustl.cab2b.common.util.Constants.OLD_ENTITY_ID_TAG_NAME);
		taggedValue.setValue(oldEntity.getId().toString());
		newEntity.addTaggedValue(taggedValue);

		Collection<AttributeInterface> oldAttribs = oldEntity.getAttributeCollection();
		for (AttributeInterface oldAttrib : oldAttribs)
		{
			AttributeInterface newAttrib = getNewAttribute(oldAttrib);
			newAttrib.setName(oldAttrib.getName());
			newEntity.addAttribute(newAttrib);
		}

		return newEntity;
	}

	/**
	 * Saves data list metadata.
	 * @see DataListBusinessInterface#saveDataListMetadata(DataListMetadata)
	 */
	public Long saveDataListMetadata(DataListMetadata datalistMetadata)
	{
		try
		{
			insert(datalistMetadata, DAO_TYPE);
		}
		catch (BizLogicException e)
		{
			throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_SAVE_ERROR));
		}
		catch (UserNotAuthorizedException e)
		{
			throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATALIST_SAVE_ERROR));
		}
		return datalistMetadata.getId();
	}
}
