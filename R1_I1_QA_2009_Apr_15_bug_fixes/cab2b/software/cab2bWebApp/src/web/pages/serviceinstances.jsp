<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/serviceinstances.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/util.js"></SCRIPT>
</HEAD>
<BODY>
<FORM method="post" action="/DisplayServiceInstances.do">
<jsp:include page="header.jsp"/>
<DIV id="content">
	<DIV class="titlebar">
		<DIV class="titlebarheader title">
			<DIV class="titlebarleftcurve">
				<DIV class="titlebarrightcurve">
				    <bean:write name="modelGroupName"/>					
				</DIV>
			</DIV>
		</DIV>
	</DIV>
	<DIV class="text" id="toppanel"><INPUT type="checkbox" onClick="selectAll(this,'serviceInstance')">Select All</DIV>
	<DIV id="centerpanel">
		<DIV id="centerpanelcontent">
		<logic:present name="serviceInstances">
			<logic:iterate name="serviceInstances" id="serviceInstance" type="edu.wustl.cab2b.common.user.ServiceURLInterface">
				<DIV style="padding: 0.5em 0em 0.5em 0em">
					<DIV class="heading">
						<input type="checkbox" name="serviceInstance" value="<bean:write name="serviceInstance" property="entityGroupName"/>" <logic:equal name="serviceInstance" property="configured" value="true">checked</logic:equal>/>
						<bean:write name="serviceInstance" property="hostingCenter"/>
					</DIV>
					<DIV class="text">
						<bean:write name="serviceInstance" property="description"/>
					</DIV>
				</DIV>
			</logic:iterate>			
		</logic:present>
		</DIV>
	</DIV>
	<DIV id="bottompanel">
		<INPUT type="button" class="button" value="&lt;&lt;Back" onClick="history.go(-1);">&nbsp;<INPUT type="button" class="button" value="Save Settings">
	</DIV>
</DIV>
<jsp:include page="footer.jsp"/>
</FORM>
</BODY>
</HTML>