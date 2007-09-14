package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXFrame;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.ObjectSelectionPanel;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bListBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.UserObjectWrapper;
import edu.wustl.cab2b.common.CustomDataCategoryModel;
import edu.wustl.cab2b.common.IdName;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.datalist.DataListHomeInterface;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.common.experiment.ExperimentHome;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.common.util.Utility;

public class CustomCategoryPanel extends JXFrame {

	private Cab2bComboBox dataListCombo;

	private Cab2bComboBox categoryCombo;

	private Cab2bPanel finalPanel;

	private Cab2bPanel warningPanel;

	private Cab2bButton saveButton;

	private Cab2bLabel customDataCategoryLabel;

	private Cab2bLabel dataListLabel;

	private JLabel label;

	private Cab2bLabel categoryLabel;

	private Cab2bTextField customDataCategoryText;

	private Cab2bListBox leftListBox;

	private Cab2bButton add;

	private Cab2bButton addAll;

	private Cab2bButton remove;

	private Cab2bButton removeAll;

	private ExperimentBusinessInterface expBus;

	private DefaultListModel leftHandListModel;

	private Cab2bListBox rightListBox;

	private DefaultListModel rightHandListModel;

	CustomDataCategoryModel customDataCategoryModel;

	private JDialog dialog;

	private DataListMetadata dataListMetadata;
	
	private ObjectSelectionPanel objectSelectionPanel;

	public CustomCategoryPanel() {
		expBus = (ExperimentBusinessInterface) CommonUtils.getBusinessInterface(
				EjbNamesConstants.EXPERIMENT, ExperimentHome.class);
		try {
			customDataCategoryModel = expBus.getDataCategoryModel();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CheckedException e) {
			e.printStackTrace();
		}
		warningPanel = new Cab2bPanel(new RiverLayout(10, 0));
		initGUI();
	}

	public void initGUI() {
		dataListCombo = new Cab2bComboBox();
		categoryCombo = new Cab2bComboBox();
		dataListCombo.setPreferredSize(new Dimension(150, 20));
		List<IdName> dataListIdName = customDataCategoryModel.getDataListIdName();

		DefaultComboBoxModel dataListModel = new DefaultComboBoxModel();

		dataListModel.addElement("--Select DataList--");
		for (IdName idName : dataListIdName) {
			dataListModel.addElement(idName);
		}
		dataListCombo.setModel(dataListModel);
		DefaultComboBoxModel categoryModel = new DefaultComboBoxModel();

		class DataListComboListener implements ItemListener {
			DefaultComboBoxModel categoryModel;

			public DataListComboListener(DefaultComboBoxModel modelTwo) {
				this.categoryModel = modelTwo;
			}

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					categoryModel.removeAllElements();
					IdName selectedIdName = (IdName) e.getItem();
					List<IdName> rooCategoeisList = customDataCategoryModel
							.getRooCategories(selectedIdName.getId());

					if (rooCategoeisList != null) {
						for (IdName idName : rooCategoeisList) {
							categoryModel.addElement(idName);
						}
					}
				}
			}

		}
		;
		dataListCombo.addItemListener(new DataListComboListener(categoryModel));
		dataListCombo.setSelectedIndex(0);
		categoryCombo.setModel(categoryModel);
		categoryCombo.setPreferredSize(new Dimension(250, 20));

		leftHandListModel = new DefaultListModel();
		leftListBox = new Cab2bListBox(leftHandListModel);
		leftListBox.setBorder(null);
		class CategoryComboListener implements ItemListener {

			public CategoryComboListener() {
			}

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					leftHandListModel.removeAllElements();
					rightHandListModel.removeAllElements();
					IdName selectedIdName = (IdName) e.getItem();
					try {
						Collection<AttributeInterface> attributes = expBus
								.getAllAttributes(selectedIdName.getId());
						Collection<UserObjectWrapper> usrObejctCollection= new ArrayList<UserObjectWrapper>();
						
						for (AttributeInterface attributeInterface : attributes) {
							String name = Utility.getDisplayName(attributeInterface.getEntity())
									+ ": "
									+ CommonUtils.getFormattedString(attributeInterface.getName());
							int left = name.indexOf("(DataList");
							int right = name.indexOf(")");
							StringBuffer buffer = new StringBuffer(name);
							buffer.delete(left, ++right);
							name = buffer.toString();
							leftHandListModel.addElement(new UserObjectWrapper<AttributeInterface>(
									attributeInterface, name));
							usrObejctCollection.add(new UserObjectWrapper<AttributeInterface>(
									attributeInterface, name));
							
							// leftHandListModel.addElement(attributeInterface);
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					} catch (CheckedException e1) {
						e1.printStackTrace();
					}

				}
			}
		}
		;

		categoryCombo.addItemListener(new CategoryComboListener());
		categoryCombo.setSelectedItem(null);
		customDataCategoryLabel = new Cab2bLabel("Custom Data Category Title:");
		dataListLabel = new Cab2bLabel("Data List:");
		categoryLabel = new Cab2bLabel("Category:");
		customDataCategoryText = new Cab2bTextField();
		customDataCategoryText.setPreferredSize(new Dimension(150, 20));
		Cab2bPanel topPanel = new Cab2bPanel(new RiverLayout(5, 10));
		topPanel.add(customDataCategoryLabel);
		topPanel.add("tab ", customDataCategoryText);
		topPanel.add("br", dataListLabel);
		topPanel.add(dataListCombo);
		topPanel.add(new JLabel("            "));
		topPanel.add(categoryLabel);
		topPanel.add(categoryCombo);
		topPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));

		topPanel.setPreferredSize(new Dimension(Constants.WIZARD_SIZE2_DIMENSION.width, 80));
		finalPanel = new Cab2bPanel(new BorderLayout());
		finalPanel.add(topPanel, BorderLayout.NORTH);
		Cab2bPanel middlePanel = new Cab2bPanel(new RiverLayout(5, 5));
		JScrollPane jScrollPaneLeft = new JScrollPane(leftListBox,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		jScrollPaneLeft.setPreferredSize(new Dimension(300, 400));
		middlePanel.add(jScrollPaneLeft, BorderLayout.WEST);
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
		Cab2bPanel buttonPanel = new Cab2bPanel(new RiverLayout(0, 30));
		buttonPanel.setPreferredSize(new Dimension(110, 400));
		buttonPanel.add(new JLabel(" "));
		buttonPanel.add("br", new JLabel(" "));
		buttonPanel.add("br", add);
		buttonPanel.add("br", addAll);
		buttonPanel.add("br", remove);
		buttonPanel.add("br", removeAll);

		rightListBox.setBorder(null);
		JScrollPane jScrollPaneRight = new JScrollPane(rightListBox,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPaneRight.setPreferredSize(new Dimension(300, 400));
		middlePanel.add(buttonPanel);
		middlePanel.add(jScrollPaneRight);
		finalPanel.add(middlePanel, BorderLayout.CENTER);
		saveButton = new Cab2bButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = customDataCategoryText.getText();
				if (!title.equals("")) {
					IdName entityName = (IdName) categoryCombo.getSelectedItem();
					IdName dataListIdName = (IdName) dataListCombo.getSelectedItem();
					Collection<AttributeInterface> attributeInterface = new ArrayList<AttributeInterface>();
					for (int i = 0; i < rightHandListModel.getSize(); i++) {
						attributeInterface
								.add((AttributeInterface) ((UserObjectWrapper) rightHandListModel
										.getElementAt(i)).getUserObject());
					}

					try {
						DataListBusinessInterface dataListBI = (DataListBusinessInterface) CommonUtils
								.getBusinessInterface(EjbNamesConstants.DATALIST_BEAN,
										DataListHomeInterface.class);
						dataListMetadata = dataListBI.saveDataCategory(entityName,
								attributeInterface, dataListIdName.getId(), title);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					} catch (CheckedException e1) {
						e1.printStackTrace();
					}
					dialog.dispose();
				} else {
					label.setText("Please enter the name for the data category being saved");
					label.setForeground(Color.red);
				}
			}
		});
		Cab2bPanel bottomPanel = new Cab2bPanel(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(Constants.WIZARD_SIZE2_DIMENSION.width, 40));
		Cab2bPanel saveButtonPanel = new Cab2bPanel(new RiverLayout(5, 10));
		saveButtonPanel.add(saveButton);
		label = new JLabel("");
		warningPanel.add(label);
		bottomPanel.add(warningPanel, BorderLayout.WEST);
		bottomPanel.add(saveButtonPanel, BorderLayout.EAST);
		finalPanel.add(bottomPanel, BorderLayout.SOUTH);
		dialog = WindowUtilities.setInDialog(NewWelcomePanel.mainFrame, finalPanel,
				"Custom category", Constants.WIZARD_SIZE2_DIMENSION, true, false);
		dialog.setVisible(true);
	}

	/**
	 * @return Returns the dataListMetadata.
	 */
	public DataListMetadata getDataListMetadata() {
		return dataListMetadata;
	}
}
