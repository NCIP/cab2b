<html>
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="css/cab2b.css" rel="stylesheet" type="text/css" />
<link href="css/cab2b_popup.css" rel="stylesheet" type="text/css" />
<script type="text/JavaScript" src="javascript/dhtmlwindow.js" /></script>
<script type="text/JavaScript" src="javascript/queryModule.js" /></script>
</head>
<body>
<table align="center" width="100%" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td>
		<table align="center" width="100%" border="0" cellspacing="0"
			cellpadding="0">
			<tr>
				<td>
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="left" valign="middle" background="images/title_bg.gif"
							class="font_bl3_b"><img src="images/c1.gif" width="12"
							height="24" align="absmiddle" />Matching Attribute Pairs</td>
						<td align="left" valign="top" background="images/title_bg.gif"
							class="td_dblue2">&nbsp;</td>
						<td align="right" valign="top" background="images/title_bg.gif"
							class="td_dblue2"><img src="images/c2.gif" width="12"
							height="24" /></td>
					</tr>
					<tr>
						<td colspan="3" align="left" valign="top" class="td_orange"><img
							src="images/spacer.gif" width="1" height="1" /></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
				<table width="100%" border="0" align="center" cellspacing="0"
					cellpadding="0">
					<tr>
						<td width="55" height="28" align="center" bgcolor="eaeaea"
							class="font_blk_s"><strong>Select</strong></td>
						<td width="100%" bgcolor="eaeaea" class="font_blk_s"><strong>Pair
						Match</strong></td>
					</tr>

					<c:set var="counter" value="${1}" />
					<c:forEach var="attributePair"
						items="${sessionScope.attributePairSet}">
						<c:choose>
							<c:when test="${counter % 2 eq 1}">
								<c:set var="bgcolor" value="#eef9ff" />
							</c:when>
							<c:otherwise>
								<c:set var="bgcolor" value="#f7f7f7" />
							</c:otherwise>
						</c:choose>

						<tr>
							<td height="40" align="center" valign="middle"
								bgcolor="${bgcolor}"><label> <c:choose>
								<c:when test="${counter eq 1}">
									<input type="radio" name="selectPair" id="selectPair"
										value="${attributePair.attribute1.id}_${attributePair.attribute2.id}"
										checked="checked" />
								</c:when>
								<c:otherwise>
									<input type="radio" name="selectPair" id="selectPair"
										value="${attributePair.attribute1.id}_${attributePair.attribute2.id}" />
								</c:otherwise>
							</c:choose> </label></td>
							<td valign="middle" class="font_blk_s" bgcolor="${bgcolor}">
							<b>Pair ${counter}: </b>${attributePair.displayValue}</td>
						</tr>

						<c:set var="counter" value="${counter + 1}" />
					</c:forEach>

					<c:set var="bgcolor" value="#f7f7f7" />
					<c:set var="checked" value="" />
					<c:set var="entity1" value="${sessionScope.entity1}" />
					<c:set var="entity2" value="${sessionScope.entity2}" />
					
					<c:if test="${counter % 2 eq 1}">
						<c:set var="bgcolor" value="#eef9ff" />
					</c:if>
					<c:if test="${counter eq 1}">
						<c:set var="checked" value="checked" />
					</c:if>

					<tr>
						<td height="50" align="center" valign="middle"
							bgcolor="${bgcolor}"><label><input type="radio"
							name="selectPair" id="selectPair" value="manual" checked="${checked}"/></label></td>
						<td height="50" align="center" valign="middle"
							bgcolor="${bgcolor}">
						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							height="100%">
							<tr>
								<td rowspan="2" align="left" valign="middle" class="font_blk_s"><strong>Manual
								Pairing:</strong></td>
								<td align="center"><span class="small_txt">${entity1}:</span></td>
								<td align="center" valign="top" class="font_blk_s">&nbsp;</td>
								<td align="center"><span class="small_txt">${entity2}:</span></td>
								<td rowspan="2" align="left">&nbsp;</td>
							</tr>
							<tr>
								<td align="center"><select name="list1" id="list1">
									<option value="">- select -</option>
									<c:forEach var="attribute1"
										items="${sessionScope.attributeList1}">
										<option value="${attribute1.id}">${attribute1.name}</option>
									</c:forEach>
								</select></td>
								<td align="center" valign="middle" class="small_txt"><b>Connect
								with</b></td>
								<td align="center"><select name="list2" id="list2">
									<option value="">- select -</option>
									<c:forEach var="attribute2"
										items="${sessionScope.attributeList2}">
										<option value="${attribute2.id}">${attribute2.name}</option>
									</c:forEach>
								</select></td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td width="100%" height="50" colspan="2">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="100%">&nbsp;&nbsp;<input type="submit"
									name="button" id="button" class="blue_ar_b" value=" OK "
									onclick="getSelectedValues()" />&nbsp;&nbsp;<span
									class="td_blue"><img src="images/nav_bg.gif" alt=""
									width="1" height="20" hspace="1" align="absmiddle" /></span>&nbsp;&nbsp;<a
									href="#" onclick="javascript:window.close();" class="font_blk_b">Cancel</a></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>
