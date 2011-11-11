package edu.wustl.common.util.global;


import java.io.BufferedReader;
import java.io.FileReader;

public class CommonFileReader {

	/**
	 * @param args
	 */
	public String readData(String fileName)
	{
		StringBuffer buffer = new StringBuffer();
		try
		{
//			Logger.out.debug("filename.............................."+fileName);
			//String file = Variables.catissueHome +System.getProperty("file.separator") + fileName;
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = "";
			while((line = br.readLine())!= null)
			{
				//buffer.append(line+System.getProperty("line.separator"));
				buffer.append(line+"<br>");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Data : "+buffer.toString());
		return buffer.toString();
	}
	
//	public String getFileName(String key)
//	{
//		ResourceBundle myResources = ResourceBundle.getBundle("ApplicationResources");
//		String fileName = myResources.getString(key);
//		return fileName;
//		
//	}
	
//	public static void main(String[] args)
//	{
//		CommonFileReader com = new CommonFileReader();
//		com.readData("C:/jboss-4.0.2/server/default/catissuecore-properties/ContactUs.txt");
//	}

}
