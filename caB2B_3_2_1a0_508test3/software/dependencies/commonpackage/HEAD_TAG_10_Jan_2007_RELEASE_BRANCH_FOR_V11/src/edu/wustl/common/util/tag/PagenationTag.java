
package edu.wustl.common.util.tag;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Divyabhanu Singh
 *@version 1.0
 */

public class PagenationTag extends TagSupport
{

    protected String name = "Bhanu";
    protected int pageNum = 1;
    protected String prevPage = null;
    protected int totalResults = 1000;
    protected int numResultsPerPage = 15;
    protected int m_pageLinkStart = 1; 
    protected int m_pageLinkEnd = 10;
    protected boolean m_showNext;
    protected String searchTerm = null;
    protected String searchTermValues = null;
    protected String [] selectedOrgs = null;
    private int numLinks = 10;
    private int resultLowRange = 1;
    private int resultHighRange = 1;
    private String pageName = null;
    protected boolean showPageSizeCombo=false;
    protected int[] recordPerPageList = Constants.RESULT_PERPAGE_OPTIONS;

    public int doStartTag()
    {
        try
        {
        	m_showNext = true;
            JspWriter out = pageContext.getOut();
            if (getPageName().equals("SpreadsheetView.do"))  //pageName = SpreadsheetView for ViewResults page (SimpleSearchDataView.jsp) 
            {
            	out.println("<table class=\"black_ar\" border=0 bordercolor=#FFFFFF width=98% >");
            }
            else
            {
            	out.println("<table class=\"black_ar\" border=0 bordercolor=#FFFFFF width=38%>");
            }

            if (pageNum > numLinks)
            {
                if (pageNum % numLinks != 0)
                    m_pageLinkStart = ((pageNum / numLinks) * numLinks + 1);
                else
                    m_pageLinkStart = (pageNum - numLinks) + 1;
            }
            else
                //For first time or for PageNum < 10.
                m_pageLinkStart = 1;

            //Set the values of the ending Links on the Page.
            //This checks if number of Results left in the arrayList is less than numResults i.e. showNext==zero
//            System.out.println("totalResults = " + totalResults
//                    + "  numResultsPerPage = " + numResultsPerPage
//                    + " m_pageLinkStart = " + m_pageLinkStart);
//           System.out.println(" totalResults "+totalResults+" numResultsPerPage =  "+numResultsPerPage+"  ");
            if (numResultsPerPage==Integer.MAX_VALUE) // If user has opted to view all Records on this page.
            {
            	m_pageLinkStart =1;
            	m_pageLinkEnd = 1;
            	m_showNext = false;
            	resultLowRange = 1;
            	resultHighRange = totalResults;
            }
            else
            {
            	if ((totalResults - ((m_pageLinkStart - 1) * numResultsPerPage)) >= numResultsPerPage
                        * numLinks)
                {
                    m_pageLinkEnd = m_pageLinkStart + (numLinks - 1);

                }
                else
                {
                    if ((totalResults - (m_pageLinkStart * numResultsPerPage)) > 0)
                    {
                        if (totalResults % numResultsPerPage == 0)
                        {
                            m_pageLinkEnd = (m_pageLinkStart + (totalResults - (m_pageLinkStart * numResultsPerPage))
                                    / numResultsPerPage);
                        }
                        else
                        {
                            m_pageLinkEnd = (m_pageLinkStart + (totalResults - (m_pageLinkStart * numResultsPerPage))
                                    / numResultsPerPage) + 1;
                        }
                    }
                    else
                    {
                        m_pageLinkEnd = (m_pageLinkStart + (totalResults - (m_pageLinkStart * numResultsPerPage))
                                / numResultsPerPage);

                    }
                    //            System.out.println("totalResults = " + totalResults
//                  + "  m_pageLinkStart" + m_pageLinkStart
//                  + " numResultsPerPage = " + numResultsPerPage
//                  + " numLinks = " + numLinks+" m_pageLinkEnd = "+m_pageLinkEnd);
          // If we have exhausted our resultset, then set m_showNext as false. which means NEXT link must not be shown
          
          /*
           * Changed by Jitendra on 20/09/06.
           * Previously for some conditions it was failing because of which Next>> link was not showing for some conditions.
           */
                    
                
                }
            	
            	if ((m_pageLinkEnd*numResultsPerPage >= totalResults))
                {
                	m_showNext = false;
                }
            	
            	resultLowRange = (pageNum - 1) * numResultsPerPage + 1;
	            if (totalResults - ((pageNum - 1) * numResultsPerPage) < numResultsPerPage)
	            {
	                resultHighRange = resultLowRange + totalResults
	                        - ((pageNum - 1) * numResultsPerPage) - 1;
	            }
	            else
	            {
	                resultHighRange = resultLowRange + numResultsPerPage - 1;
	            }
            }

//            System.out.println("resultLowRange = "+resultLowRange+" resultHighRange "+resultHighRange+" pageNum = "+pageNum);
            if (!getPageName().equals("SpreadsheetView.do"))  //pageName = SpreadsheetView for ViewResults page (SimpleSearchDataView.jsp) 
            {
            	out.println("<tr> <td class = \"formtextbg\" align=\"CENTER\">"+name+"</td>");
            }
            
            if (showPageSizeCombo)
            {
                // Showing combo for Records/page values.
                
                String options = "";
                
                int possibleResultPerPageValues[] = putValueIfNotPresent(recordPerPageList, numResultsPerPage);
                
            	for (int i=0;i<possibleResultPerPageValues.length;i++)
            	{
            		int value = possibleResultPerPageValues[i];
            		String name = possibleResultPerPageValues[i]+"";
            		
            		if (value==Integer.MAX_VALUE)
            			name = "All";
            		
            		if (possibleResultPerPageValues[i]==numResultsPerPage)
            			options = options + "<option value=\"" + value + "\" selected=\"selected\" >"+name + "</option>";
            		else	
            			options = options + "<option value=\"" + value + "\">"+name + "</option>";
            	}
            	
            	
            	out.println("<td>Records Per Page <select name=\"recordPerPage\" size=\"1\" onChange=\"javascript:changeRecordPerPage("+(m_pageLinkStart)+",this,'"+pageName+"')\""+
            			" >" + options + "</select> <td>");  
            	
            }
            //Mandar 19-Apr-06 : 1697 :- Summary shows wrong data. Checking for zero records.
            if(totalResults > 0)
            {
            out.println("<td  align = \"CENTER\" class = \"formtextbg\">"
                            + resultLowRange
                            + " - "
                            + resultHighRange
                            + " of "
                            + totalResults + "</td>");
            }
            else
            {
                out.println("<td  align = \"CENTER\" class = \"formtextbg\">Showing Results "
                        + "0"
                        + " - "
                        + "0"
                        + " of "
                        + "0" + "</td>");
            }
            //Mandar 19-Apr-06 : 1697 :- Summary shows wrong data. end

            if ((m_pageLinkEnd) > numLinks)
            {
                out.print("<td align=\"CENTER\"><a href=\"javascript:send("+(m_pageLinkStart -1)+","+numResultsPerPage+",'"+prevPage+"','"+pageName+"')"
                                    + "\"> &lt;&lt;  </a></td>");
            }
            else
            {

                out.print("<td align=\"CENTER\">&nbsp;</td>");
            }

            int i = m_pageLinkStart;
            for (i = m_pageLinkStart; i <= m_pageLinkEnd; i++)
            {
                if (i != pageNum)
                {
                    out.print("<td align=\"CENTER\"> <a href=\"javascript:send("+i+","+numResultsPerPage+",'"+prevPage+"','"+pageName+"')"
                                    + "\">"
                                    + i + " </a></td>");
                }
                else
                {
                    out.print("<td align=\"CENTER\">" + i + " </td>");
                }
            }
            if (m_showNext == true)
            {
                out.print("<td align=\"CENTER\"><a href=\"javascript:send("+i+","+numResultsPerPage+",'"+prevPage+"','"+pageName+"')"
                                +"\"> >>  </a> </td>");
            }
            else
            {
                out.print("<td align=\"CENTER\">&nbsp;</td>");
            }
            out.print("</tr></table>");

        }
        catch (IOException ioe)
        {
        	Logger.out.debug("Error generating prime: " + ioe, ioe);
        }
        catch (Exception e)
        {
        	Logger.out.debug(e.getMessage(),e);
        }
        return (SKIP_BODY);
    }

    private int[] putValueIfNotPresent(int originalArray[], int value)
    {
    	for (int i=0;i<originalArray.length;i++)
    	{
    		if (value==originalArray[i]) //if array contains the value, then return same array. 
    			return originalArray;
    	}
    	
    	int newArray[]=new int[originalArray.length+1]; // array doesn't containe value, hence define new array.
    	int i=0;

    	for (;i<originalArray.length && value>originalArray[i];i++) // copying all elements less than value.
    		newArray[i] = originalArray[i];

    	newArray[i++] = value;
    	
    	for (;i<newArray.length;i++) // moving array elements 1 position next.
    	{
    		newArray[i] = originalArray[i-1];
    	}
    	return newArray;
		
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @param pageNum The pageNum to set.
     */
    public void setPageNum(int pageNum)
    {
        try
        {
            this.pageNum = pageNum;
        }
        catch (NumberFormatException nfe)
        {
            this.pageNum = 1;
            nfe.printStackTrace();
        }

    }

    /**
     * @param totalResults The totalResults to set.
     */
    public void setTotalResults(int totalResults)
    {
        try
        {
            this.totalResults = totalResults;
        }
        catch (NumberFormatException nfe)
        {
            this.totalResults = 1000;
        }
    }

    /**
     * @param numResultsPerPage The numResultsPerPage to set.
     */
    public void setNumResultsPerPage(int numResultsPerPage)
    {
        try
        {
            this.numResultsPerPage = numResultsPerPage;
        }
        catch (NumberFormatException nfe)
        {
            this.numResultsPerPage = 10;
        }
    }

    /**
     * @param searchTerm The searchTerm to set.
     */
    public void setSearchTerm(String searchTerm)
    {
        this.searchTerm = searchTerm;
    }
    /**
     * @param searchTermvalues The searchTermvalues to set.
     */
    public void setSearchTermValues(String searchTermvalues)
    {
        this.searchTermValues = searchTermvalues;
    }
    /**
     * @param selectedOrgs The selectedOrgs to set.
     */
    public void setSelectedOrgs(String[] selectedOrgs)
    {
        this.selectedOrgs = selectedOrgs;
    }
    /**
     * @return Returns the prevPage.
     */
    public String getPrevPage()
    {
        return prevPage;
    }
    /**
     * @param prevPage The prevPage to set.
     */
    public void setPrevPage(String prevPage)
    {
        this.prevPage = prevPage;
    }
    /**
     * @return Returns the pageName.
     */
    public String getPageName()
    {
        return pageName;
    }
    /**
     * @param pageName The pageName to set.
     */
    public void setPageName(String pageName)
    {
        this.pageName = pageName;
    }
    
    
	/**
	 * @param showPageSizeCombo The showPageSizeCombo to set.
	 */
	public void setShowPageSizeCombo(boolean showPageSizeCombo)
	{
		this.showPageSizeCombo = showPageSizeCombo;
	}
	
	
	/**
	 * @param recordPerPageList The recordPerPageList to set.
	 */
	public void setRecordPerPageList(int[] recordPerPageList)
	{
		this.recordPerPageList = recordPerPageList;
	}
}