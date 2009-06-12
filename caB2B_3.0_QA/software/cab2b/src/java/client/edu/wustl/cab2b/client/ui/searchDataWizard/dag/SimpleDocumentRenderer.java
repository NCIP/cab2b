package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.IGraphDocumentRenderer;
import org.netbeans.graph.api.control.IGraphLinkRenderer;
import org.netbeans.graph.api.control.IGraphLinkRouter;
import org.netbeans.graph.api.control.IGraphNodeRenderer;
import org.netbeans.graph.api.control.IGraphNodesLayouter;
import org.netbeans.graph.api.control.IGraphPortRenderer;
import org.netbeans.graph.api.control.builtin.AnimatedNodesLayouter;
import org.netbeans.graph.api.control.builtin.BreadthFirstNodesLayouter;
import org.netbeans.graph.api.control.builtin.OrthogonalLinkRouter;
import org.netbeans.graph.api.control.editor.IGraphEditor;
import org.netbeans.graph.api.model.IGraphLink;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.IGraphPort;

/**
 * SimpleDocumentRenderer
 * @author Deepak Shingan
 */
public class SimpleDocumentRenderer implements IGraphDocumentRenderer {
    /**
     * Set FONT for title 
     */
    protected static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 16); // NOI18N

    /**
     * Sets paper header default height
     */
    protected static final int PAPER_HEADER_DEFAULT_HEIGHT = 32;

    /**
     * Top panel color
     */
    protected static final Color TOP_PANEL_COLOR = new Color(255, 255, 255);

    /**
     * Top line color
     */
    protected static final Color TOP_LINE_COLOR = new Color(250, 232, 213);

    /**
     * Center panel color
     */
    protected static final Color CENTER_PANEL_COLOR = new Color(252, 250, 245);

    /**
     * Top Label color
     */
    protected static final Color TOP_LABEL_COLOR = new Color(213, 211, 204);

    /**
     * Node selection color 
     */
    protected static final Color SELECTION_BACKGROUND = new Color(0xEEEEEE);

    /**
     * Node Selection foreground color
     */
    protected static final Color SELECTION_FOREGROUND = Color.BLUE;

    /**
     * Background color
     */
    protected static final Color ALTERNATE_BACKGROUND = new Color(0xEE4040);

    /**
     * Foreground color
     */
    protected static final Color ALTERNATE_FOREGROUND = new Color(0xCD0000);

    /**
     * Grid image
     */
    static Image IMAGE_PAPER_GRID;

    /**
     * Grid image height 
     */
    static int IMAGE_PAPER_GRID_HEIGHT;

    /**
     * Grid paper width
     */
    static int IMAGE_PAPER_GRID_WIDTH;

    /**
     * Graph utility class object
     */
    protected GraphHelper helper;

    /**
     * Title for document
     */
    protected String title;

    /**
     * Image for port
     */
    private Image simpleDocPortImage;

    /**
     * Graph note layout 
     */
    private final IGraphNodesLayouter nodesLayouter = new AnimatedNodesLayouter(new BreadthFirstNodesLayouter() {
        protected IGraphNode getRootNode(GraphHelper helper) {
            final Object[] selectedComponents = helper.getSelectedComponents();
            if (selectedComponents != null && selectedComponents.length == 1
                    && selectedComponents[0] instanceof IGraphNode) {
                return (IGraphNode) selectedComponents[0];
            }
            final IGraphNode[] nodes = helper.getNodes();
            return nodes != null && nodes.length > 0 ? nodes[0] : null;
        }
    });

    /**
     * Router for Orthogonal Links
     */
    private final OrthogonalLinkRouter orthogonalLinkRouter = new OrthogonalLinkRouter();

    /**
     * Simple document renderer.
     * @param paperImage
     * @param portImage
     */
    public SimpleDocumentRenderer(Image paperImage, Image portImage) {
        IMAGE_PAPER_GRID = paperImage;
        IMAGE_PAPER_GRID_HEIGHT = IMAGE_PAPER_GRID.getHeight(null);
        IMAGE_PAPER_GRID_WIDTH = IMAGE_PAPER_GRID.getWidth(null);
        simpleDocPortImage = portImage;
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#notifyAttached(org.netbeans.graph.api.control.GraphHelper)
     */
    public void notifyAttached(GraphHelper helper) {
        this.helper = helper;
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#notifyDettached(org.netbeans.graph.api.control.GraphHelper)
     */
    public void notifyDettached(GraphHelper helper) {
        if (this.helper == helper) {
            this.helper = null;
        }
    }

    /**
     * Set title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method for rendering selected rectangle, handles changing background and recreating object
     * @param gr
     * @param rect
     */
    public static void renderSelectedRect(Graphics2D gr, Rectangle rect) {
        if (rect == null) {
            return;
        }
        gr.setColor(SELECTION_FOREGROUND);
        gr.draw(new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - 1, rect.height - 1));
    }

    /**
     * Method for rendering rectangle when not slected
     * @param gr
     * @param rect
     */
    public static void renderAltRect(Graphics2D gr, Rectangle rect) {
        if (rect == null) {
            return;
        }
        gr.setColor(ALTERNATE_BACKGROUND);
        gr.fillRect(rect.x, rect.y, rect.width, rect.height);
        gr.setColor(ALTERNATE_FOREGROUND);
        gr.draw(new Rectangle2D.Float(rect.x + 0.5f, rect.y + 0.5f, rect.width - 1, rect.height - 1));
    }

    /**
     * Gets selection background color
     * @return Color
     */
    public static Color getSelectionBackgroundColor() {
        return SELECTION_BACKGROUND;
    }

    /**
     * Gets selection foreground color
     * @return Color
     */
    public static Color getSelectionForegroundColor() {
        return SELECTION_FOREGROUND;
    }

    /**
     * Gets paper color
     * @return Color
     */
    public static Color getPaperColor() {
        return CENTER_PANEL_COLOR;
    }

    /**
     * Gets paper header height
     * @param gr
     * @return int
     */
    protected int getPaperHeaderHeight(Graphics2D gr) {
        return title != null ? PAPER_HEADER_DEFAULT_HEIGHT : -1;
    }

    /**
     * Creates Paper Header on graph document
     * @param gr
     */
    protected void renderPaperHeader(Graphics2D gr) {
        if (title != null) {
            gr.setColor(TOP_LABEL_COLOR);
            gr.setFont(TITLE_FONT);
            gr.drawString(title, 40, (3 * PAPER_HEADER_DEFAULT_HEIGHT) / 4);
            gr.setColor(TOP_PANEL_COLOR);
            Rectangle clip = gr.getClipBounds();
            gr.fillRect(clip.x, 0, clip.width, PAPER_HEADER_DEFAULT_HEIGHT + 1);
            gr.setColor(TOP_LINE_COLOR);
            gr.fillRect(clip.x, PAPER_HEADER_DEFAULT_HEIGHT, clip.width, PAPER_HEADER_DEFAULT_HEIGHT + 1);
        }
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#renderPaper(java.awt.Graphics2D)
     */
    public void renderPaper(Graphics2D gr) {
        Rectangle clip = gr.getClipBounds();
        AffineTransform at = gr.getTransform();

        if (helper.isSnapToGrid() && at.getScaleX() > 0.7 && at.getScaleY() > 0.7) {
            final int gridX = IMAGE_PAPER_GRID_WIDTH;
            final int gridY = IMAGE_PAPER_GRID_HEIGHT;

            int fromx = (clip.x / gridX) * gridX;
            int tox = ((clip.x + clip.width) / gridX + 1) * gridX;
            int fromy = (clip.y / gridY) * gridY;
            int toy = ((clip.y + clip.height) / gridY + 1) * gridY;

            for (int posy = fromy; posy <= toy; posy += gridY) {
                for (int posx = fromx; posx <= tox; posx += gridX) {
                    gr.drawImage(IMAGE_PAPER_GRID, posx, posy, null);
                }
            }
        } else {
            int paperTopHeight = getPaperHeaderHeight(gr);
            gr.setColor(CENTER_PANEL_COLOR);
            gr.fillRect(clip.x, /*imagePaperWidth + */paperTopHeight + 1, clip.width, clip.y + clip.height);
        }
        renderPaperHeader(gr);
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#renderSelectionRectangle(java.awt.Graphics2D, java.awt.Rectangle)
     */
    public void renderSelectionRectangle(Graphics2D gr, Rectangle selectionRectangle) {
        Composite composite = gr.getComposite();
        gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
        renderSelectedRect(gr, selectionRectangle);
        gr.setComposite(composite);
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#getRelinkingRectangle(org.netbeans.graph.api.model.IGraphLink, org.netbeans.graph.api.model.IGraphPort, org.netbeans.graph.api.model.IGraphPort, java.awt.Point, java.awt.Point)
     */
    public Rectangle getRelinkingRectangle(IGraphLink link, IGraphPort sourcePort, IGraphPort targetPort,
                                           Point sourcePoint, Point targetPoint) {
        if (sourcePoint == null || targetPoint == null) {
            return null;
        }
        Rectangle sourceRect = new Rectangle(sourcePoint);
        Rectangle targetRect = new Rectangle(targetPoint);
        Rectangle2D.union(sourceRect, targetRect, sourceRect);
        sourceRect.grow(8, 8);
        if (sourceRect.width < 32) {
            sourceRect.grow(16, 0);
        }
        if (sourceRect.height < 32) {
            sourceRect.grow(0, 16);
        }
        return sourceRect;
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#renderRelinking(java.awt.Graphics2D, org.netbeans.graph.api.model.IGraphLink, org.netbeans.graph.api.model.IGraphPort, org.netbeans.graph.api.model.IGraphPort, java.awt.Point, java.awt.Point)
     */
    public void renderRelinking(Graphics2D gr, IGraphLink link, IGraphPort sourcePort, IGraphPort targetPort,
                                Point sourcePoint, Point targetPoint) {
        if (sourcePoint == null || targetPoint == null) {
            int[] xs = new int[] { sourcePoint.x, targetPoint.x };
            int[] ys = new int[] { sourcePoint.y, targetPoint.y };
            gr.setColor(SimpleLinkRenderer.relinkingColor);
            SimpleLinkRenderer.paintLink(false, gr, xs, ys, 2);
        }
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#isAcceptable(java.awt.datatransfer.DataFlavor[], org.netbeans.graph.api.model.IGraphNode, java.awt.Point)
     */
    public boolean isAcceptable(DataFlavor[] dataFlavors, IGraphNode node, Point location) {
        return helper.getEventHandler().isAcceptable((IGraphNode) helper.getModelComponent(node), dataFlavors);
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#accept(java.awt.datatransfer.Transferable, org.netbeans.graph.api.model.IGraphNode, java.awt.Point)
     */
    public void accept(Transferable transferable, IGraphNode node, Point location) {
        helper.setNewNodePosition(location);
        helper.getEventHandler().accept((IGraphNode) helper.getModelComponent(node), transferable);
        helper.setNewNodePosition(null);
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#getEditor(java.awt.Point)
     */
    public IGraphEditor getEditor(Point position) {
        return null;
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#getToolTipText(java.awt.Point)
     */
    public String getToolTipText(Point position) {
        return null;
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#getNodesLayouter()
     */
    public IGraphNodesLayouter getNodesLayouter() {
        return nodesLayouter;
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#getNodeRenderer(org.netbeans.graph.api.model.IGraphNode)
     */
    public IGraphNodeRenderer getNodeRenderer(IGraphNode node) {
        return new IconNodeRenderer(helper, node);
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#getPortRenderer(org.netbeans.graph.api.model.IGraphPort)
     */
    public IGraphPortRenderer getPortRenderer(IGraphPort port) {
        return new IconPortRenderer(helper, port, simpleDocPortImage);
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#getLinkRenderer(org.netbeans.graph.api.model.IGraphLink)
     */
    public IGraphLinkRenderer getLinkRenderer(IGraphLink link) {
        return new OrthogonalLinkRouterLinkRenderer(helper, link, orthogonalLinkRouter);
    }

    /**
     * @see org.netbeans.graph.api.control.IGraphDocumentRenderer#getLinkRouter(org.netbeans.graph.api.model.IGraphLink)
     */
    public IGraphLinkRouter getLinkRouter(IGraphLink link) {
        return orthogonalLinkRouter;
    }
}
