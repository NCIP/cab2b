/*L
 * Copyright Georgetown University.
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

    public PageDimension(int noOfRows, int noOfColumns) {
        this.noOfRows = noOfRows;
        this.noOfColumns = noOfColumns;
    }
    
    public int getNoOfColumns() {
        return noOfColumns;
    }
    
    public int getNoOfRows() {
        return noOfRows;
    }
}