/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.mainframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXFrame;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.searchDataWizard.MainSearchPanel;

/**
 * This is a top level navigation panel, which is placed at the top of the
 * <code>MainFrame</code>.
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public class GlobalNavigationPanel extends Cab2bPanel {
	private static final long serialVersionUID = 1L;

	private GlobalNavigationGlassPane myGlassPane;

	/** Experiment Home Button*/
	static final public String EXPERIMENT_HOMEBUTTON_CLICK = "EXPERIMENT_HOMEBUTTON_CLICK";

	/**
	 * Background color of the panel.
	 */
	private Color bgColor = new Color(255, 255, 255);

	private MainFrame mainFrame;

	/**
	 * <code>MainFrame</code> reference.
	 */
	private JXFrame frame;

	private static MainSearchPanel mainSearchPanel = null;

	/**
	 * @param frame
	 * @param mainFrame
	 */
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
		int width = MainFrame.getScreenDimension().width;
		width = width - 600;
		RepeatIcon repeatIcon = new RepeatIcon(logoIcon, 0, width);
		JLabel labelMiddle = new JLabel(repeatIcon);
		Icon iconLeft = new ImageIcon(loader.getResource("banner_img1.gif"));
		Icon iconRight = new ImageIcon(loader.getResource("banner_img3.gif"));
		JLabel labelLeft = new JLabel(iconLeft);
		JLabel labelRight = new JLabel(iconRight);
		cab2bPanel.add(labelLeft, BorderLayout.WEST);
		cab2bPanel.add(labelMiddle, BorderLayout.CENTER);
		cab2bPanel.add(labelRight, BorderLayout.EAST);
		myGlassPane = new GlobalNavigationGlassPane(labelLeft, labelMiddle,
				labelRight, mainFrame, frame);
		cab2bPanel.setBackground(Color.WHITE);
		this.frame.setGlassPane(myGlassPane);
		this.add("hfill", cab2bPanel);
		this.addPropertyChangeListener(new PropertyListener());
	}

	/**
	 * @return
	 */
	public GlobalNavigationGlassPane getGlobalNavigationGlassPane() {
		return this.myGlassPane;
	}

	/**
	 * @return the mainSearchPanel
	 */
	public static MainSearchPanel getMainSearchPanel() {
		return mainSearchPanel;
	}

	/**
	 * @param mainSearchPanel
	 *            the mainSearchPanel to set
	 */
	public static void setMainSearchPanel(MainSearchPanel mainSearchPanel) {
		GlobalNavigationPanel.mainSearchPanel = mainSearchPanel;
	}

	/**
	 * @author deepak_shingan
	 *
	 */
	class PropertyListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(EXPERIMENT_HOMEBUTTON_CLICK)) {
				System.out.println("Got property change");
				myGlassPane.setExperimentHomePanel();
			}
		}
	}
}
