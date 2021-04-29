package deeplearningGroup1;

/**
 * @author Timothy Mitchell
 *
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnection {
	
    // Connect to the egs database.
    public String connect() {
    	String connectionUrl =
                "jdbc:sqlserver://egs.database.windows.net:1433;"
                        + "database=egs;"
                        + "user=egs;"
                        + "password=Grading2019!;"
                        + "encrypt=true;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";
    	return connectionUrl;
    }
    
    /*public void saveData (double[][] data, String table) {
    	
    }*/
}