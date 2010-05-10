function mmLoadMenus() {
  if (window.mm_menu_1203113257_0) return;
  
  window.mm_menu_1203113257_0 = new Menu("root",175,19,"Arial, Helvetica, sans-serif",11,"#145575","#EFF8FC","#BBE3F6","#1B72B1","left","middle",4,0,1000,-5,7,true,false,true,5,false,true);
  mm_menu_1203113257_0.addMenuItem("Create&nbsp;Category&nbsp;","location='CreateCategoryInformation.action'");
  mm_menu_1203113257_0.addMenuItem("Load&nbsp;Models&nbsp;","location='LoadModels.action?callFrom=menu'");
  mm_menu_1203113257_0.addMenuItem("Service&nbsp;Instances&nbsp;","location='LoadServiceModels.action?callFrom=menu'");
  mm_menu_1203113257_0.addMenuItem("Define&nbsp;Model&nbsp;Groups","location='DefineModelGroups.action?callFrom=menu'");
  mm_menu_1203113257_0.addMenuSeparator();
  mm_menu_1203113257_0.addMenuItem("Curate&nbsp;Paths","location='CuratePaths.action'");
  mm_menu_1203113257_0.addMenuItem("Create&nbsp;InterModel&nbsp;Join","location='ConnectCategory.action'");
  mm_menu_1203113257_0.addMenuSeparator();
  mm_menu_1203113257_0.addMenuItem("Attribute&nbsp;Ordering&nbsp;","location='CreateCategoryAttributeOrder.action'");
  mm_menu_1203113257_0.addMenuItem("Record&nbsp;Name&nbsp;Settings&nbsp;","location='RecordNameSettings.action'");
  mm_menu_1203113257_0.hideOnMouseOut=true;
  mm_menu_1203113257_0.bgColor='#73C5E5';
  mm_menu_1203113257_0.menuBorder=1;
  mm_menu_1203113257_0.menuLiteBgColor='#73c5e5';
  mm_menu_1203113257_0.menuBorderBgColor='#73C5E5';
  
  window.mm_menu_1203115052_0 = new Menu("root",150,17,"Arial, Helvetica, sans-serif",11,"#145575","#EFF8FC","#BBE3F6","#1B72B1","left","middle",3,0,1000,-5,7,true,false,true,5,true,true);
  mm_menu_1203115052_0.addMenuItem("Create&nbsp;Data&nbsp;view","location='CreateDataView.action'");
  mm_menu_1203115052_0.addMenuSeparator();
  mm_menu_1203115052_0.addMenuItem("Filters&nbsp;","location='Filters.action'");
  mm_menu_1203115052_0.addMenuSeparator();
  mm_menu_1203115052_0.addMenuItem("Visualize&nbsp;Data&nbsp;","location='VisualizeData.action'");
  mm_menu_1203115052_0.addMenuItem("Browser&nbsp;Window&nbsp;Viewer&nbsp;","location='BrowserWindowViewer.action'");
  mm_menu_1203115052_0.hideOnMouseOut=true;
  mm_menu_1203115052_0.bgColor='#73C5E5';
  mm_menu_1203115052_0.menuBorder=2;
  mm_menu_1203115052_0.menuLiteBgColor='#73c5e5';
  mm_menu_1203115052_0.menuBorderBgColor='#73C5E5';
  mm_menu_1203115052_0.writeMenus();
} // mmLoadMenus()

function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
  var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
  if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
  d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
  if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
