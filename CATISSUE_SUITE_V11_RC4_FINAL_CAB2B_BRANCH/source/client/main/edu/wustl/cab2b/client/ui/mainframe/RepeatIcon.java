/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.mainframe;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.SwingConstants;

/**
 * This class repeats the icon of 1 pixel width over the entire banner length
 * @author hrishikesh_rajpathak
 * 
 */
public class RepeatIcon implements Icon {

	private Icon icon;

	private int direction;

	private int size;

	public RepeatIcon(Icon icon, int direction, int size) {
		this.icon = icon;
		this.direction = direction;
		this.size = size;
	}

	/**
	 * To get icon width
	 */
	public int getIconWidth() {

		if (this.direction == SwingConstants.HORIZONTAL) {
			return (this.size);
		} else {
			return (this.icon.getIconWidth());
		}
	}

	/**
	 * To get icon height
	 */
	public int getIconHeight() {
		if (this.direction == SwingConstants.HORIZONTAL) {
			return (this.icon.getIconHeight());
		} else {
			return (this.size);
		}
	}

	/**
	 * To paint icon over the entire specified area both verically and
	 * horizontally
	 * 
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
	 *      int, int)
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.translate(x, y);
		if (this.direction == SwingConstants.HORIZONTAL) {
			int iw = this.icon.getIconWidth();
			for (int i = 0; i < this.size; i += iw) {
				this.icon.paintIcon(c, g, i, 0);
			}
		} else {
			int ih = this.icon.getIconHeight();
			for (int i = 0; i < this.size; i += ih) {
				this.icon.paintIcon(c, g, 0, i);
			}
		}
		g.translate(-x, -y);
	}
}