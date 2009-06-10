<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<logic:empty name="savedSearches">
	<DIV id="messages">
		<bean:message key="message.savedsearches.empty"/>
	</DIV>
</logic:empty>

<logic:present name="savedSearches">
	<logic:iterate name="savedSearches" id="savedSearch" type="edu.wustl.cab2b.common.queryengine.ICab2bQuery">
		<A href="#" class="link" style="font-weight:0.84em"><bean:write name="savedSearch" property="name"/></A>
		<DIV class="text" style="display: inline; margin-bottom: 0.5em"><bean:write name="savedSearch" property="description"/></DIV><BR/><BR style="line-height:10px"/>
	</logic:iterate>
</logic:present>