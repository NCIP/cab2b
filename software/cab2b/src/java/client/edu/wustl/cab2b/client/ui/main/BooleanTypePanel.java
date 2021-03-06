/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bRadioButton;

public class BooleanTypePanel extends AbstractTypePanel {
    private static final long serialVersionUID = 1L;

    private JRadioButton noneButton;

    private ButtonGroup group;

    static private JRadioButton selectedButton;

    /**
     * @param conditionList
     * @param maxLabelDimension
     */
    public BooleanTypePanel(ArrayList<String> conditionList, Dimension maxLabelDimension) {
        super(conditionList, maxLabelDimension);
    }

    private JPanel getComboForBoolean(Collection<String> permissibleValueList) {
        JPanel radioPanel = new Cab2bPanel();
        group = new ButtonGroup();

        RadioButtonListener radioButtonListener = new RadioButtonListener();

        for (String permissibleValue : permissibleValueList) {
            JRadioButton jRadioButton = new Cab2bRadioButton(permissibleValue);
            if (permissibleValue.equals("None")) {
                noneButton = jRadioButton;
                noneButton.setVisible(false);
            } else {
                jRadioButton.addActionListener(radioButtonListener);
            }
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
     * @return JComponent
     */
    public JComponent getFirstComponent() {
        return getComboForBoolean();
    }

    /**
     * Returns the second component. There is no need of second component for
     * Boolean type, hence returns an empty component.
     * @return JComponent
     */
    public JComponent getSecondComponent() {
        JPanel secondComponent = new JPanel();
        secondComponent.setVisible(false);
        secondComponent.setOpaque(false);
        return secondComponent;
    }

    /**
     * Checks whether the radio Button is Selected or not
     * @param jRadioButton
     * @return Boolean Value
     */
    public static boolean isSelected(JRadioButton jRadioButton) {
        DefaultButtonModel model = (DefaultButtonModel) jRadioButton.getModel();
        return model.getGroup().isSelected(model);
    }

    /**
     * @return ArrayList
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#getValues() 
     */
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<String>();

        if (isSelected((JRadioButton) (((JPanel) firstComponent).getComponent(0)))) {
            values.add("true");
        } else if (isSelected((JRadioButton) (((JPanel) firstComponent).getComponent(1)))) {
            values.add("false");
        }

        return values;
    }

    /**
     * @param values
     * @see edu.wustl.cab2b.client.ui.main.IComponent#setValues(java.util.ArrayList)
     */
    public void setValues(ArrayList<String> values) {
        if (values.size() == 0) {
            return;
        }

        JPanel panel = (JPanel) firstComponent;
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

    /**
     * @param condition
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#setComponentPreference(java.lang.String)
     */
    public void setComponentPreference(String condition) {
    }

    /**
     * Resets the Panel
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#resetPanel()
     */
    public void resetPanel() {
        JPanel panel = (JPanel) firstComponent;
        JRadioButton radioButton = (JRadioButton) panel.getComponent(2);
        radioButton.setSelected(true);
    }

    class RadioButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JRadioButton jRadioButton = (JRadioButton) actionEvent.getSource();
            if (selectedButton != null && selectedButton == jRadioButton) {
                group.setSelected(noneButton.getModel(), true);
                selectedButton = null;
            } else {
                selectedButton = jRadioButton;
            }
        }
    }
}
