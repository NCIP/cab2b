/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

/**
 * A special type of Panel that has a Title section and a Content section.
 * @author Chetan_BH
 *
 */
public class Cab2bTitledPanel extends JXTitledPanel {
    private static final long serialVersionUID = 1L;

    /**
     * Paint object
     */
    GradientPaint gp = new GradientPaint(new Point2D.Double(.3d, 0), new Color(185, 211, 238), new Point2D.Double(
            .7, 0), Color.WHITE);

    /**
     * Creates Cab2bTitledPanel with empty text title
     */
    public Cab2bTitledPanel() {
        this("");
    }

    /**
     * Creates Cab2bTitledPanel with specified text title
     * @param title
     */
    public Cab2bTitledPanel(String title) {
        this(title, new Cab2bPanel());
        this.setBorder(null);
    }

    /**
     * Creates Cab2bTitledPanel with empty text title and container
     * @param title
     * @param container
     */
    public Cab2bTitledPanel(String title, Container container) {
        super(title, container);
        this.setBackground(Color.WHITE);
        this.setBorder(null);
        setTitlePainter(new BasicGradientPainter(gp));
    }
}
