<%@ page errorPage="failure.jsp"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<LINK rel="shortcut icon" href="../images/favicon.ico">
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/searchresults.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
<SCRIPT language="javaScript">
 function executeQuery()
{	
   if(${sessionScope.isFirstRequest})
  {		
	processAJAXRequest('ExecuteQuery.do');
	getTransformedResults();
	<% session.setAttribute("isFirstRequest", false); %>
  }


}

 function getTransformedResults()
{
  processAJAXRequest('TransformQueryResultsAction.do', (navigator.appName.indexOf('Netscape')==-1?'centerpanelcontent':'centerpanelcontentbuffer'), 1);
   if(document.getElementById("resultcountAJAX") && document.getElementById('resultcount'))


  {
	document.getElementById('resultcount').innerHTML = document.getElementById("resultcountAJAX").innerHTML;
  }
  var resultCount = document.getElementById('resultcountAJAX')?document.getElementById('resultcountAJAX').innerHTML:'0';
   if(resultCount != '0')
  {
    document.getElementById('resultsmessage').innerHTML = document.getElementById('completeresultsmessage')?document.getElementById('completeresultsmessage').innerHTML:document.getElementById('exportmessage').innerHTML;
	
	//(document.getElementById('partialresultsmessage')!=null?"(document.getElementById('partialresultsmessage').innerHTML + document.getElementById('resultsmessage').innerHTML)":document.getElementById('resultsmessage').innerHTML);
	 if(document.getElementById('completeresultsmessage') || document.getElementById('partialresultsmessage'))
	{
	  document.getElementById('resultsmessage').style.textAlign = 'left';
	}
  }
   if(document.getElementById("completeresultsmessage"))
  { 	
	 if(document.getElementById("failedservicesAJAX"))
	{
	  document.getElementById('failedserviceslink').style.display = 'block';
	  document.getElementById("failedservicescount").innerHTML = document.getElementById("failedservicescountAJAX").innerHTML;    
      document.getElementById("failedservicespanelbody").innerHTML = document.getElementById("failedservicesAJAX").innerHTML;    
	}	
	 if(document.getElementById('executeinbackgroundbutton'))
	{
	  document.getElementById('executeinbackgroundbutton').disabled = true;
	  document.getElementById('executeinbackgroundbutton').className = 'buttondisabled';
	}    
	 if(resultCount=='0')
	{
	  document.getElementById('resultsmessage').innerHTML = "";
	}
	
	 document.getElementById('exportbutton').disabled = false;
     document.getElementById('exportbutton').className = 'button';
	
	return;
  }
  setTimeout("getTransformedResults()", 5000);
}

 function updateView()
{ 
   if(navigator.appName.indexOf('Netscape')==-1 || navigator.appVersion.indexOf('Apple')!=-1)
  {	  
	document.getElementById('centerpanelcontent').style.height = getScreenHeight() - (310);
    document.getElementById('centerpanelcontent').style.overflow = 'auto';
	 if(document.getElementById('searchresultstable'))
    {
      document.getElementById('searchresultstable').getElementsByTagName('thead')[0].getElementsByTagName('tr')[0].id = 'noscroll';
	}
  }
   else
  {
	document.getElementById('toppanel').style.marginLeft = "5";
	document.getElementById('toppanel').style.width = "98%";
	document.getElementById('centerpanelcontent').style.height = getScreenHeight() - (325);
	 if(document.getElementById('searchresultstable'))
    {
	  document.location = "#scroller";
	   if(document.body.scrollTop>0)
	  {	  
	    document.getElementById('searchresultstable').getElementsByTagName('tbody')[0].style.height = getScreenHeight() - '370';
	  }
	}
	 if(document.getElementById('centerpanelcontentbuffer').innerHTML!="")
	{
	  document.getElementById('centerpanelcontent').innerHTML = document.getElementById('centerpanelcontentbuffer').innerHTML;
	  document.getElementById('centerpanelcontentbuffer').innerHTML = "";
	}	
  }
  setTimeout("updateView()", 1);
}
</SCRIPT>
</HEAD>
<BODY onLoad="executeQuery();">
<jsp:include page="header.jsp"/>
<DIV style="text-align:center;">
	<DIV id="toppanel">
		<DIV class="label" style="font-weight:bold;padding-right:0.2em;">
			<bean:message key="label.savedsearchselect"/>
		 </DIV>
		<DIV id="queryDropDown" style="text-align:left;">
			<bean:size id="queryCount" name="savedQueries"/>
			<logic:notEqual name="queryCount" value="1">
				<SELECT class="select" name="savedQueries" onChange="processAJAXRequest('TransformQueryResultsAction.do?selectedQueryName=' + this.value + '&id=' + Math.floor(Math.random()*1000), 'centerpanelcontent');"/>
					<logic:present name="savedQueries">
						<logic:iterate name="savedQueries" id="savedSearch" type="edu.wustl.cab2bwebapp.dvo.SavedQueryDVO">
							<OPTION value="<bean:write name="savedSearch" property="name"/>" <logic:equal name="savedSearch" property="selected" value="true">selected</logic:equal>>
							<bean:write name="savedSearch" property="name"/>
							</OPTION>
						</logic:iterate>
					</logic:present>
				</SELECT>
			</logic:notEqual>
			<logic:equal name="queryCount" value="1">
				<logic:iterate name="savedQueries" id="savedSearch">
					<DIV class="label"><bean:write name="savedSearch" property="name"/>&nbsp;(<SPAN id="resultcount"><bean:write name="savedSearch" property="resultCount"/></SPAN>)</DIV>
				</logic:iterate>
			</logic:equal>	 
		</DIV>
		<DIV style="float:right;margin-top:0.12em;">
			<A class="link" style="margin-top:0.3em;display:none;" id="failedserviceslink" href="#this" onclick="document.getElementById('pageoverlay').style.display='block';document.getElementById('failedservicespanel').style.display='block';"><bean:message key="link.failedserviceinstances"/>(<SPAN id="failedservicescount"></SPAN>)</A>			
		</DIV><BR style="line-height:0.2em;"><HR style="height:1;color:#bbb">
		
		<DIV style="font-size:0.85em;display:none" id="exportmessage"><IMG SRC="images/PageLoading.gif" style="height:1.2em;width:1.2em;"> <bean:message key="message.export"/></DIV>
		<logic:notPresent name="keyword">
			<DIV style="font-size:0.85em;" id="resultsmessage"><IMG SRC="images/PageLoading.gif" style="height:1.2em;width:1.2em;"> <bean:message key="message.partialresults.formbased" arg0="${sessionScope.selectedQueryName}"/></DIV>
		</logic:notPresent>
		<logic:present name="keyword">
			<DIV style="font-size:0.85em;" id="resultsmessage"><IMG SRC="images/PageLoading.gif" style="height:1.2em;width:1.2em;"> <bean:message key="message.partialresults.keyword" arg0="${sessionScope.keyword}" arg1="${sessionScope.selectedQueryName}"/></DIV>
		</logic:present>
	</DIV>
	<DIV id="pageoverlay"></DIV>
	<DIV id="failedservicespanel">
		<DIV id="failedservicespanelheader" class="title"><bean:message key="title.failedserviceinstances"/><IMG style='cursor:pointer;position:absolute;right:0.6em;' alt='Close' src='images/close.jpg' onmouseover=this.src='images/close_hover.jpg' onmouseout=this.src='images/close.jpg' onclick="document.getElementById('pageoverlay').style.display='none';document.getElementById('failedservicespanel').style.display='none'"/></DIV>
		<DIV id="failedservicespanelbody">
		</DIV>
	</DIV>
</DIV>
<DIV id="centerpanel">
	<DIV id="centerpanelcontentbuffer" style="display:none;"></DIV>
	<DIV id="centerpanelcontent">
		<%@ include file="searchresultspanel.jsp" %>
	</DIV>
</DIV>
<SCRIPT language="JavaScript">updateView();</SCRIPT>
<DIV id="bottompanel">	
	<logic:notPresent name="userName">
		<DIV class="text"><bean:message key="message.offlineexecution.anonymous.user"/></DIV>
	</logic:notPresent>
	<logic:present name="userName">
		<DIV class="text"><bean:message key="message.offlineexecution.signedin.user"/> <A href="#" title="<bean:message key="message.offlineexecution.signedin.user.moreinfo"/>">(More info) </A></DIV>
		<INPUT type="button" class="button" id="executeinbackgroundbutton" value="<bean:message key="button.executeinbackground"/>" onClick="document.location='BackgroundQuery.do'">&nbsp;
	</logic:present>
	<INPUT type="button" class="buttondisabled" id="exportbutton" value="<bean:message key="button.export"/>" onClick="document.location = 'ExportResults.do?queryId=<bean:write name="queryId"/>';TogglePreloader(0);" disabled>
</DIV>
<jsp:include page="footer.jsp"/>
</BODY>
</HTML>
