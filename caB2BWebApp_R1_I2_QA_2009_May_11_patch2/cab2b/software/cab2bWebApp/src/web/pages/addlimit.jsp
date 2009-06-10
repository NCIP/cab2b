<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/addlimit.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/addlimit.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/calendar.js"></SCRIPT>
</HEAD>
<BODY onLoad="showAddLimitForm()">
<FORM method="post" action="ExecuteQuery.do?page=addlimit" onSubmit="createQueryString()">
	<jsp:include page="header.jsp"/>
	<DIV id="leftpanel" style="width: 24%;">
		<DIV id="savedsearchespanel">
			<DIV class="titlebar">
				<DIV class="titlebarheader title">
					<DIV class="titlebarleftcurve">
						<DIV class="titlebarrightcurve">
							<DIV class="titlebarcollapsed" onClick="document.getElementById('savedsearchespanelbody').style.display=(document.getElementById('savedsearchespanelbody').style.display=='none'?'block':'none');this.className=(this.className=='titlebarexpanded'?'titlebarcollapsed':'titlebarexpanded');">
							<bean:message key="title.savedsearches"/>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<DIV id="savedsearchespanelbody">				
				<SCRIPT language="JavaScript">
					processAJAXRequest('DisplaySavedSearches.do?addlimit=addlimit&modelGroups=<bean:parameter name="modelGroups" id="modelGroups"/><bean:write name="modelGroups"/>', 'savedsearchespanelbody');
				</SCRIPT>
			</DIV>
		</DIV>
	</DIV>
	<DIV id="definelimitspanel">
		<DIV id="definelimitspanelheader">
			<DIV class="titlebar">
				<DIV class="titlebarheader title">
					<DIV class="titlebarleftcurve">
						<DIV class="titlebarrightcurve">
							<bean:message key="title.defineconditionsfor"/> <DIV id="queryname" style="display: inline"></DIV><INPUT type="hidden" name="queryId" id="queryId" value="<bean:parameter name="queryId" id="queryId"/><bean:write name="queryId"/>">
						</DIV>
					</DIV>
				</DIV>
			</DIV>
		</DIV>
		<DIV id="definelimitspanelbody">
		</DIV>
		<DIV id="definelimitspanelfooter">
			<INPUT type="button" class="button" value="<bean:message key="button.back"/>" onClick="document.forms[0].action='Home.do';document.forms[0].submit();">&nbsp;<INPUT type="submit" class="button" value="<bean:message key="button.executequery"/>">
		</DIV>
	</DIV>
	<jsp:include page="footer.jsp"/>
</FORM>
</BODY>
</HTML>