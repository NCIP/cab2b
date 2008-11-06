package edu.wustl.cab2b.server.datalist;

import java.sql.Connection;
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
import edu.wustl.cab2b.server.category.CategoryCache;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * A "structure" containing a bunch of info about the tree of entities and
 * categorial classes within a category. All the info is populated in the
 * constructor and used when saving/retrieving a category record from data list.
 * <p>
 * Note that there are two trees in the picture; one is the tree of categorial
 * classes within the category and the other is the corresponding tree of
 * entities created while saving a data list that contains this category. The
 * tree of entities exactly matches the tree of categorial classes.
 * 
 * @author srinath_k
 * 
 */
class CategoryEntityTreeParser {
    /**
     * key=id of the categorial class; value=entity corresponding to this
     * categorial class.
     */
    private Map<Long, EntityInterface> categorialClassIdToEntity;

    /**
     * key=entity; value=categorial class for which this entity was created.
     */
    private Map<EntityInterface, CategorialClass> entityToOriginCategorialClass;

    /**
     * key = {@link EntityPair} with src as parent entity and dest as child
     * entity, within the category entity tree; value = the association.
     */
    private Map<EntityPair, AssociationInterface> associationForEntityPair;

    /**
     * key = entity; value = list of associations from that entity i.e.
     * associations linking the entity to its children.
     */
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

    /**
     * Does a simultaneous BFS on both entity and categorial class trees and
     * populates all the member variables.
     * 
     * @param rootEntity
     */
    CategoryEntityTreeParser(EntityInterface rootEntity) {
        categorialClassIdToEntity = new HashMap<Long, EntityInterface>();
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
                    if (isRelevantEntity(targetEntity)) {
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

    /**
     * Checks if this entity is a part of this category's entity tree (except
     * root). This if is needed bcos in context of data list, we may run out the
     * entities for the category and move into the next associated
     * class/category.
     * 
     * @param entity
     * @return
     */
    private boolean isRelevantEntity(EntityInterface entity) {
        return isCategorialClassEntity(entity)
                && Utility.getTaggedValue(entity, CategoryDataListSaver.CATEGORY_ID_TAG_KEY) == null;
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
        Connection connection = ConnectionUtil.getConnection();
        return CategoryCache.getInstance(connection).getCategoryById(id);
    }

    Map<EntityInterface, List<AssociationInterface>> getAssociationsForEntity() {
        return associationsForEntity;
    }
}

/**
 * 
 * @author hrishikesh_rajpathak, srinath_k
 *
 */
class EntityPair {
    private EntityInterface parentEntity;

    private EntityInterface childEntity;

    /**
     * Constructor for EntityPair
     * @param parentEntity
     * @param childEntity
     */
    public EntityPair(EntityInterface parentEntity, EntityInterface childEntity) {
        this.parentEntity = parentEntity;
        this.childEntity = childEntity;
    }

    @Override
    /**
     * overided method
     * @see java.lang.Object#equals(Object)
     */
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
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(parentEntity.getName()).append(childEntity.getName()).toHashCode();
    }
}
