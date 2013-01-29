<%--L
  Copyright Georgetown University, Washington University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
<title>caBench-To-Bedside</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script src="javascript/jquery-1.4.4.js"></script>
</head>
<BODY>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#groupForm").submit(function() {
				if($(this).find(":checked").length > 0) {
					$('.error').hide();
					return true;
				} else {
					$('.error').show();
					return false;
				}
			});
		});
	</script>
	<s:form id="groupForm" action="ServiceGroup" >
	<table width="100%" border="0" cellpadding="0" cellspacing="0">


	        <tr>
	          <td colspan="2" align="center" valign="bottom"><table width="100%" border="0" cellpadding="0" cellspacing="0">
	            <tr>
	              <td height="35" colspan="4" align="left" valign="top" background="\\"><table width="200" border="0" cellpadding="0" cellspacing="0" class="td_dgrey">
	                <tr>
	                  <td><img src="images/spacer.gif" width="10" height="1"></td>
	                  <td><img src="images/arrow.gif" width="12" height="11" hspace="5" align="absmiddle"><span class="font_bl2_b">Define Service Groups </span></td>
	                  <td align="right" valign="top"><img src="images/c6.gif" width="15" height="23"></td>
	                </tr>
	              </table></td>
	              </tr>
	            <tr>
	              <td width="25%" align="center" background="images/wizard_bg.gif"><img src="images/1_choose_active.gif" alt="Category information" width="173" height="28"></td>
	              <td width="25%" align="center" background="images/wizard_bg.gif"><img src="images/2_service_inactive.gif" alt="Create Category" width="143" height="28"></td>
	              <td width="25%" align="center" background="images/wizard_bg.gif"></td>
	              <td align="center" background="images/wizard_bg.gif">&nbsp;</td>
	            </tr>
	            <tr>
	              <td colspan="4" align="center"><img src="images/spacer.gif" width="1" height="15"></td>
	              </tr>
	          </table></td>
	        </tr>

	        <tr>

	          <td width="25" align="center" valign="bottom">&nbsp;</td>
	          <td align="left" valign="top">
				<table width="570" border="0" cellpadding="1" cellspacing="0">
	              <tr>
	                <td align="left" valign="middle">
						<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" bordercolor="grey">
							<tr>
								<td align="left" valign="middle" background="images/title_bg.gif"
									class="font_bl3_b"><img src="images/c1.gif" width="12"
									height="24" align="absmiddle" />&nbsp;&nbsp;Please select a query to create service groups for:</td>
								<td align="right" valign="top" background="images/title_bg.gif"
									class="td_dblue2"><img src="images/c2.gif" width="12"
									height="24" /></td>
							</tr>
							<tr>
								<td colspan="3" align="left" valign="top" class="td_orange"><img
									src="images/spacer.gif" width="1" height="1"></td>
							</tr>							
						<s:iterator value="allQueries"> 
	                    	<tr>
								<td style="border: 1px solid grey; bgcolor: #FFFFFF;"  colspan="2"><div style="color: black; font-size: 12;"><input type="radio" name="selectedQuery"  class="required" value="<s:property value='id'/>"  /> <s:property value="name"/></div></td>
	                    	</tr>
						</s:iterator> 
	                </table>
					<br/>
					<div class="error" style="color: red; display: none;">Please select a query</div> 
					</td>
	              </tr>
	          </table></td>
	        </tr>
	        <tr>
			
	          <td align="center" valign="bottom">&nbsp;</td>
	          <td align="center" valign="bottom">&nbsp;</td>
	        </tr>
	        <tr>
	          <td colspan="2" align="center" valign="bottom"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="td_white">
	            <tr>
	              <td><table width="100%" border="0" cellpadding="0" cellspacing="0" >
	                <tr>
	                  <td class="td_dgrey"><img src="images/spacer.gif" width="1" height="1" /></td>
	                    </tr>
	                </table></td>
	              </tr>
	            <tr>

	                <td height="35" align="right" class="td_grey"><s:submit action="defineServiceGroup" cssClass="font_bl1_b" name="Submit" theme="simple"/>
	                  &nbsp;&nbsp;&nbsp; </td>

	              </tr>
	          </table></td>

	        </tr>
	      </table>
	</s:form>
</Body>
</HTML>