<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<logic:empty name="SavedQueries">
	<DIV id="messages">
		<bean:message key="message.savedsearches.empty"/>
	</DIV>
</logic:empty>

<logic:present name="SavedQueries">
	<logic:iterate name="SavedQueries" id="savedquery" type="edu.wustl.cab2b.common.queryengine.ICab2bQuery">
		<A href="#" class="link"><bean:write name="savedquery" property="name"/></A><br/>
	</logic:iterate>
</logic:present>
