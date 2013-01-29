/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/*
 * Created on Oct 4, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.ui.webui.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.ui.webui.util.TreeData;
import edu.wustl.common.util.logger.Logger;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TreeGeneratorTag extends TagSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TreeData treeDataObject = null;
	private String fieldForSelectedObject=null;
	private String name=null;
	private String showExpanded =null;
	private String nodeClickedFunction = null;
	public String getFieldForSelectedObject()
	{
		return this.fieldForSelectedObject;
	}

	public String getShowExpanded()
	{
		return this.showExpanded;
	}

	public void setShowExpanded(String showExpanded)
	{
		this.showExpanded = showExpanded;
	}

	public void setFieldForSelectedObject(String fieldForSelectedObject)
	{
		this.fieldForSelectedObject = fieldForSelectedObject;
	}

	/**
	 */
	public TreeGeneratorTag()
	{
		super();
	}

	/**
	 * @return EVAL_BODY_INCLUDE
	 * @throws JspException : JSP Exception
	 */
	public int doStartTag() throws JspException
	{
		JspWriter jspWriter = pageContext.getOut();
		try
		{
			if (treeDataObject != null)
			{
				//Add hidden variable
				jspWriter.print("<input type='hidden' id='"+fieldForSelectedObject  +"' name='" + fieldForSelectedObject +"' value=''>");
				jspWriter.print("<input type='hidden' id='"+fieldForSelectedObject  +"Name' name='" + fieldForSelectedObject +"Name' value=''>");
				//Add the actual code for tree generation
				jspWriter.print("<div  valign='top' scroll='auto' style='overflow:auto;' >");
				jspWriter.print(treeDataObject.getTree(name,fieldForSelectedObject,showExpanded,nodeClickedFunction));
				jspWriter.print("</div>");
				
				//Expand first row
				/*jspWriter.print("<script language='JavaScript'> \n" + "<!-- \n" //+ toggleFunctionJSCode + changeSelectionJSCode
						+ "toggle('"+fieldForSelectedObject +"','"+name+"N0_0','"+name+"P00'); \n" + "// --> \n" + "</script>");*/

			}
			else
			{
				jspWriter.print("Tree Data obect null Please check");
			}
		}
		catch (IOException e)
		{
			Logger.out.error("Exception occured " + e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
	}
	/**
	 * @return EVAL_PAGE
	 * @throws JspException : JSP Exception
	 */
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}
	/**
	 * @return :TreeData object
	 */
	public TreeData getTreeDataObject()
	{
		return this.treeDataObject;
	}
	/**
	 * 
	 * @param treeDataObject TreeDataObject
	 */
	public void setTreeDataObject(TreeData treeDataObject)
	{
		this.treeDataObject = treeDataObject;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNodeClickedFunction()
	{
		return this.nodeClickedFunction;
	}

	public void setNodeClickedFunction(String nodeClickedFunction)
	{
		this.nodeClickedFunction = nodeClickedFunction;
	}

}
