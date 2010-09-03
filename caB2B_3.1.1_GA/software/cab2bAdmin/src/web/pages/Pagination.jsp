<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="edu.wustl.cab2b.admin.util.AdminConstants"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>

<%
		String style = getParam(request, "style", "simple");
		String position = getParam(request, "position", "top");
		String index = getParam(request, "index", "center");
		int maxPageItems = getParam(request, "maxPageItems", AdminConstants.MAX_PAGE_ITEMS);
		int maxIndexPages = getParam(request, "maxIndexPages",AdminConstants.MAX_INDEX_PAGES);
		
%>





<%!private static final String getParam(ServletRequest request, String name, String defval) {
        String param = request.getParameter(name);
        return (param != null ? param : defval);
    }

    private static final int getParam(ServletRequest request, String name, int defval) {
        String param = request.getParameter(name);
        int value = defval;
        if (param != null) {
            try {
                value = Integer.parseInt(param);
            } catch (NumberFormatException ignore) {
            }
        }
        return value;
    }%>
</body>
</html>
