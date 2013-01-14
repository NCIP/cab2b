<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<DIV id="footer">
	<DIV class="textfooter" style="padding-bottom:0.3em">
		<bean:message key="application.build" bundle="ServerResources"/>&nbsp;|&nbsp;<bean:message key="text.optimalviewinstruction"/>
	</DIV>
	<DIV style="padding-bottom:0.3em">
		<SPAN class="title">|</SPAN>
		<A class="linkfooter" href="mailto:cab2bsupport@bmi.wustl.edu"><bean:message key="link.contactus"/></A>
		<SPAN class="title">|</SPAN>
		<A class="linkfooter" href="http://www.nih.gov/about/privacy.htm" target="_blank"><bean:message key="link.privacynotice"/></A>
		<SPAN class="title">|</SPAN>
		<A class="linkfooter" href="http://www.nih.gov/about/disclaim.htm" target="_blank"><bean:message key="link.disclaimer"/></A>
		<SPAN class="title">|</SPAN>
		<A class="linkfooter" href="http://www3.cancer.gov/accessibility/nci508.htm" target="_blank"><bean:message key="link.accessibility"/></A>
		<SPAN class="title">|</SPAN>
	</DIV>
	<A href="http://www.cancer.gov/" target="_blank"><IMG alt="<bean:message key="img.alt.nci"/>" hspace="5" src="images/nci.gif" border="0" align="absmiddle"></A>
	<A href="http://www.dhhs.gov/" target="_blank"><IMG alt="<bean:message key="img.alt.dhhs"/>" hspace="5" src="images/hhs.gif" border="0" align="absmiddle"></A>
	<A href="http://www.nih.gov/" target="_blank"><IMG alt="<bean:message key="img.alt.nih"/>" hspace="5" src="images/nih.gif" border="0" align="absmiddle"></A>
	<A href="http://www.usa.gov/" target="_blank"><IMG alt="<bean:message key="img.alt.usagov"/>" hspace="5" src="images/firstgov.gif" border="0" align="absmiddle"></A>
</DIV>