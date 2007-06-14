package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bRadioButton;

public class BooleanTypePanel extends AbstractTypePanel {
    private static final long serialVersionUID = 1L;

    public BooleanTypePanel(
            ArrayList<String> conditionList,
            AttributeInterface attributeEntity,
            Boolean showCondition,
            Dimension maxLabelDimension) {
        super(conditionList, attributeEntity, showCondition, maxLabelDimension);
    }

    private JPanel getComboForBoolean(Collection<String> permissibleValueList) {
        JPanel radioPanel = new Cab2bPanel(new GridLayout(1, 0));
        ButtonGroup group = new ButtonGroup();

        for (String permissibleValue : permissibleValueList) {
            JRadioButton jRadioButton = new Cab2bRadioButton(permissibleValue);
            group.add(jRadioButton);
            radioPanel.add(jRadioButton);
        }

        return radioPanel;
    }

    private JPanel getComboForBoolean() {
        Collection<String> permissibleValueList = new LinkedList<String>();
        permissibleValueList.add("True");
        permissibleValueList.add("False");
        permissibleValueList.add("None");
        return getComboForBoolean(permissibleValueList);
    }

    /**
     * Returns the first component, with model set to true and false value.
     */
    public JComponent getFirstComponent() {
        return getComboForBoolean();
    }

    /**
     * Returns the second component.
     * There is no need of second component for Boolean type, 
     * hence returns an empty component.
     */
    public JComponent getSecondComponent() {
        JPanel secondComponent = getComboForBoolean();
        secondComponent.setVisible(false);
        secondComponent.setOpaque(false);
        return secondComponent;
    }

    public static boolean isSelected(JRadioButton jRadioButton) {
        DefaultButtonModel model = (DefaultButtonModel) jRadioButton.getModel();
        return model.getGroup().isSelected(model);
    }

    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<String>();

        if (isSelected((JRadioButton) (((JPanel) m_NameEdit).getComponent(0)))) {
            values.add("true");
        } else if (isSelected((JRadioButton) (((JPanel) m_NameEdit).getComponent(1)))) {
            values.add("false");
        }

        return values;
    }

    public void setValues(ArrayList<String> values) {
        if (values.size() == 0) {
            return;
        }

        JPanel panel = (JPanel) m_NameEdit;
        JRadioButton radioButton = (JRadioButton) panel.getComponent(0);
        if (radioButton.getText().compareToIgnoreCase(values.get(0)) == 0) {
            radioButton.setSelected(true);
            return;
        }
        
        radioButton = (JRadioButton) panel.getComponent(1);
        if (radioButton.getText().compareToIgnoreCase(values.get(0)) == 0) {
            radioButton.setSelected(true);
            return;
        }

    }

    public void setComponentPreference(String condition) {
    }
    
    public void resetPanel() {
        JPanel panel = (JPanel) m_NameEdit;
        JRadioButton radioButton = (JRadioButton) panel.getComponent(2);
        radioButton.setSelected(true);
    }

}
