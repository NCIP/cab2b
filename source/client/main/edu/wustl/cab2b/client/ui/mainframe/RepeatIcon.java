package edu.wustl.cab2b.client.ui.mainframe;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.SwingConstants;

/**
 * @author hrishikesh_rajpathak
 *
 */
public class RepeatIcon implements Icon {

	private Icon icon;

	// static private MyGlassPane myGlassPane;

	private int direction, size;

	public RepeatIcon(Icon icon, int direction, int size) {
		this.icon = icon;
		this.direction = direction;
		this.size = size;
	}

	public int getIconWidth() {
		return (this.direction == SwingConstants.HORIZONTAL) ? this.size : this.icon.getIconWidth();
	}

	public int getIconHeight() {
		return (this.direction == SwingConstants.HORIZONTAL) ? this.icon.getIconHeight()
				: this.size;
	}

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