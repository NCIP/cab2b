
package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;

import javax.swing.Action;

import org.jdesktop.swingx.JXHyperlink;

import edu.wustl.cab2b.client.ui.WindowUtilities;

/**
 * By Default Cab2bHyperlink is underlined, unlike JXHyperlink,
 * where hyperlinks are underlined only on mouse over.
 * 
 * To get JXHyperlink behaviour in Cab2bHyperlink call Cab2bHyperlink constructors
 * with isHyperlinkUnderlined boolean set to false. 
 * 
 * @author chetan_bh
 *
 */
public class Cab2bHyperlink extends JXHyperlink
{

	/**
	 * User object associated with this hyperlink.
	 */
	private Object userObject = null;	 
	private static Color m_clickedHyperlinkColor = new Color(0x006699);
	private static Color m_unclickedHyperlinkColor = new Color(0x034E74);
	private static boolean m_isHyperlinkUnderlined = true;

	public Cab2bHyperlink()
	{
		this(null,m_isHyperlinkUnderlined,m_clickedHyperlinkColor,m_unclickedHyperlinkColor);
	}

	public Cab2bHyperlink(boolean isHyperlinkUnderlined)
	{
		
		this(null,isHyperlinkUnderlined,m_clickedHyperlinkColor,m_unclickedHyperlinkColor);
	}
	
	public Cab2bHyperlink( Color clickedColor,Color unclickedColor)
	{	
		this(null,m_isHyperlinkUnderlined,clickedColor,unclickedColor);
	}

	public Cab2bHyperlink(Action action)
	{	
		this(action,m_isHyperlinkUnderlined,m_clickedHyperlinkColor,m_unclickedHyperlinkColor);
	}

	public Cab2bHyperlink(Action action, boolean isHyperlinkUnderlined)
	{
	
		this(action,isHyperlinkUnderlined,m_clickedHyperlinkColor,m_unclickedHyperlinkColor);		
	}
	
	public Cab2bHyperlink(Action action, boolean isHyperlinkUnderlined, Color clickedColor,Color unclickedColor)
	{
		super(action);		
		
		m_isHyperlinkUnderlined = isHyperlinkUnderlined;	 
		this.setClickedColor(clickedColor);
		this.setUnclickedColor(unclickedColor);
		if (m_isHyperlinkUnderlined)
			this.setUI(new Cab2bHyperlinkUI());
	}

	/**
	 * Returns user object associated with this hyperlink.
	 */
	public Object getUserObject()
	{
		return userObject;
	}

	/**
	 * Sets the user object.
	 */
	public void setUserObject(Object userObject)
	{
		this.userObject = userObject;
	}

	public static void main(String[] args)
	{
		Cab2bHyperlink hyperlink = new Cab2bHyperlink( true);
		
		hyperlink.setText("hyperlink text");
		
		WindowUtilities.showInFrame(hyperlink, "Cab2b Hyperlink");
	}

}
