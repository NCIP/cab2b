<%--L
  Copyright Georgetown University, Washington University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
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

<script>
 function moveUp()
 {
   for(i=0;i<5;i++)
   {
     var chk_box = document.getElementById("checkbox_"+i);
     if(chk_box.checked != true)
     {
       for(j=i+1;j<6;j++)
       {
           var chk_box11 = document.getElementById("checkbox_"+j);
           if(chk_box11.checked == true)
           {
             var tr = document.getElementById("tr_"+i);
             var tr_2 = document.getElementById("tr_"+j);
             var tr_data = tr.innerHTML ;
             tr.innerHTML = tr_2.innerHTML;
             tr_2.innerHTML = tr_data;
           }
       } 
     }
   }
 
 }
 
 function moveDown()
 {
 
 
 }
 var backColor;
function SubmitForm(id)
 {
	 var frm = document.getElementById("attributeOrderForm");

	if(id=="back")
		frm.action="CreateCategory.action?isBack=true";
	else
	 {
		 var displayMessage="";
		 var isDuplicateLabels = false;
			for (var i = 0; i < frm.length; i++) {
					if(frm.elements[i].type=="text"){
					   var fldValue = frm.elements[i].value;
					    
					   fldValue = removeSpaces(fldValue);
					  
					   if(fldValue==''||fldValue.length==0)
					   {
					     alert('Display Label cannot be blank.');
					     frm.elements[i].focus();
					     return false;
					   }
						if(checkSpecialCharacter(frm.elements[i].value)==false)
						{
							displayMessage=displayMessage+frm.elements[i].value +" "+"\n";
							frm.elements[i].style.background="#CD9B9B";
							backColor=frm.elements[i].style.background;
							frm.elements[i].focus();
						}
						for(var j=0;j<i;j++)
						{
						    if(frm.elements[j].type=="text")
						    {
						     var prevValue = frm.elements[j].value;  
						     prevValue = removeSpaces(prevValue);
						     
						   		if(fldValue==prevValue)
						   		{   
						            frm.elements[i].style.background="#CD9B9B";
						            frm.elements[j].style.background="#CD9B9B";
						            isDuplicateLabels = true;
						            
						   		}
						   	}
						   	
						}
			}
	  }
	    if(isDuplicateLabels)
	    {
	    	alert('Display Label should be unique.');
			return false;
		}
		if(displayMessage!=""){
			alert("Please Remove Special Characters From "+"\n"+displayMessage);
			frm.action="AttributeOrder.action";
			return false;
		}

	 }
	frm.submit();
	 

 }
 
 // Removes all starting and ending white spaces from fieldValue. e.g. ' cab2b  ' will be 'cab2b'
  function removeSpaces(fieldValue)
  {
   	return fieldValue.replace(/(^\s*)|(\s*$)/g, "");
  } 
 
function changeBackGroundColor(element){

if(element.style.background==backColor){
element.style.background="";
}



}
 
 
 
</script>
</head>
<body>
<!--Begin content area -->
<!--Begin content area -->
<table width="100%" border="0" cellpadding="0" cellspacing="0">

	<tr>
		<td align="center" valign="top">
		<table width="100%" cellpadding="0" cellspacing="0">

			<tr>
				<td colspan="2" align="center" valign="top" class="td_white">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td height="35" colspan="4" align="left" valign="top"
							background="\\">
						<table width="200" border="0" cellpadding="0" cellspacing="0"
							class="td_dgrey">
							<tr>
								<td><img src="images/spacer.gif" width="10" height="1"></td>
								<td><img src="images/arrow.gif" width="12" height="11"
									hspace="5" align="absmiddle"><span class="font_bl2_b">Create
								Category </span></td>
								<td align="right" valign="top"><img src="images/c6.gif"
									width="15" height="23"></td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td width="25%" align="center" background="images/wizard_bg.gif"><img
							src="images/1_inactive.gif" alt="Category information"
							width="173" height="28"></td>
						<td width="25%" align="center" background="images/wizard_bg.gif"><img
							src="images/2_inactive.gif" alt="Create Category" width="143"
							height="28"></td>
						<td width="25%" align="center" background="images/wizard_bg.gif"><img
							src="images/3_active.gif" alt="Attribute Order" width="136"
							height="28"></td>
						<td align="center" background="images/wizard_bg.gif">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="4" align="center"><img src="images/spacer.gif"
							width="1" height="15"></td>
					</tr>
				</table>

				</td>
			</tr>

			<tr>
				<td width="25" align="center" valign="top" class="td_white">&nbsp;</td>
				<td align="left" valign="top" class="td_white">
				<table border="0" cellspacing="0" cellpadding="0" width="500px">
					<tr>
						<td align="center" valign="top">
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<form name="attributeOrderForm" id="attributeOrderForm"
								method="post" action="AttributeOrder.action">
							<tr>
								<td colspan="2" valign="middle" background="images/title_bg.gif"
									class="font_bl3_b"><img src="images/c1.gif" width="12"
									height="24" hspace="0" vspace="0" align="absmiddle" /></td>
								<td align="left" valign="middle"
									background="images/title_bg.gif" class="font_bl3_b">
								&nbsp;&nbsp;</td>
								<td background="images/title_bg.gif"></td>
								<td align="left" valign="middle"
									background="images/title_bg.gif" class="font_bl3_b">Display
								Label</td>
								<td background="images/title_bg.gif" width="20px"><img
									src="images/spacer.gif" width="20" height="1"></td>
								<td align="left" valign="middle"
									background="images/title_bg.gif" class="font_bl3_b">Attributes</td>
								<td background="images/title_bg.gif"></td>
								<td colspan="2" align="right" valign="middle"
									background="images/title_bg.gif" class="td_dblue2"><img
									src="images/c2.gif" width="12" height="24" /></td>
							</tr>
							<tr align="center">
								<td colspan="2" class="td_orange"><input type="hidden"
									name="formAction" value="save" /></td>
								<td colspan="8" valign="middle" class="td_orange"><img
									src="images/spacer.gif" width="1" height="1"></td>
								<td><input type="hidden" name="title"
									value="${param.title}" /> <input type="hidden"
									name="description" value="${param.description}" /> <input
									type="hidden" name="formAction" value="save" /></td>
							</tr>
							<%
								String css = "td_grey";
								Map<Long, List<String>> allAtributes = (Map<Long, List<String>>) session
										.getAttribute("attributeList");

								Set<Long> set = allAtributes.keySet();

								Iterator<Long> iterator = set.iterator();
								int row = 0;
								while (iterator.hasNext()) {

									long nodeId = iterator.next();
									List<String> list = allAtributes.get(nodeId);

									for (int i = 0; i < list.size(); i++) {

										css = row % 2 == 0 ? "td_grey" : "td_dgrey";
							%>
							<tr class="<%=css%>" id="tr_<%=i%>">
								<td width="0"></td>
								<td width="1">&nbsp;</td>
								<td height="40" align="left" valign="middle"></td>
								<td width="1">&nbsp;</td>
								<td align="left" valign="middle"><input
									name="<%=list.get(i)%>" type="text" class="input_tbox"
									onkeypress="changeBackGroundColor(this)"
									value="<%=list.get(i)%>"></td>
								<td width="1">&nbsp;</td>
								<td align="left" valign="middle"><span class="font_blk_s"><%=list.get(i)%>
								</span></td>
								<td width="1">&nbsp;</td>
								<td align="left" valign="middle"><img
									src="images/spacer.gif" width="1" height="1"></td>
								<td align="left" valign="middle">&nbsp;</td>
							</tr>

							<%
								row++;
									}

								}
							%>

							<tr>
								<td colspan="7" align="center" valign="middle" class="td_grey"><img
									src="images/spacer.gif" width="1" height="1"></td>
							</tr>

						</table>
						</td>
						<td width="100" align="center"><!--                         	
	This code will display move up & down images
	                       	<span class="font_bl2_b">
                        	<img src="images/ic_up.gif" alt="Move Up" width="80" height="23" vspace="3" onclick="moveUp();">
                        	<br>
                            <img src="images/ic_down.gif" alt="Move Down" width="80" height="23" vspace="3" onclick="moveDown();"></span>
 --></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td align="center" valign="top" class="td_white">&nbsp;</td>
				<td align="left" valign="top" class="td_white">&nbsp;</td>
			</tr>
			<tr>
				<td align="center" valign="top" class="td_white">&nbsp;</td>
				<td align="left" valign="top" class="td_white">&nbsp;</td>
			</tr>


		</table>
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="td_white">
			<tr>
				<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="td_dgrey"><img src="images/spacer.gif" width="1"
							height="1" /></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>

				<td height="35" align="right" class="td_grey"><input id="back"
					name="Submit2" type="button" class="font_bl1_b"
					onClick="SubmitForm(this.id);" value="Back" />
				&nbsp;&nbsp;&nbsp;&nbsp; <input id="finish" name="Submit2"
					type="button" class="font_bl1_b" onClick="SubmitForm(this.id);"
					value="Finish" /> &nbsp;&nbsp;&nbsp;</td>
				</form>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td align="center" valign="bottom">&nbsp;</td>
	</tr>
</table>
<!--end content area -->

</body>
</html>
