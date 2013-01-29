<%--L
  Copyright Georgetown University, Washington University.

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
				<IMG alt="<bean:message key="img.alt.bullet"/>" src="images/arrow.jpg" class="bullet"><A href="webpage" class="link" target="_blank"><bean:message key="link.webpage.cab2b"/></A><BR/>
				<IMG alt="<bean:message key="img.alt.bullet"/>" src="images/arrow.jpg" class="bullet"><A href="/cab2b" CLASS="link">caB2B Web Client</a><BR/>
				<IMG alt="<bean:message key="img.alt.bullet"/>" src="images/arrow.jpg" class="bullet"><A href="/cab2b/webpage/jnlp/cab2b_client_webstart.jnlp" CLASS="link">caB2B Client Application</A><BR/>
				<IMG alt="<bean:message key="img.alt.bullet"/>" src="images/arrow.jpg" class="bullet"><A href="admin/" class="link" target="_blank"><bean:message key="link.webpage.cab2badmin"/></A>
			</DIV>
		</DIV>
	</DIV>
</DIV>
