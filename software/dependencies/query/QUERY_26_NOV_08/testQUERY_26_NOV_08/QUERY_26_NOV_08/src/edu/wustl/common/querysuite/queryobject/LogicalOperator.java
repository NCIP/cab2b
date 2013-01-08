/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

/**
 * Enumeration of logical operators. Unknown is the default.
 * 
 * @version 1.0
 * @updated 11-Oct-2006 02:57:20 PM
 * @see edu.wustl.common.querysuite.queryobject.ILogicalConnector
 */
public enum LogicalOperator implements IBinaryOperator {
    And("AND"), Or("OR"), Unknown("UNKNOWN");

    private String operator;

    /**
     * Parameterized constructor
     * 
     * @param type the action command to be set
     */
    LogicalOperator(String operator) {
        this.operator = operator;
    }

    /**
     * This method returns the action command for this Chart type
     * 
     * @return
     */
    public String getOperatorString() {
        return operator;
    }

    /**
     * Returns the corresponding enumration for the given action Command.
     * 
     * @param type
     * @return the corresponding enumration for the given action Command
     */
    public static LogicalOperator getLogicalOperator(final String operator) {
        final LogicalOperator[] logicalOperators = LogicalOperator.values();

        LogicalOperator requiredOperator = null;
        for (LogicalOperator logicalOperator : logicalOperators) {
            if (logicalOperator.operator.equals(operator)) {
                requiredOperator = logicalOperator;
            }
        }

        if (requiredOperator == null) {
            throw new RuntimeException("Unknown chart type found : " + operator);
        }
        return requiredOperator;
    }
}