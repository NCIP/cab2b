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
<title></title>
</head>
<body>
 
<table width="100%" border="0">
<tr>
 <td>
</td>
 </tr>
 <tr>
 <td>
 
 </td>

 <td width="98%">
  <B>Category Name </B> :  ${session.title} <br><br>
  <B>Error Detail
  <textarea cols="115" rows="33" readonly>
	${sessionScope.exceptionMessage}
	</textarea> 
 </td>
 </tr>
</table>
</body>
</html>