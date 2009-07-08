<%@ page isErrorPage="true" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<LINK rel="shortcut icon" href="../images/favicon.ico">
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/failure.css" type="text/css">
</HEAD>
<BODY>
	<jsp:include page="header.jsp"/>
	<jsp:include page="leftpanel.jsp"/>
	<DIV id="content">
		<DIV class="titlebar collapsible">
			<DIV class="titlebarheader title">
				<DIV class="titlebarleftcurve">
					<DIV class="titlebarrightcurve">
						<DIV class="titlebarcollapsed" onClick="document.getElementById('contentbody').style.display=(document.getElementById('contentbody').style.display=='none'?'block':'none');this.className=(this.className=='titlebarexpanded'?'titlebarcollapsed':'titlebarexpanded');">
							<bean:message key="title.unexpectederror"/>
						</DIV>						
					</DIV>
				</DIV>
			</DIV>
		</DIV>
		<DIV id="contentbody">
			<DIV id="errornotificationpanel">
				<IMG alt="<bean:message key="img.alt.error"/>" src="images/esclamation.jpg" align="middle"><BR/>
				<SPAN class="error">
					<bean:message key="error.unexpected"/><BR/>
					${pageContext.exception}<html:errors/>
					<BR/><BR/>				
				</SPAN>
			</DIV>
			<INPUT type="button" class="button" value="<bean:message key="button.back"/>" onClick="history.go(-1)">&nbsp;&nbsp;
			<INPUT type="button" class="button" value="<bean:message key="button.home"/>" onClick="document.location='Home.do'">
		</DIV>
	</DIV>
	<jsp:include page="footer.jsp"/>
</BODY>
</HTML>