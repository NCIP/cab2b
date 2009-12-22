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
<%@ page import="edu.wustl.cab2bwebapp.dvo.* "%>

function checkImageSize()
   {
	var imageObj=document.getElementsByTagName('img');
		  
	  for(var i=0;i<imageObj.length;i++)
		{
			if(imageObj[i].width >120 ){
				imageObj[i].width='120';
			}
			if(imageObj[i].height >50 ){
				imageObj[i].height='50';
			}
	}

	var link=document.getElementsByTagName('a');
	
    }


</SCRIPT>
</HEAD>
<BODY onLoad="checkImageSize();">
<DIV style="text-align:center;">

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
<DIV id="toppanel">
		<TABLE cellspacing="1" cellpadding="1" width="100%" style="background-color: #ddd;">
			<TR>
			<TD width="20%" >
					<logic:notEmpty name="displayallannotation">
						<div style="text-align:center;font-weight:bold">
						<img style="align:center" src="${sessionScope.displayallannotation.resourceLogoURl}"/>
						<br> ${sessionScope.displayallannotation.resourceName}
						</div>		
				</logic:notEmpty>
				
			</TD>
			<TD width="100%">
				<DIV class="label" style="padding-left:1em;">
					<logic:notEmpty name="displayallannotation">${sessionScope.displayallannotation.resourceDescription} </logic:notEmpty>

				</DIV>
			</TD>

		</TR>
		
	</TABLE>
</DIV>

<Div id="toppanel">
<TABLE class='simple'cellspacing="1" cellpadding="1" width="100%" class="simple" style="background-color: #ddd;">
	<TR>
		<TD>
			<DIV style="color:black;font-weight:bold;margin-left:.75em"">Annotation Found</DIV>
		</TD>
	</TR>
	<TR>
	 	<TD>
		<logic:iterate collection="${sessionScope.displayallannotation.list}" id="eachRow" type="edu.wustl.cab2bwebapp.dvo.AnnotationElementDVO">

		<a class='link' href='${eachRow.resourceURL}' target='_blank' title='${eachRow.fullDescription}'>
		<span style="margin-left:.75em">${eachRow.elementId}</span></a>: ${eachRow.description} <br>
		</logic:iterate>
		</TD>
		</TR>
</TABLE>
</div>
</DIV>
</BODY>
</HTML>