<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<bean:define id="i" value="0"/>
<DIV id="preResultsQueryInfoPanel" style="display:none;" class="text" align="center" valign="middle">
	<logic:present name="keyword">
		<br><br><br><br>
		<IMG SRC="images/PageLoading.gif" style="height:1.2em;width:1.2em;"> <b style="font-size: 1.3em;"><bean:message key="message.preresults.keyword" arg0="${sessionScope.keyword}" arg1="${sessionScope.selectedQueryName}"/></b><br><br>
		<logic:present name="userName">
			<IMG style="height:1.3em;width:1.3em;" src="images/more_info.gif" title="<bean:message key="message.offlineexecution.signedin.user.moreinfo"/>" />
			<bean:message key="message.preresults.signedin.user"/><br><br>
			<INPUT type="button" style="font-size:1em;" class="button" value="<bean:message key="button.offlineexecution"/>" onClick="document.location='BackgroundQuery.do'">
		</logic:present>
		<logic:notPresent name="userName">
			<bean:message key="message.preresults.anonymous.user"/>
		</logic:notPresent>
	</logic:present>
	<logic:notPresent name="keyword">
		<br><br><br><br>
		<IMG SRC="images/PageLoading.gif" style="height:1.2em;width:1.2em;"> <b style="font-size: 1.3em;"><bean:message key="message.preresults.formbased" arg0="${sessionScope.selectedQueryName}"/></b><br><br>
		<logic:present name="userName">
			<IMG style="height:1.3em;width:1.3em;" src="images/more_info.gif" title="<bean:message key="message.offlineexecution.signedin.user.moreinfo"/>" />
			<bean:message key="message.preresults.signedin.user"/><br><br>
			<INPUT type="button" style="font-size:1em;" class="button" value="<bean:message key="button.offlineexecution"/>" onClick="document.location='BackgroundQuery.do'">
		</logic:present>
		<logic:notPresent name="userName">
			<bean:message key="message.preresults.anonymous.user"/>
		</logic:notPresent>
	</logic:notPresent>
</DIV>
<logic:present name="searchResultsView">
	<logic:notEqual name="searchResultsView" value="processing">
		<display:table class="simple" name="${sessionScope.searchResultsView}" cellspacing="1" cellpadding="4" uid="row" htmlId="searchresultstable" id="row">
			<logic:present name="stopAjax">
				<display:column title="<input type='checkbox' onClick='checkAll()'>" sortable="false" headerClass="unsortable">
					<input type='checkbox' id='${row_rowNum}' name='checkBox' >
				</display:column>
			</logic:present>
			<logic:iterate name="row" id="column" type="edu.wustl.cab2bwebapp.dvo.SearchResultDVO">
				<display:column title="${column.title}" value="${column.value}" media="${column.media}" sortable="false" headerClass="unsortable"/>
			</logic:iterate>
		</display:table>
	</logic:notEqual>
</logic:present>
<logic:notPresent name="searchResultsView">
	<logic:present name="stopAjax">
		<br>
		<DIV class="text" align="center"><bean:message key="text.resultsempty" arg0="${sessionScope.selectedQueryName}"/></DIV>
	</logic:present>
</logic:notPresent>
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
	<DIV style="display:none" id="failedserviceslinkAJAX">
		<IMG src="images/service_instance.jpg"  style="margin-top:0.3em;cursor:pointer;" title="<bean:message key="link.failedserviceinstances"/>" onclick="document.getElementById('pageoverlay').style.display='block';document.getElementById('failedservicespanel').style.display='block';"/>&nbsp;&nbsp;
	</DIV>
</logic:present>
<DIV style="display:none">
	<DIV id="exportbuttonAJAX" >
		<IMG src="images/export.jpg" style="margin-top:0.3em;cursor:pointer;" title="<bean:message key="img.alt.exportresults"/>" onClick="document.location='ExportResults.do';setTimeout('TogglePreloader(0)', 1);">&nbsp;&nbsp;
	</DIV>
	<logic:present name="queryUIPartialCount">
		<DIV id="partialresultsmessage"><bean:message key="message.partialresults" arg0="${savedSearch.resultCount}"/></DIV>
	</logic:present>
	<logic:present name="stopAjax">
		<logic:present name="transformationMaxLimit">
			<logic:present name="queryResultCount">
				<logic:lessEqual name="transformationMaxLimit" value="${queryResultCount}">
					<DIV id="completeresultsmessage"><bean:message key="message.completeresults.morethan.uilimit" arg0="${queryResultCount}"/></DIV>
				</logic:lessEqual>
				<logic:greaterThan name="transformationMaxLimit" value="${queryResultCount}">
					<DIV id="completeresultsmessage"><bean:message key="message.completeresults.lessthan.uilimit" arg0="${queryResultCount}"/></DIV>
				</logic:greaterThan>
			</logic:present>
		</logic:present>
	</logic:present>
</DIV>