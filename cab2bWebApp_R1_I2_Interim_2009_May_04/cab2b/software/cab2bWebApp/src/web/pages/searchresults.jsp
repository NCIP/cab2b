<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/searchresults.css" type="text/css">
</HEAD>
<BODY>
<jsp:include page="header.jsp"/>
<DIV style="text-align:center">
	<DIV id="toppanel">
		<DIV class="label">
			<bean:message key="label.savedsearchselect"/>
		</DIV>
		<DIV style="width:80%;float:right">
			<SELECT class="select" name="savedSearches"/>
				<logic:present name="savedSearches">
					<logic:iterate name="savedSearches" id="savedSearch">
						<OPTION value="<bean:write name="savedSearch"/>">
							<bean:write name="savedSearch"/>
						</OPTION>
					</logic:iterate>
				</logic:present> 
			</SELECT> 
		</DIV>
		<BR/>
		<DIV class="label">
			<bean:message key="label.serviceinstanceselect"/>
		</DIV>
		<DIV style="width:80%;float:right">
			<SELECT class="select" name="queryNames"/>
				<logic:present name="serviceInstances">
					<logic:iterate name="serviceInstances" id="serviceInstance">
						<OPTION value="<bean:write name="serviceInstance"/>">
							<bean:write name="serviceInstance"/>
						</OPTION>
					</logic:iterate>
				</logic:present> 
			</SELECT> 
		</DIV>
	</DIV>
</DIV>
<BR/>
<DIV id="centerpanel">
	<DIV id="centerpanelcontent">
		<%@ include file="searchresultspanel.jsp" %>
	</DIV>
</DIV>
<DIV id="bottompanel">
	<INPUT type="button" class="button" value="<bean:message key="button.home"/>" onClick="document.location='Home.do'">
</DIV>
<jsp:include page="footer.jsp"/>
</BODY>
</HTML>