package edu.wustl.cab2b.client.ui.dag;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.Map;

import org.netbeans.graph.api.model.GraphEvent;
import org.netbeans.graph.api.model.builtin.GraphDocument;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.query.ClientPathFinder;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImages;

public class Cab2bDag extends Cab2bPanel {
    private static final long serialVersionUID = 1L;
    private MainDagPanel dagPanel ;
	
	public Cab2bDag() {

		Map<DagImages, Image> imageMap = CommonUtils.getDagImageMap();

		IPathFinder pathFinder = new ClientPathFinder();
		dagPanel = new CDCDagPanel(null, imageMap, pathFinder, false);
		//dagPanel.m_viewController= new CDCViewController(dagPanel);
		this.setLayout(new BorderLayout());
		this.add(dagPanel);
	}

	public void addNode(EntityInterface entity) {

		IndependentClassNode node = new IndependentClassNode();
		node.setDisplayName(entity.getName());
		node.setID("ID");		
		node.setEntityId(entity.getId());
		dagPanel.currentNodeList.add(node);
		GraphDocument document = dagPanel.getDocument();
		document.addComponents(GraphEvent.createSingle(node));
		
	}

}
