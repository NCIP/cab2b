/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import javax.swing.JDialog;

/**
 * Customized dialogbox interface
 * @author Chetan_BH
 *
 */
public interface IDialogInterface {
    /**
     * Sets parent window for dialog box
     * @param dialog
     */
    public void setParentWindow(JDialog dialog);
}
