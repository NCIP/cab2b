package edu.wustl.cab2b.server.experiment;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.hibernate.HibernateException;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.CustomDataCategoryModel;
import edu.wustl.cab2b.common.IdName;
import edu.wustl.cab2b.common.domain.AdditionalMetadata;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.DataListUtil;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.DatalistCache;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.cab2b.server.util.UtilityOperations;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.tree.ExperimentTreeNode;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Hrishikesh Rajpathak
 * @author chetan_bh
 */
public class ExperimentOperations extends DefaultBizLogic {
    /**
     * Hibernate DAO Type to use.
     */
    int daoType = Constants.HIBERNATE_DAO;

    /**
     * A function to persist an experiment object.
     * 
     * @param exp
     *            experiment ot persist.
     * @param daoType
     *            DAO Type to use.
     * @throws UserNotAuthorizedException
     * @throws BizLogicException
     */
    public void addExperiment(Object exp) throws BizLogicException, UserNotAuthorizedException {
        insert(exp, daoType);
        Logger.out.info("experiment saved Successfully " + ((Experiment) exp).getId());
    }

    /**
     * This method adds given experiment into goven experimentGroupId and
     * persist it.
     * 
     * @param exp
     *            experiment ot persist.
     * @param daoType
     *            DAO Type to use.
     * @throws UserNotAuthorizedException
     * @throws BizLogicException
     * @throws DAOException
     */
    public void addExperiment(Long experimentGroupId, Experiment experiment) throws BizLogicException,
            UserNotAuthorizedException, DAOException {
        ExperimentGroup experimentGroup = new ExperimentGroupOperations().getExperimentGroup(experimentGroupId);
        experiment.getExperimentGroupCollection().add(experimentGroup);
        insert(experiment, daoType);
        Logger.out.info("experiment saved Successfully " + (experiment).getId());
    }

    /**
     * A function to move an experiment from one experiment group to another.
     * 
     * @param exp
     *            experiment to move.
     * @param srcExpGrp
     *            source experiment group
     * @param tarExpGrp
     *            target experiment group.
     * @param daoType
     *            DAO type to use.
     * @throws UserNotAuthorizedException
     * @throws BizLogicException
     * @throws Exception
     */
    public void move(Object exp, Object srcExpGrp, Object tarExpGrp) throws BizLogicException,
            UserNotAuthorizedException {
        Experiment experiment = (Experiment) exp;

        ExperimentGroup sourceExpGroup = (ExperimentGroup) srcExpGrp;

        if (!isExperimentContainedInGroup(experiment, sourceExpGroup)) {
            throw (new BizLogicException("Experiment doesn't belong to Experiment Group."));
        }

        // Logger.out.info("sourceExpGroup.getExperimentCollection() : after
        // "+sourceExpGroup.getExperimentCollection().size());

        ExperimentGroup targetExpGroup = (ExperimentGroup) tarExpGrp;
        if (isExperimentContainedInGroup(experiment, targetExpGroup)) {
            throw (new BizLogicException("Experiment already belongs to target Experiment Group."));
        }

        if (sourceExpGroup == targetExpGroup) {
            throw (new BizLogicException("Source and Target Experiment Groups are same."));
        }

        sourceExpGroup.getExperimentCollection().remove(experiment);
        targetExpGroup.getExperimentCollection().add(experiment);

        // Logger.out.info("targetExpGroup.getExperimentCollection()
        // "+targetExpGroup.getExperimentCollection().size());

        // Logger.out.info("updating target source group collection");

        experiment.getExperimentGroupCollection().remove(sourceExpGroup);
        experiment.getExperimentGroupCollection().add(targetExpGroup);

        update(experiment, daoType);
        update(sourceExpGroup, daoType);
        update(targetExpGroup, daoType);

    }

    /**
     * A function to copy an experiment from one group to another, which is a
     * shallow copy.
     * 
     * @param exp
     *            experiment to copy.
     * @param tarExpGrp
     *            target experiment group.
     * @param daoType
     *            DAO type to use.
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     * @throws Exception
     */
    public void copy(Object exp, Object tarExpGrp) throws BizLogicException, UserNotAuthorizedException {
        Experiment experiment = (Experiment) exp;
        experiment.getName();

        ExperimentGroup targetExperimentGroup = (ExperimentGroup) tarExpGrp;

        if (isExperimentContainedInGroup(experiment, targetExperimentGroup)) {
            throw (new BizLogicException("Experiment already belongs to target Experiment Group"));
        }
        targetExperimentGroup.getExperimentCollection().add(experiment);
        experiment.getExperimentGroupCollection().add(targetExperimentGroup);

        update(experiment, daoType);
        update(targetExperimentGroup, daoType);

    }

    /**
     * @param userName
     * @return
     */
    public List<Experiment> getLatestExperimentForUser(String userName) {
        String hql = "from Experiment as Exp order by Exp.lastUpdatedOn ";
        DAO dao = DAOFactory.getInstance().getDAO(daoType);
        List<Experiment> returner = null;
        try {
            ((AbstractDAO) dao).openSession(null);
            returner = dao.executeQuery(hql, null, false, null);
            ((AbstractDAO) dao).closeSession();
        } catch (DAOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returner;
    }

    /**
     * @return
     * @throws DAOException
     */
    public Vector getExperimentHierarchy() throws DAOException {
        Vector expHierarchyData = new Vector();
        List returner = new ArrayList();
        List returner1 = new ArrayList();

        String hql = "from ExperimentGroup as ExpGrp where ExpGrp.parentGroup.id is null ";

        DAO dao = DAOFactory.getInstance().getDAO(daoType);
        ((AbstractDAO) dao).openSession(null);

        try {
            // List myReturner = dao.executeQuery("from ExperimentGroup", null,
            // false, null);
            // Logger.out.info("returner expGrp ::::::: "+myReturner);
            returner = dao.executeQuery(hql, null, false, null);

        } catch (Exception exp) {
            exp.printStackTrace();
        }

        // TODO we have to use HQL here temporarily since DAO.retrieve doesn't
        // support where conditions on aggregate function like collection.size > 0, etc;
        /*		String hql1 = "from Experiment as Exp where Exp.experimentGroupCollection.size = 0";
         try {
         returner1 = dao.executeQuery(hql1, null, false, null);
         
         } catch (Exception exp) {
         exp.printStackTrace();
         }

         returner.addAll(returner1);*/

        ((AbstractDAO) dao).closeSession();

        expHierarchyData = getExperimentMetadataHierarchy(returner);

        return expHierarchyData;
    }

    public Vector getExperimentMetadataHierarchy(Collection firstLevelRootNodes) {
        //  
        // "+firstLevelRootNodes);
        Vector returner = new Vector();
        Iterator iter = firstLevelRootNodes.iterator();
        while (iter.hasNext()) {
            AdditionalMetadata metadata = (AdditionalMetadata) iter.next();

            if (metadata instanceof ExperimentGroup) {
                ExperimentGroup expGrp = (ExperimentGroup) metadata;
                // Logger.out.info("------expGrp------ "+expGrp+" ----------");
                ExperimentTreeNode expTreeNode = new ExperimentTreeNode();
                expTreeNode.setExperimentGroup(true);

                Collection childrenExpNodes = expGrp.getExperimentCollection();
                Collection childrenGrpNodes = expGrp.getChildrenGroupCollection();

                updateMetadataHierarchy((AdditionalMetadata) expGrp, expTreeNode, childrenExpNodes); // exp
                // 1
                updateMetadataHierarchy((AdditionalMetadata) expGrp, expTreeNode, childrenGrpNodes); // expGrp
                // 3
                // expTreeNode.setU
                returner.add(expTreeNode);

            } else {
                // Logger.out.info("------exp------ "+metadata+" ----------");
                Long nodeId = metadata.getId();
                String nodeName = metadata.getName();
                ExperimentTreeNode expTreeNode = new ExperimentTreeNode(nodeId, nodeName);
                expTreeNode.setDesc(metadata.getDescription());
                expTreeNode.setCreatedOn(metadata.getCreatedOn());
                expTreeNode.setLastUpdatedOn(metadata.getLastUpdatedOn());
                expTreeNode.setExperimentGroup(false);
                returner.add(expTreeNode);
            }
        }
        return returner;
    }

    /**
     * To recursively update the TreeNode similar to expGroup
     */
    private void updateMetadataHierarchy(AdditionalMetadata expGroup, ExperimentTreeNode treeNode,
                                         Collection children) {
        treeNode.setIdentifier(expGroup.getId());
        treeNode.setName(expGroup.getName());
        treeNode.setDesc(expGroup.getDescription());
        treeNode.setCreatedOn(expGroup.getCreatedOn());
        treeNode.setLastUpdatedOn(expGroup.getLastUpdatedOn());

        if (children != null && children.size() != 0) {
            Iterator iter = children.iterator();
            while (iter.hasNext()) {
                AdditionalMetadata expOrGrp = (AdditionalMetadata) iter.next();
                ExperimentTreeNode expTreeNode = new ExperimentTreeNode();
                if (expOrGrp instanceof ExperimentGroup) {
                    expTreeNode.setExperimentGroup(true);
                    ExperimentGroup eGroup = (ExperimentGroup) expOrGrp;
                    Collection childsGroupChildren = eGroup.getChildrenGroupCollection();
                    updateMetadataHierarchy(expOrGrp, expTreeNode, childsGroupChildren);

                    Collection childsExpChildren = eGroup.getExperimentCollection();
                    updateMetadataHierarchy(expOrGrp, expTreeNode, childsExpChildren);

                } else {
                    expTreeNode.setExperimentGroup(false);
                    updateMetadataHierarchy(expOrGrp, expTreeNode, null);
                }
                treeNode.getChildNodes().add(expTreeNode);
            }
        }
    }

    /**
     * A validation function to check the containment of an experiment in a
     * experiment group
     * 
     * @param exp
     *            experiment to check.
     * @param expGrp
     * @return true if the experiment object is present in the experiment group,
     *         else false.
     */
    private boolean isExperimentContainedInGroup(Experiment exp, ExperimentGroup expGrp) {
        if (expGrp.getExperimentCollection().contains(exp)) {
            return true;
        }
        return false;
    }

    /**
     * A callback validation function.
     */
    protected boolean validate(Object obj, DAO dao, String operation) throws DAOException {
        Experiment exp = ((Experiment) obj);
        Validator validator = new Validator();
        if (exp == null) {
            throw new DAOException("Null parameter passed instead of Experiment");
        }
        if (validator.isEmpty(exp.getName())) {
            throw new DAOException("Experiment name empty");
        }
        return true;
    }

    public Experiment getExperiment(Long identifier) throws DAOException {
        List expList = retrieve("Experiment", "id", identifier);
        Experiment exp = null;
        if (identifier != 0) {
            exp = (Experiment) expList.get(0);
        }
        populateEntitiesNames(exp.getDataListMetadataCollection());
        return exp;
    }

    /**
     * Returns root Id of given datalist
     * 
     * @param DataListMetadata
     * @return Long id
     * @throws HibernateException
     */
    public long getRootId(DataListMetadata dl) throws HibernateException {
        ArrayList al = new ArrayList();
        al.add(dl.getId());
        ArrayList list = (ArrayList) Utility.executeHQL("getRootIdForDataList", al);
        return Long.parseLong(list.get(0).toString());
    }

    /**
     * Returns all the datalists in the system.
     * 
     * @return Collection of datalists
     * @throws HibernateException
     */
    public Collection getAllDataLists() throws HibernateException {
        return Utility.executeHQL("getAllDataLists");
    }

    private void populateEntitiesNames(Collection<DataListMetadata> dataListMetadataCollection) {
        for (DataListMetadata dataListMetadata : dataListMetadataCollection) {
            for (Long id : dataListMetadata.getEntityIds()) {
                dataListMetadata.addEntityInfo(id, getEntityName(id), getOriginalEntityId(id));
            }
        }
    }

    /**
     * Returns entity name on entity id.
     * 
     * @param id
     * @return
     */
    private String getEntityName(Long id) {
        return DatalistCache.getInstance().getEntityWithId(id).getName();
    }

    private Long getOriginalEntityId(Long id) {
        EntityInterface entity = DatalistCache.getInstance().getEntityWithId(id);
        return DataListUtil.getOriginEntity(entity).getId();
    }

    /**
     * Adds given datalist to the given experiment
     * 
     * @param experimentId
     * @param dataListMetaDataId
     */
    public void addDataListToExperiment(Long experimentId, Long dataListMetaDataId) {
        try {
            List expList = retrieve("Experiment", "id", experimentId);
            Experiment exp = (Experiment) expList.get(0);

            List dataLists = retrieve("DataListMetadata", "id", dataListMetaDataId);
            DataListMetadata dataList = (DataListMetadata) dataLists.get(0);

            exp.addDataListMetadata(dataList);

            new UtilityOperations().update(exp);

        } catch (DAOException e) {
            throw (new RuntimeException(e.getMessage(), e));
        }
    }

    /**
     * get a set of root entities for an experiment where each root entity
     * represents a datalist
     * 
     * @param exp
     *            the experiment
     * @return set of root entities for an experiment where each root entity
     *         represents a datalist
     */
    public Set<Set<EntityInterface>> getDataListEntitySet(Experiment exp) {
        Set<Set<EntityInterface>> entitySet = new HashSet<Set<EntityInterface>>();

        // one datalistmetadata represents one datalist
        for (DataListMetadata dataListMetadata : exp.getDataListMetadataCollection()) {
            Set<Long> entityIds = dataListMetadata.getEntityIds();
            Set<EntityInterface> entities = new HashSet<EntityInterface>();
            entitySet.add(entities);
            for (Long id : entityIds) {
                entities.add(DatalistCache.getInstance().getEntityWithId(id));
            }
        }

        return entitySet;
    }

    /**
     * save the given data as a data category
     * 
     * @param title
     *            the title for the category
     * @param attributes
     *            list of attributes needed for the new entity
     * @param data
     *            the data to be saved
     * @return the newly created entity
     */
    public EntityInterface saveDataCategory(String title, List<AttributeInterface> attributes, Object[][] data) {
        List<AttributeInterface> newAtttributes = new ArrayList<AttributeInterface>();

        EntityInterface parentEntity = attributes.get(0).getEntity();
        EntityGroupInterface entityGroup = Utility.getEntityGroup(parentEntity);
        // DE's domain object factory
        DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
        // DE's Entity manager instance.
        EntityManagerInterface entityManager = EntityManager.getInstance();

        // create new entity
        EntityInterface newEntity = domainObjectFactory.createEntity();
        newEntity.setName(title);
        // addding tagged value for display name
        DynamicExtensionUtility.addTaggedValue(newEntity,
                                               edu.wustl.cab2b.common.util.Constants.ENTITY_DISPLAY_NAME, title);
        // adding tag value to indicate this is a filtered data category
        DynamicExtensionUtility.addTaggedValue(newEntity, edu.wustl.cab2b.common.util.Constants.FILTERED, "true");
        newEntity.addEntityGroupInterface(entityGroup);
        entityGroup.addEntity(newEntity);

        // add association. this call changes parent and new entities
        AssociationInterface association = DynamicExtensionUtility.createNewOneToManyAsso(parentEntity, newEntity);

        // add attributes
        for (AttributeInterface attribute : attributes) {
            AttributeInterface newAttribute = DynamicExtensionUtility.getAttributeCopy(attribute);
            newEntity.addAbstractAttribute(newAttribute);
            // needed for persistData()
            newAtttributes.add(newAttribute);

        }

        try {
            // no need to persist new entity separately
            entityManager.persistEntity(parentEntity, false);
            persistData(newEntity, newAtttributes, data);

        } catch (DynamicExtensionsApplicationException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATACATEGORY_SAVE_ERROR));
        } catch (DynamicExtensionsSystemException e) {
            throw (new RuntimeException(e.getMessage(), e, ErrorCodeConstants.DATACATEGORY_SAVE_ERROR));
        }

        return newEntity;

    }

    /**
     * persist the given data in the given entity
     * 
     * @param attributes
     *            list of attributes of the entity
     * @param data
     *            the data to be persisted
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     * 
     */
    private void persistData(EntityInterface entity, List<AttributeInterface> attributes, Object[][] data)
            throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException {
        // DE's Entity manager instance.
        EntityManagerInterface entityManager = EntityManager.getInstance();

        for (int i = 0; i < data.length; i++) {
            Map<AbstractAttributeInterface, Object> attributeMap = new HashMap<AbstractAttributeInterface, Object>();

            for (int j = 0; j < data[i].length; j++) {
                attributeMap.put(attributes.get(j), data[i][j]);
            }

            entityManager.insertData(entity, attributeMap);

        }

    }

    /**
     * Returns a model created for custom data category
     * 
     * @return CustomDataCategoryModel
     * @throws CheckedException
     */
    public CustomDataCategoryModel getDataCategoryModel(Experiment experiment) throws CheckedException {

        List<IdName> dataListIdName;
        Map<Long, List<IdName>> rootDlToLeafDlIdName = new HashMap<Long, List<IdName>>();

        Collection<DataListMetadata> dataListCollection;
        Collection<DataListMetadata> finalDataListCollection = new ArrayList<DataListMetadata>();
        try {
            dataListCollection = experiment.getDataListMetadataCollection();
            dataListsToAssociatedDataListsIdName(rootDlToLeafDlIdName, dataListCollection, finalDataListCollection);
            dataListIdName = getAllDataListIdName(finalDataListCollection);
            CustomDataCategoryModel model = new CustomDataCategoryModel(dataListIdName, rootDlToLeafDlIdName);
            return model;

        } catch (HibernateException e) {
            throw new CheckedException(e);
        } catch (DynamicExtensionsSystemException e) {
            throw new CheckedException(e);
        } catch (DynamicExtensionsApplicationException e) {
            throw new CheckedException(e);
        }
    }

    /**
     * 
     * Returns a map of every datalist id mapped to all its assiciated
     * datalists's IdName.
     * 
     * @param rootDlToLeafDlIdName
     * @param dataListCollection
     * @return
     * @throws HibernateException
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public Map dataListsToAssociatedDataListsIdName(Map<Long, List<IdName>> rootDlToLeafDlIdName,
                                                    Collection<DataListMetadata> dataListCollection,
                                                    Collection<DataListMetadata> finalDataListCollection)
            throws HibernateException, DynamicExtensionsSystemException, DynamicExtensionsApplicationException {
        for (DataListMetadata dl : dataListCollection) {
            if (dl.isCustomDataCategory()) {
                continue;
            }
            finalDataListCollection.add(dl);
            long dlId = dl.getId();
            EntityInterface entity = EntityManager.getInstance().getEntityByIdentifier(dl.getRootEntityId());
            Collection<AssociationInterface> associationCollection = entity.getAssociationCollection();
            List<IdName> enityIdName = new ArrayList<IdName>();
            for (AssociationInterface association : associationCollection) {
                EntityInterface entityInterface = association.getTargetEntity();
                IdName idName = new IdName(entityInterface.getId(), entityInterface.getName());
                enityIdName.add(idName);
            }
            rootDlToLeafDlIdName.put(dlId, enityIdName);
        }
        return rootDlToLeafDlIdName;
    }

    /**
     * Returns IdNames of given datalists
     * 
     * @param dataLists
     * @return List<IdName>
     */
    public List<IdName> getAllDataListIdName(Collection<DataListMetadata> dataLists) {

        List dataListIdName = new ArrayList<IdName>();
        for (DataListMetadata dlMetaData : dataLists) {
            IdName idName = new IdName(dlMetaData.getId(), dlMetaData.getName());
            dataListIdName.add(idName);
        }
        return dataListIdName;
    }

    /**
     * Returns entity by its Id
     * 
     * @param id
     * @return
     * @throws CheckedException
     * @throws RemoteException
     */
    public EntityInterface getEnityById(Long id) throws CheckedException, RemoteException {
        EntityInterface entity = null;
        try {
            entity = EntityManager.getInstance().getEntityByIdentifier(id);
        } catch (DynamicExtensionsSystemException e) {
            throw new CheckedException(e);
        } catch (DynamicExtensionsApplicationException e) {
            throw new CheckedException(e);
        }
        return entity;
    }

    /**
     * Returns collection of attributes of given entity Id.
     * 
     * @param id
     * @return
     * @throws RemoteException
     * @throws CheckedException
     */
    public Collection<AttributeInterface> getAllAttributes(Long id) throws RemoteException, CheckedException {
        List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
        List<AttributeInterface> finalAttributes = new ArrayList<AttributeInterface>();
        EntityInterface entity = getEnityById(id);
        processEntity(entity, attributes);
        for (AttributeInterface attr : attributes) {
            if (!edu.wustl.cab2b.server.datalist.DataListUtil.isVirtualAttribute(attr)) {
                finalAttributes.add(attr);
            }
        }
        return finalAttributes;
    }

    /**
     * Generates a tree which has enities and its attributes and all associated
     * entities structure.
     * 
     * @param entity
     * @param attributes
     */
    private void processEntity(EntityInterface entity, List<AttributeInterface> attributes) {
        attributes.addAll(entity.getAttributeCollection());
        for (AssociationInterface association : entity.getAssociationCollection()) {
            processEntity(association.getTargetEntity(), attributes);
        }
    }

    /**
     * Returns all the experiments which have datalist id same as given.
     * 
     * @param id
     * @return
     * @throws HibernateException
     */
    public Collection getExperimentsWithSimilarDataList(Long id) throws HibernateException {
        List idList = new ArrayList();
        idList.add(id);
        return Utility.executeHQL("getExperimentSimilarDataList", idList);
    }

}
