/* JavaScript file for common utility functions. */

/* 
 * Function to select/deselect all check-boxes on the current form
 * Function Author : Chetan_Pundhir 
 * Used in : serviceInstances.jsp
 */
 function selectAll(obj,elementName)
{
  var items = document.getElementsByName(elementName); 
   for(var i=0;i<items.length;i++)
  {
    items[i].checked = obj.checked;
  }	
}