<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@page import="edu.common.dynamicextensions.ui.webui.util.OptionValueObject"%>
<%@page import="java.util.List"%>

<c:set var="groupNamesList" value="${controlsForm.groupNames}"/>
<jsp:useBean id="groupNamesList" type="java.util.List"/>

<c:set var="selectedAttributes" value="${controlsForm.selectedAttributes}"/>
<jsp:useBean id="selectedAttributes" type="java.util.List"/>

<c:set var="separatorList" value="${controlsForm.separatorList}"/>
<jsp:useBean id="separatorList" type="java.util.List"/>

<c:set var="optionDetailList" value="${controlsForm.optionDetails}"/>
<jsp:useBean id="optionDetailList" type="java.util.List"/>

<script>
	function initOptionGrid()
	{
		optionGrid = new dhtmlXGridObject('optiongrid');
		optionGrid.setImagePath("dhtml_comp/imgs/");
		optionGrid.setHeader("#,Option Name,Concept Code(s),Definiton");
		optionGrid.setInitWidthsP("5,35,30,30");
		optionGrid.setColAlign("center,left,left,left");
		optionGrid.setColTypes("ch,ed,ed,ed");
		optionGrid.setSerializableColumns("true,true,true,true");
		optionGrid.enableMultiselect(true);
		optionGrid.init();
		
		var csvStringObj = document.getElementById('csvString');
		if(csvStringObj!=null && csvStringObj.value!="")
		{
			reloadOptionGrid(csvStringObj);
		}
		else
		{
			loadOptionGridData();
		}
	}
	
	function reloadOptionGrid(csvStringObj)
	{
		var csvString = csvStringObj.value;
		optionGrid.setCSVDelimiter("\t");
		optionGrid.loadCSVString(csvString);
		optionGrid.setCSVDelimiter(",");
		
		var attributeDefaultValue = document.getElementById('attributeDefaultValue').value;
		var itemIds = optionGrid.getAllItemIds(",");
		if(itemIds != null)
		{
			var rowIds = new Array();
			rowIds = itemIds.split(',');
			var item=0;
			for(item=0; item<rowIds.length; item++)
			{
				rowValue = optionGrid.cells(rowIds[item],1).getValue();
				if(rowValue == attributeDefaultValue)
				{
					optionGrid.setRowTextBold(rowIds[item]);
				}
				else
				{
					optionGrid.setRowTextNormal(rowIds[item]);
				}
			}
		}
	}
	
	function loadOptionGridData()
	{
		var attributeDefaultValue = document.getElementById('attributeDefaultValue').value;
		var rowId = "";
		<%
			if(optionDetailList!=null && optionDetailList.size() > 0)
			{
				int noOfOptions = optionDetailList.size();
				for(int i = 0; i < noOfOptions; i++)
				{
					OptionValueObject optionValueObject = (OptionValueObject)optionDetailList.get(i);
					if(optionValueObject != null)
					{
						String optionName = optionValueObject.getOptionName();
						String optionConceptCode = optionValueObject.getOptionConceptCode();
						String optionDescription = optionValueObject.getOptionDescription();
					
						String gridContentStr = "," + optionName + "," + optionConceptCode + "," + optionDescription;
						%>
							rowId = <%=i%> + "";
							optionGrid.addRow(rowId,"<%=gridContentStr%>");
							if(attributeDefaultValue == "<%=optionName%>")
							{
								optionGrid.setRowTextBold(rowId);
							}
						<%
					}
				}
			}
		%>
	}

    function uploadValues()
	{
		var fileName = document.getElementById('csvFile').value;
		if(fileName != null || fileName != "")
		{
			var controlsForm = document.getElementById('controlsForm');
			var rowNos = optionGrid.getRowsNum();
	
			var div = document.createElement('DIV');
			div.id = 'tempDiv';
	        div.innerHTML = '<iframe style="display:none" src="about:blank" id="tempIframe" name="tempIframe" onload="updateOptionGrid()"></iframe>';
	        document.body.appendChild(div);
	
			controlsForm.action = "UploadFileAction.do?totalRows=" + rowNos;
			controlsForm.target = "tempIframe";
			controlsForm.submit();
		}
	}
	
	function updateOptionGrid()
	{
		var iframe = document.getElementById('tempIframe');
		var returnedString = getContent(iframe);
		if(returnedString != null && returnedString != "")
		{
			var loadCsvString = "";
			var temp = new Array();
			temp = returnedString.split('|');
			for(i=0; i<temp.length-1;i++)
			{
				loadCsvString += temp[i] + "\r\n";
			}
			loadCsvString = trim(loadCsvString);
			optionGrid.loadCSVString(loadCsvString);
		}
		var div = document.getElementById('tempDiv');
		document.body.removeChild(div);
	}

	function getContent(iframe)
	{
		var xmlContent = '';
		if (iframe.contentDocument)
		{
			xmlContent = iframe.contentDocument.body.innerHTML; 
		}
		else if (iframe.contentWindow)
		{
			xmlContent = iframe.contentWindow.document.body.innerHTML;
		}
		else if (iframe.document)
		{
		  xmlContent = iframe.document.body.innerHTML;
		}
		return xmlContent;
	}
</script>

<!--User defined values specification-->
<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants"%>
<div id="UserDefinedValues" style="display:none" width="100%">
	<hr/>
	<table id="optionListTable" width="100%">
		<tr>
			<td width="100%">
				<html:file styleId="tempcsvFile" property="tempcsvFile"/>&nbsp;
				<html:button styleClass="formButton" property="uploadFile" onclick="uploadValues()">
					<bean:message key="buttons.upload"/>
				</html:button>
			</td>
		</tr>
		<tr id="optionsListRow">
			<td width="100%">
				<div id="tempoptiongrid" width="100%" height="250px" style="overflow:hidden"></div>
			</td>
		</tr>
		<tr>
			<td width="100%">
				<html:button styleClass="formButton" property="addPermisibleValue" onclick="addOptionRow()">
					<bean:message key="buttons.add.permissible.value"/>
				</html:button>&nbsp;
				<html:button styleClass="formButton" property="deletePermissibleValue" onclick="deleteSelectedOptions()">
					<bean:message key="buttons.delete"/>
				</html:button>&nbsp;
				<html:button styleClass="formButton" property="makeDefault" onclick="setDefaultValue()">
					<bean:message key="buttons.makeDefault"/>
				</html:button>&nbsp;
			</td>
		</tr>
	</table>
	<html:hidden styleId='attributeDefaultValue' property='attributeDefaultValue' />
	<html:hidden styleId='csvString' property='csvString' />
</div>

<!--CDE Spcfn-->
<div id="CDEValues" style="display:none">
	<hr/>
	<table summary="" valign="top" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
		<tr>
			<td class="formRequiredNoticeWithoutBorder" width="2%">*</td>
			<td class="formRequiredLabelWithoutBorder" width="30%">
				<bean:message  key="eav.att.CDEPublicDomainId" /> :
			</td>
			<td class="formFieldWithoutBorder">
				<html:text styleId="publicDomainId"  property="publicDomainId" styleClass="formDateSized" > </html:text>
			</td>
		</tr>
	</table>
</div>

<!--Lookup specification-->
<div id="LookupValues" style="display:none">
	<hr/>
	<table summary="" valign="top" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
		<tr valign="top">
			<td class="formRequiredNoticeWithoutBorder" width="2%">*</td>
			<td class="formRequiredLabelWithoutBorder" width="30%">
				<bean:message  key="eav.att.LookupFormTypeSelection" /> :
			</td>
			<td class="formFieldWithoutBorder">
				<html:radio styleId="formTypeForLookup"  property="formTypeForLookup" value="<%=ProcessorConstants.LOOKUP_USER_FORMS %>" >
					<bean:message  key="eav.att.LookupUserForms" />
				</html:radio>
				<html:radio styleId="formTypeForLookup" property="formTypeForLookup" value="<%=ProcessorConstants.LOOKUP_SYSTEM_FORMS %>" disabled="true">
					<bean:message  key="eav.att.LookupSytsemForms" />
				</html:radio>
			</td>
		</tr>

		<tr valign="top">
			<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
			<td class="formRequiredLabelWithoutBorder" width="30%">
				<bean:message  key="eav.att.Group" /> :
			</td>
			<td>
				<html:select styleClass="formFieldVerySmallSized" property="groupName" styleId="groupName" onchange="groupChanged(true)">
					<html:options collection="groupNamesList" labelProperty="name" property="value" />

				</html:select>
			</td>
		</tr>
		<tr valign="top">
			<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
			<td class="formRequiredLabelWithoutBorder" width="30%">
				<bean:message  key="eav.att.Form" /> :
			</td>
			<td  >
				<html:select styleClass="formFieldVerySmallSized" property="formName" styleId="formName" onchange="formChanged(true)">

				</html:select>
			</td>
		</tr>
		<tr valign="top">
			<td colspan="3">
				<table valign="top" align = 'center' width='100%' >
					<thead>
						<tr>
							<th width="45%" class="formRequiredLabelWithoutBorder">
								Available Attributes
							</th>
							<th width="10%" valign="middle" align="center" class="formMessage" >
								&nbsp;
							</th>
							<th width="45%" class="formRequiredLabelWithoutBorder">
								Selected Attributes
							</th>
						</tr>
					</thead>
					<tr>
						<td width="45%" align="center">
							<select class="formFieldVerySmallSized" multiple size="3" id="formAttributeList" name="formAttributeList">
							</select>
						</td>
						<td width="10%" align="center">
							<input type="button" class="subFormGroupButton" name="addFormAttribute" value="Add" onclick="selectFormAttribute()" />
							<input type="button" class="subFormGroupButton" name="removeFormAttribute" value="Remove" onclick="unSelectFormAttribute()" />
						</td>
						<td width="45%" align="center">
							<html:select multiple="true" size="3" property="selectedAttributeIds" styleId="selectedAttributeIds" styleClass="formFieldVerySmallSized">
								<html:options collection="selectedAttributes" labelProperty="name" property="value" />
							</html:select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr valign="top">
			<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
			<td class="formRequiredLabelWithoutBorder" width="30%">Separate with : </td>
			<td>
				<html:select styleClass="formFieldVerySmallSized" property="separator" styleId="separator" >
					<html:options collection="separatorList" labelProperty="name" property="value" />
				</html:select>
			</td>
		</tr>
	</table>
</div>
