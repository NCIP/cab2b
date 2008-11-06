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

    /**
     * @param conditionList
     * @param maxLabelDimension
     */
    public EnumTypePanel(ArrayList<String> conditionList, Dimension maxLabelDimension) {
        super(conditionList, maxLabelDimension);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#getFirstComponent()
     * @return JComponent the first component in Enum Panel
     */
    public JComponent getFirstComponent() {
        return getComponent();
    }

    /**
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#getSecondComponent()
     * @return JComponent the second component in Enum Panel
     */
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

    /**
     * Gets the values for Enum Panel
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#getValues()
     * @return values
     */
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

    /**
     * Sets the values for Enum Panel
     * @see edu.wustl.cab2b.client.ui.main.IComponent#setValues(java.util.ArrayList)
     * @param values
     */
    public void setValues(ArrayList<String> values) {
        ((Cab2bListBox) firstComponent).setSelectedValues(values);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#setComponentPreference(java.lang.String)
     * @param condition
     */
    public void setComponentPreference(String condition) {
    }

    /**
     * Resets all the values in the Enum panel
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#resetPanel()
     */
    public void resetPanel() {
        ArrayList<String> newValues = new ArrayList<String>();
        newValues.add(Constants.SELECT);
        ((Cab2bListBox) firstComponent).setSelectedValues(newValues);
    }

}
