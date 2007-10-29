/**
 * 
 */
package edu.wustl.cab2b.client.ui.visualization.heatmap;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.genepattern.clustering.hierarchical.ArrayTreePanel;
import org.genepattern.clustering.hierarchical.GeneTreePanel;
import org.genepattern.clustering.hierarchical.Node;
import org.genepattern.data.expr.ExpressionData;
import org.genepattern.data.expr.IExpressionData;
import org.genepattern.data.expr.MetaData;
import org.genepattern.data.matrix.DoubleMatrix2D;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.viewresults.BDQTableModel;
import edu.wustl.cab2b.client.ui.viewresults.DataListDetailedPanelInterface;
import edu.wustl.cab2b.client.ui.viewresults.ThreeDResultObjectDetailsPanel;
import edu.wustl.common.util.global.Constants;

/**
 * @author chetan_patil
 *
 */
public class HeatMapModel extends Observable {
    private double[][] data;

    private String[] selectedColumnNames;
    
    private String[] selectedRowNames;

    private IExpressionData expressionData;

    private ArrayTreePanel sampleTree;

    private GeneTreePanel geneTreePanel;

    public HeatMapModel() {

    }

    /**
     * @param cab2bTable the cab2bTable to set
     */
    public void setData(DataListDetailedPanelInterface dataListDetailedPanel) {
        if (dataListDetailedPanel instanceof ThreeDResultObjectDetailsPanel) {
            ThreeDResultObjectDetailsPanel threeDResultObjectDetailsPanel = (ThreeDResultObjectDetailsPanel) dataListDetailedPanel;
            boolean isWholeColumnSelected = threeDResultObjectDetailsPanel.getIsWholeColumnSelected();
            if (isWholeColumnSelected) {
                Cab2bTable cab2bTable = dataListDetailedPanel.getDataTable();

                int[] selectedColumnIndices = cab2bTable.getSelectedColumns();
                selectedColumnNames = new String[cab2bTable.getSelectedColumnCount()];
                for (int i = 0; i < cab2bTable.getSelectedColumnCount(); i++) {
                    selectedColumnNames[i] = cab2bTable.getColumnName(selectedColumnIndices[i]);
                }
                
                BDQTableModel datCubeTableModel = (BDQTableModel) cab2bTable.getModel();
                Object[][] tableData = datCubeTableModel.getColumnValues(cab2bTable.getSelectedColumns());
                
                selectedRowNames = new String[tableData.length];
                for (int i = 0; i < tableData.length; i++) {
                    selectedRowNames[i] = threeDResultObjectDetailsPanel.getRowHeaderName(i);
                }
                
                data = new double[tableData.length][cab2bTable.getSelectedColumnCount()];
                for (int i = 0; i < tableData.length; i++) {
                    for (int j = 0; j < tableData[i].length; j++) {
                        data[i][j] = convertValue(tableData[i][j]);
                    }
                }

                //sampleTree = generateTreePanel(0);
                //geneTreePanel = generateGeneTreePanel(0);
                expressionData = generateExpressionData();

                setChanged();
                notifyObservers();
            }
        }
    }

    /**
     * @return the expressionData
     */
    public IExpressionData getExpressionData() {
        return expressionData;
    }

    /**
     * @return the sampleTree
     */
    public ArrayTreePanel getSampleTree() {
        return sampleTree;
    }

    /**
     * @return the geneTreePanel
     */
    public GeneTreePanel getGeneTreePanel() {
        return geneTreePanel;
    }

    private ArrayTreePanel generateTreePanel(int noOfLeafNodes) {
        Map<String, Node> idVsNodeMap = new HashMap<String, Node>();
        Node[] leafNodeArray = new Node[noOfLeafNodes];
        String[] leafNodeIds = new String[noOfLeafNodes];

        for (int i = 0; i < noOfLeafNodes; i++) {
            Node node = new Node(Integer.toString(i));
            node.setIndex(i);
            node.setMinIndex(i);
            node.setMaxIndex(i);
            idVsNodeMap.put(node.getId(), node);

            leafNodeIds[i] = node.getId();
            leafNodeArray[i] = node;
        }
        Node node = leafNodeArray[0];
        for (int i = 1; i < noOfLeafNodes; i++) {
            Node tempNode = new Node("_" + i, node, leafNodeArray[i], i + 1);
            idVsNodeMap.put(tempNode.getId(), tempNode);
            node = tempNode;
        }

        ArrayTreePanel sampleTree = new ArrayTreePanel(idVsNodeMap, node.getId(), leafNodeIds);
        sampleTree.setYMax(node.getCorrelation());
        sampleTree.setYMin(1);

        Dimension dimension = sampleTree.getPreferredSize();
        sampleTree.setPreferredSize(new Dimension(dimension.width, dimension.height + 10));
        return sampleTree;
    }

    /**
     * @param noOfLeafNodes
     * @return
     */
    private GeneTreePanel generateGeneTreePanel(int noOfLeafNodes) {
        Map<String, Node> idVsNodeMap = new HashMap<String, Node>();
        Node[] leafNodeArray = new Node[noOfLeafNodes];
        String[] leafNodeIds = new String[noOfLeafNodes];

        for (int i = 0; i < noOfLeafNodes; i++) {
            Node node = new Node(Integer.toString(i));
            node.setIndex(i);
            node.setMinIndex(i);
            node.setMaxIndex(i);
            idVsNodeMap.put(node.getId(), node);

            leafNodeIds[i] = node.getId();
            leafNodeArray[i] = node;
        }
        Node node = leafNodeArray[0];
        for (int i = 1; i < noOfLeafNodes; i++) {
            Node tempNode = new Node("_" + i, node, leafNodeArray[i], i + 1);
            idVsNodeMap.put(tempNode.getId(), tempNode);
            node = tempNode;
        }
        GeneTreePanel geneTree = new GeneTreePanel(idVsNodeMap, node.getId(), leafNodeIds);
        geneTree.setXMin(node.getCorrelation());
        geneTree.setXMax(1);

        Dimension dimension = geneTree.getPreferredSize();
        geneTree.setPreferredSize(new Dimension(dimension.width + 10, dimension.height));

        return geneTree;
    }

    /**
     * @param cols
     * @param rows
     * @return
     */
    private ExpressionData generateExpressionData() {
        String[] rowNames = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            rowNames[i] = "Row" + i;
        }

        DoubleMatrix2D matrix = new DoubleMatrix2D(data, selectedRowNames, selectedColumnNames);
        HashMap name2Matrices = new HashMap();
        ExpressionData expressionData = new ExpressionData(matrix, new MetaData(selectedRowNames.length), new MetaData(
                selectedColumnNames.length), name2Matrices);
        return expressionData;
    }

    /**
     * To check equality of the two object.
     * @param obj to be check for equality.
     * @return true if objects are equals.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;

        if (this == object) {
            isEqual = true;
        } else if (object != null && this.getClass() == object.getClass()) {
            HeatMapModel heatMapDataModel = (HeatMapModel) object;
            IExpressionData expressionData = heatMapDataModel.getExpressionData();
            ArrayTreePanel sampleTree = heatMapDataModel.getSampleTree();
            GeneTreePanel geneTreePanel = heatMapDataModel.getGeneTreePanel();

            if (this.expressionData.equals(expressionData) && this.sampleTree.equals(sampleTree)
                    && geneTreePanel.equals(geneTreePanel)) {
                isEqual = true;
            }
        }

        return isEqual;
    }

    /**
     * To get the HashCode for the object.
     * @return The hash code value for the object.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 1;
        if (expressionData != null && sampleTree != null && geneTreePanel != null) {
            hash *= Constants.HASH_PRIME + expressionData.hashCode();
            hash *= Constants.HASH_PRIME + sampleTree.hashCode();
            hash *= Constants.HASH_PRIME + geneTreePanel.hashCode();
        }
        return hash;
    }

    /**
     * This method converts the value passed to the chart into a valid number. If value is not a valid number it return 0.
     * @param value
     * @return
     */
    private double convertValue(Object value) {
        double output = 0;
        if (value == null) {
            return output;
        }
        if (Number.class.isAssignableFrom(value.getClass())) {
            output = ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            try {
                output = Double.valueOf((String) value);
            } catch (Exception exception) {
            }
        }
        return output;
    }

}
