/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.mainframe;
/**
 * Enumeration for status on main frame
 * @author chandrakant_talele
 */
public enum Status {
    READY("Ready"), BUSY("Busy");
    private String textToShow;

    Status(String textToShow) {
        this.textToShow = textToShow;
    }

    /**
     * @return Text String
     */
    public String getTextToShow() {
        return textToShow;
    }
};