<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<logic:notEmpty name="queryStatusDVOList">
	<display:table class="simple" cellspacing="1" cellpadding="4" name="${requestScope.queryStatusDVOList}" uid="query" requestURI="" defaultsort="4"  defaultorder="descending" htmlId="dashboardtable">
		<display:column sortable="false" headerClass="unsortable">
			<logic:equal name="query" property="status" value="Processing">
				<IMG src="images/task_progress.gif" title="<bean:message key="img.alt.task.inprogress"/>">
			</logic:equal>
			<logic:equal name="query" property="status" value="Complete">
				<IMG src="images/task_success.gif" title="<bean:message key="img.alt.task.complete"/>">
			</logic:equal>
			<logic:equal name="query" property="status" value="Failed">
				<IMG src="images/task_failure.gif" title="<bean:message key="img.alt.task.failed"/>">
			</logic:equal>
			<logic:equal name="query" property="status" value="Suspended">
				<IMG src="images/task_suspended.gif" title="Suspended">
			</logic:equal>
		</display:column>
		<display:column title="Title" sortable="true" headerClass="sortable">
			<logic:equal name="query" property="type" value="Saved Search">
				<IMG src="images/form_search.jpg" title="Saved Search">
			</logic:equal>
			<logic:equal name="query" property="type" value="Keyword">
				<IMG src="images/keyword_search.jpg" title="Keyword Search">
			</logic:equal>
			${query.title}
		</display:column>
		<display:column title="Result Count" value="${query.resultCount}" sortable="true" headerClass="sortable"/>
		<display:column title="Executed On" value="${query.executedOn}" sortable="true" headerClass="sortable" format="{0,date,yyyy-MM-dd HH:mm:ss}"/>	
		<display:column title="Action(s)" sortable="false" headerClass="unsortable">
			<IMG class="tooltipinvoker" src="images/form.jpg" style="cursor:pointer" title="Query Parameters" onmouseover="clearTimeout(t);tooltipinvoker();toolTipId='${query.conditions}'" onmouseout="updateView();">&nbsp;
			<DIV class="tooltip" id="${query.conditions}">
				<display:table class="simple" cellspacing="1" cellpadding="4" name="${query.conditions}" uid="queryCondition" requestURI="">
					<display:column title="Parameter" value="${queryCondition.parameter}"/>
					<display:column title="Condition" value="${queryCondition.condition}"/>
					<display:column title="Value" value="${queryCondition.value}"/>
				</display:table>
			</DIV>
			<IMG class="tooltipinvoker" src="images/service_instance.jpg" style="cursor:pointer;" title="Hosting Institutions" onmouseover="clearTimeout(t);tooltipinvoker();toolTipId='${query.serviceInstances}'" onmouseout="updateView();">&nbsp;
			<DIV class="tooltip" id="${query.serviceInstances}">
				<display:table class="simple" cellspacing="1" cellpadding="4" name="${query.serviceInstances}" uid="serviceInstance" requestURI="">
					<display:column>
						<logic:equal name="serviceInstance" property="status" value="Processing">
							<IMG src="images/task_progress.gif" title="<bean:message key="img.alt.task.inprogress"/>">
						</logic:equal>
						<logic:equal name="serviceInstance" property="status" value="Complete">
							<IMG src="images/task_success.gif" title="<bean:message key="img.alt.task.complete"/>">
						</logic:equal>
						<logic:equal name="serviceInstance" property="status" value="Complete With Error">
							<IMG src="images/task_failure.gif" title="<bean:message key="img.alt.task.failed"/>">
						</logic:equal>
						<logic:equal name="serviceInstance" property="status" value="Suspended">
							<IMG src="images/task_suspended.gif" title="Suspended">
						</logic:equal>
					</display:column>
					<display:column title="Hosting Institution Name" value="${serviceInstance.name}"/>					
					<display:column title="Result Count" value="${serviceInstance.resultCount}"/>
				</display:table>
			</DIV>
			<logic:notEqual name="query" property="status" value="Complete">
				<IMG src="images/stop.gif" style="cursor:pointer;display:none;" title="<bean:message key="img.alt.abortexecution"/>">&nbsp;
			</logic:notEqual>
			<logic:equal name="query" property="status" value="Complete">
				<IMG src="images/ico_file_excel.png" style="cursor:pointer;" title="<bean:message key="img.alt.exportresults"/>" onClick="document.location='ExportResults.do?fileName=<bean:write name="query" property="fileName"/>';TogglePreloader(0);">&nbsp;
			</logic:equal>
		</display:column>
	</display:table>
</logic:notEmpty>
<logic:empty name="queryStatusDVOList">
	<DIV class="text" style="text-align:center;"><bean:message key="text.offlinequeriesempty"/></DIV>
</logic:empty>
<DIV id="scroller"></DIV>