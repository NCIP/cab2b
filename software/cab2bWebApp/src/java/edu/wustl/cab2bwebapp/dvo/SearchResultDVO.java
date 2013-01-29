/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.dvo;


/**
 * @author chetan_pundhir
 *
 */
public class SearchResultDVO {
    String title = null;

    Object value = null;

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
    public Object getValue() {
        return value;
    }

    /**
     * @param columnValue
     */
    public void setValue(Object value) {
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