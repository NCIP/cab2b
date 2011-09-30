
package edu.wustl.common.querysuite;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author prafull_kadam
 * Class to execute queries for performance testing. 
 *
 */
public class ExecuteQueries
{
	// To print all info on console set this attribute as true.
	private static boolean DEBUG = true; 
	private final static String DATABASE_MYSQL = "mysql";
	private final static String DATABASE_ORACLE = "oracle";

	private static String database;
	private static String host;
	private static String port;
	private static String databaseName;
	private static String userName;
	private static String password;

	private static String sqlFile;
	private static String tables[];
	
	private static BufferedWriter writer;

	/**
	 * To get the Connection object reference.
	 * @return The reference to Connection object.
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception
	{
		String driver = null;
		String url = null;
		if (database.equals(DATABASE_MYSQL))
		{
			driver = "org.gjt.mm.mysql.Driver";
			url = "jdbc:mysql://" + host + "/" + databaseName;
		}
		else if (database.equals(DATABASE_ORACLE))
		{
			driver = "oracle.jdbc.driver.OracleDriver";
			url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + databaseName;
		}
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, userName, password);
		return conn;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Connection conn = null;
		Statement stmt = null;
		if (args.length == 8)
		{
			database = args[0];
			host = args[1];
			port = args[2];
			databaseName = args[3];
			userName = args[4];
			password = args[5];
			sqlFile = args[6];
			tables = args[7].split(",");
		}
		else
		{
			System.out.println("Incorrect Parameters....");
			System.out
					.println("Parameters: <Database Type>, <Database Host>, <Port>, <Database Name>, <User Name>, <Password>,<Sql file>, <Tables>");
			return;
		}
		try
		{
			conn = getConnection();
			// create a statement
			stmt = conn.createStatement();

			try
			{
				writer = new BufferedWriter(new FileWriter("report.txt"));
				printSummary(stmt);
				BufferedReader in = new BufferedReader(new FileReader(sqlFile));
				String str;
				while ((str = in.readLine()) != null)
				{
					executeQuery(str, stmt);
				}
				in.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		finally
		{
			if (writer!=null)
			{
				try
				{
					writer.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// release database resources
			try
			{
				stmt.close();
				conn.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void printSummary(Statement stmt) throws SQLException, IOException
	{
		println("****************************************");
		println("Database:" + database);
		displayRecordCounts(stmt);
		println("****************************************");
	}

	private static void displayRecordCounts(Statement stmt) throws SQLException, IOException
	{
		for (int i = 0; i < tables.length; i++)
		{
			ResultSet rs = stmt.executeQuery("select count(*) From " + tables[i]);
			rs.next();
			int noOfRecords = rs.getInt(1);
			println(tables[i] + ":" + noOfRecords);
			rs.close();
		}
	}

	private static void executeQuery(String query, Statement stmt) throws SQLException, IOException
	{
		println("Executing Query:" + query);
		long start = System.currentTimeMillis();
		// execute query.
		ResultSet rs = stmt.executeQuery("select count(*) from ("+query +") aa");
		long end = System.currentTimeMillis();
		println("Time in seconds:" + (end - start) / 1000.0);
		rs.next();
		println("Records Fetched by Query:"+rs.getInt(1));
		println("--------------------------------------------------------");
	}
	/**
	 * To write message in logger & To print message on console.
	 * @param message
	 * @throws IOException 
	 */
	private static void println(String message) throws IOException
	{
		if (DEBUG)
			System.out.println(message);
		writer.write(message+"\n");
	}
	
}
