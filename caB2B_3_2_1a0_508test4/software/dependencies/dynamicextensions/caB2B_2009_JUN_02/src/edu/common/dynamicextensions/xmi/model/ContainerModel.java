/**
 * 
 */
package edu.common.dynamicextensions.xmi.model;

import java.util.List;

import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.interfaces.ContainerUIBeanInterface;
import edu.common.dynamicextensions.ui.interfaces.EntityUIBeanInterface;


/**
 * @author ashish_gupta
 *
 */
public class ContainerModel implements EntityUIBeanInterface, ContainerUIBeanInterface
{
	protected String conceptCode;
	/**
	 * CreateAs
	 */
	protected String createAs = ProcessorConstants.DEFAULT_FORM_CREATEAS;
	/**
	 * Name : 
	 */
	protected String formName;

	/**
	 * Description
	 */
	protected String formDescription;
	/**
	 * 
	 */
	protected String isAbstract;
	/**
	 * 
	 */
	protected String formCaption;
	/**
	 * 
	 */
	protected List formList;
	/**
	 * 
	 */
	protected String parentForm;
	

	public String getButtonCss()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getMainTableCss()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getRequiredFieldIndicatior()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getRequiredFieldWarningMessage()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitleCss()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setButtonCss(String buttonCss)
	{
		// TODO Auto-generated method stub
		
	}

	public void setMainTableCss(String mainTableCss)
	{
		// TODO Auto-generated method stub
		
	}	

	public void setRequiredFieldIndicatior(String requiredFieldIndicatior)
	{
		// TODO Auto-generated method stub
		
	}

	public void setRequiredFieldWarningMessage(String requiredFieldWarningMessage)
	{
		// TODO Auto-generated method stub
		
	}

	public void setTitleCss(String titleCss)
	{
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * @return the conceptCode
	 */
	public String getConceptCode()
	{
		return conceptCode;
	}

	
	/**
	 * @param conceptCode the conceptCode to set
	 */
	public void setConceptCode(String conceptCode)
	{
		this.conceptCode = conceptCode;
	}

	
	/**
	 * @return the createAs
	 */
	public String getCreateAs()
	{
		return createAs;
	}

	
	/**
	 * @param createAs the createAs to set
	 */
	public void setCreateAs(String createAs)
	{
		this.createAs = createAs;
	}

	
	/**
	 * @return the formCaption
	 */
	public String getFormCaption()
	{
		return formCaption;
	}

	
	/**
	 * @param formCaption the formCaption to set
	 */
	public void setFormCaption(String formCaption)
	{
		this.formCaption = formCaption;
	}

	
	/**
	 * @return the formDescription
	 */
	public String getFormDescription()
	{
		return formDescription;
	}

	
	/**
	 * @param formDescription the formDescription to set
	 */
	public void setFormDescription(String formDescription)
	{
		this.formDescription = formDescription;
	}

	
	/**
	 * @return the formList
	 */
	public List getFormList()
	{
		return formList;
	}

	
	/**
	 * @param formList the formList to set
	 */
	public void setFormList(List formList)
	{
		this.formList = formList;
	}

	
	/**
	 * @return the formName
	 */
	public String getFormName()
	{
		return formName;
	}

	
	/**
	 * @param formName the formName to set
	 */
	public void setFormName(String formName)
	{
		this.formName = formName;
	}

	
	/**
	 * @return the isAbstract
	 */
	public String getIsAbstract()
	{
		return isAbstract;
	}

	
	/**
	 * @param isAbstract the isAbstract to set
	 */
	public void setIsAbstract(String isAbstract)
	{
		this.isAbstract = isAbstract;
	}

	
	/**
	 * @return the parentForm
	 */
	public String getParentForm()
	{
		return parentForm;
	}

	
	/**
	 * @param parentForm the parentForm to set
	 */
	public void setParentForm(String parentForm)
	{
		this.parentForm = parentForm;
	}

}
