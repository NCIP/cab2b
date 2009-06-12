	/*This is the wrapper function over show_calendar() that allows to select the date only if the operator is Not 'ANY'
	opratorListId : Id of the date-operators list box
	formNameAndDateFieldId : A string that contains the form name & date field's id
	isSecondDateField : A boolean variable that tells whether the date field is first or second
	*/
	function onDate(operatorListId,formNameAndDateFieldId,isSecondDateField)
	{
	var dateCombo = document.getElementById(operatorListId);
	
	if(dateCombo.options[dateCombo.selectedIndex].value != "Any")
	{
		if(!isSecondDateField)
		{
			show_calendar(formNameAndDateFieldId,null,null,'MM-DD-YYYY')
		}
		else
		{
			if(dateCombo.options[dateCombo.selectedIndex].value == "Between" || dateCombo.options[dateCombo.selectedIndex].value == "Not Between")
			{
				show_calendar(formNameAndDateFieldId,null,null,'MM-DD-YYYY');
			}
		}
	}
	}
	
	var addedNodes = "";
	var isFirtHit = true;
	var isNodeAddedToDag = false;  // Checked before adding new entity to DAG in Create Category Functionality.
	function treeNodeClicked(id)
	{
		if(id.indexOf('_NULL') == -1)
		{
			var aa = id.split("::");		
			var nodes = addedNodes.split(",");
			var isNodeAdded = false;
			if(nodes != "")
			{
			for(i=0; i<nodes.length; i++)
				{
					if(nodes[i] == id)
					{
						isNodeAdded = true;
						break;
					}
				}
			}
			if(!isNodeAdded)
			{
				
				var request = newXMLHTTPReq();			
				var actionURL;
				var handlerFunction = getReadyStateHandler(request,showChildNodes,true);	
				request.onreadystatechange = handlerFunction;				
				actionURL = "nodeId=" + id;				
				var url = "BuildQueryOutputTree.do";
				<!-- Open connection to servlet -->
				request.open("POST",url,true);	
				request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
				request.send(actionURL);	
				addedNodes = addedNodes + ","+id;
			}
		}
		isFirtHit = false;
		if(!isFirtHit)
		{
			//alert(isFirtHit);
		  buildSpreadsheet(id);
		  
		}
		isFirtHit = false;
	}
	
		
			function checkTextBox()
		{
                var textBox_id = document.getElementById("searchText");        
                var form  = document.getElementById("advanceSearch");           
                var checkbox = document.getElementById("checkboxDescription");       
                if(textBox_id.value=="")
                {
                 alert("Please Enter String Value For Search ");
                }
                else
                 form.submit();
          }



	function getUpdatedStatus()
	{
	   var request = newXMLHTTPReq();	
	   var handlerFunction = getReadyStateHandler(request,showStatusChanged,true);
	   request.onreadystatechange = handlerFunction;
	   var url = "LoadSelectedModels.action";
	   request.open("POST",url,true);	
       request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
	   request.send("action=status");
	
	}
	var ModelName = null ;
	function showStatusChanged(text)
	{
		if(text!="")
		{
			var listOfModel = text.split(";");
			for(i=1; i<listOfModel.length; i++)
			{
				var Model = listOfModel[i];			
				
				var nameStatus = Model.split("|");		

				var name = nameStatus[0];
				var status = nameStatus[1];				
				var error = nameStatus[2];
				var html ="";
				var details_link ="";
				var tag = document.getElementById(name);
				if(tag==null)
				 { return; 
				 }
				var detail_tag = document.getElementById(name+"_details");
				if(detail_tag==null)
				{ return; 
				 }
				if(status=='true')
				 {
				   html = "<img src='images/ic_pass.gif' alt='Pass' width='20' height='20' hspace='3' align='absmiddle'> &nbsp;Pass" ;
				 }
				 else if(status=='false')
				 {
				  if(name.indexOf('\(')>-1)
				  {
				    name.replace("\(","\\(");
				    name.replace("\)","\\)");
				  }
				  
 			      ModelName = name;
				  html = "<img src='images/ic_fail.gif' alt='Failed' width='20' height='20' hspace='3' align='absmiddle'>&nbsp;Failed" ;
				  details_link = "<a href='#' class='font_blk_s' onClick=getDetails('"+name+"');><span class='font_bl1_b'>"+error+"</span></a>";
				  detail_tag.innerHTML = details_link;
				  
				  
				 }
				 else
				 {
				   html = "<img src='images/ic_inprogress2.gif' alt='In Progress' width='20' height='20' hspace='3' align='absmiddle'>"+status ;
				 }
				  
				
				tag.innerHTML = html;
					
			}  
		}
	}


	
	
	
	function getDetails(name)
	{

	   var actionURL = "LoadSelectedModels.action?action=details&modelName="+name;
 	   var platform = navigator.platform.toLowerCase();
	   if (platform.indexOf("mac") != -1)
	 	{
		   	window.open(actionURL,"Load Model",'height=500, width=1000,scrollbars');
		}
		else
		{
		   	window.open(actionURL,"LoadModel","height=500,width=1000,scrollbars");
		}
		
	
	}

	function expandOnClick()
	{			
		switchObj = document.getElementById('image');
		dataObj = document.getElementById('collapsableTable');
		var advancedSearchHeaderTd = document.getElementById('advancedSearchHeaderTd');
		var imageContainer = document.getElementById('imageContainer');
	   if(dataObj.style.display != 'none') //Clicked on - image
		{
			advancedSearchHeaderTd.style.borderBottom = "0";
            imageContainer.style.borderBottom = "0";
			dataObj.style.display = 'none';				
			imageContainer.innerHTML = '<img width="12" hspace="3" height="12" border="0" align="absmiddle" onclick="expandOnClick()" src="images/plus.gif"/>';
		}
		else  							   //Clicked on + image
		{
           advancedSearchHeaderTd.style.borderBottom = "0";
			imageContainer.style.borderBottom = "0";
			if(navigator.appName == "Microsoft Internet Explorer")
			{					
				dataObj.style.display = 'block';
			}
			else
			{
			dataObj.style.display = 'block';
			}
	imageContainer.innerHTML = '<img width="12" hspace="3" height="12" border="0" align="absmiddle" onclick="expandOnClick()" src="images/minus.gif"/>';
		}
	}
		




	function expand()
	{			
		switchObj = document.getElementById('image');
		dataObj = document.getElementById('collapsableTable');
        var td1 = document.getElementById('td1');
		var td2 = document.getElementById('td3');
		resultSetDivObj = document.getElementById('resultSetTd');
		var resultSetDiv = document.getElementById('resultSet');
	    //var advancedSearchHeaderTd = document.forms[0].elements['advancedSearchHeaderTd'];
		var advancedSearchHeaderTd = document.getElementById('advancedSearchHeaderTd');
		var imageContainer = document.getElementById('imageContainer');
        
		 	   
	   if(dataObj.style.display != 'none') //Clicked on - image
		{
		//	advancedSearchHeaderTd.style.borderBottom = "1px solid #000000";
     //       imageContainer.style.borderBottom = "1px solid #000000";
			dataObj.style.display = 'none';				
			imageContainer.innerHTML = '<img src="images/plus.gif" border="0" hspace="0" vspace="0" onClick="expand();"/>';
			if(navigator.appName == "Microsoft Internet Explorer")
			{					
				resultSetDivObj.height = "530";
			}
			else
			{
				resultSetDivObj.height = "530";
			}
			resultSetDiv.style.height = "530"+'px';
		}
		else  							   //Clicked on + image
		{
            advancedSearchHeaderTd.style.borderBottom = "0";
			imageContainer.style.borderBottom = "0";
			if(navigator.appName == "Microsoft Internet Explorer")
			{					
				dataObj.style.display = 'block';
	//			td1.style.display = 'block';
	//			td2.style.display = 'block';
				resultSetDivObj.height = "420";
			}
			else
			{
				dataObj.style.display = 'table-row';
				dataObj.style.display = 'block';
		//		td1.style.display = 'block';
		//		td2.style.display = 'block';
				resultSetDivObj.height = "420";
			}
			resultSetDiv.style.height = "420"+'px'
			imageContainer.innerHTML = '<img src="images/minus.gif" border="0" hspace="0" vspace="0" onClick="expand();"/>';
		}
	}
	
	
	function setFocusOnSearchButton(e)
	{
		if (!e) var e = window.event
		if (e.keyCode) code = e.keyCode;
		else if (e.which) code = e.which;
		
		if(code == 13)
		{
			var platform = navigator.platform.toLowerCase();
			if (platform.indexOf("mac") == -1)
			{						
				document.getElementById('searchButton').focus();
			}	
		} 
		else return true;
		
	}
	function retriveSearchedEntities(url,nameOfFormToPost,currentPage) 
	{
		//waitCursor();
		
		var request = newXMLHTTPReq();		
		var textFieldValue = document.forms[0].textField.value;
		var classCheckStatus = document.forms[0].classChecked.checked;
		var attributeCheckStatus = document.forms[0].attributeChecked.checked;
		var permissibleValuesCheckStatus = document.forms[0].permissibleValuesChecked.checked;
		var includeDescriptionCheckedStatus = document.forms[0].includeDescriptionChecked.checked;
		
		var radioCheckStatus;
		var actionURL;
		if(document.forms[0].selected[0].checked)
			radioCheckStatus = "text_radioButton";
		else if(document.forms[0].selected[1].checked)
			radioCheckStatus = "conceptCode_radioButton";

		if(currentPage == 'null')
		{
			var handlerFunction = getReadyStateHandler(request,onResponseUpdate,true);
			actionURL = "textField=" + textFieldValue + "&attributeChecked=" + attributeCheckStatus + "&classChecked=" + classCheckStatus + "&permissibleValuesChecked=" + permissibleValuesCheckStatus + "&includeDescriptionChecked="+includeDescriptionCheckedStatus+ "&selected=" + radioCheckStatus+"&currentPage=AddLimits";
		}
		else
		{
			actionURL = "textField=" + textFieldValue + "&attributeChecked=" + attributeCheckStatus + "&classChecked=" + classCheckStatus + "&permissibleValuesChecked=" + permissibleValuesCheckStatus + "&includeDescriptionChecked="+includeDescriptionCheckedStatus+ "&selected=" + radioCheckStatus +"&currentPage=DefineResultsView";
			var handlerFunction = getReadyStateHandler(request,showEntityListOnDefineViewPage,true);
		}
		request.onreadystatechange = handlerFunction;
				
		
		if(!(classCheckStatus || attributeCheckStatus || permissibleValuesCheckStatus) ) 
		{
			alert("Please choose at least one option for metadata search from advanced options ");
			onResponseUpdate(" ");
		}
		else if(textFieldValue == "")
		{
			alert("Please Enter the String to search.");
			onResponseUpdate(" ");
		}
		else if(radioCheckStatus == null)
		{
			alert("Please select any of the radio button : 'based on' criteria");
			onResponseUpdate(" ");
		}
		else
		{
			request.open("POST",url,true);	
			request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
			request.send(actionURL);
		}
	}
	
	
	function checkCheckbox(formName){
	var element ;
	var checkSelected =0;
	if(navigator.appName == "Microsoft Internet Explorer")	{
				element = document.getElementById('select_check_box');	
	}
	else{
				element = document.forms[formName];
	}
	count =element.elements.length;		
	for (i=0; i < count; i++) 
	{

	if((element.elements[i].name=="checkboxValues") &&  (element.elements[i].checked==true ))
		{
			checkSelected=checkSelected+1;
			continue;
		 }
	}
	
	if(checkSelected <1){
	alert("please select atleast one model ");
	}
	else{

	element.submit();
	
	}
	
	}

	/*
		temprory variable for text value
	*/	
	var tempText;
	
	
	/*	
		showingSelectedLink - This function is used for identify and distinguish selected hyperlink for metaDatasearch. 
	*/
	
	var interModelText;

	function showingSelectedLink(text,selectedName)
	{
		tempText=text;
	
		
		if(selectedName!=" ")
		{
			var element = document.getElementById('resultSet');
			var row ='<table width="100%" border="0" bordercolor="#FFFFFF" cellspacing="0" cellpadding="1">';
			if(text.indexOf("No Result Found") > -1)
			{
				element.innerHTML = row+'<tr><td class="font_bl1_b" >'+text+'</td></tr></table>';
			} 
			else
			{
				var listOfEntities = tempText.split("~!@#$~");
				
				for(i=1; i<listOfEntities.length; i++)
				{
					var e = listOfEntities[i];			
					var nameIdDescription = e.split("~!@!~");		
					var name = nameIdDescription[0];
					var id = nameIdDescription[1];				
					var description = nameIdDescription[2];
					var functionCall;
						if(document.getElementById("curate_or_intermodel")==null)
						 functionCall = "addNodeToView('SearchCategory.action','"+id+"','"+name+"')";		
						else
						 functionCall = "addNodeToCuratePath('SearchCategory.action','"+id+"','"+name+"')";		
						
		
					var entityName = ""+name+"";
					var entityShortName = entityName;
					if(entityName.length>30)
					{
						 entityShortName = entityName.substr(0,30) +"...";
					}
					var desc = "<b> "+entityName+"</b> - <i>"+ description+"</i>";
					if(desc.indexOf("'")>-1)
					{
						while(desc.indexOf("'")>-1)
						{
						   desc = desc.replace("'","\"");
						}
					}

					if(desc.indexOf("\"")>-1)
					{
						while(desc.indexOf("\"")>-1)
						{
						   desc = desc.replace("\""," ");
						}
					}
				
					var tooltipFunction = "Tip('"+desc+"', WIDTH, 200)";
					if(entityName==selectedName)
					{		
						row = row+'<tr><td><div class="font_blk_b" onmouseover="'+tooltipFunction+'">' +entityShortName+ "</div></td></tr><tr height='5'><td></td></tr>";
					}
					else
						row = row+'<tr><td><a class="font_bl1_b" onmouseover="'+tooltipFunction+'"  href="javascript:'+functionCall+'">' +entityShortName+ "</a></td></tr><tr height='5'><td></td></tr>";

				}
				row = row+'</table>';		
				element.innerHTML =row;
			}	
		}
	}
	


	var interModelSearchedText=null;
	var flag=null;

	function setFlag(text){
	flag=text;
	}

	function showEntityListOnDefineViewPage(text)
	{	
		interModelSearchedText=text;
		var element = document.getElementById('resultSet');
		var row ='<table width="100%" border="0" bordercolor="#FFFFFF" cellspacing="0" cellpadding="1">';
		if(text.indexOf("No Result Found") > -1)
		{
			element.innerHTML = row+'<tr><td class="font_bl1_b" >'+text+'</td></tr></table>';
		} 
		else
		{
			var listOfEntities = text.split("~!@#$~");
			
			for(i=1; i<listOfEntities.length; i++)
			{
				var e = listOfEntities[i];			
				var nameIdDescription = e.split("~!@!~");		
				var name = nameIdDescription[0];
				var id = nameIdDescription[1];				
				var description = nameIdDescription[2];
				var functionCall;
				if(document.getElementById("curate_or_intermodel")==null)
				 functionCall = "addNodeToView('SearchCategory.action','"+id+"','"+name+"')";		
				else
				 functionCall = "addNodeToCuratePath('SearchCategory.action','"+id+"','"+name+"')";		
				
				var entityName = ""+name +"";
				var entityShortName = entityName;
				if(entityName.length>30)
				{
				 entityShortName = entityName.substr(0,30) +"...";
				}
				var desc = "<b> "+entityName+"</b> - <i>"+ description+"</i>";
				
				if(desc.indexOf("'")>-1)
				{
					while(desc.indexOf("'")>-1)
					{
					   desc = desc.replace("'","\"");
					}
				}

				if(desc.indexOf("\"")>-1)
				{
					while(desc.indexOf("\"")>-1)
					{
					   desc = desc.replace("\""," ");
					}
				}
				
				var tooltipFunction = "Tip('"+desc+"', WIDTH, 200)";				
				row = row+'<tr><td><a class="font_bl1_b" onmouseover="'+tooltipFunction+'"  href="javascript:'+functionCall+'">' +entityShortName+ "</a></td></tr><tr height='5'><td></td></tr>";
				
			}
			row = row+'</table>';		
			element.innerHTML =row;
			showingSelectedLink(text," ");
		}
		//hideCursor();
	}
	
	function onResponseUpdate(text)
	{
		var element = document.getElementById('resultSet');
		if(text.indexOf("No result found") != -1)
		{
			element.innerHTML =text;
		} 
		else
		{
		
			var listOfEntities = text.split(";");
			var row ='<table width="100%" border="0" bordercolor="#FFFFFF" cellspacing="0" cellpadding="1">';
			for(i=1; i<listOfEntities.length; i++)
			{
				var e = listOfEntities[i];			
				var nameIdDescription = e.split("|");		
				var name = nameIdDescription[0];
				var id = nameIdDescription[1];				
				var description = nameIdDescription[2];
				var functionCall = "retriveEntityInformation('loadDefineSearchRules.do','categorySearchForm','"+id+"')";		
				var entityName = ""+name +"";
				var tooltipFunction = "Tip('"+description+"', WIDTH, 200)";				
				row = row+'<tr><td ><a  class="font_bl1_b"  onmouseover="'+tooltipFunction+'"  href="javascript:'+functionCall+'">' +entityName+ '</a></td></tr>';
			}			
			row = row+'</table>';		
			element.innerHTML =row;
		}
		//hideCursor();
	}
	function retriveEntityInformation(url,nameOfFormToPost,entityName) 
	{	
		//waitCursor();
		var request = newXMLHTTPReq();			
		var actionURL;
		var handlerFunction = getReadyStateHandler(request,showEntityInformation,true);	
		request.onreadystatechange = handlerFunction;	
		actionURL = "entityName=" + entityName;				
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);		
	} 
	
	function showAttributeForEdit(text)
	{
	 
	 
	    
	   var element = document.getElementById('avaliableAttributes');
	   var element2 = document.getElementById('selectedAttributes');
	   var add_edit = document.getElementById('add_edit');
	   var entityId = document.getElementById("entityid");
	   var editEntityName=document.getElementById("editEntityName");
	   
	   
	   for(j=element.length;j>=0;j--)
			   {
				 element.remove(j);
			   }
	   for(j=element2.length;j>=0;j--)
			   {
				 element2.remove(j);
			   }

	  		var splitForEntityName= text.split("#entity_name#")	;

			 editEntityName.innerHTML="<img src=\"images/c1.gif\" width=\"12\" height=\"24\" align=\"absmiddle\" />&nbsp;&nbsp;Edit Attributes For&nbsp; <i>"+splitForEntityName[0] +"</i> <span id=\"entityTitle\"> </span>";			
				
			var splitForEntityId= splitForEntityName[1].split("#entity_id#");
			entityId.value=splitForEntityId[0];
			var str= splitForEntityName[1].split("#edit#");
			add_edit.name=str[0];
			var list=str[1].split("#add_edit#");
			var listOfEntities = list[0].split(";");
		
		
		
			for(i=1; i<listOfEntities.length; i++)
			{
				var o = document.createElement("option");
				var idValue = listOfEntities[i].split(":");
				o.text = idValue[0];
				o.value = listOfEntities[i];
				element[element.length] = o;

			}

		 var listOfEntities = list[1].split(";");	
	
			for(i=1; i<listOfEntities.length; i++)
				{
					var o = document.createElement("option");
					var idValue = listOfEntities[i].split(":");
					o.text = idValue[0];
					o.value = listOfEntities[i];
					element2[element2.length] = o;
				}

	
	
	}
	
	function showEntityInformation(text)
	{	
		var row = document.getElementById('validationMessagesRow');
		row.innerHTML = "";	
		row.style.display = 'none';		
		var element = document.getElementById('addLimits');
		var addLimitsButtonElement = document.getElementById('AddLimitsButtonRow');
		if(text.indexOf("####") != -1)
		{
			var htmlArray = text.split('####');
			addLimitsButtonElement.style.display = 'block';
			//addLimitsButtonElement.height = "30";
			addLimitsButtonElement.innerHTML = htmlArray[0];
			element.innerHTML =htmlArray[1];
		} else 
		{
			element.innerHTML = "";
			addLimitsButtonElement.innerHTML = text;
		}
	//	hideCursor();
	}
	 function createQueryStringCab2b()
        {
        	  isNodeAddedToDag = true;	
	          var strToCreateQueyObject ="";
			  var attributesList = new Array();	
			   var avaliableAttributeList =  document.getElementById("avaliableAttributes");
			  var source_lists  = document.getElementById("selectedAttributes");
			  if(source_lists.length==0)
			  {
			   alert('Please select attributes before adding the node to DAG');
			   return;
			  }   
			  var isAdd=document.getElementById("add_edit");
			  var entityName=document.getElementById("entityid").value;   
			  var i=0;
			  for(i=0;i<=source_lists.length-1;i++)
				  {
					attributesList[i]=source_lists[i].value;
				  }
 
 			//Once node added to DAG both avaliable & selected attributes will be cleared from lists.
			source_lists.length = 0;
			avaliableAttributeList.length =0 ;
			//End
			
			
			for(i=0; i<attributesList.length; i++)
			{
				var attribute = attributesList[i].split(":");
				strToCreateQueyObject =  strToCreateQueyObject + attributesList[i]+";";
			
			
			}
			
			if(isAdd.name=="add_edit")
			   addLimit(strToCreateQueyObject,entityName,null);
            else
            	editLimits(strToCreateQueyObject,entityName);
            	
		 } 
	
		function produceQuery(isTopButton, url,nameOfFormToPost, entityName , attributesList) 
	{
        var strToCreateQueyObject = createQueryString(nameOfFormToPost, entityName , attributesList,'addLimit');
 		if(navigator.appName == "Microsoft Internet Explorer")
		{
			if(isTopButton)
			{
				var isEditLimit = document.getElementById('TopAddLimitButton').value;
			}
			else 
			{
				var isEditLimit = document.getElementById('BottomAddLimitButton').value;
			}
	
		}else
		{
		if(isTopButton)
			{
				var isEditLimit = document.forms[nameOfFormToPost].elements["TopAddLimitButton"].value;
			}
			else 
			{
				var isEditLimit = document.forms[nameOfFormToPost].elements["BottomAddLimitButton"].value;
			}
		}
		if(isEditLimit == 'Add Limit')
		{	
			//document.applets[0].addExpression(strToCreateQueyObject,entityName);
			addLimit(strToCreateQueyObject,entityName);
		}
		else if(isEditLimit == 'Edit Limit')
		{
			//document.applets[0].editExpression(strToCreateQueyObject,entityName);
			editLimits(strToCreateQueyObject,entityName);
			
		}
		//	hideCursor();
	}
	
	function showValidationMessages(text)
	{
		var rowId= 'validationMessagesRow';
		var textBoxId1 = document.getElementById("rowMsg");
		var element = document.getElementById('validationMessages');
		var row = document.getElementById(rowId);
		row.innerHTML = "";
		if(text == "")
		{
			if(document.all)
			{
				document.getElementById("validationMessagesRow").style.display="none";		
			} 
			else if(document.layers) 
			{
				document.elements['validationMessagesRow'].visibility="none";
			}
			else 
			{
				row.style.display = 'none';		
			}	
		}
		else
		{
			row.style.display = 'block';
			row.innerHTML = text;
		}	
	}
	function showErrorPage()
	{
		document.forms['categorySearchForm'].action='ViewSearchResultsJSPAction.do';
		document.forms['categorySearchForm'].nextOperation.value = "showErrorPage";
		document.forms['categorySearchForm'].submit();	
	}
	function showViewSearchResultsJsp()
	{
		document.forms['categorySearchForm'].action='ViewSearchResultsJSPAction.do';
		document.forms['categorySearchForm'].submit();			
	}
	
	
	function enableDisplayField(frm, textfield)
	{
	   var fieldName = textfield+'_displayName';
       var sts =  document.getElementById(fieldName).disabled;
          if(sts==true)
           document.getElementById(fieldName).disabled=false;
          else
            document.getElementById(fieldName).disabled=true;
	}
	
 	function saveQuerySubmitForm(frm,action)
 	{
 	  if(action=='preview')
 	  {
 	    frm.action="/previewExecuteQuery.do?action=preview";
 	    frm.submit();
 	  }
 	  else
 	  { 
 	    frm.action="/saveQuery.do";
 	  }
 	}
	
	function showAlertBox(text)
	{
		if(text != "")
		{
			alert(text);
		}
		else
		{
			var url = "LoadSaveQueryPage.do";
			platform = navigator.platform.toLowerCase();
		    if (platform.indexOf("mac") != -1)
			{
		    	NewWindow(url,'name',screen.width,screen.height,'yes');
		    }
		    else
		    {
		    	NewWindow(url,'name','800','600','yes');
		    }
		}
	}

	
	function defineSearchResultsView()
	{
	//	waitCursor();
		document.forms['categorySearchForm'].action='DefineSearchResultsView.do';
		document.forms['categorySearchForm'].submit();
	//	hideCursor();
						
	}
	function showAddLimitsPage()
	{
		document.forms['categorySearchForm'].action='SearchCategory.do';
		document.forms['categorySearchForm'].currentPage.value = "AddLimits222";
		document.forms['categorySearchForm'].submit();
	}
	function previousFromDefineResults()
	{
	//	waitCursor();
		var action ="SearchCategory.do";
		document.forms['categorySearchForm'].action=action;
		document.forms['categorySearchForm'].isQuery.value = "true";  // change for flex
		document.forms['categorySearchForm'].currentPage.value = "prevToAddLimits";
		document.forms['categorySearchForm'].submit();
	//	hideCursor();
	}
	
	
	var radio="";
	var toggleRadio ="";
	
	
	function disableIncludeDes(val)
	{
		document.forms[0].includeDescriptionChecked.checked = false;
		document.forms[0].includeDescriptionChecked.disabled = val;		
  	}
  

//---Flex Call
var interfaceObj;

	function callFlexMethod()
	{
		if(navigator.appName.indexOf("Microsoft") != -1)
		{
					
			interfaceObj = window["DAG"];				
		}
		else
		{
			interfaceObj = document["DAG"];
		}
		
	}

var jsReady = false;

// ??- functions called by ActionScript ??-
// called to check if the page has initialized and JavaScript is available
	function isReady()
	{
		return jsReady;
	}
// called by the onload event of the <body> tag
	function pageInit()
	 {
	// Record that JavaScript is ready to go.
		jsReady = true;
	}

	function addLimit(strToCreateQueyObject,entityName,callingPage)
	{	
		callFlexMethod();
		interfaceObj.createNode(strToCreateQueyObject,entityName,callingPage);
	}

	function editLimits(strToCreateQueyObject,entityName)
	{	
		callFlexMethod();
		interfaceObj.editLimit(strToCreateQueyObject,entityName);
	}
	
	window.onload=function(){
		pageInit();
	}
	
	 function search()
	 {
		 callFlexMethod();
		 interfaceObj.searchResult();
	 }
	 
	function showAttributeInformation(text)
	{
	   var checkForPage=document.getElementById("curate_or_intermodel");
			if(checkForPage==null){	
			   var element = document.getElementById('avaliableAttributes');
			   var element2 = document.getElementById('selectedAttributes');
			   var add_limit = 	document.getElementById('add_edit');
			   add_limit.name="add_edit";	
			   
				   for(j=element.length;j>=0;j--)
				   {
				     element.remove(j);
				   }
				  for(j=element2.length;j>=0;j--)
					{
					   element2.remove(j);
					  }
			  
					var listOfEntities = text.split(";");
					for(i=1; i<listOfEntities.length; i++)
					{
						var o = document.createElement("option");
						var idValue = listOfEntities[i].split(":");
						o.text = idValue[0];
						o.value = listOfEntities[i];
						element[element.length] = o;
					}
				}		
		else {
			var listOfEntities = text.split(";");
			var strToCreateQueyObject="";
			var entityId=document.getElementById("entityId");
			var nodeStr=entityId.value;

			for(i=1; i<listOfEntities.length; i++)
			{
					strToCreateQueyObject =  strToCreateQueyObject +listOfEntities[i]+";";
			}

			   if(checkForPage.name=="CuratePath")
				   addLimit(strToCreateQueyObject,nodeStr,"CuratedPath");
			   else	{
				   callFlexMethod();
				   interfaceObj.setSearchedTextForInterModel(interModelText);
				   addLimit(strToCreateQueyObject,nodeStr,"InterModelJoin");
				   }
				   
		}
	
	} 
	
	
	
	function addNodeToView(url,nodesStr,name)
	{	
	  var selectedBox = document.getElementById('selectedAttributes');   
	  if(selectedBox.length>0 && isNodeAddedToDag == false)
		{
		   createQueryStringCab2b();
		}
	  var request = newXMLHTTPReq();			
   	  var handlerFunction = getReadyStateHandler(request,showAttributeInformation,true);	
	  request.onreadystatechange = handlerFunction;	
	  var actionURL = "entityId=" + nodesStr;	
	  var element = document.getElementById('entityid');
	  var title = document.getElementById('entityTitle');
	  var editEntityName=document.getElementById("editEntityName");
	 // title.innerHTML = name;
	  element.value=nodesStr;
	  editEntityName.innerHTML="<img src=\"images/c1.gif\" width=\"12\" height=\"24\" align=\"absmiddle\" />&nbsp;&nbsp;Select Attributes For&nbsp; <i>"+name +"</i> <span id=\"entityTitle\"> </span>";			
	  request.open("GET",url+'?'+actionURL,true);	
	  request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
	  request.send(actionURL);		
	  showingSelectedLink(tempText,name);	
	//	callFlexMethod();
	//	interfaceObj.addNodeToView(nodesStr);
	}
	
	
	function addNodeToCuratePath(url,nodesStr,name)
	{	
	 
	  var request = newXMLHTTPReq();			
   	  var handlerFunction = getReadyStateHandler(request,showAttributeInformation,true);	
	  request.onreadystatechange = handlerFunction;	
	  var actionURL = "entityId=" + nodesStr;	
	  var entityId=document.getElementById("entityId");
	  entityId.value=nodesStr;
	  request.open("GET",url+'?'+actionURL,true);	
	  request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
	  request.send(actionURL);	

	   if(flag==null)
		showingSelectedLink(tempText,name);	
	  else
		showEntityListOnDefineViewPage(interModelSearchedText);
	}
	
	
	
	
	/*This function is called form QueryListView.jsp. It invokes the FetchAndExecuteQueryAction*/
	function executeQuery(queryId)
	{
		document.getElementById('queryId').value=queryId;
		document.forms[0].submit();
	} 
	
	/*This function is called form QueryListView.jsp. Pops up for confirmation while deleting the query*/
	function deleteQueryPopup(queryId, popupMessage)
	{
		var r=confirm(popupMessage);
		if (r==true)
		{
			deleteQuery(queryId);
		}

	} 
	
	function deleteQuery(queryId)
	{
		action="DeleteQuery.do?queryId="+queryId;
		document.forms[0].action = action;
		document.forms[0].submit();
	}  
	
	function openDecisionMakingPage()
	{
		action="OpenDecisionMakingPage.do";
		document.forms[0].action = action;
		document.forms[0].submit();
	}
	function proceedClicked()
	{
		var radioObj = document.forms[0].options;
		var option = "";
		var radioLength = radioObj.length;
		
		for(var i = 0; i < radioLength; i++) 
		{
			if(radioObj[i].checked)
			{
				option =  radioObj[i].value;
			}
		}		
		if(option == "")
		{
			alert("Please select the option to proceed");
		}
		else if(option == 'redefineQuery')
		{
			onRedefineQueryOption();
		}
		else
		{
			document.forms[0].submit();
		}
	}	
	
	function onRedefineQueryOption()
	{
		waitCursor();
		document.forms[0].action='SearchCategory.do?currentPage=resultsView';
		document.forms[0].submit();
		hideCursor();
	}
	function checkItDefault()
	{
		document.forms[0].options[1].checked = "true";
	}
	function showMainObjectNotPresentMessage()
	{
		var request = newXMLHTTPReq();			
		var actionURL;
		var handlerFunction = getReadyStateHandler(request,showValidationMessages,true);	
		request.onreadystatechange = handlerFunction;				
		var url = "QueryMessageAction.do";
		<!-- Open connection to servlet -->
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);	
	}
	
	function validateDAG(){
		callFlexMethod();
		if(interfaceObj.isDAGEmpty())
		{
			var message = "Graph must have atleast two nodes";
			interfaceObj.showMessageOnDAG(message);
			return;
		}
		str=interfaceObj.showMessageOnNotConnected();
		if(str!=""){
			interfaceObj.showMessageOnDAG(str);
			return ;		
		}
		var request = newXMLHTTPReq();			
		var handlerFunction = getReadyStateHandler(request,getCurateInfo,true);	
		request.onreadystatechange = handlerFunction;			
		var isSelected = document.getElementById("isSelected");
		var url = "AttributeOrder.action";
		var actionURL="checkMultipleRoot" ;
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);		
		var frm =  document.getElementById("attributesForm");
		frm.action="AttributeOrder.action";
		frm.submit();
		}
	
	
	
	
	

	function showMessage(text){
	callFlexMethod();
	if(text!="noexception")
		interfaceObj.showMessageOnDAG(text);
	else{
		 var frm =  document.getElementById("attributesForm");
		frm.action="AttributeOrder.action";
		frm.submit();
	}	
	}
			
	function validateQuery(text)
	{
		var request = newXMLHTTPReq();			
		var handlerFunction = getReadyStateHandler(request,displayValidationMessage,true);	
		request.onreadystatechange = handlerFunction;			
		var actionURL = "buttonClicked=" + text;		
		var url = "ValidateQuery.do";
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);		
	}

	function displayValidationMessage(text)
	{		
		if (text == "save")
		{
			saveClientQueryToServer('save');
		}
		else
		{
			if (text == "search")
			{
				viewSearchResults();
			}			
			else 
			{
				if(text != "")
				{		
					showValidationMessages(text);
				}		
			}
		}
	}
	
	function getCurateInfo(text){
		if(text=="")
			return;
		interfaceObj.showMessageOnDAG(text);
		if(text=="Saved successfully")
			refreshDAG();
		}
	
	function saveInterModelJoinClasses(){
		callFlexMethod();
		if(interfaceObj.isDAGEmpty())
		{
			var message = "Diagrammatic View Should have Only Two Nodes";
			interfaceObj.showMessageOnDAG(message);
			return;
		}
		str=interfaceObj.showMessageOnNotConnected();
		if(str!=""){
			interfaceObj.showMessageOnDAG(str);
			return ;		
		}
		var request = newXMLHTTPReq();			
		var handlerFunction = getReadyStateHandler(request,showSaveMessage,true);	
		request.onreadystatechange = handlerFunction;			
		var isSelected = document.getElementById("isSelected");
		var actionURL = "";
		var url = "PersistInterModel.action";
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);		
	}
	
	
	function  showSaveMessage(text){
	if(text=="success"){
		alert("InterModel Join Saved Successfully");
		refreshDAG();
		return ;	
	}
		alert("Erros occurs ---->"+text);
	}
	
	function refreshDAG(){
	callFlexMethod();
	interfaceObj.refreshDAG();

	}

	function saveCuratePath(){
		callFlexMethod();
		if(interfaceObj.isDAGEmpty())
		{
			var message = "Graph must have atleast two nodes";
			interfaceObj.showMessageOnDAG(message);
			return;
		}
		str=interfaceObj.showMessageOnNotConnected();
		if(str!=""){
			interfaceObj.showMessageOnDAG(str);
			return ;		
		}
		var request = newXMLHTTPReq();			
		var handlerFunction = getReadyStateHandler(request,getCurateInfo,true);	
		request.onreadystatechange = handlerFunction;			
		var isSelected = document.getElementById("isSelected");
		var actionURL = "isSelected="+isSelected.checked;	
		var url = "CuratePath.action";
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);		
	}
		
	function checkForInterJoinClasses(){
		var request = newXMLHTTPReq();			
		var handlerFunction = getReadyStateHandler(request,getResultForInterModelJoin,true);	
		request.onreadystatechange = handlerFunction;			
		var actionURL =""; 
		var url = "InterModelMatching.action";
		request.open("POST",url,true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		request.send(actionURL);		
	
	}
	
	function getResultForInterModelJoin(text){
		if(text!="success") {
			callFlexMethod();
			interfaceObj.showMessageOnDAG(text);
			return;
		}
		openPopup("InterModelMatching.action?newWindow=true", 685, 480);
	}
	
	function openPopup(pageURL, width, height) {
		var left = (screen.width/2)-(width/2);
		var top = (screen.height/2)-(height/2);
		
		window.open(pageURL, "", 'dependent=no, dialog=yes, modal=yes, toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width='+width+', height='+height+', top='+top+', left='+left);
		/*if(window.showModalDialog) {
			window.showModalDialog(pageURL, "", 'dialogWidth:'+width+'px, dialogHeight:'+height+'px, center:yes, resizable:no, status:no, edge:sunken');
		}else{
			window.open(pageURL, "", 'dependent=no, dialog=yes, modal=yes, toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+width+', height='+height+', top='+top+', left='+left);
	  	}*/
	  
	  	/*popupWindow=dhtmlmodal.open(title, 'iframe', pageURL, title, 'width='+width+'px, height='+height+'px, center=1, resize=0, scrolling=1')
    	popupWindow.onclose=function() { 
	   		return true;
	  	}*/
	}
	
	/* This function closes the dhtml window  		
	function closePopup() {
		parent.popupWindow.hide();
	}*/
	
	function getSelectedValues() {
		var selectedOption = "";
		var radios = document.getElementsByName('selectPair');
		for(i = 0; i < radios.length; i++) {
			if(radios[i].checked){
				selectedOption = radios[i].value;
			}
		}

		if(selectedOption=='manual') {
			var list1 = document.getElementById('list1');
			var selectedAttribute1 = list1[list1.selectedIndex].value;
			if(selectedAttribute1 == "") {
				alert('Select an attribute form the first list.');
				return;
			}
			
			var list2 = document.getElementById('list2');
			var selectedAttribute2 = list2[list2.selectedIndex].value;
			if(selectedAttribute2 == "") {
				alert('Select an attribute form the second list.');
				return;
			}
			
			selectedOption = selectedAttribute1 + "_" + selectedAttribute2 + "_manually connected";
		}
		window.opener.callFlexMethod();
		window.opener.interfaceObj.getModelAttributeInfo(selectedOption);
		window.close();
	}
	
	
	function showExceptionDetail(){
	window.open("AttributeOrder.action?newWindow=true",'name','440','450','false');
//	window.open("AttributeOrder.action?newWindow=true",'width=800,height=600');
	//openPopup("AttributeOrder.action?newWindow=true",800,600)
	}
	
	function checkSpecialCharacter(text){
	var iChars = "!@#$%^&*+=-[]\\';,/{}|\":<>?";

	for (var i = 0; i < text.length; i++) {
  	if (iChars.indexOf(text.charAt(i)) != -1) 
  	{
  		return false;
  	}
	
	}
	
	}
	