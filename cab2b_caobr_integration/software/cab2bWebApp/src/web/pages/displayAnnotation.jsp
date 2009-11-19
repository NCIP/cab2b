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

function checkImageSize()
   {
	var imageObj=document.getElementsByTagName('img');
		  
	  for(var i=0;i<imageObj.length;i++)
		{
			if(imageObj[i].width >200 ){
				imageObj[i].width='200';
			}
			if(imageObj[i].height >100 ){
				imageObj[i].height='100';
			}
	}

	var link=document.getElementsByTagName('a');
	
	 for(var i=0;i<link.length;i++)
		{
		link[i].style.fontSize='.68em';
		}
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
				<logic:notEmpty name="selectedRresourceImage">	<img src="${sessionScope.displayallannotation.resourceLogoURl}"</td>
				</logic:notEmpty>			
				
			</TD>
			<TD width="100%">
				<DIV class="label" style="padding-left:1em;">
					<logic:notEmpty name="selectedRresourceImage">${sessionScope.displayallannotation.resourceDescription} </logic:notEmpty>
				</DIV>
			</TD>

		</TR>
		
	</TABLE>
</DIV>

<Div id="toppanel">
<TABLE cellspacing="1" cellpadding="1" width="100%" style="background-color: #ddd;">
	<TR>
		<TD>
			<DIV class="label" style="float: none;"><span style="color: blue;margin-left:.75em"">Annotation Found</span></DIV>
		</TD>
	</TR>
	<TR>
	 	<TD>
		<logic:iterate name="${sessionScope.displayallannotation.list)" id="eachRow" type="edu.wustl.cab2bwebapp.dvo.AnnotationElementDVO">
				
		</logic:iterate>
					
	    </TD>
		</TR>
</TABLE>
</div>


</DIV>
</BODY>
</HTML>