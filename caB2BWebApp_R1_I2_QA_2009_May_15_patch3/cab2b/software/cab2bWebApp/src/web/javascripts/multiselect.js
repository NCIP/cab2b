selectedItemsCount = 0;

 function setDropDown()
{
   if(document.getElementById('myselectboxitems').style.display == 'block')
  {
	document.getElementById('myselectboxitems').style.display='none';
  }
   else
  {
	document.getElementById('myselectboxitems').style.display='block';
  }
}

 function setChecked(checkBoxes)
{
  var values = '';
   for(var i=0;i<checkBoxes.length;i++)
  {
	 if(checkBoxes[i].checked)
	{
	  selectedItemsCount += 1;
	  values = values + (values!=""?", ":"") + checkBoxes[i].id;
	}
  }
   if(selectedItemsCount == '')
  {
    selectedItemsCount = "---Select Data Types---";
	values = "No items selected";
  }
   else 
  {
	selectedItemsCount = selectedItemsCount + " item(s) selected"
  }
  document.getElementById('selectshow').innerHTML = selectedItemsCount;
  document.getElementById('selectshow').title = 'Selected item(s): ' + values;
}

 function singleSelect(checkBoxes, obj)
{
  selectedItemsCount = 0;
   for(var i=0;i<checkBoxes.length;i++)
  {
	 if(!(checkBoxes[i].checked && checkBoxes[i]==obj))
	{
	  checkBoxes[i].checked = false;
	}
	 else
	{
	  selectedItemsCount++;
	}
  }
  if(selectedItemsCount==1)
  {
	  document.getElementById('selectshow').innerHTML = obj.id;
  }
  else if(selectedItemsCount>1)
  {
	  document.getElementById('selectshow').innerHTML = "Multiple services selected";
  }
  else 
  {
	  document.getElementById('selectshow').innerHTML = "---Select Data Types---";
  }
  document.getElementById('selectshow').title = 'Selected item(s): ' + (selectedItemsCount>0?obj.id:"No items selected");
   if(selectedItemsCount==0 && document.getElementById('savedsearchespanelbody'))
  {
	document.getElementById('savedsearchespanelbody').innerHTML = "";
  }
}