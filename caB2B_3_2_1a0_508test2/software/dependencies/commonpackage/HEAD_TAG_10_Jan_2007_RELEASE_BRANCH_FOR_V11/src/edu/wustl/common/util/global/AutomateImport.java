package edu.wustl.common.util.global;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

/**
 * This class is for import/export data to database.
 * @author abhishek_mehta
 *
 */
public class AutomateImport
{
	// The Name of the server for the database. For example : localhost
	static String DATABASE_SERVER_NAME;
	// The Port number of the server for the database.
	static String DATABASE_SERVER_PORT_NUMBER;
	// The Type of Database. Use one of the three values 'MySQL', 'Oracle', MsSqlServer.
	static String DATABASE_TYPE;
	//	Name of the Database.
	static String DATABASE_NAME;
	// Database User name
	static String DATABASE_USERNAME;
	// Database Password
	static String DATABASE_PASSWORD;
	// The database Driver 
	static String DATABASE_DRIVER;
	//Oracle Version
	static String ORACLE_TNS_NAME;
	
	/**
	 * 
	 * @param args the arguments to be passed are:
	 * 		 	- database.host
	 * 			- database.port
	 * 			- database.type
	 * 			- database.name
	 * 			- database.username
	 * 			- database.password
	 * 			- database driver
	 * 			- import/export
	 * 			- path for dumpFileColumnInfo.txt which contains the table name list to be imported/exported
	 * 			- folder path for CAModelCSVs files
	 * 			- folder path for CAModelCTLs files required in case of oracle OR 
	 * 			  Format File path in case of mssqlserver (arg[10]) 
	 * 			- oracle.tns.name required in case of oracle 
	 */
	public static void main(String[] args) throws Exception
    {
		Connection connection=null;
		try
		{
			AutomateImport automateImport = new AutomateImport();
			automateImport.configureDBConnection(args);
			connection = automateImport.getConnection();
			ArrayList<String> tableNamesList = automateImport.getTableNamesList(args[8]);
			int size = tableNamesList.size();
			String filePath = args[9].replaceAll("\\\\", "//");
			if(Constants.ORACLE_DATABASE.equals(DATABASE_TYPE.toUpperCase()))
			{
				ORACLE_TNS_NAME = args[11];
				String filePathCTL = args[10].replaceAll("\\\\", "//");
				if(args[7].toLowerCase().equals("import"))
				{
					for(int i = 0 ; i < size; i++)
					{
						String ctlFilePath = filePathCTL+tableNamesList.get(i)+".ctl";
						if(!new File(ctlFilePath).exists())
						{
							String csvFilePath = filePath+tableNamesList.get(i)+".csv";
							automateImport.createCTLFiles(connection,csvFilePath,ctlFilePath,tableNamesList.get(i));
						}
						automateImport.importDataOracle(ctlFilePath);
					}
				}
				else
				{
					for(int i = 0 ; i < size; i++)
					{
						String ctlFilePath = filePathCTL+tableNamesList.get(i)+".ctl";
						String csvFilePath = filePath+tableNamesList.get(i)+".csv";
						automateImport.createCTLFiles(connection,csvFilePath,ctlFilePath,tableNamesList.get(i));
					}
				}
			} else if (Constants.MSSQLSERVER_DATABASE.equals(DATABASE_TYPE.toUpperCase())) {
				if(args[7].toLowerCase().equals("import")) {
					Statement stmt = connection.createStatement();
					for(int i = 0 ; i < size; i++) {
						String dumpFilePath = filePath+tableNamesList.get(i)+".csv";
						String formatFilePath = filePath + tableNamesList.get(i) + Constants.FORMAT_FILE_EXTENTION;
						automateImport.importDataMsSQLServer(connection,dumpFilePath,tableNamesList.get(i), formatFilePath);
					}
				} else {
					for(int i = 0 ; i < size; i++) {
						String dumpFilePath = filePath+tableNamesList.get(i)+".csv";
						automateImport.exportDataMySQL(connection,dumpFilePath,tableNamesList.get(i));
					}
				}
			} else {
				if(args[7].toLowerCase().equals("import"))
				{
					Statement stmt = connection.createStatement();
					stmt.execute("SET FOREIGN_KEY_CHECKS=0;");
					for(int i = 0 ; i < size; i++)
					{
						String dumpFilePath = filePath+tableNamesList.get(i)+".csv";
						automateImport.importDataMySQL(connection,dumpFilePath,tableNamesList.get(i));
					}
					stmt.execute("SET FOREIGN_KEY_CHECKS=1;");
				}
				else
				{
					for(int i = 0 ; i < size; i++)
					{
						String dumpFilePath = filePath+tableNamesList.get(i)+".csv";
						automateImport.exportDataMySQL(connection,dumpFilePath,tableNamesList.get(i));
					}
				}
			}
		}catch(Exception e)
		{
			throw e;
		}
		finally
		{
			if (connection!=null)
				try
				{
					connection.close();
					connection = null;
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
		}
    }
	
	private void test(String[] args) throws SQLException, IOException , ClassNotFoundException
	{
		DATABASE_SERVER_NAME="10.88.199.74";
		DATABASE_SERVER_PORT_NUMBER ="1521";
		DATABASE_TYPE="oracle";
		DATABASE_NAME="clindb";
		DATABASE_USERNAME="oracle_test22";
		DATABASE_PASSWORD="oracle_test22";
		DATABASE_DRIVER="oracle.jdbc.driver.OracleDriver";
		ORACLE_TNS_NAME="climax10g";
		AutomateImport automateImport = new AutomateImport();
		Connection connection = automateImport.getConnection();
		String filePath = "I:/oracleTest/";
		ArrayList<String> tableNamesList = automateImport.getTableNamesList(filePath+"dumpFileColumnInfo1.txt");
		int size = tableNamesList.size();
		for(int i = 0 ; i < size; i++)
		{
			String ctlFilePath = filePath+tableNamesList.get(i)+".ctl";
			String csvFilePath = filePath+tableNamesList.get(i)+".csv";
			automateImport.createCTLFiles(connection,csvFilePath,ctlFilePath,tableNamesList.get(i));
			//automateImport.importDataOracle(ctlFilePath);
		}
	}
	
	/**
	 * Configuration
	 * @param args
	 */
	private void configureDBConnection(String[] args)
	{
		if(args.length < 10)
		{
			throw new RuntimeException("In sufficient number of arguments");
		}
			DATABASE_SERVER_NAME = args[0];
			DATABASE_SERVER_PORT_NUMBER = args[1];
			DATABASE_TYPE = args[2];
			DATABASE_NAME = args[3];
			DATABASE_USERNAME = args[4];
			DATABASE_PASSWORD = args[5];
			DATABASE_DRIVER = args[6];
	}
	
    /**
	 * This method will create a database connection using configuration info.
	 * @return Connection : Database connection object
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Connection connection = null;
		// Load the JDBC driver
		Class.forName(DATABASE_DRIVER);
		// Create a connection to the database
		String url = "";
		if ("MySQL".equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:mysql://" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER + "/"
					+ DATABASE_NAME; // a JDBC url
		}
		if ("Oracle".equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:oracle:thin:@" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER
					+ ":" + DATABASE_NAME;
		}
		if("MsSqlServer".equalsIgnoreCase(DATABASE_TYPE)) {
			url = "jdbc:sqlserver://" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER + ";"
					+ "databaseName=" + DATABASE_NAME + ";";
		}
		System.out.println("URL : " + url);
		connection = DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
		return connection;
	}
    
	/**
	 *  This method will insert the data to database.
	 * @param conn
	 * @param filename
	 * @param tableName
	 */
    private void importDataMySQL(Connection conn,String filename, String tableName) throws SQLException
    {
        Statement stmt;
        try
        {
        	System.out.println("Loding File : " + filename + " to table : " + tableName);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "LOAD DATA LOCAL INFILE '"+filename+ "' INTO TABLE "+tableName+" FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n';";
            stmt.execute(query);
       	}     
        finally
        {
            stmt = null;
        }
    }
    
    /**
	 *  This method will insert the data to mssqlserver database.
	 * @param conn
	 * @param filename
	 * @param tableName
	 */
    private void importDataMsSQLServer(Connection conn,String dataFileName, String tableName, String formatFileName) throws SQLException {
        Statement stmt;
        try {
        	System.out.println("Loding File : " + dataFileName + " using FormatFile : "+ formatFileName + " to table : " + tableName);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //String query = "LOAD DATA LOCAL INFILE '"+filename+ "' INTO TABLE "+tableName+" FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n';";
            String query =  "BULK INSERT " + tableName + " FROM '" + dataFileName + "' WITH ( FIELDTERMINATOR  = ',' , FORMATFILE = '" + formatFileName + "' )";
            stmt.execute(query);
       	} finally {
            stmt = null;
        }
    }
    
    /**
	 *  This method will export the data to database.
	 * @param conn
	 * @param fileName
	 * @param tableName
	 */
    private void exportDataMySQL(Connection conn,String fileName, String tableName)throws SQLException
    {
        Statement stmt;
        try
        {
        	File file = new File(fileName);
        	if(file.exists())
			{
        		file.delete();
			}
        	System.out.println("Exporting table data : " + tableName + " to file : " + fileName);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT * INTO OUTFILE '"+fileName+ "' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' FROM "+tableName+";";
            stmt.execute(query);
        }
        finally
        {
            stmt = null;
        }
    }
    
    /**
     * This method will read the table list file.
     * @param fileName
     * @return
     * @throws IOException
     */
    private ArrayList<String> getTableNamesList(String fileName) throws IOException
    {
    	ArrayList<String> tableNamesList = new ArrayList<String>();
    	BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
		String linereader=reader.readLine();
		while(linereader!=null)
		{
			tableNamesList.add(linereader);
			linereader=reader.readLine();
		}
		return tableNamesList;
    }
    
    /**
     * This method will insert the data to database.
     * @param fileName
     * @throws IOException
     */
    private void importDataOracle(String fileName) throws Exception
    {
    	String cmd="sqlldr "+DATABASE_USERNAME+"/"+DATABASE_PASSWORD+"@"+ORACLE_TNS_NAME+" control="+fileName;
    	System.out.println("Running script: " + cmd);
    	Runtime rt = Runtime.getRuntime();
    	Process proc = rt.exec(cmd);
		 // any error message?
        StreamGobbler errorGobbler = new 
            StreamGobbler(proc.getErrorStream());            
        
        // any output?
        StreamGobbler outputGobbler = new 
            StreamGobbler(proc.getInputStream());
        errorGobbler.start();
        outputGobbler.start();
    	proc.waitFor();
        
    	System.out.println("Executed control file : " + fileName);
    }
    /**
     * This method will create control file for SQL loader.
     * @param connection
     * @param csvFileName
     * @param ctlFileName
     * @param tableName
     * @throws IOException
     * @throws SQLException
     */
    private void createCTLFiles(Connection connection,String csvFileName, String ctlFileName , String tableName) throws IOException, SQLException
    {
    	File file = new File(ctlFileName);
    	if(file.exists())
		{
    		file.delete();
		}
    	BufferedWriter bw=new BufferedWriter(new FileWriter(new File(ctlFileName)));
    	String value = "LOAD DATA INFILE '"+csvFileName+"' "
    					+ "\nBADFILE '/sample.bad'"
    					+ "\nDISCARDFILE '/sample.dsc'"
    					+"\nAPPEND "
    	 				+"\nINTO TABLE "+tableName+" "
    	 				+"\nFIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"'\n" ;
    	String columnName = getColumnName(connection,tableName);
        bw.write(value+columnName);
        bw.flush();
        bw.close();
        System.out.println(value);
        System.out.println("Exporting table data : " + tableName + " to file : " + ctlFileName);
    }

    /**
     * This method will retrieve the column name list for a given table.
     * @param connection
     * @param tableName
     * @return
     * @throws SQLException
     */
    private String getColumnName(Connection connection,String tableName)throws SQLException 
    {
    	String query = "select * from "+tableName+" where 1=2";
    	Statement stmt =null;
    	ResultSet rs = null;
    	try
    	{
	        stmt = connection.createStatement();
	        rs = stmt.executeQuery(query);
	    	StringBuffer sb = new StringBuffer();
	        sb.append("(");
	        ResultSetMetaData rsMetaData = rs.getMetaData();
	        int numberOfColumns = rsMetaData.getColumnCount();
	        for (int i = 1; i < numberOfColumns + 1; i++) {
	        	sb.append(rsMetaData.getColumnName(i));
	        	if(Types.DATE == rsMetaData.getColumnType(i) || Types.TIMESTAMP == rsMetaData.getColumnType(i))
	        	{
	        		sb.append(" DATE 'YYYY-MM-DD'");
	        	}
	        	if(!("HIDDEN".equals(rsMetaData.getColumnName(i))) && !("FORMAT".equals(rsMetaData.getColumnName(i))))
	        	{
		        	sb.append(" NULLIF ");
		        	sb.append(rsMetaData.getColumnName(i));
		        	sb.append("='\\\\N'");
	        	}
	          if(i<numberOfColumns)
	     		 sb.append(",");
	        }
	        sb.append(")");
	       
	        return sb.toString();
    	}
	    catch(SQLException exception){
	    	throw exception;
	    } 
	    finally
	    {
	    	if (stmt != null)
	    	{
	    		stmt.close();
	    	}
	    	if(rs != null)
	    	{
	    		rs.close();
	    	}
	    		
	    }
    }
 }
class StreamGobbler extends Thread
{
    InputStream is;
    
    StreamGobbler(InputStream is)
    {
        this.is = is;
    }
    
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;

            while ( (line = br.readLine()) != null)
                System.out.println(line);    
            } catch (IOException ioe)
              {
                ioe.printStackTrace();  
              }
    }

}
