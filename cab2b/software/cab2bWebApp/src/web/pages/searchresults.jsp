<%@ page errorPage="failure.jsp" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/searchresults.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
</HEAD>
<BODY onLoad="updateView();">
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
				<TD <logic:notEmpty name="failedServices">colspan="2"</logic:notEmpty>>
					<DIV>
						<bean:size id="queryCount" name="savedQueries"/>
						<logic:notEqual name="queryCount" value="1">
							<SELECT class="select" name="savedQueries" onChange="document.body.style.overflow='hidden';document.getElementById('centerpanel').style.visibility='hidden';processAJAXRequest('KeywordSearch.do?savedQueries=' + this.value, 'centerpanelcontent');updateView()"/>
								<logic:present name="savedQueries">
									<logic:iterate name="savedQueries" id="savedSearch" type="edu.wustl.cab2bwebapp.dvo.SavedQueryDVO">
										<OPTION value="<bean:write name="savedSearch" property="name"/>" <logic:equal name="savedSearch" property="selected" value="true">selected</logic:equal>>
											<bean:write name="savedSearch" property="name"/>&nbsp;(<bean:write name="savedSearch" property="resultCount"/>)
										</OPTION>
									</logic:iterate>
								</logic:present>
							</SELECT>
						</logic:notEqual>
						<logic:equal name="queryCount" value="1">
							<logic:iterate name="savedQueries" id="savedSearch">
								<DIV class="text" style="font-size:0.9em;"><bean:write name="savedSearch" property="name"/>&nbsp;(<bean:write name="savedSearch" property="resultCount"/>)</DIV>
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
				<logic:notEmpty name="failedServices">
					<TD nowrap>
						&nbsp;&nbsp;<A class="link" href="#this" onclick="document.getElementById('grayBG').style.display='block';document.getElementById('popup').style.display='block';">Failed Hosting Institutions</A>
						<SPAN class="text">(<bean:write name="failedServicesCount"/>)</SPAN>
					</TD>
				</logic:notEmpty>
			</TR>
		</TABLE>
	</DIV>		
</DIV>
<DIV id="grayBG"></DIV>
<DIV id="popup" class="text">
	<DIV style="text-align:right">
		<A class="link" href="#this" onclick="document.getElementById('grayBG').style.display='none';document.getElementById('popup').style.display='none'">close</A>
	</DIV>
	<DIV style="font-weight:bold;margin-bottom:0.4em;text-align:center;">Following Hosting Institutions could not be queried</DIV>
	<DIV style="height:65%;padding-bottom:2em;overflow:auto;">
		<logic:present name="failedServices">
			<logic:iterate name="failedServices" id="failedServices" indexId="indexId" type="edu.wustl.cab2b.common.user.ServiceURLInterface">
				<DIV class="<%=indexId%2==0?"lightgrey":"darkgrey"%>" style="margin:0.1em;">
					<DIV><bean:write name="failedServices" property="hostingCenter"/></DIV>
					<DIV><bean:write name="failedServices" property="urlLocation"/></DIV>
				</DIV>
			</logic:iterate>
		</logic:present>
		<BR style="line-height:0.4em"/>
	</DIV>
</DIV>
<DIV id="centerpanel">
	<DIV id='top'></DIV>
	<DIV id="centerpanelcontent">
		<%@ include file="searchresultspanel.jsp" %>
	</DIV>
	<DIV id='bottom'></DIV>
</DIV>
<DIV id="bottompanel">
	<INPUT type="button" class="button" value="<bean:message key="button.home"/>" onClick="document.location='Home.do'">
</DIV>
<jsp:include page="footer.jsp"/>
<SCRIPT language="javaScript">
 function updateView()
{
  var spanObjs = document.getElementById("centerpanelcontent").getElementsByTagName('SPAN');
  var divObjs = document.getElementById("centerpanelcontent").getElementsByTagName('DIV');
  if(!document.getElementById('searchresultstable'))
  setTimeout("updateView()", 100);
   else
  {
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
    clearTimeout("updateView()");
	document.getElementById('centerpanel').style.visibility = 'visible';
	document.body.style.overflow='auto';
  }
   if(navigator.appName.indexOf('Netscape')==-1)
  {
	document.getElementById('centerpanelcontent').style.height = 250;
	document.getElementById('centerpanelcontent').style.overflow = 'auto';
	document.getElementById('searchresultstable').getElementsByTagName('thead')[0].getElementsByTagName('tr')[0].id = 'noscroll';
	document.getElementById('searchresultstable').getElementsByTagName('thead')[0].getElementsByTagName('tr')[0].style.backgroundColor = '#ccc';
  }
}
</SCRIPT>
</BODY>
</HTML>