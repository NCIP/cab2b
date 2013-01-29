<%--L
  Copyright Georgetown University, Washington University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@page import="edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic" %>
<%@page import="java.util.List"%>
<%@page	import="edu.common.dynamicextensions.domaininterface.EntityGroupInterface"%>
<%@taglib prefix="page" uri="/custom-tags" %> 
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %> 
<%@taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager" %> 
<%@include file="/jsp/Pagination.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>caBench-To-Bedside</title>



<script type="text/JavaScript" src="javascript/set_resolution.js">
</script>
<script type="text/javascript" src="javascript/ajax.js"></script> 
<script type="text/javascript" src="javascript/wz_tooltip.js"></script> 
<script type="text/JavaScript" src="javascript/menu_popup.js">
</script>
<script type="text/JavaScript" src="javascript/menu_create.js">
</script>
<script type="text/JavaScript" src="javascript/queryModule.js">
</script>
<script>
	
	 window.onload = getFocus;

	function getFocus(){	
	document.getElementById('searchText').focus();
	}


</script>


</head>
<%!
public String filterSpecialChars(String value) {
        if (value == null) { return "";
        }

        StringBuffer result = new StringBuffer(value.length());
        for (int i=0; i<value.length(); ++i) {
          switch (value.charAt(i)) {

                case '<':
                        result.append("&lt;");
                        break;
                case '>':
                        result.append("&gt;");
                        break;
                case '"':
                        result.append("&quot;");
                        break;
                case '\'':
                        result.append("&#39;");
                        break;
                case '%':
                        result.append("&#37;");
                        break;
                case ';':
                        result.append("&#59;");
                        break;
                case '(':
                        //result.append("&#40;");
                        break;
                case ')':
                        //result.append("&#41;");
                        break;
                case '&':
                        result.append("&amp;");
                        break;
                case '+':
                        result.append("&#43;");
                        break;
                default:
                        result.append(value.charAt(i)); break;
          }
        }
        return result.toString();
}
%>


<%
 List<EntityGroupInterface> loadEntity = (List) session.getAttribute(AdminConstants.FILTERED_LOADED_MODELS); 
  Integer pageNum = new Integer(0); 
  //String searchString = request.getParameter("textbox");
  String searchString = filterSpecialChars(request.getParameter("textbox"));
  String pageTitle = searchString==null||searchString.length()==0?"Available Models":"Search for "+searchString;
  searchString = searchString==null?"":searchString;
%>
<!--Begin Content area -->
      <table width="100%" border="0" cellpadding="0" cellspacing="0">

		 <tr>
          <td height="35" colspan="4" align="left" valign="top" background="\\">
            <table width="200" border="0" cellpadding="0" cellspacing="0" class="td_dgrey">
              <tr>
                 <td><img src="images/spacer.gif" width="10" height="1"></td>
                 <td><img src="images/arrow.gif" width="12" height="11" hspace="5" align="absmiddle"><span class="font_bl2_b">Service Instances</span></td>
                 <td align="right" valign="top"><img src="images/c6.gif" width="15" height="23"></td>
               </tr>
            </table>
          </td>
       </tr>
        <tr>
          <td align="center" valign="top"><table width="100%" cellpadding="0" cellspacing="0" >
            
            <tr>
              <td align="center" valign="top" class="td_white"><table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
<!--                 <tr>
                  <td align="left" valign="middle" background="images/title_bg.gif" class="font_bl3_b"><img src="images/c1.gif" width="12" height="24" align="absmiddle" />&nbsp;&nbsp;Search </td>
                  <td align="left" valign="top" background="images/title_bg.gif" class="td_dblue2">&nbsp;</td>
                  <td align="right" valign="top" background="images/title_bg.gif" class="td_dblue2"><img src="images/c2.gif" width="12" height="24" /></td>
                </tr>
                <tr>
                  <td colspan="3" align="left" valign="top" class="td_orange"><img src="images/spacer.gif" width="1" height="1"></td>
                </tr>
 -->              </table>
                <form id="advanceSearch" method="post"  action="LoadServiceModels.action">
                  <table width="99%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td align="left"><table border="0" cellpadding="2" cellspacing="0">
                        
                        <tr>
                          <td rowspan="2" align="left" width="15">&nbsp;</td>
                          <td align="left"> <span class="font_blk_s"> Search For </span></td>
                          <td align="left">&nbsp;<input id="searchText"  type="text" class="input_tbox" name="textbox" value="<%= filterSpecialChars(searchString) %>" onKeyDown="setFocusOnSearchButton(event)" size="30" /></td>
                          <td align="left"><input  type="checkbox" id ="includeDescription" name="includeDescription" value="checked" ${param.includeDescription}/>
                              <span class="font_blk_s"> Include Description </span></td>
                          <td align="left">
                            <input  type="button" name="Search2" id="searchButton"  onClick="checkTextBox()"  value="Search"  />
                          </td>
                          <td align="right"></td>
						 
                          
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td colspan="3" align="left"><span class="font_blk_s">(E.g.
								caArray , caTissue )</span></td>
                        </tr>
                       
                      </table></td>
                    </tr>
                  </table>
                </form></td>
            </tr>
            <tr>
			<pg:pager   items="<%= loadEntity.size() %>" 
         	  index="<%= index %>" 
         	  maxPageItems="<%=5%>" 
         	  maxIndexPages="<%= maxIndexPages %>"
			  isOffset="<%= true %>"
			  export="offset,currentPageNumber=pageNumber"
			  scope="request">  
              <%					if(loadEntity.size()>0)
					{pageNum =offset+1; }
%>					  
              <td align="center" valign="top" class="td_white">
			    <table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td align="left" valign="middle" background="images/title_bg.gif" class="font_bl3_b"><img src="images/c1.gif" width="12" height="24" align="absmiddle" />&nbsp;&nbsp;<%=pageTitle%></td>
                    
                   

                    <td align="right" valign="middle" background="images/title_bg.gif" class="td_dblue2">
<% if(loadEntity.size()>0)
{  %>                      
                    <span class="font_blk_b">Results:</span> <span class="font_blk_s"><%=pageNum%> to <%=Math.min(offset.intValue() + maxPageItems, loadEntity.size()) %> of <%=loadEntity.size() %> </span></td>
   <% }%>     
       
                    <td width="12" align="right" valign="top" background="images/title_bg.gif" class="td_dblue2"><img src="images/c2.gif" width="12" height="24" /></td>
                  </tr>
                  <tr>
                    <td colspan="3" align="left" valign="top" class="td_orange"><img src="images/spacer.gif" width="1" height="1" /></td>
                  </tr>
                </table>
          	<form id="select_check_box" name="form4" method="post"  action="LoadSelectedModels.action">
				  <table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
				
                      <tr >
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                      </tr>
                      

<%      if(loadEntity.size()>0)
		{	
			StringBuilder description = null; 
			for (int i=offset.intValue(),l = Math.min(i + maxPageItems, loadEntity.size());	i < l; i++)
				{
				
					description=new StringBuilder(loadEntity.get(i).getDescription());
					 String descriptionWithoutComma = loadEntity.get(i).getDescription().replace("\'","\\'");
					if(description.length()>250)
						description= new StringBuilder(description.substring(0,250));
					String pagNumber = request.getParameter(AdminConstants.OFFSET_PARAMETER);
		                                                
%>         
		
		  <pg:item >                      
                      <tr>
                        <td align="left" colspan="2"><span class="font_bl1_b" onmouseover="Tip('<%=descriptionWithoutComma%>',400,200)"><a href="SearchServiceInstances.action?serviceName=<%=loadEntity.get(i).getLongName()%>&version=<%=loadEntity.get(i).getVersion()%>&fromPage=<%=pagNumber%>&searchString=<%=searchString%>" class="set4"><%=loadEntity.get(i).getLongName()%>  V<%=loadEntity.get(i).getVersion()%> </a></span><br />
                            <span class="font_blk_s"><%=description %>&hellip;</span></td>
                      </tr>
                      <tr align="center">
                        <td height="5" colspan="2" >&nbsp;</td>
                      </tr>
   </pg:item>

<%				}	
	}
else
{ %>                 
			<tr>
                        <td align="center" valign="top" >&nbsp;</td>
                        <td align="left" ><span class="font_blk_b">No records exists.</span> </td>
            </tr>
            <tr align="center">
                        <td height="25" colspan="2" >&nbsp;</td>
            </tr>
<%} %>                     
			<% if(searchString.length()>0)                      
 { %>

    		<tr>
                        <td align="left" ><span class="font_bl1_b"><a href="LoadServiceModels.action?action=showall" class="set4">Show All Models</a></span><br />
                            <span class="font_blk_s"></span></td>
                      </tr>
                      <tr align="center">
                        <td height="10" colspan="2" >&nbsp;</td>
                      </tr>
 <%} %>        

                      <tr>
                        <td height="15" colspan="2" align="center" valign="middle" class="font_blk_b" ><table width="100%" border="0" cellpadding="0" cellspacing="0" >
                            <tr>
                              <td class="td_dgrey"><img src="images/spacer.gif" width="1" height="1" /></td>
                            </tr>
                        </table></td>
                      </tr>
                      <tr>
						  <td colspan="2" align="left" valign="middle" class="font_blk_b" ><img src="images/ic_page2.gif"  hspace="0" align="absmiddle" />
<% 
if(loadEntity.size()>0){	
%> 
                        
                        	<pg:prev>
<%  String url = request.getAttribute("URL") + pageUrl.substring(pageUrl.indexOf("?"))+"&textbox="+filterSpecialChars(searchString);
%>            		
            		  <a href="<%= url %>" class="set4"><nobr>[<< Prev ]</nobr></a> </pg:prev>
                      <pg:pages>
<%					
				
						 if(pageNumber==currentPageNumber)
							{ %>
								   <b><%= pageNumber %>&nbsp;|</b>
						<%  }  
							else
							{String url = request.getAttribute("URL") + pageUrl.substring(pageUrl.indexOf("?"))+"&textbox="+filterSpecialChars(searchString);
								
								%>
							   <a href="<%=url %>" class="set4"> <%= pageNumber %>&nbsp;|</a> 
						  <%}%>
 					</pg:pages>
					<pg:next>
<%  String url = request.getAttribute("URL") + pageUrl.substring(pageUrl.indexOf("?"))+"&textbox="+filterSpecialChars(searchString);
%>
							<a href="<%= url  %>" class="set4"><nobr>[ Next  >> ]</nobr></a>					</pg:next>
                        
                        
                        </td>
                      </tr>
   <%} %>                   
                      
                  </table></td>
            </tr>
 </pg:pager>           
          <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="td_white">
              <tr>
                <td><table width="100%" border="0" cellpadding="0" cellspacing="0" >
                    <tr>
                      <td class="td_dgrey"><img src="images/spacer.gif" width="1" height="1" /></td>
                    </tr>
                </table></td>
              </tr>
              
            </table></td>
        </tr>
        
        <tr>
          <td align="center" valign="bottom">&nbsp;</td>
        </tr>
      </table>
      
		
