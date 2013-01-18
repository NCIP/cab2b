/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.IGraphLinkRenderer;
import org.netbeans.graph.api.control.editor.IGraphEditor;
import org.netbeans.graph.api.model.IGraphLink;
import org.netbeans.graph.api.model.ability.IDisplayable;
import org.openide.util.Utilities;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

public class SimpleLinkRenderer implements IGraphLinkRenderer {

    private static final boolean disableAntialiasing = Utilities.isWindows()
            && System.getProperty("java.version", "").startsWith("1.4");

    private static final Color colorLink = Color.BLACK;//new Color (230, 139, 44); // sanjeev

    //private static final Color colorLinkSelected = SimpleDocumentRenderer.getSelectionForegroundColor ();
    private static final Color colorLinkSelected = Color.BLUE;

    private static final Color colorLinkHighlighted = new Color(0x4B, 0x79, 0xBF);

    private static final double CORNER_SIZE = 5;

    static Color relinkingColor = Color.GRAY;

    private static final float ARROW_SIZE = 12;

    private static final float ARROW_ANGLE = (float) Math.PI / 10;

    private static final int LAYER_LINK = 200;

    private static final int LAYER_SELECTED_PORTS = 350;

    private static final int LAYER_ALT_MODE = 500;

    private final int[] layers = new int[] { LAYER_LINK, LAYER_SELECTED_PORTS, LAYER_ALT_MODE };

    private GraphHelper helper;

    public SimpleLinkRenderer(GraphHelper helper, IGraphLink link) {
        this.helper = helper;
    }

    public void layoutLink(IGraphLink link, Graphics2D gr) {
        Point[] controlPoints = helper.getControlPoints(link);
        Rectangle bounds = new Rectangle();
        if (controlPoints != null && controlPoints.length > 0) {
            bounds.setLocation(controlPoints[0]);
            Rectangle rect = new Rectangle();
            for (int i = 1; i < controlPoints.length; i++) {
                rect.setLocation(controlPoints[i]);
                Rectangle.union(bounds, rect, bounds);
            }
            bounds.grow(8, 8); // lines and arrow size
        }

        layoutLinkHook(link, bounds);
        helper.setLinkBounds(link, bounds);
    }

    protected void layoutLinkHook(IGraphLink link, Rectangle bounds) {
    }

    public int[] getLayers(IGraphLink link) {
        return layers;
    }

    public void renderLink(IGraphLink link, Graphics2D gr, int layer) {
        final boolean hightlighted = helper.isComponentSelected(link) || helper.isComponentMouseOver(link);
        if (layer == LAYER_LINK) {
            renderLinkHook(link, gr);
            gr.setColor(hightlighted ? colorLinkSelected
                    : (helper.isComponentHighlighted(link) ? colorLinkHighlighted : colorLink));
            paintLink(hightlighted, link, gr);
        } else if (layer == LAYER_SELECTED_PORTS) {
            if (hightlighted) {
                final Point[] controlPoints = helper.getControlPoints(link);
                if (controlPoints != null) {
                    final int length = controlPoints.length;
                    if (length > 0) {
                        paintSelectedPoint(gr, controlPoints[0]);
                    }
                    if (length > 1) {
                        paintSelectedPoint(gr, controlPoints[length - 1]);
                    }
                }
            }
        } else if (layer == LAYER_ALT_MODE && helper.isAltMode()) {
            final Point[] controlPoints = helper.getControlPoints(link);
            if (controlPoints != null) {
                for (int i = 0; i < controlPoints.length; i++) {
                    paintAltPoint(gr, controlPoints[i]);
                }
            }
        }

    }

    protected void renderLinkHook(IGraphLink link, Graphics2D gr) {
    }

    public boolean isInActiveAreaOfControlPoint(IGraphLink link, Point controlPoint, Point location) {
        return new Rectangle(controlPoint.x - 3, controlPoint.y - 3, 7, 7).contains(location);
    }

    public IGraphEditor getEditor(IGraphLink link, Point position) {
        return null;
    }

    public String getToolTipText(IGraphLink link, Point position) {
        IDisplayable displayable = (IDisplayable) link.getLookup().lookup(IDisplayable.class);
        return displayable != null ? displayable.getTooltipText() : null;
    }

    private void paintLink(boolean hightlighted, IGraphLink link, Graphics2D gr) {
        Point[] points = helper.getControlPoints(link);
        paintLinkWithCorners(hightlighted, gr, points);
    }

    private static void paintLinkWithCorners(boolean hightlighted, Graphics2D gr, Point[] points) {
        if (points == null || points.length <= 0) {
            return;
        }
        final int maxnewpoints = points.length * 2 - 2;
        final int maxpoints2 = points.length - 2;

        int[] newpointsX = new int[maxnewpoints];
        int[] newpointsY = new int[maxnewpoints];
        int newpointspos = 0;

        for (int a = 0; a < points.length - 1; a++) {
            Point p1 = points[a];
            Point p2 = points[a + 1];
            double len = p1.distance(p2);

            if (a > 0) {
                Point p0 = points[a - 1];
                double ll = p0.distance(p1);
                if (len < ll) {
                    ll = len;
                }
                ll /= 2;
                double cll = CORNER_SIZE;
                if (cll > ll) {
                    cll = ll;
                }
                double direction = Math.atan2(p2.y - p1.y, p2.x - p1.x);
                if (!Double.isNaN(direction)) {
                    newpointsX[newpointspos] = p1.x + (int) (cll * Math.cos(direction));
                    newpointsY[newpointspos] = p1.y + (int) (cll * Math.sin(direction));
                    newpointspos++;
                }
            } else {
                newpointsX[newpointspos] = p1.x;
                newpointsY[newpointspos] = p1.y;
                newpointspos++;
            }

            if (a < maxpoints2) {
                Point p3 = points[a + 2];
                double ll = p2.distance(p3);
                if (len < ll) {
                    ll = len;
                }
                ll /= 2;
                double cll = CORNER_SIZE;
                if (cll > ll) {
                    cll = ll;
                }
                double direction = Math.atan2(p2.y - p1.y, p2.x - p1.x);
                if (!Double.isNaN(direction)) {
                    newpointsX[newpointspos] = p2.x - (int) (cll * Math.cos(direction));
                    newpointsY[newpointspos] = p2.y - (int) (cll * Math.sin(direction));
                    newpointspos++;
                }
            } else {
                newpointsX[newpointspos] = p2.x;
                newpointsY[newpointspos] = p2.y;
                newpointspos++;
            }
        }
        assert newpointspos == maxnewpoints;
        paintLink(hightlighted, gr, newpointsX, newpointsY, newpointspos);
    }

    static void paintLink(boolean hightlighted, Graphics2D gr, int[] xs, int[] ys, int length) {
        Stroke previousStroke = gr.getStroke();
        if (hightlighted) {
            gr.setStroke(new BasicStroke(2));
        }
        if (length <= 1) {
            return;
        }

        // WARNING - because the bug in jdk 1.4.2 (rendering gr.draw (new Line2D()) cause PRException), disabling antialiasing as a workaround
        Object originalAntialiasingRenderingHint = null;
        if (disableAntialiasing) {
            originalAntialiasingRenderingHint = gr.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        double direction = Double.NaN;
        for (int i = length - 2; i >= 0; i--) {
            final int x = xs[i + 1] - xs[i];
            final int y = ys[i + 1] - ys[i];
            if (x == 0 && y == 0) {
                continue;
            }
            direction = Math.atan2(y, x);
            break;
        }

        GeneralPath line = new GeneralPath();
        line.moveTo(xs[0] + 0.5f, ys[0] + 0.5f);
        for (int i = 1; i < length; i++) {
            line.lineTo(xs[i] + 0.5f, ys[i] + 0.5f);
        }
        gr.draw(line);
        if (!Double.isNaN(direction)) {
            float xend = (float) xs[length - 1] + 0.5f - 3.0f * (float) Math.cos(direction);
            float yend = (float) ys[length - 1] + 0.5f - 3.0f * (float) Math.sin(direction);
            float xleft = xend - ARROW_SIZE * (float) Math.cos(direction + ARROW_ANGLE);
            float yleft = yend - ARROW_SIZE * (float) Math.sin(direction + ARROW_ANGLE);
            float xright = xend - ARROW_SIZE * (float) Math.cos(direction - ARROW_ANGLE);
            float yright = yend - ARROW_SIZE * (float) Math.sin(direction - ARROW_ANGLE);

            Line2D.Float floatLine;

            floatLine = new Line2D.Float();
            floatLine.x1 = xend;
            floatLine.y1 = yend;
            floatLine.x2 = xleft;
            floatLine.y2 = yleft;
            gr.draw(floatLine);

            floatLine = new Line2D.Float();
            floatLine.x1 = xend;
            floatLine.y1 = yend;
            floatLine.x2 = xright;
            floatLine.y2 = yright;
            gr.draw(floatLine);
        }

        // WARNING - because the bug in jdk 1.4.2 (rendering gr.draw (new Line2D()) cause PRException), disabling antialiasing as a workaround
        if (disableAntialiasing) {
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, originalAntialiasingRenderingHint);
        }
        gr.setStroke(previousStroke);
    }

    private void paintSelectedPoint(Graphics2D gr, Point point) {
        if (point != null) {
            SimpleDocumentRenderer.renderSelectedRect(gr, new Rectangle(point.x - 3, point.y - 3, 7, 7));
        }
    }

    private void paintAltPoint(Graphics2D gr, Point point) {
        if (point != null) {
            SimpleDocumentRenderer.renderAltRect(gr, new Rectangle(point.x - 3, point.y - 3, 7, 7));
        }
    }

}