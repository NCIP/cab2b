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
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;

/**
 * This is a top level navigation panel, which is placed at the top of the
 * <code>MainFrame</code>.
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public class GlobalNavigationPanel extends Cab2bPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Background color of the panel.
	 */
	private Color bgColor = new Color(255, 255, 255);

	public MainFrame mainFrame;

	/**
	 * <code>MainFrame</code> reference.
	 */
	public JXFrame frame;

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

	/**
	 * Initialize UI with RepeatIcon and GlobalNavigationGlassPane
	 */
	private void initGUIWithGB() {
        ClassLoader loader = this.getClass().getClassLoader();
        
		JPanel cab2bPanel = new JPanel(new BorderLayout());
		Icon logoIcon = new ImageIcon(loader.getResource("r_1p.gif"));
		int width=MainFrame.getScreenDimesion().width;
		width=width-600;
		RepeatIcon repeatIcon = new RepeatIcon(logoIcon, 0, width);
		JLabel labelMiddle = new JLabel(repeatIcon);
		Icon iconLeft = new ImageIcon(loader.getResource("banner_img1.gif"));
		Icon iconRight = new ImageIcon(loader.getResource("banner_img3.gif"));
		JLabel labelLeft = new JLabel(iconLeft);
		JLabel labelRight = new JLabel(iconRight);
		cab2bPanel.add(labelLeft, BorderLayout.WEST);
		cab2bPanel.add(labelMiddle, BorderLayout.CENTER);
		cab2bPanel.add(labelRight, BorderLayout.EAST);
		GlobalNavigationGlassPane myGlassPane = new GlobalNavigationGlassPane(labelLeft,
				labelMiddle, labelRight, mainFrame, frame);
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
