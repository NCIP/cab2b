<%@ page errorPage="failure.jsp"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<LINK rel="shortcut icon" href="../images/favicon.ico">
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/searchresults.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/jquery.js"></SCRIPT>
<SCRIPT language="javaScript">
checkFlag = false;

function checkAll(){
 	var obj = document.getElementsByName('checkBox');
	if(checkFlag){
		checkFlag = false;
	}
	else {
		checkFlag = true;
	}

 	for(var i =0; i< obj.length;i++){
		 obj[i].checked=checkFlag;
	}
	}


function changeText(){
	var obj = document.getElementById('LabelPreloader');
	
	obj.innerHTML="<SPAN ID='LabelPreloader' STYLE='font-size:11px'><STRONG>Please Wait ..   </STRONG><BR> Searching  Concepts ..</SPAN>"
}


function isChecked(){
	var obj = document.getElementsByName('checkBox');
	var flag=false;

 	for(var i =0; i< obj.length;i++){
		 flag=obj[i].checked;
		 if(flag==true){
		 break;
		 }
	}
	if(flag==false){
	alert("Please select the record for getting annotation")
	}
	
if(flag){
	changeText();
	TogglePreloader(1);
	processAJAXRequest('CheckConcept.do?index='+getIndex()+'&id=' + Math.floor(Math.random()*1000), 'centerpanelcontent',true,showConcepts);
	}
	}

function showConcepts(){

if(navigator.appName.indexOf('Netscape')==-1 || navigator.appVersion.indexOf('Apple')!=-1)
  {	  
	document.getElementById('centerpanelcontent').style.height = getScreenHeight() - (335);
    document.getElementById('centerpanelcontent').style.overflow = 'auto';
	document.getElementById('centerpanelcontent').style.width = '100%';
	document.getElementById('queryConditions').style.width = '0%';
  }
   else
  {
	document.getElementById('toppanel').style.marginLeft = "5";
	document.getElementById('toppanel').style.width = "98%";
	document.getElementById('centerpanelcontent').style.height = getScreenHeight() - (340);
	 if(document.getElementById('searchresultstable'))
    {
      document.getElementById('searchresultstable').getElementsByTagName('tbody')[0].style.height = getScreenHeight() - '370';
	}
	 if(document.getElementById('centerpanelcontentbuffer').innerHTML!="")
	{
	  document.getElementById('centerpanelcontent').innerHTML = document.getElementById('centerpanelcontentbuffer').innerHTML;
	  document.getElementById('centerpanelcontentbuffer').innerHTML = "";
	}	
  }

}


function getIndex(){
	index ='';
	var obj = document.getElementsByName('checkBox');
	
 	for(var i =0; i< obj.length;i++){
 		if(obj[i].checked){
 			index += obj[i].id-1+'sep';
 		 }
		} 
	return index;
}
 function executeQuery()
{	
  processAJAXRequest('ExecuteQuery.do');	
  getTransformedResults();
}

 function getTransformedResults()
{
  processAJAXRequest('TransformQueryResultsAction.do', (navigator.appName.indexOf('Netscape')==-1 || navigator.appVersion.indexOf('Apple')?'centerpanelcontent':'centerpanelcontentbuffer'), 1, updateView);
}

 function updateView()
{  
   if(navigator.appName.indexOf('Netscape')==-1 || navigator.appVersion.indexOf('Apple')!=-1)
  {	  
	document.getElementById('centerpanelcontent').style.height = getScreenHeight() - (335);
    document.getElementById('centerpanelcontent').style.overflow = 'auto';
	document.getElementById('centerpanelcontent').style.width = '100%';
	document.getElementById('queryConditions').style.width = '0%';
  }
   else
  {
	document.getElementById('toppanel').style.marginLeft = "5";
	document.getElementById('toppanel').style.width = "98%";
	document.getElementById('centerpanelcontent').style.height = getScreenHeight() - (340);
	 if(document.getElementById('searchresultstable'))
    {
      document.getElementById('searchresultstable').getElementsByTagName('tbody')[0].style.height = getScreenHeight() - '370';
	}
	 if(document.getElementById('centerpanelcontentbuffer').innerHTML!="")
	{
	  document.getElementById('centerpanelcontent').innerHTML = document.getElementById('centerpanelcontentbuffer').innerHTML;
	  document.getElementById('centerpanelcontentbuffer').innerHTML = "";
	}	
  }
   if(document.getElementById("resultcountAJAX") && document.getElementById('resultcount'))
  {
	document.getElementById('resultcount').innerHTML = document.getElementById("resultcountAJAX").innerHTML;
  }
  var resultCount = document.getElementById('resultcountAJAX')?document.getElementById('resultcountAJAX').innerHTML:'0';
   if(resultCount != '0')
  {  if(document.getElementById('offlineExecutionButton'))
	{
		document.getElementById('offlineExecutionButton').style.display = 'block';
    }
	document.getElementById('resultsmessage').innerHTML = document.getElementById('completeresultsmessage')?document.getElementById('completeresultsmessage').innerHTML:document.getElementById('queryInProgressMessage').innerHTML;
	 if(document.getElementById('completeresultsmessage') || document.getElementById('partialresultsmessage'))
	{
	  document.getElementById('resultsmessage').style.textAlign = 'left';
	}
  }
   else
   {
    document.getElementById('resultsmessage').innerHTML = "";
	if(document.getElementById('preResultsQueryInfoPanel'))
	{
		document.getElementById('preResultsQueryInfoPanel').style.display = 'block';
	}
	if(document.getElementById('offlineExecutionButton'))
	  {
	  	document.getElementById('offlineExecutionButton').style.display = 'none';
	  }
   }
   if(document.getElementById("completeresultsmessage"))
  {
	
	   
	 if(document.getElementById("failedservicesAJAX"))
	{
	  document.getElementById('failedserviceslink').innerHTML = document.getElementById("failedserviceslinkAJAX").innerHTML;
	  
      document.getElementById("failedservicespanelbody").innerHTML = document.getElementById("failedservicesAJAX").innerHTML;    
	}	
	 if(document.getElementById('offlineExecutionButton'))
	{
	  document.getElementById('offlineExecutionButton').disabled = true;
	  document.getElementById('offlineExecutionButton').className = 'buttondisabled';
	}    
	 if(resultCount=='0')
	{
	  document.getElementById('resultsmessage').innerHTML = "";
	   if(document.getElementById('offlineExecutionButton'))
	  {
	  	document.getElementById('offlineExecutionButton').style.display = 'block';
	  }
	  document.getElementById('preResultsQueryInfoPanel').style.display = 'none';
	}
	else{
		document.getElementById('annotateButton').style.display = 'block';
	}
    document.getElementById('exportbutton').innerHTML = document.getElementById("exportbuttonAJAX").innerHTML;
	
	return;
  }
   t = setTimeout("getTransformedResults()", 5000);
}
toolTipId = null;
 this.tooltipinvoker = function()
{			
  xOffset = (navigator.appName.indexOf('Netscape')==-1?(100 - document.getElementById('toppanel').scrollTop):0);
   $(".tooltipinvoker").hover(function(e)
  {	
    if(navigator.appVersion.indexOf('Apple')!=-1)
	{
	  document.getElementById(toolTipId).style.width = "50%";
	}	
	$(document.getElementById(toolTipId))
    .css("top",(e.pageY) + "px")
    .css("right",(getScreenWidth() - e.pageX) + "px")
    .show();		
  }, 
   function()
  {	
    $(document.getElementById(toolTipId)).hide();
  });			
};
</SCRIPT>
</HEAD>
<BODY onLoad="executeQuery();">
<jsp:include page="header.jsp"/>
<DIV style="text-align:center;">
	<DIV id="toppanel">
		<DIV class="label" style="font-weight:bold;padding-right:0.2em;">
			<bean:message key="label.savedsearchselect"/>
		 </DIV>
		<DIV id="queryDropDown" style="float:left;">
			<bean:size id="queryCount" name="savedQueries"/>
			<logic:notEqual name="queryCount" value="1">
				<SELECT class="select" style="float:left;margin-right:0.4em;" name="savedQueries" onChange="clearTimeout(t);processAJAXRequest('TransformQueryResultsAction.do?selectedQueryName=' + this.value + '&id=' + Math.floor(Math.random()*1000), 'centerpanelcontent', 0, updateView);"/>
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

		<DIV style="float:right;">
			<logic:present name="userName">
				<INPUT style="margin-top:0.3em;display:none;" type="button" class="button" id="offlineExecutionButton" value="<bean:message key="button.offlineexecution"/>" onClick="document.location='BackgroundQuery.do'">
			</logic:present>	
		</DIV>
		<DIV id="failedserviceslink" style="float:right;">
			<IMG src="images/service_instance_disabled.jpg"  style="margin-top:0.3em;" title="<bean:message key="link.failedserviceinstances"/>"/>&nbsp;&nbsp;
		</DIV>
		<DIV id="exportbutton" style="float:right;">
			<IMG src="images/export.jpg" style="margin-top:0.3em;" title="<bean:message key="img.alt.exportresults"/>" />&nbsp;&nbsp;
		</DIV>
				
		<DIV style="float:right;">
			<IMG class="tooltipinvoker" src="images/form.jpg" style="margin-top:0.3em;cursor:pointer;" onmouseover="toolTipId='queryConditions'">&nbsp;&nbsp;
		</DIV>
		<DIV class="tooltip" id="queryConditions" style="display:none;">
			<display:table class="simple" cellspacing="1" cellpadding="4" name="${requestScope.queryConditions}" uid="queryCondition" requestURI="">
				<display:column title="Parameter" value="${queryCondition.parameter}"/>
				<display:column title="Condition" value="${queryCondition.condition}"/>
				<display:column title="Value" value="${queryCondition.value}"/>
			</display:table>
		</DIV>
		
		<BR style="line-height:0.2em;">
		<HR style="height:1;color:#bbb">	
		
		<DIV style="font-size:0.85em;display:none" id="queryInProgressMessage"><IMG SRC="images/PageLoading.gif" style="height:1.2em;width:1.2em;"/> 
			<logic:notPresent name="userName">
				<bean:message key="message.query.inprogress.anonymous.user"/>
			</logic:notPresent>
			<logic:present name="userName">
				<bean:message key="message.query.inprogress.signedin.user"/>&nbsp;
				<IMG style="height:1.3em;width:1.3em;" src="images/more_info.gif" title="<bean:message key="message.offlineexecution.signedin.user.moreinfo"/>" />
			</logic:present>
		</DIV>
		<DIV style="font-size:0.85em;" id="resultsmessage"></DIV>
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
	<DIV id="centerpanelcontent"><%@ include file="searchresultspanel.jsp" %>
	</DIV>
	<INPUT type="button" class="button" id="annotateButton" style="display:none" value="<bean:message key="button.annotate"/>"onClick="isChecked();">
</DIV>

<SCRIPT language="JavaScript">updateView();tooltipinvoker();</SCRIPT>
<DIV id="bottompanel">
	<jsp:include page="footer.jsp"/>
</DIV>
</BODY>
</HTML>