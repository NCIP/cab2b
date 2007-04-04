package edu.wustl.cab2b.client.ui.controls;

/**
 * http://blog.elevenworks.com/?p=5
 */

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

/**
 * An implementation of the TabbedPaneUI that looks like the tabs.
 * 
 */
public class Cab2bTabbedPaneUI extends BasicTabbedPaneUI
{
	private static final Insets TAB_INSETS = new Insets(1, 0, 0, 0);

	/**
	 * The font to use for the selected tab
	 */
	private Font boldFont;

	/**
	 * The font metrics for the selected font
	 */
	private FontMetrics boldFontMetrics;

	/**
	 * The color to use to fill in the background
	 */
	private Color selectedColor;


	/**
	 * The color to use to fill in the background
	 */
	private Color unselectedColor;

	// ------------------------------------------------------------------------------------------------------------------
	//  Custom installation methods
	// ------------------------------------------------------------------------------------------------------------------

	public static ComponentUI createUI(JComponent c)
	{
		return new Cab2bTabbedPaneUI();
	}

	protected void installDefaults()
	{
		super.installDefaults();
		tabAreaInsets.left = (calculateTabHeight(0, 0, tabPane.getFont().getSize()) / 4) + 1;
		selectedTabPadInsets = new Insets(0, 0, 0, 0);

		selectedColor = Color.WHITE;
		unselectedColor = tabPane.getBackground().darker();

		boldFont = tabPane.getFont().deriveFont(Font.BOLD);
		boldFontMetrics = tabPane.getFontMetrics(boldFont);
	}

	// ------------------------------------------------------------------------------------------------------------------
	//  Custom sizing methods
	// ------------------------------------------------------------------------------------------------------------------

	public int getTabRunCount(JTabbedPane pane)
	{
		return 1;
	}

	protected Insets getContentBorderInsets(int tabPlacement)
	{
		return TAB_INSETS;
	}

	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight)
	{
		int vHeight = fontHeight + 2;
		if (vHeight % 2 == 0)
		{
			vHeight += 1;
		}
		return vHeight;
	}

	protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics)
	{
		return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + metrics.getHeight();
	}

	// ------------------------------------------------------------------------------------------------------------------
	//  Custom painting methods
	// ------------------------------------------------------------------------------------------------------------------


	// ------------------------------------------------------------------------------------------------------------------
	//  Methods that we want to suppress the behaviour of the superclass
	// ------------------------------------------------------------------------------------------------------------------

	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected)
	{
		Polygon shape = new Polygon();

		//shape.addPoint(x - (h / 4), y + h);
		if(tabIndex == 0)
		{
			shape.addPoint(x - (h / 4), y + h);
			shape.addPoint(x - (h / 4), y);
		}else
		{
			shape.addPoint(x + (h / 4), y + h);
			shape.addPoint(x + (h / 4), y);
		}
		
		shape.addPoint(x + w - (h / 4), y);

		if (isSelected || (tabIndex == (rects.length - 1)))
		{
			if (isSelected)
			{
				g.setColor(selectedColor);
			}
			else
			{
				g.setColor(unselectedColor);
			}
			//shape.addPoint(x + w + (h / 4), y + h);
			shape.addPoint(x + w - (h / 4), y + h);
		}
		else
		{
			g.setColor(unselectedColor);
			//shape.addPoint(x + w, y + (h / 2));
			shape.addPoint(x + w - (h / 4), y + h);
		}

		g.fillPolygon(shape);
	}

	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected)
	{
		g.setColor(darkShadow);
		//g.drawLine(x - (h / 4), y + h, x + (h / 4), y); // Left Line drwn from bottom to top.
		if(tabIndex == 0)
		{
			g.drawLine(x - (h / 4), y+h, x - (h / 4), y);
			//g.drawLine(x + (h / 4), y, x + w - (h / 4), y); // Top Line. drawn from left to right.
			g.drawLine(x - (h/4), y, x + w - (h / 4), y);
		}else
		{
			g.drawLine(x + (h / 4), y+h, x + (h / 4), y);
			//g.drawLine(x + (h / 4), y, x + w - (h / 4), y); // Top Line. drawn from left to right.
			g.drawLine(x + (h/4), y, x + w - (h / 4), y);
		}
		//g.drawLine(x + w + (h / 4), y, x + w + (h / 4), y + h);  // Right Line. drawn from bottom to top. 
		g.drawLine(x + w - (h / 4), y, x + w - (h / 4), y+h);
		
		g.drawLine(x - (h / 4), y+h, x + (h / 4), y+h);
		//g.drawLine(x + w - (h / 4), y+h, x + w + (h / 4), y+h);
		
	}

	protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h)
	{
		Rectangle selectedRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);
		g.setColor(darkShadow);
		g.drawLine(x, y, selectedRect.x - (selectedRect.height / 4), y);
		g.drawLine(selectedRect.x + selectedRect.width + (selectedRect.height / 4), y, x + w, y);
		g.setColor(selectedColor);
		g.drawLine(selectedRect.x - (selectedRect.height / 4)+1, y,selectedRect.x + selectedRect.width + (selectedRect.height / 4)-1, y);		

	}

	protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h)
	{

	}

	protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h)
	{

	}

	protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h)
	{

	}

	protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected)
	{
		// Do nothing
	}

	protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected)
	{
		if (isSelected)
		{
			int vDifference = (int) (boldFontMetrics.getStringBounds(title, g).getWidth()) - textRect.width;
			textRect.x -= (vDifference / 2);
			super.paintText(g, tabPlacement, boldFont, boldFontMetrics, tabIndex, title, textRect, isSelected);
		}
		else
		{
			super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
		}
	}

	protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected)
	{
		return 0;
	}
	
	
	public static void main(String[] args)
	{
//		try
//		{
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		}
//		catch (Exception exc)
//		{
//			// Do nothing...
//		}

		JFrame vFrame = new JFrame();
		vFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vFrame.setSize(200, 200);
		JTabbedPane vTab = new JTabbedPane();
		vTab.setUI(new Cab2bTabbedPaneUI());

		JPanel vPanel1 = new JPanel();
		vPanel1.setBackground(Color.WHITE);
		vTab.add("One", vPanel1);

		JPanel vPanel2 = new JPanel();
		vPanel2.setBackground(Color.WHITE);
		vPanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
		vTab.add("Two", vPanel2);

		vTab.add("Three", new JButton("three"));

		vFrame.getContentPane().add(vTab);
		vFrame.setTitle("PPT Tabs");
		vFrame.setVisible(true);
	}
	
}
