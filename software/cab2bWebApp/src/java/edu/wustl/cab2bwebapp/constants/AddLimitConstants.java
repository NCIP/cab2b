/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.constants;

/**
 * Constants for Add Limit
 * @author Chetan Pundhir
 */
public interface AddLimitConstants {
    /** Constant for DATE */
    public static final String DATE = "date";

    /** Constant for IN */
    public static final String IN = "In";

    /** Constant for NOT_IN */
    public static final String NOT_IN = "Not In";

    /** Constant for IS_NOT_NULL */
    public static final String IS_NOT_NULL = "is not null";

    /** Constant for IS_NULL */
    public static final String IS_NULL = "is null";

    /** Constant for BETWEEN */
    public static final String BETWEEN = "Between";

    /** Constant for CONTAINS */
    public static final String CONTAINS = "Contains";

    /** Constant for STRATS_WITH */
    public static final String STRATS_WITH = "Starts With";

    /** Constant for ENDS_WITH */
    public static final String ENDS_WITH = "Ends With";

    /** Constant for NOT_BETWEEN */
    public static final String NOT_BETWEEN = "Not Between";

    /** Constant for DATE_FORMAT */
    //public static final String DATE_FORMAT = "MM-dd-yyyy";
    public static final String DATE_FORMAT = "yyyy/mm/dd";


    /** Constant for FILE_TYPE */
    public static final String FILE_TYPE = "file";

    /** Constant for ID */
    public static final String ID = "id";

    /** Constant for MISSING_TWO_VALUES */
    public static final String MISSING_TWO_VALUES = "missingTwoValues";

    /** Constant for ARGUMENT_ZERO */    
    public static final int ARGUMENT_ZERO = 0;

    /** Constant for INDEX_PARAM_ZERO */     
    public static final int INDEX_PARAM_ZERO = 0;

    /** Constant for INDEX_PARAM_TWO */     
    public static final int INDEX_PARAM_TWO = 2;

    /** Constant for INDEX_LENGTH */     
    public static final int INDEX_LENGTH = 3;

    /** Constant for BIG_INT */     
    public static final String BIG_INT = "bigint";

    /** Constant for INTEGER */     
    public static final String INTEGER = "integer";

    /** Constant for LONG */     
    public static final String LONG = "Long";

    /** Constant for DOUBLE */     
    public static final String DOUBLE = "double";

    /** Constant for TINY_INT */     
    public static final String TINY_INT = "tinyint";

    /** Constant for UNDERSCORE */     
    public static final String UNDERSCORE = "_";

    /** Constant for BOOLEAN_NO */     
    public static final String BOOLEAN_NO = "No";

    /** Constant for BOOLEAN_YES */     
    public static final String BOOLEAN_YES = "Yes";

    /** Constant for ENTITY_SEPARATOR */     
    public static final String ENTITY_SEPARATOR = ";";

    /** Constant for QUERY_VALUES_DELIMITER */     
    public static final String QUERY_VALUES_DELIMITER = "&";

    /** Constant for QUERY_OPERATOR_DELIMITER */     
    public static final String QUERY_OPERATOR_DELIMITER = "!*=*!";

    /** Constant for QUERY_CONDITION_DELIMITER */     
    public static final String QUERY_CONDITION_DELIMITER = "@#condition#@";
}
