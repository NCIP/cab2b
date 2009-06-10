
package edu.wustl.cab2b.client.ui.controls;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;

import org.jdesktop.swingx.plaf.basic.BasicHyperlinkUI;

/**
 * Customized UI for Cab2bHyperlink.
 * @author chetan_bh
 */
public class Cab2bHyperlinkUI extends BasicHyperlinkUI
{

	public Cab2bHyperlinkUI()
	{
		super();
	}

	protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text)
	{
		super.paintText(g, b, textRect, text);
		
		/* By Default Cab2bHyperlink will have hyperlinks underlined, instead of
		 * underlining hyperlinks on mouse rollover */
		paintUnderline(g, b, textRect, text);
	}

	private void paintUnderline(Graphics g, AbstractButton b, Rectangle rect, String text)
	{
		FontMetrics fm = g.getFontMetrics();
		int descent = fm.getDescent();
		
		g.drawLine(rect.x + getTextShiftOffset(), (rect.y + rect.height) - descent + 1
				+ getTextShiftOffset(), rect.x + rect.width + getTextShiftOffset(),
				(rect.y + rect.height) - descent + 1 + getTextShiftOffset());
	}

}
