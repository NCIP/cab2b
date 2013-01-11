<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants"%>

<c:set var="displayChoice" value="${controlsForm.displayChoice}"/>
<jsp:useBean id="displayChoice" type="java.lang.String"/>

<input id='hiddenDisplayChoice' type="hidden" name="hiddenDisplayChoice" value="<%=displayChoice%>">

<table summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
	<tr>
    	<td>
			<table summary="" cellpadding="3" cellspacing="0" align='center' width='100%' >
				<tr valign="top">
					<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">
						<bean:message key="eav.att.RadioButtonOptionTypes"></bean:message> :
					</td>
					<td class="formFieldWithoutBorder">
						<html:radio styleId='<%=ProcessorConstants.DISPLAY_CHOICE_USER_DEFINED%>' property="displayChoice" value="<%=ProcessorConstants.DISPLAY_CHOICE_USER_DEFINED%>" onclick="">
							<bean:message key="eav.att.OptionsUserDefined"/>
						</html:radio>
						<html:radio styleId='<%=ProcessorConstants.DISPLAY_CHOICE_CDE%>' property="displayChoice" value="<%=ProcessorConstants.DISPLAY_CHOICE_CDE%>" disabled="true" >
							<bean:message key="eav.att.OptionsCDE"/>
						</html:radio>
					</td>
				</tr>
	 		</table>
		</td>
 	</tr>
	<tr>
		<td valign="top">
			<div id="optionValuesSpecificationDiv"></div>
		</td>
 	</tr>
 	<tr width="100%">
		<td width="100%">
			<hr/>
			<div id="substitutionDivRules"></div>
		</td>
	</tr>
</table>
<html:hidden styleId='dataType' property="dataType" value="<%=ProcessorConstants.DATATYPE_STRING%>"/>
<jsp:include page="/pages/toolTypeHTML/ListValuesSpecification.jsp"/>
<jsp:include page="/pages/ValidationRules.jsp" />
