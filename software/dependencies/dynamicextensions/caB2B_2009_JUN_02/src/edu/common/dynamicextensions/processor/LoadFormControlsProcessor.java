/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.AbstractAttributeUIBeanInterface;
import edu.common.dynamicextensions.ui.interfaces.ControlUIBeanInterface;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author sujay_narkar
 *
 */
public class LoadFormControlsProcessor
{

	/**
	 * Protected constructor for entity processor
	 *
	 */
	protected LoadFormControlsProcessor()
	{

	}

	/**
	 * this method gets the new instance of the entity processor to the caller.
	 * @return LoadFormControlsProcessor LoadFormControlsProcessor instance
	 */
	public static LoadFormControlsProcessor getInstance()
	{
		return new LoadFormControlsProcessor();
	}

	/**
	 * 
	 * @param controlsForm ControlsForm
	 * @param containerInterface ContainerInterface
	 * @return redirection page path
	 * @throws DynamicExtensionsSystemException dynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException 
	 */
	public void loadFormControls(ControlsForm controlsForm, ContainerInterface containerInterface) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		if ((containerInterface != null) && (controlsForm != null))
		{
			String controlOperation = controlsForm.getControlOperation();

			if (controlOperation == null || controlOperation.equals(""))
			{
				controlOperation = ProcessorConstants.OPERATION_ADD;
				controlsForm.setControlOperation(controlOperation);
			}
			if (controlOperation.equalsIgnoreCase(ProcessorConstants.OPERATION_ADD))
			{
				addControl(controlsForm);
			}

			else if (controlOperation.equalsIgnoreCase(ProcessorConstants.OPERATION_EDIT))
			{
				ControlInterface selectedControl = getSelectedControl(controlsForm,containerInterface);
				editControl(selectedControl, controlsForm, controlsForm);
			}
			//Initialize default values for controls
			initializeControlDefaultValues(controlsForm);
			//initialize form attribute values
			initializeFormAttributeValues(controlsForm,containerInterface);
		}
	}

	/**
	 * @param controlsForm
	 * @throws DynamicExtensionsSystemException 
	 */
	private void initializeFormAttributeValues(ControlsForm controlsForm,ContainerInterface containerInterface) throws DynamicExtensionsSystemException
	{
		ControlConfigurationsFactory controlConfigurationsFactory = ControlConfigurationsFactory.getInstance();
		String userSelectedTool = controlsForm.getUserSelectedTool();
		//	List of tools/controls
		controlsForm.setToolsList(controlConfigurationsFactory.getListOfControls());
		controlsForm.setSelectedControlCaption(ControlsUtility.getControlCaption(controlConfigurationsFactory.getControlDisplayLabel(userSelectedTool)));
		String jspName = controlConfigurationsFactory.getControlJspName(userSelectedTool);
		if (jspName == null)
		{
			jspName = "";
		}
		controlsForm.setHtmlFile(jspName);
		//Data types for selected control
		controlsForm.setDataTypeList(controlConfigurationsFactory.getControlsDataTypes(userSelectedTool));

		//Set Entity Name as root
		EntityInterface entity = containerInterface.getEntity();
		if (entity != null)
		{
			controlsForm.setRootName(entity.getName());
		}
		else
		{
			controlsForm.setRootName("");
		}
		controlsForm.setCurrentContainerName(containerInterface.getCaption());
		controlsForm.setChildList(ControlsUtility.getChildList(containerInterface));
		controlsForm.setControlRuleMap(getControlRulesMap(userSelectedTool));
	}

	/**
	 * @param controlsForm
	 */
	private void addControl(ControlsForm controlsForm)
	{
		String userSelectedTool = controlsForm.getUserSelectedTool();
		if (userSelectedTool == null || userSelectedTool.equals(""))
		{
			userSelectedTool = ProcessorConstants.DEFAULT_SELECTED_CONTROL;
		}
		controlsForm.setUserSelectedTool(userSelectedTool);
	}

	/**
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 * 
	 */
	public void editControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface,AbstractAttributeUIBeanInterface attributeUIBeanInterface) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		ControlProcessor controlProcessor = ControlProcessor.getInstance();
		controlProcessor.populateControlUIBeanInterface(controlInterface, controlUIBeanInterface);
		
		AttributeProcessor attributeProcessor = AttributeProcessor.getInstance();
		if (controlInterface != null)
		{
			attributeProcessor.populateAttributeUIBeanInterface(controlInterface.getAbstractAttribute(), attributeUIBeanInterface);
		}

		String userSelectedTool = DynamicExtensionsUtility.getControlName(controlInterface);
		if (userSelectedTool == null || userSelectedTool.equals(""))
		{
			userSelectedTool = ProcessorConstants.DEFAULT_SELECTED_CONTROL;
		}
		controlUIBeanInterface.setUserSelectedTool(userSelectedTool);
	}

	/**
	 * @param userSelectedTool 
	 * @param controlsForm
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private void initializeControlDefaultValues(ControlsForm controlsForm) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		String userSelectedTool = controlsForm.getUserSelectedTool();
		if ((userSelectedTool != null) && (controlsForm != null))
		{
			if (userSelectedTool.equals(ProcessorConstants.TEXT_CONTROL))
			{
				initializeTextControlDefaultValues(controlsForm);
			}
			else if (userSelectedTool.equals(ProcessorConstants.COMBOBOX_CONTROL))
			{
				initializeComboboxControlDefaultValues(controlsForm);
			}
			else if (userSelectedTool.equals(ProcessorConstants.DATEPICKER_CONTROL))
			{
				initializeDatePickerControlDefaultValues(controlsForm);
			}
			else if (userSelectedTool.equals(ProcessorConstants.CHECKBOX_CONTROL))
			{
				initializeCheckBoxControlDefaultValues(controlsForm);
			}
			else if (userSelectedTool.equals(ProcessorConstants.RADIOBUTTON_CONTROL))
			{
				initializeOptionButtonControlDefaultValues(controlsForm);
			}
			else if (userSelectedTool.equals(ProcessorConstants.FILEUPLOAD_CONTROL))
			{
				initializeFileUploadControlDefaultValues(controlsForm);
			}
		}
	}

	/**
	 * 
	 */
	private void initializeFileUploadControlDefaultValues(ControlsForm controlsForm)
	{
		if (controlsForm.getSupportedFileFormatsList() == null)
		{
			controlsForm.setSupportedFileFormatsList(getFileFormatsList());
		}
		initializeFileFormats(controlsForm);
	}
	/**
	 * This method will separate out the file formats explicitly specified by the user
	 * from the supported file format list. 
	 * @param controlsForm
	 */
	private void initializeFileFormats(ControlsForm controlsForm)
	{
		String unsupportedFileFormatList = null;
		List<String> supportedFileFormats = controlsForm.getSupportedFileFormatsList();
		String[] userSelectedFileFormats = controlsForm.getFileFormats();
		if(userSelectedFileFormats!=null)
		{
			int noOfSelectedFormats = userSelectedFileFormats.length;
			String selectedFileFormat = null;
			for(int i=0;i<noOfSelectedFormats;i++)
			{
				selectedFileFormat = userSelectedFileFormats[i];
				if(!DynamicExtensionsUtility.isStringInList(selectedFileFormat, supportedFileFormats))
				{
					if((unsupportedFileFormatList==null)||(unsupportedFileFormatList.equals("")))
					{
						unsupportedFileFormatList = selectedFileFormat;
					}
					else
					{
						unsupportedFileFormatList = unsupportedFileFormatList + ProcessorConstants.FILE_FORMATS_SEPARATOR  + selectedFileFormat;
					}
				}
			}
		}
		controlsForm.setFormat(unsupportedFileFormatList);
	}

	/**
	 * @return list of file formats available for file control
	 */
	private List<String> getFileFormatsList()
	{
		ArrayList<String> fileFormatsList = new ArrayList<String>();
		fileFormatsList.add("bmp");
		fileFormatsList.add("jpeg");
		fileFormatsList.add("gif");
		fileFormatsList.add("doc");
		fileFormatsList.add("xls");
		fileFormatsList.add("pdf");
		return fileFormatsList;
	}
	/**
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 * 
	 */
	private void initializeOptionButtonControlDefaultValues(ControlsForm controlsForm) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		//Set default display choice	 
		if ((controlsForm.getDisplayChoice() == null)||(controlsForm.getDisplayChoice().equals(ProcessorConstants.DISPLAY_CHOICE_LOOKUP)))
		{
			controlsForm.setDisplayChoice(ProcessorConstants.DEFAULT_DISPLAY_CHOICE_TYPE);
		}
		controlsForm.setGroupNames(getGroupNamesList());
		controlsForm.setSeparatorList(getSeparatorsList());
		if(controlsForm.getSelectedAttributes()==null)
		{
			controlsForm.setSelectedAttributes(new ArrayList<NameValueBean>());
		}
		if(controlsForm.getOptionDetails() == null)
		{
			controlsForm.setOptionDetails(new ArrayList());
		}
	}

	/**
	 * 
	 */
	private void initializeCheckBoxControlDefaultValues(ControlsForm controlsForm)
	{
		if((controlsForm.getAttributeDefaultValue()==null)||((controlsForm.getAttributeDefaultValue().trim().equals(""))))
		{
			controlsForm.setAttributeDefaultValue(ProcessorConstants.DEFAULT_CHECKBOX_VALUE);
		}
	}

	/**
	 * 
	 */
	private void initializeDatePickerControlDefaultValues(ControlsForm controlsForm)
	{
		//Date value type
		if (controlsForm.getDateValueType() == null)
		{
			controlsForm.setDateValueType(ProcessorConstants.DEFAULT_DATE_VALUE);
		}
		//Date format
		if (controlsForm.getFormat() == null)
		{
			controlsForm.setFormat(ProcessorConstants.DEFAULT_DATE_FORMAT);
		}
	}

	/**
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 * 
	 */
	private void initializeComboboxControlDefaultValues(ControlsForm controlsForm) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		//Set default display choice
		if (controlsForm.getDisplayChoice() == null)
		{
			controlsForm.setDisplayChoice(ProcessorConstants.DEFAULT_DISPLAY_CHOICE_TYPE);
		}
		//Default list type
		if (controlsForm.getAttributeMultiSelect() == null)
		{
			controlsForm.setAttributeMultiSelect(ProcessorConstants.DEFAULT_LIST_TYPE);
		}
		if (controlsForm.getFormTypeForLookup() == null)
		{
			controlsForm.setFormTypeForLookup(ProcessorConstants.DEFAULT_LOOKUP_TYPE);
		}
		controlsForm.setGroupNames(getGroupNamesList());
		controlsForm.setSeparatorList(getSeparatorsList());
		if(controlsForm.getSelectedAttributes()==null)
		{
			controlsForm.setSelectedAttributes(new ArrayList<NameValueBean>());
		}
		if(controlsForm.getOptionDetails() == null)
		{
			controlsForm.setOptionDetails(new ArrayList());
		}
	}

	/**
	 * @return
	 */
	private List<NameValueBean> getSeparatorsList()
	{
		ArrayList<NameValueBean> separatorList = new ArrayList<NameValueBean>();
		separatorList.add(new NameValueBean("Comma",","));
		separatorList.add(new NameValueBean("Colon",":"));
		separatorList.add(new NameValueBean("Space"," "));
		separatorList.add(new NameValueBean("Dot","."));
		DynamicExtensionsUtility.sortNameValueBeanListByName(separatorList);
		return separatorList;
	}

	/**
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private List getGroupNamesList() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		ArrayList<NameValueBean> groupNamesList = new ArrayList<NameValueBean>();
		EntityManagerInterface entityManager = EntityManager.getInstance();
		Collection<EntityGroupInterface> listOfGroups = entityManager.getAllEntitiyGroups();
		if(listOfGroups!=null)
		{
			EntityGroupInterface entityGroup = null;
			NameValueBean groupName = null;
			Iterator<EntityGroupInterface> groupIterator = listOfGroups.iterator();
			while(groupIterator.hasNext())
			{
				entityGroup = groupIterator.next();
				if(entityGroup!=null)
				{
					groupName = new NameValueBean(entityGroup.getName(),entityGroup.getId());
					groupNamesList.add(groupName);
				}
			}
		}
		DynamicExtensionsUtility.sortNameValueBeanListByName(groupNamesList);
		return groupNamesList;
	}

	/**
	 * 
	 */
	private void initializeTextControlDefaultValues(ControlsForm controlsForm)
	{
		//Default Data type
		if ((controlsForm.getDataType() == null)||(controlsForm.getDataType().equals("")))
		{
			controlsForm.setDataType(ProcessorConstants.DEFAULT_DATA_TYPE);
		}
		//Default single line type
		if (controlsForm.getLinesType() == null)
		{
			controlsForm.setLinesType(ProcessorConstants.DEFAULT_LINE_TYPE);
		}

		//measurement units list
		if (controlsForm.getMeasurementUnitsList() == null)
		{
			controlsForm.setMeasurementUnitsList(getListOfMeasurementUnits());
		}
	}

	/**
	 * Gets List of Measurement Units
	 * @return List<String>
	 */
	private List<String> getListOfMeasurementUnits()
	{
		List<String> measurementUnits = new ArrayList<String>();
		measurementUnits.add("none");
		measurementUnits.add("inches");
		measurementUnits.add("cm");
		measurementUnits.add("gms");
		measurementUnits.add("kms");
		measurementUnits.add("kgs");
		measurementUnits.add(ProcessorConstants.MEASUREMENT_UNIT_OTHER);
		return measurementUnits;
	}

		
	/**
	 * 
	 * @param controlName name of the control
	 * @param dataTypeName name of datatype
	 * @return Map ControlRulesMap
	 * @throws DynamicExtensionsSystemException dynamicExtensionsSystemException
	 */
	private Map getControlRulesMap(String controlName) throws DynamicExtensionsSystemException
	{
		ControlConfigurationsFactory ccf = ControlConfigurationsFactory.getInstance();
		return ccf.getRulesMap(controlName);
	}
	
	public ControlInterface getSelectedControl(ControlsForm controlsForm,ContainerInterface containerInterface)
	{
		ControlInterface selectedControl = null;
		if((containerInterface!=null)&&(controlsForm!=null))
		{
			String selectedControlId = controlsForm.getSelectedControlId();
			if((selectedControlId!=null)&&(!selectedControlId.trim().equals("")))
			{
				selectedControl = containerInterface.getControlInterfaceBySequenceNumber(selectedControlId);
			}
		}
		return selectedControl;
	}
}