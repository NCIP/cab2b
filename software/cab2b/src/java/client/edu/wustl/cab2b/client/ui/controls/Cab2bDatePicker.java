/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;

import com.toedter.calendar.JDateChooser;

/**
 * A Cab2b component similar to date chooser containig a date spinner 
 * and a button, that makes a JCalendar visible for choosing a date.
 * @author deepak_shingan
 *
 */
public class Cab2bDatePicker extends JDateChooser {
    private static final long serialVersionUID = 1L;

    /**
     * Default dimension
     */
    final Dimension dim = new Dimension(100, 20);

    /**
     * Creates a new Cab2bDatePicker object with default size
     */
    public Cab2bDatePicker() {
        super(null, "yyyy/MM/dd");
        setPreferredSize(dim);
    }
}
