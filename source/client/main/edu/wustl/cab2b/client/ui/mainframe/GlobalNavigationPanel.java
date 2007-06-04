package edu.wustl.cab2b.client.ui.mainframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXFrame;

import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;


/**
 * This is a top level navigation panel, which is placed at the top of the
 * <code>MainFrame</code>.
 * 
 * @author hrishikesh_rajpathak
 *
 */
public class GlobalNavigationPanel extends Cab2bPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Background color of the panel.
	 */
	Color bgColor = new Color(255, 255, 255);

	MainFrame mainFrame;

	/**
	 * Label to show details of the logged in user to the application.
	 */
	Cab2bLabel loggedInUserLabel;

	/**
	 * <code>MainFrame</code> reference.
	 */
	JXFrame frame;

	public static MainSearchPanel mainSearchPanel = null;

	public GlobalNavigationPanel(JXFrame frame, MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.frame = frame;
		this.setBackground(bgColor);
		this.setLayout(new RiverLayout(0, 0));
		this.setPreferredSize(new Dimension(1024, 75));
		this.setMaximumSize(new Dimension(1024, 75));
		this.setMinimumSize(new Dimension(1024, 75));

		initGUIWithGB();

	}

	private void initGUIWithGB() {
		JPanel cab2bPanel = new JPanel(new BorderLayout());
		Icon logoIcon = new ImageIcon("resources/images/a/r_1p.gif");
		RepeatIcon repeatIcon = new RepeatIcon(logoIcon, 0, 648);
		JLabel labelMiddle = new JLabel(repeatIcon);
		Icon iconLeft = new ImageIcon("resources/images/a/banner_img1.gif");
		Icon iconRight = new ImageIcon("resources/images/a/banner_img3.gif");
		JLabel labelLeft = new JLabel(iconLeft);
		JLabel labelRight = new JLabel(iconRight);
		cab2bPanel.add(labelLeft, BorderLayout.WEST);
		cab2bPanel.add(labelMiddle, BorderLayout.CENTER);
		cab2bPanel.add(labelRight, BorderLayout.EAST);
		MyGlassPane myGlassPane = new MyGlassPane(labelLeft, labelMiddle, labelRight, mainFrame,
				frame);
		cab2bPanel.setBackground(Color.WHITE);
		this.frame.setGlassPane(myGlassPane);
		this.add("hfill", cab2bPanel);

	}
	/*
	 *//**
		 * @param args
		 */
	/*
	 * public static void main(String[] args) { Logger.configure("");
	 * GlobalNavigationPanel globalNavigationPanel = new
	 * GlobalNavigationPanel(new JXFrame(), new MainFrame()); JXFrame frame =
	 * WindowUtilities.showInFrame(globalNavigationPanel, "Global Navigation
	 * Panel"); }
	 */

}
