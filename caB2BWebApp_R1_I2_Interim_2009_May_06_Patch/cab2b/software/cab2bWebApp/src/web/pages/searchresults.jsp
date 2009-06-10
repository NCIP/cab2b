<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="edu.wustl.cab2b.common.queryengine.result.FailedTargetURL"%>
<%@page import="edu.wustl.cab2b.common.user.ServiceURLInterface"%>
<%@page import="java.util.Set"%>
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
		<BR/>
		<logic:notEmpty name="failedServices">
			<DIV style="margin-top:0.5em">
				<A class="link" href="#this" onclick="document.getElementById('grayBG').style.display='block';document.getElementById('popup').style.display='block';">Error in following Service URL's</A>
			</DIV>
		</logic:notEmpty>
	</DIV>
</DIV>

<DIV id="grayBG"></DIV>

<DIV id="popup" class="text">
	<DIV style="text-align:right">
		<A class="link" href="#this" onclick="document.getElementById('grayBG').style.display='none';document.getElementById('popup').style.display='none'">close</A>
	</DIV>
	<DIV>Following services Instances could not be queried</DIV>
	<logic:present name="failedServices">
		<logic:iterate name="failedServices" id="failedServices" indexId="indexId" type="edu.wustl.cab2b.common.user.ServiceURLInterface">
			<DIV class="<%=indexId%>%2=='0'?'lightgrey':'darkgrey'">
				<DIV><bean:write name="failedServices" property="hostingCenter"/></DIV>
				<DIV><bean:write name="failedServices" property="urlLocation"/></DIV>
			</DIV>
		</logic:iterate>
	</logic:present>
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