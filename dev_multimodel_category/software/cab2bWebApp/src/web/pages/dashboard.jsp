<%@ page errorPage="failure.jsp" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<LINK rel="shortcut icon" href="../images/favicon.ico">
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/dashboard.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/jquery.js"></SCRIPT>
<BODY>
<FORM>
<jsp:include page="header.jsp"/>		
<DIV id="content">
	<DIV class="titlebar">
		<DIV class="titlebarheader title">
			<DIV class="titlebarleftcurve">
				<DIV class="titlebarrightcurve">
					<bean:message key="title.offlinequeries"/>
				</DIV>
			</DIV>
		</DIV>
	</DIV>
	<DIV id="toppanel">
		<display:table class="simple" cellspacing="1" cellpadding="4" name="${requestScope.queryStatusDVOList}" uid="query" requestURI="" defaultsort="4"  defaultorder="descending">
			<display:column title="Title" value="${query.title}" sortable="true" headerClass="sortable"/>			
			<display:column title="Status" value="${query.status}" sortable="true" headerClass="sortable"/>
			<display:column title="Result Count" value="${query.resultCount}" sortable="true" headerClass="sortable"/>
			<display:column title="Executed On" value="${query.executedOn}" sortable="true" headerClass="sortable" format="{0,date,yyyy-MM-dd HH:mm}"/>	
			<display:column title="Action(s)" sortable="false" headerClass="unsortable">
				<IMG src="images/view_results.jpg" title="<bean:message key="img.alt.viewresults"/>" style="cursor:pointer;display:none;">&nbsp;
				<IMG class="tooltipinvoker" src="images/form.jpg" style="cursor:pointer" onmouseover="toolTipId='${query.conditions}'">&nbsp;
				<DIV class="tooltip" id="${query.conditions}" style="display:none">
					<display:table class="simple" cellspacing="1" cellpadding="4" name="${query.conditions}" uid="queryCondition" requestURI="">
						<display:column title="Parameter" value="${queryCondition.parameter}"/>
						<display:column title="Condition" value="${queryCondition.condition}"/>
						<display:column title="Value" value="${queryCondition.value}"/>
					</display:table>
				</DIV>
				<IMG src="images/ico_file_excel.png" title="<bean:message key="img.alt.exportresults"/>" style="cursor:pointer" onClick="document.location='ExportResults.do?fileName=<bean:write name="query" property="fileName"/>';TogglePreloader(0);">&nbsp;
				<IMG src="images/stop.gif" title="<bean:message key="img.alt.abortexecution"/>" style="cursor:pointer;display:none;">&nbsp;
			</display:column>
		</display:table>
	</DIV>
	<DIV id="bottompanel">
		<INPUT type="submit" class="button" value="<bean:message key="button.refresh"/>" onClick="document.reload();">
	</DIV>
</DIV>
<jsp:include page="footer.jsp"/>
</FORM>
</BODY>
</HTML>