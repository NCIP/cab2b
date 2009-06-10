package edu.wustl.cab2b.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

/**
 * The custom border that is applied to all the cards in the main search dialog.
 * This is in accordance with UE specs.
 * 
 * @author mahesh_iyer
 * 
 */

public class SearchDialogBorder extends AbstractBorder {
    private static final long serialVersionUID = 1L;

    public Insets getBorderInsets(Component c) {
        return new Insets(8, 4, 4, 4);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.WHITE);
        /*Top Border.*/
        g.fillRect(x, y, width, 8);
        /*Left Border.*/
        g.fillRect(x, 8, 4, height - 12);
        /*Bottom Border.*/
        g.fillRect(x, height - 4, width, 4);
        /*Right Border.*/
        g.fillRect(width - 4, 8, 4, height - 12);
    }

}
