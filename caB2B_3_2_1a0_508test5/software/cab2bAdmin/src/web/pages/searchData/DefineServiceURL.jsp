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
<script src="javascript/jquery.validate.js"></script>
<style type="text/css">
	.error {
		color: red;
	}
</style>
</head>
<BODY>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#groupForm").validate();
			
		});
	</script>
	<s:form id="groupForm" action="ServiceURL" >
	<table width="100%" border="0" cellpadding="0" cellspacing="0">


	        <tr>
	          <td colspan="2" align="center" valign="bottom"><table width="100%" border="0" cellpadding="0" cellspacing="0">
	            <tr>
	              <td height="35" colspan="4" align="left" valign="top" background="\\"><table width="200" border="0" cellpadding="0" cellspacing="0" class="td_dgrey">
	                <tr>
	                  <td><img src="images/spacer.gif" width="10" height="1"></td>
	                  <td><img src="images/arrow.gif" width="12" height="11" hspace="5" align="absmiddle"><span class="font_bl2_b">Add Service URL </span></td>
	                  <td align="right" valign="top"><img src="images/c6.gif" width="15" height="23"></td>
	                </tr>
	              </table></td>
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
							<table class="instanceTable normalTable" width="100%">
								<tr>
									<td width="25%">
										Model
									</td>
									<td colspan="2">
										<s:select name="model" list="modelGroups" listKey="name" listValue="name" theme="simple" />
									</td>
								</tr>
								<tr>
									<td>
										Service URL<span style="color: red">*</span>
									</td>
									<td>
										<s:textfield name="url"  theme="simple" cssClass="required url" size="80"/><br/>
									</td>
								</tr>
								<tr>
									<td>
										Description
									</td>
									<td>
										<s:textfield name="description"  theme="simple" size="80"/><br/>
									</td>
								</tr>
								<tr>
									<td>
										Hosting Center
									</td>
									<td>
										<s:textfield name="center"  theme="simple" size="80"/><br/>
									</td>
								</tr>	
								<tr>
									<td>
										Hosting Center Short Name
									</td>
									<td>
										<s:textfield name="centerShort"  theme="simple" size="80"/><br/>
									</td>
								</tr>
								<tr>
									<td>
										Contact Name
									</td>
									<td>
										<s:textfield name="contactName"  theme="simple" size="80"/><br/>
									</td>
								</tr>

								<tr>
									<td>
										Contact Email
									</td>
									<td>
										<s:textfield name="contactEmail"  theme="simple" cssClass="email" size="80"/><br/>
									</td>
								</tr>
							</table>
							
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
						<s:submit action="saveServiceURL"  cssClass="font_bl1_b" value="Submit" theme="simple"/>
	                  &nbsp;&nbsp;&nbsp; </td>

	              </tr>
	          </table></td>

	        </tr>
	      </table>
	</s:form>
</Body>
</HTML>