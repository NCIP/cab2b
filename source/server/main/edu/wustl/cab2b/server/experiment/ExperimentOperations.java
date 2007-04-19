package edu.wustl.cab2b.server.experiment;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.cab2b.common.domain.AdditionalMetadata;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.domain.ExperimentGroup;
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


public class ExperimentOperations extends DefaultBizLogic
{
	/**
	 * Hibernate DAO Type to use.
	 */
	int daoType = Constants.HIBERNATE_DAO;
	
	/**
	 * A function to persist an experiment object.
	 * @param exp experiment ot persist.
	 * @param daoType DAO Type to use.
	 * @throws UserNotAuthorizedException 
	 * @throws BizLogicException
	 */
	public void addExperiment(Object exp) throws BizLogicException, UserNotAuthorizedException 
	{
		insert(exp, daoType);
		Logger.out.info("experiment saved Successfully "+((Experiment)exp).getId());
	}
	
	/**
	 * A function to move an experiment from one experiment group to another.
	 * @param exp experiment to move.
	 * @param srcExpGrp source experiment group
	 * @param tarExpGrp target experiment group.
	 * @param daoType DAO type to use.
	 * @throws UserNotAuthorizedException 
	 * @throws BizLogicException 
	 * @throws Exception 
	 */
	public void move(Object exp, Object srcExpGrp, Object tarExpGrp) throws BizLogicException, UserNotAuthorizedException
	{
		Experiment experiment = (Experiment) exp;
		
		ExperimentGroup sourceExpGroup = (ExperimentGroup)srcExpGrp;
		
		if(! isExperimentContainedInGroup(experiment,sourceExpGroup))
		{
			throw (new BizLogicException("Experiment doesn't belong to Experiment Group."));
		}
		
		
		//Logger.out.info("sourceExpGroup.getExperimentCollection() : after "+sourceExpGroup.getExperimentCollection().size());
		
		ExperimentGroup targetExpGroup = (ExperimentGroup)tarExpGrp;
		if(isExperimentContainedInGroup(experiment,targetExpGroup))
		{
			throw (new BizLogicException("Experiment already belongs to target Experiment Group."));
		}
		
		if(sourceExpGroup == targetExpGroup){
			throw (new BizLogicException("Source and Target Experiment Groups are same."));
		}
		
		sourceExpGroup.getExperimentCollection().remove(experiment);
		targetExpGroup.getExperimentCollection().add(experiment);
		
		//Logger.out.info("targetExpGroup.getExperimentCollection() "+targetExpGroup.getExperimentCollection().size());
		
		//Logger.out.info("updating target source group collection");
		
		experiment.getExperimentGroupCollection().remove(sourceExpGroup);
		experiment.getExperimentGroupCollection().add(targetExpGroup);
		
		update(experiment, daoType);
		update(sourceExpGroup, daoType);
		update(targetExpGroup, daoType);
		
	}
	
	/**
	 * A function to copy an experiment from one group to another, 
	 * which is a shallow copy.
	 * 
	 * @param exp experiment to copy.
	 * @param tarExpGrp target experiment group.
	 * @param daoType DAO type to use.
	 * @throws BizLogicException 
	 * @throws UserNotAuthorizedException 
	 * @throws Exception 
	 */
	public void copy(Object exp, Object tarExpGrp) throws BizLogicException, UserNotAuthorizedException
	{
		Experiment experiment = (Experiment) exp;
		experiment.getName();
		
		ExperimentGroup targetExperimentGroup = (ExperimentGroup)tarExpGrp;
		
		if(isExperimentContainedInGroup(experiment,targetExperimentGroup))
		{
			throw (new BizLogicException("Experiment already belongs to target Experiment Group"));
		}
		
		targetExperimentGroup.getExperimentCollection().add(experiment);
		
		experiment.getExperimentGroupCollection().add(targetExperimentGroup);
		
		update(experiment, daoType);
		update(targetExperimentGroup, daoType);
		
	}
	
	
	
	
	
	public Vector getExperimentHierarchy() throws DAOException
	{
		Vector expHierarchyData = new Vector();
		List returner = new ArrayList();
		List returner1 = new ArrayList();
		
		String hql = "from ExperimentGroup as ExpGrp where ExpGrp.parentGroup.id is null ";
		
		DAO dao = DAOFactory.getInstance().getDAO(daoType);
		((AbstractDAO)dao).openSession(null);
				
		try{
	        //List myReturner = dao.executeQuery("from ExperimentGroup", null, false, null);
	        //Logger.out.info("returner expGrp ::::::: "+myReturner);
			returner = dao.executeQuery(hql, null, false, null);
			
		}catch(Exception exp)
		{
			exp.printStackTrace();
		}
		
		//TODO we have to use HQL here temporarily since DAO.retrieve doesn't suiport 
		// where conditions on aggregate function like collection.size > 0, etc;
		String hql1 = "from Experiment as Exp where Exp.experimentGroupCollection.size = 0";
		try{
			returner1 = dao.executeQuery(hql1, null, false, null);
			System.out.println("returner1 exp :: "+returner1);
		}catch(Exception exp)
		{
			exp.printStackTrace();
		}
		
		returner.addAll(returner1);
		
		((AbstractDAO)dao).closeSession();
		
		
		expHierarchyData = getExperimentMetadataHierarchy(returner);
		
		return expHierarchyData;
	}
	
	public Vector getExperimentMetadataHierarchy(Collection firstLevelRootNodes)
	{
		//System.out.println("In getExperimentMetadataHierarchy() "+firstLevelRootNodes);
		Vector returner = new Vector();
		Iterator iter = firstLevelRootNodes.iterator();
		while(iter.hasNext())
		{
			AdditionalMetadata metadata = (AdditionalMetadata)iter.next();
			
			if(metadata instanceof ExperimentGroup)
			{
				ExperimentGroup expGrp = (ExperimentGroup) metadata;
				//Logger.out.info("------expGrp------  "+expGrp+"  ----------");
				ExperimentTreeNode expTreeNode = new ExperimentTreeNode();
				expTreeNode.setExperimentGroup(true);
				
				Collection childrenExpNodes = expGrp.getExperimentCollection(); 				
				Collection childrenGrpNodes =expGrp.getChildrenGroupCollection();
				
				updateMetadataHierarchy((AdditionalMetadata)expGrp, expTreeNode, childrenExpNodes);  // exp    1
				updateMetadataHierarchy((AdditionalMetadata)expGrp, expTreeNode, childrenGrpNodes);  // expGrp 3 
				//expTreeNode.setU
				returner.add(expTreeNode);
				
			}else
			{
				//Logger.out.info("------exp------  "+metadata+"  ----------");
				Long nodeId = metadata.getId();
				String nodeName = metadata.getName();				
				ExperimentTreeNode expTreeNode = new ExperimentTreeNode(nodeId,nodeName);
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
	private void updateMetadataHierarchy(AdditionalMetadata expGroup, ExperimentTreeNode treeNode, Collection children)
	{
		treeNode.setIdentifier(expGroup.getId());
		treeNode.setName(expGroup.getName());
		treeNode.setDesc(expGroup.getDescription());
		treeNode.setCreatedOn(expGroup.getCreatedOn());
		treeNode.setLastUpdatedOn(expGroup.getLastUpdatedOn());
		
		if(children != null && children.size() != 0)
		{
			Iterator iter = children.iterator();
			while(iter.hasNext())
			{
				AdditionalMetadata expOrGrp = (AdditionalMetadata) iter.next();
				ExperimentTreeNode expTreeNode = new ExperimentTreeNode();
				if(expOrGrp instanceof ExperimentGroup)
				{
					expTreeNode.setExperimentGroup(true);
					ExperimentGroup eGroup = (ExperimentGroup) expOrGrp ;
					Collection childsGroupChildren = eGroup.getChildrenGroupCollection();
						updateMetadataHierarchy(expOrGrp, expTreeNode, childsGroupChildren);
					
					Collection childsExpChildren = eGroup.getExperimentCollection();
						updateMetadataHierarchy(expOrGrp, expTreeNode, childsExpChildren);
					
				}else
				{
					expTreeNode.setExperimentGroup(false);
					updateMetadataHierarchy(expOrGrp, expTreeNode, null);
				}
				treeNode.getChildNodes().add(expTreeNode);
			}
		}
	}
	
	
	/**
	 * A validation function to check the containment of an experiment in a experiment group
	 * @param exp experiment to check.
	 * @param expGrp 
	 * @return true if the experiment object is present in the experiment group, else false.
	 */
	private boolean isExperimentContainedInGroup(Experiment exp, ExperimentGroup expGrp)
	{
		if(expGrp.getExperimentCollection().contains( exp))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * A callback validation function.
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
		Experiment exp = ((Experiment)obj);
		Validator validator = new Validator();
		if(exp == null)
		{
			throw new DAOException("Null parameter passed instead of Experiment");
		}
		if(validator.isEmpty(exp.getName()))
		{
			throw new DAOException("Experiment name empty");
		}
		return true;
    }
	
	public Experiment getExperiment(Long identifier) throws DAOException
	{
		List expList = retrieve("Experiment", "id", identifier);
		Experiment exp = null;
		if (identifier != 0)
		{
			exp = (Experiment) expList.get(0);
		}

		return exp;
	}	
	
	public Set<EntityInterface> getDataListEntityNames(Experiment exp) 
	{
		Set<EntityInterface> entitySet = new HashSet<EntityInterface>();
		for (DataListMetadata dataListMetadata : exp.getDataListMetadataCollection()) 
		{
         Long rootDataListEntityId = dataListMetadata.getEntityId();
         EntityInterface rootDataListEntity = null;
		 try {
			rootDataListEntity = EntityManager.getInstance().getEntityByIdentifier(rootDataListEntityId);			
			
		 } catch (DynamicExtensionsSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 } catch (DynamicExtensionsApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}         
         getAssociatedEntities(rootDataListEntity,entitySet);          
	 	}
		return entitySet;  
	}
         
	
	private void getAssociatedEntities(EntityInterface entity, Set<EntityInterface> entitySet) {
        for(AssociationInterface association: entity.getAssociationCollection()) {
            EntityInterface targetEntity = association.getTargetEntity();
            entitySet.add(targetEntity);
            Logger.out.info("Entity Names :"+targetEntity);
            getAssociatedEntities(targetEntity,entitySet);
        }
    }
	
	
	
	
}
