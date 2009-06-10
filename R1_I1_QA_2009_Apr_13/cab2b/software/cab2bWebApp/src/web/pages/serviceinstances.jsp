<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/serviceinstances.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/validator.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
</HEAD>
<BODY>
	<FORM method="post" action="/DisplayServiceInstances.do" >
		<jsp:include page="header.jsp"/>			
		<DIV>
			<logic:present name="serviceInstances">
				<logic:iterate name="serviceInstances" id="serviceInstance" type="edu.wustl.cab2b.common.user.ServiceURLInterface">
					<input type="checkbox" name="serviceInstances" value="<bean:write name="serviceInstance" property="entityGroupName"/>"/>
					<span><bean:write name="serviceInstance" property="hostingCenter"/></span>
					<br/>
					<span><bean:write name="serviceInstance" property="description"/></span>
					<br/>
				</logic:iterate>
			</logic:present>
		</DIV>
		<jsp:include page="footer.jsp"/>	
	</FORM>
</BODY>
</HTML>