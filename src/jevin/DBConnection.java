package jevin;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
     Connection Con;
    private final String Url = "jdbc:mysql://localhost:3306/this_juke_db";
    private final String UserName = "root";
    private final String PassWord = "JeViN123";
    public Connection getConnection() {



        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Obtain a connection
            Con = DriverManager.getConnection(Url, UserName, PassWord);
           // System.out.println(Con);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Con;
    }



}