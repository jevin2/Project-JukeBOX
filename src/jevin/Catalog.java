package jevin;

import java.sql.*;
import java.util.Scanner;

public class Catalog {

    DBConnection connectionCall;
    static Connection connect;

    public Catalog() {
        try {
            connectionCall = new DBConnection();
            connect = connectionCall.getConnection();

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void viewList() {
        try {
            PreparedStatement ps = connect.prepareStatement(" SELECT id, name ,artist,album,genre FROM SongTable;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Song ID : " + rs.getInt("id"));
                System.out.println("Song Name : " + rs.getString("name"));
                System.out.println("Artist : " + rs.getString("artist"));
                System.out.println("Album : " + rs.getString("album"));
                System.out.println("Genre : " + rs.getString("genre"));
                System.out.println("-----------------------------------------------");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void searchByName(String s) {


        try {
            PreparedStatement ps = connect.prepareStatement("SELECT id,name ,artist,genre FROM SongTable WHERE name COLLATE utf8mb4_general_ci = ?");
            // Set the parameter value (user input)
            ps.setString(1, s);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // Process the results
            while (rs.next()) {
                System.out.println("Song ID : " + rs.getInt("id"));
                System.out.println("Song Name : " + rs.getString("name"));
                System.out.println("Artist : " + rs.getString("artist"));
                System.out.println("Genre : " + rs.getString("genre"));
                System.out.println("-----------------------------------------------");


                // Retrieve data from each row
                // Example: String value = resultSet.getString("column_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void searchByGenre(String g) {


        try {
            PreparedStatement ps = connect.prepareStatement("SELECT id,name ,artist,genre FROM SongTable WHERE genre COLLATE utf8mb4_general_ci = ?");
            // Set the parameter value (user input)
            ps.setString(1, g);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // Process the results
            while (rs.next()) {
                System.out.println("Song ID : " + rs.getInt("id"));
                System.out.println("Song Name : " + rs.getString("name"));
                System.out.println("Artist : " + rs.getString("artist"));
                System.out.println("Genre : " + rs.getString("genre"));
                System.out.println("-----------------------------------------------");


                // Retrieve data from each row
                // Example: String value = resultSet.getString("column_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void searchByArtist(String a) {
        try {
            PreparedStatement ps = connect.prepareStatement("SELECT id, name ,artist,genre FROM SongTable WHERE artist COLLATE utf8mb4_general_ci = ?");
            // Set the parameter value (user input)
            ps.setString(1, a);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // Process the results
            while (rs.next()) {
                System.out.println("Song ID : " + rs.getInt("id"));
                System.out.println("Song Name : " + rs.getString("name"));
                System.out.println("Artist : " + rs.getString("artist"));
                System.out.println("Genre : " + rs.getString("genre"));
                System.out.println("-----------------------------------------------");


                // Retrieve data from each row
                // Example: String value = resultSet.getString("column_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


