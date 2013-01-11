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
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico">
<script type="text/JavaScript">
	function submitForm()
	{
	    
			var userName = document.getElementById('userName');
			var password = document.getElementById('password');
			if(userName.value=='' || userName.value.length==0)
			{
			  alert('Please enter user name!');
			  userName.focus();
			  return false;
			}
			var iChars = "~!^?*|,\\\\/\":<>[]{}`\';()@&$#% ";
      		for (var i = 0; i < userName.value.length; i++) {
          		if (iChars.indexOf(userName.value.charAt(i)) != -1){
              		alert ("User name should not contain special characters!");
              		userName.focus();
              		return false; 
    	    	}
    		}			
			if(password.value=='' || password.value.length==0)
			{
			  alert('Please enter password!');
			  password.focus();
			  return false;
			}			
	}

</script>
</head>

<body onLoad="<%if(request.getAttribute("invalidRequest")!=null){%>alert('Invalid request. Redirected to login page.');<%}%>">
<div id="skipmenu">
<a href="#skip" class="skippy">Skip Navigation</a>
<a name="top"></a>
</div> <!-- end skipmenu -->
<form id="form1" name="form1" method="post" action="Login.action"
	onsubmit="return submitForm();">
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0" class="content_table">
	<tr>
		<td align="left" valign="top">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td colspan="2" align="right" background="images/top_bg.jpg">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="300"><img src="images/logo.jpg"
							alt="caBench-to-Bedside" width="300" height="70" /></td>
						<td align="left">&nbsp;</td>
						<td align="right"><img src="images/top_right.jpg"
							alt="caB2B " width="400" height="70" /></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="2"><img src="images/spacer.gif" width="1"
					height="1" alt="spacer" /></td>
			</tr>
			<tr>
				<td colspan="2"><img src="images/spacer.gif" width="1"
					height="10" alt="spacer"></td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">

			<tr>
				<td align="left" valign="top" width="252" class="td_blue">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="left" valign="middle" background="images/title_bg.gif"
							class="font_bl2_b"><img src="images/c1.gif" width="12"
							height="24" align="absmiddle" alt="c1"/>&nbsp;&nbsp;Quick Links</td>
						<td align="left" valign="top" background="images/title_bg.gif"
							class="td_dblue2">&nbsp;</td>
						<td align="right" valign="top" background="images/title_bg.gif"
							class="td_dblue2"><img src="images/c2.gif" width="12"
							height="24" alt="c2" /></td>
					</tr>
					<tr>
						<td colspan="3" align="left" valign="top" class="td_orange"><img
							src="images/spacer.gif" width="1" height="1" alt="spacer"></td>
					</tr>
				</table>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="3">
					<tr>
						<td width="15" align="right" valign="middle"><img
							src="images/arrow.gif" width="12" height="11" alt="arrow"></td>
						<td class="font_blk_b"><a href="https://cabig.nci.nih.gov/" target="_blank" CLASS="set3">caBIG Home </a></td>
					</tr>
					<tr>
						<td align="right" valign="middle"><img src="images/arrow.gif"
							width="12" height="11" alt="arrow"></td>
						<td class="font_blk_b"><A href="../webpage" target="_blank" CLASS="set3">caB2B Webpage </a></td>
					</tr>
                                <tr>
                                                <td align="right" valign="middle"><img src="images/arrow.gif" width="12" height="11" alt="arrow"></td>
                                                <td class="font_blk_b"><A href="../" CLASS="set3">caB2B Web Client</a></td>
                                </tr>
                                <tr>
                                                <td align="right" valign="middle"><img src="images/arrow.gif" width="12" height="11" alt="arrow"></td>
                                                <td class="font_blk_b"><A href="../webpage/jnlp/cab2b_client_webstart.jnlp" CLASS="set3">caB2B Client Application</a></td>
                                </tr>
                                <tr>
                                                <td align="right" valign="middle"><img src="images/arrow.gif" width="12" height="11" alt="arrow"></td>
                                                <td class="font_blk_b"><A href="../admin" CLASS="set3">caB2B Admin</a></td>
                                </tr>


				</table>
				</td>
<a name="skip"></a>
				<td align="center" valign="top"><img
					src="images/concept_image.jpg" width="500" height="500" alt="concept image"></td>
				<td width="252" align="center" valign="top">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<th scope="col" rowspan="2" valign="top"><img src="images/bg.gif"
							width="13" height="458" alt="bg" /></th>
						<td align="left" valign="top">
						<table width="100%" border="0" cellpadding="4" cellspacing="0">
							<tr>
								<td class="error_font_b" colspan="2">${requestScope.error}</td>
							</tr>
							<tr>
								<th scope="row" nowrap="nowrap" class="font_blk_b"><label for="userName">User Name:</label></th>
								<td align="left"><input name="userName" id="userName" type="text"
									class="font_blk_s" value="" id="userName" autocomplete="off" />
								</td>
							</tr>
							<tr>
 								<th scope="row" nowrap="nowrap"><span class="font_blk_b"><label for="password">Password:</label></span></th>
								<td align="left">
<input name="password" type="password" class="font_blk_s" value="" id="password" autocomplete="off" /></td>
							</tr>

							<tr>
								<td colspan="2" align="center"><input name="Submit"
									type="Submit" class="font_bl1_b" value="Login" /></td>
							</tr>

						</table>
						</td>
					</tr>
					<tr>
						<td align="left" valign="bottom">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<th scope="col" width="15" rowspan="5">&nbsp;</th>
								<th scope="col" class="font_bl1_b">Certified Browsers</th>
							</tr>
							<tr>
								<td class="font_blk_s"><img src="images/logo_ie.gif"
									alt="Internet Explorer 6.0" width="16" height="16" hspace="3"
									vspace="3" align="absmiddle" />Internet Explorer 7.0 <br />
								<img src="images/logo_firefox.gif"
									alt="Mozilla Firefox-2.0.0.3 " width="16" height="16"
									hspace="3" vspace="3" align="absmiddle" />Mozilla
								Firefox 3.0<br />
								<img src="images/logo_safari.gif" alt="Mac Safari 3.1.1 "
									width="16" height="16" hspace="3" vspace="3" align="absmiddle" />Mac
								Safari 3.1.1</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td class="font_bl1_b">Optimal Resolutions</td>
							</tr>
							<tr>
								<td class="font_blk_s"><img src="images/logo_windows.gif"
									alt="Windows " width="16" height="16" hspace="3" vspace="3"
									align="absmiddle" />Windows - 1024 X 768 <br />
								<img src="images/logo_mac.gif" alt="Mac" width="16" height="16"
									hspace="3" vspace="3" align="absmiddle" />Mac - 1024 X 768 <br />
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td align="right" valign="bottom" class="td_blue"><img
					src="images/c5.gif" width="12" height="20" alt="c5" /></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="3" align="left" valign="bottom">&nbsp;</td>
			</tr>
		</table>
		<!--end content area --></td>
	</tr>
	<tr>
		<td valign="bottom">
			<jsp:include page="footer.jsp"/>
		</td>
	</tr>
</table>
</form>
</body>
<script type="text/JavaScript">
 document.getElementById('userName').focus();
</script>
</html>
