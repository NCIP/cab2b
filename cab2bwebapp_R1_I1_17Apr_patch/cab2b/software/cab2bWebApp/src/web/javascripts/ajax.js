/* JavaScript file for AJAX related operations. */

 function XMLHTTPObject()
{
  var xmlhttp; 
   if(window.ActiveXObject) 
  {
	//Instantiate the latest Microsoft ActiveX Objects
	 if(_XML_ActiveX)
	{
	  xmlhttp = new ActiveXObject(_XML_ActiveX);
	}
	 else
	{ 
	  //Loop through the various versions of XMLHTTP to ensure that we're using the latest.
	  var versions = ["MSXML2.XMLHTTP", "Microsoft.XMLHTTP", "Msxml2.XMLHTTP.7.0", "Msxml2.XMLHTTP.6.0", "Msxml2.XMLHTTP.5.0", "Msxml2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0"];			
	   for(var i = 0; i < versions.length ; i++) 
  	  { 
		 try
		{
		  //Try and create the ActiveXObject for Internet Explorer. If it doesn't work, try again.
		  xmlhttp = new ActiveXObject(versions[i]); 
		   if(xmlhttp) 
		  { 
			var _XML_ActiveX = versions[i];
			break;
		  }
		}
		 catch (e)
		{
		  //TRAP.
		}
	  }
	}			
  }
  
  //If there is no ActiveXObject available, then, browser must be firefox, opera, or something else.
   if(!xmlhttp && typeof XMLHttpRequest != 'undefined')
  {
	 try 
	{ 
	  xmlhttp = new XMLHttpRequest(); 
	} 
	 catch(e) 
	{
	  xmlhttp = false; 
	}
  }
  return xmlhttp;
}

 function processAJAXRequest(requestURL, responseReceiver)
{ 
  var httpRequest = XMLHTTPObject();
  httpRequest.open("POST", requestURL, true);
  httpRequest.onreadystatechange = function(){ 
     if(httpRequest.readyState==4) 
    { 
      results = httpRequest.status==200?httpRequest.responseText:"Sorry, there was an error in processing your request! Please contact support."; //http.responseXML can be used to get an XML based response, if we need to have some XML output from a server file.
	  document.getElementById(responseReceiver).innerHTML = results;
    }
  }
  httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  httpRequest.send(null);
}