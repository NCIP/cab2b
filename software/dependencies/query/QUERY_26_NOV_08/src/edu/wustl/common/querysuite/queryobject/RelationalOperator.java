/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

import java.util.EnumSet;
import java.util.Set;

/**
 * Enum: Equals LessThan GreaterThan Like In Between
 * 
 * etc....
 * 
 * @version 1.0
 * @updated 11-Oct-2006 02:57:23 PM
 */
public enum RelationalOperator {
    Equals(1, "Equals"), NotEquals(1, "Not Equals"), Between(2, "Between"), NotBetween(2, "Not Between"), IsNull(0,
            "Is Null"), IsNotNull(0, "Is Not Null"), LessThan(1, "Less than"), LessThanOrEquals(1,
            "Less than or Equal to"), GreaterThan(1, "Greater than"), GreaterThanOrEquals(1, "Greater than or Equal to"), In(
            Integer.MAX_VALUE, "In"), Contains(1, "Contains"), StartsWith(1, "Starts With"), EndsWith(1, "Ends With"), NotIn(
            Integer.MAX_VALUE, "Not In");

    public static final Set<RelationalOperator> NUMERIC_OPERATORS;

    public static final Set<RelationalOperator> STRING_OPERATORS;

    static {
        Set<RelationalOperator> stringOnly = EnumSet.of(Contains, StartsWith, EndsWith);
        Set<RelationalOperator> numericOnly = EnumSet.of(LessThan, LessThanOrEquals, GreaterThan, GreaterThanOrEquals,
                Between, NotBetween);
        
        NUMERIC_OPERATORS = EnumSet.allOf(RelationalOperator.class);
        NUMERIC_OPERATORS.removeAll(stringOnly);
        
        STRING_OPERATORS = EnumSet.allOf(RelationalOperator.class);
        STRING_OPERATORS.removeAll(numericOnly);
    }

    private int numberOfValuesRequired;

    /**
     * String Representation of the operator
     */
    private String stringRepresentation;

    /**
     * 
     * @param numberOfValuesRequired numberOfValuesRequired to set.
     * @param stringRepresentation String Representation of the operator
     */
    RelationalOperator(int numberOfValuesRequired, String stringRepresentation) {
        this.numberOfValuesRequired = numberOfValuesRequired;
        this.stringRepresentation = stringRepresentation;
    }

    /**
     * To get the number of values required for the Operator.
     * 
     * @return the number of values required for the Operator.
     */
    public int numberOfValuesRequired() {
        return numberOfValuesRequired;
    }

    /**
     * To get the String representation of the operator.
     * 
     * @param relationalOperator The reference to RelationalOperator
     * @return The SQL String representation of the Operator.
     */
    public static String getSQL(RelationalOperator relationalOperator) {
        String sql = null;
        if (relationalOperator.equals(RelationalOperator.Equals)) {
            sql = "=";
        } else if (relationalOperator.equals(RelationalOperator.NotEquals)) {
            sql = "!=";
        } else if (relationalOperator.equals(RelationalOperator.IsNull)) {
            sql = "is NULL";
        } else if (relationalOperator.equals(RelationalOperator.IsNotNull)) {
            sql = "is NOT NULL";
        } else if (relationalOperator.equals(RelationalOperator.LessThan)) {
            sql = "<";
        } else if (relationalOperator.equals(RelationalOperator.LessThanOrEquals)) {
            sql = "<=";
        } else if (relationalOperator.equals(RelationalOperator.GreaterThan)) {
            sql = ">";
        } else if (relationalOperator.equals(RelationalOperator.GreaterThanOrEquals)) {
            sql = ">=";
        } else if (relationalOperator.equals(RelationalOperator.In)) {
            sql = "in";
        } else if (relationalOperator.equals(RelationalOperator.NotIn)) {
            sql = "NOT in";
        }
        return sql;
    }

    /**
     * @return The String Representation of the enumeration.
     */
    public String getStringRepresentation() {
        return stringRepresentation;
    }

    /**
     * @param stringRepresentation String Representation
     * @return Returns the RelationalOperator which has given string
     *         representation
     */
    public static RelationalOperator getOperatorForStringRepresentation(String stringRepresentation) {
        RelationalOperator relationalOpr = null;
        for (RelationalOperator relationalOperator : RelationalOperator.values()) {
            if (relationalOperator.getStringRepresentation().equals(stringRepresentation)) {
                relationalOpr = relationalOperator;
            }
        }
        return relationalOpr;
    }
}