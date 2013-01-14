/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;

import org.jdesktop.swingx.plaf.basic.BasicHyperlinkUI;

/**
 * Customized UI for Cab2bHyperlink. By Default Cab2bHyperlink will have
 * hyperlinks underlined, instead of underlining hyperlinks on mouse rollover.
 * 
 * @author chetan_bh
 * @author Chandrakant Talele
 */
public class Cab2bHyperlinkUI extends BasicHyperlinkUI {
	/**
	 * TRUE of this hyperlink is to be painted with plain font
	 */
	private boolean usePlainFont;

	/**
	 * @param usePlainFont
	 *            TRUE of this hyperlink is to be painted with plain font
	 *            otherwise FALSE
	 */
	public Cab2bHyperlinkUI(boolean usePlainFont) {
		super();
		this.usePlainFont = usePlainFont;
	}

	/**
	 * Paints the given Text with given color
	 * 
	 * @param graphics
	 * @param button
	 * @param textRect
	 * @param text
	 * @see javax.swing.plaf.basic.BasicButtonUI#paintText(java.awt.Graphics,
	 *      javax.swing.AbstractButton, java.awt.Rectangle, java.lang.String)
	 */

	protected void paintText(Graphics graphics, AbstractButton button,
			Rectangle textRect, String text) {
		Font temp = graphics.getFont();
		if (usePlainFont) {
			Font f = new Font(temp.getName(), Font.PLAIN, temp.getSize());
			graphics.setFont(f);
		}
		textRect.width=graphics.getFontMetrics().stringWidth(text);
		super.paintText(graphics, button, textRect, text);
		paintUnderline(graphics, textRect);
		graphics.setFont(temp);
	}

	/**
	 * Paints the hyperlink as underlined.
	 * 
	 * @param graphics
	 *            graphics to use
	 * @param textRect
	 *            Text Rectangle
	 */
	private void paintUnderline(Graphics graphics, Rectangle textRect) {
		FontMetrics fm = graphics.getFontMetrics();
		int descent = fm.getDescent();

		graphics.drawLine(textRect.x + getTextShiftOffset(),
				(textRect.y + textRect.height) - descent + 1
						+ getTextShiftOffset(), textRect.x + textRect.width
						+ getTextShiftOffset(), (textRect.y + textRect.height)
						- descent + 1 + getTextShiftOffset());
	}
}