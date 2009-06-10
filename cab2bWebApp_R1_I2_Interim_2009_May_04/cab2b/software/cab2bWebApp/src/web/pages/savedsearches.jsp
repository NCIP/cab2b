<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<logic:empty name="savedSearches">
	<DIV id="messages">
		<bean:message key="message.savedsearches.empty"/>
	</DIV>
</logic:empty>

<logic:present name="savedSearches">
	<logic:notPresent name="addlimit">
		<logic:iterate name="savedSearches" id="savedSearch" type="edu.wustl.cab2b.common.queryengine.ICab2bQuery">
			<A href="#this" class="link" onClick="document.forms[0].action='AddLimit.do';document.forms[0].submit();" name="savedquery"><bean:write name="savedSearch" property="name"/></A>
			<DIV class="text" style="display: inline; line-height: 1.8em"><bean:write name="savedSearch" property="description"/>&nbsp;</DIV><BR/>
		</logic:iterate>
	</logic:notPresent>
	<logic:present name="addlimit">
		<logic:iterate name="savedSearches" id="savedSearch" type="edu.wustl.cab2b.common.queryengine.ICab2bQuery">
			<A href="#this" class="link" onClick="updateLinks(this);processAJAXRequest('AddLimit.do?queryId=<bean:write name="savedSearch" property="id"/>', 'definelimitspanelbody');" title="<bean:write name="savedSearch" property="description"/>" name="savedquery" id="<bean:write name="savedSearch" property="id"/>"><bean:write name="savedSearch" property="name"/></A>
			<DIV class="text" style="display: inline; line-height: 1.8em">&nbsp;</DIV><BR/>
		</logic:iterate>
	</logic:present>			
</logic:present>