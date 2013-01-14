/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * A cab2b component extended from JComboBox that combines a button or editable field and a drop-down list. 
 * The user can select a value from the drop-down list, which appears at the user's request. 
 * If you make the combo box editable, then the combo box includes an editable field into which the user can type a value. 
 * @author deepak_shingan
 *
 */
public class Cab2bComboBox extends JComboBox {

    /**
     * Default combobox dimension
     */
    final Dimension dim = new Dimension(100, 20);

    /**
     * Default combobox color
     */
    static Color defaultBgColor = Color.WHITE;

    /**
     *   Creates a Cab2bComboBox that takes it's items from an existing ComboBoxModel.
     * @param aModel
     */
    public Cab2bComboBox(ComboBoxModel aModel) {
        super(aModel);
        setCommonPreferences();
    }

    /**
     * Creates a Cab2bComboBox that contains the elements in the specified array.
     * @param items
     */
    public Cab2bComboBox(Object[] items) {
        super(items);
        setCommonPreferences();
    }

    /**
     * Creates a Cab2bComboBox that contains the elements in the specified Vector.
     * @param items
     */
    public Cab2bComboBox(Vector<?> items) {
        super(items);
        setCommonPreferences();
    }

    /**
     * Creates a Cab2bComboBox with a default data model.
     */
    public Cab2bComboBox() {
        super();
        setCommonPreferences();
    }

    /**
     * This will set common preferences which should be
     * common to all Cab2bComboBoxes.
     */
    private void setCommonPreferences() {
        this.setPreferredSize(dim);
        this.setFont(new Font("SansSerif", Font.PLAIN, 10));
        this.setBackground(defaultBgColor);
    }

}
