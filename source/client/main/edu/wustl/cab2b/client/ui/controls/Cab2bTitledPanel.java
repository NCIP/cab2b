package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXTitledPanel;

import edu.wustl.cab2b.client.ui.WindowUtilities;


public class Cab2bTitledPanel extends JXTitledPanel
{
	
	static Color defaultBgColor = Color.WHITE;
	
	public Cab2bTitledPanel()
	{
		this("");
	}

	public Cab2bTitledPanel(String title)
	{
		this(title, new Cab2bPanel());
	}

	public Cab2bTitledPanel(String title, Container container)
	{
		super(title, container);
		this.setBackground(defaultBgColor);
		//this.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
	}
	
//	@Override
//    public Component add(Component comp)
//    {
//    	Container contentPanel = this.getContentContainer();
//    	return contentPanel.add(comp);
//    }
//    
//    @Override
//    public void add(Component comp, Object constraints)
//    {
//    	Container contentPanel = this.getContentContainer();
//    	contentPanel.add(comp, constraints);
//    }
//    
//    @Override
//    public Component add(Component comp, int index)
//    {
//    	Container contentPanel = this.getContentContainer();
//    	return contentPanel.add(comp, index);
//    }
//    
//    @Override
//    public Component add(String name, Component comp)
//    {
//    	Container contentPanel = this.getContentContainer();
//    	return contentPanel.add(name, comp);
//    }
//    
//    @Override
//    public void remove(Component comp)
//    {
//    	Container contentPanel = this.getContentContainer();
//    	contentPanel.remove(comp);
//    }
//    
//    @Override
//    public void remove(int index)
//    {
//    	Container contentPanel = this.getContentContainer();
//    	contentPanel.remove(index);
//    }
	
	
	public static void main(String[] args)
	{
		Cab2bPanel parentPanel = new Cab2bPanel();
		
		
		Cab2bTitledPanel titledPanel = new Cab2bTitledPanel("Titled Panel");
		
		
		parentPanel.add(new JLabel("This is a sample Label"));
    	
    	JTextField textField = new JTextField();
    	textField.setColumns(10);
    	parentPanel.add(textField);
    	parentPanel.add(new JButton("Button"));
    	
    	titledPanel.add(parentPanel);
    	
    	WindowUtilities.showInFrame(titledPanel, "Titskldjfdf ");
	}

}
