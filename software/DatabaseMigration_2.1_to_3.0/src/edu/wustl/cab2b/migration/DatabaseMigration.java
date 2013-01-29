/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.migration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import edu.wustl.cab2b.util.DatabaseProperties;

public class DatabaseMigration {

    private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    private ResultSet result = null;

    private ResultSet resultSet = null;

    private ResultSet resultSetforQuery = null;

    List<String> queries = new ArrayList<String>();

    Calendar calendar = new GregorianCalendar();
    
    DatabaseProperties databaseProperties = new DatabaseProperties();

    String databaseName = databaseProperties.getDatabaseName();

    public void migrateDatabase(int oldVersion, int newVersion) throws SQLException {
        if (oldVersion == 2 && newVersion == 3) {
            try {
                createExtraTables();
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                databaseConnection.CloseConnection();
            }
        }
    }

    private void createExtraTables() throws SQLException {
        queries.add(SQLCommands.USE_TEMPPORARY_DATABASE);
        result = databaseConnection.getDatabaseMetadata(databaseName, "de_e");
        while (result.next()) {
            String tableName = result.getString("TABLE_NAME");
            StringBuffer query = new StringBuffer("create table if not exists ");
            query.append(tableName).append(" like ").append(databaseName).append(".").append(tableName)
                .append(";");
            queries.add(query.toString());
        }
        copyOldToNew();
    }

    private void copyOldToNew() throws SQLException {
        result = databaseConnection.getDatabaseMetadata(databaseName, "");
        while (result.next()) {
            String tableName = result.getString("TABLE_NAME");
            if ("CAB2B_QUERY".equalsIgnoreCase(tableName) || "CAB2B_SERVICE_URL".equalsIgnoreCase(tableName)
                    || "QUERY_PARAMETERIZED_QUERY".equalsIgnoreCase(tableName)
                    || "QUERY_OUTPUT_ATTRIBUTE".equalsIgnoreCase(tableName)) {
                copyManually(tableName);
            } else {
                StringBuffer insertQuery = new StringBuffer("insert into ");
                insertQuery.append(tableName).append(" select * from ").append(databaseName).append(".");
                insertQuery.append(tableName).append(";");
                queries.add(insertQuery.toString());
            }
        }
        databaseConnection.batchUpdate(queries);
        System.out.println("Old Database copied to New Database");
        createDatabaseCaB2B();
    }

    private void createDatabaseCaB2B() throws SQLException {
        queries.clear();
        queries.add(SQLCommands.DROP_DATABASE_CAB2B);
        queries.add(SQLCommands.CREATE_DATABASE_CAB2B);
        queries.add(SQLCommands.USE_CAB2B_DATABASE);
        result = databaseConnection.getDatabaseMetadata("cab2b_temp", "");
        while (result.next()) {
            String tableName = result.getString("TABLE_NAME");
            StringBuffer createQuery = new StringBuffer("create table ");
            createQuery.append(tableName).append(" like cab2b_temp.").append(tableName).append(";");
            queries.add(createQuery.toString());

            StringBuffer insertQuery = new StringBuffer("insert into ");
            insertQuery.append(tableName).append(" select * from cab2b_temp.").append(tableName).append(";");
            queries.add(insertQuery.toString());
        }
        queries.add(SQLCommands.DROP_DATABASE_CAB2B_TEMP);
        databaseConnection.batchUpdate(queries);
    }

    private void copyManually(String tableName) throws SQLException {
        if ("CAB2B_QUERY".equalsIgnoreCase(tableName)) {
            resultSetforQuery = databaseConnection.executeQuery("select * from CAB2B_QUERY");

            while (resultSetforQuery.next()) {
                int identifier = resultSetforQuery.getInt(1);
                int entityId = resultSetforQuery.getInt(2);
                int userId = resultSetforQuery.getInt(3);
                StringBuffer query1 =
                        new StringBuffer("select * from QUERY_PARAMETERIZED_QUERY where IDENTIFIER = ");
                query1.append(Integer.toString(identifier));
                resultSet = databaseConnection.executeQuery(query1.toString());

                while (resultSet.next()) {
                    int queryIdentifier = resultSet.getInt(1);
                    String queryname = resultSet.getString(2);
                    String description = resultSet.getString(3);
                    java.util.Date createdDate = resultSet.getTimestamp(4);

                    StringBuffer otherQuery = new StringBuffer("insert into QUERY_PARAMETERIZED_QUERY values(");
                    otherQuery.append(queryIdentifier).append(");");
                    queries.add(otherQuery.toString());

                    StringBuffer originalQuery = new StringBuffer("insert into CAB2B_QUERY values(");
                    originalQuery.append(identifier).append(",").append(entityId).append(");");
                    queries.add(originalQuery.toString());

                    StringBuffer query = new StringBuffer("insert into QUERY_ABSTRACT_QUERY values(");
                    query.append(identifier).append(",'").append(queryname).append("','ANDed','")
                        .append(description);
                    query.append("','").append(createdDate).append("',").append(userId).append(");");
                    queries.add(query.toString());
                }
            }
        } else if ("CAB2B_SERVICE_URL".equalsIgnoreCase(tableName)) {
            resultSetforQuery = databaseConnection.executeQuery("select * from CAB2B_SERVICE_URL");
            while (resultSetforQuery.next()) {
                int urlId = resultSetforQuery.getInt(1);
                String entityName = resultSetforQuery.getString(2);
                String url = resultSetforQuery.getString(3);
                boolean adminDefined = resultSetforQuery.getBoolean(4);

                String[] entityGroupName = entityName.split("_v");
                StringBuffer query =
                        new StringBuffer("insert into CAB2B_SERVICE_URL(URL_ID,DOMAIN_MODEL,VERSION,URL,"
                                + "ADMIN_DEFINED) values(");
                query.append(urlId).append(",'").append(entityGroupName[0]).append("','")
                    .append(entityGroupName[1]);
                query.append("','").append(url).append("',").append(adminDefined).append(");");
                queries.add(query.toString());
            }
        } else if ("QUERY_OUTPUT_ATTRIBUTE".equalsIgnoreCase(tableName)) {
            resultSet = databaseConnection.executeQuery("select * from QUERY_OUTPUT_ATTRIBUTE");
            while (resultSet.next()) {
                int identifier = resultSet.getInt(1);
                int expressionId = resultSet.getInt(2);
                int attributeId = resultSet.getInt(3);
                int parameterizedQueryId = resultSet.getInt(4);
                int position = resultSet.getInt(5);

                StringBuffer query =
                        new StringBuffer(
                                "insert into QUERY_OUTPUT_ATTRIBUTE(IDENTIFIER,EXPRESSION_ID,ATTRIBUTE_ID,"
                                        + "PARAMETERIZED_QUERY_ID,POSITION) values (");
                query.append(identifier).append(",").append(expressionId).append(",").append(attributeId);
                query.append(",").append(parameterizedQueryId).append(",").append(position).append(");");
                queries.add(query.toString());
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        new DatabaseMigration().migrateDatabase(2, 3);
    }

}
