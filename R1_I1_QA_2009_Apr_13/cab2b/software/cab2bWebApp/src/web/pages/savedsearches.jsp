<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<logic:empty name="savedSearches">
	<DIV id="messages">
		<bean:message key="message.savedsearches.empty"/>
	</DIV>
</logic:empty>

<logic:present name="savedSearches">
	<logic:iterate name="savedSearches" id="savedSearch" type="edu.wustl.cab2b.common.queryengine.ICab2bQuery">
		<A href="#" class="link"><bean:write name="savedSearch" property="name"/></A><br/>
		<bean:write name="savedSearch" property="description"/></br>
	</logic:iterate>
</logic:present>