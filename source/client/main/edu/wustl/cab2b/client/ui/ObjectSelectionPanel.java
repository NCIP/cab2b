package edu.wustl.cab2b.client.ui;

import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.SEARCH_FRAME_TITLE;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bListBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.UserObjectWrapper;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * Component to which allows to return selective objects from a list of objects.
 * 
 * @author Hrishikesh Rajpathak
 *
 */
public class ObjectSelectionPanel extends Cab2bPanel {

	private Cab2bListBox leftListBox;

	private Cab2bListBox rightListBox;

	private DefaultListModel leftHandListModel;

	private DefaultListModel rightHandListModel;

	private Cab2bButton add;

	private Cab2bButton addAll;

	private Cab2bButton remove;

	private Cab2bButton removeAll;

	private JLabel leftListTitle;

	private JLabel rightListTitle;

	private int listBoxWidth;

	private int listBoxHeight;

	private int heightBetweenButtons;

	public ObjectSelectionPanel(Collection<UserObjectWrapper> collection, int width, int height,
			String leftTitle, String rightTitle, int heightBetweenButtons) {

		this.listBoxWidth = width;
		this.listBoxHeight = height;
		this.heightBetweenButtons = heightBetweenButtons;
		leftHandListModel = new DefaultListModel();
		rightHandListModel = new DefaultListModel();
		leftListBox = new Cab2bListBox(leftHandListModel);
		rightListBox = new Cab2bListBox(rightHandListModel);
		add = new Cab2bButton("Add");
		addAll = new Cab2bButton("Add All");
		remove = new Cab2bButton("Remove");
		removeAll = new Cab2bButton("Remove All");

		leftListTitle = new JLabel(leftTitle);
		rightListTitle = new JLabel(rightTitle);

		for (Object coll : collection) {
			leftHandListModel.addElement(coll);
		}
		initGUI();
	}

	public ObjectSelectionPanel(Collection<UserObjectWrapper> collection, String leftTitle, String rightTitle) {
		this(collection, 300, 350, leftTitle, rightTitle, 10);
	}

	private void initGUI() {
		this.setPreferredSize(new Dimension(130+listBoxWidth+listBoxWidth,listBoxHeight+30));
		leftListBox.setPreferredSize(new Dimension(listBoxWidth, listBoxHeight));
		Cab2bPanel leftPanel = new Cab2bPanel(new RiverLayout(5, 5));
		leftPanel.add(leftListTitle);
		leftPanel.add("br", leftListBox);
		this.add(leftPanel);

		
		
		rightHandListModel = new DefaultListModel();
		rightListBox = new Cab2bListBox(rightHandListModel);
		add = new Cab2bButton("Add");
		add.setPreferredSize(new Dimension(100, 22));
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] selectedValues = leftListBox.getSelectedValues();
				for (int i = 0; i < selectedValues.length; i++) {
					rightHandListModel.addElement(selectedValues[i]);
				}

			}
		});

		addAll = new Cab2bButton("Add All");
		addAll.setPreferredSize(new Dimension(100, 22));
		addAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < leftHandListModel.getSize(); i++) {
					rightHandListModel.addElement(leftHandListModel.get(i));
				}

			}
		});
		remove = new Cab2bButton("Remove");
		remove.setPreferredSize(new Dimension(100, 22));
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] selectedValues = rightListBox.getSelectedValues();
				for (int i = 0; i < selectedValues.length; i++) {
					rightHandListModel.removeElement(selectedValues[i]);
				}
			}
		});
		removeAll = new Cab2bButton("Remove All");
		removeAll.setPreferredSize(new Dimension(100, 22));
		removeAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rightHandListModel.removeAllElements();
			}
		});
		Cab2bPanel buttonPanel = new Cab2bPanel(new RiverLayout(0, heightBetweenButtons));
		buttonPanel.setPreferredSize(new Dimension(110, listBoxHeight));
		buttonPanel.add(new JLabel(" "));
		buttonPanel.add("br", new JLabel(" "));
		buttonPanel.add("br", add);
		buttonPanel.add("br", addAll);
		buttonPanel.add("br", remove);
		buttonPanel.add("br", removeAll);

		rightListBox.setPreferredSize(new Dimension(listBoxWidth, listBoxHeight));

		Cab2bPanel rightPanel = new Cab2bPanel(new RiverLayout(5, 5));
		rightPanel.add(rightListTitle);
		rightPanel.add("br", rightListBox);
		
		this.add(buttonPanel);
		this.add(rightPanel);
	}
	
	/**
	 * @return Collection of selected objects
	 */
	public Collection getSelectedObjects() {
		Collection returnSet = new ArrayList();
		int size = rightHandListModel.getSize();
		for (int i = 0; i < size; i++) {
			returnSet.add(rightHandListModel.getElementAt(i));
		}
		return returnSet;
	}
}
