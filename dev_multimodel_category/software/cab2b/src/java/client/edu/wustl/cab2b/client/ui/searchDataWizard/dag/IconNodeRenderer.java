/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.IGraphNodeRenderer;
import org.netbeans.graph.api.control.editor.IGraphEditor;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.IGraphPort;
import org.netbeans.graph.api.model.ability.IDisplayable;
import org.netbeans.graph.api.model.ability.INameEditable;
import org.openide.util.Utilities;

/**
 * This node control renders node as an icon and a display name below it. A default port is located on the left-side.
 * Other ports are on the right-side one below one.
 *
 * @author Pratibha Dhok
 */
public class IconNodeRenderer implements IGraphNodeRenderer {

    /**
     * Node fontS
     */
    private static Font font = Font.decode("dialog-bold").deriveFont(12.0f); // NOI18N

    /**
     * Font color 
     */
    private static Color colorFont = new Color(119, 68, 0); // 174, 100

    /**
     * Shadow font color
     */
    private static Color colorFontShadow = new Color(0xDEDEDE);

    /**
     * Shadow selected font color
     */
    private static Color colorFontShadowSelected = new Color(0xDEDEDE);

    /**
     * Space for node port
     */
    protected static int PORT_SPACE = 16; // sanjeev

    /**
     * Space for second node port
     */
    protected static int PORT_SPACE_2 = 8;

    /**
     * Rectangle background area 
     */
    private static final int LAYER_BACKGROUND = 100;

    /**
     * Node area
     */
    private static final int LAYER_NODE = 400;

    /**
     * Layers in node
     */
    private int[] layers = new int[] { LAYER_BACKGROUND, LAYER_NODE };

    /**
     * Old node rectangle 
     */
    Rectangle oldrect;

    /**
     * Returns font color
     * @return Color
     */
    public static Color getFontColor() {
        return colorFont;
    }

    /**
     * Returns font shadow color
     * @return Color
     */
    public static Color getFontShadowColor() {
        return colorFontShadow;
    }

    /**
     * Returns font selected shadow color
     * @return Color
     */
    public static Color getFontShadowSelectedColor() {
        return colorFontShadowSelected;
    }

    /**
     * Grpah helper utility class object
     */
    protected GraphHelper helper;

    /**
     * Rectangle for image 
     */
    private Rectangle imageRect;

    /**
     * Rectangle for text
     */
    protected Rectangle textRect;

    /**
     * Constructor
     * @param helper
     * @param node
     */
    public IconNodeRenderer(GraphHelper helper, IGraphNode node) {
        this.helper = helper;
    }

    /**
     * Sets location for ports
     * @param node
     * @param gr
     */
    protected void layoutPortsLocationsCore(IGraphNode node, Graphics2D gr) {
        IGraphPort defaultPort = helper.getNodeDefaultPort(node);

        final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(IDisplayable.class);
        Image image = displayable != null ? displayable.getIcon() : null;
        final int imageWidth = image != null ? image.getWidth(null) : 0;
        final int imageHeight = image != null ? image.getHeight(null) : 0;

        IGraphPort[] ports = helper.getNodePorts(node);

        Point rightSidePoint = new Point((imageWidth / 2) + PORT_SPACE_2, -PORT_SPACE_2);
        if (ports != null) {
            for (int i = ports.length - 1; i >= 0; i--) {
                IGraphPort port = ports[i];
                boolean isDefault = port == defaultPort;
                Point loc;
                if (isDefault) {
                    loc = new Point(-imageWidth / 2 - PORT_SPACE_2, -imageHeight / 2);
                } else {
                    loc = new Point(rightSidePoint);
                    rightSidePoint.translate(0, -PORT_SPACE);
                }
                helper.setPortRelativeLocation(port, loc);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#layoutNode(org.netbeans.graph.api.model.IGraphNode, java.awt.Graphics2D)
     */
    public void layoutNode(IGraphNode node, Graphics2D gr) {

        String displayName = null;
        Image image = null;
        final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(IDisplayable.class);
        if (displayable != null) {
            displayName = displayable.getDisplayName();
            image = displayable.getIcon();
        }

        final int imageWidth = image != null ? image.getWidth(null) : 0;
        final int imageHeight = image != null ? image.getHeight(null) : 0;

        imageRect = new Rectangle(-imageWidth / 2, -imageHeight, imageWidth, imageHeight);

        textRect = null;
        if (displayName != null) {
            final FontMetrics fontMetrics = gr.getFontMetrics(font);
            final int ascent = fontMetrics.getAscent();

            textRect = fontMetrics.getStringBounds(displayName, gr).getBounds();
            textRect.width += 2;
            textRect.height += 2;
            textRect.translate(-textRect.width / 2, 2 + ascent);
        }

        // sanjeev
        // now set a new ractangle for the attributes..
        Rectangle attributeRect = new Rectangle(-imageWidth / 2, -imageHeight, imageWidth, imageHeight + 100);
        //Rectangle attributeRect = new Rectangle(imageWidth, imageHeight+100);
        // sanjeev

        layoutPortsLocationsCore(node, gr);

        Rectangle bounds = new Rectangle();
        if (textRect != null) {
            Rectangle2D.union(imageRect, textRect, bounds);
        } else {
            bounds.setRect(imageRect);
        }
        //sanjeev
        oldrect = bounds.getBounds();
        Rectangle2D.union(attributeRect, bounds, bounds);
        //sanjeev

        helper.includeNodePortsToNodeRelativeBounds(node, bounds);
        bounds.grow(4, 4);

        //helper.setNodeRelativeActiveAreas(node, new Rectangle[] { imageRect, textRect } );
        helper.setNodeRelativeActiveAreas(node, new Rectangle[] { imageRect, textRect, attributeRect }); // sanjeev
        helper.setNodeRelativeBounds(node, bounds);

    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#locationSuggested(org.netbeans.graph.api.model.IGraphNode, java.awt.Point)
     */
    public Point locationSuggested(IGraphNode node, Point suggestedLocation) {
        return suggestedLocation;
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#getLayers(org.netbeans.graph.api.model.IGraphNode)
     */
    public int[] getLayers(IGraphNode node) {
        return layers;
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#renderNode(org.netbeans.graph.api.model.IGraphNode, java.awt.Graphics2D, int)
     */
    public void renderNode(IGraphNode node, Graphics2D gr, int layer) {
        Point location = helper.getNodeLocation(node);

        if (layer == LAYER_BACKGROUND) {
            //sanjeev drawing a box covering the node..
            Rectangle rect = helper.getBounds(node);
            gr.setColor(new Color(0xEEEEEE));
            gr.fillRect(rect.x, rect.y, rect.width, rect.height);
            gr.setColor(new Color(0xCDCDCD));
            gr.draw(new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - 1, rect.height - 1));
            //sanjeev now drawing a ract for attributes..
            gr.draw(new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - 1, oldrect.height + 4));
            // now draw the attributes
            // sanjeev
            if (helper.isComponentSelected(node)) {
                SimpleDocumentRenderer.renderSelectedRect(gr, helper.getBounds(node));
            }
        } else if (layer == LAYER_NODE) {
            String displayName = null;
            Image image = null;
            final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(IDisplayable.class);
            if (displayable != null) {
                displayName = displayable.getDisplayName();
                image = displayable.getIcon();
            }

            if (image != null) {
                final Rectangle imageRect = new Rectangle(this.imageRect);
                imageRect.translate(location.x, location.y);
                final IGraphPort defaultPort = helper.getNodeDefaultPort(node);
                if (helper.isComponentMouseOver(node) || helper.isComponentMouseOver(defaultPort)) {
                    Composite composite = gr.getComposite();
                    gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
                    gr.drawImage(image, imageRect.x, imageRect.y, null);
                    gr.setComposite(composite);
                } else {
                    gr.drawImage(image, imageRect.x, imageRect.y, null);
                }
            }

            gr.setFont(font);

            if (displayName != null) {
                final FontMetrics fontMetrics = gr.getFontMetrics(font);
                final int ascent = fontMetrics.getAscent();
                final Rectangle textRect = new Rectangle(this.textRect);
                textRect.translate(location.x, location.y);
                final boolean selected = helper.isComponentSelected(node);

                if (selected) {
                    gr.setColor(colorFontShadowSelected);
                    gr.drawString(displayName, textRect.x + 1, textRect.y + 1 + ascent);
                } else {
                    gr.setColor(colorFontShadow);
                    gr.drawString(displayName, textRect.x + 2, textRect.y + 2 + ascent);
                }
                gr.setColor(colorFont);
                gr.drawString(displayName, textRect.x, textRect.y + ascent);
                //sanjeev
                gr.setColor(new Color(174, 100, 0)); // 
                drawAttributes(gr, node.getID(), helper.getBounds(node).x + 15, textRect.y + 1 + ascent + 20);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#getEditor(org.netbeans.graph.api.model.IGraphNode, java.awt.Point)
     */
    public IGraphEditor getEditor(IGraphNode node, Point position) {
        if (this.textRect == null) {
            return null;
        }
        final Rectangle rectangle = new Rectangle(this.textRect);
        final Point location = helper.getNodeLocation(node);
        rectangle.translate(location.x, location.y);
        final INameEditable nameEditable = (INameEditable) node.getLookup().lookup(INameEditable.class);
        if (rectangle.contains(position) && nameEditable != null && nameEditable.canRename()) {
            return new DagTextFieldEditor(rectangle, nameEditable);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#getToolTipText(org.netbeans.graph.api.model.IGraphNode, java.awt.Point)
     */
    public String getToolTipText(IGraphNode node, Point position) {
        final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(IDisplayable.class);
        return displayable != null ? displayable.getTooltipText() : null;
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#portLocationSuggested(org.netbeans.graph.api.model.IGraphPort, java.awt.Point)
     */
    public void portLocationSuggested(IGraphPort port, Point suggestedLocation) {
        // TODO
    }

    /**
     * 
     * @param gr
     * @param nodeName
     * @param xx
     * @param yy
     */
    private void drawAttributes(Graphics2D gr, String nodeName, int xx, int yy) {
        // keep x same and y increment with every text string
        // 
        Image imagePort = Utilities.loadImage("org/netbeans/graph/examples/control/resources/port.gif");
        gr.drawImage(imagePort, xx - 10, yy - 8, null);

        if (nodeName.startsWith("caCore.Gene")) {
            gr.drawString("id", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("clusterId", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("symbol", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);

        } else if (nodeName.startsWith("caCore.Taxon")) {
            gr.drawString("id", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("ethnicityStrain", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("commonName", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);

        } else if (nodeName.startsWith("caCore.Chromosome")) {
            gr.drawString("id", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("name", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);

        } else if (nodeName.startsWith("caCore.Clone")) {
            gr.drawString("id", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("insertSize", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("strain", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);

        } else if (nodeName.startsWith("caCore.Library")) {
            gr.drawString("id", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("type", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("unigeneId", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("keyword", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);

        } else if (nodeName.startsWith("caCore.Sequence")) {
            gr.drawString("id", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("accessionNumber", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("length", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("type", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);

        } else if (nodeName.startsWith("caCore.Target")) {
            gr.drawString("id", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("name", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("type", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);

        } else if (nodeName.startsWith("PIR.Taxon")) {
            gr.drawString("id", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);

        } else if (nodeName.startsWith("PIR.Gene")) {
            gr.drawString("id", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);

        } else if (nodeName.startsWith("PIR.ProteinSequence")) {
            gr.drawString("id", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("checksum", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("length", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);
            yy = yy + 20;
            gr.drawString("value", xx, yy);
            gr.drawImage(imagePort, xx - 10, yy - 8, null);

        }

    }
}
