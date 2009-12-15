/**
 * 
 */
package edu.wustl.cab2b.client.ui.visualization.charts;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bRadioButton;
import edu.wustl.cab2b.common.util.Constants.ChartOrientation;

/**
 * This class is the panel of the chart and provides a method to set the type of chart in this panel
 * @author chetan_patil
 */
public class Cab2bChartPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    private ChartModel chartModel = null;

    private AbstractChart barChart = null;

    private AbstractChart lineChart = null;

    private AbstractChart scatterPlot = null;

    private JPanel chart = null;

    /**
     * Parameterized constructor
     * @param cab2bTable
     */
    public Cab2bChartPanel() {
        setName("cab2bChartPanel");
        setBorder(null);
        removeAll();
        add("left ", createRadioPanel());
    }

    public void setModel(ChartModel chartModel) {
        this.chartModel = chartModel;
    }

    public void drawChart(final String chartType) {
        chartModel.deleteObservers();
        if (chartType.equals("Bar Chart") && barChart == null) {
            barChart = new BarChart();
            chartModel.addObserver(barChart);
        } else if (chartType.equals("Line Chart") && lineChart == null) {
            lineChart = new LineChart();
            chartModel.addObserver(lineChart);
        } else if (chartType.equals("Scatter Plot") && scatterPlot == null) {
            scatterPlot = new ScatterPlot();
            chartModel.addObserver(scatterPlot);
        }

        chartModel.setChartType(chartType);
        setChartPanel();
    }

    private void setChartPanel() {

        if (chart != null) {
            this.remove(chart);
        }

        String chartType = chartModel.getChartType();
        chartModel.notifyObservers();
        if (chartType.equals("Bar Chart")) {
            chart = barChart.getChartPanel();
        } else if (chartType.equals("Line Chart")) {
            chart = lineChart.getChartPanel();
        } else if (chartType.equals("Scatter Plot")) {
            chart = scatterPlot.getChartPanel();
        }

        if (chart != null) {
            this.add("br hfill vfill ", chart);
        } else {
            throw new IllegalStateException("Unable to create chart component for charttype =" + chartType
                    + " Chart orientation =" + chartModel.getChartOrientation());
        }

        updateUI();
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
                chartModel.setChartOrientation(ChartOrientation.ROW_AS_CATEGORY);

            } else {
                chartModel.setChartOrientation(ChartOrientation.COLUMN_AS_CATEGORY);

            }
            setChartPanel();
        }
    };

}
