<%@ page errorPage="failure.jsp" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<logic:notPresent name="modelGroupDVOList"><jsp:forward page="/Home.do"/></logic:notPresent>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<LINK rel="shortcut icon" href="../images/favicon.ico">
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/home.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/validator.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/multiselect.js"></SCRIPT>
<SCRIPT language="JavaScript">
 function validate()
{
  var invalidChars = "~!^?*|,\\\\/\":<>[]{}`\';()@&$#%";
  var userName = document.form.userName;
  var email = document.form.email;
  var phone = document.form.phone;
  var institution = document.form.institution;

   if(userName.value.length < 1){
  	alert('Enter your full name');
	return false;
   }

   if(invalidChars.indexOf(userName.value.charAt(i))!=-1){
  	alert('Invalid characters');
	return false;
   }

   if(email.value.length < 5){
  	alert('Enter your email address');
	return false;
   }

   if(phone.value.length < 7){
  	alert('Enter your phone number');
	return false;
   }

   if(institution.value.length < 2){
  	alert('Enter your institution name'); 
	return false;
   }

   for(var i=0;i<userName.value.length;i++)
  {
    if(invalidChars.indexOf(userName.value.charAt(i))!=-1) {
      alert("User name should not contain special characters!");
      userName.focus();
      return false;
    }
  }
  invalidChars = "~!^?*|,\\\\/\":<>[]{}`\';@&$#%";

   for(var i=0;i<phone.value.length;i++)
  {
    if(invalidChars.indexOf(phone.value.charAt(i))!=-1) {
      alert("Phone number should not contain special characters!");
      phone.focus();
      return false;
    }
  }


  invalidChars = "~!^?*|,\\\\/\":<>[]{}`\';&$#%";

   for(var i=0;i<email.value.length;i++)
  {
    if(invalidChars.indexOf(email.value.charAt(i))!=-1) {
      alert("Email address should not contain special characters!");
      email.focus();
      return false;
    }
  }

  return true;
}

<!--
 function setPage()
{
  <html:messages id="successUserLogout" message="true">
	alert('<bean:message key="success.user.logout"/>');
  </html:messages>
  <logic:present name="errorSessionTimeout">
	alert('<bean:message key="alert.sessiontimeout"/>');
  </logic:present>
  <logic:present name="invalidRequest">
	alert('<bean:message key="alert.invalidrequest"/>');
  </logic:present>
  <logic:notPresent name="modelGroupDVOList">
	document.forms[0].action = 'Home.do';
    document.forms[0].submit();
  </logic:notPresent>
  setSelection(document.getElementsByName('modelGroups'));
   if(selectedItemsCount>0)
  {
    processAJAXRequest('DisplaySavedSearches.do?modelGroups=' + document.getElementsByName('modelGroups')[singleSelectIndex].value, 'savedsearchespanelbody');
	if(selectedItemsCount==0)
    document.getElementById('savedsearchespanelbody').innerHTML = '';	
  }
  <html:messages id="serviceInstancesNotConfigured">  
  	if(confirm('<bean:message key="message.serviceinstancesnotconfigured"/>'))
   {
     validateDataTypeSelection();
   }
  </html:messages>
  <%
	 if(request.getAttribute("error")!=null) 
	{
  %>
	   if(confirm('<bean:message key="message.incorrectserviceinstanceconfigured"/>'))
      {
        validateDataTypeSelection();
      }
  <%
	}
     if(request.getParameter("redirect")!=null)
	{
  %>
	  document.body.style.display="none";
	  validateDataTypeSelection();
  <%
	}
  %>
  
   if("${requestScope.keywordQueryNotPresent}")
  {
	alert('<bean:message key="alert.keywordquerynotpresent"/>');
  }
}
-->

</SCRIPT>
<!--<BODY onLoad="setPage()">-->
<BODY> 
<div id="skipmenu">
<a href="#skip" class="skippy">Skip Navigation</a>
<a name="top"></a>
</div> <!-- end skipmenu -->
	<FORM name="form" method="post" action="mailto:ncicb@pop.nci.nih.gov?SUBJECT=caB2B Registration" onSubmit="return validate()" enctype="text/plain">

		<jsp:include page="header.jsp">
			<jsp:param name="home" value="home"/>
		</jsp:include>
		<jsp:include page="leftpanel.jsp"/>				

<DIV id="content">
		<DIV class="titlebarheader title">
			<DIV class="titlebarleftcurve">
				<DIV class="titlebarrightcurve">
			Register For Accounts
				</DIV>
			<BR/>
<a name="skip"></a>
			Register for National Cancer Institute Credentials:<br/>

<table>
                    <thead>
                    <tr><td>&nbsp;</td><td>&nbsp;</td></tr></thead>
                    <tbody>
<caption><strong>Registration Form</strong></caption>
		    <tr><td style="padding-right:5px; text-align:right"></td><td>(If you prefer you can email the information directly to ncicb at pop.nci.nih.gov)</td><tr>
                    <tr><td style="padding-right:5px; text-align:right"><label for="userName">Full Name:</label></td><td><input name="userName" id="userName" type="text" autocomplete="off" value="" size="29"/></td></tr>
                    <tr><td style="padding-right:5px; text-align:right"><label for="email">Email Address:</label></td><td><input name="email" id="email" type="text" autocomplete="off" value="" size="29"/></td></tr>
                    <tr><td style="padding-right:5px; text-align:right"><label for="phone">Phone:</label></td><td><input name="phone" id="phone" type="text" autocomplete="off" value="" size="29"/></td></tr>
                    <tr><td style="padding-right:5px; text-align:right"><label for="institution">Institution:</label></td><td><input name="institution" type="text" autocomplete="off" value="" size="29"/></td></tr>
		    <tr><td><br></td></tr>
                    <tr><td style="padding-right:5px; text-align:right"><input type="submit" value="Send"></td></tr>
		    <tr><td><br></td></tr>
		    <tr><td></td></tr>
</table>
<HR/>
						Register for caGrid Dorian Credentials:
			</DIV>
						<BR/>
						<A HREF="http://cagrid-portal.nci.nih.gov/web/guest/register">caGrid Portal</A>
<BR/>
<DIV>
						<A HREF="http://cagrid-portal.nci.nih.gov/web/guest/register">
						<IMG src="images/portalreg.jpg" alt="Portal Registration" WIDTH="658" HEIGHT="204"/>
</A>
		<jsp:include page="footer.jsp"/>
</DIV>
		</DIV>
</DIV>
	</FORM>
</BODY>
</HTML>
