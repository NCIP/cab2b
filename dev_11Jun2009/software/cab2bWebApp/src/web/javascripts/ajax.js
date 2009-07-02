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
  document.getElementById(responseReceiver).innerHTML = "<TABLE style='height:100%;width:100%;'><TR><TD style='text-align:center;vertical-align:middle;'><IMG style='position:relative;top:-20' src='images/PageLoading.gif'></TD></TR></TABLE>";
  var httpRequest = XMLHTTPObject();
  httpRequest.open("POST", requestURL, true);
  httpRequest.onreadystatechange = function(){ 
     if(httpRequest.readyState==4) 
    { 
	  results = httpRequest.responseText;      
	   if(httpRequest.status==500) //http.responseXML can be used to get an XML based response, if we need to have some XML output from a server file.
	  {
		var start = results.indexOf("<DIV id=\"errornotificationpanel\">");
		results = results.substring(start, results.length-1);
		var end = results.indexOf("</DIV>");
		 if(start>0 && end>0)
		{
		  results = results.substring(0, end + 6);
		}
		 else
		{
		  results = "<SPAN class='error'>An unexpected error has occured while processing your request. Please contact helpdesk!</SPAN>";
		}
		results = "<DIV style='text-align:center'>" + results + "</DIV>";
	  }
	   if(httpRequest.status!=403)
	  {
        document.getElementById(responseReceiver).innerHTML = results;
	  }
	   else
	  {
  	    alert(httpRequest.responseText);
		document.location = "Home.do";
	  }
    }
  }
  httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  httpRequest.setRequestHeader("Ajax-Call", "true");
  httpRequest.send(null);
}