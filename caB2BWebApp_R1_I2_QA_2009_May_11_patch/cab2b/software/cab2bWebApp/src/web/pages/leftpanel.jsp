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
				<IMG alt="Bullet" src="images/arrow.jpg" class="bullet"><A href="https://cabig.nci.nih.gov/" class="link" target="_blank"><bean:message key="link.home.cabig"/></A><BR/>
				<IMG alt="Bullet" src="images/arrow.jpg" class="bullet"><A href="http://ncicb.nci.nih.gov/" class="link" target="_blank"><bean:message key="link.home.ncicb"/></A><BR/>
				<IMG alt="Bullet" src="images/arrow.jpg" class="bullet"><A href="http://en.wikipedia.org/wiki/CaGrid" class="link" target="_blank"><bean:message key="link.wiki.cagrid"/></A><BR/>
				<IMG alt="Bullet" src="images/arrow.jpg" class="bullet"><A href="http://cab2b.wustl.edu/" class="link" target="_blank"><bean:message key="link.webpage.cab2b"/></A>
			</DIV>
		</DIV>
		<DIV id="certifiedbrowserspanel">
			<DIV class="title"><bean:message key="browsers.certified"/></DIV>
			<DIV class="text"><IMG alt="<bean:message key="browser.ie"/>" hspace="3" src="images/logo_ie.gif" align="absMiddle" vspace="3"><bean:message key="browser.ie"/></DIV>
			<DIV class="text"><IMG alt="<bean:message key="browser.mozilla"/>" hspace="3" src="images/logo_firefox.gif" align="absMiddle" vspace="3"><bean:message key="browser.mozilla"/></DIV>
			<DIV class="text"><IMG alt="<bean:message key="browser.safari"/>" hspace="3" src="images/logo_safari.gif" align="absMiddle" vspace="3"><bean:message key="browser.safari"/></DIV>
		</DIV>
		<DIV id="optimalresolutionspanel">
			<DIV class="title""><bean:message key="resolutions.optimal"/></DIV>
			<DIV class="text"><IMG alt="<bean:message key="os.resolution.windows"/>" hspace=3 src="images/logo_windows.gif" align="absMiddle" vspace="3"><bean:message key="os.resolution.windows"/></DIV>
			<DIV class="text"><IMG alt="<bean:message key="os.resolution.mac"/>" hspace="3" src="images/logo_mac.gif" align="absMiddle" vspace="3"><bean:message key="os.resolution.mac"/></DIV>
		</DIV>
	</DIV>
</DIV>