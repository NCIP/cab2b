package edu.wustl.cab2b.server.path;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.xmi.*;

import java.io.File;
import java.io.FileWriter;

public class ConvertModelUtil {
   /**
    * @param args
    */
   public static void main(String[] args) {
       // TODO Auto-generated method stub
       String xmiFileName =null;
       String projectShortName =null;
       String projectVersion =null;
       String xmiFileType =null; 
       String xmlFileName =null;
       DomainModel model = null;

       if(args.length <5){
		System.err.println("Usage: ant convert.model inputFile ProjectShortName ProjectVersion xmiFileType outputFile");
		System.err.println("example: ant convert.model inputFile.xmi \"My Model\" \"1.0\" \"SDK_40_EA\" outputFile.xml");
               throw new IllegalArgumentException("Missing arguments.");
       }

       xmiFileName=args[0];
       projectShortName = args[1];
       projectVersion = args[2];
       xmiFileType = args[3];
       xmlFileName = args[4];

       try{
           File xmiFile = new File(xmiFileName);
           // convert the model
           XMIParser parser = new XMIParser(projectShortName, projectVersion);
           
           if(xmiFileType.compareToIgnoreCase("SDK_32_EA") == 0){
               model = parser.parse(xmiFile,XmiFileType.SDK_32_EA);
           }
           else if (xmiFileType.compareToIgnoreCase("SDK_40_EA") == 0){
               model = parser.parse(xmiFile,XmiFileType.SDK_40_EA);
           }
           else if (xmiFileType.compareToIgnoreCase("SDK_40_ARGO") == 0){
               model = parser.parse(xmiFile,XmiFileType.SDK_40_ARGO);
           }
           else{
               throw new IllegalArgumentException("Unknown XMI file format "+ xmiFileType);
           }
            File modelFile = new File(xmlFileName);
           FileWriter writer = new FileWriter(modelFile);
           MetadataUtils.serializeDomainModel(model, writer);
           writer.flush();
           writer.close();
       }
       catch(Exception e){
           e.printStackTrace();
       }
   }
}

