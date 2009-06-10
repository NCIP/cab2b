<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<SCRIPT language="JavaScript" src="javascript/Preloader.js"></SCRIPT>
<DIV id="header">
	<IMG alt="caBench-to-Bedside" src="images/logo.gif" id="headerlogo">
	<DIV id="headerpanel">
		<DIV id="headerpanelbody">
			<logic:notPresent name="loginPage">
				<SPAN class="title"><bean:message key="title.welcome"/></SPAN>
				<logic:notPresent name="userName">
					<A href="Login.do" class="link"><bean:message key="link.signin"/></A> 
					<SPAN class="title">or</SPAN> 
					<A href="http://cagrid-portal.nci.nih.gov/web/guest/register" class="link" target="_blank"><bean:message key="link.register"/></A>
				</logic:notPresent>
				<logic:present name="userName">
					<SPAN class="title"><bean:write name="userName"/></SPAN>
					<SPAN class="title">|</SPAN>
					<A href="Logout.do" class="link"><bean:message key="link.signout"/></A>
				</logic:present>
			</logic:notPresent>
		</DIV>
	</DIV>
</DIV>