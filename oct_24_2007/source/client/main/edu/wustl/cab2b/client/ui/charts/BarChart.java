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
        int[] selectedRowIndices = chartRawData.getSelectedRowIndices();
        int[] selectedColumnsIndices = chartRawData.getSelectedColumnsIndices();
        ChartOrientation chartOrientation = chartRawData.getChartOrientation();

        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();

        if (chartOrientation == ChartOrientation.COLUMN_AS_CATEGORY) {
            String[] series = getColumnIndicesSeries(selectedColumnsIndices);
            String[] categories = getRowIndicesSeries(selectedRowIndices);

            if (chartRawData.isWholeColumnSelected())
                defaultcategorydataset = addColumnValueToDataSet(selectedRowIndices, selectedColumnsIndices,
                                                                 series, categories);
            else
                defaultcategorydataset = addTableValueToDataSet(selectedRowIndices, selectedColumnsIndices,
                                                                series, categories);

        } else {
            String[] series = getRowIndicesSeries(selectedRowIndices);
            String[] categories = getColumnIndicesSeries(selectedColumnsIndices);

            if (chartRawData.isWholeColumnSelected())
                defaultcategorydataset = addColumnValueToDataSet(selectedRowIndices, selectedColumnsIndices,
                                                                 series, categories);
            else
                defaultcategorydataset = addTableValueToDataSet(selectedRowIndices, selectedColumnsIndices,
                                                                series, categories);

        }
        return defaultcategorydataset;
    }

    public String[] getColumnIndicesSeries(int[] selectedColumnsIndices) {
        String[] series = new String[selectedColumnsIndices.length];
        for (int i = 0; i < selectedColumnsIndices.length; i++) {
            series[i] = chartRawData.getCab2bTable().getColumnName(selectedColumnsIndices[i]);
        }
        return series;
    }

    public String[] getRowIndicesSeries(int[] selectedRowIndices) {
        String[] series = new String[selectedRowIndices.length];
        for (int j = 0; j < selectedRowIndices.length; j++) {
            series[j] = "Row" + (selectedRowIndices[j] + 1);
        }
        return series;
    }

    public DefaultCategoryDataset addColumnValueToDataSet(int[] selectedRowIndices, int[] selectedColumnsIndices,
                                                          String[] series, String[] categories) {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        int i;
        int j;
        if (chartRawData.getChartOrientation() == ChartOrientation.COLUMN_AS_CATEGORY) {
            for (i = 0; i < selectedRowIndices.length; i++) {
                for (j = 0; j < selectedColumnsIndices.length; j++) {
                    Object value =  chartRawData.getCab2bTableData()[i][j];
                    defaultcategorydataset.addValue(convertValue(value), series[j], categories[i]);
                }
            }
        } else {
            for (i = 0; i < selectedColumnsIndices.length; i++) {
                for (j = 0; j < selectedRowIndices.length; j++) {

                    Object value =  chartRawData.getCab2bTableData()[j][i];
                    defaultcategorydataset.addValue(convertValue(value), series[j], categories[i]);
                }
            }
        }
        return defaultcategorydataset;
    }

    public DefaultCategoryDataset addTableValueToDataSet(int[] selectedRowIndices, int[] selectedColumnsIndices,
                                                         String[] series, String[] categories) {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        int i;
        int j;
        if (chartRawData.getChartOrientation() == ChartOrientation.COLUMN_AS_CATEGORY) {
            for (i = 0; i < selectedRowIndices.length; i++) {
                for (j = 0; j < selectedColumnsIndices.length; j++) {
                    Object value =  chartRawData.getCab2bTable().getValueAt(selectedRowIndices[i],
                                                                                    selectedColumnsIndices[j]);
                    defaultcategorydataset.addValue(convertValue(value), series[j], categories[i]);
                }
            }
        } else {
            for (i = 0; i < selectedColumnsIndices.length; i++) {
                for (j = 0; j < selectedRowIndices.length; j++) {

                    Object value =  chartRawData.getCab2bTable().getValueAt(selectedRowIndices[j],
                                                                                    selectedColumnsIndices[i]);
                    defaultcategorydataset.addValue(convertValue(value), series[j], categories[i]);
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

        JFreeChart jFreeChart = ChartFactory.createBarChart(ChartType.BAR_CHART.getType(), entityName, "Value",
                                                            categoryDataSet, PlotOrientation.VERTICAL, true, true,
                                                            false);
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