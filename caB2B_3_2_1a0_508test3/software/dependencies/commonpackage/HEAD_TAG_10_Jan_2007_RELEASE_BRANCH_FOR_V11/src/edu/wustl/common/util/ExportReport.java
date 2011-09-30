
package edu.wustl.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import oracle.sql.CLOB;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;

/**
 * This class for creating a file with a given list of data.
 * It creates the file according to delimeter specified.
 * For eg: if comma is the delimter specified then a CSV file is created. 
 * @author Poornima Govindrao
 * @author deepti_shelar
 * @author Supriya Dankh
 *  
 */

public class ExportReport
{
	/**
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public ExportReport(String fileName) throws IOException
	{
		temp = new BufferedWriter(new FileWriter(fileName));
		/*this.fileName = fileName.substring(0,fileName.lastIndexOf("\""));
		cvsFileWriter = new BufferedWriter(new FileWriter(this.fileName));*/
	}
	/**
	 * This method creates the file according to delimeter specified, without any indentation
	 * for the any row and no blank lines are inserted before start of values.
	 * 
	 * @param values
	 * @param delimiter
	 * @throws IOException
	 */
	public void writeData(List values, String delimiter) throws IOException
	{
		writeData(values, delimiter, 0, 0);
	}
	private BufferedWriter temp;
	private String zipFileName;
	private String path;
	private BufferedWriter cvsFileWriter;
	String fileName;
	/**
	 * This constructor is called when the exported list contains data for 'file' attributes.
	 * 
	 * @param path
	 * @param csvFileName
	 * @param zipFileName
	 * @throws IOException
	 */
	public ExportReport(String path,String csvFileName,String zipFileName) throws IOException
	{
		this.path = path;
		this.zipFileName = zipFileName;
		this.fileName = csvFileName;
		cvsFileWriter = new BufferedWriter(new FileWriter(csvFileName));
	}
	/**
	 * 
	 * @param values
	 * @param delimiter
	 * @param exportMainEntityIdsList
	 * @throws IOException
	 */
	public void writeDataToZip(List values, String delimiter ,List exportMainEntityIdsList) throws IOException
	{
		writeDataToZip(values, delimiter, 0, 0,exportMainEntityIdsList);
	}

	/**
	 * This method creates a zip file which contains a csv file and other data files.
	 * The file is according to delimeter specified.
	 * 
	 * @param values values list. It is List of List. 
	 * @param delimiter delimiter used for separting individaul fields.
	 * @param noblankLines No of blank lines added before values
	 * @param columnIndent No columns that will be left blank for values
	 * @param exportMainEntityIdsList list of main entity ids : required in case of file exports.
	 * @throws IOException
	 */
	public void writeDataToZip(List values, String delimiter, int noblankLines, int columnIndent,List exportMainEntityIdsList)
	throws IOException
	{
		List<String> files = new ArrayList<String>();
		Map<String,String> idFileNameMap = new HashMap<String,String>();
		if(exportMainEntityIdsList!=null && !exportMainEntityIdsList.isEmpty())
		{
			String sql = "SELECT catissue_report_content.IDENTIFIER,REPORT_DATA FROM catissue_report_content,catissue_report_textcontent " +
			" WHERE catissue_report_content.IDENTIFIER = catissue_report_textcontent.REPORT_ID " +
			" AND catissue_report_textcontent.REPORT_ID IN ( ";
			for (Iterator iterator = exportMainEntityIdsList.iterator(); iterator.hasNext();)
			{
				String mainEntityId = (String) iterator.next();
				sql = sql + mainEntityId +",";
				String file = Constants.EXPORT_FILE_NAME_START +mainEntityId +".txt";
				files.add(path+file);
				idFileNameMap.put(mainEntityId+".txt",file);
			}
			sql = sql.substring(0,sql.lastIndexOf(","));
			sql = sql + ")";
			createDataFiles(sql);
		}
		createCSVFile(values, delimiter, noblankLines, columnIndent,idFileNameMap);
		closeFile();
		files.add(fileName);
		createZip(files);
	}
	/**
	 * Creates text files for reports.
	 * @param sql to get the reports 
	 */
	private void createDataFiles(String sql) {
		try
		{
			String newLine = System.getProperty(("line.separator")); 
			List list = executeQuery(sql);
			CLOB clob=null;
			BufferedReader br ;
			if (!list.isEmpty())
			{
				Iterator iterator = list.iterator();
				while(iterator.hasNext())
				{
					List columnList = (List) iterator.next();
					if (!columnList.isEmpty())
					{
						if(columnList.get(1) instanceof CLOB) {
							clob = (CLOB)columnList.get(1);	
							br = new BufferedReader(clob.getCharacterStream());
						}
						else
						{
							String data = (String)columnList.get(1);
							br = new BufferedReader(new StringReader(data));
						}
						String mainEntityId = (String)columnList.get(0); 
						String dataFileName = path +Constants.EXPORT_FILE_NAME_START +mainEntityId+ ".txt";
						File outFile = new File(dataFileName); 
						FileWriter out = new FileWriter(outFile);
						StringBuffer strOut = new StringBuffer();
						String aux;
						while ((aux=br.readLine())!=null)
						{
							strOut.append(aux);
							strOut.append(newLine);
						}
						out.write(strOut.toString());
						out.close();
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Creates a csv file
	 * @param values list data
	 * @param delimiter comma
	 * @param noblankLines no of blank lines
	 * @param columnIndent 
	 * @param idFileNameMap map which stores id and file name 
	 * @throws IOException 
	 */
	private void createCSVFile(List values, String delimiter, int noblankLines, int columnIndent, Map<String,String> idFileNameMap) throws IOException {
		String newLine = System.getProperty(("line.separator"));
		for (int i = 0; i < noblankLines; i++)
		{
			cvsFileWriter.write(newLine);
		}
		if (values != null)
		{
			Iterator itr = values.iterator();
			while (itr.hasNext())
			{
				List rowValues = (List) itr.next();
				Iterator rowItr = rowValues.iterator();

				for (int i = 0; i < columnIndent; i++)
				{
					cvsFileWriter.write("" + delimiter);
				}
				while (rowItr.hasNext())
				{
					String tempStr = (String) rowItr.next();
					if(tempStr.indexOf(Constants.EXPORT_FILE_NAME_START) != -1)
					{
						String[] split = tempStr.split("_");
						String entityId = split[2];
						String fName = idFileNameMap.get(entityId);
						tempStr = fName;	
					}
					if (tempStr == null)
						tempStr = "";
					tempStr = tempStr.replaceAll("\"", "'");
					tempStr = "\"" + tempStr + "\"";
					String data = tempStr + delimiter;
					cvsFileWriter.write(data);
				}
				cvsFileWriter.write(newLine);
			}
		}
	}


	/**
	 * Closes file stream
	 * @throws IOException
	 */
	public void closeFile() throws IOException
	{
		if (temp != null)
		{
			temp.close();
		}
		if (cvsFileWriter != null)
		{
			cvsFileWriter.close();
		}
	}


	/**
	 * This method creates the file according to delimeter specified.
	 * 
	 * @param values values list. It is List of List. 
	 * @param delimiter delimiter used for separting individaul fields.
	 * @param noblankLines No of blank lines added before values
	 * @param columnIndent No columns that will be left blank for values
	 * @throws IOException
	 */
	public void writeData(List values, String delimiter, int noblankLines, int columnIndent)
	throws IOException
	{
		//Writes the list of data into file 
		String newLine = System.getProperty(("line.separator"));
		for (int i = 0; i < noblankLines; i++)
		{
			temp.write(newLine);
		}

		if (values != null)
		{
			Iterator itr = values.iterator();
			while (itr.hasNext())
			{
				List rowValues = (List) itr.next();
				Iterator rowItr = rowValues.iterator();

				for (int i = 0; i < columnIndent; i++)
				{
					temp.write("" + delimiter);
				}

				while (rowItr.hasNext())
				{
					String tempStr = (String) rowItr.next();
					if (tempStr == null)
						tempStr = "";
					tempStr = tempStr.replaceAll("\"", "'");
					tempStr = "\"" + tempStr + "\"";
					String data = tempStr + delimiter;
					temp.write(data);
				}
				temp.write(newLine);
			}
		}
		//closeFile();
	}
	/**
	 * Creates a zip file , contains a csv and other txt files
	 * @param files files to be compressed
	 */
	public void createZip(List<String> files)
	{
		byte[] buf = new byte[1024];
		try {
			String outFilename  = this.zipFileName;
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

			for (Iterator iterator = files.iterator(); iterator.hasNext();)
			{
				String fileName = (String) iterator.next();
				File f = new File(fileName);
				if(fileName.indexOf("csv")!= -1)
				{
					BufferedReader bufRdr  = new BufferedReader(new FileReader(fileName));
					String line = null;
					out.putNextEntry(new ZipEntry(fileName));
					while((line = bufRdr.readLine()) != null)
					{
						StringTokenizer st = new StringTokenizer(line,",");
						while (st.hasMoreTokens())
						{
							String s = st.nextToken();
							out.write(s.getBytes(), 0, s.length());
							s=",";
							out.write(s.getBytes(), 0, s.length());

						}
						String newLine = System.getProperty(("line.separator"));
						out.write(newLine.getBytes(), 0, newLine.length());
						System.out.println(newLine);
					}
					out.closeEntry();
					bufRdr.close(); 
				}
				else
				{
					FileInputStream in = new FileInputStream(fileName);
					out.putNextEntry(new ZipEntry(fileName));
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
				}
				f.delete();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Executes sql and returns data list. 
	 * @param sql
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private List<List<String>> executeQuery(String sql) throws DAOException, ClassNotFoundException
	{
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		dao.openSession(null);

		List<List<String>> dataList = dao.executeQuery(sql, null, false, null);
		return dataList;
	}
}