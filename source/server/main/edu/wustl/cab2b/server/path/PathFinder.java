package edu.wustl.cab2b.server.path;


import static edu.wustl.cab2b.server.path.PathConstants.INTER_MODEL_ASSOCIATION_TYPE;
import static edu.wustl.cab2b.server.path.PathConstants.INTRA_MODEL_ASSOCIATION_TYPE;

import java.sql.Connection;
import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.util.SQLQueryUtil;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.impl.InterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.impl.IntraModelAssociation;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.util.logger.Logger;

/**
 * This class provides methods to find all possible paths in which 
 * a collection of classes can be connected to a destination.
 * It finds inter model as well as intra model paths. 
 * A path is defined as ordered list of associations.
 * This class internally refers to tables PATH,INTER_MODEL_ASSOCIATION,
 * INTRA_MODEL_ASSOCIATION,ASSOCIATION.
 * 
 * @author Chandrakant Talele
 */
public class PathFinder {
    private List<InterModelConnection> interModelConnections;

    private Map<String, List<PathRecord>> pathRecordCache;

    private EntityCache cache;

    static private PathFinder pathFinder;
    private Map<Long, PathRecord> idVsPathRecord;
    private Map<String,Set<ICuratedPath>> entitySetVsCuratedPath;
    /**
     * @return Returns the singleton instance of the pathFinder class.
     */
    public static synchronized PathFinder getInstance() {
        if (pathFinder == null) {
            Logger.out.info("PathFinder Called first Time.Loading cache...");
            pathFinder = new PathFinder();
            pathFinder.setCache(EntityCache.getInstance());
        }
        return pathFinder;
    }
    /**
     * Finds all curated paths defined for given source and desination entity.
     * If no curated path is present, a empty set will be returned.
     * @param source The source entity
     * @param destination The destination entity
     * @param con Database connection to use
     * @return Set of curated paths
     */
    public Set<ICuratedPath> getCuratedPaths(EntityInterface source, EntityInterface destination, Connection con) {
        HashSet<EntityInterface> entitySet = new HashSet<EntityInterface>(2);
        entitySet.add(source);
        entitySet.add(destination);
        Set<ICuratedPath> curatedPathSet = autoConnect(entitySet, con);
        //Filtering the paths. As paths in a curated paths does not form cycles.So checking only first path 
        //in the pathList of each curated path will be sufficient
        Set<ICuratedPath> curatedPaths = new HashSet<ICuratedPath>();
        for (ICuratedPath curatedPath : curatedPathSet) {
            Set<IPath> paths = curatedPath.getPaths();
            //a curated path will have atleast one path present in it. no need to check it.
            IPath path = paths.iterator().next();
            if (source.getId().equals(path.getSourceEntity().getId())) {
                //this is a matching path. 
                curatedPaths.add(curatedPath);
            }
        }
        return curatedPaths;
    }
    /**
     * Finds all curated paths defined for given set of entities.
     * @param entitySet Set of entities
     * @param con Database connection to use
     * @return Set of curated paths
     */
    public Set<ICuratedPath> autoConnect(Set<EntityInterface> entitySet, Connection con) {
        //throw new java.lang.RuntimeException("This method is not implemented yet !!!! ");
        String entityIdSet = getStringRepresentation(entitySet);
        entitySetVsCuratedPath = getAllCuratedPaths(con);
        Set<ICuratedPath> curatedPathSet = entitySetVsCuratedPath.get(entityIdSet);
        
        if(curatedPathSet==null) {
            curatedPathSet = new HashSet<ICuratedPath>(0);
        }
        return curatedPathSet;
    }

    /**
     * Finds all possible paths in which a collection of classes (sourceList) can be connected to a destination.
     * Returns map with key : EntityInterface and value as "List of IPath".
     * VALUE in Map is list of all possible paths from Entity which is its KEY and passed destination.<br>
     * KeySet of the map contains only those classes from which there are one or more paths present to destination.
     * That is KeySet will be subset of collection of classes passed as "sourceList". 
     * 
     * This method can be used when a new class is added in DAG view.
     * 
     * @param sourceList collection of classes each of which will be treated as source.
     * @param destination End of the path 
     * @param con Database connection to be used.
     * @return Returns the Map<EntityInterface,List<IPath>>
     */
    public Map<EntityInterface, List<IPath>> getAllPossiblePaths(List<EntityInterface> sourceList,
                                                                 EntityInterface destination, Connection con) {
        Logger.out.debug("Entering in method getAllPossiblePaths()");
        Map<EntityInterface, List<IPath>> mapToReturn = new HashMap<EntityInterface, List<IPath>>(
                sourceList.size());
        
        for (EntityInterface srcEntity : sourceList) {
            List<IPath> pathList = getAllPossiblePaths(srcEntity, destination, con);
            if (pathList.size() != 0) {
                mapToReturn.put(srcEntity, pathList);
            }
        }
        return mapToReturn;
    }

    public List<IPath> getAllPossiblePaths(EntityInterface source, EntityInterface destination, Connection con) {
        Logger.out.debug("Entering in method getAllPossiblePaths()");
        cachePathRecords(con);
        Long desEntityId = destination.getId();
        String desName = destination.getName();
        EntityGroupInterface desEntityGroup = Utility.getEntityGroup(destination);
        
        Long srcEntityId = source.getId();
        String srcName = source.getName();
        EntityGroupInterface srcEntityGroup = Utility.getEntityGroup(source);
        interModelConnections = getAllInterModelConnections(con);
        //Are they belong to same entity group ?? 
        if (desEntityGroup.equals(srcEntityGroup)) {
            Logger.out.info("Finding intramodel paths between : " + srcName + " and "+ desName);
            List<PathRecord> pathRecords = fetchPathRecords(srcEntityId, desEntityId);
            List<IPath> pathList = new ArrayList<IPath>(getPathList(pathRecords, con));
            return pathList;
        } else { 
            //they are from different models
            Logger.out.info("Finding intermodel paths between : " + srcName + " and " + desName);
            for (InterModelConnection interModelConnection : interModelConnections) {

                Long leftEntityId = interModelConnection.getLeftEntityId();
                EntityGroupInterface leftEntityGroup = Utility.getEntityGroup(cache.getEntityById(leftEntityId));
                if (srcEntityGroup.equals(leftEntityGroup)) {

                    Long rightEntityId = interModelConnection.getRightEntityId();
                    EntityGroupInterface rightEntityGroup = Utility.getEntityGroup(cache.getEntityById(rightEntityId));

                    if (rightEntityGroup.equals(desEntityGroup)) {
                        List<Path> pathsSrcToLeftEntity = new ArrayList<Path>();
                        if (!srcEntityId.equals(leftEntityId)) {
                            List<PathRecord> pathRecordsSrcToLeftEntity = fetchPathRecords(srcEntityId,
                                                                                           leftEntityId);
                            if (pathRecordsSrcToLeftEntity.size() == 0) {
                                continue;
                            }
                            pathsSrcToLeftEntity = getPathList(pathRecordsSrcToLeftEntity, con);
                        }
                        List<Path> pathsRightEntityToDes = new ArrayList<Path>();
                        if (!rightEntityId.equals(desEntityId)) {
                            List<PathRecord> pathRecordsRightEntityToDes = fetchPathRecords(rightEntityId,
                                                                                            desEntityId);
                            if (pathRecordsRightEntityToDes.size() == 0) {
                                continue;
                            }
                            pathsRightEntityToDes = getPathList(pathRecordsRightEntityToDes, con);
                        }
                        InterModelAssociation association = getInterModelAssociation(interModelConnection);
                        List<IPath> pathList = connectPaths(pathsSrcToLeftEntity, association,
                                                            pathsRightEntityToDes);
                        return pathList;
                    }
                }
            }

        }
        return new ArrayList<IPath>(0);
    }

    /**
     * Connect each path of passes "prePaths" to each path of passed "postPaths"  by association.
     * If "prePaths" is empty then "association" will be prefixed to all paths in "postPaths".
     * If "postPaths" is empty then "association" will be postfixed to all paths in "prePaths".
     * else the size of the returned list will be = prePaths.size() * postPaths.size().
     * @param prePaths   list of paths which will be prefixed in the returned path list. 
     * @param association  The association which serves as connection between those.
     * @param postPaths list of paths which will be postfixed in the returned path list.
     * @return Path list generated after concatination. 
     */
    List<IPath> connectPaths(List<Path> prePaths, InterModelAssociation association, List<Path> postPaths) {
        List<IPath> list = new ArrayList<IPath>(prePaths.size() * postPaths.size());
        if (prePaths.size() == 0 && postPaths.size() == 0) {
            List<IAssociation> allAssociation = new ArrayList<IAssociation>(1);
            allAssociation.add(association.clone());
            list.add(new Path(association.getSourceEntity(), association.getTargetEntity(), allAssociation));

        } else if (prePaths.size() == 0) {
            for (Path postPath : postPaths) {
                List<IAssociation> allAssociation = new ArrayList<IAssociation>();
                allAssociation.add(association.clone());
                allAssociation.addAll(postPath.getIntermediateAssociations());
                list.add(new Path(association.getSourceEntity(), postPath.getTargetEntity(), allAssociation));
            }
        } else if (postPaths.size() == 0) {
            for (Path prePath : prePaths) {
                List<IAssociation> allAssociation = new ArrayList<IAssociation>(
                        prePath.getIntermediateAssociations());
                allAssociation.add(association.clone());
                list.add(new Path(prePath.getSourceEntity(), association.getTargetEntity(), allAssociation));
            }
        } else {
            for (Path prePath : prePaths) {
                for (Path postPath : postPaths) {
                    List<IAssociation> allAssociation = new ArrayList<IAssociation>(
                            prePath.getIntermediateAssociations());
                    allAssociation.add(association.clone());
                    allAssociation.addAll(postPath.getIntermediateAssociations());
                    list.add(new Path(prePath.getSourceEntity(), postPath.getTargetEntity(), allAssociation));
                }
            }
        }
        return list;
    }

    /**
     * Converts given interModelConnection to InterModelAssociation and returns it.
     * @param interModelConnection InterModelConnection to transform
     * @return Returns the InterModelAssociation
     */
    InterModelAssociation getInterModelAssociation(InterModelConnection interModelConnection) {
        Long leftEntityId = interModelConnection.getLeftEntityId();
        Long leftAttrId = interModelConnection.getLeftAttributeId();

        AttributeInterface leftAttr = getAttribute(leftEntityId, leftAttrId);

        Long rightEntityId = interModelConnection.getRightEntityId();
        Long rightAttrId = interModelConnection.getRightAttributeId();
        AttributeInterface rightAttr = getAttribute(rightEntityId, rightAttrId);

        return new InterModelAssociation(leftAttr, rightAttr);
    }

    /**
     * Returns all the intermodel connections available in the system. 
     * @param con Database connection to use for fetching the intermodel connections
     * @return List of all InterModelConnection.
     */
    public List<InterModelConnection> getAllInterModelConnections(Connection con) {
        if (interModelConnections != null) {
            return interModelConnections;
        }
        String sql = "select LEFT_ENTITY_ID,LEFT_ATTRIBUTE_ID,RIGHT_ENTITY_ID,RIGHT_ATTRIBUTE_ID from INTER_MODEL_ASSOCIATION";
        String[][] result = executeQuery(sql, con);
        List<InterModelConnection> list = getInterModelAssociations(result);
        return list;
    }

    /**
     * Generates a Path for each PathRecord and returns it.
     * @param pathRecords Array of PathRecords to process
     * @param con Database connection to use.
     * @return Returns the List of Generated Paths.
     */
    List<Path> getPathList(InterModelConnection interModelConnection) {
        InterModelAssociation association = getInterModelAssociation(interModelConnection);
        EntityInterface src = association.getSourceEntity();
        EntityInterface des = association.getTargetEntity();

        List<IAssociation> list = new ArrayList<IAssociation>(1);
        list.add(association);

        Path path = new Path(src, des, list);

        ArrayList<Path> returnList = new ArrayList<Path>(1);
        returnList.add(path);

        return returnList;
    }

    /**
     * Generates a Path for each PathRecord and returns it.
     * @param pathRecords Array of PathRecords to process
     * @param con Database connection to use.
     * @return Returns the List of Generated Paths.
     */
    List<Path> getPathList(List<PathRecord> pathRecords, Connection con) {
        List<Path> pathList = new ArrayList<Path>(pathRecords.size());
        for (PathRecord pathRecord : pathRecords) {
            pathList.add(getPath(pathRecord,con));
        }
        return pathList;
    }
    /**
     * Generates a Path for given PathRecord and returns it.
     * @param pathRecord PathRecords to convert.
     * @param con Database connection to use.
     * @return Returns the generated Path.
     */
    Path getPath(PathRecord pathRecord, Connection con) {
        List<IAssociation> associations = new ArrayList<IAssociation>();
        for (Long associationId : pathRecord.getAssociationSequence()) {
            associations.add(getAssociation(associationId, con));
        }
        Long firstEntityId = pathRecord.getFirstEntityId();
        Long lastEntityId = pathRecord.getLastEntityId();

        EntityInterface firstEntity = cache.getEntityById(firstEntityId);
        EntityInterface lastEntity = cache.getEntityById(lastEntityId);
        return new Path(pathRecord.getPathId(), firstEntity, lastEntity, associations);
    }
    /**
     * It returns the IAssociation for given associationId.
     * This method first find out the type of the assication. 
     * Then based on the type it looks into INTRA_MODEL_ASSOCIATION or INTER_MODEL_ASSOCIATION table to get correct object.
     * @param associationId ID for which a IAssociation is to be found.
     * @param con Database conenction to use.
     * @return The IAssociation for given indentifier
     */
    IAssociation getAssociation(Long associationId, Connection con) {
        Logger.out.debug("executing SQL for association Id :" + associationId);
        String[] result = runSqlToGetOneRecord(
                                               "select ASSOCIATION_TYPE from ASSOCIATION where ASSOCIATION_ID = ?",
                                               con, associationId);
        Integer type = Integer.parseInt(result[0]);
        if (type.equals(INTRA_MODEL_ASSOCIATION_TYPE)) {
            String sql = "select DE_ASSOCIATION_ID from INTRA_MODEL_ASSOCIATION where ASSOCIATION_ID = ?";
            result = runSqlToGetOneRecord(sql, con, associationId);
            AssociationInterface association = cache.getAssociationById(Long.parseLong(result[0]));
            return new IntraModelAssociation(association);

        } else if (type.equals(INTER_MODEL_ASSOCIATION_TYPE)) {
            String sql = "select LEFT_ENTITY_ID,LEFT_ATTRIBUTE_ID,RIGHT_ENTITY_ID,RIGHT_ATTRIBUTE_ID from INTER_MODEL_ASSOCIATION where ASSOCIATION_ID = ?";

            result = runSqlToGetOneRecord(sql, con, associationId);
            Long sourceEntityId = Long.parseLong(result[0]);
            Long sourceAttributeId = Long.parseLong(result[1]);
            Long targetEntityId = Long.parseLong(result[2]);
            Long targetAttributeId = Long.parseLong(result[3]);
            AttributeInterface sourceAttribute = getAttribute(sourceEntityId, sourceAttributeId);
            AttributeInterface targetAttribute = getAttribute(targetEntityId, targetAttributeId);
            return new InterModelAssociation(sourceAttribute, targetAttribute);
        }
        throw new RuntimeException("ASSOCIATION.ASSOCIATION_TYPE has values which are not permissible");
    }

    /**
     * @param id Path id for which a path is expected.
     * @param connection Database connection to use.
     * @return IPath object for given pathId
     */
    public IPath getPathById(Long id, Connection connection) {
        String sql = "select PATH_ID,FIRST_ENTITY_ID,INTERMEDIATE_PATH,LAST_ENTITY_ID from PATH where PATH_ID = ?";
        String[] resultSet = runSqlToGetOneRecord(sql, connection, id);
        List<PathRecord> list = new ArrayList<PathRecord>();
        list.add(getPathRecord(resultSet));
        List<Path> result = getPathList(list, connection);
        return result.get(0);
    }

    /**
     * This method fetches all the paths from database from table PATH.
     * It then popullates {@link PathRecord} object per row and returns the array.
     * This is to encapsulate the database code from other.  
     * @param srcId Source entity Id
     * @param desId Target Entity Id
     * @return Returns the PathRecord[] all records 
     */
    List<PathRecord> fetchPathRecords(long srcId, long desId) {
        String key = srcId + "_" + desId;
        if (pathRecordCache.containsKey(key)) {
            return pathRecordCache.get(key);
        }
        return new ArrayList<PathRecord>(0);
    }

    /**
     * This method fetches all the paths from database from table PATH.
     * It then popullates {@link PathRecord} object per row and returns the array.
     * This is to encapsulate the database code from other.  
     * @param srcId Source entity Id
     * @param desId Target Entity Id
     * @param connection Database connection to use to fire queries.
     * @return Returns the PathRecord[] all records 
     */
    public synchronized Map<String, List<PathRecord>> cachePathRecords(Connection connection) {
        Logger.out.debug("Entering in method cachePathRecords");
        if (pathRecordCache == null) { 
            String sql = "select PATH_ID,FIRST_ENTITY_ID,INTERMEDIATE_PATH,LAST_ENTITY_ID from PATH";
            
            String[][] resultSet = executeQuery(sql, connection);
            pathRecordCache = new HashMap<String, List<PathRecord>>(resultSet.length);
            idVsPathRecord = new HashMap<Long, PathRecord>(resultSet.length);
            for (int i = 0; i < resultSet.length; i++) {
                PathRecord pathRecord = getPathRecord(resultSet[i]);
                String key = pathRecord.getFirstEntityId() + "_" + pathRecord.getLastEntityId();
                idVsPathRecord.put(pathRecord.getPathId(), pathRecord);
                if (pathRecordCache.containsKey(key)) {
                    pathRecordCache.get(key).add(pathRecord);
                } else {
                    ArrayList<PathRecord> list = new ArrayList<PathRecord>();
                    list.add(pathRecord);
                    pathRecordCache.put(key, list);
                }
    
            }
        }
        Logger.out.debug("Leaving method cachePathRecords");
        return pathRecordCache;
    }

    /**
     * @param record
     * @return
     */
    private PathRecord getPathRecord(String[] record) {
        Long pathId = Long.parseLong(record[0]);
        Long firstId = Long.parseLong(record[1]);
        String path = record[2];
        Long lastId = Long.parseLong(record[3]);
        Logger.out.debug(pathId + "  " + firstId + " " + path + " " + lastId);
        return new PathRecord(pathId, firstId, path, lastId);

    }

    /**
     * Returns the AttributeInterface whose id is passed "attributeId" and parent is entity of id "entityId".
     * @param entityId Id of parent Entity.
     * @param attributeId Id of the required attribiute.
     * @return Attribute of passed entity with given id. 
     */
    AttributeInterface getAttribute(Long entityId, Long attributeId) {
        return cache.getEntityById(entityId).getAttributeByIdentifier(attributeId);
    }

    /**
     * Returns all the InterModelAssociations where given entity is present.
     * @param sourceEntityId id of the source Entity
     * @return The list of IInterModelAssociation
     */
    public List<IInterModelAssociation> getInterModelAssociations(Long sourceEntityId, Connection connection) {

        String sql = "select LEFT_ENTITY_ID,LEFT_ATTRIBUTE_ID,RIGHT_ENTITY_ID,RIGHT_ATTRIBUTE_ID from inter_model_association where left_entity_id = ?";

        String[][] result = executeQuery(sql, connection, sourceEntityId);
        List<IInterModelAssociation> list = new ArrayList<IInterModelAssociation>();
        for (InterModelConnection conn : getInterModelAssociations(result)) {
            list.add(getInterModelAssociation(conn));
        }
        return list;
    }

    /**
     * @param result Result array where each row represents a row in INTER_MODEL_ASSOCIATION.
     * Order of column is LEFT_ENTITY_ID,LEFT_ATTRIBUTE_ID,RIGHT_ENTITY_ID,RIGHT_ATTRIBUTE_ID 
     * @return List of InterModelConnection.
     */
    private List<InterModelConnection> getInterModelAssociations(String[][] result) {
        List<InterModelConnection> list = new ArrayList<InterModelConnection>(result.length);
        for (int i = 0; i < result.length; i++) {
            Long leftEntityId = Long.parseLong(result[i][0]);
            Long leftAttrId = Long.parseLong(result[i][1]);
            Long rightEntityId = Long.parseLong(result[i][2]);
            Long rightAttrId = Long.parseLong(result[i][3]);

            AttributeInterface leftAttr = getAttribute(leftEntityId, leftAttrId);
            AttributeInterface rightAttr = getAttribute(rightEntityId, rightAttrId);

            list.add(new InterModelConnection(leftAttr, rightAttr));
        }
        return list;
    }

    /**
     * @param sql SELECT SQL to execute
     * @param connection Database connection to use.
     * @param params Parameters for SQL.
     * @return 2-D array of results with rows as records and columns as COLUMNS in select. 
     */
    //this method is package scoped for testing purpose
    String[][] executeQuery(String sql, Connection connection, Object... params) {
        return SQLQueryUtil.executeQuery(sql, connection, params);
    }

    /**
     * @param sql SELECT sql which expect only one record to be returned. 
     * @param connection Database connection to use.
     * @param params Parameters for SQL.
     * @return Only one record
     */
    //  this method is package scoped for testing purpose
    String[] runSqlToGetOneRecord(String sql, Connection connection, Object... params) {
        String[][] resultSet = SQLQueryUtil.executeQuery(sql, connection, params);
        if (resultSet.length != 1) {
            throw new RuntimeException("Only one record is expected for SQL " + sql,
                    new java.lang.RuntimeException(), ErrorCodeConstants.DE_0003);
        }
        return resultSet[0];
    }
    /**
     * Splits string representation into list of IDs, gets entity for each ID and returns ste of entities.
     * @param stringRepresentationOfEntitySet The string representation of entity set generated by {@link PathFinder#getStringRepresentation(Set)} 
     * @return Set of entities whose ids are present in given string representation of entity set.
     */
    Set<EntityInterface> getEntitySet(String stringRepresentationOfEntitySet) {
        EntityCache cache = EntityCache.getInstance();
        HashSet<EntityInterface> entitySet = new HashSet<EntityInterface>();
        String[] ids = stringRepresentationOfEntitySet.split(PathConstants.ID_CONNECTOR);
        for(String str : ids) {
            Long entityId = Long.parseLong(str);
            entitySet.add(cache.getEntityById(entityId));
        }
        return entitySet;
    }
    /**
     * Generates string representation of given entity set after sorting it based on id.
     * String representation is IDs of entities concatenated by {@link PathConstants#ID_CONNECTOR}
     * @param entitySet Set of entities
     * @return String representation of the given entity set. 
     */
    String getStringRepresentation(Set<EntityInterface> entitySet) {
        if (entitySet.isEmpty()) {
            return "";
        }
        ArrayList<Long> ids = new ArrayList<Long>(entitySet.size());
        for (EntityInterface en : entitySet) {
            ids.add(en.getId());

        }
        Collections.sort(ids);
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(ids.get(0));
        for (int i = 1; i < ids.size(); i++) {
            strBuilder.append(PathConstants.ID_CONNECTOR);
            strBuilder.append(ids.get(i));
        }
        return strBuilder.toString();
    }
    /**
     * @param con
     */
    synchronized Map<String,Set<ICuratedPath>> getAllCuratedPaths(Connection con) {
        if(entitySetVsCuratedPath !=null) {
            return entitySetVsCuratedPath;
        }
        cachePathRecords(con);
        Map<String,Set<ICuratedPath>> entitySetStringVsCuratedPath = new HashMap<String,Set<ICuratedPath>>();
        HashMap<Long,CuratedPath> idVsCuratedPath = new HashMap<Long, CuratedPath>();
        String sql = "SELECT CURATED_PATH.curated_path_Id,entity_ids,selected,path_id from CURATED_PATH JOIN CURATED_PATH_TO_PATH ON CURATED_PATH.curated_path_Id = CURATED_PATH_TO_PATH.curated_path_Id";
        String[][] resultSet = executeQuery(sql, con);
        for(String[] oneRecord : resultSet) {
            Long curatedPathId = Long.parseLong(oneRecord[0]);
            CuratedPath curatedPath = null;
            if(idVsCuratedPath.containsKey(curatedPathId)) {
                curatedPath = idVsCuratedPath.get(curatedPathId);
            } else {
                String entitySetString = oneRecord[1];
                Set<EntityInterface> entitySet = getEntitySet(entitySetString);
                Boolean isSelected = Boolean.parseBoolean(oneRecord[2]);
                curatedPath = new CuratedPath(curatedPathId,entitySet,isSelected);
                idVsCuratedPath.put(curatedPathId,curatedPath);
                Set<ICuratedPath> paths = entitySetStringVsCuratedPath.get(entitySetString);
                if(paths==null) {
                    paths = new HashSet<ICuratedPath>();
                    entitySetStringVsCuratedPath.put(entitySetString, paths);
                }
                paths.add(curatedPath);
            }
            Long pathId = Long.parseLong(oneRecord[3]);
            PathRecord pathRecord = idVsPathRecord.get(pathId);
            curatedPath.addPath(getPath(pathRecord, con));
        }
        return entitySetStringVsCuratedPath;
    }
    /**
     * Default Constructor for singleton. 
     */
    //  this method is package scoped for testing purpose
    PathFinder() {

    }

    //  this method is package scoped for testing purpose
    void setInstance(PathFinder pathFinder) {
        PathFinder.pathFinder = pathFinder;
    }

    //  this method is package scoped for testing purpose
    void setCache(EntityCache entityCache) {
        cache = entityCache;
    }
    /**
     * Drops all the static data structures and creates them with latest database state 
     */
    public void refreshCache() {
        interModelConnections = null;
        pathRecordCache = null;
        setCache(EntityCache.getInstance());
    }
}