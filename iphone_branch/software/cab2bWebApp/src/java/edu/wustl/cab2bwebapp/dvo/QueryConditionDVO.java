/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.dvo;

/**
 * @author chetan_pundhir
 *
 */
public class QueryConditionDVO {
    private String parameter = null;

    private String condition = null;

    private String value = null;

    /**
     * @return String
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * @param parameter
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * @return String
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @param condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
}