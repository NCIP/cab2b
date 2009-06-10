<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>


<script src="javascript/queryModule.js"></script>
<script type="text/javascript" src="javascript/ajax.js"></script> 
<script type="text/javascript" src="javascript/wz_tooltip.js"></script> 

<script>
	
function checkEnterKey(event){
	var platform = navigator.platform.toLowerCase();
	if (platform.indexOf("mac") != -1)
	{		
		var key = event.keyCode; 
		if (key == 13) { event.returnValue=false; } 
	}
	else
	{
		event.returnValue=true;

	}



}

function checkKey(event) 
{
	var platform = navigator.platform.toLowerCase();
	if (platform.indexOf("mac") != -1)
	{		
		var key = event.keyCode; 
		if (key == 13) { event.returnValue=false; } 
	}
	else
	{
		event.returnValue=true;

	}
	var string = document.forms[0].textField.value;
		if (string == "")
		{
			var element = document.getElementById('resultSet');
			element.innerHTML = string;
			return ;
		}
retriveSearchedEntities('SearchCategory.action','','currentPage');	
return;
}

</script>
</head>
<%
boolean mac = false;
Object os = request.getHeader("user-agent");
if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
{
	mac = true;
}
if (mac)
{
%>
<body  onkeypress="checkEnterKey(event)"  onkeyup="checkKey(event)" >
<%
}
else
{
%>
<body  onkeyup="checkKey(event)" >
<% } %>
 <form  method="GET" action="">
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td align="left" valign="middle" background="images/title_bg.gif" class="font_bl3_b"><img src="images/c1.gif" width="12" height="24" align="absmiddle" />&nbsp;&nbsp;Search </td>
                    <td align="left" valign="top" background="images/title_bg.gif" class="td_dblue2">&nbsp;</td>
                    <td align="right" valign="top" background="images/title_bg.gif" class="td_dblue2"><img src="images/c2.gif" width="12" height="24" /></td>
                  </tr>
                  <tr>
                    <td colspan="3" align="left" valign="top" class="td_orange"><img src="images/spacer.gif" width="1" height="1"></td>
                  </tr>
                </table>
                  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="25" align="left">&nbsp;</td>
                      <td align="left">&nbsp;</td>
                    </tr>
                    <tr>
                      <td rowspan="2" align="left">&nbsp;</td>
                     
                        <td align="left"><input id="searchText"  type="text" class="input_tbox" name="textField" onkeydown="setFocusOnSearchButton(event)" size="30" /></td>
                      
                    </tr>
                    <tr>
                      <td align="left"><span class="font_blk_s">(E.g. Participant, Gene, Collection )</span></td>
                    </tr>
                    <tr>
                      <td colspan="2" align="left" class="font_blk_b" ><img src="images/spacer.gif" width="1" height="5"></td>
                    </tr>
                    <tr>
                      <td align="left"  class="font_blk_b" >&nbsp;</td>
                      <td height="25"  align="left" id="advancedSearchHeaderTd" ><span id="imageContainer"><img src="images/plus.gif" width="12" height="12" hspace="0" border="0" onClick="expand()" align="absmiddle" /></span>&nbsp; <span class="font_blk_b">Advanced Search</span></td>
                    </tr>
                      <td></td>
                      <td>
                         <table valign="top" class="collapsableTable" style="display:none" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" id="collapsableTable">
								<tr id="class_view">
									<td ><input type="checkbox"   onclick="setIncludeDescriptionValue()" value="on" name="classChecked" checked /><span class="font_blk_s"> Class </span></td>
								</tr>
								<tr id="attribute_view" >
									<td ><input type="checkbox"   onclick="setIncludeDescriptionValue()" value="on" name="attributeChecked" /><span class="font_blk_s"> Attribute</span></td>
								</tr>
								<tr id="permissible_view" >
									<td ><input type="checkbox"  onclick="permissibleValuesSelected(this)" value="on" name="permissibleValuesChecked" /><span class="font_blk_s">Permissible Values</span> </td>
								</tr>
								<tr id="description_view" >
									<td ><input type="checkbox" name="includeDescriptionChecked"
									 value="off" /><span class="font_blk_s"> Include Description </span></td>
								</tr>
								
								<tr id="radio_view" >
									<td class="standardTextQuery">
										<input type="radio"  value="text_radioButton" name="selected" checked   onclick="radioButtonSelected(this)"/><span class="font_blk_s">Text</span>
										<input type="radio"  value="conceptCode_radioButton" name="selected" onclick="radioButtonSelected(this)" disabled /><span class="font_blk_s">Concept Code</span>
									</td>
								</tr>											
							</table>
                      </td>
                    <tr>
                      <td align="left" class="font_blk_b" >&nbsp;</td>
                      <td align="left"  ><input  type="button" id="searchButton" class="font_blk_s" value="Search" onclick="retriveSearchedEntities('SearchCategory.action','','currentPage');" /></td>
                    </tr>
                    <tr>
                      <td align="left" class="font_blk_b" >&nbsp;</td>
                      <td align="left"  >&nbsp;</td>
                    </tr>
                  </table>
                  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                      <td align="left" valign="middle" background="images/title_bg.gif" class="font_bl3_b"><img src="images/c1.gif" width="12" height="24" align="absmiddle" />&nbsp;&nbsp;Search Results </td>
                      <td align="left" valign="top" background="images/title_bg.gif" class="td_dblue2">&nbsp;</td>
                      <td align="right" valign="top" background="images/title_bg.gif" class="td_dblue2"><img src="images/c2.gif" width="12" height="24" /></td>
                    </tr>
                    <tr>
                      <td colspan="3" align="left" valign="top" class="td_orange"><img src="images/spacer.gif" width="1" height="1"></td>
                    </tr>
                  </table>
                  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                      <td><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                          <td><img src="images/spacer.gif" width="15" height="1"></td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td width="100%"  id='resultSetTd' height="100%"><div id="resultSet" style="overflow:auto;height:290;width:227"></div></td>
                        </tr>
    
                      </table></td>
                    </tr>
                    
                    <tr>
                      <td height="15"><table width="100%" border="0" cellpadding="0" cellspacing="0" >
                        <tr>
                          <td class="td_dgrey"><img src="images/spacer.gif" width="1" height="1" /></td>
                        </tr>
                      </table></td>
                    </tr>
                    
                  </table>
                  </form>
				
					<script type="text/javascript">
					document.getElementById('searchText').focus();
					</script>


</body>
</html>