package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bListBox;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.common.util.PermissibleValueComparator;

public class EnumTypePanel extends AbstractTypePanel {
    private static final long serialVersionUID = 1L;

    public EnumTypePanel(
            ArrayList<String> conditionList,
            AttributeInterface attributeEntity,
            Boolean showCondition,
            Dimension maxLabelDimension) {
        super(conditionList, attributeEntity, showCondition, maxLabelDimension);
    }
    
    public EnumTypePanel(
            ArrayList<String> conditionList,
            AttributeInterface attributeEntity,
            Boolean showCondition,
            Dimension maxLabelDimension,Boolean isParameterized,String displayName) {
        super(conditionList, attributeEntity, showCondition, maxLabelDimension,isParameterized,displayName);
    }

    public JComponent getFirstComponent() {
        return getComponent();
    }

    public JComponent getSecondComponent() {
        return getComponent();
    }

    private JComponent getComponent() {
        Collection<PermissibleValueInterface> permissibleValues = edu.wustl.cab2b.common.util.Utility.getPermissibleValues(attributeEntity);
        List<PermissibleValueInterface> permissibleValueList = new ArrayList<PermissibleValueInterface>(
                permissibleValues);
        Collections.sort(permissibleValueList, new PermissibleValueComparator());

        JComponent jComponent = null;
        if (showCondition) {
            DefaultListModel defaultListModel = new DefaultListModel();
            defaultListModel.addElement(Constants.SELECT);
            for (PermissibleValueInterface permissibleValue : permissibleValueList) {
                String item = permissibleValue.getValueAsObject().toString();
                defaultListModel.addElement(item);
            }
            jComponent = new Cab2bListBox(defaultListModel);
        } else {
            DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel();
            defaultComboBoxModel.addElement(Constants.SELECT);
            for (PermissibleValueInterface permissibleValue : permissibleValueList) {
                String item = permissibleValue.getValueAsObject().toString();
                defaultComboBoxModel.addElement(item);
            }
            jComponent = new Cab2bComboBox(defaultComboBoxModel);
        }
        return jComponent;
    }

    public ArrayList<String> getValues() {
        ArrayList<String> selected = new ArrayList<String>();

        if (showCondition) {
            Object[] values = ((Cab2bListBox) m_NameEdit).getSelectedValues();
            for (Object value : values) {
                if (!value.equals(Constants.SELECT)) {
                    selected.add((String) value);
                }
            }
        } else {
            String value = (String) ((Cab2bComboBox) m_NameEdit).getSelectedItem();
            if (value.equals(Constants.SELECT)) {
                value = null;
            }
            selected.add(value);
        }

        return selected;
    }

    public void setValues(ArrayList<String> values) {
        if (showCondition) {
            ((Cab2bListBox) m_NameEdit).setSelectedValues(values);
        } else {
            for (String value : values) {
                if (!value.equals(Constants.SELECT)) {
                    ((Cab2bComboBox) m_NameEdit).addItem(value);
                }
            }
        }
    }

    public void setComponentPreference(String condition) {
    }

    public void resetPanel() {
        if (showCondition) {
            ArrayList<String> newValues = new ArrayList<String>();
            newValues.add(Constants.SELECT);
            ((Cab2bListBox) m_NameEdit).setSelectedValues(newValues);
        } else {
            ((Cab2bComboBox) m_NameEdit).setSelectedItem(Constants.SELECT);
        }
    }

}
