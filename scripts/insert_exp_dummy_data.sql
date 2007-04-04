INSERT INTO abstractdomainobject values(1,"active");
INSERT INTO abstractdomainobject values(2,"active");
INSERT INTO abstractdomainobject values(3,"inactive");
INSERT INTO abstractdomainobject values(4,"active");

INSERT INTO additionalmetadata values(1,"Mouse Exp Grp","",current_date(),current_date());
INSERT INTO additionalmetadata values(2,"Mouse gene knockout","Mouse p53 gene knockout experiment",current_date(),current_date());
INSERT INTO additionalmetadata values(3,"Human Microarray Exp Grp","Human microarrya experiment group",current_date(),current_date());
INSERT INTO additionalmetadata values(4,"Human Ins Exp","Experiment on INS gene in Human",current_date(),current_date());


INSERT INTO experimentgroup values(1,"");
INSERT INTO experimentgroup values(3,"");

INSERT INTO experiment values(2);
INSERT INTO experiment values(4);

INSERT INTO expgrpmapping values(1,2,1);
INSERT INTO expgrpmapping values(2,4,3);


INSERT INTO abstractdomainobject values(5,"active");
INSERT INTO abstractdomainobject values(6,"active");
INSERT INTO abstractdomainobject values(7,"inactive");

INSERT INTO additionalmetadata values(5,"DataList-1","My first datalist",current_date(),current_date());
INSERT INTO additionalmetadata values(6,"DataList-2","second data list",current_date(),current_date());
INSERT INTO additionalmetadata values(7,"MyDataList"," this is my data list",current_date(),current_date());


INSERT INTO datalist values(5);
INSERT INTO datalist values(6);
INSERT INTO datalist values(7);

INSERT INTO dlexpmap values(1,2,5);
INSERT INTO dlexpmap values(2,4,7);