package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class CustomBorder extends AbstractBorder {
	private static final long serialVersionUID = 1L;

	/** Gets the Border Insets for a given Component
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
	 * @param c
	 * @return Insets of a given component
	 */
	public Insets getBorderInsets(Component c) {
		return new Insets(1, 1, 1, 1);
	}

	/** Checks for Opaque Border
	 * @see javax.swing.border.AbstractBorder#isBorderOpaque()
	 * @return Boolean value 
	 */
	public boolean isBorderOpaque() {
		return true;
	}

	/** Paints the Border of Given component with given color
	 * @see javax.swing.border.AbstractBorder#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
	 * @param c
	 * @param g
	 * @param x
	 * @param y
	 * @param widht
	 * @param height
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		g.setColor(Color.BLACK);

		g.drawLine(x, y, x, y + height);
		g.drawLine(x, y + height - 1, x + width, y + height - 1);
		g.drawLine(x + width - 1, y + height - 1, x + width - 1, y);
	}

}
