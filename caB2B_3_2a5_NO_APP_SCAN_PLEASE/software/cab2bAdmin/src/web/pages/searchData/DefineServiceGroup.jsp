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
<script src="javascript/jquery.table.addrow.js"></script>

</head>
<BODY>
	<script type="text/javascript">
		$(document).ready(function(){
			$(".addRow").btnAddRow();
			$(".delRow").btnDelRow();
			$("#groupForm").submit(function(event) {
				var formValid = true;
				$(this).find("input[name='groupNames']").each(function(index, item) {
					if(!$(item).val()) {
						formValid = false;
						$($('.error').get(index)).show();
					} else {
						$($('.error').get(index)).hide();
					}
				});
				return formValid;
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
	              <td width="25%" align="center" background="images/wizard_bg.gif"><img src="images/1_choose_inactive.gif" alt="Category information" width="173" height="28"></td>
	              <td width="25%" align="center" background="images/wizard_bg.gif"><img src="images/2_service_active.gif" alt="Create Category" width="143" height="28"></td>
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
	          <td align="left" valign="top"><table width="570" border="0" cellpadding="1" cellspacing="0" class="td_grey">
	              <tr>
	                <td align="left" valign="middle">
						<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
						<tr>
							<td align="left" valign="middle" background="images/title_bg.gif"
								class="font_bl3_b"><img src="images/c1.gif" width="12"
								height="24" align="absmiddle" />&nbsp;&nbsp;Please define service groups for "<b><s:property value="query.name" /></b>":</td>
							<td align="right" valign="top" background="images/title_bg.gif"
								class="td_dblue2"><img src="images/c2.gif" width="12"
								height="24" /></td>
						</tr>
						<tr>
							<td colspan="3" align="left" valign="top" class="td_orange"><img
								src="images/spacer.gif" width="1" height="1"></td>
						</tr>
	                    <tr>
	                    	<s:if test="%{query.serviceGroups.empty}">
							<table class="instanceTable normalTable">
								<tr>
									<th>
										Service Group Name 
									</th>
									<th colspan="2">
										<span style="float: left; padding-top: 4px;">Service Instances</span><span style="float: right"><input type="button" class="addRow" value="Add Service Group"/></span>
									</th>
								</tr>
								<tr>
										<td>
											<s:textfield name="groupNames"  theme="simple" cssClass="required"/><br/>
											<div class="error" style="color: red; display: none;">Please enter a name</div> 
											
										</td>
										<td>
											<table class="noBorder">
											<s:iterator  value="query.constraints.queryEntities">
												<tr>
													<td>
														<s:property value="entityInterface.name" />
													</td>
													<td>
														<s:select name="selectedUrls['%{entityInterface.name}']" list="serviceUrls[entityInterface.name]" listKey="urlId" listValue="%{hostingCenter + ' (' + urlLocation + ')'}" theme="simple" />
													</td>
												</tr>
											</s:iterator>
											</table>
										</td>
										<td>
											<input type="button" class="delRow" value="Delete Row"/>
										</td>
								</tr>	
							</table>
							</s:if>
							<s:else>
							<table class="instanceTable normalTable">
								<tr>
									<th>
										Service Group Name
									</th>
									<th colspan="2">
										<span style="float: left; padding-top: 4px;">Service Instances</span><span style="float: right"><input type="button" class="addRow" value="Add Service Group"/></span>
									</th>
								</tr>
								<s:iterator value="query.serviceGroups">
								<tr>
										<td>
											<s:textfield name="groupNames"  theme="simple" cssClass="required" value="%{name}"/><br/>
											<div class="error" style="color: red; display: none;">Please enter a name</div> 
											
										</td>
										<td>
											<table class="noBorder">
											<s:iterator  value="items">
												<tr>
													<td>
														<s:property value="targetObject" />
													</td>
													<td>
														<s:select name="selectedUrls['%{targetObject}']" list="serviceUrls[targetObject]" listKey="urlId" listValue="%{hostingCenter + ' (' + urlLocation + ')'}" theme="simple" value="serviceUrl.urlId" />
													</td>
												</tr>
											</s:iterator>
											</table>
										</td>
										<td>
											<input type="button" class="delRow" value="Delete Row"/>
										</td>
								</tr>
								</s:iterator>	
							</table>							
							</s:else>
	                    </tr>

	                </table></td>
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

	                <td height="35" align="right" class="td_grey">
						<s:hidden name="selectedQuery" value="%{query.id}" />
						<s:submit action="saveServiceGroup"  cssClass="font_bl1_b" value="Submit" theme="simple"/>
	                  &nbsp;&nbsp;&nbsp; </td>

	              </tr>
	          </table></td>

	        </tr>
	      </table>
	</s:form>
</Body>
</HTML>