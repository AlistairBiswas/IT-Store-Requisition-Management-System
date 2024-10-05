/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafeed
 */
public class DB {
    
    private static Connection connection;
    private static Statement st;
    private static PreparedStatement pst;
    
    public DB() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ProjectDB;selectMethod=cursor", "sa", "123456");
            if (connection != null) {
                //DatabaseMetaData dm = (DatabaseMetaData) connection.getMetaData();
                //System.out.println("Driver name: " + dm.getDriverName());
                //System.out.println("Driver version: " + dm.getDriverVersion());
                //System.out.println("Product name: " + dm.getDatabaseProductName());
                //System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }
            else {
                System.err.println("Could not connect to Database");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet executeQuery(String query) throws SQLException {
        st = connection.createStatement();
        return st.executeQuery(query);
    }
    
    public void updateQuery(String query, String ... keys) throws SQLException {
        pst = connection.prepareStatement(query);
        int i = 1;
        for (String key : keys) {
            pst.setString(i, key);
            ++i;
        }
        pst.executeUpdate();
    }
    
}
