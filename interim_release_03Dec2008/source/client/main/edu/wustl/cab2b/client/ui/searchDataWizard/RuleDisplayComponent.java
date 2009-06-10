/**
 * <p>Title: RuleDisplayComponent Class>
 * <p>Description:  This class represents the component in the Limit set window.
 * It represents the rule added and the logical operator with the next rule.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.client.ui.searchDataWizard;

import javax.swing.DefaultComboBoxModel;

import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;

/**
 * This class represents the component in the Limit set window.
 * It represents the rule added and the logical operator with the next rule.
 * @author gautam_shetty
 */
public class RuleDisplayComponent extends Cab2bPanel implements IRuleDisplayComponent
{
    
    private static final long serialVersionUID = 1234567890L;
    
    /**
     * Label for displaying the attribute name.
     */
    private Cab2bLabel m_Name;
    
    /**
     * Combobox for displaying the conditions based on the data-type.
     */
    private Cab2bComboBox m_Conditions;
    
    /**
     * Creates a RuleDisplayComponent with the label name of the control.
     * @param labelName
     */
    public RuleDisplayComponent(String labelName)
    {
        setLayout(new RiverLayout());
        
        m_Name = new Cab2bLabel(labelName);
        add("tab",m_Name);
        
        m_Conditions = new Cab2bComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("AND");
        model.addElement("OR");
        m_Conditions.setModel(model);
        add("tab",m_Conditions);
    }
    
    /**
     * Returns the label of this rule. 
     * @return the label of this rule.
     */
    public String getLabel()
    {
        return m_Name.getText();
    }
    
    /**
     * Returns the operator selected for this rule.
     * @return the operator selected for this rule.
     */
    public String getLogicalOperator()
    {
        return (String)m_Conditions.getSelectedItem();
    }
}