package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.net.URL;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.wustl.cab2b.client.ui.RiverLayout;

/**
 * Stacks components vertically in boxes. Each box is created with a title and a component.<br>
 * The <code>StackedBox</code> can be added to a {@link javax.swing.JScrollPane}.
 */
public class StackedBox extends Cab2bPanel implements Scrollable {

    private static final long serialVersionUID = 1L;

    private Color titleBackgroundColor;

    private Color titleForegroundColor;

    private Color separatorColor;

    public StackedBox() {
        setLayout(new VerticalLayout());
        setOpaque(true);
        setBackground(Color.WHITE);
        setTitleForegroundColor(Color.BLACK);
        setTitleBackgroundColor(new Color(248, 248, 248));
        setSeparatorColor(Color.BLACK);
    }

    public Color getSeparatorColor() {
        return separatorColor;
    }

    public void setSeparatorColor(Color separatorColor) {
        this.separatorColor = separatorColor;
    }

    public Color getTitleForegroundColor() {
        return titleForegroundColor;
    }

    public void setTitleForegroundColor(Color titleForegroundColor) {
        this.titleForegroundColor = titleForegroundColor;
    }

    public Color getTitleBackgroundColor() {
        return titleBackgroundColor;
    }

    public void setTitleBackgroundColor(Color titleBackgroundColor) {
        this.titleBackgroundColor = titleBackgroundColor;
    }

    /**
     * Adds a new component to this <code>StackedBox</code>
     * 
     * @param title
     * @param component
     */
    public void addBox(String title, Component component, String iconFile, boolean toBeCollapsed) {
        final JXCollapsiblePane collapsible = new JXCollapsiblePane();
        collapsible.getContentPane().setBackground(Color.WHITE);
        collapsible.add(component);
        Action toggleAction = collapsible.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION);
        // use the collapse/expand icons from the JTree UI
        toggleAction.putValue(JXCollapsiblePane.COLLAPSE_ICON, UIManager.getIcon("Tree.expandedIcon"));
        toggleAction.putValue(JXCollapsiblePane.EXPAND_ICON, UIManager.getIcon("Tree.collapsedIcon"));

        URL url = this.getClass().getClassLoader().getResource(iconFile);
        ImageIcon imageIcon = null;
        if (url != null) {
            imageIcon = new ImageIcon(url);
        } else {
            imageIcon = new ImageIcon();
        }

        StackTitlePanel stackTitlePanel = new StackTitlePanel(title, imageIcon, toggleAction);
        if (toBeCollapsed) {
            stackTitlePanel.collapse(false);
        }
        stackTitlePanel.setBorder(new CustomizableBorder(new Insets(0, 0, 1, 0), new Color(125, 146, 147)));
        add(stackTitlePanel);
        add(collapsible);
    }

    /**
     * @see Scrollable#getPreferredScrollableViewportSize()
     */
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    /**
     * @see Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int, int)
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        //TODO need to look into this 
        return 10;
    }

    /**
     * @see Scrollable#getScrollableTracksViewportHeight()
     */
    public boolean getScrollableTracksViewportHeight() {
        if (getParent() instanceof JViewport) {
            return (((JViewport) getParent()).getHeight() > getPreferredSize().height);
        }
        return false;
    }

    /**
     * @see Scrollable#getScrollableTracksViewportWidth()
     */
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    /**
     * @see Scrollable#getScrollableUnitIncrement(java.awt.Rectangle, int, int)
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    /**
     * The border between the stack components. It separates each component with
     * a fine line border.
     */
    class SeparatorBorder implements Border {

        boolean isFirst(Component c) {
            return c.getParent() == null || c.getParent().getComponent(0) == c;
        }

        public Insets getBorderInsets(Component c) {
            // if the collapsible is collapsed, we do not want its border to be
            // painted.
            /*
             * if (c instanceof JXCollapsiblePane) { if
             * (((JXCollapsiblePane)c).isCollapsed()) { return new Insets(0, 0,
             * 0, 0); } }
             */
            // return new Insets(isFirst(c)?4:1, 0, 1, 0);
            return new Insets(0, 0, 0, 0);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(getSeparatorColor());
            if (isFirst(c)) {
                g.drawLine(x, y + 2, x + width, y + 2);
            }
            g.drawLine(x, y + height - 1, x + width, y + height - 1);
        }
    }

    public class StackTitlePanel extends JXPanel implements MouseInputListener {
        private static final long serialVersionUID = 1L;

        boolean toggleAction = true;

        String title = "";

        GradientPaint gp = new GradientPaint(new Point2D.Double(0, 1.0d), new Color(205, 215, 216),
                new Point2D.Double(0, 0.0d), new Color(255, 255, 255));

        Painter painter = new BasicGradientPainter(gp);

        Action action;

        public StackTitlePanel(String title, Icon imageIcon, Action action) {
            this.title = title;
            this.action = action;
            this.setBackgroundPainter(painter);

            this.addMouseListener(this);
            this.setLayout(new RiverLayout(5, 5));

            JLabel imageLabel = new JLabel(imageIcon);

            JLabel titleLabel = new JLabel(title);
            titleLabel.setBorder(new EmptyBorder(new Insets(1, 1, 8, 1)));

            this.add("", imageLabel);
            this.add("", titleLabel);
        }

        @Override
        public void paint(Graphics gp) {
            super.paint(gp);

            Graphics2D g2d = (Graphics2D) gp;
            Color oldColor = g2d.getColor();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (toggleAction) {
                Polygon openPoly = getPolygon("open");
                g2d.setColor(Color.GRAY);
                g2d.fillPolygon(openPoly);
            } else {
                Polygon closedPoly = getPolygon("closed");
                g2d.setColor(Color.GRAY);
                g2d.fillPolygon(closedPoly);
            }
            g2d.setColor(oldColor);
        }

        private Polygon getPolygon(String openOrClosed) {
            Polygon polygon = new Polygon();
            Dimension size = this.getSize();

            int x = size.width - 24;
            int y = (size.height / 2);
            if (openOrClosed.equalsIgnoreCase("open")) {
                polygon.addPoint(x + 6, y + 6);
                polygon.addPoint(x + 18, y + 6);
                polygon.addPoint(x + 12, y - 6);
            } else {
                polygon.addPoint(x + 6, y - 6); // Arrow Down.
                polygon.addPoint((x + 18), y - 6);
                polygon.addPoint((x + 12), y + 6);
            }
            return polygon;
        }

        public void collapse(boolean e) {
            toggleAction = !(e);
            ActionEvent ae = new ActionEvent(this, 123, "stackedBox");
            action.actionPerformed(ae);
            this.repaint();
        }

        public void mouseClicked(MouseEvent e) {
            toggleAction = !(toggleAction);
            ActionEvent ae = new ActionEvent(this, 123, "stackedBox");
            action.actionPerformed(ae);
            this.repaint();
        }

        public void mouseEntered(MouseEvent e) {
            Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
            this.setCursor(handCursor);
        }

        public void mouseExited(MouseEvent e) {
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            this.setCursor(normalCursor);
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }
    }
}