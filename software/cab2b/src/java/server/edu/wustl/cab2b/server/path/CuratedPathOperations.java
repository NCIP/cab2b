/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.path;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.metadata.path.Path;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;

/**
 * Class for C R U D operations on curated path
 * @author atul_jawale
 * @author chetan_patil
 */
public class CuratedPathOperations extends DefaultBizLogic {
//TODO need to take care of post processing of PATH similar to Curated path, Path should also be populated
    /**
     * This method returns the CuratedPath for the given curated path identifier
     * @param id identifier of the curated path
     * @return curated path
     */
    public ICuratedPath getCuratePathById(Long id) {
        List<?> curatePathList = null;
        try {
            curatePathList = retrieve(CuratedPath.class.getName(), "id", id);
        } catch (DAOException e) {
            throw new RuntimeException("Unable to retreive object, Exception:" + e.getMessage());
        }
        return (ICuratedPath) curatePathList.get(0);
    }

    /**
     * This method retrieves all the CuratedPath objects in the system.
     * Returns all the curated path available in the system.
     * @return List of all curated paths.
     */
    @SuppressWarnings("unchecked")
    public List<ICuratedPath> getAllCuratedPath() {
        List<ICuratedPath> curatePathList = null;
        try {
            curatePathList = (List<ICuratedPath>) retrieve(CuratedPath.class.getName());
        } catch (DAOException e) {
            throw new RuntimeException("Unable to retreive object, Exception:" + e.getMessage());
        }
        return curatePathList;
    }

    /**
     * This method persists the CuratedPath object
     * @param curatedPath
     */
    public final void saveCuratedPath(CuratedPath curatedPath) {
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
        List<?> pathList = null;
        try {
            pathList = retrieve(Path.class.getName(), "pathId", id);
        } catch (DAOException e) {
            throw new RuntimeException("Unable to retreive object, Exception:" + e.getMessage());
        }
        return (IPath) pathList.get(0);
    }

    /**
     * Checks whether path is duplicate or not.
     * @param curatedPath
     * @return Result whether this path is duplicate or not
     */
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
}