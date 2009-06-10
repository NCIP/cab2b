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