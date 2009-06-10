package edu.wustl.cab2b.client.ui.dag;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.LIMIT_CONNECT_DESELECTED;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.LIMIT_CONNECT_SELECTED;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.PAPER_GRID_ADD_LIMIT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.PARENTHISIS_ICON_ADD_LIMIT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.PARENTHISIS_ICON_ADD_LIMIT_MOUSEOVER;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.PORT_IMAGE_ADD_LIMIT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SELECT_ICON_ADD_LIMIT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SELECT_ICON_ADD_LIMIT_MOUSEOVER;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import org.netbeans.graph.api.model.GraphEvent;
import org.netbeans.graph.api.model.builtin.GraphDocument;
import org.openide.util.Utilities;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.query.ClientPathFinder;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImages;

public class Cab2bDag extends Cab2bPanel {
	MainDagPanel dagPanel ;
	
	public Cab2bDag() {

		Map<DagImages, Image> imageMap = new HashMap<DagImages, Image>();
		imageMap.put(DagImages.SelectIcon, Utilities.loadImage(SELECT_ICON_ADD_LIMIT));
		imageMap.put(DagImages.selectMOIcon, Utilities.loadImage(SELECT_ICON_ADD_LIMIT_MOUSEOVER));
		imageMap.put(DagImages.ArrowSelectIcon, Utilities.loadImage(LIMIT_CONNECT_DESELECTED));
		imageMap.put(DagImages.ArrowSelectMOIcon, Utilities.loadImage(LIMIT_CONNECT_SELECTED));
		imageMap.put(DagImages.ParenthesisIcon, Utilities.loadImage(PARENTHISIS_ICON_ADD_LIMIT));
		imageMap.put(DagImages.ParenthesisMOIcon, Utilities
				.loadImage(PARENTHISIS_ICON_ADD_LIMIT_MOUSEOVER));
		imageMap.put(DagImages.DocumentPaperIcon, Utilities.loadImage(PAPER_GRID_ADD_LIMIT));
		imageMap.put(DagImages.PortImageIcon, Utilities.loadImage(PORT_IMAGE_ADD_LIMIT));

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
		dagPanel.m_currentNodeList.add(node);
		GraphDocument document = dagPanel.getDocument();
		document.addComponents(GraphEvent.createSingle(node));
		
	}

}
