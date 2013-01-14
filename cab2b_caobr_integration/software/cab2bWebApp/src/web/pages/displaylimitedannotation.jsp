<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ page errorPage="failure.jsp"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title" /></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/resourcedisplay.css"
	type="text/css">
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
<SCRIPT language="javaScript">

function showWindow(resourceId){
	var url = "ShowAllAnnotation.do?resourceId="+resourceId;

	window.open(url,'name','width=800,height=400,scrollbars=yes');
}

function checkImageSize()
   {
	var imageObj=document.getElementById("annotationTable").getElementsByTagName('img');
		  
	  for(var i=0;i<imageObj.length;i++)
		{
		
			if(imageObj[i].width > '150' ){
			imageObj[i].width='150';
			}
			if(imageObj[i].height > '50' ){
			imageObj[i].height='50';
			}
			
		}
	var widthObj=document.getElementById("annotationTable").getElementsByTagName('td');

		for(var i=0;i<widthObj.length;i=i+2)
		{	
			widthObj[i].width='1%';
			widthObj[i+1].width='100%';
		}
    }

	function changeBackgoundColor(){

	var imageObj=document.getElementById("annotationTable").getElementsByTagName('td');
	
	 for(var i=0;i<imageObj.length;i++)
		{
			imageObj[i].style.backgroundColor ='#EEEEEE';
	    }
	}

  function updateView()
{ 

   checkImageSize();
   changeBackgoundColor();
   if(navigator.appName.indexOf('Netscape')==-1)
  {	  
	document.getElementById('centerpanelcontent').style.height = getScreenHeight() - (310);
    document.getElementById('centerpanelcontent').style.overflow = 'auto';
	 if(document.getElementById('annotationTable'))
    {
      document.getElementById('annotationTable').getElementsByTagName('thead')[0].getElementsByTagName('tr')[0].id = 'noscroll';
	}
  }
   else
  {
	document.getElementById('centerpanelcontent').style.height = getScreenHeight() - (325);
	 if(document.getElementById('annotationTable'))
    {
	  document.getElementById('annotationTable').getElementsByTagName('tbody')[0].style.height = getScreenHeight() - '370';
	}
  }
  setTimeout("updateView()", 1);
}


</SCRIPT>
</HEAD>
<BODY onLoad="updateView();">
<jsp:include page="header.jsp" />
<DIV style="text-align: center;">
<DIV id="toppanel">
<TABLE cellspacing="1" cellpadding="1" width="100%"
	style="background-color: #ddd;">
	<TR>
		<TD width="1%">
		<DIV class="label" style="float: none;"><bean:message
			key="label.termdisplay" />&nbsp;<span style="color: blue;">${sessionScope.token}</span></DIV>
		</TD>
	</TR>
</TABLE>
</DIV>
</DIV>
<DIV id="centerpanel">
<DIV id='top'></DIV>
<DIV id="centerpanelcontent"><%@ include file="annotationresultspanel.jsp"%></DIV>
<DIV id='bottom'></DIV>
</DIV>
<jsp:include page="footer.jsp" />
</BODY>
</HTML>