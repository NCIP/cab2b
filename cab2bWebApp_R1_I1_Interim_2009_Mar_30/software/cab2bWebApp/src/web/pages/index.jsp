<%@ page session="false" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/login.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/validator.js"></SCRIPT>
<SCRIPT language="JavaScript">
 function validate()
{
  var check1 = checkEmptyTextFileld('userName');
  var check2 = checkEmptyTextFileld('password');
  return check1 && check2;
}
</SCRIPT>
</HEAD>
<BODY>
	<%@ include file="header.jsp" %>
	<DIV id="leftpanel">
		<DIV id="quicklinkspanel">
			<DIV class="titlebar title">
				<DIV class="leftcurve">
					<DIV class="rightcurve">
						<bean:message key="title.quicklinks"/>
					</DIV>
				</DIV>
			</DIV>
			<DIV id="quicklinkspanelbody">
				<DIV>
					<IMG alt="Bullet" src="images/arrow.jpg" class="bullet"><A href="https://cabig.nci.nih.gov/" class="link"><bean:message key="link.home.cabig"/></A><BR/>
					<IMG alt="Bullet" src="images/arrow.jpg" class="bullet"><A href="http://ncicb.nci.nih.gov/" class="link"><bean:message key="link.home.ncicb"/></A><BR/>
					<IMG alt="Bullet" src="images/arrow.jpg" class="bullet"><A href="http://www.cagrid.org/mwiki/index.php?title=CaGrid" class="link"><bean:message key="link.wiki.cagrid"/></A><BR/>
					<IMG alt="Bullet" src="images/arrow.jpg" class="bullet"><A href="http://cab2b.wustl.edu/" class="link"><bean:message key="link.webpage.cab2b"/></A>
				</DIV>
			</DIV>
			<DIV id="certifiedbrowserspanel">
				<DIV class="title"><bean:message key="browsers.certified"/></DIV>
				<DIV class="text"><IMG alt="Internet Explorer 6.0" hspace="3" src="images/logo_ie.gif" align="absMiddle" vspace="3"><bean:message key="browser.ie"/></DIV>
				<DIV class="text"><IMG alt="Mozilla Firefox-2.0.0.3" hspace="3" src="images/logo_firefox.gif" align="absMiddle" vspace="3"><bean:message key="browser.mozilla"/></DIV>
				<DIV class="text"><IMG alt="Mac Safari 3.1.1" hspace="3" src="images/logo_safari.gif" align="absMiddle" vspace="3"><bean:message key="browser.safari"/></DIV>
			</DIV>
			<DIV id="optimalresolutionspanel">
				<DIV class="title""><bean:message key="resolutions.optimal"/></DIV>
				<DIV class="text"><IMG alt="Windows" hspace=3 src="images/logo_windows.gif" align="absMiddle" vspace="3"><bean:message key="os.resolution.windows"/></DIV>
				<DIV class="text"><IMG alt="Mac" hspace="3" src="images/logo_mac.gif" align="absMiddle" vspace="3"><bean:message key="os.resolution.mac"/></DIV>
			</DIV>
		</DIV>
	</DIV>
	<DIV id="content">
		<IMG alt="caB2B Concept" src="images/concept.jpg" id="conceptimage">
	</DIV>
	<DIV id="rightpanel">
		<DIV id="loginpanel">
			<html:form method="post" action="/Login.do" onsubmit="return validate();">
				<DIV class="titlebar title">
					<DIV class="leftcurve">
						<DIV class="rightcurve">
							<bean:message key="title.signin"/>
						</DIV>
					</DIV>
				</DIV>
				<DIV id="loginpanelbody">
					<DIV class="label required">
						<bean:message key="label.username"/>:
					</DIV>
					<DIV style="width:60%;float:right">
						<html:text styleClass="textbox" property="userName"/>
					</DIV>
					<br/>
					<DIV class="label required">
						<bean:message key="label.password"/>:
					</DIV>
					<DIV style="width:60%;float:right">
						<html:password styleClass="textbox" property="password"/>
					</DIV>
					<br/>
					<DIV style="display:none">
					<DIV class="label">
						<bean:message key="label.identityprovider"/>:
					</DIV>
					<DIV style="width:60%;float:right">
						<html:select styleClass="select" property="identityProvider"><html:option value="option1"><bean:message key="label.identityprovider.option1"/></html:option></html:select>
					</DIV>
					<br/>
					</DIV>
					<DIV style="text-align:center">
						<INPUT type="submit" value="<bean:message key="button.login"/>" class="button">
						<SPAN style="margin:0.5em"></SPAN>
						<INPUT type="Reset" value="Reset" class="button">
					</DIV>
					<DIV id="notificationpanel">
						<DIV id="messages"><html:messages id="SuccessLogout" message="true"><bean:write name="SuccessLogout"/></html:messages></DIV>
						<DIV id="errors"><html:errors/></DIV>
						<DIV class="error hidden" id="error_userName"><bean:message key="error.username.empty"/></DIV>
						<DIV class="error hidden" id="error_password"><bean:message key="error.password.empty"/></DIV>
					</DIV>
					<DIV style="text-align:center">
						<A href="Login.do" class="link"><bean:message key="link.guestlogin"/></A> <SPAN class="title">|</SPAN> <A href="http://cagrid-portal.nci.nih.gov/web/guest/register" class="link" target="_blank"><bean:message key="link.register"/></A>
					</DIV>
				</DIV>
			</html:form>
		</DIV>
	</DIV>
	<jsp:include page="footer.jsp"/>
</BODY>
</HTML>