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
<SCRIPT language="JavaScript" src="javascript/multiselect.js"></SCRIPT>
<BODY onLoad="<logic:notPresent name="modelGroups">document.forms[0].action='Home.do';document.forms[0].submit();</logic:notPresent><logic:present name="modelGroups">if(document.getElementsByName('modelGroups')){processAJAXRequest('DisplaySavedSearches.do?modelGroups=' + document.getElementsByName('modelGroups')[0].value, 'savedsearchespanelbody');document.getElementsByName('modelGroups')[0].checked=true;singleSelect(document.getElementsByName('modelGroups'), document.getElementsByName('modelGroups')[0])}</logic:present>">
	<FORM method="post" action="KeywordSearch.do?page=home" onsubmit="return checkEmptyTextFileld('keyword', keywordSearchExample.value, true, '<bean:message key="error.keywordsearch.empty"/>')">
		<jsp:include page="header.jsp"/>
		<jsp:include page="leftpanel.jsp"/>				
		<DIV id="content">
			<DIV id="toppanel">
				<DIV class="label">
					<bean:message key="label.searchdatatype"/>
				</DIV>
				<DIV>
					<DIV class="myselect" style="float:left">		
						<DIV class="myselectbox" onclick="setDropDown()">
							<DIV id="selectshow" class="selectshow">---Select Data Types---</DIV>
							<INPUT type="hidden" name="selectinfo" id="selectinfo"/>							
						</DIV>
						<DIV class="myselectboxitems" id="myselectboxitems">
							<logic:present name="modelGroups">							
								<logic:iterate name="modelGroups" id="modelGroup" type="edu.wustl.cab2b.common.modelgroup.ModelGroupInterface">
									<DIV class="myselectboxitem">
										<INPUT type="checkbox" name="modelGroups" id="<bean:write name="modelGroup" property="modelGroupName"/>" value="<bean:write name="modelGroup" property="modelGroupName"/>" onClick="<logic:notPresent name="userName">if(<%=modelGroup.isSecured()%>){alert('This is a secure service! Please sign in to access data.');this.checked=false;}</logic:notPresent>if(this.checked || <%=!modelGroup.isSecured()%>){setChecked(document.getElementsByName('modelGroups'), document.getElementsByName('itemNames'));if(this.checked)processAJAXRequest('DisplaySavedSearches.do?modelGroups=' + this.value, 'savedsearchespanelbody');singleSelect(document.getElementsByName('modelGroups'), this);}else{singleSelect(document.getElementsByName('modelGroups'));}">
										<LABEL><bean:write name="modelGroup" property="modelGroupName"/></LABEL>
									</DIV>
								</logic:iterate>
							</logic:present>						
						</DIV>						
					</DIV>		
					<DIV>
						<A href="#this" class="link" onClick="if(selectedItemsCount>0){document.forms[0].action='DisplayServiceInstances.do';document.forms[0].submit()}else{alert('Please select data type!');}"><bean:message key="link.databasestosearch"/></A>
					</DIV>
				</DIV>
			</DIV>
			<DIV id="keywordsearchpanel">
				<DIV class="label"><bean:message key="label.keywordsearch"/></DIV> <INPUT type="text" class="textbox examplevalue" name="keyword" value="<bean:message key="textbox.keywordsearch.examplevalue"/>" onFocus="if(keywordSearchExample.value=='')keywordSearchExample.value=this.value;if(this.value==keywordSearchExample.value)this.value='';this.className='textbox'" onBlur="if(this.value=='')this.value=keywordSearchExample.value;if(this.value==keywordSearchExample.value)this.className='textbox exampleValue'"><INPUT type="hidden" name="keywordSearchExample" value="<bean:message key="textbox.keywordsearch.examplevalue"/>"> <INPUT type="submit" class="button" value="<bean:message key="button.keywordsearch"/>">
			</DIV>			
			<DIV id="savedsearchespanel">
				<DIV class="titlebar">
					<DIV class="titlebarheader title">
						<DIV class="titlebarleftcurve">
							<DIV class="titlebarrightcurve">
								<DIV class="titlebarcollapsed" onClick="document.getElementById('savedsearchespanelbody').style.display=(document.getElementById('savedsearchespanelbody').style.display=='none'?'block':'none');this.className=(this.className=='titlebarexpanded'?'titlebarcollapsed':'titlebarexpanded');">
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