package edu.wustl.cab2bwebapp.dvo;


/**
 * @author chetan_pundhir
 *
 */
public class SearchResultDVO {
    String title = null;

    String value = null;

    String media = null;

    /**
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param columnTitle
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * @param columnValue
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return String
     */
    public String getMedia() {
        return media;
    }

    /**
     * @param media
     */
    public void setMedia(String media) {
        this.media = media;
    }
}