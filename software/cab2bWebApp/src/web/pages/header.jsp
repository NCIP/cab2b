<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<bean:define id="home" value="${param.home}"/>
<SCRIPT language="JavaScript" src="javascript/overlay.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
<DIV id="header">
	<IMG alt="<bean:message key="img.alt.cab2b"/>" src="images/logo.gif" id="headerlogo">
	<DIV id="headerpanel">
		<DIV id="headerpanelbody" <logic:present name="userName">style="top: 20"</logic:present>>
			<logic:notPresent name="loginPage">
				<SPAN class="title"><bean:message key="title.welcome"/></SPAN>
				<logic:notPresent name="userName">
					<A href="Login.do" class="link" alt="Login"><bean:message key="link.signin"/></A> 
					<SPAN class="title"><bean:message key="title.or"/></SPAN> 
					<!--<A href="http://cagrid-portal.nci.nih.gov/web/guest/register" class="link" target="_blank"><bean:message key="link.register"/></A>					-->
					<A href="Register.do" class="link" alt="Register" target="_blank"><bean:message key="link.register"/></A>					
					<logic:notEqual name="home" value="home">
						<SPAN class="title">|</SPAN>
						<A href="Home.do" class="link" alt="Home">Home</A>
					</logic:notEqual>
				</logic:notPresent>
				<logic:present name="userName">			
					<SPAN class="title"><bean:write name="userName"/></SPAN>
					<SPAN class="title">|</SPAN>
					<A href="Logout.do" class="link" alt="Logout"><bean:message key="link.signout"/></A><BR/>
					<logic:notEqual name="home" value="home">
						<A href="Home.do" class="link" alt="home">Home</A>
						<SPAN class="title">|</SPAN>
					</logic:notEqual>					
					<A href="DisplayDashboard.do" class="link" alt="Dashboard" id="dashboardlink"><bean:message key="link.dashboard" arg0="${sessionScope.completedQueryCount}" arg1="${sessionScope.inProgressQueryCount}"/></A>					
				</logic:present>
			</logic:notPresent>
		</DIV>
	</DIV>
</DIV>
