<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="edu.common.dynamicextensions.util.global.Constants"%>

<br>
  <table width="700" border="0" cellspacing="0" cellpadding="0" align="center">
    
    <tr>
      <td>
        <table width="701" border="0" cellspacing="0" cellpadding="0">
        <tr>
           <td>&nbsp;</td>
       </tr>
        <tr>
           <td>&nbsp;</td>
       </tr>
       <tr>
          <td align="center">
 
            <b>
               <bean:write name='<%= Constants.ERROR_DETAIL %>' />
            </b>
          </td>
       </tr>
  		<tr>
           <td>&nbsp;</td>
       </tr>

  </table>
</td>
</tr>
</table>

