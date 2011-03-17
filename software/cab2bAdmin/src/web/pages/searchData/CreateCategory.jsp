<%@page import="edu.wustl.cab2b.admin.util.AdminConstants"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>caBench-To-Bedside</title>
<%!

public String filterSpecialChars(String value) {
	if (value == null) { return "";
	} 

	StringBuffer result = new StringBuffer(value.length()); 
        for (int i=0; i<value.length(); ++i) {
	  switch (value.charAt(i)) { 

		case '<':
			result.append("&lt;");
			break; 
		case '>':
			result.append("&gt;");
			break; 	
		case '"':
		//	result.append("&quot;");
			break; 	
		case '\'':
		//	result.append("&#39;");
			break; 	
		case '%':
			result.append("&#37;");
			break; 
		case ';':
			result.append("&#59;"); 
			break; 
		case '(':
			//result.append("&#40;");
			break; 
		case ')':
			//result.append("&#41;");
			break; 
		case '&':
			result.append("&amp;");
			break; 
		case '+':
			result.append("&#43;");
			break; 
		default:
			result.append(value.charAt(i)); break;
	  } 
	}
	return result.toString();
}
%>
<%

String isBack= (String)request.getParameter("isBack");
String titleparam= filterSpecialChars((String)request.getParameter("title"));
String descriptionparam= filterSpecialChars((String)request.getParameter("description"));
session.removeAttribute(AdminConstants.PAGE_IDENTIFIER);
%>

<link href="css/cab2b.css" rel="stylesheet" type="text/css" />

<link href="css/cab2b_1024.css" rel="stylesheet" type="text/css">
<script type="text/JavaScript" src="javascript/menu_popup.js">
</script>
<script type="text/JavaScript" src="javascript/menu_create.js">
</script>
<script>

function removeSpecialCharacter(text){
        var iChars = "!@#$%^&*+=-[]\\';,/{}|\":<>?";
	scs = false;

        for (var i = 0; i < text.length; i++) {
	        if (iChars.indexOf(text.charAt(i)) != -1) {
                	scs = true;
       		 }

        }

	if(scs == true) return "";

        return text;
}



function AddToSelected()
{
  var source_lists = document.getElementById("avaliableAttributes");   
  var destination_lists  = document.getElementById("selectedAttributes");   
  var i=0;
  for(i=source_lists.length-1;i>=0;i--)
  {
   var obj =  source_lists[i];
    if(source_lists[i].selected)
     {
       source_lists.remove(i);
	   destination_lists[destination_lists.length] = obj;; 
       }
       
  }
 
	 isNodeAddedToDag = false;
}

function RemoveFromSelected()
{
  var destination_lists = document.getElementById("avaliableAttributes");   
  var source_lists  = document.getElementById("selectedAttributes");   
  var i=0;
  for(i=source_lists.length-1;i>=0;i--)
  {
   var obj =  source_lists[i];
    if(source_lists[i].selected)
     {
       source_lists.remove(i);
        destination_lists[destination_lists.length] = obj; 
       }
       
  }
 

}

function Previous()
{
   var frm =  document.getElementById("attributesForm");
   frm.action ="CreateCategoryInformation.action";
   frm.submit();
}



</script>


</head>
<body>
<!--Begin content area HERE -->
      <table width="100%" border="0" cellpadding="0" cellspacing="0">

        <tr>
          <td align="center" valign="top"><table width="100%" cellpadding="0" cellspacing="0" >
                  <tr>
                    <td colspan="3" align="center" valign="top" class="td_white"><table width="100%" border="0" cellpadding="0" cellspacing="0">
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
                        <td width="25%" align="center" background="images/wizard_bg.gif"><img src="images/1_inactive.gif" alt="Category information" width="173" height="28"></td>
                        <td width="25%" align="center" background="images/wizard_bg.gif"><img src="images/2_active.gif" alt="Create Category" width="143" height="28"></td>
                        <td width="25%" align="center" background="images/wizard_bg.gif"><img src="images/3_inactive.gif" alt="Attribute Order" width="136" height="28"></td>
                        <td align="center" background="images/wizard_bg.gif">&nbsp;</td>
                      </tr>
                      <tr>
                        <td colspan="4" align="center"><img src="images/spacer.gif" width="1" height="15"></td>
                      </tr>
                    </table></td>
                  </tr>
                  
                
              <tr>
                <td width="252" rowspan="3" align="left" valign="top" class="td_white">
                  <%@ include file="ChooseCategory.jsp" %>
                 </td>
                <td width="1" rowspan="3" align="right" class="td_dgrey"><img src="images/spacer.gif" width="1" height="1"></td>
                <td align="center" valign="top" class="td_white"><table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td id="editEntityName" align="left" valign="middle" background="images/title_bg.gif" class="font_bl3_b"><img src="images/c1.gif" width="12" height="24" align="absmiddle" />&nbsp;&nbsp;Select Attributes For &nbsp;&nbsp; <span id="entityTitle"> </span></td>
                    <td align="left" valign="top" background="images/title_bg.gif" class="td_dblue2">&nbsp;</td>
                    <td align="right" valign="top" background="images/title_bg.gif" class="td_dblue2"><img src="images/c2.gif" width="12" height="24" /></td>
                  </tr>
                  <tr>
                    <td colspan="3" align="left" valign="top" class="td_orange"><img src="images/spacer.gif" width="1" height="1"></td>
                  </tr>
                </table>
                <form id="attributesForm" name="attributesForm" method="post" action="AttributeOrder.action">
                  <table width="99%" border="0" cellpadding="4" cellspacing="0">
                      
                      <tr>
                        <td width="50" align="left">&nbsp;</td>
                        <td width="250" align="left" valign="bottom"><span class="font_blk_b">Available Attributes</span></td>
                        <td width="20">&nbsp;</td>
                        <td width="75">&nbsp;</td>
                        <td width="20" align="left">&nbsp;</td>
                        <td width="250" align="left" valign="bottom"><span class="font_blk_b">Selected Attributes</span></td>
                        <td  align="left" valign="middle">&nbsp;</td>
                      </tr>
                     
                      <tr>
                        <td align="left">&nbsp;
                        	 <input type="hidden" id="entityid" value="123"/>
                        </td>
                        <td align="left" valign="top"><select style="width: 200px;" id ="avaliableAttributes" name="avaliableAttributes" size="6" multiple="multiple" class="input_a">
                            
                            
                        </select></td>
                        <td align="center" valign="middle">&nbsp;</td>
                        <td align="center" valign="middle"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td align="center" valign="middle"><input name="Add" type="button" class="font_bl1_b" value="&nbsp;&nbsp;&nbsp;&nbsp;Add &gt;&gt;&nbsp;&nbsp;&nbsp;&nbsp;" onClick="AddToSelected();"/></td>
                            </tr>
                            <tr>
                              <td align="center" valign="middle">&nbsp;</td>
                            </tr>
                            <tr>
                              <td align="center" valign="middle"><input name="Remove" type="button" class="font_bl1_b" value="&lt;&lt; Remove" onClick="RemoveFromSelected();"/></td>
                            </tr>
                        </table></td>
                        <td align="left">&nbsp;</td>
                        <td align="left" valign="top"><select style="width: 200px;" id ="selectedAttributes" name="selectedAttributes" size="6" multiple="multiple" class="input_a" >
 
                        </select></td>
                        <td align="left">&nbsp;</td>
                      </tr>
                      <tr>
                        <td align="left" class="td_grey">&nbsp;</td>
                        <td align="left" class="td_grey">&nbsp;</td>
                        <td align="center" valign="middle" class="td_grey">&nbsp;</td>
                        <td align="center" valign="middle" class="td_grey">&nbsp;</td>
                        <td align="left" class="td_grey">&nbsp;</td>
                        <td colspan="2" align="left" class="td_grey"><input name="add_edit" type="button" id="add_edit"	class="font_bl1_b" value="Add to DAG" onClick="createQueryStringCab2b()"/></td>
                      </tr>
                   
                  </table></td>
            </tr>
            <tr>
              <td align="center" class="td_white"><table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
                 </table>
                  
                <table width="99%" height="309" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td height="1" class="td_orange"><img src="images/spacer.gif" width="1" height="1" /></td>
                    </tr>
                    <tr>
                      
                      <td height="400px">											
											<div id="queryTableTd" style="overflow:auto;height:100%;width:100%">
											<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
												id="DAG" width="100%" height="100%"
												codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
											<param name="movie" value="flexclient/dag/DAG.swf?isBack=<%=isBack%>"/>
												<param name="quality" value="high" />
												<param name="bgcolor" value="#869ca7" />
												<param name="allowScriptAccess" value="sameDomain"/>
												<embed src="flexclient/dag/DAG.swf?isBack=<%=isBack%>" quality="high" bgcolor="#869ca7"
													width="100%" height="100%" name="DAG" align="middle"
													play="true"
													loop="false"
													quality="high"
													allowScriptAccess="sameDomain"
													type="application/x-shockwave-flash"
													pluginspage="http://www.adobe.com/go/getflashplayer">
												</embed>
											</object>
											<!--This script is for DAG to run ON IE   -->
											<script type="text/javascript">
																				
											var objects = document.getElementsByTagName("object");
											for(i=0;i<objects.length;i++){
												window[objects[i].id] = objects[i];
											}
											</script>

											
											</div>
										</td>
                      
                       
                      
                    </tr>
                </table></td>
            </tr>
            <tr>
              <td colspan="3" align="left" valign="top" class="td_white">&nbsp;</td>
              </tr>
          </table>
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="td_white">
              <tr>
                <td><table width="100%" border="0" cellpadding="0" cellspacing="0" >
                    <tr>
                      <td class="td_dgrey"><img src="images/spacer.gif" width="1" height="1" /></td>
                    </tr>
                </table></td>
              </tr>
              <tr>
                  <td height="35" align="right" class="td_grey"><input name="Submit22" type="submit" class="font_bl1_b" onClick="Previous();" value="Previous" />
                   <input type="hidden" name="attributelist" id="attributelist"  value="abc"/> 
		           <input type="hidden" name="title" value="<%= titleparam %>"/>
				   <input type="hidden" name="description" value="<%= descriptionparam %>"/>	
                  
                
                   <input name="Submit2" type="button" class="font_bl1_b" onClick="validateDAG();" value="Next &gt;&gt;" />  
                    &nbsp;&nbsp;&nbsp; </td>
        
              </tr>
            </table></td>
        </tr>
          </form>  
        <tr>
          <td align="center" valign="bottom">&nbsp;</td>
        </tr>
      </table>
      <!--end content area -->

</body>
</html>
