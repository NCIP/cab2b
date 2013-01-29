/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.builtin.OrthogonalLinkRouter;
import org.netbeans.graph.api.model.IGraphLink;

import java.awt.*;

public class OrthogonalLinkRouterLinkRenderer extends SimpleLinkRenderer {

    private OrthogonalLinkRouter router;

    public OrthogonalLinkRouterLinkRenderer (GraphHelper helper, IGraphLink link, OrthogonalLinkRouter router) {
        super (helper, link);
        this.router = router;
    }

    public void layoutLinkHook (IGraphLink link, Rectangle bounds) {

    }

    public void renderLinkHook (IGraphLink link, Graphics2D gr) {

    }

}
