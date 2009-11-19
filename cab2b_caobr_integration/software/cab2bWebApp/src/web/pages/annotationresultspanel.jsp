<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ page import="java.util.* "%>

<% int j = 0; 
List<edu.wustl.cab2bwebapp.dvo.AnnotationElementDVO> dvoElement = null;
List<edu.wustl.cab2bwebapp.dvo.AnnotationDVO> dvoList=(List<edu.wustl.cab2bwebapp.dvo.AnnotationDVO>)session.getAttribute("displayannotation");
out.println("size "+dvoList.size());
%>
<logic:present name="displayannotation">
	<display:table class="simple" name="${sessionScope.displayannotation}" cellspacing="1" cellpadding="4" uid="row" id="row" htmlId="annotationTable">
		<display:column title="Resource">
			<img src='${row.resourceLogoURl}' title='${row.resourceDescription}'/><br>
            <span style="font-weight:bold">${row.resourceName}</span>
		</display:column>
		<display:column title="Annotation Found">
		<% 
		  for(int i=0;i<dvoList.get(j).getList().size();i++)
			 {
			 dvoElement=dvoList.get(j).getList();
			
			if(i<=4){
			%>
				  <a class='link' href='<%=dvoElement.get(i).getResourceURL()%>' target='_blank' title='<%=dvoElement.get(i).getFullDescription()%>'><%=dvoElement.get(i).getElementId()%>
				  </a>: <%=dvoElement.get(i).getDescription()%><br>
			<%  
			}
			else{
				%>
				   <a class='link' href='javascript:void(0)' onClick='showWindow("<%=dvoList.get(j).getResourceId()%>")'><span style="float:right;margin-right: 2em;">
				   View All( Annotation:<%=dvoElement.size()%>) </span></a>";
			<%
			break;
			}
						
			}
			
			if(dvoList.get(j).getList().size()==0){
			%>
			<span style='color: blue;'>No Annotation Found </span>
			<%
			}
		 j++;
	    %>
		</display:column>
	</display:table>
</logic:present>