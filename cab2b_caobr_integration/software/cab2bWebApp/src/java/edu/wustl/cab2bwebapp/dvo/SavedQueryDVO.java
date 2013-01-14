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
public class SavedQueryDVO {
    String name = null;

    boolean selected = false;

    int resultCount = 0;

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        if (name.lastIndexOf("#") == name.length() - 1)
            name = name.substring(0, name.length() - 1);
        this.name = name;
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

    /**
     * @return int
     */
    public int getResultCount() {
        return resultCount;
    }

    /**
     * @param resultCount
     */
    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }
}