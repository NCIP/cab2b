/**
 * This Enumeration defines the type of Charts
 */
package edu.wustl.cab2b.client.ui.charts;

public enum ChartType {
    BAR_CHART("Bar Chart"), LINE_CHART("Line Chart"), SCATTER_PLOT("Scatter Plot");

    private String actionCommand;

    /**
     * Parameterized constructor
     * @param actionCommmad the action command to be set
     */
    ChartType(String actionCommmad) {
        this.actionCommand = actionCommmad;
    }

    /**
     * This method returns the action command for this Chart type
     * @return
     */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
     * Returns the corresponding enumration for the given action Command.
     * @param actionCommand
     * @return the corresponding enumration for the given action Command
     */
    public static ChartType getChartType(final String actionCommand) {
        final ChartType[] allDataypes = ChartType.values();

        ChartType requiredChartType = null;
        for (ChartType dataType : allDataypes) {
            if (dataType.actionCommand.equals(actionCommand)) {
                requiredChartType = dataType;
            }
        }

        if (requiredChartType == null) {
            throw new RuntimeException("Unknown chart type found : " + actionCommand);
        }
        return requiredChartType;
    }

}
