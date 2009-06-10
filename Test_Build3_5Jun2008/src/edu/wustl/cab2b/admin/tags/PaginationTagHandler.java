/**
 * 
 */

package edu.wustl.cab2b.admin.tags;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author atul_jawale
 *
 */
public class PaginationTagHandler extends TagSupport
{

	private int totalNumberOfRecords = 0;

	private int recordsPerPage = 0;

	private int currentPageNumber = 0;
	
	private String cssClass ="";
	
	
	public int doStartTag() throws JspException
	{
		try
		{
			final JspWriter out = pageContext.getOut();
            final ServletRequest request = pageContext.getRequest();
            currentPageNumber =  Integer.parseInt((String) request.getParameter("currentPage"));
			String htmlCode ="<span class=\"font_blk_b\">"+currentPageNumber+"| ";
			final int maxLimit = (currentPageNumber+5 > totalNumberOfRecords) ? totalNumberOfRecords:currentPageNumber+5;
			for(int i=currentPageNumber+1;i<maxLimit;i++)
			{
			  htmlCode= htmlCode + "<a href=\"#\" class=\"set4\"> "+i+" </a> | ";
			}
			
			htmlCode = htmlCode+" <a href=\"#\" class=\"set4\">Next &gt;&gt;</a></span>";
			out.println(htmlCode);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.doStartTag();
	}

	/**
	 * @return the currentPageNumber
	 */
	public int getCurrentPageNumber()
	{
		return currentPageNumber;
	}

	/**
	 * @param currentPageNumber the currentPageNumber to set
	 */
	public void setCurrentPageNumber(final int currentPageNumber)
	{
		this.currentPageNumber = currentPageNumber;
	}

	/**
	 * @return the recordsPerPage
	 */
	public int getRecordsPerPage()
	{
		return recordsPerPage;
	}

	/**
	 * @param recordsPerPage the recordsPerPage to set
	 */
	public void setRecordsPerPage(final int recordsPerPage)
	{
		this.recordsPerPage = recordsPerPage;
	}

	/**
	 * @return the totalNumberOfRecords
	 */
	public int getTotalNumberOfRecords()
	{
		return totalNumberOfRecords;
	}

	/**
	 * @param totalNumberOfRecords the totalNumberOfRecords to set
	 */
	public void setTotalNumberOfRecords(final int totalNumberOfRecords)
	{
		this.totalNumberOfRecords = totalNumberOfRecords;
	}

	
	/**
	 * @return the cssClass
	 */
	public String getCssClass()
	{
		return cssClass;
	}

	
	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(final String cssClass)
	{
		this.cssClass = cssClass;
	}

	
	
}
