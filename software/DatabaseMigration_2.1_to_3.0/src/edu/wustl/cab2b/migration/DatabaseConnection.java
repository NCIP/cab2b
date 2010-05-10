package edu.wustl.cab2b.migration;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.wustl.cab2b.util.DatabaseProperties;



public class DatabaseConnection {

    private Connection connection = null;

    private Statement statement = null;

    private ResultSet resultSet = null;

    private static DatabaseConnection databaseConnection = null;
    
    DatabaseProperties databaseProperties = new DatabaseProperties();

    String databaseServer = databaseProperties.getDatabaseServer();

    String databaseServerPort = databaseProperties.getDatabaseServerPort();

    String databaseName = databaseProperties.getDatabaseName();

    String databaseUserName = databaseProperties.getDatabaseUserName();

    String databasePassword = databaseProperties.getDatabasePassword();

    StringBuffer connectionUrl = new StringBuffer("jdbc:mysql://");

    private DatabaseConnection() {
        connectionUrl.append(databaseServer).append(":").append(databaseServerPort).append("/");
    }

    public static DatabaseConnection getInstance() {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
            databaseConnection.OpenConnection();
        }
        return databaseConnection;
    }

    private void OpenConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            connection = DriverManager.getConnection(connectionUrl.toString() + databaseName, databaseUserName, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OpenConnectionforCab2b() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionUrl.toString() + "cab2b_temp", databaseUserName, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CloseConnection() throws SQLException {
        resultSet.close();
        statement.close();
        connection.close();
    }

    public ResultSet executeQuery(String query) throws SQLException {
        getInstance();
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        return resultSet;
    }

    public void executeUpdate(String query) throws SQLException {
        getInstance();
        statement = connection.createStatement();
        System.out.println("Executing Query:" + query);
        statement.executeUpdate(query);
        resultSet.close();
        statement.close();
    }

    public void batchUpdate(List<String> queries) throws SQLException {
        getInstance();
        statement = connection.createStatement();
        connection.setAutoCommit(true);
        for (String query : queries) {
            statement.addBatch(query);
        }
        statement.executeBatch();
        resultSet.close();
        statement.close();
    }

    public ResultSet getDatabaseMetadata(String databaseName, String tablePattern) throws SQLException {
        if (this.databaseName.equalsIgnoreCase(databaseName)) {
            getInstance();
        } else {
            OpenConnectionforCab2b();
        }
        statement = connection.createStatement();
        DatabaseMetaData databaseMetadata = connection.getMetaData();
        String[] type = { "Table" };
        return databaseMetadata.getTables(null, null, "%" + tablePattern + "%", type);
    }
}
