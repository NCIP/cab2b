<%--L
  Copyright Georgetown University, Washington University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
  <pg:prev>
<%  String url = request.getRequestURL().toString() + pageUrl.substring(pageUrl.indexOf("?"));
%>            		
            		  <a href="<%= url %>"><nobr>[< Prev ]</nobr></a> </pg:prev>
                      <pg:pages>
<%					
				
						 if(pageNumber==currentPageNumber)
							{ %>
								   <b><%= pageNumber %>&nbsp;|</b>
						<%  }  
							else
							{String url = request.getRequestURL().toString() + pageUrl.substring(pageUrl.indexOf("?"));
								
								%>
							   <a href="<%=url %>" > <%= pageNumber %>&nbsp;|</a> 
						  <%}%>
 					</pg:pages>
					<pg:next>
<%  String url = request.getRequestURL().toString() + pageUrl.substring(pageUrl.indexOf("?"));
	%>
							<a href="<%= url  %>"><nobr>[ Next  > ]</nobr></a>
					</pg:next>