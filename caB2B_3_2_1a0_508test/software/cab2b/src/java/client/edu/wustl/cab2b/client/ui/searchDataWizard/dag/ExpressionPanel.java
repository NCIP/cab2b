package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ExpressionPanel extends JPanel 
{
	private JLabel label;
	
	public ExpressionPanel(String displayString)
	{
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		this.setLayout(flowLayout);
		Border blackline = BorderFactory.createLineBorder(Color.black);
		this.setBorder(blackline);
		this.setBackground(new Color(255,235,185));
		label = new JLabel();
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		label.setText(displayString);
		this.add(label);
	}
	
	public void setText(String text)
	{
		label.setText(text);
	}
}
