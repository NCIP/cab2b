<%@ page errorPage="failure.jsp" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<LINK rel="shortcut icon" href="../images/favicon.ico">
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/login.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/validator.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">
 function validate()
{
  var invalidChars = "~!^?*|,\\\\/\":<>[]{}`\';()@&$#% ";
  var userName = document.getElementsByName("userName")[0];
  var password = document.getElementsByName("password")[0];
   for(var i=0;i<userName.value.length;i++)
  {
  	 if(invalidChars.indexOf(userName.value.charAt(i))!=-1)
  	{
      alert("User name should not contain special characters!");
      userName.focus();
      return false; 
    }
  }	  	
  var check1 = checkEmptyTextFileld('userName');
  var check2 = checkEmptyTextFileld('password');
  return check1 && check2;
}
</SCRIPT>
</HEAD>
<BODY onLoad="document.forms[0].userName.focus()">
	<%@ include file="header.jsp" %>
	<jsp:include page="leftpanel.jsp"/>
	<DIV id="content">
		<bean:message key="heading.content"/>
	</DIV>
	<DIV id="rightpanel">
		<DIV id="loginpanel">
			<html:form method="post" action="/Login.do" onsubmit="return validate();">
				<DIV class="titlebar">
					<DIV class="titlebarheader title">
						<DIV class="titlebarleftcurve">
							<DIV class="titlebarrightcurve">
								<bean:message key="title.signin"/>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV id="loginpanelbody">
<!--					<DIV class="label">
						<bean:message key="label.username"/>
					</DIV>
					<DIV style="width:60%;float:right">
						<INPUT type="text" class="textbox" name="userName" autocomplete="off"/>
					</DIV>
					<BR/>
					<DIV class="label">
						<bean:message key="label.password"/>
					</DIV>
					<DIV style="width:60%;float:right">
						<INPUT type="password" class="textbox" name="password" autocomplete="off"/>
					</DIV>-->
 <table>
                    <thead>
                    <tr><td>&nbsp;</td><td>&nbsp;</td></tr></thead>
                    <tbody>
                    <tr><th style="padding-right:5px; text-align:right">Username:</th><td><input name="userName" type="text" autocomplete="off" value="" size="29"/></td></tr><tr><th style="padding-right:5px; text-align:right;">Password:</th><td><input id="password" name="password" type="password" value="" autocomplete="off" size="29"/></td></tr><tr><th style="padding-right:5px; text-align:right;"> 
					<logic:present name="secondaryAuthName">Identity Provider:</th><td>
						<SELECT name="auth">
							<OPTION value="1"><bean:write name="authName"/></OPTION>
							<OPTION value="2"><bean:write name="secondaryAuthName"/></OPTION>
						</SELECT>
       		                         </logic:present>
               		                 <logic:notPresent name="secondaryAuthName">
						<input type="hidden" name="auth" value="-1" />
                       		         </logic:notPresent>
                        </td></tr><tr><td></td><td style="padding-top:10px;">
                            <span id="_cagriddirectauthn_WAR_cagridportlets_loginButtonContainer"></span>
                        </td></tr></tbody>
</table>
					<DIV style="text-align:center">
						<INPUT type="submit" value="<bean:message key="button.login"/>" class="button">
						<SPAN style="margin:0.5em"></SPAN>
						<INPUT type="button" value="<bean:message key="button.reset"/>" class="button" onclick="userName.value='';password.value='';userName.focus()">
					</DIV>
					<BR style="line-height: 2em"/>
						<DIV class="error hidden" id="error_userName"><bean:message key="error.username.empty"/></DIV>
						<DIV class="error hidden" id="error_password"><bean:message key="error.password.empty"/></DIV>
						<DIV id="errors"><html:errors/></DIV>
					</DIV>
					<DIV>
					</DIV>
				</DIV>
			</html:form>
		</DIV>
	</DIV>
	<jsp:include page="footer.jsp"/>
</BODY>
</HTML>
