package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.IGraphPortRenderer;
import org.netbeans.graph.api.control.editor.IGraphEditor;
import org.netbeans.graph.api.model.IGraphPort;
import org.netbeans.graph.api.model.ability.IDirectionable;

import java.awt.*;

public final class IconPortRenderer implements IGraphPortRenderer 
{
    private SimplePortRenderer leftsidePortDriver;
    private SimplePortRenderer rightSidePortDriver;
    private GraphHelper helper;
    private IGraphPort port;

    private boolean isDefaultResolved = false;
    private boolean isDefault = false;

    public IconPortRenderer (GraphHelper helper, IGraphPort port, Image image)
    {
        this.helper = helper;
        this.port = port;
        leftsidePortDriver = new SimplePortRenderer (helper, port, true, IDirectionable.LEFT, image);
        rightSidePortDriver = new SimplePortRenderer (helper, port, true, IDirectionable.RIGHT, image);
    }

    private boolean resolvedDefault () 
    {
        if (! isDefaultResolved)
        {
            isDefault = port == helper.getNodeDefaultPort (helper.getPortNode (port));
            isDefaultResolved = true;
        }
        return isDefault;
    }

    public void layoutPort (IGraphPort port, Graphics2D gr) 
    {
        if (resolvedDefault ())
            leftsidePortDriver.layoutPort (port, gr);
        else
            rightSidePortDriver.layoutPort (port, gr);
    }

    public int[] getLayers (IGraphPort port)
    {
        return SimplePortRenderer.layers;
    }

    public void renderPort (IGraphPort port, Graphics2D gr, int layer)
    {
        if (resolvedDefault ())
        {
            leftsidePortDriver.renderPort (port, gr, layer);
        }
        else
        {
            rightSidePortDriver.renderPort (port, gr, layer);
        }
    }

    public IGraphEditor getEditor (IGraphPort port, Point position) {
        if (resolvedDefault ())
            return leftsidePortDriver.getEditor (port, position);
        else
            return rightSidePortDriver.getEditor (port, position);
    }

    public String getToolTipText (IGraphPort port, Point position) {
        if (resolvedDefault ())
            return leftsidePortDriver.getToolTipText (port, position);
        else
            return rightSidePortDriver.getToolTipText (port, position);
    }

}
