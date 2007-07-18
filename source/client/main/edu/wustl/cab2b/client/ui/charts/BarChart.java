package edu.wustl.cab2b.client.ui.charts;

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

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.common.util.Constants.ChartOrientation;

/**
 * This class generates the Bar Chart
 * @author chetan_patil
 */
public class BarChart extends AbstractChart {
    /**
     * 
     */
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
    public BarChart(Cab2bChartRawData chartRawData, String entityName) {
        super(chartRawData);
        this.entityName = entityName;
    }

    /*
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.charts.AbstractChart#createDataset()
     */
    protected CategoryDataset createDataset() {
        Cab2bTable cab2bTable = chartRawData.getCab2bTable();
        int[] selectedRowIndices = chartRawData.getSelectedRowIndices();
        int[] selectedColumnsIndices = chartRawData.getSelectedColumnsIndices();
        ChartOrientation chartOrientation = chartRawData.getChartOrientation();

        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        int i, j;

        if (chartOrientation == ChartOrientation.COLUMN_AS_CATEGORY) {
            String[] series = new String[selectedColumnsIndices.length];
            String[] categories = new String[selectedRowIndices.length];

            for (i = 0; i < selectedRowIndices.length; i++) {
                categories[i] = "Row" + (selectedRowIndices[i] + 1);
            }

            for (j = 0; j < selectedColumnsIndices.length; j++) {
                series[j] = cab2bTable.getColumnName(selectedColumnsIndices[j]);
            }

            for (i = 0; i < selectedRowIndices.length; i++) {
                for (j = 0; j < selectedColumnsIndices.length; j++) {
                    String value = (String) cab2bTable.getValueAt(selectedRowIndices[i], selectedColumnsIndices[j]);

                    Double doubleValue = null;
                    try {
                        doubleValue = Double.valueOf(value);
                    } catch (Exception exception) {
                        doubleValue = 0D;
                    }

                    defaultcategorydataset.addValue(doubleValue, series[j], categories[i]);
                }
            }
        } else {
            String[] series = new String[selectedRowIndices.length];
            String[] categories = new String[selectedColumnsIndices.length];

            for (i = 0; i < selectedColumnsIndices.length; i++) {
                categories[i] = cab2bTable.getColumnName(selectedColumnsIndices[i]);
            }

            for (j = 0; j < selectedRowIndices.length; j++) {
                series[j] = "Row" + (selectedRowIndices[j] + 1);
            }

            for (i = 0; i < selectedColumnsIndices.length; i++) {
                for (j = 0; j < selectedRowIndices.length; j++) {
                    String value = (String) cab2bTable.getValueAt(selectedRowIndices[j], selectedColumnsIndices[i]);
                    Double yAxisValue = null;
                    try {
                        yAxisValue = Double.valueOf(value);
                    } catch (Exception exception) {
                        yAxisValue = 0D;
                    }

                    defaultcategorydataset.addValue(yAxisValue, series[j], categories[i]);
                }
            }
        }

        return defaultcategorydataset;
    }

    /*
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.charts.AbstractChart#createChart(org.jfree.data.general.Dataset)
     */
    protected JFreeChart createChart(Dataset dataset) {
        CategoryDataset categoryDataSet = (CategoryDataset) dataset;

        JFreeChart jFreeChart = ChartFactory.createBarChart(ChartType.BAR_CHART.getType(), entityName,
                                                            "Value", categoryDataSet, PlotOrientation.VERTICAL,
                                                            true, true, false);
        jFreeChart.setBackgroundPaint(Color.white);

        CategoryPlot categoryPlot = (CategoryPlot) jFreeChart.getPlot();
        categoryPlot.setBackgroundPaint(Color.white);
        categoryPlot.setDomainGridlinePaint(Color.white);
        categoryPlot.setRangeGridlinePaint(Color.white);

        NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();
        barRenderer.setDrawBarOutline(false);
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