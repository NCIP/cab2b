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

<c:set var="linesType" value="${controlsForm.linesType}"/>
<jsp:useBean id="linesType" type="java.lang.String"/>

<c:set var="measurementUnitsList" value="${controlsForm.measurementUnitsList}"/>
<jsp:useBean id="measurementUnitsList" type="java.util.List"/>

<input type="hidden" id='linesTypeHidden' name="linesTypeHidden" value='<%=linesType%>' />
<div id="TextDataType" style="display:none">
	<table  summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%' >
		<tr valign="top" >
 			<td class="formRequiredNoticeWithoutBorder" width="2%">*</td>
 			<td class="formRequiredLabelWithoutBorder" width="30%">
 				<bean:message key="eav.control.type"/> :
 			</td>
 			<td class="formFieldWithoutBorder">
	 			<html:radio styleId = 'linesTypeSingleLine'  property='linesType' value='<%=ProcessorConstants.LINE_TYPE_SINGLELINE%>' onclick='textBoxTypeChange(this)'>
		 			<bean:message key="eav.att.TextBoxSingleLineTitle"/>
	 			</html:radio>
	 			<html:radio styleId = 'linesTypeMultiline' property='linesType' value='<%=ProcessorConstants.LINE_TYPE_MULTILINE%>' onclick='textBoxTypeChange(this)'>
		 			<bean:message key="eav.att.TextBoxMultiLineTitle"/>
	 			</html:radio>
 			</td>
		</tr>
 		<tr  style="display:none" id="rowForNumberOfLines" >
 			<td class="formRequiredNoticeWithoutBorder" width="2%">
 				&nbsp; 
 		 	</td>

			<td class="formRequiredLabelWithoutBorder" id="noOfLines" width="30%">
				<bean:message key="eav.text.noOfLines"/> :
			</td>
			<td class="formFieldWithoutBorder">
				<html:text styleClass="formFieldSized5" maxlength="3" size="60"  styleId ='attributeNoOfRows' property="attributeNoOfRows" />
			</td>
		</tr>
       	<tr valign="top">
       		<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
			<td class="formRequiredLabelWithoutBorder" width="30%">
				<bean:message key="eav.att.MaxCharacters"/> :
			</td>
			<td class="formFieldWithoutBorder">
				<html:text styleClass="formFieldSized5" maxlength="3" size="60" styleId = 'attributeSize' property="attributeSize" />
			</td>
		</tr>
		<tr valign="top">
			<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
			<td class="formRequiredLabelWithoutBorder" width="30%">
				<bean:message key="eav.att.DefaultValue"/> :
			</td>
			<td class="formFieldWithoutBorder">
				<html:text styleClass="formDateSized" maxlength="100" size="60"  styleId='attributeDefaultValue' property="attributeDefaultValue" />
			</td>
		</tr>
		<tr valign="top">
			<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
			<td class="formRequiredLabelWithoutBorder" width="30%">&nbsp;</td>
			<td class="formFieldWithoutBorder" align="left">
				<html:checkbox  styleId = 'attributeDisplayAsURL' property="attributeDisplayAsURL" value="true">
					<bean:message key="app.att.displayAsURL" />
				</html:checkbox>
			</td>
		</tr>

	</table>
</div>

<div id="NumberDataType" style="display:none">
	<table summary="" cellpadding="3" cellspacing="0"  align = 'center'  width='100%'>
		<tr>
			<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
			<td class="formRequiredLabelWithoutBorder" width="30%">
				<bean:message key="eav.att.AttributeDecimalPlaces"/> :
			</td>
			<td class="formFieldWithoutBorder">
				<html:text styleClass="formFieldSized5" maxlength="100" size="60" styleId='attributeDecimalPlaces' property="attributeDecimalPlaces" />
			</td>
		</tr>
		<tr>
			<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
			<td class="formRequiredLabelWithoutBorder" width="30%">
				<bean:message key="eav.att.DefaultValue"/> :
			</td>
			<td class="formFieldWithoutBorder">
				<html:text styleClass="formFieldSized5" maxlength="100" size="60" styleId='attributeDefaultValue' property="attributeDefaultValue" />
			</td>
		</tr>
		
	</table>
</div>
