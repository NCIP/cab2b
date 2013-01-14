/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

/**
 * http://blog.elevenworks.com/?p=5
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 * An implementation of the TabbedPaneUI that looks like the tabs.
 * @author Chetan_BH
 * @author Deepak_Shingan
 *
 */
public class Cab2bTabbedPaneUI extends BasicTabbedPaneUI {
    /**
     * Tab insects
     */
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

    // ------------------------------------------
    //  Custom installation methods
    // ------------------------------------------

    /**
     * Initilizes UI
     * @param c JComponent
     * @return ComponentUI
     */
    public static ComponentUI createUI(JComponent c) {
        return new Cab2bTabbedPaneUI();
    }

    /** 
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installDefaults()
     * It is used to set default settings of Font and color
     */
    protected void installDefaults() {
        super.installDefaults();
        tabAreaInsets.left = (calculateTabHeight(0, 0, tabPane.getFont().getSize()) / 4) + 1;
        selectedTabPadInsets = new Insets(0, 0, 0, 0);

        selectedColor = Color.WHITE;
        unselectedColor = tabPane.getBackground();

        boldFont = tabPane.getFont().deriveFont(Font.BOLD);
        boldFontMetrics = tabPane.getFontMetrics(boldFont);
    }

    // --------------------------------------------------
    //  Custom sizing methods
    // --------------------------------------------------

    /**
     * @param pane is passed whose tab count is to be known
     * @return 1
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getTabRunCount(javax.swing.JTabbedPane)
     */
    public int getTabRunCount(JTabbedPane pane) {
        return 1;
    }

    /**
     * @param tabPlacement
     * @return TAB_INSETS
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getContentBorderInsets(int)
     */
    protected Insets getContentBorderInsets(int tabPlacement) {
        return TAB_INSETS;
    }

    /** 
     * Function calculates tab height based on input parameters of tabPlacement and fontHeight
     * @param tabPlacement
     * @param tabIndex
     * @param fontHeight
     * @return vHeight
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabHeight(int, int, int)
     */
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        int vHeight = fontHeight + 2;
        if (vHeight % 2 == 0) {
            vHeight += 1;
        }
        return vHeight;
    }

    /**Function calculates tab width based on input parameters of tabPlacement and metrics
     * @param tabPlacement
     * @param tabIndex
     * @param metrics
     * @return tabWidth
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#calculateTabWidth(int, int, java.awt.FontMetrics)
     */
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + metrics.getHeight();
    }

    // ------------------------------------------------------
    //  Custom painting methods
    // ------------------------------------------------------

    // --------------------------------------------------------
    //  Methods that we want to suppress the behaviour of the superclass
    // --------------------------------------------------------

    /**Function paints tab background based on input parameters of tabPlacement and tab co-ordinates
     * @param g
     * @param tabPlacement
     * @param tabIndex
     * @param x
     * @param y
     * @param w
     * @param h
     * @param isSelected
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintTabBackground(java.awt.Graphics, int, int, int, int, int, int, boolean) 
     */
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                      boolean isSelected) {
        Polygon shape = new Polygon();

        //shape.addPoint(x - (h / 4), y + h);
        if (tabIndex == 0) {
            shape.addPoint(x - (h / 4), y + h);
            shape.addPoint(x - (h / 4), y);
        } else {
            shape.addPoint(x + (h / 4), y + h);
            shape.addPoint(x + (h / 4), y);
        }

        shape.addPoint(x + w - (h / 4), y);

        if (isSelected || (tabIndex == (rects.length - 1))) {
            if (isSelected) {
                g.setColor(selectedColor);
            } else {
                g.setColor(unselectedColor);
            }
            //shape.addPoint(x + w + (h / 4), y + h);
            shape.addPoint(x + w - (h / 4), y + h);
        } else {
            g.setColor(unselectedColor);
            //shape.addPoint(x + w, y + (h / 2));
            shape.addPoint(x + w - (h / 4), y + h);
        }

        g.fillPolygon(shape);
    }

    /** Function paints tab border based on input parameters of tabPlacement and tab co-ordinates
     * @param g
     * @param tabPlacement
     * @param tabIndex
     * @param x
     * @param y
     * @param w
     * @param h
     * @param isSelected 
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintTabBorder(java.awt.Graphics, int, int, int, int, int, int, boolean)
     */
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                  boolean isSelected) {
        g.setColor(darkShadow);
        //g.drawLine(x - (h / 4), y + h, x + (h / 4), y); // Left Line drwn from bottom to top.
        if (tabIndex == 0) {
            g.drawLine(x - (h / 4), y + h, x - (h / 4), y);
            //g.drawLine(x + (h / 4), y, x + w - (h / 4), y); // Top Line. drawn from left to right.
            g.drawLine(x - (h / 4), y, x + w - (h / 4), y);
        } else {
            g.drawLine(x + (h / 4), y + h, x + (h / 4), y);
            //g.drawLine(x + (h / 4), y, x + w - (h / 4), y); // Top Line. drawn from left to right.
            g.drawLine(x + (h / 4), y, x + w - (h / 4), y);
        }
        //g.drawLine(x + w + (h / 4), y, x + w + (h / 4), y + h);  // Right Line. drawn from bottom to top. 
        g.drawLine(x + w - (h / 4), y, x + w - (h / 4), y + h);

        g.drawLine(x - (h / 4), y + h, x + (h / 4), y + h);
        //g.drawLine(x + w - (h / 4), y+h, x + w + (h / 4), y+h);

    }

    /**
     * Function paints tab borderTopEdge based on input parameters of tabPlacement and tab co-ordinates
     * @param g
     * @param tabPlacement
     * @param selectedIndex
     * @param x
     * @param y
     * @param w
     * @param h
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderTopEdge(java.awt.Graphics, int, int, int, int, int, int)
     */
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w,
                                             int h) {
        Rectangle selectedRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);
        g.setColor(darkShadow);
        g.drawLine(x, y, selectedRect.x - (selectedRect.height / 4), y);
        g.drawLine(selectedRect.x + selectedRect.width + (selectedRect.height / 4), y, x + w, y);
        g.setColor(selectedColor);
        g.drawLine(selectedRect.x - (selectedRect.height / 4) + 1, y, selectedRect.x + selectedRect.width
                + (selectedRect.height / 4) - 1, y);

    }

    /**
     * Function paints tab borderRightEdge based on input parameters of tabPlacement and tab co-ordinates
     * @param g
     * @param tabPlacement
     * @param selectedIndex
     * @param x
     * @param y
     * @param w
     * @param h
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderRightEdge(java.awt.Graphics, int, int, int, int, int, int)
     */
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y,
                                               int w, int h) {

    }

    /**
     * Function paints tab borderLeftEdge based on input parameters of tabPlacement and tab co-ordinates
     * @param g
     * @param tabPlacement
     * @param selectedIndex
     * @param x
     * @param y
     * @param w
     * @param h
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderRightEdge(java.awt.Graphics, int, int, int, int, int, int)
     */
    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y,
                                              int w, int h) {

    }

    /**
     * Function paints tab borderBottomEdge based on input parameters of tabPlacement and tab co-ordinates
     * @param g
     * @param tabPlacement
     * @param selectedIndex
     * @param x
     * @param y
     * @param w
     * @param h
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderRightEdge(java.awt.Graphics, int, int, int, int, int, int)
     */
    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y,
                                                int w, int h) {

    }

    /**
     * Function paints focusIndicator based on input parameters of tabPlacement and Text orientation
     * @param g
     * @param tabPlacement
     * @param rects
     * @param tabIndex
     * @param iconRect
     * @param textRect
     * @param isSelected
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintContentBorderRightEdge(java.awt.Graphics, int, int, int, int, int, int)
     */
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
                                       Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        // Do nothing
    }

    /**
     * Function paints Text based on input parameters of tabPlacement and Text orientation and its Font
     * @param g
     * @param tabPlacement
     * @param font
     * @param metrics
     * @param tabIndex
     * @param title
     * @param textRect
     * @param isSelected
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintText(java.awt.Graphics, int, java.awt.Font, java.awt.FontMetrics, int, java.lang.String, java.awt.Rectangle, boolean)
     */
    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex,
                             String title, Rectangle textRect, boolean isSelected) {
        if (isSelected) {
            int vDifference = (int) (boldFontMetrics.getStringBounds(title, g).getWidth()) - textRect.width;
            textRect.x -= (vDifference / 2);
            super.paintText(g, tabPlacement, boldFont, boldFontMetrics, tabIndex, title, textRect, isSelected);
        } else {
            super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
        }
    }

    /**
     * Function gives the tabLabel
     * @param tabPlacement
     * @param tabIndex
     * @param isSelected
     * @return 0
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#getTabLabelShiftY(int, int, boolean)
     */
    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }
}
