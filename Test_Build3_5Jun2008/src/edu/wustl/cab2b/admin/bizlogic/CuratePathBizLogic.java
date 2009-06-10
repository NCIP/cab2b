package edu.wustl.cab2b.admin.bizlogic;

/**
 * 
 * @author lalit_chand
 * This class sets the CuratePath variables 
 *
 */

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.admin.flex.CategoryDagPanel;
import edu.wustl.cab2b.admin.flex.DAGLink;
import edu.wustl.cab2b.admin.flex.DAGNode;
import edu.wustl.cab2b.admin.util.AdminConstants;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;

public class CuratePathBizLogic {

    /**
     * this method initialize the CuratedPath object 
     *     
     * @param session
     * @param curatePath
     */

    public void initialize(Map session, CuratedPath curatePath, boolean isSelected) {
        CategoryDagPanel categoryDAGPanel = (CategoryDagPanel) session.get(AdminConstants.CATEGORY_INSTANCE);
        Set<DAGNode> dagNodeSet = categoryDAGPanel.getDagNodeSet();
        Set<DAGLink> dagLinkSet = categoryDAGPanel.getDagPathSet();
        Set<IPath> iPathSet = new HashSet<IPath>();
        Set<EntityInterface> entitySet = new HashSet<EntityInterface>();
        Map<String, IPath> idVsPathMap = (Map<String, IPath>) session.get(AdminConstants.ID_VS_PATH_MAP);
        String key;
        for (DAGLink dagLink : dagLinkSet) {

            key = dagLink.getPathId();
            iPathSet.add(idVsPathMap.get(key));
            for (DAGNode dagNode : dagNodeSet) {

                if (dagLink.getSourceNodeId() == dagNode.getNodeId()) {
                    entitySet.add(EntityCache.getInstance().getEntityById(dagNode.getEntityId()));
                }
                if (dagLink.getDestinatioNodeId() == dagNode.getNodeId()) {
                    entitySet.add(EntityCache.getInstance().getEntityById(dagNode.getEntityId()));
                }
            }
        }

        curatePath.setPaths(iPathSet);
        curatePath.setEntitySet(entitySet);
        curatePath.setSelected(isSelected);

    }

}
