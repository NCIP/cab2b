<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>caBench-To-Bedside</title>

<link href="css/cab2b.css" rel="stylesheet" type="text/css" />

<link href="css/cab2b_1024.css" rel="stylesheet" type="text/css">
<script type="text/JavaScript" src="javascript/menu_popup.js">
</script>
<script type="text/JavaScript" src="javascript/menu_create.js">
</script>

<script type="text/JavaScript" src="javascript/queryModule.js">
</script>

<script type="text/javascript" language="javascript">
function trim(str, chars) {
	return ltrim(rtrim(str, chars), chars);
}

function ltrim(str, chars) {
	chars = chars || "\\s";
	return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
}

function rtrim(str, chars) {
	chars = chars || "\\s";
	return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
}

 function submitForm()
 {
   var titleField = document.getElementById('title');
   var title = titleField.value;

   if(title.length==0 )
   {
     alert('Please enter the category title');
     titleField.focus();
     return false;
   }
     if(trim(title)=="")
	{
     alert('Please enter the valid category title');
     titleField.focus();
     return false;
   }

	if(checkSpecialCharacter(title)==false){
	alert("Please remove special character  from "+ title);
	titleField.focus();
	return false;
	}	

   return true;
 }
 
</script>
</head>
<body>

<!--Begin content area -->
      <form id="categoryInformation" name="form1" method="post" action="CreateCategory.action"  onsubmit="return submitForm();">
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
	 
        
        <tr>
          <td colspan="2" align="center" valign="bottom"><table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td height="35" colspan="4" align="left" valign="top" background="\\"><table width="200" border="0" cellpadding="0" cellspacing="0" class="td_dgrey">
                <tr>
                  <td><img src="images/spacer.gif" width="10" height="1"></td>
                  <td><img src="images/arrow.gif" width="12" height="11" hspace="5" align="absmiddle"><span class="font_bl2_b">Create Category </span></td>
                  <td align="right" valign="top"><img src="images/c6.gif" width="15" height="23"></td>
                </tr>
              </table></td>
              </tr>
            <tr>
              <td width="25%" align="center" background="images/wizard_bg.gif"><img src="images/1_active.gif" alt="Category information" width="173" height="28"></td>
              <td width="25%" align="center" background="images/wizard_bg.gif"><img src="images/2_inactive.gif" alt="Create Category" width="143" height="28"></td>
              <td width="25%" align="center" background="images/wizard_bg.gif"><img src="images/3_inactive.gif" alt="Attribute Order" width="136" height="28"></td>
              <td align="center" background="images/wizard_bg.gif">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="4" align="center"><img src="images/spacer.gif" width="1" height="15"></td>
              </tr>
          </table></td>
        </tr>
         
        <tr>
        
          <td width="25" align="center" valign="bottom">&nbsp;</td>
          <td align="left" valign="top"><table width="570" border="0" cellpadding="1" cellspacing="0" class="td_grey">
              <tr>
                <td align="center" valign="middle"><table width="100%" border="0" cellpadding="4" cellspacing="0" bgcolor="#FFFFFF">
                 
                    <tr>
                      <td align="left" ><img src="images/star.gif" width="5" height="5" hspace="3" vspace="3" align="absmiddle"><span class="font_blk_s">Category Title: </span></td>
                      <td align="left" valign="middle" nowrap><input name="title" id="title" type="text" class="font_blk_s" value="${param.title}" size="40" />
                      <!--   
                          <span class="font_blk_s"><img src="images/ic_list2.gif" alt="List Existing Categories"  hspace="3" vspace="3" border="0" align="absbottom" disabled="true" />List Existing Categories</span>  
                       -->
                     </td>
                    </tr>
                    <tr>
                      <td align="left" valign="top" nowrap="nowrap" class="font_blk_s"><img src="images/spacer.gif" width="5" height="5" hspace="3" vspace="3" align="absmiddle">Category Description:</td>
                      <td align="left" valign="middle"><textarea name="description" id="description" cols="26" rows="5" >${param.description}</textarea>
                        <a href="#" class="font_blk_s"></a></td>
                    </tr>
             
                </table></td>
              </tr>
          </table></td>
        </tr>
        <tr>
          <td align="center" valign="bottom">&nbsp;</td>
          <td align="center" valign="bottom">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" align="center" valign="bottom"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="td_white">
            <tr>
              <td><table width="100%" border="0" cellpadding="0" cellspacing="0" >
                <tr>
                  <td class="td_dgrey"><img src="images/spacer.gif" width="1" height="1" /></td>
                    </tr>
                </table></td>
              </tr>
            <tr>
             
                <td height="35" align="right" class="td_grey"><input name="submit" type="submit" class="font_bl1_b"  value="Next &gt;&gt;" />
                  &nbsp;&nbsp;&nbsp; </td>
               
              </tr>
          </table></td>
           
        </tr>
      </table>
      </form>
					<script type="text/javascript">
					document.getElementById('title').focus();
					</script>
</body>
</html>
