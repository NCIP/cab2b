<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/home.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/validator.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
</HEAD>
<BODY onLoad="<logic:notPresent name="modelGroups">document.forms[0].action='Home.do';document.forms[0].submit();</logic:notPresent>">
	<FORM method="post" action="KeywordSearch.do" onsubmit="return checkEmptyTextFileld('keywordSearch', keywordSearchExample.value, true)">
		<jsp:include page="header.jsp"/>
		<jsp:include page="leftpanel.jsp"/>
		<DIV id="content">
			<DIV id="toppanel">
				<DIV class="label">
					<bean:message key="label.searchdatatype"/>
				</DIV>
				<DIV>
					<SELECT class="select" name="modelGroups" multiple="multiple" onChange="processAJAXRequest('DisplaySavedSearches.do?modelGroups=' + this.value, 'savedsearchespanelbody')" style="margin-right:2.4em">
						<logic:present name="modelGroups">
							<logic:iterate name="modelGroups" id="modelGroup" type="edu.wustl.cab2b.common.modelgroup.ModelGroupInterface">
								<OPTION value="<bean:write name="modelGroup" property="modelGroupName"/>">
									<bean:write name="modelGroup" property="modelGroupName"/>
								</OPTION>
							</logic:iterate>
						</logic:present>
					</SELECT>
					<A href="#this" class="link" onClick="document.forms[0].action='DisplayServiceInstances.do';document.forms[0].submit()"><bean:message key="link.databasestosearch"/></A>
				</DIV>
			</DIV>
			<DIV id="keywordsearchpanel">
				<DIV class="label"><bean:message key="label.keywordsearch"/></DIV> <INPUT type="text" class="textbox examplevalue" name="keyword" value="<bean:message key="textbox.keywordsearch.examplevalue"/>" onFocus="if(keywordSearchExample.value=='')keywordSearchExample.value=this.value;if(this.value==keywordSearchExample.value)this.value='';this.className='textbox'" onBlur="if(this.value=='')this.value=keywordSearchExample.value;if(this.value==keywordSearchExample.value)this.className='textbox exampleValue'"><INPUT type="hidden" name="keywordSearchExample" value="<bean:message key="textbox.keywordsearch.examplevalue"/>"> <INPUT type="submit" class="button" value="<bean:message key="button.keywordsearch"/>">
			</DIV>
			<DIV id="notificationpanel" style="padding-bottom: 1.2em">
				<DIV class="error hidden" id="error_keywordSearch"><bean:message key="error.keywordsearch.empty"/></DIV>
			</DIV>
			<DIV id="savedsearchespanel">
				<DIV class="titlebar">
					<DIV class="titlebarheader title">
						<DIV class="titlebarleftcurve">
							<DIV class="titlebarrightcurve">
								<DIV class="titlebarexpanded" onClick="document.getElementById('savedsearchespanelbody').style.display=(document.getElementById('savedsearchespanelbody').style.display=='block'?'none':'block');this.className=(this.className=='titlebarexpanded'?'titlebarcollapsed':'titlebarexpanded');">
								<bean:message key="title.savedsearches"/>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV id="savedsearchespanelbody">
					<jsp:include page="savedsearches.jsp"/>
				</DIV>
			</DIV>
		</DIV>
		<jsp:include page="footer.jsp"/>
	</FORM>
</BODY>
</HTML>