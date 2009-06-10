package edu.wustl.cab2b.client.ui.visualization.charts;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

/**
 * This class generates the Bar Chart
 * @author chetan_patil
 */
public class BarChart extends AbstractChart {
    private static final long serialVersionUID = 1L;

    /**
     * The name of the entity for which the chart is being generated
     */
    private String entityName;

    /**
     * Parameterized constructor
     * @param chartRawData
     * @param entityName
     */
    public BarChart(String entityName) {
        this.entityName = entityName;
    }

    protected Dataset createColumnWiseData() {
        int[] selectedRowIndices = chartModel.getSelectedRowIndices();
        int[] selectedColumnsIndices = chartModel.getSelectedColumnsIndices();
        String[] series = getColumnIndicesSeries(selectedColumnsIndices);

        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();

        for (int i = 0; i < selectedRowIndices.length; i++) {
            for (int j = 0; j < selectedColumnsIndices.length; j++) {
                Object value = null;
                if (chartModel.isWholeColumnSelected()) {
                    value = chartModel.getCab2bTableData()[i][j];
                } else {
                    value = chartModel.getCab2bTable().getValueAt(selectedRowIndices[i], selectedColumnsIndices[j]);
                }
                defaultcategorydataset.addValue(convertValue(value), series[j], (selectedRowIndices[i] + 1));
            }
        }

        return defaultcategorydataset;
    }
    
    protected Dataset createRowWiseData() {
        int[] selectedRowIndices = chartModel.getSelectedRowIndices();
        int[] selectedColumnsIndices = chartModel.getSelectedColumnsIndices();
        String[] categories = getColumnIndicesSeries(selectedColumnsIndices);

        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();

        for (int i = 0; i < selectedColumnsIndices.length; i++) {
            for (int j = 0; j < selectedRowIndices.length; j++) {
                Object value = null;
                if (chartModel.isWholeColumnSelected()) {
                    value = chartModel.getCab2bTableData()[j][i];
                } else {
                    value = chartModel.getCab2bTable().getValueAt(selectedRowIndices[j], selectedColumnsIndices[i]);
                }
                defaultcategorydataset.addValue(convertValue(value), (selectedRowIndices[j] + 1), categories[i]);
            }
        }

        return defaultcategorydataset;
    }

    private String[] getColumnIndicesSeries(int[] selectedColumnsIndices) {
        String[] series = new String[selectedColumnsIndices.length];
        for (int i = 0; i < selectedColumnsIndices.length; i++) {
            series[i] = chartModel.getCab2bTable().getColumnName(selectedColumnsIndices[i]);
        }
        return series;
    }

    /*
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.charts.AbstractChart#createChart(org.jfree.data.general.Dataset)
     */
    protected JFreeChart createChart(Dataset dataset) {
        CategoryDataset categoryDataSet = (CategoryDataset) dataset;

        JFreeChart jFreeChart = ChartFactory.createBarChart("Bar Chart", entityName, "Value", categoryDataSet,
                                                            PlotOrientation.VERTICAL, true, true, false);
        jFreeChart.setBackgroundPaint(Color.white);

        CategoryPlot categoryPlot = (CategoryPlot) jFreeChart.getPlot();
        categoryPlot.setBackgroundPaint(Color.white);
        categoryPlot.setDomainGridlinePaint(Color.white);
        categoryPlot.setRangeGridlinePaint(Color.white);

        NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();
        barRenderer.setDrawBarOutline(false);
        barRenderer.setMinimumBarLength(0.7D);
        barRenderer.setMaximumBarWidth(0.7D);
        barRenderer.setItemMargin(0.1D);

        CategoryAxis categoryaxis = categoryPlot.getDomainAxis();
        categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        return jFreeChart;
    }

    /**
     * This class is used for the generation of the labels to be displayed against each group on x-axis. 
     * @author chetan_patil
     */
    static class LabelGenerator extends StandardCategoryItemLabelGenerator {
        private static final long serialVersionUID = 1L;

        LabelGenerator() {
        }

        public String generateLabel(CategoryDataset categoryDataSet, int i, int j) {
            return categoryDataSet.getRowKey(i).toString();
        }
    }

}