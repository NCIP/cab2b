package edu.wustl.cab2b.server.datalist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.CategoryCache;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;

public class CategoryDataListSaver extends AbstractDataListSaver<ICategorialClassRecord> {
    static final String CATEGORY_ID_TAG_KEY = "categoryId";

    static final String CATEGORIAL_CLASS_ID_TAG_KEY = "categorialClassId";

    private CategoryEntityTreeParser parser;

    @Override
    public void initialize(EntityInterface oldEntity) {
        super.initialize(oldEntity);
        parser = new CategoryEntityTreeParser(newEntity);
    }

    @Override
    protected void populateNewEntity(EntityInterface oldEntity) {
        EntityInterface originEntity = edu.wustl.cab2b.common.util.DataListUtil.getOriginEntity(oldEntity);
        if (!Utility.isCategory(originEntity)) {
            throw new IllegalArgumentException();
        }
        Category category = getCategoryByEntityId(originEntity.getId());
        CategorialClass rootCategorialClass = category.getRootClass();
        tagWithCategory(newEntity, category);
        tagWithCategorialClass(newEntity, rootCategorialClass);
        copyAttributesFromCategorialClass(newEntity, rootCategorialClass);

        Map<CategorialClass, EntityInterface> catClassToEntity = new HashMap<CategorialClass, EntityInterface>();
        catClassToEntity.put(rootCategorialClass, newEntity);
        // one level below root.
        List<CategorialClass> currCatClasses = new ArrayList<CategorialClass>();
        currCatClasses.addAll(rootCategorialClass.getChildren());

        while (!currCatClasses.isEmpty()) {
            List<CategorialClass> nextCatClasses = new ArrayList<CategorialClass>();

            for (CategorialClass categorialClass : currCatClasses) {
                EntityInterface entity = createEntity(categorialClass);
                catClassToEntity.put(categorialClass, entity);

                EntityInterface parentEntity = catClassToEntity.get(categorialClass.getParent());
                DynamicExtensionUtility.createNewOneToManyAsso(parentEntity, entity);

                nextCatClasses.addAll(categorialClass.getChildren());
            }

            currCatClasses = nextCatClasses;
        }
    }

    @Override
    public Map<AbstractAttributeInterface, Object> transformToMap(ICategorialClassRecord record) {
        return getRecordAsMap(record, newEntity);
    }

    private Map<AbstractAttributeInterface, Object> getRecordAsMap(ICategorialClassRecord record,
                                                                   EntityInterface entity) {

        Map<AbstractAttributeInterface, Object> res = getValueAsMap(record, entity.getAttributeCollection());

        for (Map.Entry<CategorialClass, List<ICategorialClassRecord>> entry : record.getChildrenCategorialClassRecords().entrySet()) {
            CategorialClass categorialClass = entry.getKey();
            EntityInterface childEntity = parser.getEntityForCategorialClassId(categorialClass.getId());
            for (ICategorialClassRecord childRecord : entry.getValue()) {
                Map<AbstractAttributeInterface, Object> childRecordMap = getRecordAsMap(childRecord, childEntity);
                AssociationInterface association = parser.getAssociation(entity, childEntity);
                DataListUtil.getAssociatedRecordsList(res, association).add(childRecordMap);
            }
        }
        putRecordIdInMap(record.getRecordId(), res, entity);
        return res;
    }

    private Map<AbstractAttributeInterface, Object> getValueAsMap(ICategorialClassRecord categorialClassRecord,
                                                                  Collection<AttributeInterface> newAttributes) {
        Map<AbstractAttributeInterface, Object> recordMap = new HashMap<AbstractAttributeInterface, Object>();
        Set<AttributeInterface> recordAttributes = categorialClassRecord.getAttributes();
        for (AttributeInterface attribute : newAttributes) {
            AttributeInterface recordAttribute = DataListUtil.getAttributeByName(recordAttributes,
                                                                                 attribute.getName());
            recordMap.put(attribute, categorialClassRecord.getValueForAttribute(recordAttribute));

        }
        return recordMap;
    }

    private EntityInterface createEntity(CategorialClass categorialClass) {
        EntityInterface entity = getDomainObjectFactory().createEntity();
        entity.setName(categorialClass.getCategorialClassEntity().getName());
        DynamicExtensionUtility.addTaggedValue(entity, CATEGORIAL_CLASS_ID_TAG_KEY,
                                               categorialClass.getId().toString());

        copyAttributesFromCategorialClass(entity, categorialClass);
        addVirtualAttributes(entity);
        return entity;
    }

    private void copyAttributesFromCategorialClass(EntityInterface entity, CategorialClass categorialClass) {
        for (CategorialAttribute categorialAttribute : categorialClass.getCategorialAttributeCollection()) {
            entity.addAttribute(DataListUtil.createNewAttribute(categorialAttribute.getCategoryAttribute()));
        }
    }

    private Category getCategoryByEntityId(Long id) {
        return CategoryCache.getInstance().getCategoryByEntityId(id);
    }

    private void tagWithCategory(EntityInterface entity, Category category) {
        DynamicExtensionUtility.addTaggedValue(entity, CATEGORY_ID_TAG_KEY, category.getId().toString());
    }

    private void tagWithCategorialClass(EntityInterface entity, CategorialClass categorialClass) {
        DynamicExtensionUtility.addTaggedValue(entity, CATEGORIAL_CLASS_ID_TAG_KEY,
                                               categorialClass.getId().toString());
    }
}
