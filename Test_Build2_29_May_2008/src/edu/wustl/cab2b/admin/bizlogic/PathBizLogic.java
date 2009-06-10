package edu.wustl.cab2b.admin.bizlogic;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.path.PathConstants;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;

/**
 * @author atul_jawale
 * @author chetan_patil
 *
 */
public class PathBizLogic extends DefaultBizLogic {

    /**
     * This method returns the CuratedPath for the given curated path identifier
     * @param id identifier of the curated path
     * @return curated path
     */
    public ICuratedPath getCuratePathById(Long id) {
        List curatePathList = null;
        try {
            curatePathList = retrieve(CuratedPath.class.getName(), "id", id);
        } catch (DAOException e) {
            throw new RuntimeException("Unable to retreive object, Exception:" + e.getMessage());
        }

        CuratedPath curatedPath = null;
        if (curatePathList != null && !curatePathList.isEmpty()) {
            curatedPath = (CuratedPath) curatePathList.get(0);
            postProcessCuratedPath(curatedPath);
        }

        return curatedPath;
    }

    /**
     * This method retrieves all the CuratedPath objects in the system.
     * Returns all the curated path available in the system.
     * @return List of all curated paths.
     */
    public List<ICuratedPath> getAllCuratedPath() throws RemoteException {
        List<ICuratedPath> curatePathList = null;
        try {
            curatePathList = (List<ICuratedPath>) retrieve(CuratedPath.class.getName());

            for (ICuratedPath curatedPath : curatePathList) {
                postProcessCuratedPath((CuratedPath) curatedPath);
            }
        } catch (DAOException e) {
            throw new RuntimeException("Unable to retreive object, Exception:" + e.getMessage());
        }

        return curatePathList;
    }

    /**
     * This method persists the CuratedPath object
     * @param curatedPath
     * @throws RemoteException
     */
    public final void saveCuratedPath(CuratedPath curatedPath) throws RemoteException {
        preProcessCuratedPath(curatedPath);
        try {
            insert(curatedPath, Constants.HIBERNATE_DAO);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save curatedPath, Exception:" + e.getMessage());
        }
    }

    /**
     * This method returns the Path object for the given path identifier
     * @param id identifier of the path
     * @return path object
     */
    public IPath getPathById(Long id) {
        List curatePathList = null;
        try {
            curatePathList = retrieve(Path.class.getName(), "pathId", id);
        } catch (DAOException e) {
            throw new RuntimeException("Unable to retreive object, Exception:" + e.getMessage());
        }

        IPath curatedPath = null;
        if (curatePathList != null && !curatePathList.isEmpty()) {
            curatedPath = (IPath) curatePathList.get(0);
            postProcessPath((Path) curatedPath);
        }

        return curatedPath;
    }

    public boolean isDuplicate(ICuratedPath curatedPath) {
        boolean isDuplicate = false;
        Set<ICuratedPath> curatedPaths = PathFinder.getInstance().autoConnect(curatedPath.getEntitySet());
        for (ICuratedPath path : curatedPaths) {
            if (curatedPath.equals(path)) {
                isDuplicate = true;
                break;
            }
        }
        return isDuplicate;
    }

    /**
     * This method process the curated path after retrieving it.
     * @param curatedPath
     */
    private void preProcessCuratedPath(CuratedPath curatedPath) {
        String entityIds = getStringRepresentation(curatedPath.getEntitySet());
        curatedPath.setEntityIds(entityIds);
    }

    /**
     * This method process the given curated path object before saving it.
     * @param curatedPath
     */
    private void postProcessCuratedPath(CuratedPath curatedPath) {
        AbstractEntityCache abstractEntityCache = EntityCache.getCache();

        Set<EntityInterface> entitySet = new HashSet<EntityInterface>();
        String[] entityIds = curatedPath.getEntityIds().split("-");
        for (String entityId : entityIds) {
            EntityInterface entity = abstractEntityCache.getEntityById(Long.getLong(entityId));
            entitySet.add(entity);
        }

        for (IPath iPath : curatedPath.getPaths()) {
            postProcessPath((Path) iPath);
        }
    }

    /**
     * This method process the given path object before saving it.
     * @param path
     */
    private void postProcessPath(Path path) {
        AbstractEntityCache abstractEntityCache = EntityCache.getCache();

        EntityInterface sourceEntity = abstractEntityCache.getEntityById(path.getSourceEntityId());
        path.setSourceEntity(sourceEntity);

        EntityInterface targetEntity = abstractEntityCache.getEntityById(path.getTargetEntityId());
        path.setTargetEntity(targetEntity);
    }

    /**
     * Generates string representation of given entity set after sorting it based on id.
     * String representation is IDs of entities concatenated by {@link PathConstants#ID_CONNECTOR}
     * @param entitySet Set of entities
     * @return String representation of the given entity set. 
     */
    private String getStringRepresentation(final Set<EntityInterface> entitySet) {
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

}
