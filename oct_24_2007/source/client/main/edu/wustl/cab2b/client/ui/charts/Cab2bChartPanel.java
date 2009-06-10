/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bRadioButton;
import edu.wustl.cab2b.client.ui.viewresults.DataListDetailedPanelInterface;
import edu.wustl.cab2b.common.util.Constants.ChartOrientation;

/**
 * This class is the panel of the chart and provides a method to set the type of chart in this panel
 * @author chetan_patil
 */
public class Cab2bChartPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    private Cab2bChartRawData chartRawData;

    private ChartType chartType;

    private String entityName;

    /**
     * Parameterized contructor
     * @param cab2bTable
     */
    public Cab2bChartPanel(DataListDetailedPanelInterface dataListDetailedPanel) {
        this.chartRawData = new Cab2bChartRawData(dataListDetailedPanel, ChartOrientation.COLUMN_AS_CATEGORY);
        this.setName("cab2bChartPanel");
        this.setBorder(null);
    }

    public void setChartType(final ChartType chartType, final String entityName) {
        this.chartType = chartType;
        this.entityName = entityName;

        JPanel jPanel = getChartPanel();

        this.removeAll();
        this.add("left ", createRadioPanel());
        this.add("br vfill hfill ", jPanel);
    }

    private void setChartType() {
        this.remove(1);
        this.add("br vfill hfill ", getChartPanel());
        updateUI();
    }

    public void setChartType(final ChartType chartType) {
        this.remove(1);
        this.add("br vfill hfill ", getChartPanel(chartType));
        updateUI();
    }

    private JPanel getChartPanel() {
        JPanel jPanel = null;
        switch (chartType) {
            case BAR_CHART:
                jPanel = getBarChart(entityName);
                break;
            case LINE_CHART:
                jPanel = getLineChart();
                break;
            case SCATTER_PLOT:
                jPanel = getScatterPlot();
        }
        return jPanel;
    }

    private JPanel getChartPanel(final ChartType chartType) {
        this.chartType = chartType;
        return getChartPanel();
    }

    private Cab2bPanel createRadioPanel() {
        Cab2bPanel radioPanel = new Cab2bPanel(new FlowLayout());
        radioPanel.add(new Cab2bLabel("Series in: "));
        ButtonGroup group = new ButtonGroup();

        RadioButtonListener radioButtonListener = new RadioButtonListener();

        JRadioButton columnButton = new Cab2bRadioButton("Column");
        columnButton.setActionCommand("Column");
        columnButton.addActionListener(radioButtonListener);
        group.add(columnButton);
        radioPanel.add(columnButton);

        JRadioButton rowButton = new Cab2bRadioButton("Row");
        rowButton.setActionCommand("Row");
        rowButton.addActionListener(radioButtonListener);
        group.add(rowButton);
        radioPanel.add(rowButton);

        columnButton.setSelected(true);
        group.setSelected(columnButton.getModel(), true);

        return radioPanel;
    }

    /**
     * This method returns the JPanel consisting of generated Bar Chart
     * @param entityName label to be displayed for x-axis 
     * @return JPanel consisting of Bar Chart
     */
    private JPanel getBarChart(String entityName) {
        AbstractChart barChart = new BarChart(chartRawData, entityName);
        JPanel jPanel = barChart.createChartPanel();
        return jPanel;
    }

    /**
     * This method returns the JPanel consisting of generated Line Chart
     * @return JPanel consisting of Line Chart
     */
    private JPanel getLineChart() {
        AbstractChart lineChart = new LineChart(chartRawData);
        JPanel jPanel = lineChart.createChartPanel();
        return jPanel;
    }

    /**
     * This method returns the JPanel consisting of generated Scatter Plot
     * @return JPanel consisting of Scatter Plot
     */
    private JPanel getScatterPlot() {
        AbstractChart scatterPlot = new ScatterPlot(chartRawData);
        JPanel jPanel = scatterPlot.createChartPanel();
        return jPanel;
    }

    private class RadioButtonListener implements ActionListener {

        /**
         * @param tabComponent
         * @param newVisualizeDataPanel
         * @param entityName
         * @param chartType
         */
        public RadioButtonListener() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            String actionCommand = actionEvent.getActionCommand();
            if (actionCommand != null && actionCommand == "Row") {
                chartRawData.setChartOrientation(ChartOrientation.ROW_AS_CATEGORY);
            } else {
                chartRawData.setChartOrientation(ChartOrientation.COLUMN_AS_CATEGORY);
            }

            setChartType();
        }

    };

}
