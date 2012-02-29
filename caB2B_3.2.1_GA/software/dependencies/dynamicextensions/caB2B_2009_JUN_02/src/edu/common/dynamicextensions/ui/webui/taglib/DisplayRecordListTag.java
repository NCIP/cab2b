/**
 * 
 */

package edu.common.dynamicextensions.ui.webui.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.entitymanager.EntityRecord;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * @author chetan_patil
 *
 */
public class DisplayRecordListTag extends TagSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	protected String containerIdentifier = null;
	/**
	 * 
	 */
	protected List<EntityRecord> entityRecordList = null;
	/**
	 * 
	 */
	protected String mode = null;

	/**
	 * Validates all the attributes passed to the tag
	 * @return boolean - true if all the attributes passed to the tag are valid
	 */
	private boolean isDataValid()
	{
		if (this.getEntityRecordList() == null)
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
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer
					.append("<div style='border:solid 1px; padding:1px; height:500px; overflow:auto;' width='100%'>");
			stringBuffer
					.append("<table class='dataTable' width='100%' cellpadding='4' cellspacing='0' border='1'>");

			stringBuffer.append("<thead><tr class='formTitle'>");
			stringBuffer.append("<th width='5%' align='center'>");
			stringBuffer.append(ApplicationProperties.getValue("record.id"));
			stringBuffer.append("</th><th>Delete</th>");
			stringBuffer.append("</tr></thead>");

			stringBuffer.append("<tbody>");
			for (EntityRecord entityRecord : this.entityRecordList)
			{
				stringBuffer.append("<tr><td>");

				Long recordId = entityRecord.getRecordId();
				if (recordId != null)
				{
					//					stringBuffer.append("<input type='checkbox' name='deleteCheckbox' id='deleteCheckbox' value=" + recordId.toString() + "/>");
					//					stringBuffer.append("</td><td>");

					stringBuffer.append("<span style='cursor:hand' ");
					stringBuffer.append("onclick=\"setRecordListTarget(");

					String target = "'/dynamicExtensions/LoadDataEntryFormAction.do?containerIdentifier="
							+ this.containerIdentifier
							+ "&recordIdentifier="
							+ recordId.toString()
							+ "&showFormPreview=false" + "&mode=" + mode + "'";

					stringBuffer.append(target);
					stringBuffer.append(")\">");

					stringBuffer.append("Record No. " + recordId.toString());
					stringBuffer.append("</span>");
					stringBuffer.append("</td><td>");

					stringBuffer.append("<span style='cursor:hand' ");
					stringBuffer.append("onclick='deleteRecord(" + this.containerIdentifier + " , "
							+ recordId.toString() + " ,\"" + mode + "\"" + ")'>");
					stringBuffer.append("Delete </span>");

				}
				stringBuffer.append("</td></tr>");

			}

			stringBuffer.append("</tbody>");

			stringBuffer.append("</table></div>");

			JspWriter out = pageContext.getOut();
			out.println(stringBuffer.toString());
		}
		catch (IOException e)
		{
			Logger.out.debug("IOException. No response generated.");
		}
		return EVAL_PAGE;
	}

	/**
	 * @return the recordSnippetList
	 */
	public List<EntityRecord> getEntityRecordList()
	{
		return entityRecordList;
	}

	/**
	 * @param recordSnippetList the recordSnippetList to set
	 */
	public void setEntityRecordList(List<EntityRecord> entityRecordList)
	{
		this.entityRecordList = entityRecordList;
	}

	/**
	 * @return the containerIdentifier
	 */
	public String getContainerIdentifier()
	{
		return containerIdentifier;
	}

	/**
	 * @param containerIdentifier the containerIdentifier to set
	 */
	public void setContainerIdentifier(String containerIdentifier)
	{
		this.containerIdentifier = containerIdentifier;
	}

	/**
	 * @return the mode
	 */
	public String getMode()
	{
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode)
	{
		this.mode = mode;
	}

}