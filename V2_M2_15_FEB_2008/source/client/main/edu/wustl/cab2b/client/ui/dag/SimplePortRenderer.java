package edu.wustl.cab2b.client.ui.dag;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.IGraphPortRenderer;
import org.netbeans.graph.api.control.editor.IGraphEditor;
import org.netbeans.graph.api.control.editor.TextFieldEditor;
import org.netbeans.graph.api.model.IGraphPort;
import org.netbeans.graph.api.model.ability.IDirectionable;
import org.netbeans.graph.api.model.ability.IDisplayable;
import org.netbeans.graph.api.model.ability.INameEditable;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public final class SimplePortRenderer implements IGraphPortRenderer 
{

    private static Image imagePort ; // NOI18N
    private static  int portHeight;
    private static  int portWidth ;

    private static Font fontNormal = Font.decode ("dialog").deriveFont (10.0f); // NOI18N

    private static int TEXT_GAP = 10;

    private static final int LAYER_PORT = 300;
    private static final int LAYER_ALT_MODE = 500;
    static final int[] layers = new int[] { LAYER_PORT, LAYER_ALT_MODE };

    private GraphHelper helper;

    private boolean visible;
    private int alignment; // overriden by IOrderable ability

    private Rectangle portRect = new Rectangle ();
    private Rectangle textRect = new Rectangle ();

    public SimplePortRenderer (GraphHelper helper, IGraphPort port, Image portImage) 
    {
    	this (helper, port, true, IDirectionable.RIGHT, portImage);
    }

    public SimplePortRenderer (GraphHelper helper, IGraphPort port, boolean visible, int alignment, Image portImage) 
    {
        this.helper = helper;
        this.visible = visible;
        this.alignment = alignment;
        imagePort = portImage;
    	portHeight = imagePort.getHeight (null);
    	portWidth = imagePort.getWidth (null);
    }

    public void layoutPort (IGraphPort port, Graphics2D gr) 
    {
        if (visible)
            portRect.setBounds (- portWidth / 2, - portHeight / 2, portWidth, portHeight);
        else
            portRect.setBounds (0, 0, 0, 0);

        final IDisplayable displayable = (IDisplayable) port.getLookup ().lookup (IDisplayable.class);
        String dn = displayable != null ? displayable.getDisplayName ()  : null;

        Rectangle bou = new Rectangle (portRect);

        if (visible  &&  dn != null) {
            FontMetrics fontMetrics = gr.getFontMetrics (fontNormal);
            int ascent = fontMetrics.getAscent ();
            Rectangle textRectangle = textRect;
            textRectangle.setRect (fontMetrics.getStringBounds (dn, gr).getBounds ());
            textRectangle.width += 1;
            textRectangle.height += 1;
            if (alignment == IDirectionable.RIGHT)
                textRectangle.translate (TEXT_GAP, ascent - textRectangle.height / 2);
            else
                textRectangle.translate (- TEXT_GAP - textRectangle.width, ascent - textRectangle.height / 2);
            Rectangle2D.union (textRectangle, bou, bou);
        }

        Rectangle actArea = new Rectangle (portRect);
        actArea.grow (3, 3);
        helper.setPortRelativeActiveAreas (port, new Rectangle[] { actArea } );
        helper.setPortRelativeBounds (port, bou);
    }

    public int[] getLayers (IGraphPort port)
    {
        return layers;
    }

    public void renderPort (IGraphPort port, Graphics2D gr, int layer)
    {
        final IDisplayable displayable = (IDisplayable) port.getLookup ().lookup (IDisplayable.class);
        String dn = displayable != null ? displayable.getDisplayName () : null;

        Point location = helper.getPortLocation (port);
        gr.translate (location.x, location.y);
        
        if (layer == LAYER_PORT  &&  visible)
        {
            if (helper.isComponentMouseOver (port))
            {
                SimpleDocumentRenderer.renderSelectedRect (gr, portRect);
            }
            else 
            {
                final Rectangle rect = portRect;
                gr.drawImage (imagePort, rect.x, rect.y, null);
            }

            if (dn != null) 
            {
                boolean nodeSelected = helper.isComponentSelected (port);
                Rectangle textRectangle = textRect;
                FontMetrics fontMetrics = gr.getFontMetrics (fontNormal);
                int ascent = fontMetrics.getAscent ();

                gr.setFont (fontNormal);

                gr.setColor (nodeSelected ? SimpleDocumentRenderer.getSelectionBackgroundColor () : SimpleDocumentRenderer.getPaperColor ());
                gr.setColor (nodeSelected ? IconNodeRenderer.getFontShadowSelectedColor () : IconNodeRenderer.getFontShadowColor ());
                gr.drawString (dn, textRectangle.x + 1, textRectangle.y + ascent + 1);

                gr.setColor (IconNodeRenderer.getFontColor ());
                gr.drawString (dn, textRectangle.x, textRectangle.y + ascent);
            }
        } 
        else if (layer == LAYER_ALT_MODE  &&  helper.isAltMode ()) 
        {
            SimpleDocumentRenderer.renderAltRect (gr, portRect);
        }
        gr.translate (- location.x, - location.y);
    }

    public void setVisible (boolean visible)
    {
        this.visible = visible;
    }

    public IGraphEditor getEditor (IGraphPort port, Point position) 
    {
        final Rectangle rectangle = new Rectangle (textRect);
        final INameEditable nameEditable = (INameEditable) port.getLookup ().lookup (INameEditable.class);
        if (rectangle.contains (position)  &&  nameEditable != null  &&  nameEditable.canRename ())
        {
            return new TextFieldEditor () 
            {
                public String getValue () 
                {
                    final String name = nameEditable.getName ();
                    return name != null ? name : NbBundle.getMessage (SimplePortRenderer.class, "TXT_SimplePortDriver_EnterName"); // NOI18N
                }

                public void setValue (String value) {
                    nameEditable.setName (value);
                }

                public int getAlignment () {
                    switch (SimplePortRenderer.this.alignment) {
                        case IDirectionable.LEFT:
                            return IDirectionable.RIGHT;
                        case IDirectionable.RIGHT:
                            return IDirectionable.LEFT;
                        default:
                            return IDirectionable.CENTER;
                    }
                }

                public Rectangle getActiveArea () {
                    return rectangle;
                }

                public void notifyAttached (IGraphEditor.EditorPresenter presenter) {
                    super.notifyAttached (presenter);

                    final int height = getComponent ().getPreferredSize ().height;
                    getComponent ().setMinimumSize (new Dimension (64, height));
                    getComponent ().setMaximumSize (new Dimension (256, height));
                    getComponent ().setPreferredSize (new Dimension (128, height));
                }
            };
        }
        return null;
    }

    public String getToolTipText (IGraphPort port, Point position)
    {
        final IDisplayable displayable = (IDisplayable) port.getLookup ().lookup (IDisplayable.class);
        return displayable != null ? displayable.getTooltipText () : null;
    }

}
