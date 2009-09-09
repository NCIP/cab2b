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