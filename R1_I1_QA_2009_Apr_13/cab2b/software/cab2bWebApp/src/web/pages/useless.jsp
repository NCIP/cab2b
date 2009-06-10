<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
<title><bean:message key="application.title"/></title>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/home.css" type="text/css">
</head>
<body>
	<FORM method="post" action="SaveModelGroup.do">
		
		<BR/>
		<DIV class="label">
			<bean:message key="label.modelgroupname"/>
		</DIV>
		<DIV>
			<INPUT type="text" class="textbox" name="modelGroupName"> 
			<INPUT type="checkbox" name="secured" value="True"/><span>Is Secured</span>
		</DIV>
		
		<DIV>
			<logic:present name="entityGroupCollection">
				<logic:iterate name="entityGroupCollection" id="entityGroup" type="edu.common.dynamicextensions.domaininterface.EntityGroupInterface">
					<input type="checkbox" name="entityGroupCollection" value="<bean:write name="entityGroup" property="longName"/>_v<bean:write name="entityGroup" property="version"/>"/>
					<span><bean:write name="entityGroup" property="longName"/>_v<bean:write name="entityGroup" property="version"/></span>
					<br/>
				</logic:iterate>
			</logic:present>
		</DIV>
		
		<DIV>
			<INPUT type="submit" class="button" value="<bean:message key="button.saveapplicationgroup"/>"/>
		</DIV>
	</FORM>
</body>
</HTML>