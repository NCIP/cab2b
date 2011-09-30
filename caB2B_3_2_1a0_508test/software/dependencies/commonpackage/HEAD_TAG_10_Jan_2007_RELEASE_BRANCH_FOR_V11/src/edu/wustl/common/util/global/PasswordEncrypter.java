
package edu.wustl.common.util.global;

import edu.wustl.common.security.exceptions.PasswordEncryptionException;
import gov.nih.nci.security.util.StringUtilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * PasswordEncrypter: This class encrypts all data of given field from a given table. The database connection parameters have to be provided.
 * 
 * The encryption is done using the class PasswordManager.
 * 
 * @author abhishek_mehta
 *
 */

public class PasswordEncrypter
{

	// The Name of the server for the database. For example : localhost
	static String DATABASE_SERVER_NAME;
	// The Port number of the server for the database.
	static String DATABASE_SERVER_PORT_NUMBER;
	// The Type of Database. Use one of the two values 'MySQL', 'Oracle'.
	static String DATABASE_TYPE;
	//	Name of the Database.
	static String DATABASE_NAME;
	// Database User name
	static String DATABASE_USERNAME;
	// Database Password
	static String DATABASE_PASSWORD;
	// The database Driver 
	static String DATABASE_DRIVER;
	// The name of the csm table whose password field is to be encrypted.
	static String CSM_DATABASE_TABLE_NAME = "csm_user";
	// The name of the catissue table whose password field is to be encrypted.
	static String CATISSUE_DATABASE_TABLE_NAME = "catissue_password";
	// The name of the field whose row values have to be encrypted.
	static String DATABASE_TABLE_FIELD_NAME = "password";

	public static void main(String[] args)
	{

		System.out
				.println("Encrypting password for the following tables: csm_user & catissue_password");
		System.out.println("Configuring Database Connection Parameters");
		configureDBConnection(args);

		try
		{
			// Create an updatable result set
			Connection connection = getConnection();

			//Encrypting password for csm_user table
			System.out.println("Encrypting passwords for csm_user table...");
			String sql = "SELECT " + CSM_DATABASE_TABLE_NAME + ".* FROM " + CSM_DATABASE_TABLE_NAME;
			updatePasswords(connection, sql);

			//Encrypting password for catissue_password table
			System.out.println("Encrypting passwords for catissue_password table...");
			sql = "SELECT " + CATISSUE_DATABASE_TABLE_NAME + ".* FROM "
					+ CATISSUE_DATABASE_TABLE_NAME;
			updatePasswords(connection, sql);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method will update password field with new encrypted password in the database.
	 * @param connection Database connection object
	 * @param sql Query String
	 * @throws SQLException
	 */
	private static void updatePasswords(Connection connection, String sql) throws SQLException
	{
		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		ResultSet resultSet = stmt.executeQuery(sql);

		boolean first = false;
		while (resultSet.next())
		{
			if (!first)
			{
				resultSet.first();
				first = true;
			}
			String userPassword = resultSet.getString(DATABASE_TABLE_FIELD_NAME);
			if (!StringUtilities.isBlank(userPassword))
			{
				try
				{
					//Encrypting the password and updating the database.
					String encryptedPasswordWithNewEncryption = PasswordManager
							.encrypt(PasswordManager.decode(userPassword));
					if (!StringUtilities.isBlank(encryptedPasswordWithNewEncryption))
					{
						resultSet.updateString(DATABASE_TABLE_FIELD_NAME,
								encryptedPasswordWithNewEncryption);
						resultSet.updateRow();
					}
				}
				catch (PasswordEncryptionException e)
				{
					e.printStackTrace();
				}
			}
		}
		resultSet.close();
		stmt.close();
	}

	/**
	 * This method is for configuring database connection.
	 * @param args String[] of configuration info 
	 */
	private static void configureDBConnection(String[] args)
	{
		if (args.length == 7)
		{
			DATABASE_SERVER_NAME = args[0];
			DATABASE_SERVER_PORT_NUMBER = args[1];
			DATABASE_TYPE = args[2];
			DATABASE_NAME = args[3];
			DATABASE_USERNAME = args[4];
			DATABASE_PASSWORD = args[5];
			DATABASE_DRIVER = args[6];
			printDBInfo();
		}
		else
		{
			System.out.println("Incorrect number of parameters!");
			throw new RuntimeException("Incorrect number of parameters!!!!");
		}
	}

	/**
	 * Printing the Configuration info for database.
	 */
	private static void printDBInfo()
	{
		System.out.println("DATABASE_SERVER_NAME        : " + DATABASE_SERVER_NAME);
		System.out.println("DATABASE_SERVER_PORT_NUMBER : " + DATABASE_SERVER_PORT_NUMBER);
		System.out.println("DATABASE_TYPE               : " + DATABASE_TYPE);
		System.out.println("DATABASE_NAME               : " + DATABASE_NAME);
		System.out.println("DATABASE_DRIVER             : " + DATABASE_DRIVER);
		System.out.println("CSM_DATABASE_TABLE_NAME     : " + CSM_DATABASE_TABLE_NAME);
		System.out.println("CATISSUE_DATABASE_TABLE_NAME: " + CATISSUE_DATABASE_TABLE_NAME);
		System.out.println("DATABASE_TABLE_FIELD_NAME   : " + DATABASE_TABLE_FIELD_NAME);
	}

	/**
	 * This method will create a database connection using configuration info.
	 * @return Connection : Database connection object
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static Connection getConnection() throws ClassNotFoundException, SQLException
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
		System.out.println("URL : " + url);
		connection = DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
		return connection;
	}

}
