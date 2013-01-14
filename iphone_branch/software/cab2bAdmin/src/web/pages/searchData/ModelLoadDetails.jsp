<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>${param.modelName} Failure Details</title>
</head>
<body>
 <c:set var="modelName" value="${param.modelName}"></c:set>
<table width="100%" border="0">
<tr>
 <td>
	${param.modelName} 
 </td>
 </tr>
 <tr>
 <td>
	<textarea cols="100" rows="25" readonly>
	${sessionScope.loadModelResultList[modelName].stackTrace}
	</textarea> 
 </td>
 </tr>
</table>
</body>
</html>