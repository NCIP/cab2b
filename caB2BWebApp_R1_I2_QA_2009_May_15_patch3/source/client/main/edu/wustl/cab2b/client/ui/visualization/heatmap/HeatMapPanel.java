/**
 * 
 */
package edu.wustl.cab2b.client.ui.visualization.heatmap;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import org.genepattern.clustering.hierarchical.ArrayTreePanel;
import org.genepattern.clustering.hierarchical.GeneTreePanel;
import org.genepattern.data.expr.IExpressionData;
import org.genepattern.heatmap.HeatMapComponent;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;

/**
 * @author chetan_patil
 *
 */
public class HeatMapPanel extends Cab2bPanel implements Observer {
    private static final long serialVersionUID = 1L;

    private HeatMapModel heatMapDataModel;

    public HeatMapPanel() {
        this.setName("heatMapPanel");
        this.setLayout(new BorderLayout());
    }

    public void update(Observable observable, Object object) {
        heatMapDataModel = (HeatMapModel) observable;

        IExpressionData expressionData = heatMapDataModel.getExpressionData();
        ArrayTreePanel sampleTree = heatMapDataModel.getSampleTree();
        GeneTreePanel geneTreePanel = heatMapDataModel.getGeneTreePanel();

        String title = "# # Heat Map # #";
        JFrame frame = new JFrame(title);
        Component accessoryPanel = null;
        boolean showGeneCruiser = true;

        HeatMapComponent heatMapComponent = new HeatMapComponent(frame, expressionData, geneTreePanel, sampleTree,
                accessoryPanel, showGeneCruiser);

        this.add(heatMapComponent, BorderLayout.CENTER);
        this.add(heatMapComponent.createMenuBar(true),BorderLayout.NORTH);
    }

}
