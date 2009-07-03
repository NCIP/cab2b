<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<DIV style="display:none" id="failedservicesAJAX">
	<logic:present name="failedServices">
		<logic:iterate name="failedServices" id="failedServices" indexId="indexId" type="edu.wustl.cab2b.common.user.ServiceURLInterface">
			<DIV class="${indexId%2==0?"odd":"even"}">
				<DIV class="failedservice text"><bean:write name="failedServices" property="hostingCenter"/>
				<BR/><bean:write name="failedServices" property="urlLocation"/></DIV>
			</DIV>
		</logic:iterate>
	</logic:present>
	<BR style="line-height:0.4em"/>
</DIV>
<DIV style="display:none" id="failedservicescountAJAX">
	(<bean:write name="failedServicesCount"/>)
</DIV>
<logic:present name="searchResultsView">
<logic:equal name="searchResultsView" value="processing">
	<DIV id="messages"><IMG src="images/PageLoading.gif" width="30" height="30" alt="Loading Data">  Processing
	</DIV>
</logic:equal>
<logic:notEqual name="searchResultsView" value="processing">
	<display:table name="${sessionScope.searchResultsView}" uid="row" class="simple" sort="page" defaultsort="1" pagesize="20" export="true" requestURI="" cellspacing="1" cellpadding="4" htmlId="searchresultstable">
		<logic:iterate name="row" id="column" type="edu.wustl.cab2bwebapp.dvo.SearchResultDVO">
			<display:column title="${column.title}" value="${column.value}" sortable="true" media="${column.media}" headerClass="sortable" scope="sessionScope"/>
			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="Results.pdf"/>
			<display:setProperty name="export.csv.filename" value="Results.csv"/>
			<display:setProperty name="export.excel.filename" value="Results.xls"/>	
		</logic:iterate>
	</display:table>
</logic:notEqual>
</logic:present>
<logic:notPresent name="searchResultsView">
	<DIV id="messages"><bean:message key="text.resultsempty"/></DIV>
</logic:notPresent>