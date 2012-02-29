<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:set var="dataTypeList" value="${controlsForm.dataTypeList}"/>
<jsp:useBean id="dataTypeList" type="java.util.List"/>

<c:set var="selectedDataType" value="${controlsForm.dataType}"/>
<jsp:useBean id="selectedDataType" type="java.lang.String"/>
<input type="hidden" id = 'initialDataType' name="initialDataType" value="<%=selectedDataType%>"/>
<table summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
	<tr>
    	<td>
			<table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%' border='0'>
				<tr valign="top">
					<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">
						<bean:message key="eav.att.TextFieldWidth"/> :
					</td>
					<td class="formFieldWithoutBorder">
						<html:text styleClass="formFieldSized5" maxlength="3" size="60" styleId='attributenoOfCols' property="attributenoOfCols" />
					</td>
				</tr>
				<tr valign="top" >
					<td class="formRequiredNoticeWithoutBorder" width="2%">*</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">
						<bean:message key="eav.att.DataInput"/> :
					</td>
					<td class="formFieldWithoutBorder">
						<c:forEach items="${dataTypeList}" var="dataTypeObj">
						<jsp:useBean id="dataTypeObj" type="edu.wustl.common.beans.NameValueBean" />
							<c:set var="dataTypeValue" value="${dataTypeObj.value}" />
							<jsp:useBean id="dataTypeValue" type="java.lang.String" />
							<html:radio styleId='dataType' property="dataType" value="<%=dataTypeValue%>" onclick="dataFldDataTypeChanged(this)" >
								<c:out value="${dataTypeObj.name}"/>
							</html:radio>
						</c:forEach>
					</td>
				</tr>
			 </table>
		</td>
 	</tr>
 	<tr valign="top">
		<td valign="top">
		<jsp:include page="/pages/toolTypeHTML/Datatypes.jsp" />
		</td>
	</tr>
	<tr valign="top">
		<td>
			<table summary="" cellpadding="3" cellspacing="0"  align = 'center' valign="top" border="0" width="37%">
				<tr valign="top">
					<td class="formFieldWithoutBorder" align="left">
						&nbsp;<html:checkbox styleId='attributeIsPassword' property="attributeIsPassword" value="true">
							<bean:message key="app.att.isPassword" />
						</html:checkbox>
					</td>
					<td class="formRequiredLabelWithoutBorder" width="10%">&nbsp;</td>
				</tr>
			</table>
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
