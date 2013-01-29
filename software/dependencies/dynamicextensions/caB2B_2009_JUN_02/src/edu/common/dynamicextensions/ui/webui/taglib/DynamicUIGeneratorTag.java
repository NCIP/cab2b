/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.ui.webui.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.util.logger.Logger;

public class DynamicUIGeneratorTag extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	protected ContainerInterface containerInterface = null;

	/**
	 * 
	 * @return
	 */
	public ContainerInterface getContainerInterface()
	{
		return containerInterface;
	}

	/**
	 * 
	 * @param containerInterface
	 */
	public void setContainerInterface(ContainerInterface containerInterface)
	{
		this.containerInterface = containerInterface;
	}

	/**
	 * Validates all the attributes passed to the tag
	 * @return boolean - true if all the attributes passed to the tag are valid
	 * @since TODO
	 */
	private boolean isDataValid()
	{
		if (this.getContainerInterface() == null)
		{
			Logger.out.debug("Container interface is null");
			return false;
		}
		return true;
	}

	/**
	 * This method contains no operations.
	 * @return int SKIP_BODY
	 * @since TODO
	 */
	public int doStartTag()
	{
		Logger.out.debug("Entering Selector List Tag ...");
		return SKIP_BODY;
	}

	/**
	 * 
	 */
	public int doEndTag()
	{
		if (!isDataValid())
		{
			return EVAL_PAGE;
		}
		try
		{
			String generatedHTML = this.containerInterface.generateContainerHTML();
			JspWriter out = pageContext.getOut();
			out.println(generatedHTML);
		}
		catch (DynamicExtensionsSystemException e)
		{
			Logger.out.debug("DynamicExtensionsSystemException. No response generated.");
		}
		catch (IOException e)
		{
			Logger.out.debug("IOException. No response generated.");
		}
		return EVAL_PAGE;
	}


}
