package edu.wustl.cab2b.admin.bizlogic;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.admin.flex.CategoryDagPanel;
import edu.wustl.cab2b.admin.flex.DAGLink;
import edu.wustl.cab2b.admin.flex.DAGNode;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.path.CuratedPathOperations;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * This class sets the CuratePath variables
 * 
 * @author lalit_chand
 */
public class CuratePathBizLogic {

    public boolean saveCuratedPath(CategoryDagPanel categoryDAGPanel, Map<String, IPath> idVsPathMap,
                                   boolean isSelected) {
        CuratedPath curatedPath = createCuratedPath(categoryDAGPanel, idVsPathMap, isSelected);

        return saveCuratedPath(curatedPath);
    }

    private boolean saveCuratedPath(final CuratedPath curatedPath) {
        boolean isSaved = false;
        CuratedPathOperations curatedPathOperations = new CuratedPathOperations();
        if (!curatedPathOperations.isDuplicate(curatedPath)) {
            curatedPathOperations.saveCuratedPath(curatedPath);
            PathFinder.getInstance().addCuratedPath(curatedPath);
       //     Utility.addPathToCab2bCache(curatedPath);
            isSaved = true;
        }

        return isSaved;
    }

    /**
     * This method initialize the CuratedPath object
     * 
     * @param categoryDAGPanel
     * @param idVsPathMap
     * @param isSelected
     * @return
     */
    private CuratedPath createCuratedPath(CategoryDagPanel categoryDAGPanel, Map<String, IPath> idVsPathMap,
                                          boolean isSelected) {
        Set<DAGNode> dagNodeSet = categoryDAGPanel.getDagNodeSet();
        Set<DAGLink> dagLinkSet = categoryDAGPanel.getDagPathSet();

        Set<IPath> iPathSet = new HashSet<IPath>();
        Set<EntityInterface> entitySet = new HashSet<EntityInterface>();

        String key = null;
        for (DAGLink dagLink : dagLinkSet) {
            key = dagLink.getPathId();
            iPathSet.add(idVsPathMap.get(key));
            addEntityInterface(dagNodeSet, dagLink, entitySet);
        }

        CuratedPath curatedPath = new CuratedPath();
        curatedPath.setPaths(iPathSet);
        curatedPath.setEntitySet(entitySet);
        curatedPath.setSelected(isSelected);

        return curatedPath;
    }

    /**
     * It adds EntityInterface
     * 
     * @param dagNodeSet
     * @param dagLink
     * @param entitySet
     */
    private void addEntityInterface(Set<DAGNode> dagNodeSet, DAGLink dagLink, Set<EntityInterface> entitySet) {
        for (DAGNode dagNode : dagNodeSet) {
            if (dagLink.getSourceNodeId() == dagNode.getNodeId()) {
                entitySet.add(EntityCache.getInstance().getEntityById(dagNode.getEntityId()));
            }

            if (dagLink.getDestinatioNodeId() == dagNode.getNodeId()) {
                entitySet.add(EntityCache.getInstance().getEntityById(dagNode.getEntityId()));
            }
        }

    }

}
