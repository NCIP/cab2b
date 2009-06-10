<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/search.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/validator.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
</HEAD>
<BODY>
	<jsp:include page="header.jsp"/>
	<FORM method="post" action="/search.do" onsubmit="return checkEmptyTextFileld('searchKeywords')">
		<DIV id="toppanel">
			<DIV class="label">
				<bean:message key="label.searchdatatype"/>
			</DIV>
			<DIV>
				<SELECT class="select" name="modelName" name="SelectField" onChange="processAJAXRequest('LoadModelRelatedDataAction.do?modelId=' + this.value + '&task=serviceinstance', 'serviceinstances');processAJAXRequest('LoadModelRelatedDataAction.do?modelId=' + this.value + '&task=savedsearches', 'savedsearchespanelbody')">
					<logic:iterate name="ModelNames" id="model" type="edu.wustl.cab2bwebapp.bean.ModelBean">
						<OPTION value="<bean:write name="model" property="id"/>">
							<bean:write name="model" property="name"/>
						</OPTION>
					</logic:iterate>
				</SELECT>
			</DIV>
		</DIV>
		<DIV id="leftpanel">
			<DIV>
				<DIV>
					<DIV class="label"><bean:message key="label.searchkeywords"/></DIV> <INPUT type="text" class="textbox" name="searchKeywords"> <INPUT type="submit" class="button" value="<bean:message key="button.searchkeywords"/>">
				</DIV>
			</DIV>
			<DIV id="notificationpanel" style="padding-bottom: 1.2em">
				<DIV class="error hidden" id="error_searchKeywords"><bean:message key="error.searchkeywords.empty"/></DIV>
			</DIV>
			<DIV id="savedsearchespanelheaderexpand" class="title" onClick="document.getElementById('savedsearchespanelbody').style.display=(document.getElementById('savedsearchespanelbody').style.display=='block'?'none':'block');this.id=(this.id=='savedsearchespanelheaderexpand'?'savedsearchespanelheadercontract':'savedsearchespanelheaderexpand')">
			<bean:message key="title.savedsearches"/>
			</DIV>
			<DIV id="savedsearchespanelbody">
				<jsp:include page="savedsearches.jsp"/>
			</DIV>
		</DIV>
		<DIV id="rightpanel">
			<DIV class="label">
				<bean:message key="label.location"/>
			</DIV>
			<DIV id="serviceinstances">
				<jsp:include page="serviceinstances.jsp"/>
			</DIV>
		</DIV>
	</FORM>
	<jsp:include page="footer.jsp"/>
</BODY>
</HTML>