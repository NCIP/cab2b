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
	  	this.setBorder(null);
	    StackedBox box = new StackedBox();
	    box.setTitleBackgroundColor(new Color(224,224,224));
	    box.setBorder(null);
	    JScrollPane scrollPane = new JScrollPane(box);
	    //scrollPane.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
	    
	    this.add(scrollPane, BorderLayout.CENTER);
	        	   
	    // the status pane
	    JPanel status = new JPanel();
	    status.setPreferredSize(new Dimension(265,123));
	    status.setOpaque(false);
	    status.setBorder(null);
	    box.addBox("My Categories", status,"");
	    
	    // the profiling results
	    JPanel profilingResults = new JPanel();
	    profilingResults.setOpaque(false);
	    profilingResults.setPreferredSize(new Dimension(265,123));
	    profilingResults.setBorder(null);
	    box.addBox("My Search Queries", profilingResults,"");

	    // the saved snapshots pane
	    JPanel savedSnapshots = new JPanel();
	    savedSnapshots.setPreferredSize(new Dimension(265,123));
	    savedSnapshots.setOpaque(false);
	    savedSnapshots.setBorder(null);
	    box.addBox("Popular Categories", savedSnapshots,"");
	        
	 }
	  
	  static JLabel makeBold(JLabel label) {
	    label.setFont(label.getFont().deriveFont(Font.BOLD));
	    return label;    
	  }
}
