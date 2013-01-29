/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.entitymanager;

/**
 * This class provides the methods that builds the queries that are specific to ORACLE database 
 * 
 * @author Rahul Ner
 */
public class DynamicExtensionOracleQueryBuilder extends DynamicExtensionBaseQueryBuilder {

    /**
     * @see edu.common.dynamicextensions.entitymanager.DynamicExtensionBaseQueryBuilder#getEscapedStringValue(java.lang.String)
     */
    protected String getEscapedStringValue(String value) {
        return value.replaceAll("'", "''");
    }
}
