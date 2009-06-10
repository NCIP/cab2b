
package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.border.LineBorder;

public class DynList extends JList
{

	final Dimension dim = new Dimension(100, 20);

	public DynList()
	{
		super();
		setPreferredSize(dim);
		setBorder(new LineBorder(Color.GRAY));
	}

}
