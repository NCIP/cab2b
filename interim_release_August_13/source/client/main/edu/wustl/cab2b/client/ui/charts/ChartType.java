/**
 * This Enumeration defines the type of Charts
 */
package edu.wustl.cab2b.client.ui.charts;

public enum ChartType {
    BAR_CHART("Bar Chart"), LINE_CHART("Line Chart"), SCATTER_PLOT("Scatter Plot");

    private String type;

    /**
     * Parameterized constructor
     * @param type the action command to be set
     */
    ChartType(String type) {
        this.type = type;
    }

    /**
     * This method returns the action command for this Chart type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the corresponding enumration for the given action Command.
     * @param type
     * @return the corresponding enumration for the given action Command
     */
    public static ChartType getChartType(final String type) {
        final ChartType[] allDataypes = ChartType.values();

        ChartType requiredChartType = null;
        for (ChartType dataType : allDataypes) {
            if (dataType.type.equals(type)) {
                requiredChartType = dataType;
            }
        }

        if (requiredChartType == null) {
            throw new RuntimeException("Unknown chart type found : " + type);
        }
        return requiredChartType;
    }

}
