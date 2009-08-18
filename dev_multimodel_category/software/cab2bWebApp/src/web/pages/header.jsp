<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<SCRIPT language="JavaScript" src="javascript/overlay.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
<DIV id="header">
	<IMG alt="<bean:message key="img.alt.cab2b"/>" src="images/logo.gif" id="headerlogo">
	<DIV id="headerpanel">
		<DIV id="headerpanelbody" <logic:present name="userName">style="top: 20"</logic:present>>
			<logic:notPresent name="loginPage">
				<SPAN class="title"><bean:message key="title.welcome"/></SPAN>
				<logic:notPresent name="userName">
					<A href="Login.do" class="link"><bean:message key="link.signin"/></A> 
					<SPAN class="title"><bean:message key="title.or"/></SPAN> 
					<A href="http://cagrid-portal.nci.nih.gov/web/guest/register" class="link" target="_blank"><bean:message key="link.register"/></A>
				</logic:notPresent>
				<logic:present name="userName">
					<SCRIPT language="JavaScript"></SCRIPT>				
					<SPAN class="title"><bean:write name="userName"/></SPAN>&nbsp;<SPAN class="title">|</SPAN>
					<A href="Logout.do" class="link"><bean:message key="link.signout"/></A><BR/>			
					<A href="DisplayDashboard.do" class="link"><bean:message key="link.offlinequeries" arg0="${sessionScope.completedQueryCount}" arg1="${sessionScope.inProgressQueryCount}"/></A>					
				</logic:present>
			</logic:notPresent>
		</DIV>
	</DIV>
</DIV>