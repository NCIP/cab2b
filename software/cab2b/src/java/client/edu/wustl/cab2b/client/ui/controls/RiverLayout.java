/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;
import static edu.wustl.cab2b.client.ui.controls.LayoutConstants.HFILL;
import static edu.wustl.cab2b.client.ui.controls.LayoutConstants.LINE_BREAK;
import static edu.wustl.cab2b.client.ui.controls.LayoutConstants.PARAGRAPH_BREAK;
import static edu.wustl.cab2b.client.ui.controls.LayoutConstants.TAB_SPACE;
import static edu.wustl.cab2b.client.ui.controls.LayoutConstants.TAB_STOP;
import static edu.wustl.cab2b.client.ui.controls.LayoutConstants.VCENTER;
import static edu.wustl.cab2b.client.ui.controls.LayoutConstants.VFILL;
import static edu.wustl.cab2b.client.ui.controls.LayoutConstants.VTOP;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * <p>
 * RiverLayout makes it very simple to construct user interfaces as components
 * are laid out similar to how text is added to a word processor (Components
 * flow like a "river". RiverLayout is however much more powerful than
 * FlowLayout: Components added with the add() method generally gets laid out
 * horizontally, but one may add a string before the component being added to
 * specify "constraints" like this: add("br hfill", new JTextField("Your name
 * here"); The code above forces a "line break" and extends the added component
 * horizontally. Without the "hfill" constraint, the component would take on its
 * preferred size.
 * </p>
 * <p>
 * List of constraints:
 * <ul>
 * <li>br - Add a line break
 * <li>p - Add a paragraph break
 * <li>tab - Add a tab stop (handy for constructing forms with labels followed
 * by fields)
 * <li>hfill - Extend component horizontally
 * <li>vfill - Extent component vertically (currently only one allowed)
 * <li>left - Align following components to the left (default)
 * <li>center - Align following components horizontally centered
 * <li>right - Align following components to the right
 * <li>vtop - Align following components vertically top aligned
 * <li>vcenter - Align following components vertically centered (default)
 * </ul>
 * </p>
 * @author Mahesh
 */
public class RiverLayout extends FlowLayout implements LayoutManager, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Map of component and constraint 
     */
    private Map<Component, String> constraints = new HashMap<Component, String>();

    /**
     * Default vertical alignment
     */
    private String valign = VCENTER;

    /**
     * Horizontal gap
     */
    private int hgap;

    /**
     * Vertical gap
     */
    private int vgap;

    /**
     * Extra insets
     */
    private Insets extraInsets;

    /**
     * Dummy values for insets
     */
    private Insets totalInsets = new Insets(0, 0, 0, 0);
    // Dummy values. Set by getInsets()

    /**
     * Constructor
     */
    public RiverLayout() {
        this(0, 0);
    }

    /**
     * Constructor
     * @param hgap Horizontal Gap
     * @param vgap Vertical Gap
     */
    public RiverLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
        setExtraInsets(new Insets(0, hgap, hgap, hgap));
    }

    /**
     * Gets the horizontal gap between components.
     * @return Horizontal Gap
     */
    public int getHgap() {
        return hgap;
    }

    /**
     * Sets the horizontal gap between components.
     * @param Sets Horizontal Gap
     */
    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    /**
     * Gets the vertical gap between components.
     * @return vertical gap
     */
    public int getVgap() {
        return vgap;
    }

    /**
     * Gets the extra insets between components.
     * @return Insets
     */
    public Insets getExtraInsets() {
        return extraInsets;
    }

    /**
     * Sets the extra insets between components.
     * @param newExtraInsets
     */
    public void setExtraInsets(Insets newExtraInsets) {
        extraInsets = newExtraInsets;
    }

    /**
     * Get insets for a given container
     * @param target target
     * @return Insets Insets
     */
    protected Insets getInsets(Container target) {
        Insets insets = target.getInsets();
        totalInsets.top = insets.top + extraInsets.top;
        totalInsets.left = insets.left + extraInsets.left;
        totalInsets.bottom = insets.bottom + extraInsets.bottom;
        totalInsets.right = insets.right + extraInsets.right;
        return totalInsets;
    }

    /**
     * Sets the vertical gap between components.
     * @param vgap Vertical Gap
     */
    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    /**
     * @param name
     *            the name of the component
     * @param comp
     *            the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
        constraints.put(comp, name);
    }

    /**
     * Removes the specified component from the layout. Not used by this class.
     * 
     * @param comp
     *            the component to remove
     * @see java.awt.Container#removeAll
     */
    public void removeLayoutComponent(Component comp) {
        constraints.remove(comp);
    }

    /**
     * Returns true if component is placed  in first in row
     * @param comp
     * @return boolean
     */
    boolean isFirstInRow(Component comp) {
        String cons = (String) constraints.get(comp);
        return cons != null
                && (cons.indexOf(LINE_BREAK) != -1 || cons.indexOf(PARAGRAPH_BREAK) != -1);
    }

    /**
     * Returns true if Hfill Constraints are set otherwise false
     * @param comp
     * @return boolean
     */
    boolean hasHfill(Component comp) {
        return hasConstraint(comp, HFILL);
    }

    /**
     * Returns true if Vfill Constraints are set otherwise false
     * @param comp
     * @return boolean
     */
    boolean hasVfill(Component comp) {
        return hasConstraint(comp, VFILL);
    }

    /**
     * Returns true if Constraints are set otherwise false
     * @param comp
     * @param test
     * @return
     */
    boolean hasConstraint(Component comp, String test) {
        String cons = (String) constraints.get(comp);
        if (cons == null) {
            return false;
        }
        StringTokenizer tokens = new StringTokenizer(cons);
        while (tokens.hasMoreTokens())
            if (tokens.nextToken().equals(test)) {
                return true;
            }
        return false;
    }

    /**
     * Figure out tab stop x-positions
     * @param target target
     * @return Ruler
     */
    protected Ruler calcTabs(Container target) {
        Ruler ruler = new Ruler();
        int nmembers = target.getComponentCount();

        int x = 0;
        int tabIndex = 0; // First tab stop
        for (int i = 0; i < nmembers; i++) {
            Component m = target.getComponent(i);
            // if (m.isVisible()) {
            if (isFirstInRow(m) || i == 0) {
                x = 0;
                tabIndex = 0;
            } else {
                x += TAB_SPACE;
            }    
            if (hasConstraint(m, TAB_STOP)) {
                ruler.setTab(tabIndex, x); // Will only increase
                x = ruler.getTab(tabIndex++); // Jump forward if neccesary
            }
            Dimension d = m.getPreferredSize();
            x += d.width;
        }
        // }
        return ruler;
    }

    /**
     * Returns the preferred dimensions for this layout given the <i>visible</i>
     * components in the specified target container.
     * 
     * @param target
     *            the component which needs to be laid out
     * @return the preferred dimensions to lay out the subcomponents of the
     *         specified container
     * @see Container
     * @see #minimumLayoutSize
     * @see java.awt.Container#getPreferredSize
     */
    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            Dimension rowDim = new Dimension(0, 0);
            int nmembers = target.getComponentCount();
            boolean firstVisibleComponent = true;
            int tabIndex = 0;
            Ruler ruler = calcTabs(target);

            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                // if (m.isVisible()) {
                if (isFirstInRow(m)) {
                    tabIndex = 0;
                    dim.width = Math.max(dim.width, rowDim.width);
                    dim.height += rowDim.height + vgap;
                    if (hasConstraint(m, PARAGRAPH_BREAK)) {
                        dim.height += 2 * vgap;
                    }
                    rowDim = new Dimension(0, 0);
                }
                if (hasConstraint(m, TAB_STOP)) {
                    rowDim.width = ruler.getTab(tabIndex++);
                }
                Dimension d = m.getPreferredSize();
                rowDim.height = Math.max(rowDim.height, d.height);
                if (firstVisibleComponent) {
                    firstVisibleComponent = false;
                } else {
                    rowDim.width += hgap;
                }
                rowDim.width += d.width;
                // }
            }
            dim.width = Math.max(dim.width, rowDim.width);
            dim.height += rowDim.height;

            Insets insets = getInsets(target);
            dim.width += insets.left + insets.right;// + hgap * 2;
            dim.height += insets.top + insets.bottom;// + vgap * 2;
            return dim;
        }
    }

    /**
     * Centers the elements in the specified row, if there is any slack.
     * 
     * @param target the component which needs to be moved
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width dimensions
     * @param height the height dimensions
     * @param rowStart the beginning of the row
     * @param rowEnd the the ending of the row
     * @param ltr ltr
     * @param ruler ruler
     */
    protected void moveComponents(Container target, int x, int y, int width, int height, int rowStart, int rowEnd,
                                  boolean ltr, Ruler ruler) {
        synchronized (target.getTreeLock()) {
            switch (getAlignment()) {
                case FlowLayout.LEFT:
                    x += ltr ? 0 : width;
                    break;
                case FlowLayout.CENTER:
                    x += width / 2;
                    break;
                case FlowLayout.RIGHT:
                    x += ltr ? width : 0;
                    break;
                case LEADING:
                    break;
                case TRAILING:
                    x += width;
                    break;
                default: break;
            }
            int tabIndex = 0;
            for (int i = rowStart; i < rowEnd; i++) {
                Component m = target.getComponent(i);
                if (hasConstraint(m, TAB_STOP)) {
                    x = getInsets(target).left + ruler.getTab(tabIndex++);
                }
                int dy = (valign == VTOP) ? 0 : (height - m.getHeight()) / 2;
                if (ltr) {
                    m.setLocation(x, y + dy);
                } else {
                    m.setLocation(target.getWidth() - x - m.getWidth(), y + dy);
                }
                x += m.getWidth() + hgap;
            }
        }
    }

    /**
     * Relative movement
     * @param target Target
     * @param dx X 
     * @param dy Y
     * @param rowStart Row Start
     * @param rowEnd Row End 
     */
    protected void relMove(Container target, int dx, int dy, int rowStart, int rowEnd) {
        synchronized (target.getTreeLock()) {
            for (int i = rowStart; i < rowEnd; i++) {
                Component m = target.getComponent(i);
                m.setLocation(m.getX() + dx, m.getY() + dy);
            }

        }
    }

    /**
     * Adjust Alignment
     * @param m Component
     */
    protected void adjustAlignment(Component m) {
        if (hasConstraint(m, LayoutConstants.LEFT)) {
            setAlignment(FlowLayout.LEFT);
        } else if (hasConstraint(m, LayoutConstants.RIGHT)) {
            setAlignment(FlowLayout.RIGHT);
        } else if (hasConstraint(m, LayoutConstants.CENTER)) {
            setAlignment(FlowLayout.CENTER);
        }
        if (hasConstraint(m, LayoutConstants.VTOP)) {
            valign = VTOP;
        } else if (hasConstraint(m, LayoutConstants.VCENTER)) {
            valign = VCENTER;
        }
    }

    /**
     * Lays out the container. This method lets each component take its
     * preferred size by reshaping the components in the target container in
     * order to satisfy the constraints of this <code>FlowLayout</code>
     * object.
     * @param target the specified component being laid out
     */
    public void layoutContainer(Container target) {
        setAlignment(FlowLayout.LEFT);
        synchronized (target.getTreeLock()) {
            Insets insets = getInsets(target);
            int maxwidth = target.getWidth() - (insets.left + insets.right);
            int maxheight = target.getHeight() - (insets.top + insets.bottom);

            int nmembers = target.getComponentCount();
            int x = 0; 
            int y = insets.top + vgap;
            int rowh = 0;
            int start = 0;
            int moveDownStart = 0;

            boolean ltr = target.getComponentOrientation().isLeftToRight();
            Component toHfill = null;
            Component toVfill = null;
            Ruler ruler = calcTabs(target);
            int tabIndex = 0;

            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                Dimension d = m.getPreferredSize();
                m.setSize(d.width, d.height);

                if (isFirstInRow(m)) {
                    tabIndex = 0;
                }
                if (hasConstraint(m, TAB_STOP)) {
                    x = ruler.getTab(tabIndex++);
                }
                if (!isFirstInRow(m)) {
                    if (i > 0 && !hasConstraint(m, TAB_STOP)) {
                        x += hgap;
                    }
                    x += d.width;
                    rowh = Math.max(rowh, d.height);
                } else {
                    if (toVfill != null && moveDownStart == 0) {
                        moveDownStart = i;
                    }
                    if (toHfill != null) {
                        toHfill.setSize(toHfill.getWidth() + maxwidth - x, toHfill.getHeight());
                        x = maxwidth;
                    }
                    moveComponents(target, insets.left, y, maxwidth - x, rowh, start, i, ltr, ruler);
                    x = d.width;
                    y += vgap + rowh;
                    if (hasConstraint(m, PARAGRAPH_BREAK)) {
                        y += 2 * vgap;
                    }
                    rowh = d.height;
                    start = i;
                    toHfill = null;
                }

                if (hasHfill(m)) {
                    toHfill = m;
                }
                if (hasVfill(m)) {
                    toVfill = m;
                }
                adjustAlignment(m);
            }

            if (toVfill != null && moveDownStart == 0) { 
                // Don't move anything if hfill component is last component
                moveDownStart = nmembers;
            }
            if (toHfill != null) { // last component
                toHfill.setSize(toHfill.getWidth() + maxwidth - x, toHfill.getHeight());
                x = maxwidth;
            }
            moveComponents(target, insets.left, y, maxwidth - x, rowh, start, nmembers, ltr, ruler);
            int yslack = maxheight - (y + rowh);
            if (yslack != 0 && toVfill != null) {
                toVfill.setSize(toVfill.getWidth(), yslack + toVfill.getHeight());
                relMove(target, 0, yslack, moveDownStart, nmembers);
            }
        }
    }
}

/**
 * Ruler class for container
 * @author Chetan_BH
 */
class Ruler {
    private Vector<Integer> tabs = new Vector<Integer>();

    /**
     * Sets Tab
     * @param num Number
     * @param xpos x-position
     */
    public void setTab(int num, int xpos) {
        if (num >= tabs.size()) {
            tabs.add(num, new Integer(xpos));
        } else {
            // Transpose all tabs from this tab stop and onwards
            int delta = xpos - getTab(num);
            if (delta > 0) {
                for (int i = num; i < tabs.size(); i++) {
                    tabs.set(i, new Integer(getTab(i) + delta));
                }
            }
        }
    }

    /**
     * Get tabs  
     * @param num number
     * @return tabs
     */
    public int getTab(int num) {
        return tabs.get(num).intValue();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer ret = new StringBuffer(getClass().getName() + " {");
        for (int i = 0; i < tabs.size(); i++) {
            ret.append(tabs.get(i));
            if (i < tabs.size() - 1) {
                ret.append(',');
            }
        }
        ret.append('}');
        return ret.toString();
    }   
}
