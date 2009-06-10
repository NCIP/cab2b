package edu.wustl.cab2b.client.ui.filter;



import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.Icon;

public class ReverseArrowIcon implements Icon {

	public int getIconHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getIconWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void paintIcon(Component arg0, Graphics arg1, int arg2, int arg3) 
	{
		// TODO Auto-generated method stub
		Polygon p = new Polygon();
		p.addPoint(5, 10);
		p.addPoint(15, 5);
		p.addPoint(15, 15);
		arg1.setColor(Color.WHITE);
		arg1.fillPolygon(p);

	}

}
