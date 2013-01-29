/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

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

/**
 * Panel for generating DAG 
 * @author deepak_shingan
 *
 */
public class Cab2bDag extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /**
     * Main dag panel referance 
     */
    private MainDagPanel dagPanel;

    /**
     *  Constructor
     *  Sets ImageIcons and components in DAG panel
     */
    public Cab2bDag() {

        Map<DagImages, Image> imageMap = CommonUtils.getDagImageMap();

        IPathFinder pathFinder = new ClientPathFinder();
        dagPanel = new CDCDagPanel(null, imageMap, pathFinder, false);
        // dagPanel.m_viewController= new CDCViewController(dagPanel);
        this.setLayout(new BorderLayout());
        this.add(dagPanel);
    }

    /**
     * Method to add nodes in dag panel for a given EntityInterface
     * @param entity EntityInterface
     */
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
