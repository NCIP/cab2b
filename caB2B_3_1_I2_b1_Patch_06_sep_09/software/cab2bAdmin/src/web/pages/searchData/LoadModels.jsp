<%@page import="java.util.List"%>
<%@page import="edu.wustl.cab2b.admin.beans.CaDSRModelDetailsBean"%>
<%@taglib prefix="page" uri="/custom-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@include file="/jsp/Pagination.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>caBench-To-Bedside</title>



<script type="text/JavaScript" src="javascript/set_resolution.js">
</script>

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




function goToPage(pageUrl)
{
  var frm = document.getElementById("select_check_box");
  frm.action = pageUrl;
  frm.submit();

}



	function checkCheckbox(formName)
	{
                var element ;
                var checkSelected =0;
                if(navigator.appName == "Microsoft Internet Explorer")               
                {
                   element = document.getElementById('select_check_box');        
                }
                else
                {
                   element = document.forms[formName];
                }
                var isSelected =document.getElementById('isModelSelected'); 
                if(isSelected.value=='true')
                {
                	element.submit();
                }
				else
				{
                    count =element.elements.length;
                    for (i=0; i < count; i++) 
			        {
			          if((element.elements[i].name=="checkboxValues") &&  (element.elements[i].checked==true ))
			          {
			            checkSelected=checkSelected+1;
			            continue;
			          }
			        }
				    if(checkSelected <1)
				    {
				      alert("please select atleast one model ");
			        }
			        else
			        {
				       element.submit();
			        }
               }

		}
</script>
</head>



<%
    List<CaDSRModelDetailsBean> loadModel = (List) session.getAttribute(AdminConstants.FILTERED_AVAILABLE_MODELS_TO_LOAD);
    CaDSRModelDetailsBean modelBean;
    Integer pageNum = new Integer(0);
    String searchString = request.getParameter("textbox");
    String pageTitle =  searchString==null||searchString.length()==0? "Available Models" : "Search for " + searchString;
    searchString = searchString == null ? "" : searchString;
    String isDisabled = "";
    Map<String, List<String>> allSelectedModelList =(Map<String, List<String>>) session.getAttribute(AdminConstants.ALL_SELECTED_MODELS_LIST);
	
    int size = allSelectedModelList==null?0:allSelectedModelList.size();
	String isSelected = "false";
	if(size>0)
	{
	    isSelected = "true";
	}
	
%>


<!--Begin content area -->
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td height="35" colspan="4" align="left" valign="top" background="\\">
		<table width="200" border="0" cellpadding="0" cellspacing="0" class="td_dgrey">
			<tr>
				<td><img src="images/spacer.gif" width="10" height="1"></td>
				<td><img src="images/arrow.gif" width="12" height="11" 	hspace="5" align="absmiddle"><span class="font_bl2_b">Load 		Models </span></td>
				<td align="right" valign="top"><img src="images/c6.gif" width="15" height="23"></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="center" valign="top">
		<table width="100%" cellpadding="0" cellspacing="0">

			<tr>
				<td align="center" valign="top" class="td_white">
				<table width="99%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<!--                 <tr>
                  <td align="left" valign="middle" background="images/title_bg.gif" class="font_bl3_b"><img src="images/c1.gif" width="12" height="24" align="absmiddle" />&nbsp;&nbsp;Search </td>
                  <td align="left" valign="top" background="images/title_bg.gif" class="td_dblue2">&nbsp;</td>
                  <td align="right" valign="top" background="images/title_bg.gif" class="td_dblue2"><img src="images/c2.gif" width="12" height="24" /></td>
                </tr>
                <tr>
                  <td colspan="3" align="left" valign="top" class="td_orange"><img src="images/spacer.gif" width="1" height="1"></td>
                </tr>
 -->
				</table>
				<form id="advanceSearch" method="post" action="LoadModels.action">
				<table width="99%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="left">
						<table border="0" cellpadding="2" cellspacing="0">

							<tr>
								<td rowspan="2" align="left">&nbsp;</td>
								<td align="left"><span class="font_blk_s"> Search
								For </span></td>
								<td align="left">&nbsp;<input id="searchText" type="text"
									class="input_tbox" name="textbox" 
									onKeyDown="setFocusOnSearchButton(event)"
									value="${param.textbox}" size="30" /></td>
								<td align="left"><input type="checkbox"
									id="includeDescription" name="includeDescription" value="checked" ${param.includeDescription} />
								<span class="font_blk_s"> Include Description </span></td>
								<td align="left"><input type="button" name="Search2"
									id="searchButton" onClick="checkTextBox()" value="Search" /></td>
								<td align="left"></td>

							</tr>
							<tr>
								<td>&nbsp;</td>
								<td colspan="3" align="left">&nbsp;<span class="font_blk_s">
								(E.g. caArray , caTissue )</span></td>
							</tr>

						</table>
						</td>
					</tr>
				</table>
				</form>
				</td>
			</tr>
			<tr>
				<pg:pager items="<%= loadModel.size() %>" index="<%= index %>"
					maxPageItems="<%=5%>" maxIndexPages="<%= maxIndexPages %>"
					isOffset="<%= true %>" export="offset,currentPageNumber=pageNumber"
					scope="request">
					<%
					    if (loadModel.size() > 0) {
					                    pageNum = offset + 1;
					                }
					%>
					<td align="center" valign="top" class="td_white">

					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td align="left" valign="middle" background="images/title_bg.gif"
								class="font_bl3_b"><img src="images/c1.gif" width="12"
								height="24" align="absmiddle" />&nbsp;&nbsp;<%=pageTitle%></td>




							<td align="right" valign="middle"
								background="images/title_bg.gif" class="td_dblue2">
							<%
							    if (loadModel.size() > 0) {
							%> <span class="font_blk_b">Results:</span> <span class="font_blk_s"><%=pageNum%>
							to <%=Math.min(offset.intValue() + maxPageItems, loadModel.size())%>
							of <%=loadModel.size()%> </span> <%
     }
 %>
							</td>
							<td width="12" align="right" valign="top"
								background="images/title_bg.gif" class="td_dblue2"><img
								src="images/c2.gif" width="12" height="24" /></td>
						</tr>
						<tr>
							<td colspan="3" align="left" valign="top" class="td_orange"><img
								src="images/spacer.gif" width="1" height="1" /></td>
						</tr>
					</table>
					<form id="select_check_box" name="form4" method="post"
						action="LoadSelectedModels.action">
					<table width="96%" border="0" align="center" cellpadding="0"
						cellspacing="0">



						<tr align="center">
							<td height="5" colspan="2">&nbsp;</td>
						</tr>



						<%
						    String page11 = "" + currentPageNumber;
						                List<String> selectedModels = null;
						                if (page11 != null) {
						                    selectedModels = (List<String>) session.getAttribute(page11);
						                    selectedModels = selectedModels == null ? new ArrayList<String>(0) : selectedModels;
						                }

						                if (loadModel.size() > 0) {
						                    StringBuilder description = null;
						                    for (int i = offset.intValue(), l = Math.min(i + maxPageItems, loadModel.size()); i < l; i++) {

						                        CaDSRModelDetailsBean model = loadModel.get(i);

						                        description = new StringBuilder(model.getDescription());
						                        String descriptionWithoutComma = model.getDescription().replace("\'","\\'");
						                       
						                        if (description.length() > 250)
						                            description = new StringBuilder(description.substring(0, 250));
						                        
						%>

						<pg:item>
							<tr>
								<%
								    String select = "";
								                            if (selectedModels != null && selectedModels.size() > 0) {
								                                if (selectedModels.contains(model.getLongName())) {
								                                    select = "checked";
								                                }
								                            }
								%>
								<td align="center" valign="top"><input type="checkbox"
									name="checkboxValues" <%=select %>
									value="<%=model.getLongName()%> v<%=model.getVersion()%>" /> </td>
								<td align="left"><span class="font_bl1_b" onmouseover="Tip('<%=descriptionWithoutComma%>',400,200)"><%=model.getLongName()%>&nbsp;  v<%=model.getVersion()%>
								</span><br />
								<span class="font_blk_s"><%=description%>&hellip;</span></td>
							</tr>
							<tr align="center">
								<td height="10" colspan="2">&nbsp;</td>
							</tr>
						</pg:item>
						<input type="hidden" name="pageNO" value="<%=currentPageNumber%>" />
						<%
						    }

						                } else {
						                    isDisabled = "disabled";
						%>
						<tr>
							<td align="center" valign="top">&nbsp;</td>
							<td align="left"><span class="font_blk_b">No records
							exists.</span></td>
						</tr>
						<tr align="center">
							<td height="25" colspan="2">&nbsp;</td>
						</tr>
						<%
						    }
						%>
						<%
						    if (searchString.length() > 0) {
						%>
						<tr align="center">
							<td height="5" colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td align="center" valign="top"></td>
							<td><span class="font_bl1_b"><a
								href="LoadModels.action" class="set4">Show All Models</a></span><br>
							</td>
						</tr>
						<%
						    }
						%>
						<tr>
							<td height="25" colspan="2" align="center" valign="middle"
								class="font_blk_b">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td class="td_dgrey"><img src="images/spacer.gif"
										width="1" height="1" /></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td width="25" align="center" valign="middle" class="font_blk_b"><img
								src="images/ic_page2.gif" hspace="0" align="absmiddle" /></td>
							<td align="left" class="font_blk_b">
							<%
							    if (loadModel.size() > 1) {
							%> <pg:prev>
								<%
								    String url = request.getAttribute("URL")
								                                        + pageUrl.substring(pageUrl.indexOf("?")) + "&textbox="
								                                        + searchString;
								%>
								<a onclick="goToPage('<%=url%>');" href="#" class="set4"><nobr>[<<
								Prev ]</nobr></a>
							</pg:prev> <pg:pages>
								<%
								    if (pageNumber == currentPageNumber) {
								%>
								<b><%=pageNumber%>&nbsp;|</b>
								<%
								    } else {
								                                    String url = request.getAttribute("URL")
								                                            + pageUrl.substring(pageUrl.indexOf("?")) + "&textbox="
								                                            + searchString;
								%>
								<a onclick="goToPage('<%=url%>');" href="#" class="set4"> <%=pageNumber%>&nbsp;|</a>
								<%
								    }
								%>
							</pg:pages> <pg:next>
								<%
								    String url = request.getAttribute("URL")
								                                        + pageUrl.substring(pageUrl.indexOf("?")) + "&textbox="
								                                        + searchString;
								%>
								<a onclick="goToPage('<%=url%>');" href="#" class="set4"><nobr>[
								Next >> ]</nobr></a>
							</pg:next> </a></td>
						</tr>
						<%
						    }
						%>
					</table>
					</td>
			</tr>
			</pg:pager>

			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="td_white">
				<tr>
					<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="td_dgrey"><img src="images/spacer.gif" width="1"
								height="1" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>

					<td height="35" align="left" class="td_grey">
					<input type="hidden"  id="isModelSelected" value="<%=isSelected%>" />
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="Submit2" class="font_bl1_b" 	type="button" onclick="checkCheckbox(this.form.name);" 	<%=isDisabled%> value="Load Model" /> &nbsp;&nbsp;&nbsp;</td>
					<!--  </form>-->
				</tr>
			</table>
			</td>
			</tr>

			<tr>
				<td align="center" valign="bottom">&nbsp;</td>
			</tr>
		</table>
		<!--end content area -->
		
		
		
		