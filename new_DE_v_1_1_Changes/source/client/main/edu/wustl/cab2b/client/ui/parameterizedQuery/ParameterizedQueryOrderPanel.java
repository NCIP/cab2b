/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.main.AbstractTypePanel;
import edu.wustl.cab2b.client.ui.main.ParseXMLFile;
import edu.wustl.cab2b.client.ui.main.SwingUIManager;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.common.querysuite.queryobject.ICondition;

public class ParameterizedQueryOrderPanel extends
		ParameterizedQueryPreviewPanel {
	private static final long serialVersionUID = 1L;

	private Cab2bButton okButton;

	private Cab2bButton cancelButton;

	private ParameterizedQueryMainPanel parameterizedQueryMainPanel;

	public ParameterizedQueryOrderPanel(
			final ParameterizedQueryMainPanel parameterizedQueryMainPanel) {
		super();
		this.parameterizedQueryMainPanel = parameterizedQueryMainPanel;
		initGUI();
	}

	/**
	 * This method initializes the query order preview panel.
	 */
	protected void initGUI() {
		this.setLayout(new BorderLayout());
		bottomConditionPanel = new Cab2bPanel();
		topConditionPanel = new Cab2bPanel();

		okButton = new Cab2bButton("Ok");
		okButton.addActionListener(new OkButtonActionListener());

		cancelButton = new Cab2bButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
				parameterizedQueryMainPanel.parameterConditionPanel
						.refreshConditionPanel();
				parameterizedQueryMainPanel.showInDialog();
			}
		});

		creatOrderPreviewPanel(parameterizedQueryMainPanel
				.getParameterizedQueryDataModel().getConditions());
		initTopConditionPanel();
		addArrowButtons();
		initBottomConditionPanel();
		this.add(getNavigationPanel(), BorderLayout.SOUTH);
	}

	/**
	 * This method initialize the arrow buttons and sets them in
	 * topPanelContainer.
	 */
	private void addArrowButtons() {
		ImageIcon icon = createImageIcon("upArrow.gif");
		Cab2bButton upArrowButton = new Cab2bButton(icon);
		upArrowButton.setBorder(null);
		upArrowButton.setPreferredSize(new Dimension(75, 20));
		upArrowButton.setBackground(Color.white);
		upArrowButton.addActionListener(new UpArrowActionListener());

		icon = createImageIcon("downArrow.gif");
		Cab2bButton downArrowButton = new Cab2bButton(icon);
		downArrowButton.setBorder(null);
		downArrowButton.setPreferredSize(new Dimension(75, 20));
		downArrowButton.setBackground(Color.white);
		downArrowButton.addActionListener(new DownArrowActionListener());

		Cab2bPanel orderLabelPanel = new Cab2bPanel();
		orderLabelPanel.setLayout(new BorderLayout());
		orderLabelPanel.add(upArrowButton, BorderLayout.NORTH);
		orderLabelPanel.add(new Cab2bLabel(" "), BorderLayout.CENTER);
		orderLabelPanel.add(downArrowButton, BorderLayout.SOUTH);

		Cab2bPanel topPanelContainer = (Cab2bPanel) topConditionTitlePanel
				.getContentContainer();
		topPanelContainer.add("tab ", orderLabelPanel);
	}

	/**
	 * This method creates and returns the ImageIcon from the given icon image
	 * name.
	 * 
	 * @param name
	 *            name of the image to be used to create icon
	 * @return
	 */
	private ImageIcon createImageIcon(final String name) {
		return new ImageIcon(getClass().getClassLoader().getResource(name));
	}

	/**
	 * This method initializes and return the navigation panel
	 */
	protected Cab2bPanel getNavigationPanel() {
		if (navigationPanel == null) {
			navigationPanel = new Cab2bPanel();
		}
		navigationPanel.removeAll();
		navigationPanel.setLayout(new RiverLayout(5, 10));
		navigationPanel.setBackground(new Color(240, 240, 240));

		if (parameterizedQueryMainPanel != null) {
			navigationPanel.add("right ", okButton);
		}
		navigationPanel.add("right ", cancelButton);
		navigationPanel.add("br ", new Cab2bLabel());

		return navigationPanel;
	}

	/**
	 * This method is UpArrowButton ActionListener
	 * 
	 * @author deepak_shingan
	 */
	private class UpArrowActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			Map<Integer, AbstractTypePanel> panelMap = getAllSelectedPanel();
			for (Integer index : panelMap.keySet()) {
				if (index != 0) {
					AbstractTypePanel downPanel = (AbstractTypePanel) topConditionPanel
							.getComponent(index);
					topConditionPanel.remove(downPanel);
					topConditionPanel.add(downPanel, "br ", index - 1);
					topConditionPanel.revalidate();
					topConditionPanel.repaint();
				}
			}
		}
	}

	/**
	 * This method create order preview panel from given conditions
	 * 
	 * @param conditionMap
	 *            Map of expressionId -> List<IConditions>
	 */
	private void creatOrderPreviewPanel(
			Map<Integer, Collection<ICondition>> conditionMap) {
		AbstractTypePanel componentPanel = null;

		ParameterizedQueryConditionPanel conditionPanel = parameterizedQueryMainPanel
				.getParameterConditionPanel();
		List<AbstractTypePanel> panelList = conditionPanel
				.getCheckedAttributePanels(conditionPanel.getConditionPanel());
		for (int index = 0; index < panelList.size(); index++) {
			AbstractTypePanel panel = panelList.get(index);
			panel.setAttributeCheckBox(false);

			// set changed/unchanged label for each attribute field
			panel.setAttributeDisplayName(panel
					.getAttributeDisplayNameTextField().getText());

			// hide the attributeDisplayText box
			panel.remove(panel.getAttributeDisplayNameTextField());

			// Now add to the condition panel
			topConditionPanel.add("br ", panelList.get(index));
		}

		try {
			ParseXMLFile parseFile = ParseXMLFile.getInstance();

			panelList = parameterizedQueryMainPanel
					.getParameterConditionPanel().getUnCheckedAttributePanels();
			findMaxLabelDimension(conditionMap);
			for (int index = 0; index < panelList.size(); index++) {
				AbstractTypePanel oldComponentPanel = panelList.get(index);
				String conditionString = oldComponentPanel.getConditionItem();
				if ("Is Null".equals(conditionString)
						|| "Is Not Null".equals(conditionString)
						|| !oldComponentPanel.getValues().isEmpty()) {
					componentPanel = (AbstractTypePanel) SwingUIManager
							.generateUIPanel(parseFile, oldComponentPanel
									.getAttributeEntity(), maxLabelDimension);
					componentPanel.createParametrizedPanel(oldComponentPanel
							.getAttributeEntity());
					componentPanel.setAttributeDisplayName(oldComponentPanel
							.getAttributeDisplayName());

					setConditionValues(componentPanel, conditionMap);
					CommonUtils.disableAllComponent(componentPanel);
					bottomConditionPanel.add("br ", componentPanel);
				}
			}
		} catch (CheckedException checkedException) {
			CommonUtils.handleException(checkedException, this, true, true,
					false, false);
		}
	}

	/**
	 * DownArrowButton Action listener
	 * 
	 * @author deepak_shingan
	 */
	private class DownArrowActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			Map<Integer, AbstractTypePanel> panelMap = getAllSelectedPanel();

			Integer idxs[] = panelMap.keySet().toArray(new Integer[0]);
			int count = topConditionPanel.getComponentCount();
			for (int i = idxs.length - 1; i >= 0; i--) {
				if (idxs[i] < count - 1) {
					AbstractTypePanel downPanel = (AbstractTypePanel) topConditionPanel
							.getComponent(idxs[i]);
					topConditionPanel
							.setComponentZOrder(downPanel, idxs[i] + 1);
					topConditionPanel.revalidate();
					topConditionPanel.repaint();
				}
			}
		}
	}

	/**
	 * This method is OK button action listener. It sets the sequence of the
	 * conditions arranged by the user.
	 * 
	 * @author chetan_patil
	 */
	private class OkButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			dialog.dispose();
			Cab2bPanel basePanel = parameterizedQueryMainPanel
					.getParameterConditionPanel().getConditionPanel();
			int totalPanelCount = topConditionPanel.getComponentCount();
			// Keep in mind whenever you will add panel from topConditionPanel
			// to
			// base panel it will automatically get removed from
			// topConditionPanel
			for (int index = 0; index < totalPanelCount; index++) {
				if (topConditionPanel.getComponent(0) instanceof AbstractTypePanel) {
					AbstractTypePanel oldPanel = (AbstractTypePanel) topConditionPanel
							.getComponent(0);
					oldPanel
							.add(oldPanel.getAttributeDisplayNameTextField(), 1);
					oldPanel.setAttributeCheckBox(true);
					basePanel.add(oldPanel, "br ", index);
				}
			}
			updateUI();
			parameterizedQueryMainPanel.showInDialog();
		}
	}
}
