/**
 * 
 */

package edu.wustl.common.util.global;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * To remove the Corrupted association from database.
 * @author prafull_kadam
 *
 */
public class QueryMetadataCleanup
{

	private static Connection con = null;
	private static Statement stmt = null;
	static private BufferedWriter writer = null;

	/**
	 * 
	 * @param args The database connecttion related parameters:
	 * 	- User Name
	 * 	- Password
	 * 	- driver
	 * 	- Connection URL
	 * 	
	 */
	public static void main(String[] args)
	{
		if (args.length != 4)
		{
			System.out.println("Incorrect no of Parameners !!!");
			throw new RuntimeException("Incorrect no of Parameners !!!");
		}
		try
		{
			createConnection(args);
			writer = new BufferedWriter(new FileWriter("./MetadataCleanupLog.txt"));
			cleanup();
			writer.close();
			System.out
					.println("Please refer MetadataCleanupLog.txt for the paths/association removed from database.");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			if (con != null)
				try
				{
					con.close();
				}
				catch (SQLException e)
				{
					throw new RuntimeException(e);
				}
		}

	}

	/**
	 * TO create Connection object.
	 * @param args
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private static void createConnection(String[] args) throws SQLException,
			InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		String userName = args[0];
		String password = args[1];

		String driver = args[2];
		String url = args[3];
		Class.forName(driver).newInstance();
		System.out.println("Database Parameters Passed:");
		System.out.println("userName:" + userName);
		System.out.println("password:" + password);
		System.out.println("driver:" + userName);
		System.out.println("url:" + url);
		con = DriverManager.getConnection(url, userName, password);
		//		con.setAutoCommit(false);
		stmt = con.createStatement();
	}

	/**
	 * Removes entries from path & intramodel association table.
	 * @throws SQLException
	 * @throws IOException
	 */
	private static void cleanup() throws SQLException, IOException
	{
		StringBuffer deletedRecords = new StringBuffer("\nintra_model_association:");
		String corruptedAssociationSql = "select ASSOCIATION_ID, DE_ASSOCIATION_ID From intra_model_association where de_association_id not in (select IDENTIFIER from dyextn_association)";
		ResultSet rs = stmt.executeQuery(corruptedAssociationSql);
		Set<Long> intraModelAssociationIds = new HashSet<Long>();
		while (rs.next())
		{
			deletedRecords.append("\n").append(rs.getLong(1)).append((",")).append(rs.getLong(2));
			intraModelAssociationIds.add(rs.getLong(1));
		}
		rs.close();
		writer.write("\nTotal Corrupted DE AssociationIds: " + intraModelAssociationIds.size());
		if (intraModelAssociationIds.isEmpty())
		{
			return;
		}
		StringBuffer pathDelSQL = new StringBuffer("delete from path where path_id in (");
		StringBuffer associationDelSQL = new StringBuffer(
				"delete from intra_model_association where ASSOCIATION_ID in (");
		Map<Long, String> entityNameMap = getEntityNameMap();
		int pathCnt = 0;
		writer.write("\n------------------------------------");
		writer.write("\nPaths removed: ");
		deletedRecords.append("\npath:");
		for (Long id : intraModelAssociationIds)
		{
			associationDelSQL.append(id).append(",");
			String pathSql = "select PATH_ID,FIRST_ENTITY_ID,INTERMEDIATE_PATH,LAST_ENTITY_ID from PATH where INTERMEDIATE_PATH like '%"
					+ id + "%'";
			rs = stmt.executeQuery(pathSql);
			while (rs.next())
			{
				String path = rs.getString(3);
				boolean b = isPresentInPath(id, path);
				if (b)
				{
					pathDelSQL.append(rs.getLong(1)).append(",");
					pathCnt++;
					writer.write("\n" + entityNameMap.get(rs.getLong(2)) + "--->"
							+ entityNameMap.get(rs.getLong(4)));
					deletedRecords.append("\n").append(rs.getLong(1)).append((",")).append(
							rs.getLong(2)).append((",")).append(rs.getString(3)).append((","))
							.append(rs.getLong(4));
					//					writer.write("\n"+id+":"+ rs.getLong(1) +":" +path+":" + entityNameMap.get(rs.getLong(2))+"--->"+entityNameMap.get(rs.getLong(4)));
				}

			}
		}

		writer.write("\n------------------------------------");
		writer.write("\n Total Paths Corrupted:" + pathCnt);
		writer.write("\n------------------------------------");
		writer.write("\nExecuting SQL:");
		String sql = null;
		if (pathCnt != 0)
		{
			sql = pathDelSQL.substring(0, pathDelSQL.length() - 1) + ")";
			writer.write("\n"+sql);
			stmt.executeUpdate(sql);
		}

		sql = associationDelSQL.substring(0, associationDelSQL.length() - 1) + ")";
		writer.write("\n"+sql);
		stmt.executeUpdate(sql);
		writer.write("\nDeleted following records from corresponding table: ");
		writer.write(deletedRecords.toString());
		writer.write("\n------------------------------------");

		stmt.close();

	}

	/**
	 * To check whether given association id is present in the given path or not.
	 * @param id The intramodel association id.
	 * @param path The string representing intermediate path.
	 * @return
	 */
	private static boolean isPresentInPath(Long id, String path)
	{
		String[] splitArray = path.split("_");
		boolean b = false;
		for (int index = 0; index < splitArray.length; index++)
		{
			if (splitArray[index].equals(id.toString()))
			{
				b = true;
			}
		}
		return b;
	}

	/**
	 * Create map of entity id verses entity Name.
	 * @return The map of entity id verses entity Name.
	 * @throws SQLException
	 */
	private static Map<Long, String> getEntityNameMap() throws SQLException
	{
		Map<Long, String> entityNameMap = new HashMap<Long, String>();
		ResultSet rs = stmt
				.executeQuery("select IDENTIFIER, NAME from dyextn_abstract_metadata WHERE CREATED_DATE IS NOT NULL");
		while (rs.next())
		{
			entityNameMap.put(rs.getLong(1), rs.getString(2));
		}
		rs.close();
		return entityNameMap;
	}
}
