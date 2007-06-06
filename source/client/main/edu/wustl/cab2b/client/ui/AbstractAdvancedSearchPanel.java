package edu.wustl.cab2b.client.ui;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import edu.wustl.cab2b.client.ui.controls.Cab2bCheckBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bRadioButton;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * The abstract class that contains commonalities between the just the
 * collapsible portion of the advanced/category search panels for the main as
 * well as 'AddLimit' section from the main search dialog. Concrete classes must
 * over ride methods to effect custom layout.
 * 
 * @author mahesh_iyer/chetan_bh
 * 
 */
public abstract class AbstractAdvancedSearchPanel extends Cab2bPanel implements ItemListener
{

	/** The container for the collapsible pane.*/
	JXTaskPaneContainer m_taskPaneContainer = null;
	
	/** The actual collapsible pane.*/
	JXTaskPane m_taskPane = null;
	
	///** Check-box for specifying search target as category name */
	//Cab2bCheckBox m_chkCategory = null;
	//Cab2bCheckBox m_chkCategoryDef = null;
	//Cab2bCheckBox m_chkCategoryDesc = null;
	
	/** Check-box for specifying search target as class name as well as decription.*/
	Cab2bCheckBox m_chkClass = null;
	Cab2bCheckBox m_chkClassDef = null;
	Cab2bCheckBox m_chkClassDesc = null;
	
	/** Check-box for specifying search target as attribute.*/
	Cab2bCheckBox m_chkAttribute = null;
	//Cab2bCheckBox m_chkAttributeDef = null;
	//Cab2bCheckBox m_chkAttributeDesc = null;
	
	
	/** Check-box for specifying search target as PV.*/
	Cab2bCheckBox m_chkPermissibleValues = null;
	
	/** Radio button for specifying mode of search as text based*/
	Cab2bRadioButton m_radioText = null;
	
	/** Radio button for specifying mode of search as concept-code based*/
	Cab2bRadioButton m_radioConceptCode = null;
	
	
	
	/**
	 * Constructor
	 */
	public AbstractAdvancedSearchPanel()
	{		
		initGUI();
	}
	
	/**
	 * Method initializes the panel by appropriately laying out child components.
	 * 
	 */
	private void initGUI()
	{
		
		/* intialize all the components.*/
		m_taskPaneContainer = new JXTaskPaneContainer();
			
		m_taskPane = new JXTaskPane();		
		m_taskPane.setSpecial(false);
		m_taskPane.setTitle("Advanced Search");
		m_taskPane.setLayout(new RiverLayout(0,5));
		m_taskPane.getContentPane().setBackground(Color.WHITE);
		//m_chkCategory = new Cab2bCheckBox("Category Name");
		//m_chkCategory.addItemListener(this);
		//m_chkCategory.setActionCommand("category");
		
		//m_chkCategoryDesc = new Cab2bCheckBox("Category Description");
		//m_chkCategoryDesc.addItemListener(this);
		//m_chkCategoryDesc.setActionCommand("categorydesc");
		
		//m_chkCategoryDef = new Cab2bCheckBox("Category Definition");
		//m_chkCategoryDef.addItemListener(this);
		//m_chkCategoryDef.setActionCommand("categorydef");
		
		
		m_chkClass = new Cab2bCheckBox("Category");
		m_chkClass.setSelected(true);
		m_chkClass.addItemListener(this);
		m_chkClass.setActionCommand("class");
		
		// Added by deepak
		m_chkClassDesc = new Cab2bCheckBox("Include Description");
		m_chkClassDesc.setSelected(false);
		m_chkClassDesc.addItemListener(this);
		m_chkClassDesc.setActionCommand("classdesc");
		
		m_chkClassDef = new Cab2bCheckBox("Include Classes");
		m_chkClassDef.addItemListener(this);
		m_chkClassDef.setActionCommand("classdef");
		
		
		m_chkAttribute = new Cab2bCheckBox("Attribute");	
		m_chkAttribute.addItemListener(this);
		m_chkAttribute.setActionCommand("attribute");
		
		//m_chkAttributeDesc = new Cab2bCheckBox("Attribute Description");
		//m_chkAttributeDesc.addItemListener(this);
		//m_chkAttributeDesc.setActionCommand("attributedesc");
		//m_chkAttributeDef = new Cab2bCheckBox("Attribute Definition");
		//m_chkAttributeDef.addItemListener(this);
		//m_chkAttributeDef.setActionCommand("attributedef");

		
		m_chkPermissibleValues = new Cab2bCheckBox("Permissible Values");
		m_chkPermissibleValues.setEnabled(true);
		m_chkPermissibleValues.addItemListener(this);
		m_chkPermissibleValues.setActionCommand("permissibleValues");
		

		ButtonGroup radioGroup = new ButtonGroup();
		
		m_radioText = new Cab2bRadioButton("Text");
		m_radioText.setSelected(true);
		m_radioText.addItemListener(this);
		m_radioText.setActionCommand("textRadio");
		
		m_radioConceptCode = new Cab2bRadioButton("Concept Code");
		m_radioConceptCode.addItemListener(this);
		m_radioConceptCode.setActionCommand("conceptRadio");

		/* Bundle the radio buttons into a group.*/
		radioGroup.add(m_radioText);
		radioGroup.add(m_radioConceptCode);

		/*
		 * Invoke the method to add the initialized components to the task pane.
		 */
		addComponents();
		
		/* Add  the initialized pane to the contaier*/
		m_taskPaneContainer.add(m_taskPane);
		m_taskPaneContainer.setBackground(Color.BLUE);
		/* Finally add container to the main panel */
		this.add(m_taskPane);				
	}
	
	
	/**
	 * Method to determine the targets for the search.
	 * 
	 * @return int[] The int array, where each element is a constant defined for
	 *         each target type.
	 * 
	 */
	public int[] getSearchTargetStatus()
	{
		/*Int array for all possible target types.*/
		int[] searchTargetStatus = new int[5];
		
		int count = 0;
		int index = 0;
		if(m_chkPermissibleValues.isSelected())
		{
			
			searchTargetStatus[index++] = Constants.PV;
			count++;
			
		}
		if(m_chkClass.isSelected())
		{
			if(m_chkClassDesc.isSelected())
			{
				searchTargetStatus[index++] = Constants.CLASS_WITH_DESCRIPTION;
				count++;
			}else
			{
				searchTargetStatus[index++] = Constants.CLASS;
				count++;
			}
		}
		
		if(m_chkAttribute.isSelected())
		{
			if(m_chkClassDesc.isSelected())
			{
				searchTargetStatus[index++] = Constants.ATTRIBUTE_WITH_DESCRIPTION;
				count++;
			}else
			{			
				searchTargetStatus[index++] = Constants.ATTRIBUTE;
				count++;
			}
		}	

		
		
	    
	    
			
		
		/* Int array of size depending on all the targets selectes.*/
		int searchTargets[] = new int[count];
		count = 0;
		for (int i = 0; i < searchTargetStatus.length; i++)
		{
			if(searchTargetStatus[i] != 0)
			{
				searchTargets[count++] = searchTargetStatus[i];
			}
		}		
		return searchTargets;
	}
	
	/**
	 * Method to determine the selected mode of search
	 * 
	 * @return int The variabled maps to a constant that defines the mode of
	 *         search.
	 * 
	 */
	
	public int getSearchOnStatus()
	{
		int searchOnStatus;
		if(m_radioText.isSelected())
		{
			searchOnStatus = Constants.BASED_ON_TEXT;
		}else
		{
			searchOnStatus = Constants.BASED_ON_CONCEPT_CODE;
		}
		return searchOnStatus;
	}

	/**
	 * Method to determine the targets for the search.
	 * 
	 * @return int[] The int array, where each element is a constant defined for
	 *         each target type.
	 * 
	 */
	
	public void itemStateChanged(ItemEvent ie)
	{

		Object obj = ie.getSource();
		if (obj instanceof Cab2bCheckBox)
		{
			Cab2bCheckBox control = (Cab2bCheckBox) obj;
			if(control.getActionCommand() == "attribute" || control.getActionCommand() == "class" )
            {
              if ( m_chkClass.isSelected() || m_chkAttribute.isSelected())
              {
                    m_chkClassDesc.setEnabled(true);
              }else
              {
            	    m_chkClassDesc.setSelected(false);
                    m_chkClassDesc.setEnabled(false);
              }                 
              Logger.out.info("Clicked attribute or class");
            }
		}
		else if (obj instanceof Cab2bRadioButton)
		{
			Cab2bRadioButton control = (Cab2bRadioButton) obj;		
		}
	}

	/**
	 * The abstract method implementation that allows specific instances of
	 * {@link AbstractAdvancedSearchPanel} to layout components as is required
	 * by them
	 * 
	 * 
	 */		
	protected abstract void addComponents();
		

}
