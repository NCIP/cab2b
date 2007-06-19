package edu.wustl.cab2b.server.datalist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.CategoryCache;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;

class CategoryEntityTreeParser {
    private Map<Long, EntityInterface> categorialClassIdToEntity;

    private Map<EntityInterface, CategorialClass> entityToOriginCategorialClass;

    private Map<EntityPair, AssociationInterface> associationForEntityPair;

    private Map<EntityInterface, List<AssociationInterface>> associationsForEntity;

    private Category category;

    Map<EntityPair, AssociationInterface> getAssociationForEntityPair() {
        return associationForEntityPair;
    }

    EntityInterface getEntityForCategorialClassId(Long categorialClassId) {
        return categorialClassIdToEntity.get(categorialClassId);
    }

    CategorialClass getOriginCategorialClassForEntity(EntityInterface entity) {
        return entityToOriginCategorialClass.get(entity);
    }

    Category getCategory() {
        return category;
    }

    AssociationInterface getAssociation(EntityInterface srcEnt, EntityInterface tgtEnt) {
        return getAssociationForEntityPair().get(new EntityPair(srcEnt, tgtEnt));
    }

    CategoryEntityTreeParser(EntityInterface rootEntity) {
        categorialClassIdToEntity = new BidirectionalHashMap<Long, EntityInterface>();
        entityToOriginCategorialClass = new HashMap<EntityInterface, CategorialClass>();
        associationForEntityPair = new HashMap<EntityPair, AssociationInterface>();
        associationsForEntity = new HashMap<EntityInterface, List<AssociationInterface>>();

        category = getCategoryForDataListEntity(rootEntity);

        CategorialClass rootClass = category.getRootClass();

        List<CategorialClass> currCategorialClasses = new ArrayList<CategorialClass>();
        currCategorialClasses.add(rootClass);

        List<EntityInterface> currEntities = new ArrayList<EntityInterface>();
        currEntities.add(rootEntity);

        while (!currEntities.isEmpty()) {
            List<EntityInterface> nextEntities = new ArrayList<EntityInterface>();
            List<CategorialClass> nextCategorialClasses = new ArrayList<CategorialClass>();

            for (EntityInterface currEntity : currEntities) {
                associationsForEntity.put(currEntity, new ArrayList<AssociationInterface>());
                Long categorialClassId = getCategorialClassId(currEntity);
                CategorialClass categorialClass = findCategorialClassWithId(currCategorialClasses,
                                                                            categorialClassId);
                categorialClassIdToEntity.put(categorialClass.getId(), currEntity);
                entityToOriginCategorialClass.put(currEntity, categorialClass);
                Collection<AssociationInterface> associations = currEntity.getAssociationCollection();
                for (AssociationInterface association : associations) {
                    EntityInterface targetEntity = association.getTargetEntity();
                    if (isCategorialClassEntity(targetEntity)) {
                        associationForEntityPair.put(new EntityPair(currEntity, targetEntity), association);
                        associationsForEntity.get(currEntity).add(association);
                        nextEntities.add(targetEntity);
                    }
                }
                nextCategorialClasses.addAll(categorialClass.getChildren());
            }
            currEntities = nextEntities;
            currCategorialClasses = nextCategorialClasses;
        }
    }

    private CategorialClass findCategorialClassWithId(List<CategorialClass> currCategorialClasses,
                                                      Long categorialClassId) {
        for (CategorialClass categorialClass : currCategorialClasses) {
            if (categorialClass.getId().equals(categorialClassId)) {
                return categorialClass;
            }
        }
        throw new RuntimeException("Categorial class with given id is not present.");
    }

    private Long getCategorialClassId(EntityInterface entity) {
        TaggedValueInterface taggedValue = Utility.getTaggedValue(
                                                                  entity.getTaggedValueCollection(),
                                                                  CategoryDataListSaver.CATEGORIAL_CLASS_ID_TAG_KEY);
        if (taggedValue == null) {
            return null;
        }
        return Long.parseLong(taggedValue.getValue());
    }

    private boolean isCategorialClassEntity(EntityInterface entity) {
        return getCategorialClassId(entity) != null;
    }

    private Category getCategoryForDataListEntity(EntityInterface newEntity) {
        TaggedValueInterface taggedValue = Utility.getTaggedValue(newEntity.getTaggedValueCollection(),
                                                                  CategoryDataListSaver.CATEGORY_ID_TAG_KEY);
        if (taggedValue == null) {
            throw new IllegalArgumentException("Given entity isn't root of a category.");
        }
        String categoryIdString = taggedValue.getValue();

        return getCategoryById(Long.parseLong(categoryIdString));
    }

    private Category getCategoryById(Long id) {
        return CategoryCache.getInstance().getCategoryById(id);
    }

    Map<EntityInterface, List<AssociationInterface>> getAssociationsForEntity() {
        return associationsForEntity;
    }
}

class EntityPair {
    private EntityInterface parentEntity;

    private EntityInterface childEntity;

    public EntityPair(EntityInterface parentEntity, EntityInterface childEntity) {
        this.parentEntity = parentEntity;
        this.childEntity = childEntity;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EntityPair)) {
            return false;
        }
        EntityPair otherEntityPair = (EntityPair) other;
        return otherEntityPair.parentEntity.getName().equals(parentEntity.getName())
                && otherEntityPair.childEntity.getName().equals(childEntity.getName());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(parentEntity.getName()).append(childEntity.getName()).toHashCode();
    }
}
