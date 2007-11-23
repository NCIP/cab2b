/*
 * TableColumnNamesComparator.java
 *
 * Created on October 23, 2007, 11:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.wustl.cab2b.client.ui.controls.sheet;

import java.util.Comparator;
import javax.swing.table.TableColumn;

/**
     *  Comparator to show column names insorted order in display...
*
 * @author jasbir_sachdeva
 */
class TableColumnNamesComparator implements Comparator<TableColumn>{
    
    /** Creates a new instance of TableColumnNamesComparator */
    public TableColumnNamesComparator() {
    }
    
    public int compare(TableColumn o1, TableColumn o2) {
        String s1 = (String)o1.getHeaderValue();
        String s2 = (String)o2.getHeaderValue();
//            String s1 = (String)o1.getIdentifier();
//            String s2 = (String)o2.getIdentifier();
        return s1.compareToIgnoreCase( s2);
    }
    
}
