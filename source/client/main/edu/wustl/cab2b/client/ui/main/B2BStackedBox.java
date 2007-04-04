package edu.wustl.cab2b.client.ui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;
import edu.wustl.cab2b.client.ui.controls.StackedBox;

public class B2BStackedBox extends Cab2bPanel {


	  public B2BStackedBox() 
	  {

		//this.setBorder(new LineBorder(Color.BLACK));  
	  	this.setLayout(new BorderLayout());
	    StackedBox box = new StackedBox();
	    box.setTitleBackgroundColor(new Color(224,224,224));
	    JScrollPane scrollPane = new JScrollPane(box);
	    scrollPane.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
	    this.add(scrollPane, BorderLayout.CENTER);
	        	   
	    // the status pane
	    JPanel status = new JPanel();
	    status.setPreferredSize(new Dimension(263,122));
	    status.setOpaque(false);
	    box.addBox("My Categories", status,"");
	    
	    // the profiling results
	    JPanel profilingResults = new JPanel();
	    profilingResults.setOpaque(false);
	    profilingResults.setPreferredSize(new Dimension(263,123));
	    box.addBox("My Search Queries", profilingResults,"");

	    // the saved snapshots pane
	    JPanel savedSnapshots = new JPanel();
	    savedSnapshots.setPreferredSize(new Dimension(263,123));
	    savedSnapshots.setOpaque(false);
	    box.addBox("Popular Categories", savedSnapshots,"");
	        
	 }
	  
	  static JLabel makeBold(JLabel label) {
	    label.setFont(label.getFont().deriveFont(Font.BOLD));
	    return label;    
	  }
}
