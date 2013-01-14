/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import java.awt.Image;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.netbeans.graph.api.model.GraphEvent;
import org.netbeans.graph.api.model.builtin.GraphPort;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.cache.ClientSideCache;
import edu.wustl.cab2b.client.ui.dag.PathLink;
import edu.wustl.cab2b.client.ui.dag.ambiguityresolver.AmbiguityObject;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.client.ui.searchDataWizard.addLimit.IUpdateAddLimitUIInterface;
import edu.wustl.cab2b.client.ui.searchDataWizard.dag.ambiguityresolver.ResolveAmbiguity;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImages;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * Subclass of MainDagPanel to have a Dag which is independent of query or
 * expressions.
 * 
 * @author Hrishikesh Rajpathak
 * 
 */
public class CDCDagPanel extends MainDagPanel {

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.searchDataWizard.dag.MainDagPanel#performAutoConnect()
     */
    @Override
    public void performAutoConnect() {
        if (currentNodeList.size() < 2) {
            // Cannot perform connect all functionality
            JOptionPane.showMessageDialog(this.getParent().getParent().getParent(),
                                          "Please add atleast two nodes.", "Auto Connect Warning",
                                          JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<ClassNode> selectedNodes = viewController.getCurrentNodeSelection();
        if (selectedNodes == null || selectedNodes.size() <= 1) {
            // Cannot perform connect all functionality
            JOptionPane.showMessageDialog(this.getParent().getParent().getParent(),
                                          "Please select atleast two nodes", "Auto Connect Warning",
                                          JOptionPane.WARNING_MESSAGE);
            return;
        }

        Set<EntityInterface> entitySet = new HashSet<EntityInterface>();
        for (int i = 0; i < selectedNodes.size(); i++) {
            IndependentClassNode classNode = ((IndependentClassNode) (selectedNodes.get(i)));
            EntityInterface entity = ClientSideCache.getInstance().getEntityById(classNode.getEntityId());

            entitySet.add(entity);
        }
        super.checkPathValidity(entitySet, selectedNodes);
    }

    /**
     * Constructor
     * @param addLimitPanel
     * @param dagImageMap
     * @param pathFinder
     * @param isDAGForView
     */
    public CDCDagPanel(
            IUpdateAddLimitUIInterface addLimitPanel,
            Map<DagImages, Image> dagImageMap,
            IPathFinder pathFinder,
            boolean isDAGForView) {
        super(addLimitPanel, dagImageMap, pathFinder, isDAGForView);

    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.client.ui.searchDataWizard.dag.MainDagPanel#getPaths(edu.wustl.cab2b.client.ui.searchDataWizard.dag.ClassNode,
     *      edu.wustl.cab2b.client.ui.searchDataWizard.dag.ClassNode)
     */
    @Override
    protected List<IPath> getPaths(ClassNode sourceNode, ClassNode destNode) {

        Long sourceId = ((IndependentClassNode) sourceNode).getEntityId();
        Long destinationId = ((IndependentClassNode) destNode).getEntityId();

        EntityInterface sourceEntity = ClientSideCache.getInstance().getEntityById(sourceId);
        EntityInterface destinationEntity = ClientSideCache.getInstance().getEntityById(destinationId);

        AmbiguityObject ambiguityObject = new AmbiguityObject(sourceEntity, destinationEntity);
        ResolveAmbiguity resolveAmbigity = new ResolveAmbiguity(ambiguityObject, dagPathFinder);
        Map<AmbiguityObject, List<IPath>> map = resolveAmbigity.getPathsForAllAmbiguities();
        return map.get(ambiguityObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.client.ui.searchDataWizard.dag.MainDagPanel#linkNode(edu.wustl.cab2b.client.ui.searchDataWizard.dag.ClassNode,
     *      edu.wustl.cab2b.client.ui.searchDataWizard.dag.ClassNode)
     */
    @Override
    protected void linkNode(ClassNode sourceNode, ClassNode destNode) {
        List<IPath> paths = getPaths(sourceNode, destNode);
        if (paths == null || paths.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                                          "No path available/selected between source and destination categories",
                                          "Connect Nodes warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < paths.size(); i++) {
            LinkTwoNode(sourceNode, destNode, paths.get(i));
        }
    }

    /**
     * Links two node with provided path
     * @param sourceNode
     * @param destNode
     * @param path
     */
    private void LinkTwoNode(ClassNode sourceNode, ClassNode destNode, IPath path) {
        // From the query object get list of associations between these two node
        GraphPort sourcePort = new GraphPort();
        sourceNode.addPort(sourcePort);
        GraphPort targetPort = new GraphPort();
        destNode.addPort(targetPort);
        // The location where node is added now
        //sourceNode.addSourcePort(sourcePort);  //UNCOMMENT THIS!!!!!!!!!!!!!!!!!! DO THE NEEDFUL
        //ClassNodeRenderer -> drawAssociation();
        destNode.addTargetPort(targetPort);

        // now create the visual link..
        PathLink link = new PathLink();
        link.setSourcePort((GraphPort) sourcePort);
        link.setTargetPort((GraphPort) targetPort);

        link.setPath(path);
        sourceNode.setLinkForSourcePort(sourcePort, link);
        link.setTooltipText(edu.wustl.cab2b.common.util.Utility.getPathDisplayString(path));
        graphDocument.addComponents(GraphEvent.createSingle(link));

        viewController.getHelper().scheduleNodeToLayout(sourceNode);
        viewController.getHelper().scheduleNodeToLayout(destNode);
        viewController.getHelper().scheduleLinkToLayout(link);
        viewController.getHelper().recalculate();

    }
}
