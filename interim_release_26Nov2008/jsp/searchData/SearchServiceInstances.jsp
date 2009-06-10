<%@page import="edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic"%>
<%@page import="java.util.List"%>
<%@page autoFlush="true" buffer="4kb" %>
<%@taglib prefix="page" uri="/custom-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@include file="/jsp/Pagination.jsp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.wustl.cab2b.common.user.AdminServiceMetadata"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>caBench-To-Bedside</title>

<link href="css/cab2b.css" rel="stylesheet" type="text/css" />

<link href="css/cab2b_1024.css" rel="stylesheet" type="text/css">
<script type="text/JavaScript" src="javascript/menu_popup.js">
</script>
<script type="text/JavaScript" src="javascript/queryModule.js">
</script>

<script>
 window.onload = getFocus;

function getFocus(){	
	document.getElementById('searchText').focus();
	}


function back(pageNo,searchString)
{

  var frm = document.getElementById("serviceForm");
  frm.action = "LoadServiceModels.action?pager.offset="+pageNo+"&textbox="+searchString;
  frm.submit();
}

 function selectAll(isSelect)
 {
  
   var checkbox_names= document.getElementById("chkboxnames").value;
   var boxNameArray = checkbox_names.split("::"); 
   for(i=1;i<boxNameArray.length;i++)
   {
     var chkname= boxNameArray[i]+"_checkbox";
     var checkbox11 = document.getElementById(chkname);
     checkbox11.checked = isSelect;
    }
 
 } 
 
 function setSelectedValues()
 {
    var checkbox_names= document.getElementById("chkboxnames").value;
    var boxNameArray = checkbox_names.split("::"); 
    var serviceName ="";
	for(i=1;i<boxNameArray.length;i++)
     {
      var chkname= boxNameArray[i]+"_checkbox";
      var checkbox11 = document.getElementById(chkname);
      if(checkbox11.checked)
      {
       serviceName = serviceName+'|'+boxNameArray[i];
      }
    }
    var hiddenField = document.getElementById("selectedValues");
    hiddenField.value =    serviceName;
   
 }

function deselectSelectAll()
{
    var selectAll  = document.getElementById("selectAll");
    selectAll.checked = false;
   

}


</script>

<script type="text/JavaScript" src="javascript/menu_create.js">
</script>
</head>

<%
            List<AdminServiceMetadata> records = (List) session.getAttribute(AdminConstants.FILTERED_SERVICE_INSTANCES);
			records = records==null? new ArrayList<AdminServiceMetadata>():records;
 			Integer pageNum = new Integer(0);
            String searchString = request.getParameter("textbox");
            String pageTitle = searchString==null?"Available Models":"Search for "+searchString;
			searchString = searchString==null?"":searchString;
            String selection = "";
             String serviceName = request.getParameter("serviceName");  
             String chkboxNames = "";
             String isDisable = records.size()==0?"disabled":"";
%>
<!--Begin content area -->


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
 -->             </table>
                <form id="advanceSearch" method="post"  action="SearchServiceInstances.action">
                  <table width="99%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td align="left"><table border="0" cellpadding="2" cellspacing="0">
                        
                        <tr>
                          <td rowspan="2" align="left" width="15"></td>
                          <td align="left"><span class="font_blk_s"> Search For </span></td>
                          <td align="left">&nbsp;<input id="searchText"  type="text" class="input_tbox" name="textbox" value="<%=searchString%>" onKeyDown="setFocusOnSearchButton(event)" size="30" /></td>
                          <td align="left"><input  type="checkbox" id ="includeDescription" name="includeDescription" value="checked" ${param.includeDescription}/>
                              <span class="font_blk_s"> Include Description </span></td>
                          <td align="left">
                            <input  type="button" name="Search2" id="searchButton"  onClick="checkTextBox()"  value="Search"  />
                            <input type="hidden" id="serviceName" name="serviceName" value="<%=serviceName%>"/>
                          </td>
						 
                          
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td colspan="3" align="left"><span class="font_blk_s">(E.g.
								caArray , caTissue )</span></td>
                        </tr>
                        <tr>
                          <td align="left" class="font_blk_b" >&nbsp;</td>
                          <td colspan="3" align="left"  >&nbsp;</td>
                        </tr>
                      </table></td>
                    </tr>
                  </table>
                </form></td>
            </tr>
         <pg:pager items="<%= records.size() %>" index="<%= index %>"
					maxPageItems="<%= maxPageItems %>"
					maxIndexPages="<%= maxIndexPages %>" isOffset="<%= true %>"
					export="offset,currentPageNumber=pageNumber" scope="request">
<%					if(records.size()>0)
					{pageNum =offset+1; }
%>					                            
              <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td colspan="2"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                          <td align="left" valign="middle" background="images/title_bg.gif" class="font_bl3_b" ><img src="images/c1.gif" width="12" height="24" align="absmiddle" />&nbsp;&nbsp;<%=request.getParameter("serviceName")%> (<%=records.size()%>)</td>
                          <td align="right" valign="middle" background="images/title_bg.gif" >
                          <%                     if(records.size()>0)
                      {
 					   
 %>                         <span class="td_dblue2"><span class="font_blk_b">Results:</span> <span class="font_blk_s"><%=pageNum%> to <%=Math.min(offset.intValue() + maxPageItems, records.size())%>
						   of <%=records.size()%> </span></span>
<%					  }
					else
					{	
					    selection = "disabled";
%>					
	
<%					}	 
%>					
</td>
                          <td width="12" align="right" valign="top" background="images/title_bg.gif" class="td_dblue2"><img src="images/c2.gif" width="12" height="24" /></td>
                        </tr>
                        <tr>
                          <td colspan="3" align="left" valign="top" class="td_orange"><img src="images/spacer.gif" width="1" height="1" /></td>
                        </tr>
                    </table></td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="25" align="center"><input type="checkbox" name="selectAll"  id="selectAll" value="checkbox" <%=selection%> onClick="selectAll(this.checked);"/></td>
                    <td class="font_blk_b" >Select All</td>
                  </tr>
                  <tr>
                    <td height="25" colspan="2" align="center"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
                        <tr>
                          <td class="td_dgrey"><img src="images/spacer.gif" width="1" height="1" /></td>
                        </tr>
                    </table></td>
                  </tr>
				<form  id="serviceForm"  method="post" action="SaveServiceInstance.action"> 
		
				                 
<%
			if(records.size()>0)
			{
              for (int i = offset.intValue(), l = Math.min(i + maxPageItems, records.size()); i < l; i++) 
              {
                AdminServiceMetadata metadata = records.get(i);
                chkboxNames =chkboxNames+"::"+metadata.getHostingResearchCenter();
                String selected = metadata.isConfigured()?"checked":"";
					%>
					<pg:item>                       
                  <tr>
                    <td align="center" valign="top"><input type="checkbox" name="checkedServiceInstances" <%=selected %> value="<%=metadata.getHostingResearchCenter()%>"	id="<%=metadata.getHostingResearchCenter()%>_checkbox"  onClick="deselectSelectAll();"/></td>
                    <td ><span class="font_bl1_b"><%=metadata.getHostingResearchCenter()%></span> <br />
                        <span class="font_blk_s">  <%=metadata.getSeviceDescription()%>&hellip;..</span></td>
                  </tr>
                  <tr align="center">
                    <td colspan="2">&nbsp;</td>
                  </tr>
  </pg:item>
		<%
		}
			}
		else if(request.getAttribute("serviceInstanceError")!=null)
		{	
					%>   
 <tr>
                  <td align="center" valign="top"></td>
                  <td ><span class="font_blk_b"><%=request.getAttribute("serviceInstanceError")%>.</span>
                      </td>
                </tr>
<%} else   
    {%>
    	<tr>
                  <td align="center" valign="top"></td>
                  <td ><span class="font_blk_b">No active service instances found for <%=request.getParameter("serviceName")%>.</span>
                      </td>
        </tr>
    
  <%}%>  
				<% if(searchString.length()>0)
   {
    %>				
				 <tr>
                    <td align="center" valign="top"></td>
                    <td ><span class="font_bl1_b"><a href="SearchServiceInstances.action?serviceName=<%=serviceName%>" CLASS="set3">Show All Service Instances</a></span> </td>
                      </tr>
	<%} %>	
                  <tr>
                    <td colspan="2"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
                        <tr>
                          <td class="td_dgrey"><img src="images/spacer.gif" width="1" height="1" /></td>
                        </tr>
                    </table></td>
                  </tr>
                  <tr>
                    <td colspan="2" align="center" valign="middle">&nbsp;<input type="hidden" id="chkboxnames" value="<%=chkboxNames%>"></td>
                  </tr>
                  <tr>
                    <td width="25" align="center" valign="middle"><input type="hidden" id="serviceName" name="serviceName" value="<%=serviceName%>"/>
<% 
	if(records.size()>1)
	{
%>                    
                    <img src="images/ic_page2.gif" width="14" height="16" />
<% } %>                    
                    </td>
                    <td class="font_blk_b">
<%				if(records.size()>1)
				{
%>                  
                     <pg:prev>
							<%
							                                    String url = request.getAttribute("URL")
							                                    + pageUrl.substring(pageUrl.indexOf("?"))+"&serviceName="+serviceName;
							%>
							<a href="<%= url %>" class="set4"><nobr>[<< Prev ]</nobr></a>
						</pg:prev> <pg:pages>
							<%
							if (pageNumber == currentPageNumber) {
							%>
							<b><%=pageNumber%>&nbsp;|</b>
							<%
							                                } else {
							                                String url = request.getAttribute("URL")
							                                        + pageUrl.substring(pageUrl.indexOf("?"))+"&serviceName="+serviceName;
							%>
							<a href="<%=url %>" class="set4"> <%=pageNumber%>&nbsp;|</a>
							<%
							}
							%>
						</pg:pages> <pg:next>
							<%
							                                    String url = request.getAttribute("URL")
							                                    + pageUrl.substring(pageUrl.indexOf("?"))+"&serviceName="+serviceName;
							%>
							<a href="<%= url  %>" class="set4"><nobr>[ Next >> ]</nobr></a>
						</pg:next>&gt;&gt;</a>
<%} %>						                    
                                        
                    </td>
                  </tr>
                  <tr>
                    <td colspan="2" align="right" valign="middle">&nbsp;</td>
                  </tr>
              </table></td>
            </tr>
            <tr>
              <td colspan="3" align="left" valign="top" class="td_white"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="td_white">
                  <tr>
                    <td><table width="100%" border="0" cellpadding="0" cellspacing="0" >
                        <tr>
                          <td class="td_dgrey"><img src="images/spacer.gif" width="1" height="1" /></td>
                        </tr>
                    </table></td>
                  </tr>
                  <tr>
                    
                      <td height="35" align="left" class="td_grey">&nbsp;&nbsp;&nbsp;
                          <input name="Submit22" type="button" class="font_bl1_b" onclick="back('${param.fromPage}','${param.searchString}');" value="&lt;&lt; Back" />
                        &nbsp;&nbsp;&nbsp;
                        <input name="Submit2" type="submit" class="font_bl1_b" <%=isDisable%>  value="Save Settings" />
                        &nbsp;&nbsp;&nbsp;</td>
                   
                  </tr>
              </table></td>
               </form>
            </tr>
             </pg:pager>          </table></td>
        </tr>
      </table>
      <!--end content area -->
      
      
      