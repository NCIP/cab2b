package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bListBox;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.common.util.PermissibleValueComparator;

public class EnumTypePanel extends AbstractTypePanel {
    private static final long serialVersionUID = 1L;

    public EnumTypePanel(ArrayList<String> conditionList, Dimension maxLabelDimension) {
        super(conditionList, maxLabelDimension);
    }

    public JComponent getFirstComponent() {
        return getComponent();
    }

    public JComponent getSecondComponent() {
        return getComponent();
    }

    private JComponent getComponent() {
        Collection<PermissibleValueInterface> permissibleValues = edu.wustl.cab2b.common.util.Utility.getPermissibleValues(attribute);
        List<PermissibleValueInterface> permissibleValueList = new ArrayList<PermissibleValueInterface>(
                permissibleValues);
        Collections.sort(permissibleValueList, new PermissibleValueComparator());

        JComponent jComponent = null;
        DefaultListModel defaultListModel = new DefaultListModel();
        defaultListModel.addElement(Constants.SELECT);
        for (PermissibleValueInterface permissibleValue : permissibleValueList) {
            String item = permissibleValue.getValueAsObject().toString();
            defaultListModel.addElement(item);
        }
        jComponent = new Cab2bListBox(defaultListModel);
        return jComponent;
    }

    public ArrayList<String> getValues() {
        ArrayList<String> selected = new ArrayList<String>();
        Object[] values = ((Cab2bListBox) firstComponent).getSelectedValues();
        for (Object value : values) {
            if (!value.equals(Constants.SELECT)) {
                selected.add((String) value);
            }
        }
        return selected;
    }

    public void setValues(ArrayList<String> values) {
        ((Cab2bListBox) firstComponent).setSelectedValues(values);
    }

    public void setComponentPreference(String condition) {
    }

    public void resetPanel() {
        ArrayList<String> newValues = new ArrayList<String>();
        newValues.add(Constants.SELECT);
        ((Cab2bListBox) firstComponent).setSelectedValues(newValues);
    }

}
