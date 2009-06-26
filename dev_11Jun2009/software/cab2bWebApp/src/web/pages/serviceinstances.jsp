<%@ page errorPage="failure.jsp" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/serviceinstances.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">
chk = false;
 function validateBack()
{
    if(!(chk && !confirm('<bean:message key="confirmation.changeloss"/>')))
   {
	 document.forms[0].action = 'Home.do';
	 document.forms[0].submit(); 	 
   }
}
 function validateSubmit()
{
  var objs = document.getElementsByName("serviceInstances");
   for(i=0;i<objs.length;i++)
  {
     if(objs[i].checked)
    {
      break;
    }
  }
   if(i==objs.length)
  {
    alert("<bean:message key="alert.selectserviceinstance"/>");
    return false;
  } 
}
</SCRIPT>
</HEAD>
<BODY onLoad="document.getElementById('centerpanelcontent').style.height = getScreenHeight() - 280;">
<FORM method="post" action="SaveServiceInstances.do" onSubmit="return validateSubmit()">
<jsp:include page="header.jsp"/>
<DIV id="content">
	<DIV class="titlebar">
		<DIV class="titlebarheader title">
			<DIV class="titlebarleftcurve">
				<DIV class="titlebarrightcurve">
					<bean:parameter name="modelGroups" id="modelGroupName"/><bean:write name="modelGroupName"/>					
				    <input type="hidden" name="modelGroupName" value="<bean:write name="modelGroupName"/>">
				</DIV>
			</DIV>
		</DIV>
	</DIV>
	<DIV class="text" id="toppanel"><INPUT type="checkbox" onClick="toggleSelection('serviceInstances', this);chk=true;"><bean:message key="text.selectall"/></DIV>
	<DIV id="centerpanel">
		<DIV id="centerpanelcontent">
		<logic:present name="serviceInstances">
			<logic:iterate name="serviceInstances" id="serviceInstance" type="edu.wustl.cab2b.common.user.ServiceURLInterface">
				<DIV style="padding: 0.5em 0em 0.5em 0em">
					<DIV class="heading">
						<input type="checkbox" name="serviceInstances" value="<bean:write name="serviceInstance" property="urlLocation"/>" <logic:equal name="serviceInstance" property="configured" value="true">checked</logic:equal> onClick="chk=true"/>
						<bean:write name="serviceInstance" property="hostingCenter"/>
					</DIV>
					<DIV class="text" style="margin-left: 1.7em">
						<bean:write name="serviceInstance" property="description"/>
					</DIV>
				</DIV>
			</logic:iterate>			
		</logic:present>
		</DIV>
	</DIV>
	<DIV id="bottompanel">
		<INPUT type="button" class="button" value="<bean:message key="button.back"/>" onClick="validateBack()">&nbsp;<INPUT type="submit" class="button" value="<bean:message key="button.savesettings"/>">
	</DIV>
</DIV>
<jsp:include page="footer.jsp"/>
</FORM>
</BODY>
</HTML>