<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>${param.modelName} Failure Details</title>
</head>
<body>
<table width="100%" border="0">
<tr>
 <td>
	${param.modelName} 
 </td>
 </tr>
 <tr>
 <td>
	<textarea cols="100" rows="25" readonly>
	${requestScope.loadModelException}
	</textarea> 
 </td>
 </tr>
</table>
</body>
</html>