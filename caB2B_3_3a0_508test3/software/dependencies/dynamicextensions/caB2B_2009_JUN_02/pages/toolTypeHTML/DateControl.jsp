<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants" %>
<%@page import="edu.common.dynamicextensions.util.DynamicExtensionsUtility" %>
<%@page import="java.util.Date" %>

<script>var imgsrc="images/";</script>

<c:set var="dataTypeList" value="${controlsForm.dataTypeList}"/>
<jsp:useBean id="dataTypeList" type="java.util.List"/>

<c:set var="dateValueType" value="${controlsForm.dateValueType}"/>
<jsp:useBean id="dateValueType" type="java.lang.String"/>

<html:hidden styleId = 'dataType' property="dataType" value="<%=ProcessorConstants.DATATYPE_DATE%>"/>

<table  summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
	<tr>
  		<td>
  			<table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%'>
		   		<tr>
					<td class="formRequiredNoticeWithoutBorder" width="2%">*</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">
						<bean:message key="eav.att.Format"/> :
					</td>
					<td class="formFieldWithoutBorder">
						<html:radio styleId = 'format' property="format" value="<%=ProcessorConstants.DATE_FORMAT_OPTION_DATEONLY%>" onclick="setDateTimeControl('false', value)">
							<bean:message key="eav.att.DateFormatDateOnlyTitle"/>
						</html:radio>

						<html:radio styleId = 'format' property="format"  value="<%=ProcessorConstants.DATE_FORMAT_OPTION_DATEANDTIME%>" onclick="setDateTimeControl('true', value)">
							<bean:message key="eav.att.DateFormatDateAndTimeTitle"/>
						</html:radio>
						
						<html:radio styleId = 'format' property="format" value="MonthAndYear" onclick="setDateTimeControl('false', value)">
							Date (Month & Year)
						</html:radio>

						<html:radio styleId = 'format' property="format" value="YearOnly" onclick="setDateTimeControl('false', value)">
							Date (Year only)
						</html:radio>
					</td>
				</tr>
				<tr>
					<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">
						<bean:message key="eav.att.DefaultValue"/> :
					</td>
					<td class="formFieldWithoutBorder">
						<input type="hidden" id = 'initialDateValueType' name="initialDateValueType" value="<%=dateValueType%>">
						<html:radio styleId= 'dateValueType' property="dateValueType" value="<%=ProcessorConstants.DATE_VALUE_NONE%>" onclick="changeDateType(this)">
							<bean:message key="eav.att.DateValueNone"/>
						</html:radio>
						<html:radio styleId = 'dateValueType' property="dateValueType" value="<%=ProcessorConstants.DATE_VALUE_TODAY%>" onclick="changeDateType(this)">
							<bean:message key="eav.att.DateValueToday"/>
						</html:radio>
						<html:radio styleId = 'dateValueType' property="dateValueType" value="<%=ProcessorConstants.DATE_VALUE_SELECT%>"  onclick="changeDateType(this)">
							<bean:message key="eav.att.DateValueSelect"/>
						</html:radio>
					</td>
				</tr>
				<tr id="rowForDateDefaultValue">
					<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">&nbsp;</td>
					<td>
						<html:text styleId='attributeDefaultValue' property='attributeDefaultValue' styleClass="formFieldVerySmallSized" maxlength="100" size="60" />
						<A onclick="showCalendar('attributeDefaultValue',<%=DynamicExtensionsUtility.getCurrentYear()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentDay()%>,'MM-dd-yyyy','controlsForm','attributeDefaultValue',event,1900,2020);" href="javascript://">
							<IMG alt="This is a Calendar" src="images/calendar.gif" border=0>
						</A>
						<DIV id=slcalcodattributeDefaultValue style="Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px">
							<SCRIPT>printCalendar('attributeDefaultValue', <%=DynamicExtensionsUtility.getCurrentDay()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
						</DIV><label class="formFieldWithoutBorder">[MM-DD-YYYY]&nbsp;</label>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr width="100%">
		<td width="100%" bgcolor="#FFFFFF">
			<hr/>
			<div id="substitutionDivRules"></div>
		</td>
	</tr>
</table>
<style>changeVisibility();</style>

<div id='dateTimeDiv' style="display:none">
	<SCRIPT>printTimeCalendar('attributeDefaultValue',<%=DynamicExtensionsUtility.getCurrentDay()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentYear()%>,<%=DynamicExtensionsUtility.getCurrentHours()%>,<%=DynamicExtensionsUtility.getCurrentMinutes()%>);</SCRIPT>
</div>

<div id='dateTimeMinDiv' style="display:none">
	<SCRIPT>printTimeCalendar('min',<%=DynamicExtensionsUtility.getCurrentDay()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentYear()%>,<%=DynamicExtensionsUtility.getCurrentHours()%>,<%=DynamicExtensionsUtility.getCurrentMinutes()%>);</SCRIPT>
</div>

<div id='dateTimeMaxDiv' style="display:none">
	<SCRIPT>printTimeCalendar('max',<%=DynamicExtensionsUtility.getCurrentDay()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentYear()%>,<%=DynamicExtensionsUtility.getCurrentHours()%>,<%=DynamicExtensionsUtility.getCurrentMinutes()%>);</SCRIPT>
</div>

<div id='DateOnlyMinDiv' style="display:none">
	<SCRIPT>printCalendar('min',<%=DynamicExtensionsUtility.getCurrentDay()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
</div>

<div id='DateOnlyMaxDiv' style="display:none">
	<SCRIPT>printCalendar('max',<%=DynamicExtensionsUtility.getCurrentDay()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
</div>

<div id='DateOnlyDiv' style="display:none">
	<SCRIPT>printCalendar('attributeDefaultValue',<%=DynamicExtensionsUtility.getCurrentDay()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
</div>

<div id='YearOnlyMaxDiv' style="display:none">
	<SCRIPT>printYearCalendar('max',<%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
</div>

<div id='YearOnlyMinDiv' style="display:none">
	<SCRIPT>printYearCalendar('min',<%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
</div>

<div id='YearOnlyDiv' style="display:none">
	<SCRIPT>printYearCalendar('attributeDefaultValue',<%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
</div>


<div id='MonthAndYearDiv' style="display:none">
	<SCRIPT>printMonthYearCalendar('attributeDefaultValue',<%=DynamicExtensionsUtility.getCurrentMonth()%>, <%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
</div>

<div id='MonthAndYearMinDiv' style="display:none">
	<SCRIPT>printMonthYearCalendar('min', <%=DynamicExtensionsUtility.getCurrentMonth()%>, <%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
</div>

<div id='MonthAndYearMaxDiv' style="display:none">
	<SCRIPT>printMonthYearCalendar('max', <%=DynamicExtensionsUtility.getCurrentMonth()%>, <%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
</div>

<jsp:include page="/pages/ValidationRules.jsp" />

