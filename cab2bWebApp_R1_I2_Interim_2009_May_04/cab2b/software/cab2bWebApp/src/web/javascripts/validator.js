//JavaScript file for common form validations.

//Function to validate empty text fields.
//fieldName: Name of the field to validate.
//exampleValue: This parameter can be used if there is some default value for the test field which should be considered similar to empty text field.
//noNotificationsToggle: This parameter can be used to control whether to hide previous notification messages on validation or not.
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