package edu.wustl.cab2b.server.util;

import static edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants.IO_0001;
import static edu.wustl.cab2b.server.path.PathConstants.FIELD_SEPARATOR;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.util.logger.Logger;

/**
 * Bulk data loader for Oracle database.
 * @author Chandrakant Talele
 */
public class OracleLoader implements DataFileLoaderInterface {

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.util.FileDataLoaderInterface#loadDataFromFile(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.Class<?>[])
     */
    public void loadDataFromFile(Connection connection, String fileName, String columns, String tableName,
                                 Class<?>[] dataTypes) {
        Logger.out.debug("Entering method loadDataFromFile()");

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found :" + fileName, e, IO_0001);
        }
        int columnCount = columns.split(",").length;
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ").append(tableName).append(" ").append(columns).append(" values(");
        for (int i = 0; i < columnCount; i++) {
            sql.append("?,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        System.out.println(sql);
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql.toString());
            String oneRecord = "";
            while ((oneRecord = bufferedReader.readLine()) != null) {
                String[] values = oneRecord.split(FIELD_SEPARATOR);
                for (int j = 0; j < columnCount; j++) {
                    if (dataTypes[j].equals(String.class)) {
                        ps.setString(j + 1, values[j]);
                    } else if (dataTypes[j].equals(Long.class)) {
                        ps.setLong(j + 1, Long.parseLong(values[j]));
                    } else if (dataTypes[j].equals(Integer.class)) {
                        ps.setInt(j + 1, Integer.parseInt(values[j]));
                    }
                }
                ps.executeUpdate();
                ps.clearParameters();
            }
            bufferedReader.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.out.debug("Leaving method loadDataFromFile()");
    }
}