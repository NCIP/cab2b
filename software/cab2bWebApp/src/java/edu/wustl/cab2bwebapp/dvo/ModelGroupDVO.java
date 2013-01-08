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
public class ModelGroupDVO {
    String modelGroupName = null;

    boolean secured = false;

    boolean selected = false;

    /**
     * @return String
     */
    public String getModelGroupName() {
        return modelGroupName;
    }

    /**
     * @param modelGroupName
     */
    public void setModelGroupName(String modelGroupName) {
        this.modelGroupName = modelGroupName;
    }

    /**
     * @return boolean
     */
    public boolean getSecured() {
        return secured;
    }

    /**
     * @param secured
     */
    public void setSecured(boolean secured) {
        this.secured = secured;
    }

    /**
     * @return boolean
     */
    public boolean getSelected() {
        return selected;
    }

    /**
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
