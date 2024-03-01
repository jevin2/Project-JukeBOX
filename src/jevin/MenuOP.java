package jevin;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MenuOP {
    DBConnection connectionCall;
    Scanner sc;
    static Connection connect;
    public MenuOP() {
        try
        {
            connectionCall = new DBConnection();
            connect = connectionCall.getConnection();

        }
        catch (Exception ee)
        {
            ee.printStackTrace();
        }
    }
//      public void addSongstoPlaylist(){
//          int lastId = 0;
//          int playListID = 0;
//          System.out.println("Enter Playlist name : ");
//          String playlistName = sc.nextLine();
//
//          try {
//              PreparedStatement ps = connect.prepareStatement("INSERT INTO PlaylistTable (name) VALUES (?)");
//              ps.setString(1,playlistName);
//              ps.execute();
//              System.out.println("-----------------------------------------------");
//              System.out.println("Playlist "+playlistName+" created");
//              System.out.println("-----------------------------------------------");
//          } catch (SQLException e) {
//              throw new RuntimeException(e);
//          }
//
//          System.out.println("All songs in the catalog:");
//          System.out.println("-----------------------------------------------");
//          Catalog ct = new Catalog();
//          ct.viewList();
//
//          try {
//              PreparedStatement ps = connect.prepareStatement("SELECT MAX(id) AS last_id FROM SongTable"); //no. of songs in catalog
//              ResultSet rs = ps.executeQuery();
//              if (rs.next()) {
//                  lastId = rs.getInt("last_id");
//                  //System.out.println(lastId);
//              }
//          } catch (Exception e) {
//              e.printStackTrace();
//          }
//          try {
//              PreparedStatement ps = connect.prepareStatement("SELECT id FROM PlaylistTable ORDER BY id DESC LIMIT 1");//last added id of playlist table
//              ResultSet rs = ps.executeQuery();
//              if (rs.next()) {
//                  playListID = rs.getInt("id");
//              }
//          } catch (Exception e) {
//            e.printStackTrace();
//          }
//
//          int[] songIDs = new int[lastId];
//          int count = 0 ;
//          for(int i =0;i< songIDs.length;i++){
//              System.out.println("Please enter the song ID to add to your playlist:");
//              int songID = sc.nextInt();
//              songIDs[i]= songID;
//              count++;
//              System.out.println("Would u like to add more songs (y/n)?");
//              String yn = sc.next();
//              if (yn.equals("n")){
//                  break;
//              }
//          }
//          for (int i =0 ; i < count ; i++) {
//              try {
//                  PreparedStatement ps = connect.prepareStatement("INSERT INTO PlaylistSongTable (playlist_id, song_id) VALUES(?,?)");
//
//
//                  ps.setInt(1, playListID);
//                  ps.setInt(2, songIDs[i]);
//
//                  ps.execute();
//              }
//              catch(SQLException e){
//                  e.printStackTrace();
//              }
//          }
//
//      }
    public void Menu() {
        Playlist pl = new Playlist();
        sc = new Scanner(System.in);


        System.out.println("Enter 1 to Create a Playlist \nEnter 2 to Search and play a song \nEnter 3 to Search Playlist\nEnter 4 to exit JukeBox ");
        int n = sc.nextInt();
        sc.nextLine();


        if ( n==1 ){
            pl.createPlaylist();

            System.out.println("Enter 1 to play songs from created playlists \nEnter 2 to go back to main menu ");
            int m = sc.nextInt();
            sc.nextLine();
            if (m == 1){
            pl.playPlaylist();
            }
            if (m==2){
                Menu();
            }
        }


        else if (n ==2) {
            AudioPlayer ap = new AudioPlayer();
            ap.searchAndPlay();
        }


        else if (n == 3){
            pl.playPlaylist();


        }
        else if (n==4){
            System.out.println("Closing Jukebox....");
        }

    }


}
