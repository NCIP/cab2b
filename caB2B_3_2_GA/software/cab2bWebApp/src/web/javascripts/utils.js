//JavaScript file for common utility functions.

//Function to select/unselect all check-boxes.
//itemName: Name of the group of select boxes to be checked.
//obj: Object representing the Select All checkbox.
 function toggleSelection(itemName, obj)
{
  var items = document.getElementsByName(itemName); 
   for(var i=0;i<items.length;i++)
  {
    items[i].checked = obj.checked;
  }	
}