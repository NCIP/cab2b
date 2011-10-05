<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>caBench-To-Bedside</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico">
<link href="css/cab2b.css" rel="stylesheet" type="text/css" />
<script src="javascript/menu_popup.js">
</script>
<script language="JavaScript" src="javascript/menu_create.js"></script>
</head>
<body>
<script language="JavaScript1.2">mmLoadMenus();</script>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td width="199" valign="top"><img src="images/logo.gif" alt="caBench-to-Bedside" width="252" height="70" class="td_white" /></td>
        <td align="center" valign="middle"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td align="center" valign="bottom" background="images/bg_top.gif" >&nbsp;</td>
              <td align="left" valign="bottom" background="images/bg_top.gif" class="td_white">&nbsp;</td>
              <td align="right" valign="top" background="images/bg_top.gif" class="td_white"><a href="Logout.action" class="font_bl1_b">Sign Out</a><img src="images/spacer.gif" width="10" height="10" align="absmiddle" alt="spacer"></td>
            </tr>
            <tr>
              <td width="10" align="center" valign="bottom" background="images/bg_top.gif" >&nbsp;</td>
              <td align="left" valign="bottom" background="images/bg_top.gif" class="td_white"><table width="293" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="74"><a href="#" onMouseOut="MM_swapImgRestore();MM_startTimeout()" onMouseOver="MM_swapImage('Image28','','images/home1.gif',1)"><img src="images/home1.gif" alt="Home" name="Image28" width="74" height="23" border="0" id="Image28" /></a></td>
                    <td width="111"><a href="#" onMouseOut="MM_swapImgRestore();MM_startTimeout();" onMouseOver="MM_swapImage('Image29','','images/sd1.gif',1); MM_showMenu(window.mm_menu_1203113257_0,2,23,null,'Image29')" name="link2"  id="link1"  ><img src="images/sd2.gif" alt="Search Data" name="Image29" width="118" height="23" border="0" id="Image29" /></a></td>
                    <td width="108"><a href="#" name="link4" id="link3" onMouseOut="MM_swapImgRestore();MM_startTimeout()" onMouseOver="MM_swapImage('Image12','','images/exp1.gif',1);MM_showMenu(window.mm_menu_1203115052_0,2,23,null,'Image12')"><img src="images/exp2.gif" name="Image12" width="115" height="23" border="0" id="Image12" /></a></td>
                  </tr>
              </table></td>
              <td align="right" valign="middle" background="images/bg_top.gif" class="td_white"><span class="font_bl2_b">Welcome, <%=session.getAttribute("UserName")%><img src="images/spacer.gif" width="10" height="10" align="absmiddle" alt="spacer"></span></td>
            </tr>
            
            
        </table></td>
      </tr>
      
    </table></td>
  </tr>
</table>
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
                  <td><img src="images/arrow.gif" width="12" height="11" hspace="5" align="absmiddle" alt="arrow"><span class="font_bl2_b">Server Problem </span></td>
                  <td align="right" valign="top"><img src="images/c6.gif" width="15" height="23"></td>
                </tr>
              </table></td>
              </tr>
            
            
          </table></td>
        </tr>
        
        <tr>
          <td width="25" align="center" valign="bottom">&nbsp;</td>
          <td height="130" align="center" valign="middle"><table width="570" border="0" cellpadding="1" cellspacing="0" class="td_grey">
              <tr>
                <td align="center" valign="middle"><table width="100%" border="0" cellpadding="4" cellspacing="0" bgcolor="#FFFFFF">
                  <form id="form1" name="form1" method="post" action="">
                    <tr>
                      <td height="50" align="center" valign="middle" nowrap="nowrap" ><span class="font_bl1_b"><img src="images/ic_error.gif" alt="Success" width="32" height="32" hspace="5" align="absmiddle"></span><span class="font_bl2_b">Sorry!</span> <span class="font_blk_s">Unable to process. </span></td>
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
