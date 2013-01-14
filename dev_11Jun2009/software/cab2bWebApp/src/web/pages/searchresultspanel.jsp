<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<%
 if(session.getAttribute("UI_population_finished")!=null)
 {
%>
   <DIV id="UI_population_finished"></DIV>
<%
 }
%>
<%
 if(session.getAttribute("stopAjax")!=null)
 {
%>
   <DIV id="stopAjax"></DIV>
<%
 }
%>
<DIV style="display:none" id="partialQueryResultsAJAX">
	<logic:present name="savedQueries">
			<logic:iterate name="savedQueries" id="savedSearch">
				<DIV class="text" style="font-size:0.9em;"><bean:write name="savedSearch" property="name"/>&nbsp;(<span id="queryUIPartialCount"><bean:write name="savedSearch" property="resultCount"/></span>)<SPAN id="progressImage"><logic:notPresent name="stopajax">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <IMG height=15 src='images/PageLoading.gif'>&nbsp;&nbsp; Loading results . . . </logic:notPresent></SPAN></DIV>
			</logic:iterate>
	</logic:present>
</DIV>
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
<DIV style="display:none" id="failedservicescountAJAX">
	(<bean:write name="failedServicesCount"/>)
</DIV>
</logic:present>
<logic:present name="searchResultsView">
<logic:equal name="searchResultsView" value="processing">
	<DIV id="messages"><IMG src="images/PageLoading.gif" width="30" height="30" alt="Loading Data">
	</DIV>
</logic:equal>
<logic:notEqual name="searchResultsView" value="processing">
	<display:table name="${sessionScope.searchResultsView}" uid="row" class="simple" export="true" requestURI="" cellspacing="1" cellpadding="4" htmlId="searchresultstable">
		<logic:iterate name="row" id="column" type="edu.wustl.cab2bwebapp.dvo.SearchResultDVO">
			<display:column title="${column.title}" value="${column.value}" sortable="false" media="${column.media}" headerClass="sortable" scope="sessionScope"/>
			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="Results.pdf"/>
			<display:setProperty name="export.csv.filename" value="Results.csv"/>
			<display:setProperty name="export.excel.filename" value="Results.xls"/>	
		</logic:iterate>
	</display:table>
</logic:notEqual>
</logic:present>
<logic:notPresent name="searchResultsView">
	<DIV class="text" style="display:none" align="center" id="messagesNotEmpty"><bean:message key="text.resultsempty"/></DIV>
</logic:notPresent>