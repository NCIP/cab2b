<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<SELECT class="select" multiple name="serviceInstances">
	<logic:iterate name="ServiceInstances" id="service" type="edu.wustl.cab2b.common.user.ServiceURL">
		<OPTION value="<bean:write name="service" property="urlLocation"/>" <logic:equal name="service" property="isConfigured" value="true">selected</logic:equal>>
			<bean:write name="service" property="hostingResearchCenter"/>
		</OPTION>
	</logic:iterate>
</SELECT>