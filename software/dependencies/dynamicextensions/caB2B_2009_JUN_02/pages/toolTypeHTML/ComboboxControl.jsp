<%--L
  Copyright Georgetown University, Washington University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants"%>
<%@page import="edu.wustl.common.beans.NameValueBean"%>

<c:set var="dataTypeList" value="${controlsForm.dataTypeList}"/>
<jsp:useBean id="dataTypeList" type="java.util.List"/>

<c:set var="listType" value="${controlsForm.attributeMultiSelect}"/>
<jsp:useBean id="listType" type="java.lang.String"/>

<c:set var="displayChoice" value="${controlsForm.displayChoice}"/>
<jsp:useBean id="displayChoice" type="java.lang.String"/>

<input id = 'hiddenIsMultiSelect' type="hidden" name="hiddenIsMultiSelect" value="<%=listType%>">
<input id = 'hiddenDisplayChoice' type="hidden" name="hiddenDisplayChoice" value="<%=displayChoice%>">
<table summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
	<tr>
    	<td>
			<table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%' >
				<tr>
		 			<td class="formRequiredNoticeWithoutBorder" width="2%">*</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">
						<bean:message key="eav.att.ListBoxType"/> :
					</td>
					<td class="formFieldWithoutBorder">
						<html:radio  property="attributeMultiSelect" value="<%=ProcessorConstants.LIST_TYPE_SINGLE_SELECT%>" onclick="listTypeChanged(this)" >
							<bean:message key="eav.att.ListBoxSingleTitle"/>
						</html:radio>
						<html:radio property="attributeMultiSelect" value="<%=ProcessorConstants.LIST_TYPE_MULTI_SELECT%>" onclick="listTypeChanged(this)" >
							<bean:message key="eav.att.ListBoxMultiLineTitle"/>
						</html:radio>
					</td>
				</tr>
				<tr id="rowForDisplayHeight">
					<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">
						<bean:message key="eav.att.ListBoxDisplayLines"/> :
					</td>
					<td class="formFieldWithoutBorder">
						<html:text styleClass="formFieldSized5" maxlength="100" size="60" styleId = 'attributeNoOfRows'  property="attributeNoOfRows" />
					</td>
				</tr>
				<tr>
					<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">
						<bean:message key="eav.att.ListBoxOptionTypes"></bean:message> :
					</td>
					<td class="formFieldWithoutBorder">
						<html:radio styleId="<%=ProcessorConstants.DISPLAY_CHOICE_USER_DEFINED%>" property="displayChoice" onclick="changeSourceForValues(this)"  value="<%=ProcessorConstants.DISPLAY_CHOICE_USER_DEFINED%>" >
							<bean:message key="eav.att.OptionsUserDefined"/>
						</html:radio>
						<html:radio styleId="<%=ProcessorConstants.DISPLAY_CHOICE_CDE%>"  property="displayChoice" onclick="changeSourceForValues(this)"  value="<%=ProcessorConstants.DISPLAY_CHOICE_CDE%>" disabled="true">
							<bean:message key="eav.att.OptionsCDE"/>
						</html:radio>
						<html:radio styleId="<%=ProcessorConstants.DISPLAY_CHOICE_LOOKUP%>" property="displayChoice" onclick="changeSourceForValues(this)"  value="<%=ProcessorConstants.DISPLAY_CHOICE_LOOKUP%>" >
							<bean:message key="eav.att.OptionsLookup"/>
						</html:radio>
					</td>
				</tr>
			 </table>
		</td>
	</tr>
	<tr>
 		<td valign="top">
 			<div id="optionValuesSpecificationDiv" />
	 	</td>
	 	<td>
	 		<html:hidden styleId= 'dataType' property="dataType" value ="<%=ProcessorConstants.DATATYPE_STRING%>"/>
	 	</td>
	</tr>
	<tr width="100%">
		<td width="100%">
			<hr/>
			<div id="substitutionDivRules"></div>
		</td>
	</tr>
</table>
<jsp:include page="/pages/ValidationRules.jsp" />
<jsp:include page="/pages/toolTypeHTML/ListValuesSpecification.jsp" />
