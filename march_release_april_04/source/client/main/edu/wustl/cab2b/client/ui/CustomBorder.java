package edu.wustl.cab2b.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

import edu.wustl.common.util.logger.Logger;

public class CustomBorder extends AbstractBorder {

	
    public Insets getBorderInsets(Component c) {
     
      return new Insets(1 , 1, 1, 1);
    
    }

    public boolean isBorderOpaque() {
      return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width,
      int height) 
    {
    	//Logger.out.info("inside paint");
    	g.setColor(Color.BLACK);
    	
    	g.drawLine(x,y,x,y+height);
    	g.drawLine(x,y+height-1,x+width,y+height-1);
    	g.drawLine(x+width-1,y+height-1,x+width-1,y);
      
    /*  Top Border.
      g.fillRect(x,y,width,8);
      Left Border.
      g.fillRect(x,8,4,height-12);
      Bottom Border.
      g.fillRect(x,height-4,width, 4);
      Right Border.
      g.fillRect(width-4,8,4,height-12);*/
    }
}
