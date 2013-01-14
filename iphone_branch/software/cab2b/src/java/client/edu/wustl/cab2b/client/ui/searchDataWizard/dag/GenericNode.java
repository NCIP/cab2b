/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import org.netbeans.graph.api.control.GraphHelper;
import org.netbeans.graph.api.control.IGraphNodeRenderer;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.builtin.GraphNode;
import org.netbeans.graph.api.model.builtin.GraphPort;

public abstract class GenericNode extends GraphNode 
{
	private int lastID = 0;

	public void addPort (GraphPort port)
    {
    	port.setID (Integer.toString (++lastID));
	    super.addPort (port);
	}
    public abstract IGraphNodeRenderer createNodeRenderer (GraphHelper helper, IGraphNode node, boolean isForView);

}
