package edu.wustl.cab2b.client.ui.dag;

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
import edu.wustl.cab2b.client.ui.IUpdateAddLimitUIInterface;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.dag.ambiguityresolver.AmbiguityObject;
import edu.wustl.cab2b.client.ui.dag.ambiguityresolver.AutoConnectAmbiguityResolver;
import edu.wustl.cab2b.client.ui.dag.ambiguityresolver.ResolveAmbiguity;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImages;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * Subclass of MainDagPanel to have a Dag which is independent of query or
 * expressions.
 * 
 * @author Hrishikesh Rajpathak
 * 
 */
public class CDCDagPanel extends MainDagPanel {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.client.ui.dag.MainDagPanel#performAutoConnect()
	 */
	@Override
	public void performAutoConnect() {
		if (m_currentNodeList.size() < 2) {
			// Cannot perform connect all functionality
			JOptionPane.showMessageDialog(this.getParent().getParent().getParent(),
					"Please add atleast two nodes.", "Auto Connect Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		List<ClassNode> selectedNodes = m_viewController.getCurrentNodeSelection();
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
			EntityInterface entity = ClientSideCache.getInstance().getEntityById(
					classNode.getEntityId());

			entitySet.add(entity);
		}

		Set<ICuratedPath> paths = m_pathFinder.autoConnect(entitySet);
		if (paths == null || paths.size() <= 0) {
			// Cannot perform connect all functionality
			JOptionPane.showMessageDialog(this,
					"No curated path available between selected nodes.", "Auto-Connect warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		ICuratedPath path = getSelectedCuratedPath(paths);
		if (path == null) {
			// Show ambiguity resolver to get a curated path
			AutoConnectAmbiguityResolver childPanel = new AutoConnectAmbiguityResolver(paths);
			WindowUtilities.showInDialog(NewWelcomePanel.mainFrame, childPanel,
					"Available curated paths Panel", Constants.WIZARD_SIZE2_DIMENSION, true, false);
			path = childPanel.getUserSelectedPath();
		}

		if (path != null) {
			autoConnectNodes(selectedNodes, path);
		}
	}

	public CDCDagPanel(IUpdateAddLimitUIInterface addLimitPanel, Map<DagImages, Image> dagImageMap,
			IPathFinder pathFinder, boolean isDAGForView) {

		super(addLimitPanel, dagImageMap, pathFinder, isDAGForView);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.client.ui.dag.MainDagPanel#getPaths(edu.wustl.cab2b.client.ui.dag.ClassNode,
	 *      edu.wustl.cab2b.client.ui.dag.ClassNode)
	 */
	@Override
	protected List<IPath> getPaths(ClassNode sourceNode, ClassNode destNode) {

		Long sourceId = ((IndependentClassNode) sourceNode).getEntityId();
		Long destinationId = ((IndependentClassNode) destNode).getEntityId();

		EntityInterface sourceEntity = ClientSideCache.getInstance().getEntityById(sourceId);
		EntityInterface destinationEntity = ClientSideCache.getInstance().getEntityById(
				destinationId);

		AmbiguityObject ambiguityObject = new AmbiguityObject(sourceEntity, destinationEntity);
		ResolveAmbiguity resolveAmbigity = new ResolveAmbiguity(ambiguityObject, m_pathFinder);
		Map<AmbiguityObject, List<IPath>> map = resolveAmbigity.getPathsForAllAmbiguities();
		return map.get(ambiguityObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.cab2b.client.ui.dag.MainDagPanel#linkNode(edu.wustl.cab2b.client.ui.dag.ClassNode,
	 *      edu.wustl.cab2b.client.ui.dag.ClassNode)
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
		m_document.addComponents(GraphEvent.createSingle(link));

		m_viewController.getHelper().scheduleNodeToLayout(sourceNode);
		m_viewController.getHelper().scheduleNodeToLayout(destNode);
		m_viewController.getHelper().scheduleLinkToLayout(link);
		m_viewController.getHelper().recalculate();

	}

}
