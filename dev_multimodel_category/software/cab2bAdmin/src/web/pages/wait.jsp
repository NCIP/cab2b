<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="refresh" content="5;url=<s:url includeParams="all"/>"/>
</head>

<body>
    <p style="border: 1px solid silver; padding: 5px; background: #ffd; text-align: center;">
         Loading Service Models Please wait...
    </p>

    <p/>
    You can click this link to <a href="<s:url includeParams="all"/>">refresh</a>.

</body>
</html>
