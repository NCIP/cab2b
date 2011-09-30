package edu.wustl.common.treeApplet;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.event.MouseInputListener;
import javax.swing.tree.DefaultMutableTreeNode;

import netscape.javascript.JSObject;
import edu.wustl.common.util.global.Constants;

public abstract class AppletTreeListener implements MouseInputListener 
{
	/**
	 * String to store the propertyName of given specimen
	 */
	protected String propertyName;
	
	/**
	 * Applet variable to store the current applet
	 */
	protected Applet applet;
	
	/**
	 * Node to store the DefaultMutableTreeNode instance 
	 */
	protected DefaultMutableTreeNode node;

	/**
	 * String to store the return value of setParentWindowValue() javascript function
	 */
	protected String setValue = new String();
	
	/**
     * Corresponds to an applet environment.
     */
    protected AppletContext appletContext = null;
    
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
			if(e.getClickCount()==2)
	        {
	    		Object object = e.getSource();
	    		JTree tree = null;
	    		Object treeNode = null;
	    		JSObject window = null;
	
	    		if (object instanceof JTree)
	    		{
	    			tree = (JTree) object;
	    			//Used in subclasses.
	    			node = (DefaultMutableTreeNode) tree
	                     .getLastSelectedPathComponent();
	
	    			//Set the values in the parent window.
	    			applet = this.appletContext.getApplet(Constants.TREE_APPLET_NAME);
	    			System.out.println("applet name "+applet);
	    			
	    			//commented as this will set in MouseClick
	    			propertyName = applet.getParameter(Constants.PROPERTY_NAME);
	    			
	    			//Kapil: MAC ISSUE JDK 1.3.1	    			
	    			window = JSObject.getWindow(applet);
	    			
	    			//This method implemented in the sub class.
	    			displayClickedSpecimenNode();
	    			
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
	
	public abstract void displayClickedSpecimenNode();

	public void mousePressed(MouseEvent arg0) 
	{
	
	}

	public void mouseReleased(MouseEvent arg0) 
	{
		
	}

	public void mouseEntered(MouseEvent arg0) 
	{
	
	}

	public void mouseExited(MouseEvent arg0) 
	{
		
	}

	public void mouseDragged(MouseEvent arg0) 
	{
		
	}

	public void mouseMoved(MouseEvent arg0) 
	{
		
	}

}
