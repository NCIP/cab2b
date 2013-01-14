<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>caBench-To-Bedside</title>
<link href="css/cab2b.css" rel="stylesheet" type="text/css" />
<link href="css/cab2b_1024.css" rel="stylesheet" type="text/css">
<script>
 function saveAnotherServiceInstance(action)
 {
 
   var frm = document.getElementById('confirmationForm');
   if(action=='yes')
   { 
    frm.action = "DefineModelGroups.action";
   }
   else
   {
     frm.action = "home.action"; 
   }
   frm.submit();
 }
 
</script>

<script src="js/menu_popup.js">
</script>
<script language="JavaScript" src="js/mm_menu.js"></script>
</head>

<body onLoad="MM_preloadImages('images/home1.gif','images/sd2.gif','images/home2.gif','images/sd1.gif','images/exp1.gif')" >
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="content_table">
  <tr>
    <td align="left" valign="top"><!--Begin content area -->
      <table width="100%" border="0" cellpadding="0" cellspacing="0">

        
        <tr>
          <td colspan="2" align="center" valign="bottom"><table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="75" height="35" colspan="4" align="left" valign="top" background="\\"><table width="200" border="0" cellpadding="0" cellspacing="0" class="td_dgrey">
                <tr>
                  <td><img src="images/spacer.gif" width="10" height="1"></td>
                  <td><img src="images/arrow.gif" width="12" height="11" hspace="5" align="absmiddle"><span class="font_bl2_b">Define Model Group</span></td>
                  <td align="right" valign="top"><img src="images/c6.gif" width="15" height="23"></td>
                </tr>
              </table></td>
              </tr>
            
            
          </table></td>
        </tr>
        
        <tr>
          <td width="25" align="center" valign="bottom">&nbsp;</td>
          <td height="200" align="center" valign="middle"><table width="570" border="0" cellpadding="1" cellspacing="0" class="td_grey">
              <tr>
                <td align="center" valign="middle"><table width="100%" border="0" cellpadding="4" cellspacing="0" bgcolor="#FFFFFF">
                  <form id="confirmationForm" name="confirmationForm" method="post" action="">
                    <tr>
                      <td height="50" align="center" valign="middle" nowrap="nowrap" ><span class="font_bl1_b"><img src="images/ic_success.gif" alt="Success" width="32" height="32" hspace="5" align="absmiddle">Congratulations Model Group saved successfully.</span></td>
                      </tr>
                    <tr>
                      <td height="25" align="center" valign="middle" nowrap="nowrap" class="font_blk_s" > Do you want to Define another Model Group</td>
                    </tr>
                    <tr>
                      <td height="45" align="center" valign="top" nowrap="nowrap"  >
                        <input name="yes" type="button" class="font_blk_s" value="Yes" onclick="saveAnotherServiceInstance('yes');">
                        &nbsp;&nbsp;
                        <input name="no" type="button" class="font_blk_s" value="No" onclick="saveAnotherServiceInstance('no');">                    </td>
                    </tr>
                  </form>
                </table></td>
              </tr>
          </table></td>
        </tr>
      </table>
    <!--end content area --></td>
  </tr>
</table>
</body>
</html>
