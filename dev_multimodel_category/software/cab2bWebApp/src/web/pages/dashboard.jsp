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
<SCRIPT language="javaScript">
toolTipId = null;
 this.tooltipinvoker = function()
{			
  xOffset = (navigator.appName.indexOf('Netscape')==-1?(100 - document.getElementById('toppanel').scrollTop):0);
  yOffset = -175;			
   $(".tooltipinvoker").hover(function(e)
  {	
	$(document.getElementById(toolTipId)).show();
    $(document.getElementById(toolTipId))
    .css("top",(e.pageY - xOffset) + "px")
    .css("left",(e.pageX + yOffset) + "px")
    .fadeIn("fast");		
  }, 
   function()
  {	
    $(document.getElementById(toolTipId)).hide();
  });			
};
 function updateView()
{ 
   if(navigator.appName.indexOf('Netscape')==-1 || navigator.appVersion.indexOf('Apple')!=-1)
  {	  
	document.getElementById('toppanel').style.height = getScreenHeight() - (215);
    document.getElementById('toppanel').style.overflow = 'auto';
	 if(document.getElementById('dashboardtable'))
    {
      document.getElementById('dashboardtable').getElementsByTagName('thead')[0].getElementsByTagName('tr')[0].id = 'noscroll';
	}
  }
   else
  {
	document.getElementById('toppanel').style.height = getScreenHeight() - (220);
	 if(document.getElementById('dashboardtable'))
    {	 
	  document.location = "#scroller";
	   if(document.body.scrollTop>0)
	  {
	    document.getElementById('dashboardtable').getElementsByTagName('tbody')[0].style.height = getScreenHeight() - '265';	 
	  }
	}
	 if(document.getElementById('toppanelbuffer').innerHTML!="")
	{
	  document.getElementById('toppanel').innerHTML = document.getElementById('toppanelbuffer').innerHTML;
	  document.getElementById('toppanelbuffer').innerHTML = "";
	}	
  }
  processAJAXRequest('DisplayDashboard.do', (navigator.appName.indexOf('Netscape')==-1?'toppanel':'toppanelbuffer'), 1);
  tooltipinvoker();
  t = setTimeout("updateView()", 5000);
}
</SCRIPT>
<BODY>
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
	<DIV id="toppanelbuffer" style="display:none;"></DIV>
	<DIV id="toppanel">
		<%@ include file="dashboardpanel.jsp" %>
	</DIV>
	<SCRIPT language="JavaScript">updateView();</SCRIPT>
</DIV>
<jsp:include page="footer.jsp"/>
</BODY>
</HTML>