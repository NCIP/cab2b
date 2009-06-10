/* JavaScript file for common form validations. */

 function checkEmptyTextFileld(fieldName, exampleValue, noNotificationsToggle)
{ 
   if(!noNotificationsToggle)
  {
     if(document.getElementById('messages'))
    {
      document.getElementById('messages').style.display = 'none';
    }
     if(document.getElementById('errors'))
    {
      document.getElementById('errors').style.display = 'none';
    }
  }
   if(document.getElementsByName(fieldName))
  {  
     if(document.getElementsByName(fieldName)[0].value=="" || (exampleValue && document.getElementsByName(fieldName)[0].value==exampleValue))
    {
	  document.getElementById('error_' + fieldName).style.display = 'block';
	  return false;
    }
     else
    {
	  document.getElementById('error_' + fieldName).style.display = 'none';
	  return true;
    }
  }
}