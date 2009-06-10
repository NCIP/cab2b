/* JavaScript file for common utility functions. */

/* 
 * Function to select/deselect all check-boxes. 
 * Function Author: Chetan_Pundhir
 */
 function toggleSelection(itemName, obj)
{
  var items = document.getElementsByName(itemName); 
   for(var i=0;i<items.length;i++)
  {
    items[i].checked = obj.checked;
  }	
}