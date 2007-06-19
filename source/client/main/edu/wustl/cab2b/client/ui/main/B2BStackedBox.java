package edu.wustl.cab2b.client.ui.main;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.POPULAR_CATEGORIES_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_CATEGORIES_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_SEARCH_QUERIES_IMAGE;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.StackedBox;

public class B2BStackedBox extends Cab2bPanel {

	public B2BStackedBox() {

		// this.setBorder(new LineBorder(Color.BLACK));
		this.setLayout(new BorderLayout());
		this.setBorder(null);
		StackedBox box = new StackedBox();
		box.setTitleBackgroundColor(new Color(224, 224, 224));
		box.setBorder(null);
		JScrollPane scrollPane = new JScrollPane(box);
		// scrollPane.setBorder(new CustomizableBorder(new Insets(1,1,1,1),
		// true, true));

		this.add(scrollPane, BorderLayout.CENTER);

		// the status pane
		JPanel status = new JPanel();
		status.setPreferredSize(new Dimension(265, 123));
		status.setOpaque(false);
		status.setBorder(null);
		box.addBox("My Categories", status, MY_CATEGORIES_IMAGE, false);

		// the profiling results
		JPanel profilingResults = new JPanel();
		profilingResults.setOpaque(false);
		profilingResults.setPreferredSize(new Dimension(265, 123));
		profilingResults.setBorder(null);
		box.addBox("My Search Queries", profilingResults, MY_SEARCH_QUERIES_IMAGE, false);

		// the saved snapshots pane
		JPanel popularCategories = new JPanel();
		popularCategories.setPreferredSize(new Dimension(265, 123));
		popularCategories.setOpaque(false);
		popularCategories.setBorder(null);
		box.addBox("Popular Categories", popularCategories,POPULAR_CATEGORIES_IMAGE, false);

		this.setBorder(null);

	}

	static JLabel makeBold(JLabel label) {
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		return label;
	}
}
