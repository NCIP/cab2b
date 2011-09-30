<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:set var="dataTypeList" value="${controlsForm.dataTypeList}"/>
<jsp:useBean id="dataTypeList" type="java.util.List"/>

<table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%' >
			<tr valign="top">
				<td  class="formRequiredNoticeWithoutBorder" width="2%">*</td>
				<td class="formRequiredLabelWithoutBorder" width="20%">
					<bean:message key="eav.att.Label"/> :
				</td>
				<td class="formFieldWithoutBorder">
					<html:text styleClass="formDateSized" maxlength="100" size="60" styleId='caption' property="caption" />
				</td>
			</tr>
			<tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
				<td class="formRequiredLabelWithoutBorder" width="20%">
					<bean:message key="eav.form.conceptCode"/> :
				</td>
				<td class="formFieldWithoutBorder">
					<html:text styleClass="formDateSized" maxlength="100" size="60" styleId='attributeConceptCode' property="attributeConceptCode"/>
				</td>
			</tr>
			<tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
				<td class="formRequiredLabelWithoutBorder" width="20%">
					<bean:message key="eav.att.Description"/> :
				</td>
				<td>
					<html:textarea styleClass="formFieldSmallSized" rows = "2" cols="28"  styleId ='description' property="description"/>
				</td>
			</tr>
			<tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
				<td class="formRequiredLabelWithoutBorder" width="20%">&nbsp;</td>
				<td class="formFieldWithoutBorder" align="left">
					<html:checkbox styleId='attributeIdentified' property="attributeIdentified" value="true">
						<bean:message key="app.att.isIdentified"/>
					</html:checkbox>
				</td>
			</tr>
	</table>