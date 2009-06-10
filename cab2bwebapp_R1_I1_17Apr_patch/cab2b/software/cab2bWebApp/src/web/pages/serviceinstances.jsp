<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
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
    if(!(chk && !confirm('Your changes will be lost! Do you want to continue?')))
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
    alert("Please select at least one service instance!");
    return false;
  } 
} 
</SCRIPT>
</HEAD>
<BODY>
<FORM method="post" action="SaveServiceInstances.do" onSubmit="return validateSubmit()">
<jsp:include page="header.jsp"/>
<DIV id="content">
	<DIV class="titlebar">
		<DIV class="titlebarheader title">
			<DIV class="titlebarleftcurve">
				<DIV class="titlebarrightcurve">
					<bean:parameter name="modelGroups" id="modelGroups"/><bean:write name="modelGroups"/>					
				    <input type="hidden" name="modelGroups" value="<bean:write name="modelGroups"/>">
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
		<INPUT type="button" class="button" value="&lt;&lt;Back" onClick="validateBack()">&nbsp;<INPUT type="submit" class="button" value="Save Settings">
	</DIV>
</DIV>
<jsp:include page="footer.jsp"/>
</FORM>
</BODY>
</HTML>