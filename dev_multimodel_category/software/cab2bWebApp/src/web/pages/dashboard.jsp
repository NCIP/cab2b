<%@ page errorPage="failure.jsp" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<LINK rel="shortcut icon" href="../images/favicon.ico">
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/dashboard.css" type="text/css">
<SCRIPT language="JavaScript">
</SCRIPT>
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
		<TABLE id="dashboardtable" cellspacing="1" cellpadding="4">
			<TR>
				<TH><bean:message key="heading.querytitle"/></TH>
				<TH><bean:message key="heading.querytype"/></TH>
				<TH><bean:message key="heading.status"/></TH>
				<TH><bean:message key="heading.recordcount"/></TH>
				<TH><bean:message key="heading.failedhostinginstitutioncount"/></TH>
				<TH><bean:message key="heading.executedon"/></TH>
				<TH><bean:message key="heading.actions"/></TH>
			</TR>
			<logic:notEmpty name="queryStatusDVOList">
				<logic:iterate name="queryStatusDVOList" id="query" type="edu.wustl.cab2bwebapp.dvo.QueryStatusDVO">
				<TR>
					<TD class="row">
						<bean:write name="query" property="title"/>
					</TD>
					<TD class="row">
						<bean:write name="query" property="type"/>
					</TD>
					<TD class="row">
						<bean:write name="query" property="status"/>
					</TD>
					<TD class="row">
						<bean:write name="query" property="resultCount"/>
					</TD>
					<TD class="row">
						<bean:write name="query" property="failedHostingInstitutions"/>
					</TD>
					<TD class="row">
						<bean:write name="query" property="executedOn"/>
					</TD>
					<TD class="row">
						<IMG src="images/view_results.jpg" title="<bean:message key="img.alt.viewqueryresults"/>" style="cursor:pointer">&nbsp;
						<IMG src="images/form.gif" title="<bean:message key="img.alt.queryparameters" arg0="${query.failedHostingInstitutions}"/>" style="cursor:pointer">&nbsp;
						<IMG src="images/ico_file_excel.png" title="<bean:message key="img.alt.exportqueryresults"/>" style="cursor:pointer" onClick="document.location='ExportData.do?filePath=<bean:write name="query" property="resultsFilePath"/>';TogglePreloader(0);">&nbsp;
						<IMG src="images/cancel.gif" title="<bean:message key="img.alt.abortqueryexecution"/>" style="cursor:pointer">&nbsp;
					</TD>
				</TR>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="queryStatusDVOList">
				<TR>
					<TD class="text" colspan="7">
						<bean:message key="text.offlinequeriesempty"/>
					</TD>
				</TR>
			</logic:empty>
		</TABLE>
	</DIV>
	<DIV id="bottompanel">
		<INPUT type="button" class="button" value="<bean:message key="button.back"/>" onClick="history.go(-1)">&nbsp;<INPUT type="submit" class="button" value="<bean:message key="button.refresh"/>" onClick="document.reload();">
	</DIV>
</DIV>
<jsp:include page="footer.jsp"/>
</FORM>
</BODY>
</HTML>