/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls.LazyTable;


/**
 * This class represents the dimension / size of the page.
 *  
 * @author rahul_ner
 *
 */
public  class PageDimension {

    private int noOfRows;

    private int noOfColumns;

    /**
     * @param noOfRows
     * @param noOfColumns
     */
    public PageDimension(int noOfRows, int noOfColumns) {
        this.noOfRows = noOfRows;
        this.noOfColumns = noOfColumns;
    }
    
    /**
     * @return no of Columns
     */
    public int getNoOfColumns() {
        return noOfColumns;
    }
    
    /**
     * @return no of Rows
     */
    public int getNoOfRows() {
        return noOfRows;
    }
}