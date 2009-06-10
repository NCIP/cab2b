package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.border.AbstractBorder;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.WindowUtilities;

/**
 * A customizable border.
 * @author chetan_bh
 */
public class CustomizableBorder extends AbstractBorder
{
	/**
	 * Default Insets.
	 */
	Insets insets = new Insets(1 , 1, 1, 1);
	Color outlineColor = Color.BLACK;
	Color borderFillColor = Color.WHITE;
	boolean isBorderOpaque = true;
	boolean wantBorderline = true;
	
	public CustomizableBorder(Insets insets, boolean isBorderOpaque, boolean wantBorderline)
	{
		this.insets = insets;
		this.isBorderOpaque = isBorderOpaque;
		this.wantBorderline = wantBorderline;
	}
	
	public CustomizableBorder(Insets insets, Color borderLineColor)
	{
		this.insets = insets;
		this.isBorderOpaque = true;
		this.wantBorderline = true;
		outlineColor = borderLineColor;
	}
	
	public boolean isBorderOpaque() 
	{
	      return isBorderOpaque;
	}
	
	public Insets getBorderInsets(Component c) 
	{
	      return insets; 
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) 
	{
		
	    g.setColor(borderFillColor);
	      /*Top Border.*/
	    g.fillRect(x,y,width,insets.top);
	      /*Left Border.*/
	    g.fillRect(x,y,x+insets.left,height);
	      /*Bottom Border.*/
	    g.fillRect(x,y+height-insets.bottom,width, insets.bottom);
	      /*Right Border.*/
	    g.fillRect(x+width-insets.right,y,insets.right ,height);
	    
	    if(wantBorderline)
	    {
	    	g.setColor(outlineColor);
	    	//g.drawLine(x,y, x+width, y);
	    	g.drawLine(x+insets.left, y+insets.top, x+width-insets.right, y+insets.top);
	    	//g.drawLine(x,y,x,y+height);
	    	g.drawLine(x+insets.left, y+insets.top,x+insets.left,y+height-insets.left);
	    	//g.drawLine(x,y+height-1,x+width,y+height-1);
	    	g.drawLine(x+width-insets.right, y+height-insets.bottom,x+insets.left,y+height-insets.bottom);
	    	//g.drawLine(x+width-1,y+height-1,x+width-1,y);
	    	g.drawLine(x+width-insets.right, y+height-insets.bottom,x+width-insets.right,y+insets.top);
	    }
	    
	}
	
	public static void main(String[] args)
	{
		JXPanel panel = new JXPanel();
		
		panel.add(new JButton("This is a Button"));
		
		panel.setBorder(new CustomizableBorder(new Insets(5,0,5,8), true, false));
		
		WindowUtilities.showInFrame(panel, "Border Demo");
	}
	
	
}
