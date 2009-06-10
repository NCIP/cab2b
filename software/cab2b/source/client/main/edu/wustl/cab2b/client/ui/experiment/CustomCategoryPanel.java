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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXFrame;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.client.ui.util.UserObjectWrapper;
import edu.wustl.cab2b.client.ui.util.WindowUtilities;
import edu.wustl.cab2b.common.CustomDataCategoryModel;
import edu.wustl.cab2b.common.IdName;
import edu.wustl.cab2b.common.authentication.Authenticator;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.datalist.DataListHomeInterface;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.common.experiment.ExperimentHome;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.common.util.Utility;

public class CustomCategoryPanel extends JXFrame {

    private static final long serialVersionUID = 1L;

    private Cab2bComboBox dataListCombo;

    private Cab2bComboBox categoryCombo;

    private Cab2bPanel finalPanel;

    private Cab2bButton saveButton;

    private Cab2bButton cancelButton;

    private Cab2bLabel customDataCategoryLabel;

    private Cab2bLabel dataListLabel;

    private Cab2bLabel categoryLabel;

    private Cab2bTextField customDataCategoryText;

    CustomDataCategoryModel customDataCategoryModel;

    private JDialog dialog;

    private DataListMetadata dataListMetadata;

    private AccumulatorPanel accumulatorPanel;

    private Collection<UserObjectWrapper> availableAttributeCollection = new ArrayList<UserObjectWrapper>();

    private Cab2bPanel middlePanel;

    private Experiment experiment;

    private DefaultComboBoxModel categoryComboboxModel = new DefaultComboBoxModel();

    /**
     * @param exp
     */
    public CustomCategoryPanel(Experiment exp) {
        this.experiment = exp;
        initGUI();
    }

    /**
     *  Initializes all the GUI Component
     */
    public void initGUI() {
        dataListCombo = new Cab2bComboBox();
        categoryCombo = new Cab2bComboBox();
        dataListCombo.setPreferredSize(new Dimension(225, 20));

        ExperimentBusinessInterface expBus = (ExperimentBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                            EjbNamesConstants.EXPERIMENT,
                                                                                                            ExperimentHome.class);
        try {
            customDataCategoryModel = expBus.getDataCategoryModel(experiment);
        } catch (RemoteException e) {
            CommonUtils.handleException(e, CustomCategoryPanel.this, true, true, true, false);
        } catch (CheckedException e) {
            CommonUtils.handleException(e, CustomCategoryPanel.this, true, true, true, false);
        }

        List<IdName> dataListIdName = customDataCategoryModel.getDataListIdName();

        DefaultComboBoxModel dataListModel = new DefaultComboBoxModel();

        dataListModel.addElement("--Select DataList--");
        for (IdName idName : dataListIdName) {
            dataListModel.addElement(idName);
        }
        dataListCombo.setModel(dataListModel);

        class DataListComboListener implements ItemListener {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    categoryComboboxModel.removeAllElements();
                    IdName selectedIdName = (IdName) e.getItem();
                    List<IdName> rooCategoeisList = customDataCategoryModel.getRooCategories(selectedIdName.getId());
                    if (rooCategoeisList != null) {
                        for (IdName idName : rooCategoeisList) {
                            categoryComboboxModel.addElement(idName);
                        }
                    }
                }
            }
        }
        dataListCombo.addItemListener(new DataListComboListener());
        dataListCombo.setSelectedIndex(0);
        categoryCombo.setModel(categoryComboboxModel);
        categoryCombo.setPreferredSize(new Dimension(225, 20));
        middlePanel = new Cab2bPanel(new RiverLayout(5, 5));
        accumulatorPanel = new AccumulatorPanel("Available Attributes", "Selected Attributes");
        middlePanel.add(accumulatorPanel);
        categoryCombo.addItemListener(new CategoryComboListener(expBus));
        categoryCombo.setSelectedItem(null);
        customDataCategoryLabel = new Cab2bLabel("Custom Data Category Title:");
        dataListLabel = new Cab2bLabel("Data List:");
        categoryLabel = new Cab2bLabel("Root Category:");
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
        finalPanel.add(middlePanel, BorderLayout.CENTER);

        saveButton = new Cab2bButton("Save");
        saveButton.addActionListener(new SaveButtonActionListener());

        cancelButton = new Cab2bButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        Cab2bPanel bottomPanel = new Cab2bPanel(new RiverLayout(5, 10));
        bottomPanel.setPreferredSize(new Dimension(Constants.WIZARD_SIZE2_DIMENSION.width, 40));

        bottomPanel.add(" right ", saveButton);
        bottomPanel.add(" right ", cancelButton);
        finalPanel.add(bottomPanel, BorderLayout.SOUTH);
        dialog = WindowUtilities.setInDialog(NewWelcomePanel.getMainFrame(), finalPanel, "Custom Data Category",
                                             Constants.WIZARD_SIZE2_DIMENSION, true, false);
        dialog.setVisible(true);
    }

    class SaveButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            CustomSwingWorker swingWorker = new CustomSwingWorker(CustomCategoryPanel.this.finalPanel) {
                @Override
                protected void doNonUILogic() throws Exception {
                    Collection<UserObjectWrapper<AttributeInterface>> objectCollection = accumulatorPanel.getSelectedObjects();
                    if (!customDataCategoryText.getText().equals("") && objectCollection != null
                            && objectCollection.size() > 0) {
                        IdName entityName = (IdName) categoryCombo.getSelectedItem();

                        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
                        for (UserObjectWrapper<AttributeInterface> selectedObjects : objectCollection) {
                            attributeList.add(selectedObjects.getUserObject());
                        }
                        try {
                            DataListBusinessInterface dataListBI = (DataListBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                EjbNamesConstants.DATALIST_BEAN,
                                                                                                                                DataListHomeInterface.class);
                            dataListMetadata = dataListBI.saveCustomDataCategory(entityName, attributeList,
                                                                                 customDataCategoryText.getText(),
                                                                                 experiment,
                                                                                 Authenticator.getSerializedDCR());
                        } catch (RemoteException e1) {
                            CommonUtils.handleException(e1, CustomCategoryPanel.this, true, true, true, false);
                        } catch (CheckedException e1) {
                            CommonUtils.handleException(e1, CustomCategoryPanel.this, true, true, true, false);
                        }
                    }
                }

                @Override
                protected void doUIUpdateLogic() throws Exception {
                    if (customDataCategoryText.getText().equals("")) {
                        JOptionPane.showMessageDialog(CustomCategoryPanel.this,
                                                      "Please enter valid name before saving.", "Error",
                                                      JOptionPane.ERROR_MESSAGE);
                    } else {
                        Collection<UserObjectWrapper<AttributeInterface>> objectCollection = accumulatorPanel.getSelectedObjects();
                        if (objectCollection == null || objectCollection.size() == 0) {
                            JOptionPane.showMessageDialog(
                                                          CustomCategoryPanel.this,
                                                          "Please add attributes in 'Selected Attributes' list before saving.",
                                                          "Error", JOptionPane.ERROR_MESSAGE);

                        } else {
                            dialog.dispose();
                        }
                    }
                }

            };
            swingWorker.start();
        }
    }

    /**
     * Select category combobox listener, displays attributes for selected
     * category entity into "Available Attribute" list.
     *
     * @author deepak_shingan
     */
    class CategoryComboListener implements ItemListener {
        ExperimentBusinessInterface expBus;

        CategoryComboListener(ExperimentBusinessInterface expBus) {
            this.expBus = expBus;
        }

        public void itemStateChanged(ItemEvent e) {

            if (e.getStateChange() == ItemEvent.SELECTED) {
                IdName selectedIdName = (IdName) e.getItem();
                try {
                    Collection<AttributeInterface> attributes = expBus.getAllAttributes(selectedIdName.getId());
                    availableAttributeCollection.clear();

                    for (AttributeInterface attributeInterface : attributes) {
                        String name = Utility.getDisplayName(attributeInterface.getEntity()) + ": "
                                + Utility.getFormattedString(attributeInterface.getName());
                        int left = name.indexOf("(DataList");
                        int right = name.indexOf(')');
                        StringBuffer buffer = new StringBuffer(name);
                        buffer.delete(left, ++right);
                        name = buffer.toString();
                        availableAttributeCollection.add(new UserObjectWrapper<AttributeInterface>(
                                attributeInterface, name));
                    }
                    accumulatorPanel.setModel(availableAttributeCollection, accumulatorPanel.getSelectedObjects());
                    accumulatorPanel.revalidate();
                } catch (RemoteException e1) {
                    CommonUtils.handleException(e1, CustomCategoryPanel.this, true, true, true, false);
                } catch (CheckedException e1) {
                    CommonUtils.handleException(e1, CustomCategoryPanel.this, true, true, true, false);
                }

            }
        }
    }

    /**
     * @return Returns the dataListMetadata.
     */
    public DataListMetadata getDataListMetadata() {
        return dataListMetadata;
    }
}
