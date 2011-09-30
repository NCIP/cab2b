package edu.wustl.common.treeApplet;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.event.MouseInputListener;
import javax.swing.tree.DefaultMutableTreeNode;

import netscape.javascript.JSObject;
import edu.wustl.common.tree.CDETreeNode;
import edu.wustl.common.util.global.Constants;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CDETreeListener implements MouseInputListener
{
    /**
     * Corresponds to an applet environment.
     */
    private AppletContext appletContext = null;
    
    /**
     * @return Returns the appletContext.
     */
    public AppletContext getAppletContext()
    {
        return appletContext;
    }
    
    /**
     * @param appletContext The appletContext to set.
     */
    public void setAppletContext(AppletContext appletContext)
    {
        this.appletContext = appletContext;
    }
    
    public void mouseClicked(MouseEvent e) 
    {
        try
        {
			// TODO Auto-generated method stub
	    	if(e.getClickCount()==2)
	        {
	    		Object object = e.getSource();
	    		JTree tree = null;
	
	    		if (object instanceof JTree)
	    		{
	    			tree = (JTree) object;
	    			
	    			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
	                     .getLastSelectedPathComponent();
	
	    			CDETreeNode  treeNode = (CDETreeNode) node
	                     .getUserObject();
	             
	    			//Set the values in the parent window.
	    			Applet applet = this.appletContext.getApplet(Constants.TREE_APPLET_NAME);
	    			System.out.println("applet name "+applet);
	    			//Kapil: MAC ISSUE JDK 1.3.1	    			
	    			JSObject window = JSObject.getWindow(applet);
	    			//commented as this will set in MouseClick
	    			String propertyName = applet.getParameter(Constants.PROPERTY_NAME);
	    			
	    			// if Root node selected then do nothing
	    			if(treeNode.toString().equals(Constants.TISSUE_SITE) || 
	    					treeNode.toString().equals(Constants.CLINICAL_DIAGNOSIS))
	    			{
	    				return;
	    			}
	    			String setValue = new String();
	    			//Poornima:Make the Category nodes non-clickable if child exists. Refer to Bug 1718
	    			if(treeNode.getChildNodes()==null || treeNode.getChildNodes().size()==0)
	    			{
	    				setValue="setParentWindowValue('"+propertyName+"','"+treeNode.toString()+"')";
	    			}
					else
					{
						return;
					}
	             
	    			//Kapil: MAC ISSUE JDK 1.3.1
	    			//commented as this will set in MouseClick
	    			window.eval(setValue);
	    			window.eval("closeWindow()");
	    		}
	        }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
