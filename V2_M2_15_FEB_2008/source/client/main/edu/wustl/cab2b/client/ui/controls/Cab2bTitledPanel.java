package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

public class Cab2bTitledPanel extends JXTitledPanel {
    private static final long serialVersionUID = 1L;

    GradientPaint gp = new GradientPaint(new Point2D.Double(.3d, 0), new Color(185, 211, 238), new Point2D.Double(
            .7, 0), Color.WHITE);

    public Cab2bTitledPanel() {
        this("");

    }

    public Cab2bTitledPanel(String title) {
        this(title, new Cab2bPanel());
        this.setBorder(null);
    }

    public Cab2bTitledPanel(String title, Container container) {
        super(title, container);
        this.setBackground(Color.WHITE);
        this.setBorder(null);
        setTitlePainter(new BasicGradientPainter(gp));
    }
}
