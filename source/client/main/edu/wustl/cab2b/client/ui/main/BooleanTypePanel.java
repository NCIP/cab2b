package edu.wustl.cab2b.client.ui.main;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bRadioButton;

public class BooleanTypePanel extends AbstractTypePanel
{
	
	public BooleanTypePanel(ArrayList<String> conditionList, AttributeInterface attributeEntity)
	{
		super(conditionList, attributeEntity);
	}
		
	private JPanel getComboForBoolean()
	{
		JPanel radioPanel = new Cab2bPanel(new GridLayout(1, 0));
		ButtonGroup group = new ButtonGroup();
		JRadioButton trueButton = new Cab2bRadioButton("True");
		JRadioButton falseButton = new Cab2bRadioButton("False");
		group.add(trueButton);
		group.add(falseButton);
		radioPanel.add(trueButton);
		radioPanel.add(falseButton);
		return radioPanel;
	}
	
	/**
	 * Returns the first component, with model set to true and false value.
	 */
	@Override
	public JComponent getFirstComponent()
	{
		return getComboForBoolean();
	}
	
	/**
	 * Returns the second component.
	 * There is no need of second component for Boolean type, 
	 * hence returns an empty component.
	 */
	@Override
	public JComponent getSecondComponent()
	{
		JPanel secondComponent = getComboForBoolean();
		secondComponent.setVisible(false);
		secondComponent.setOpaque(false);
		return secondComponent;
	}

	public static boolean isSelected(JRadioButton btn)
	{
        DefaultButtonModel model = (DefaultButtonModel)btn.getModel();
        return model.getGroup().isSelected(model);
    }

	public ArrayList<String> getValues()
	{
		ArrayList<String> values = new ArrayList<String>();
		if(isSelected((JRadioButton)(((JPanel)m_NameEdit).getComponent(0))))
			values.add("true");
		else if(isSelected((JRadioButton)(((JPanel)m_NameEdit).getComponent(1))))
			values.add("false");
		return values;
	}

	public void setValues(ArrayList<String> values) 
	{
		if(values.size() == 0)
			return;
		JPanel panel = (JPanel)m_NameEdit;
		JRadioButton radioButton = (JRadioButton)panel.getComponent(0);
		if(radioButton.getText().compareToIgnoreCase(values.get(0)) == 0)
		{
			radioButton.setSelected(true);
			return;
		}
		radioButton = (JRadioButton)panel.getComponent(1);
		if(radioButton.getText().compareToIgnoreCase(values.get(0)) == 0)
		{
			radioButton.setSelected(true);
			return;
		}
	}

	@Override
	public void setComponentPreference(String condition) {
		
	}
}
