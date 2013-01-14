/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls.LazyTable;

/**
 * Represents meta data about the page. Provide inofrmation like starting x and Y co-ordiante
 * of a page in a whole data.
 * 
 * 
 * @author Rahul Ner
 *
 */
public class PageInfo {

    /**
     * starting x co-ordiante of a page in a whole data.
     */
    private int startX;

    /**
     * starting y co-ordiante of a page in a whole data.
     */
    private int startY;

    /**
     * @param startX
     * @param startY
     */
    public PageInfo(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
    }

    /**
     * @param other
     * @return boolean value
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (other == null || !(other instanceof PageInfo)) {
            return false;
        }
        PageInfo otherPageInfo = (PageInfo) other;

        if (this.startX == otherPageInfo.startX && this.startY == otherPageInfo.startY) {
            return true;
        }

        return false;
    }
    
    /**
     * @return
     */
    public int getStartX() {
        return startX;
    }
    
    /**
     * @return
     */
    public int getStartY() {
        return startY;
    }
    
    /**
     * @return string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" +  startX + " , " + startY + "]";
    }
}