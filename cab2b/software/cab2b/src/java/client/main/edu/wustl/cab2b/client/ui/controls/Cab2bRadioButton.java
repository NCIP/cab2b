package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JRadioButton;

/**
 * An implementation of a radio button -- an item that can be selected or deselected, and which displays its state to the user
 * @author Chetan_BH 
 */
public class Cab2bRadioButton extends JRadioButton {
    private static final long serialVersionUID = 1L;

    /**
     * Creates an unselected radio button with the specified text.
     * @param strLabel
     */
    public Cab2bRadioButton(String strLabel) {
        super(strLabel);
        /*Arial font in windows is mapped to the SansSerif logical name.*/
        this.setFont(new Font("SansSerif", Font.PLAIN, 12));
        this.setBackground(Color.WHITE);
    }

}
