/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.client.ui.visualization.heatmap;

import java.util.HashMap;
import java.util.Observable;

import org.genepattern.clustering.hierarchical.ArrayTreePanel;
import org.genepattern.clustering.hierarchical.GeneTreePanel;
import org.genepattern.data.expr.ExpressionData;
import org.genepattern.data.expr.IExpressionData;
import org.genepattern.data.expr.MetaData;
import org.genepattern.data.matrix.DoubleMatrix2D;
import org.genepattern.data.matrix.ObjectMatrix2D;

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
            selectedColumnNames = dataListDetailedPanel.getSelectedColumnNames().toArray(
                                                                                         new String[dataListDetailedPanel.getSelectedColumnNames().size()]);
            Object[][] tableData = null;
            if (isWholeColumnSelected) {

                BDQTableModel datCubeTableModel = (BDQTableModel) threeDResultObjectDetailsPanel.getThreeDResultTableModel();
                tableData = datCubeTableModel.getColumnValues(threeDResultObjectDetailsPanel.getSelectedColumns());

                selectedRowNames = new String[tableData.length];
                for (int i = 0; i < tableData.length; i++) {
                    selectedRowNames[i] = threeDResultObjectDetailsPanel.getRowHeaderName(i);
                }

                data = new double[tableData.length][selectedColumnNames.length];
                expressionData = generateExpressionData();

                for (int i = 0; i < tableData.length; i++) {
                    for (int j = 0; j < tableData[i].length; j++) {
                        data[i][j] = convertValue(tableData[i][j]);
                    }
                }
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
        HashMap<String, ObjectMatrix2D> name2Matrices = new HashMap<String, ObjectMatrix2D>();
        ExpressionData expressionData = new ExpressionData(matrix, new MetaData(selectedRowNames.length),
                new MetaData(selectedColumnNames.length), name2Matrices);
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
