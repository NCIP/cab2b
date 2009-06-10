package edu.wustl.cab2b.client.ui.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bListBox;
import edu.wustl.cab2b.common.util.PermissibleValueComparator;

public class EnumTypePanel extends AbstractTypePanel 
{

	public EnumTypePanel(ArrayList<String> conditionList, AttributeInterface attributeEntity)
	{
		super(conditionList, attributeEntity);
	}

	@Override
	public JComponent getFirstComponent() 
	{
		// TODO Auto-generated method stub
		DefaultListModel model = new DefaultListModel();
		Collection<PermissibleValueInterface> permissibleValues = edu.wustl.cab2b.common.util.Utility.getPermissibleValues(attributeEntity);
		Object[] values = permissibleValues.toArray();
		Arrays.sort(values, new PermissibleValueComparator());
		for(int i=0; i<values.length; i++)
		{
			PermissibleValueInterface perValue = (PermissibleValueInterface)values[i];
			String item = perValue.getValueAsObject().toString();
			model.addElement(item);
		}
		Cab2bListBox listBox = new Cab2bListBox(model);
		return listBox;
	}

	@Override
	public JComponent getSecondComponent() 
	{
		DefaultListModel model = new DefaultListModel();
		Collection<PermissibleValueInterface> permissibleValues = edu.wustl.cab2b.common.util.Utility.getPermissibleValues(attributeEntity);
		Object[] values = permissibleValues.toArray();
		Arrays.sort(values, new PermissibleValueComparator());
		for(int i=0; i<values.length; i++)
		{
			PermissibleValueInterface perValue = (PermissibleValueInterface)values[i];
			String item = perValue.getValueAsObject().toString();
			model.addElement(item);
		}
		Cab2bListBox listBox = new Cab2bListBox(model);
		return listBox;
	}

	public ArrayList<String> getValues()
	{
		Object[] values = ((Cab2bListBox)m_NameEdit).getSelectedValues();
		ArrayList<String> selected = new ArrayList<String>();
		for(int i=0; i<values.length; i++)
		{
			selected.add((String)values[i]);	
		}
		return selected;
	}

	public void setValues(ArrayList<String> values)
	{
		((Cab2bListBox)m_NameEdit).setSelectedValues(values);
	}

	@Override
	public void setComponentPreference(String condition)
	{
		// TODO Auto-generated method stub
	}

}
