 function getScreenWidth() 
{
  var result = 2000;
   if(window.innerWidth) 
  {
	result = parseInt(window.innerWidth);
  } 
   else if(document.documentElement && document.documentElement.clientWidth) 
  {
	result = parseInt(document.documentElement.clientWidth);
  } 
   else if(document.body) 
  {
	result = parseInt(document.body.clientWidth);
  }
   if(isNaN(result)) 
  {
	result = 2000;
  }
  return result;
}

 function getScreenHeight() 
{
  var result = 2000;
   if(window.innerHeight) 
  {
	result = parseInt(window.innerHeight);
  }
   else if(document.documentElement && document.documentElement.clientHeight) 
  {
	result = parseInt(document.documentElement.clientHeight);
  }
   else if(document.body) 
  {
	result = parseInt(document.body.clientHeight);
  }
   if(isNaN(result)) 
  {
	result = 2000;
  }
  return result;
}

 function getScreenOffsetX() 
{
  var result = 2000;
   if(window.pageXOffset) 
  {
	result = parseInt(window.pageXOffset);
  } 
   else if(document.documentElement && document.documentElement.scrollLeft) 
  {
	result = parseInt(document.documentElement.scrollLeft);
  } 
   else if(document.body) 
  {
	result = parseInt(document.body.scrollLeft);
  }
   if(isNaN(result)) 
  {
	result = 2000;
  }
  return result;
}

 function getScreenOffsetY() 
{
  var result = 2000;
   if(window.innerHeight) 
  {
	result = parseInt(window.pageYOffset);
  } 
   else if(document.documentElement && document.documentElement.scrollTop) 
  {
	result = parseInt(document.documentElement.scrollTop);
  } 
   else if(document.body) 
  {
	result = parseInt(document.body.scrollTop);
  }
   if(isNaN(result)) 
  {
	result = 2000;
  }
  return result;
}
 
var contents = "<IMG STYLE='display:none' SRC='images/close_hover.jpg'><TABLE ID='preloader_table' style='DISPLAY:none;Z-INDEX:1098;POSITION:absolute' height='100%' cellSpacing='0' cellPadding='0' width='100%' border='0'><TBODY><TR><TD ID='preloader_td' STYLE='FILTER:alpha(opacity=40);BACKGROUND-COLOR:#ffffff;opacity:0.4'></TD></TR></TBODY></TABLE><DIV ID='div_desktop' style='Z-INDEX:1001;LEFT:0px;WIDTH:1%;POSITION:absolute;TOP:0px;HEIGHT:1%;TEXT-ALIGN:center'></DIV><DIV ID='loader' STYLE='DISPLAY:none;Z-INDEX:1100; LEFT:0px;WIDTH:320px;POSITION:absolute;TOP:0px;HEIGHT:56px'><TABLE STYLE='BORDER-RIGHT:#2d8ac6 1px solid;BORDER-TOP:#2d8ac6 1px solid;FONT-SIZE:11px;BORDER-LEFT:#2d8ac6 1px solid;WIDTH:320px;COLOR:black;BORDER-BOTTOM:#2d8ac6 1px solid;BACKGROUND-COLOR:#DDDDDD' height='100%' cellSpacing='3' cellPadding='3' width='100%' border='0'><TBODY><TR><TD vAlign=center align=middle width='15%'><IMG ID='ImagePreloader' Style='BORDER-TOP-WIDTH:0px;BORDER-LEFT-WIDTH:0px;BORDER-BOTTOM-WIDTH:0px;BORDER-RIGHT-WIDTH:0px' ALT='Loading' SRC='images/PageLoading.gif'></TD><TD VALIGN='CENTER' ALIGN='LEFT' STYLE='FONT-FAMILY:Tahoma, Arial, Helvetica, sans-serif'><SPAN ID='LabelPreloader' STYLE='font-size:11px'><STRONG>Loading...</STRONG><BR>Please wait until this screen is completely loaded.</SPAN></TD><TD VALIGN='top'><IMG STYLE='cursor:pointer' ALT='Close' SRC='images/close.jpg' onmouseover=this.src='images/close_hover.jpg' onmouseout=this.src='images/close.jpg' onClick=TogglePreloader(0);if(navigator.appName.indexOf('Netscape')==-1){document.execCommand('Stop')}else{window.stop();}></TD></TR></TBODY></TABLE></DIV>";
document.write(contents);

var table = document.getElementById('preloader_table');
var td = document.getElementById('preloader_td');
var e1 = document.getElementById('div_desktop');
var e2 = document.getElementById('loader');
TogglePreloader(1);

 function TogglePreloader(param) 
{  
  document.body.style.overflow = (param==1)?'hidden':'auto';
  var x, y;
  x = getScreenWidth();
  y = getScreenHeight();  
  e1.style.visibility = (param==1)?'visible':'hidden';
  e1.style.display = (param==1)?'block':'none';
  e1.style.width = x + "px";
  e1.style.height = y + "px";
  e1.style.zIndex = 1;  
  var top = (y/2) - 50;
  var left = (x/2) - 160;
   if(left<=0)
  {	 
    left = 10;
  }
  td.style.width = (getScreenWidth() + getScreenOffsetX()) + "px";
  td.style.height = (getScreenHeight() + getScreenOffsetY()) + "px";
  top += getScreenOffsetY();
  left += getScreenOffsetX(); 
  table.style.display = (param==1)?'block':'none';
  e2.style.visibility = (param==1)?'visible':'hidden';
  e2.style.display = (param==1)?'block':'none';
  e2.style.left = left + "px"
  e2.style.top = top + "px";
   if(param)
  {
    var linkObjects = document.getElementsByTagName("A");
     for(var i=0;i<linkObjects.length;i++)
    {
      linkObjects[i].onmouseout = function(){TogglePreloader(0);};
    }
  }	
   if(param)
  {
    setTimeout("document.getElementById('ImagePreloader').src = 'images/PageLoading.gif'", 1);
  }
   else
  {
	clearTimeout("document.getElementById('ImagePreloader').src = 'images/PageLoading.gif'");
  }
}

 if(!window.onload)
{ 
  window.onload = function(){TogglePreloader(0);}; 
}
 else
{
  TogglePreloader(0);  
}
 if(!window.onbeforeunload)
{ 
  window.onbeforeunload = function(){TogglePreloader(1);}; 
}

 if(document.all) 
{
  top.window.moveTo(0, 0);
  top.window.resizeTo(screen.availWidth, screen.availHeight);
}
 else if(document.layers||document.getElementById) 
{
   if(top.window.outerHeight<screen.availHeight || top.window.outerWidth<screen.availWidth)
  {     
    top.window.moveTo(0, 0);
	top.window.outerHeight = screen.availHeight;
    top.window.outerWidth = screen.availWidth;
  }
}