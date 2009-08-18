<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<logic:present name="savedQueries">
	<logic:iterate name="savedQueries" id="savedSearch">
		<DIV id="resultcountAJAX" style="display:none;"><bean:write name="savedSearch" property="resultCount"/></DIV>
	</logic:iterate>
</logic:present>
<logic:present name="failedServices">
	<DIV style="display:none" id="failedservicesAJAX">
		<logic:iterate name="failedServices" id="failedServices" indexId="indexId" type="edu.wustl.cab2b.common.user.ServiceURLInterface">
			<DIV class="${indexId%2==0?"odd":"even"}">
				<DIV class="failedservice text"><bean:write name="failedServices" property="hostingCenter"/>
				<BR/><bean:write name="failedServices" property="urlLocation"/></DIV>
			</DIV>
		</logic:iterate>
		<BR style="line-height:0.4em"/>
	</DIV>
	<DIV style="display:none" id="failedservicescountAJAX">(<bean:write name="failedServicesCount"/>)</DIV>
</logic:present>
<logic:present name="searchResultsView">
	<logic:notEqual name="searchResultsView" value="processing">
		<display:table class="simple" name="${sessionScope.searchResultsView}" cellspacing="1" cellpadding="4" uid="row" htmlId="searchresultstable">
			<logic:iterate name="row" id="column" type="edu.wustl.cab2bwebapp.dvo.SearchResultDVO">
				<display:column title="${column.title}" value="${column.value}" sortable="false" headerClass="unsortable"/>
			</logic:iterate>
		</display:table>
	</logic:notEqual>
</logic:present>
<logic:notPresent name="searchResultsView">
	<logic:present name="stopAjax">
		<DIV class="text" align="center"><bean:message key="text.resultsempty"/></DIV>
	</logic:present>
</logic:notPresent>
<DIV style="display:none">
	<logic:present name="queryUIPartialCount">
		<DIV id="partialresultsmessage"><bean:message key="message.partialresults" arg0="${savedSearch.resultCount}"/></DIV>
	</logic:present>
	<logic:present name="stopAjax">
		<DIV id="completeresultsmessage"><bean:message key="message.completeresults" arg0="${savedSearch.resultCount}"/></DIV>
	</logic:present>
</DIV>