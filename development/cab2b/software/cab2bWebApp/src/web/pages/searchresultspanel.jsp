<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<logic:present name="searchResultsView">
	<display:table name="searchResultsView" uid="row" class="simple" sort="page" defaultsort="1" pagesize="20" export="true" requestURI="" cellspacing="1" cellpadding="4" htmlId="searchresultstable">
		<logic:iterate name="row" id="column" type="edu.wustl.cab2bwebapp.dvo.SearchResultDVO">
			<display:column title="${column.title}" value="${column.value}" sortable="true" media="${column.media}" headerClass="sortable" scope="sessionScope"/>
			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="Results.pdf"/>
			<display:setProperty name="export.csv.filename" value="Results.csv"/>
			<display:setProperty name="export.excel.filename" value="Results.xls"/>	
		</logic:iterate>
	</display:table>
</logic:present>
<logic:notPresent name="searchResultsView">
	<DIV id="messages"><bean:message key="text.resultsempty"/></DIV>
</logic:notPresent>