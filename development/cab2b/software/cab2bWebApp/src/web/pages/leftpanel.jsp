<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<DIV id="leftpanel">
	<DIV id="quicklinkspanel">
		<DIV class="titlebar">
			<DIV class="titlebarheader title">
				<DIV class="titlebarleftcurve">
					<DIV class="titlebarrightcurve">
						<bean:message key="title.quicklinks"/>
					</DIV>
				</DIV>
			</DIV>
		</DIV>
		<DIV id="quicklinkspanelbody">
			<DIV>
				<IMG alt="<bean:message key="img.alt.bullet"/>" src="images/arrow.jpg" class="bullet"><A href="https://cabig.nci.nih.gov/" class="link" target="_blank"><bean:message key="link.home.cabig"/></A><BR/>
				<IMG alt="<bean:message key="img.alt.bullet"/>" src="images/arrow.jpg" class="bullet"><A href="http://ncicb.nci.nih.gov/" class="link" target="_blank"><bean:message key="link.home.ncicb"/></A><BR/>
				<IMG alt="<bean:message key="img.alt.bullet"/>" src="images/arrow.jpg" class="bullet"><A href="http://en.wikipedia.org/wiki/CaGrid" class="link" target="_blank"><bean:message key="link.wiki.cagrid"/></A><BR/>
				<IMG alt="<bean:message key="img.alt.bullet"/>" src="images/arrow.jpg" class="bullet"><A href="http://cab2b.wustl.edu/" class="link" target="_blank"><bean:message key="link.webpage.cab2b"/></A><BR/>
				<IMG alt="<bean:message key="img.alt.bullet"/>" src="images/arrow.jpg" class="bullet"><A href="http://cab2b.wustl.edu/cab2badmin/" class="link" target="_blank"><bean:message key="link.webpage.cab2badmin"/></A>
			</DIV>
		</DIV>
		<DIV id="certifiedbrowserspanel">
			<DIV class="title"><bean:message key="title.certifiedbrowsers"/></DIV>
			<DIV class="text"><IMG alt="<bean:message key="img.alt.browser.ie"/>" hspace="3" src="images/logo_ie.gif" align="absMiddle" vspace="3"><bean:message key="text.browser.ie"/></DIV>
			<DIV class="text"><IMG alt="<bean:message key="img.alt.browser.mozilla"/>" hspace="3" src="images/logo_firefox.gif" align="absMiddle" vspace="3"><bean:message key="text.browser.mozilla"/></DIV>
			<DIV class="text"><IMG alt="<bean:message key="img.alt.browser.safari"/>" hspace="3" src="images/logo_safari.gif" align="absMiddle" vspace="3"><bean:message key="text.browser.safari"/></DIV>
		</DIV>
		<DIV id="optimalresolutionspanel">
			<DIV class="title""><bean:message key="title.optimalresolutions"/></DIV>
			<DIV class="text"><IMG alt="<bean:message key="img.alt.os.resolution.windows"/>" hspace=3 src="images/logo_windows.gif" align="absMiddle" vspace="3"><bean:message key="text.os.resolution.windows"/></DIV>
			<DIV class="text"><IMG alt="<bean:message key="img.alt.os.resolution.mac"/>" hspace="3" src="images/logo_mac.gif" align="absMiddle" vspace="3"><bean:message key="text.os.resolution.mac"/></DIV>
		</DIV>
	</DIV>
</DIV>