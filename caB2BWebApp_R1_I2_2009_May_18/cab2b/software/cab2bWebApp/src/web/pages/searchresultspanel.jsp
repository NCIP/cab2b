<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@page	import="edu.common.dynamicextensions.domaininterface.AttributeInterface"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="edu.wustl.cab2bwebapp.constants.Constants"%>
<%@page import="edu.wustl.cab2b.common.util.Utility"%>
<%
   request.setAttribute(Constants.SEARCH_RESULTS_VIEW, (List<Map<AttributeInterface,Object>>) session.getAttribute(Constants.SEARCH_RESULTS_VIEW));         
   List<AttributeInterface>  orderList = (List<AttributeInterface>)session.getAttribute(Constants.ATTRIBUTE_ORDER);
%>
<logic:present name="searchResultsView">
	<display:table name="searchResultsView" id="rec" class="simple" sort="page" defaultsort="1" pagesize="20" export="true" requestURI="" cellspacing="1" cellpadding="4">
		<%
			Map<AttributeInterface, Object> record = (Map<AttributeInterface, Object>) rec;
		    Collection<AttributeInterface> keys = orderList == null ? record.keySet() : orderList;
		     for (AttributeInterface a : keys) 
			{
				Object value = record.get(a);
				String title = Utility.getFormattedString(a.getName());
		%>
				<display:column title="<%=title%>" value="<%=value%>" sortable="true" headerClass="sortable" media="<%=(!title.equals("Point of Contact") && !title.equals("Hosting Cancer Research Center") && !title.equals("Contact e Mail"))?"html" + (!title.equals("Hosting Institution")?" csv excel pdf":""):"csv excel pdf"%>" />
				<display:setProperty name="export.pdf" value="true" />
				<display:setProperty name="export.pdf.filename" value="Results.pdf"/>
				<display:setProperty name="export.csv.filename" value="Results.csv"/>
				<display:setProperty name="export.excel.filename" value="Results.xls"/>		
		<%
		    }
		%>
	</display:table>
</logic:present>
<logic:notPresent name="searchResultsView">
	<DIV id="messages"><bean:message key="text.resultsempty"/></DIV>
</logic:notPresent>