package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.IGraphNodeRenderer;
import org.netbeans.graph.api.control.editor.IGraphEditor;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.IGraphPort;
import org.netbeans.graph.api.model.ability.IDisplayable;
import org.netbeans.graph.api.model.ability.INameEditable;

public class ClassNodeRenderer implements IGraphNodeRenderer {
    /**
     * Node font
     */
    public static final Font font = Font.decode("Times-bold").deriveFont(10.0f); // NOI18N

    /**
     * Font color
     */
    private static Color colorFont = new Color(0, 0, 0); // 174, 100

    /**
     * Space for port one
     */
    protected static int PORT_SPACE = 20; // sanjeev

    /**
     * Space for port two
     */
    protected static int PORT_SPACE_2 = 8;

    /**
     * Layer background
     */
    private static final int LAYER_BACKGROUND = 100;

    /**
     * Layer node
     */
    private static final int LAYER_NODE = 400;

    /**
     * Array of layers
     */
    private int[] layers = new int[] { LAYER_BACKGROUND, LAYER_NODE };

    /**
     * Helper graph utility class
     */
    private GraphHelper helper;

    /**
     * Text rectangle
     */
    protected Rectangle nodeTextRect;

    /**
     * Option box rectangle on node
     */
    private Rectangle optionNodeRect;

    /**
     * Node min max rectangle
     */
    private Rectangle maxMinRectangle;

    /**
     * Rectangle for showing node index number
     */
    private Rectangle numberRectangle;

    /**
     * Rectangle for showing associations node number
     */
    private Rectangle[] assRectangles;

    /**
     * Tool tip text
     */
    private String toolTipName;

    /**
     * Set max-min width and height
     */
    private final int maxMinLength = 15;

    /**
     * Flag to check whether node is minimized or not
     */
    private boolean nodeMinimized = false;

    /**
     * Option polygone
     */
    private Polygon optionPolygon;

    /**
     * SET index of association clicked
     */
    private int associationIndx = -1;

    /**
     * Flag to identify is node created for view
     */
    private boolean isForView = false;

    /**
     * Constructor
     * @param helper
     * @param node
     * @param isForView
     */
    public ClassNodeRenderer(GraphHelper helper, IGraphNode node, boolean isForView) {
        this.helper = helper;
        this.isForView = isForView;
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#getEditor(org.netbeans.graph.api.model.IGraphNode, java.awt.Point)
     */
    public IGraphEditor getEditor(IGraphNode node, Point position) {
        if (nodeTextRect == null) {
            return null;
        }
        final Rectangle rectangle = new Rectangle(nodeTextRect);
        final Point location = helper.getNodeLocation(node);
        rectangle.translate(location.x, location.y);
        final INameEditable nameEditable = (INameEditable) node.getLookup().lookup(INameEditable.class);
        if (rectangle.contains(position) && nameEditable != null && nameEditable.canRename()) {
            return new DagTextFieldEditor(rectangle, nameEditable);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#getLayers(org.netbeans.graph.api.model.IGraphNode)
     */
    public int[] getLayers(IGraphNode arg0) {
        return layers;
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#getToolTipText(org.netbeans.graph.api.model.IGraphNode, java.awt.Point)
     */
    public String getToolTipText(IGraphNode node, Point arg1) {
        final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(IDisplayable.class);
        return displayable != null ? displayable.getTooltipText() : null;
    }

    /**
     * Method to layout minimized node
     * 
     * @param node
     *            Node for which to perform layout
     * @param gr
     *            Graphics2D object associated with this node renderer
     */
    private void layoutMinimizedNode(IGraphNode node, Graphics2D gr) {
        setRectangleFontMatrics(node, gr);
        String displayName = getDisplayNameForFontMatrics(node);
        if (displayName.length() <= 5) {
            nodeTextRect.width += 20;
        } else {
            nodeTextRect.width += 10;
        }
        nodeTextRect.height += 10;
        nodeTextRect.x = numberRectangle.x + numberRectangle.width;
        nodeTextRect.y = numberRectangle.y;

        // MaxMin rectangle details
        maxMinRectangle = new Rectangle();
        maxMinRectangle.width = maxMinRectangle.height = maxMinLength;
        maxMinRectangle.x = nodeTextRect.x + nodeTextRect.width;
        maxMinRectangle.y = nodeTextRect.y + 5;

        Rectangle bounds = new Rectangle();
        Rectangle2D.union(numberRectangle, nodeTextRect, bounds);
        Rectangle2D.union(maxMinRectangle, bounds, bounds);
        layoutNodeCommonUpdate(node, bounds);
    }

    /**
     * Updates node layout
     * @param node
     * @param bounds
     */
    private void layoutNodeCommonUpdate(IGraphNode node, Rectangle bounds) {
        ClassNode classNode = (ClassNode) (node.getLookup().lookup(ClassNode.class));
        java.util.List<IGraphPort> associationList = classNode.getSourcePorts();
        for (int i = 0; i < associationList.size(); i++) {
            IGraphPort port = associationList.get(i);
            helper.setPortRelativeLocation(port, new Point(maxMinRectangle.x + maxMinRectangle.width + 5,
                    maxMinRectangle.y + 5));
        }
        java.util.List<IGraphPort> targetPortList = classNode.getTargetPortList();
        for (int i = 0; i < targetPortList.size(); i++) {
            helper.setPortRelativeLocation(targetPortList.get(i), new Point(numberRectangle.x - 5,
                    numberRectangle.y + 5));
        }
        helper.includeNodePortsToNodeRelativeBounds(node, bounds);
        bounds.grow(4, 4);
        helper.setNodeRelativeActiveAreas(node, new Rectangle[] { bounds, nodeTextRect, maxMinRectangle });
        helper.setNodeRelativeBounds(node, bounds);

    }

    /**
     * Method to layout maximized node
     * 
     * @param node
     *            Node for which to perform layout
     * @param gr
     *            Graphics2D object associated with this node renderer
     */
    private void layoutMaximizedNode(IGraphNode node, Graphics2D gr) {
        final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(IDisplayable.class);
        String displayName = displayable.getDisplayName();
        setNameForToolTip(displayName);
        if (displayName.indexOf("(") != -1) {
            displayName = displayName.substring(0, displayName.indexOf("("));
        }

        FontMetrics fontMetrics = gr.getFontMetrics(font);

        // Node number rectangle setup
        numberRectangle = fontMetrics.getStringBounds(node.getID(), gr).getBounds();
        numberRectangle.width += 10;
        numberRectangle.height += 5;

        // set Target ports
        ClassNode classNode = (ClassNode) (node.getLookup().lookup(ClassNode.class));
        java.util.List<IGraphPort> targetPortList = classNode.getTargetPortList();
        for (int i = 0; i < targetPortList.size(); i++) {
            helper.setPortRelativeLocation(targetPortList.get(i), new Point(numberRectangle.x - 5,
                    numberRectangle.y + 5));
        }

        // Class name area setup
        nodeTextRect = fontMetrics.getStringBounds(displayName, gr).getBounds();
        if (displayName.length() <= 5) {
            nodeTextRect.width += 20;
        } else {
            nodeTextRect.width += 10;
        }
        layoutNodeRect(fontMetrics, gr);

        // If node has associations to display
        // Render association part of the node
        assRectangles = null;
        layoutAssociations(node, gr);

        Rectangle bounds = new Rectangle();
        Rectangle2D.union(nodeTextRect, optionNodeRect, bounds);
        Rectangle2D.union(bounds, maxMinRectangle, bounds);
        Rectangle2D.union(bounds, numberRectangle, bounds);
        Rectangle[] allActiveRectangles;
        if (assRectangles != null) {
            allActiveRectangles = new Rectangle[assRectangles.length + 3];
            for (int i = 0; i < assRectangles.length; i++) {
                Rectangle2D.union(bounds, assRectangles[i], bounds);
            }
        } else {
            allActiveRectangles = new Rectangle[3];
        }
        // Add common rectangles
        allActiveRectangles[0] = bounds;
        allActiveRectangles[1] = optionNodeRect;
        allActiveRectangles[2] = maxMinRectangle;

        if (assRectangles != null) {
            for (int i = 0; i < assRectangles.length; i++) {
                allActiveRectangles[i + 3] = assRectangles[i];
            }
        }
        helper.includeNodePortsToNodeRelativeBounds(node, bounds);
        bounds.grow(4, 4);
        helper.setNodeRelativeActiveAreas(node, allActiveRectangles);
        helper.setNodeRelativeBounds(node, bounds);
    }

    /**
     * Method to layout associations of nodes
     * 
     * @param associationList
     *            List of associations to layout
     * @param gr
     *            Graphics2D object associated with this node renderer
     */
    private void layoutAssociations(IGraphNode node, Graphics2D gr) {
        ClassNode classNode = (ClassNode) (node.getLookup().lookup(ClassNode.class));
        java.util.List<IGraphPort> associationList = classNode.getSourcePorts();
        if (associationList.size() == 0) {
            return;
        }
        assRectangles = new Rectangle[associationList.size() + 1];
        // Common condtion portion where attribute conditions will be logically
        // ANDED / ORED will association
        // conditions
        assRectangles[0] = new Rectangle();
        assRectangles[0].width = nodeTextRect.width;
        assRectangles[0].height = nodeTextRect.height;
        assRectangles[0].translate(nodeTextRect.x, optionNodeRect.y + optionNodeRect.height);

        for (int i = 1; i <= associationList.size(); i++) {
            assRectangles[i] = new Rectangle();
            assRectangles[i].width = nodeTextRect.width;
            assRectangles[i].height = nodeTextRect.height;
            assRectangles[i].translate(nodeTextRect.x, assRectangles[i - 1].height + assRectangles[i - 1].y);
            IGraphPort port = associationList.get(i - 1);
            helper.setPortRelativeLocation(port, new Point(assRectangles[i].x + assRectangles[i].width + 5,
                    assRectangles[i].y + 5));
        }
    }

    /**
     * Sets Font metrics for rectangle
     * @param node
     * @param gr
     * @return FontMetrics
     */
    private FontMetrics setRectangleFontMatrics(IGraphNode node, Graphics2D gr) {
        FontMetrics fontMetrics = gr.getFontMetrics(font);

        // Node number rectangle setup
        numberRectangle = fontMetrics.getStringBounds(node.getID(), gr).getBounds();
        numberRectangle.width += 10;
        numberRectangle.height += 5;

        // Class name area setup
        String displayName = getDisplayNameForFontMatrics(node);
        nodeTextRect = fontMetrics.getStringBounds(displayName, gr).getBounds();

        return fontMetrics;
    }

    /**
     * Returns displayname with FontMatrics 
     * @param node
     * @return
     */
    private String getDisplayNameForFontMatrics(IGraphNode node) {
        //      Class name area setup
        String displayName = ((IDisplayable) node.getLookup().lookup(IDisplayable.class)).getDisplayName();
        if (displayName.indexOf("(") != -1) {
            displayName = displayName.substring(0, displayName.indexOf("("));
        }
        return displayName;
    }

    /**
     * Method to layout maximized node
     * 
     * @param node
     *            Node for which to perform layout
     * @param gr
     *            Graphics2D object associated with this node renderer
     */
    private void layoutDefineViewNode(IGraphNode node, Graphics2D gr) {
        FontMetrics fontMetrics = setRectangleFontMatrics(node, gr);
        nodeTextRect.width += 10;
        layoutNodeRect(fontMetrics, gr);
        Rectangle bounds = new Rectangle();
        Rectangle2D.union(numberRectangle, nodeTextRect, bounds);
        Rectangle2D.union(maxMinRectangle, bounds, bounds);
        Rectangle2D.union(optionNodeRect, bounds, bounds);
        layoutNodeCommonUpdate(node, bounds);
    }

    /**
     * Called to set rectangle layout
     * @param fontMetrics
     * @param gr
     */
    private void layoutNodeRect(FontMetrics fontMetrics, Graphics2D gr) {

        nodeTextRect.height += 10;
        nodeTextRect.x = numberRectangle.x + numberRectangle.width;
        nodeTextRect.y = numberRectangle.y;

        // MaxMin rectangle details
        maxMinRectangle = new Rectangle();
        maxMinRectangle.width = maxMinRectangle.height = maxMinLength;
        maxMinRectangle.x = nodeTextRect.x + nodeTextRect.width;
        maxMinRectangle.y = nodeTextRect.y + 5;

        // Option area setup
        optionNodeRect = fontMetrics.getStringBounds("Options", gr).getBounds();
        optionNodeRect.width += 10;
        optionNodeRect.height += 10;
        optionNodeRect.translate(nodeTextRect.width / 2, nodeTextRect.height);
    }

    /**
     * Called to layout a node. This means to resolve the relative active areas
     * and the relative bounds of the node. Optionally the relative location of
     * the node ports also. Use GraphHelper.setNodeRelativeBounds,
     * GraphHelper.setNodeRelativeActiveAreas, and optionally
     * GraphHelper.setPortRelativeLocation methods. <p/> Note: The node relative
     * bounds must include the node ports bounds. This could be done by calling
     * GraphHelper.includePortsToNodeRelativeBounds method. <p/> Note: The node
     * do not have resolved location, active areas, and bounds yet.
     * 
     * @param node
     *            the proxy-node
     * @param gr
     *            the graphics
     */
    public void layoutNode(IGraphNode node, Graphics2D gr) {
        if (true == nodeMinimized) {
            layoutMinimizedNode(node, gr);
        } else {
            if (isForView) {
                layoutDefineViewNode(node, gr);
            } else {
                this.layoutMaximizedNode(node, gr);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#locationSuggested(org.netbeans.graph.api.model.IGraphNode, java.awt.Point)
     */
    public Point locationSuggested(IGraphNode arg0, Point arg1) {
        // TODO Auto-generated method stub
        return arg1;
    }

    /* (non-Javadoc)
     * @see org.netbeans.graph.api.control.IGraphNodeRenderer#portLocationSuggested(org.netbeans.graph.api.model.IGraphPort, java.awt.Point)
     */
    public void portLocationSuggested(IGraphPort arg0, Point arg1) {
        // TODO Auto-generated method stub
    }

    /**
     * Repaint selected node
     * @param node
     * @param gr
     * @param rect
     */
    private void renderNodeSelection(IGraphNode node, Graphics2D gr, Rectangle rect) {
        Rectangle2D rect2D;
        final boolean selected = helper.isComponentSelected(node);
        if (true == selected) {
            gr.setColor(new Color(0x0000FF));
            for (int i = 0; i < 2; i++) {
                rect2D = new Rectangle2D.Float(rect.x + i, rect.y + i, rect.width - i, rect.height - i);
                gr.draw(rect2D);
                rect2D = new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - i, rect.height - i);
                gr.draw(rect2D);
            }
        } else {
            gr.setColor(new Color(0x000000));
            rect2D = new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - 1, rect.height - 1);
            gr.draw(rect2D);
        }
    }

    /**
     * Method to render minimized node
     * 
     * @param node
     *            GraphNode to render
     * @param gr
     *            Graphics2D object associated with this node renderer
     * @param layer
     *            Layer of the node to render
     */
    private void renderMinimizedNode(IGraphNode node, Graphics2D gr, int layer) {
        // Get the node bounds
        Rectangle rect = helper.getBounds(node);

        if (layer == LAYER_BACKGROUND) {
            ClassNode classNode = (ClassNode) node.getLookup().lookup(ClassNode.class);
            // 1. Fill class name rectangle
            if (classNode.getType().equals(ClassNodeType.ConstraintOnlyNode)) {
                gr.setColor(new Color(0xFFFFAA));
            } else if (classNode.getType().equals(ClassNodeType.ViewOnlyNode)) {
                gr.setColor(Color.pink);
            } else {
                gr.setColor(Color.orange);
            }
            gr.fillRect(rect.x, rect.y, rect.width, rect.height);
            renderNodeSelection(node, gr, rect);
        } else if (layer == LAYER_NODE) {
            String displayName = null;
            final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(IDisplayable.class);
            displayName = displayable.getDisplayName();
            if (displayName.indexOf("(") != -1) {
                displayName = displayName.substring(0, displayName.indexOf("("));
            }
            gr.setFont(font);

            // show node index rectangle
            ClassNode classNode = (ClassNode) node.getLookup().lookup(ClassNode.class);
            IGraphPort[] ports = classNode.getPorts();
            if ((ports != null) && (ports.length > 0)) {
                gr.setColor(new Color(0x85E30C));
            } else {
                gr.setColor(new Color(0xFF6666));
            }
            renderNodeSetRectangle(gr, node, displayName);
        }
    }

    /**
     * Renders node background color and graphics
     * @param classNode
     * @param gr
     * @param rect
     */
    private void renderNodeBackGround(ClassNode classNode, Graphics2D gr, Rectangle rect) {
        if (classNode.getType().equals(ClassNodeType.ConstraintOnlyNode)) {
            gr.setColor(new Color(0xFFFFAA));
        } else if (classNode.getType().equals(ClassNodeType.ViewOnlyNode)) {
            gr.setColor(Color.pink);
        } else {
            gr.setColor(Color.orange);
        }

        gr.fillRect(rect.x, rect.y, rect.width, rect.height);

        // 1. Draw border for class name rectangle
        gr.setColor(new Color(0x000000));
        Rectangle2D rect2D = new Rectangle2D.Float(rect.x, rect.y, rect.width - 1, nodeTextRect.height);
        gr.draw(rect2D);

        // 2. Fill Option rectangle and draw border for same
        gr.setColor(new Color(0xDBDBDB));
        gr.fillRect(rect.x, rect.y + nodeTextRect.height, rect.width, optionNodeRect.height);
        gr.setColor(new Color(0x000000));
        rect2D = new Rectangle2D.Float(rect.x, rect.y + nodeTextRect.height, rect.width - 1, optionNodeRect.height);
        gr.draw(rect2D);
    }

    /**
     * Method to render miximized node
     * 
     * @param node
     *            GraphNode to render
     * @param gr
     *            Graphics2D object associated with this node renderer
     * @param layer
     *            Layer of the node to render
     */
    private void renderMaximizedNode(IGraphNode node, Graphics2D gr, int layer) {
        Point location = helper.getNodeLocation(node);

        // If node has associations to display
        ClassNode classNode = (ClassNode) node.getLookup().lookup(ClassNode.class);
        java.util.List<IGraphPort> associationList = classNode.getSourcePorts();

        // Get the node bounds
        Rectangle rect = helper.getBounds(node);

        if (layer == LAYER_BACKGROUND) {
            // Fill the complete rectangle
            renderNodeBackGround(classNode, gr, rect);

            // If node has associations show them
            if (associationList.size() > 0) {
                gr.setColor(new Color(0xF5F5F5));
                gr.fillRect(rect.x, rect.y + nodeTextRect.height + optionNodeRect.height, rect.width,
                            assRectangles[0].height);
                gr.setColor(new Color(0x000000));
                Rectangle2D rect2D = new Rectangle2D.Float(rect.x, rect.y + nodeTextRect.height
                        + optionNodeRect.height, rect.width - 1, assRectangles[0].height);
                gr.draw(rect2D);
                rect2D = new Rectangle2D.Float(rect.x + 1, rect.y + nodeTextRect.height + optionNodeRect.height
                        + 1, rect.width - 2, assRectangles[0].height - 2);
                gr.draw(rect2D);
                int currY = rect.y + nodeTextRect.height + optionNodeRect.height;
                for (int i = 1; i < assRectangles.length; i++) {
                    gr.setColor(new Color(0xF5F5F5));
                    gr.fillRect(rect.x, currY + (i * assRectangles[i - 1].height), rect.width,
                                assRectangles[i].height);
                    gr.setColor(new Color(0x000000));
                    rect2D = new Rectangle2D.Float(rect.x, currY + (i * assRectangles[i - 1].height),
                            rect.width - 1, assRectangles[i].height);
                    gr.draw(rect2D);
                }
            }
            renderNodeSelection(node, gr, rect);
        } else if (layer == LAYER_NODE) {
            String displayName = null;
            final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(IDisplayable.class);
            displayName = displayable.getDisplayName();
            if (displayName.indexOf("(") != -1) {
                displayName = displayName.substring(0, displayName.indexOf("("));
            }
            gr.setFont(font);

            // show node index rectangle
            IGraphPort[] ports = classNode.getPorts();
            if ((ports != null) && (ports.length > 0)) {
                gr.setColor(new Color(0x85E30C));
            } else {
                gr.setColor(new Color(0xFF6666));
            }
            renderNodeUpdatePolygon(gr, node, displayName);
            // Render association related information
            final Rectangle numRect = new Rectangle(numberRectangle);
            final Rectangle optionRect = new Rectangle(optionNodeRect);
            optionRect.translate(location.x, location.y);
            if (associationList.size() > 0) {
                numRect.y = location.y + nodeTextRect.height + optionRect.height;
                drawAssociation(classNode, gr, location.x, numRect.y, associationList);
            }
        }
    }

    /**
     * Method to render miximized node
     * 
     * @param node
     *            GraphNode to render
     * @param gr
     *            Graphics2D object associated with this node renderer
     * @param layer
     *            Layer of the node to render
     */
    private void renderDefineViewNode(IGraphNode node, Graphics2D gr, int layer) {
        // Get the node bounds
        Rectangle rect = helper.getBounds(node);

        if (layer == LAYER_BACKGROUND) {
            ClassNode classNode = (ClassNode) node.getLookup().lookup(ClassNode.class);
            // 1. Fill class name rectangle
            renderNodeBackGround(classNode, gr, rect);
            renderNodeSelection(node, gr, rect);
        } else if (layer == LAYER_NODE) {
            String displayName = null;
            final IDisplayable displayable = (IDisplayable) node.getLookup().lookup(IDisplayable.class);
            displayName = displayable.getDisplayName();
            gr.setFont(font);

            // show node index rectangle
            ClassNode classNode = (ClassNode) node.getLookup().lookup(ClassNode.class);
            IGraphPort[] ports = classNode.getPorts();
            if ((ports != null) && (ports.length > 0)) {
                gr.setColor(new Color(0x85E30C));
            } else {
                gr.setColor(new Color(0xFF6666));
            }
            renderNodeUpdatePolygon(gr, node, displayName);
        }
    }

    /**
     * Renders node
     * @param gr
     * @param node
     * @param displayName
     */
    private void renderNodeSetRectangle(Graphics2D gr, IGraphNode node, String displayName) {
        final int ascent = gr.getFontMetrics(font).getAscent();
        Point location = helper.getNodeLocation(node);
        final Rectangle numRect = new Rectangle(numberRectangle);

        numRect.translate(location.x, location.y);
        gr.fillRect(numRect.x, numRect.y, numRect.width, numRect.height - 3);
        gr.setColor(Color.BLACK);
        gr.drawRect(numRect.x, numRect.y, numRect.width, numRect.height - 3);
        gr.setColor(colorFont);
        gr.drawString(node.getID(), numRect.x + 5, numRect.y + 1 + ascent);

        // show class name rectangle
        int currX = numRect.x + numRect.width;
        int currY = numRect.y;
        final Rectangle textRect = new Rectangle(this.nodeTextRect);
        gr.setColor(colorFont);
        gr.drawString(displayName, currX + 3, currY + 3 + ascent);

        // Draw max-min window rectangle and inner portion of same
        gr.setColor(new Color(0x808080));
        gr.fillRect(currX + textRect.width, currY + 3, maxMinLength, maxMinLength);

        // draw border for maxMin Window
        gr.setColor(new Color(0x000000)); // border color..
        gr.draw(new Rectangle2D.Float(currX + textRect.width, currY + 3, maxMinLength, maxMinLength));
        gr.setColor(new Color(0xFFFFFF));
        gr.fillRect(currX + textRect.width + maxMinLength / 4, currY + 3 + 3 * maxMinLength / 4,
                    3 * maxMinLength / 4, maxMinLength / 4);

    }

    /**
     * Updates Option box from node
     * @param gr
     * @param node
     * @param displayName
     */
    private void renderNodeUpdatePolygon(Graphics2D gr, IGraphNode node, String displayName) {
        renderNodeSetRectangle(gr, node, displayName);

        // show option rectangle
        final Rectangle optionRect = new Rectangle(optionNodeRect);
        Rectangle rect = helper.getBounds(node);
        Point location = helper.getNodeLocation(node);
        final int ascent = gr.getFontMetrics(font).getAscent();
        optionRect.translate(location.x, location.y);
        gr.setColor(colorFont);
        gr.drawString("Options", optionRect.x, optionRect.y + ascent);

        // show tringle in front of option string
        optionPolygon = new Polygon();
        optionPolygon.addPoint(0, 0);
        optionPolygon.addPoint(0, 14);
        optionPolygon.addPoint(7, 7);
        gr.setColor(Color.black);
        optionPolygon.translate(rect.x + rect.width - 20, optionRect.y + 3);
        gr.fillPolygon(optionPolygon);
    }

    /**
     * Method to draw association boxes in node
     * 
     * @param gr
     *            Graphics2D object associated with this node renderer
     * @param currX
     *            Current X location where association box has to render
     * @param currY
     *            Current y location where association box has to render
     * @param associationList
     *            List of associations to draw on node
     */
    private void drawAssociation(ClassNode classNode, Graphics2D gr, int currX, int currY,
                                 java.util.List<IGraphPort> associationList) {
        final FontMetrics fontMetrics = gr.getFontMetrics(font);
        final int ascent = fontMetrics.getAscent();

        // Show And / OR string
        drawCustomCombo(classNode.getOperatorBetAttrAndAss(), currX, currY, gr);
        for (int i = 1; i < assRectangles.length; i++) {
            currY += assRectangles[i - 1].height;

            int expId = classNode.getLinkForSourcePort(associationList.get(i - 1)).getDestinationExpressionId();
            Rectangle assRect = fontMetrics.getStringBounds(String.valueOf(expId), gr).getBounds();
            assRect.width += 10;
            assRect.height += 4;
            assRect.translate(currX, currY);
            gr.setColor(Color.WHITE);
            gr.fillRect(assRect.x, assRect.y, assRect.width, assRect.height);
            gr.setColor(Color.BLACK);
            gr.drawRect(assRect.x, assRect.y, assRect.width, assRect.height);
            gr.setColor(colorFont);
            gr.drawString(String.valueOf(expId), assRect.x + 5, assRect.y + 3 + ascent);
            if (i < assRectangles.length - 1) {
                drawCustomCombo(classNode.getLogicalOperator(associationList.get(i)), currX, currY, gr);
            }
        }
    }

    /**
     * Method to draw custom drop-down boxes for added associations
     * 
     * @param str
     *            String to draw in drop downn
     * @param x
     *            X location for drop-down
     * @param y
     *            Y location for drop-down
     * @param gr
     *            Graphics2D object associated with this node renderer
     */
    private void drawCustomCombo(String str, int x, int y, Graphics2D gr) {
        int drawWidth;
        final FontMetrics fontMetrics = gr.getFontMetrics(font);
        Rectangle assRect = fontMetrics.getStringBounds(str, gr).getBounds();
        assRect.width += 10;
        assRect.height += 4;

        Rectangle comboRect = new Rectangle();
        comboRect.width = 10;
        comboRect.height = assRect.height;

        Polygon polygon = new Polygon();
        polygon.addPoint(0, 0);
        polygon.addPoint(10, 0);
        polygon.addPoint(5, 10);

        // Calculation for making comboboxes right aligned
        drawWidth = assRect.width + comboRect.width;
        int traslateX = nodeTextRect.width - drawWidth;
        assRect.translate(x + traslateX, y);
        gr.setColor(Color.WHITE);
        gr.fill(assRect);
        gr.setColor(Color.BLACK);
        gr.draw(assRect);
        gr.drawString(str, assRect.x + 5, y + 3);

        comboRect.translate(assRect.x + assRect.width, assRect.y);
        gr.setColor(Color.LIGHT_GRAY);
        gr.fill(comboRect);
        gr.setColor(Color.BLACK);
        gr.draw(comboRect);

        gr.setColor(Color.WHITE);
        polygon.translate(assRect.x + assRect.width, assRect.y + 5);
        gr.fillPolygon(polygon);
    }

    /**
     * 
     * Called to render a layer of a node. This method is always called after
     * layoutNode method.
     * 
     */
    public void renderNode(IGraphNode node, Graphics2D gr, int layer) {
        if (true == nodeMinimized) {
            renderMinimizedNode(node, gr, layer);
        } else {
            if (isForView) {
                renderDefineViewNode(node, gr, layer);
            } else {
                renderMaximizedNode(node, gr, layer);
            }
        }
    }

    /**
     * Method to check if option show button is clicked
     * 
     * @param node
     *            The node for which to check the click
     * @param point
     *            the mouse click location
     * @return true if option button is clicked else return's false
     */
    public boolean isOptionPopupShow(IGraphNode node, Point point) {
        if (false == nodeMinimized) {
            // Check if user has clicked options button
            if (true == optionPolygon.contains(point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check if maxmin rectangle has been clicked. This checks
     * wheather to minimize or maximize the node in selection
     * 
     * @param node
     *            Node for which to check max-min rectangle
     * @param point
     *            Point location of mouse click
     * @return
     */
    public boolean isMaxMinClick(IGraphNode node, Point point) {
        Point nodePosition = helper.getNodeLocation(node);
        // Check if minmax is clicked
        Point tempPoint = new Point();
        tempPoint.x = point.x - nodePosition.x;
        tempPoint.y = point.y - nodePosition.y;
        if (true == maxMinRectangle.contains(tempPoint)) {
            nodeMinimized = !nodeMinimized;
            return true;
        }
        return false;
    }

    /**
     * Method to check if association dropdown is being checked
     * 
     * @param node
     *            Node for which to check if association dropdown is checked
     * @param point
     *            where mouse has been clicked
     * @return true if association rectangle has been clicked else return false
     */
    public boolean isAssociationPopupShow(IGraphNode node, Point point) {
        Point nodePosition = helper.getNodeLocation(node);
        Point tempPoint = new Point();
        tempPoint.x = point.x - nodePosition.x;
        tempPoint.y = point.y - nodePosition.y;
        if (false == nodeMinimized) {
            // Check if use has clicked options button
            if (assRectangles != null) {
                for (int i = 0; i < assRectangles.length - 1; i++) {
                    if (true == assRectangles[i].contains(tempPoint)) {
                        associationIndx = i;
                        return true;
                    }
                }
            }
        }
        associationIndx = -1;
        return false;
    }

    /**
     * Get selected association index
     * 
     * @return the index of selected association
     */
    public int getSelectedAssocitationIdx() {
        return associationIndx;
    }

    /**
     * Gets tool tip text
     * @return String 
     */
    public String getNameForToolTip() {

        return toolTipName;
    }

    /**
     * Sets tool tip text
     * @param name
     */
    public void setNameForToolTip(String name) {
        toolTipName = name;
    }

    /**
     * Method to get location of Popup to show
     * 
     * @param node
     * @return
     */
    public Point getLocationOfPopup(IGraphNode node) {
        Point nodePosition = helper.getNodeLocation(node);
        Point p = new Point();
        p.x = nodePosition.x + nodeTextRect.width;
        p.y = nodePosition.y + nodeTextRect.height + optionNodeRect.height;
        for (int i = 0; i < associationIndx; i++) {
            p.y += assRectangles[i].height;
        }
        p.y += 7;
        return p;
    }
}
