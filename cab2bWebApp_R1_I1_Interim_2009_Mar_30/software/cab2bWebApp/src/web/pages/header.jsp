<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<DIV id="header">
	<IMG alt="caBench-to-Bedside" src="images/logo.gif" id="headerlogo">
	<DIV id="headerpanel">
		<logic:present name="User">
			<DIV id="headerpanelbody">
				<SPAN class="title"><bean:message key="title.welcome"/> <bean:write name="UserName"/></SPAN><br/>
				<logic:notEqual name="User" property="userName" value="Anonymous"><A href="#this" class="link"><bean:message key="link.mysettings"/></A> <SPAN class="title">|</SPAN></logic:notEqual> <A href="Logout.do" class="link"><bean:message key="link.signout"/></A>
			</DIV>
		</logic:present>
	</DIV>
</DIV>