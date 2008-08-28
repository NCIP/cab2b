package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;

import javax.swing.JTextField;

/**
 * Textfiled class extended from JTextField, which is a lightweight component that allows the editing of a single line of text.
 * @author Chetan_BH
 *
 */
public class Cab2bTextField extends JTextField {

    /**
     * Default dimension
     */
    Dimension dim = new Dimension(100, 20);

    /**
     * Constructs a new TextField.
     */
    public Cab2bTextField() {
        super();
        setPreferredSize(dim);
    }

    /**
     * Constructs a new TextField initialized with the specified text.
     * @param initString
     */
    public Cab2bTextField(String initString) {
        super(initString);
        setPreferredSize(dim);
    }

    /**
     * Constructs a new TextField initialized with the specified text and dimension dim.
     * @param initString
     * @param dim
     */
    public Cab2bTextField(String initString, Dimension dim) {
        super(initString);
        this.dim = dim;
        setPreferredSize(this.dim);
    }
}
