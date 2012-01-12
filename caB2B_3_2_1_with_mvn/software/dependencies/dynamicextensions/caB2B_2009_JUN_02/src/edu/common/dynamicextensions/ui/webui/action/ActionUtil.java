
package edu.common.dynamicextensions.ui.webui.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.common.dynamicextensions.domain.Entity;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author sujay_narkar
 *
 */
public class ActionUtil
{

	/**
	 * 
	 * @return
	 * @throws DataTypeFactoryInitializationException
	 */
	/*
	 public static List getDataTypeList() throws DataTypeFactoryInitializationException{
	 DataTypeFactory dataTypeFactory = DataTypeFactory.getInstance();

	 List dataTypeNameValueList = new ArrayList();

	 List dataTypeList = dataTypeFactory.getDataTypes();
	 Iterator dataTypeIterator = dataTypeList.iterator();

	 NameValueBean nameValueBean;
	 String dataType;

	 while(dataTypeIterator.hasNext()){
	 dataType = (String) dataTypeIterator.next();
	 nameValueBean = new NameValueBean(dataType,dataType);
	 dataTypeNameValueList.add(nameValueBean);
	 }

	 return dataTypeNameValueList;
	 }

	 *//**
	 * 
	 * @return
	 */
	/*
	 public static List getDisplayChoiceList() {
	 List displayChoiceList = new ArrayList();
	 NameValueBean nameValueBean1,nameValueBean2;
	 nameValueBean1 = new NameValueBean("List","1");
	 nameValueBean2 = new NameValueBean("Dropdown","2");
	 displayChoiceList.add(nameValueBean1);
	 displayChoiceList.add(nameValueBean2);
	 return displayChoiceList;
	 }*/
	
	/**
	 * getExistingFormsList
	 * @param formsList list of forms
	 * @return List of existing forms
	 */
	public static List getExistingFormsList(List formsList)
	{
		List<NameValueBean> existingFormsList = new ArrayList<NameValueBean>();
		Entity entity;
		Iterator listIterator = formsList.iterator();
		while (listIterator.hasNext())
		{
			entity = (Entity) listIterator.next();
			if (entity.getName() != null && entity.getId() != null)
			{
				NameValueBean nameValueBean = new NameValueBean(entity.getName(), entity.getId().toString());
				existingFormsList.add(nameValueBean);
			}
		}
		return existingFormsList;
	}

	/**
	 * Returns the namevaluebeans for tools
	 * @param list list of tools
	 * @return List of namevaluebeans fo rtools
	 */
	public static List getToolsList(List list)
	{
		List<NameValueBean> toolsList = new ArrayList<NameValueBean>();
		Iterator listIterator = list.iterator();
		while (listIterator.hasNext())
		{
			String toolName = listIterator.next().toString();
			NameValueBean nameValueBean = new NameValueBean(toolName, toolName);
			toolsList.add(nameValueBean);
		}
		return toolsList;
	}

}
