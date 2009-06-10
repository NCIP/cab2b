//JavaScript file for add limit functionality.

//Function to auto select first query in the saved query panel and show the add limit form corresponding to that query.
function showAddLimitForm() {
	var objs = document.getElementsByName("savedquery");
	if (objs.length > 0) {
		for ( var i = 0; i < objs.length; i++) {
			if (objs[i]
					&& objs[i].id == document.getElementById("queryId").value) {
				objs[i].className = 'selectedquery';
				document.getElementById('queryname').innerHTML = objs[i].innerHTML;
				processAJAXRequest('AddLimit.do?queryId=' + objs[i].id,
						'definelimitspanelbody');
				document.getElementById("queryId").value = objs[i].id;
				clearTimeout("showAddLimitForm()");
			}
		}
	} else {
		setTimeout("showAddLimitForm()", 1);
	}
}

// Function to update links display style on selection of saved queries.
// Selected saved query link should look different from other saved query links.
function updateLinks(obj) {
	var objs = document.getElementsByName("savedquery");
	for ( var i = 0; i < objs.length; i++) {
		if (objs[i].id == obj.id) {
			objs[i].className = 'selectedquery';
			document.getElementById("queryId").value = objs[i].id;
		} else {
			objs[i].className = 'link';
		}
	}
}

// Function to make proper changes in the add limit form on onchange event in
// select boxes on the form.
function operatorChanged(rowId, dataType) {
	var textBoxId = rowId + "_textBox1";
	var calendarId1 = rowId + "_calendar1";
	var textBoxId0 = rowId + "_textBox";
	var calendarId0 = "calendarImg";
	var opId = rowId + "_combobox";
	if (document.getElementById(textBoxId0)) {
		document.getElementById(textBoxId0).value = "";
	}
	if (document.all) {
		var op = document.getElementById(opId).value;
	} else if (document.layers) {
		var op = document.getElementById(opId).value;
	} else {
		op = document.forms[0].elements[opId].value;
	}
	if (op == "Is Null" || op == "Is Not Null") {
		document.getElementById(textBoxId0).value = "";
		document.getElementById(textBoxId0).disabled = true;
		if (dataType == "true") {
			document.getElementById(calendarId0).disabled = true;
		}
	}

	if (!(op == "In" || op == "Not In")) {
		document.getElementById(textBoxId0).disabled = false;
		if (dataType == "true") {
			document.getElementById(calendarId0).disabled = false;
		}
	}

	if (op == "Is Null" || op == "Is Not Null") {
		document.getElementById(textBoxId0).disabled = true;
	} else {
		if (document.getElementById(textBoxId0)) {
			document.getElementById(textBoxId0).disabled = false;
		}
	}

	if (op == "Between") {
		if (document.all) {
			document.getElementById(textBoxId0).value = "";
			document.getElementById(textBoxId).style.display = "block";
			if (dataType == "true") {
				document.getElementById(calendarId1).style.display = "block";
			}
		} else if (document.layers) {
			document.elements[textBoxId0].value = "";
			document.elements[textBoxId].visibility = "visible";
		} else {
			document.getElementById(textBoxId0).value = "";
			var textBoxId1 = document.getElementById(textBoxId);
			textBoxId1.style.display = "block";
			if (dataType == "true") {
				var calId = document.getElementById(calendarId1);
				calId.style.display = "block";
			}
		}
	} else if (op == "In" || op == "Not In") {
		if (document.all) {
			if (document.getElementById(textBoxId))
				document.getElementById(textBoxId).style.display = "none";
			if (dataType == "true") {
				document.getElementById(calendarId1).style.display = "none";
			}
		} else if (document.layers) {
			document.elements[textBoxId].visibility = "none";
		} else {
			var textBoxId1 = document.getElementById(textBoxId);
			if (textBoxId1) {
				textBoxId1.style.display = "none";
			}
			if (dataType == "true") {
				var calId = document.getElementById(calendarId1);
				calId.style.display = "none";
			}
		}
	} else {
		if (document.all) {
			document.getElementById(textBoxId).style.display = "none";
			if (dataType == "true") {
				document.getElementById(calendarId1).style.display = "none";
			}
		} else if (document.layers) {
			document.elements[textBoxId].visibility = "none";
		} else {
			var textBoxId1 = document.getElementById(textBoxId);
			textBoxId1.style.display = "none";
			if (dataType == "true") {
				var calId = document.getElementById(calendarId1);
				calId.style.display = "none";
			}
		}
	}
}

// Function to make proper changes in the add limit form on onclick event in
// option buttons on the form.
var radio = "";
var toggleRadio = "";
function resetOptionButton(id, currentObj) {
	// Variable radio keeps track of which radio button is selected
	// (true/false).
	// Variable toggleRadio maintains the status of the radiobutton clicked
	// (selected/not selected).
	if (id != radio) // If previous object and current object are not same.
	{
		radio = id; // Set the current object id.
		toggleRadio = "false"; // Default value of radio button.
	}
	if (toggleRadio == "false") // If radiobutton is deselected.
	{
		currentObj.checked = true; // Select it.
		toggleRadio = "true"; // Set toggleRadio to radio button status.
	} else // If toggleRadio=="true", i.e., if radiobutton is selected.
	{
		currentObj.checked = false; // Deselect it.
		toggleRadio = "false"; // Set toggleRadio to radio button status.
	}
}

// Function to create string object representing the changes done on the Add
// Limit form. This string object will be passed to the action class query
// object will be modified accordingly.
function createQueryString() {
	var attributeList = document.getElementById('attributesList').value;
	if (attributeList != null) {
		var queryString = createStandardQueryString(attributeList);
		document.getElementById('conditionList').value = queryString;
	} else {
		alert("No conditions defined in query!")
	}

	if (!(document.getElementById && !document.all)) {
		document.getElementById("definelimitspanelbody").innerHTML += "<INPUT type='hidden' name='conditionList' value='"
				+ queryString + "'>"
	}
	var elements = document.forms[0].elements;
	for ( var i = 0; i < elements.length; i++) {
		if (elements[i].style.display == 'none') {
			elements[i].value = 'NULL';
		}
	}
}

// Function for creating standard query string.
function createStandardQueryString(attributesList) {
	var strToCreateQueyObject = "";
	var attribute = attributesList.split(";");
	var noAttribs = attribute.length - 1;
	for (i = 0; i < noAttribs; i++) {
		var opId = attribute[i] + "_combobox";
		var textBoxId = attribute[i] + "_textBox";
		var textBoxId1 = attribute[i] + "_textBox1";

		var enumBox = attribute[i] + "_enumeratedvaluescombobox";
		if (navigator.appName == "Microsoft Internet Explorer") {
			var op = document.getElementById(opId).value;
			if (document.getElementById(enumBox)) {
				enumValue = document.getElementById(enumBox).value;
			}
		} else {
			var op = document.forms[0].elements[opId].value;
			if (document.forms[0].elements[enumBox]) {
				enumValue = document.forms[0].elements[enumBox].value;
			}
		}

		if (op != "Between") {
			if (document.getElementById(textBoxId)) {
				textId = document.getElementById(textBoxId).value;
				if (textId != "") {
					if (op == "In" || op == "Not In") {
						var valString = "";
						var inVals = textId.split(",");
						for (g = 0; g < inVals.length; g++) {
							if (inVals[g] != "") {
								valString = valString + "&" + inVals[g];
							}
						}
						strToCreateQueyObject = strToCreateQueyObject
								+ "@#condition#@" + attribute[i] + "!*=*!" + op
								+ "!*=*!" + valString + ";";
					} else {
						strToCreateQueyObject = strToCreateQueyObject
								+ "@#condition#@" + attribute[i] + "!*=*!" + op
								+ "!*=*!" + textId + ";";
					}
				}
			}

			if (navigator.appName == "Microsoft Internet Explorer") {
				if (document.getElementById(enumBox)) {
					var ob = document.getElementById(enumBox);
				}
			} else {
				if (document.forms[0].elements[enumBox]) {
					var ob = document.forms[0].elements[enumBox];
				}
			}

			if (ob && ob.value != "") {
				var values = "";
				while (ob.selectedIndex != -1) {
					var selectedValue = ob.options[ob.selectedIndex].value;
					values = values + "&" + selectedValue;
					ob.options[ob.selectedIndex].selected = false;
				}
				strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"
						+ attribute[i] + "!*=*!" + op + "!*=*!" + values + ";";
			}

			var radioButtonTrue = attribute[i] + "_radioButton_true";
			var radioButtonFalse = attribute[i] + "_radioButton_false";
			if (document.getElementById(radioButtonTrue) != null
					|| document.getElementById(radioButtonFalse) != null) {
				var objTrue = document.getElementById(radioButtonTrue);
				var objFalse = document.getElementById(radioButtonFalse);
				if (objTrue.checked) {
					strToCreateQueyObject = strToCreateQueyObject
							+ "@#condition#@" + attribute[i] + "!*=*!" + op
							+ "!*=*!" + 'true' + ";";
				} else if (objFalse.checked) {
					strToCreateQueyObject = strToCreateQueyObject
							+ "@#condition#@" + attribute[i] + "!*=*!" + op
							+ "!*=*!" + 'false' + ";";
				}
			}
		}

		if (op == "Between") {
			if (document.getElementById(textBoxId1)) {
				textId1 = document.getElementById(textBoxId1).value;
			}
			if (document.getElementById(textBoxId)) {
				textId = document.getElementById(textBoxId).value;
			}
			if (textId != "" && textId1 == "") {
				strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"
						+ attribute[i] + "!*=*!" + op + "!*=*!" + textId
						+ "!*=*!" + "missingTwoValues" + ";";
			}
			if (textId1 != "" && textId == "") {
				strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"
						+ attribute[i] + "!*=*!" + op + "!*=*!"
						+ "missingTwoValues" + "!*=*!" + "textId1" + ";";
			}
			if (textId != "" && textId1 != "") {
				strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"
						+ attribute[i] + "!*=*!" + op + "!*=*!" + textId
						+ "!*=*!" + textId1 + ";";
			}
		}

		if (op == "Is Null" || op == "Is Not Null") {
			strToCreateQueyObject = strToCreateQueyObject + "@#condition#@"
					+ attribute[i] + "!*=*!" + op + ";";
		}
	}
	return strToCreateQueyObject;
}

// Function for creating temporal query string.
function createQueryStringForExcecuteSavedTQ(totalCFCount) {
	var strToCreateTQObject = "";
	for (i = 0; i < totalCFCount; i++) {
		var isTimestampFielsId = "isTimeStamp_" + i;
		var tQpId = i + "_combobox";
		var tQtextboxId = i + "_textbox";
		var tQunitId = i + "_combobox1";
		var isTimestamp = document.getElementById(isTimestampFielsId).value;
		var tQop = document.getElementById(tQpId).value;
		var tQtextbox = document.getElementById(tQtextboxId).value;
		strToCreateTQObject = strToCreateTQObject + "@#condition#@" + i + "##"
				+ tQop + "##" + tQtextbox;
		if (isTimestamp == 'false') {
			var tQunit = document.getElementById(tQunitId).value;
			strToCreateTQObject = strToCreateTQObject + "##" + tQunit;
		}
	}
	return strToCreateTQObject;
}