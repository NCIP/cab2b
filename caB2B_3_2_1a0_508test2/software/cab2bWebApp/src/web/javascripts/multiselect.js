 function setDropDown(dropDownState)
{
   if(dropDownState)
  {
	document.getElementById('myselectboxitems').style.display = 'block';
  }
   else
  {
	document.getElementById('myselectboxitems').style.display = 'none';
  }
}

 function setSelection(checkBoxes)
{
  singleSelectIndex = -1;
  selectedItemsCount = 0;
  var values = '';
   for(var i=0;i<checkBoxes.length;i++)
  {
	 if(checkBoxes[i].checked)
	{
	  selectedItemsCount += 1;
	  values = values + (values!=""?", ":"") + checkBoxes[i].id;
	  singleSelectIndex = i;
	}
  }
   if(selectedItemsCount==0)
  {
	document.getElementById('selectshow').innerHTML = "---Select Data Types---";
  }
   else if(selectedItemsCount==1)
  {
	document.getElementById('selectshow').innerHTML = checkBoxes[singleSelectIndex].id;
  }
   else 
  {
	document.getElementById('selectshow').innerHTML = "Multiple services selected";
  }
  document.getElementById('selectshow').title = 'Selected item(s): ' + (selectedItemsCount>0?values:"No items selected");
}