<%@ page errorPage="failure.jsp"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%
  String queryId = request.getParameter("queryId");
  Long query_id = null;
  Boolean isFirstRequest = null;
  if(queryId != null &&  !queryId.equals(""))
  {
  	query_id = Long.parseLong(queryId);
  	if(query_id == null )
  	    query_id = (Long) session.getAttribute("queryId");
  	isFirstRequest = (Boolean) session.getAttribute("isFirstRequest");
  	if(isFirstRequest != null && isFirstRequest == true) 
  		session.setAttribute("isFirstRequest", false);
  }
%>
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<LINK rel="shortcut icon" href="../images/favicon.ico">
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/searchresults.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
<SCRIPT language="javaScript">

function exportdata()
{
document.getElementById('exportDiv').innerHTML = ' <IMG src="images/PageLoading.gif" width="30" height="30" alt="Loading Data"/>';
var action='ExportData.do';
mywindow=window.open(action,"Download","width=10,height=10");
mywindow.moveTo(0,0);
document.getElementById('exportDiv').innerHTML = '<INPUT type="button" class="button" value="<bean:message key="button.exportData"/>" onClick="javascript:exportdata();">';
//if(document.getElementById('queryUIPartialCount') != null)
//{
//	var queryUIPartialCount = document.getElementById('queryUIPartialCount').innerHTML;
//	document.getElementById('ExportAll_Message_Div').innerHTML ='Displaying first '+ queryUIPartialCount+' records.';
//}	
}

 function executeQuery()
{
	if('<%=queryId%>' != "" && '<%=queryId%>' != null  )
	{  
		if(<%=isFirstRequest%>==true)
		{		
			processAJAXRequest('ExecuteQuery.do?queryId=' + '<%=query_id%>');
			getTransformedResults();	
		}
	}
	else	// in case of KeyWord Query, failed hosting institutions div should be made visible
	{
		if(document.getElementById("failedservicesAJAX")!=null)
		{
		  document.getElementById('failedServicesDiv').style.display = 'block';
		  document.getElementById("failedservicescount").innerHTML = document.getElementById("failedservicescountAJAX").innerHTML;    
	      document.getElementById("failedservicespanelbody").innerHTML = document.getElementById("failedservicesAJAX").innerHTML;    
		}
	}
}

 function getTransformedResults()
{
  var url = 'TransformQueryResultsAction.do?queryId=' + '<%=query_id%>';
  processAJAXRequest(url, 'centerpanelcontent', 1);

  if(document.getElementById("partialQueryResultsAJAX")!=null && document.getElementById("messages")==null)
  {
    document.getElementById('queryDropDown').innerHTML = document.getElementById("partialQueryResultsAJAX").innerHTML;
  }
  if(document.getElementById("UI_population_finished")!=null)
  {
  	document.getElementById('queryDropDown').innerHTML = document.getElementById("partialQueryResultsAJAX").innerHTML;
	if(document.getElementById('queryUIPartialCount') != null)
	{
		var queryUIPartialCount = document.getElementById('queryUIPartialCount').innerHTML;
		if(queryUIPartialCount != '0')	//Export message & button should not be visible for 0 results.
		{
		  	document.getElementById('exportDiv').style.display = 'block';	//make it visible only ( with the inactive image)
			document.getElementById('ExportAll_Message_Div').style.display = 'block'; 
			document.getElementById('ExportAll_Message_Div').innerHTML ='Displaying first '+ queryUIPartialCount+' records. You can export all the records once the query is complete.';
  		}
  	}
  	if(document.getElementById('progressImage') != null)
  	 document.getElementById('progressImage').innerHTML = "";
  }
  if(document.getElementById("stopAjax")!=null)
  { 	
	if(document.getElementById('queryUIPartialCount')!= null)
	{
		var queryUIPartialCount = document.getElementById('queryUIPartialCount').innerHTML;
		if(queryUIPartialCount != '0')	//Export message & button should not be visible for 0 results.
		{
			document.getElementById('ExportAll_Message_Div').style.display = 'block'; 
	  	    document.getElementById('exportDiv').style.display = 'block'; // make it visible along with enabling it.
	  	    document.getElementById('exportDiv').innerHTML = '<INPUT type="button" class="button" value="<bean:message key="button.exportData"/>" onClick="javascript:exportdata();">';
			document.getElementById('ExportAll_Message_Div').innerHTML ='Displaying first '+ queryUIPartialCount+' records. Export all to view complete records.';
		}
	}
	if(document.getElementById("failedservicesAJAX")!=null)
	{
	  document.getElementById('failedServicesDiv').style.display = 'block';
	  document.getElementById("failedservicescount").innerHTML = document.getElementById("failedservicescountAJAX").innerHTML;    
      document.getElementById("failedservicespanelbody").innerHTML = document.getElementById("failedservicesAJAX").innerHTML;    
	}
	
	if(document.getElementById("messagesNotEmpty")!=null)
 	{
		document.getElementById('messagesNotEmpty').style.display = 'block';
	}
	return;
  }
  
  if(results == "Exception") 
  {
	document.getElementById('centerpanelcontent').innerHTML = 'Exception occurred while executing your request.<br> Please contact administrator.';
	return;	
  }
  else
  {
    setTimeout("getTransformedResults()", 2000);
  }
}

 function updateView()
{
  if(!document.getElementById('searchresultstable'))
  setTimeout("updateView()", 100);
   else
  {
    clearTimeout("updateView()");
	 if(document.getElementById("failedservicescount"))
	{
	  document.getElementById("failedservicescount").innerHTML = document.getElementById("failedservicescountAJAX").innerHTML;    
      document.getElementById("failedservicespanelbody").innerHTML = document.getElementById("failedservicesAJAX").innerHTML;    
	}
    var spanObjs = document.getElementById("centerpanelcontent").getElementsByTagName('SPAN');
    var divObjs = document.getElementById("centerpanelcontent").getElementsByTagName('DIV');
     for(var i=0;i<spanObjs.length;i++)
    {
       if(spanObjs[i].className=='pagebanner')
      {
	    spanObjs[i].style.display = 'none';
	    pagebanner = spanObjs[i].innerHTML;
      }
       if(spanObjs[i].className=='pagelinks')
      {
	    spanObjs[i].style.display = 'none';
	    pagelinks = spanObjs[i].innerHTML;
      }
    }
     for(var i=0;i<divObjs.length;i++)
    {
       if(divObjs[i].className=='exportlinks')
      {
	    divObjs[i].style.display = 'none';
  	    exportlinks = "<div class='exportlinks'>" + divObjs[i].innerHTML + "</span>";
      } 
    }
    document.getElementById('centerpanelcontent').innerHTML = "<table id='searchresultstable' cellpadding='4' class='simple' cellspacing='1'>" + document.getElementById('searchresultstable').innerHTML + "</table>";
    document.getElementById("top").innerHTML = "<span class='pagebanner'>" + pagebanner + "<span style='float:right;position:absolute;right: 3.5em;'>" + pagelinks + "</span>" + "</span>";
    document.getElementById("bottom").innerHTML = exportlinks;
	 if(navigator.appName.indexOf('Netscape')==-1)
    {	  
      document.getElementById('centerpanelcontent').style.height = getScreenHeight() - (350);
	  document.getElementById('centerpanelcontent').style.overflow = 'auto';
	  document.getElementById('searchresultstable').getElementsByTagName('thead')[0].getElementsByTagName('tr')[0].id = 'noscroll';
	  document.getElementById('searchresultstable').getElementsByTagName('thead')[0].getElementsByTagName('tr')[0].style.backgroundColor = '#ccc';
	  for(var i=0;i<document.getElementById('searchresultstable').getElementsByTagName('tbody')[0].getElementsByTagName('tr').length;i++)
	  document.getElementById('searchresultstable').getElementsByTagName('tbody')[0].getElementsByTagName('tr')[i].height = 1;
    }
	 else
	{
      document.getElementById('searchresultstable').getElementsByTagName('tbody')[0].style.height =  getScreenHeight() - (445);
	}
  }
}
</SCRIPT>
</HEAD>
<BODY onLoad="executeQuery();">
<jsp:include page="header.jsp"/>
<DIV style="text-align:center;">
	<DIV id="toppanel">
		<TABLE cellspacing="1" cellpadding="2">
			<TR>
	 			<TD nowrap>
					<DIV class="label" style="float:none;">
						<bean:message key="label.savedsearchselect"/>
					 </DIV>
				</TD>
				<TD noWrap <logic:notEmpty name="failedServices">colspan="2"</logic:notEmpty>>
					<DIV id="queryDropDown">
						<bean:size id="queryCount" name="savedQueries"/>
						<logic:notEqual name="queryCount" value="1">
							<SELECT class="select" name="savedQueries" onChange="document.getElementById('top').innerHTML='';document.getElementById('bottom').innerHTML='';processAJAXRequest('KeywordSearch.do?savedQueries=' + this.value + '&id=' + Math.floor(Math.random()*1000), 'centerpanelcontent');updateView()"/>
								<logic:present name="savedQueries">
									<logic:iterate name="savedQueries" id="savedSearch" type="edu.wustl.cab2bwebapp.dvo.SavedQueryDVO">
										<OPTION value="<bean:write name="savedSearch" property="name"/>" <logic:equal name="savedSearch" property="selected" value="true">selected</logic:equal>>
											<bean:write name="savedSearch" property="name"/>&nbsp;(<bean:write name="savedSearch" property="resultCount"/>)&nbsp;
										</OPTION>
									</logic:iterate>
								</logic:present>
							</SELECT>
						</logic:notEqual>
						<logic:equal name="queryCount" value="1">
							<logic:iterate name="savedQueries" id="savedSearch">
								<DIV class="text" style="font-size:0.9em;"><bean:write name="savedSearch" property="name"/>&nbsp;(<bean:write name="savedSearch" property="resultCount"/>) <SPAN id="progressImage"> </SPAN></DIV>
							</logic:iterate>
						</logic:equal>	 
					</DIV>
				</TD>
			</TR>
			<TR>
				<TD nowrap>
					<DIV class="label" style="float:none;">
						<bean:message key="label.serviceinstanceselect"/>
					</DIV>	
				</TD>
				<TD>
					<SELECT class="select" name="serviceInstances" disabled/>
						<logic:present name="serviceInstances">
							<logic:iterate name="serviceInstances" id="serviceInstance">
								<OPTION value="<bean:write name="serviceInstance"/>">
									<bean:write name="serviceInstance"/>
								</OPTION>
							</logic:iterate>
						</logic:present> 
					</SELECT>
				</TD>
				<TD nowrap>
					<DIV id= "failedServicesDiv" style="display:none">
								&nbsp;&nbsp;<A class="link" href="#this" onclick="document.getElementById('pageoverlay').style.display='block';document.getElementById('failedservicespanel').style.display='block';"><bean:message key="link.failedserviceinstances"/></A>
								<SPAN class="text" id="failedservicescount"></SPAN>
					</DIV>
				</TD>
			</TR>
			<TR>
				<TD>
	      		</TD>
				<TD nowrap style="padding-top:7px">
					<DIV id="exportDiv" style="display:none" class="text">	
						<IMG style="border-top: 1px solid #cccccc; border-left:1px solid #cccccc;" src="images/ExportAll_Inactive.jpg" />&nbsp;<IMG height=15 src='images/PageLoading.gif'/>&nbsp; Query in progress...					
					</DIV>
				</TD>
			</TR>
		</TABLE>
		
	</DIV>		
	<DIV id="pageoverlay"></DIV>
	<DIV id="failedservicespanel">
		<DIV id="failedservicespanelheader" class="title"><bean:message key="title.failedserviceinstances"/><IMG style='cursor:pointer;position:absolute;right:0.6em;' alt='Close' src='images/close.jpg' onmouseover=this.src='images/close_hover.jpg' onmouseout=this.src='images/close.jpg' onclick="document.getElementById('pageoverlay').style.display='none';document.getElementById('failedservicespanel').style.display='none'"/></DIV>
		<DIV id="failedservicespanelbody">
		</DIV>
	</DIV>
</DIV>
<DIV id="centerpanel">
	<DIV id='top'></DIV>
	<DIV id="ExportAll_Message_Div" style="display:none" class="text" valign="middle" Nowrap></DIV>
	<DIV id="centerpanelcontent">
		<%@ include file="searchresultspanel.jsp" %>
	</DIV>
	<DIV id='bottom'></DIV>
</DIV>
<DIV id="bottompanel">
	<INPUT type="button" class="button" value="<bean:message key="button.home"/>" onClick="document.location='Home.do'">
</DIV>
<jsp:include page="footer.jsp"/>
</BODY>
</HTML>
