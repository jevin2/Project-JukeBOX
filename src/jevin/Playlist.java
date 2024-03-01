package jevin;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Playlist {
    private List<String> songs;
    private Clip clip;
    private int currentIndex;
    private boolean isPaused;
    private boolean isStopped;
    private boolean isPlaying;
    DBConnection connectionCall;
    Connection connect;
    Scanner sc = new Scanner(System.in);

    public Playlist() {
        songs = new ArrayList<>();
        clip = null;
        currentIndex = 0;
        isPaused = false;
        isStopped = true;
        isPlaying = false;

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

    public void addSong(String songPath) {
        songs.add(songPath);
    }

    public void viewSongs() {
        for (String song : songs) {
            System.out.println(song);
        }
    }

    public synchronized void play() {
        if (songs.isEmpty()) {
            System.out.println("No songs in playlist.");
            return;
        }

        if (isStopped) {
            loadClip();
            isStopped = false;
        }

        if (isPaused) {
            clip.start();
            isPaused = false;
            isPlaying = true;
            return;
        }

        if (!isPlaying) {
            clip.setMicrosecondPosition(0);
            clip.start();
            isPlaying = true;
        }
    }

    public synchronized void pause() {
        if (!isPaused && clip != null && clip.isRunning()) {
            clip.stop();
            isPaused = true;
            isPlaying = false;
        }
    }

    public synchronized void resume() {
        if (isPaused) {
            clip.start();
            isPaused = false;
            isPlaying = true;
        }
    }

    public synchronized void restart() {
        if (clip != null) {
            clip.setMicrosecondPosition(0);
            isPlaying = true;
        }
    }

    public synchronized void next() {
        if (currentIndex < songs.size() - 1) {
            clip.stop();
            currentIndex++;
            loadClip();
            play();
        }
    }

    public synchronized void previous() {
        if (currentIndex > 0) {
            clip.stop();
            currentIndex--;
            loadClip();
            play();
        }
    }
    public void stop() {
        clip.stop();
    }



    private void loadClip() {
        try {
            File file = new File(songs.get(currentIndex));
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);          //method that adds a listener to an audio line, enabling actions to be triggered when certain events, here as playback stopping it  occurs.
            clip.addLineListener(event -> { // lambda exp defining what should happen when an event occurs.
                if (event.getType() == LineEvent.Type.STOP) {//checks if the event is "STOP". This means that the audio has stopped playing.
                    isStopped = true;
                    isPlaying = false;
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void playPlaylist() {
        System.out.println("Created Playlists");
        System.out.println("-----------------------------------------------");
        try {
            PreparedStatement ps = connect.prepareStatement("SELECT id , name FROM PlaylistTable");
            ResultSet rs = ps.executeQuery();
              while (rs.next()) {
                  System.out.println("Playlist ID   : " + rs.getInt("id"));
                  System.out.println("Playlist Name : " + rs.getString("name"));
                  System.out.println("-----------------------------------------------");
              }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Enter the ID of playlist to be played ");
        int  n = sc.nextInt();

        ArrayList<String> paths = new ArrayList<>();
        try {                                                 // adding st path to pst path
            PreparedStatement ps = connect.prepareStatement("UPDATE PlaylistSongTable pst INNER JOIN SongTable st ON pst.song_id = st.id SET pst.songPath = st.songPath;");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try  {
                  PreparedStatement ps = connect.prepareStatement(" SELECT pst.songPath FROM PlaylistSongTable pst WHERE pst.playlist_id COLLATE utf8mb4_general_ci = ?");
                  ps.setInt(1,n);
                      ResultSet rs = ps.executeQuery();
                      String songPath;
                      while (rs.next()) {
                          songPath = rs.getString("songPath");
                          paths.add(songPath) ;
                      }

                      rs.close();
        } catch (Exception e) {
                  e.printStackTrace();
        }

        Playlist playlist = new Playlist();
        for (String path : paths) {
            playlist.addSong(path);
        }

        playlist.play();


        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Enter command: p - Pause, r - resume, n - Next, s - Previous, t - Restart, e - Exit");
            String option = scanner.nextLine().toLowerCase();

            switch (option) {
                case "p":
                    playlist.pause();
                    break;
                case "r":
                    playlist.resume();
                    break;
                case "n":
                    playlist.next();
                    break;
                case "s":
                    playlist.previous();
                    break;
                case "t":
                    playlist.restart();
                    break;
                case "e":
                    exit = true;
                    playlist.stop();
                    break;
                default:
                    System.out.println("Invalid option.");
            }

        }
        platlistMenu();
    }



    private void platlistMenu() {
        System.out.println("Enter 1 to go back to main menu");
        System.out.println("Enter 2 to create another PlayList");
        int d =sc.nextInt();
        if(d==1) {
            MenuOP mo = new MenuOP();
            mo.Menu();
        }
        if (d==2){
        createPlaylist();
        }


    }

    public void createPlaylist(){
        sc = new Scanner(System.in);
        int lastId = 0;
        int playListID = 0;
        System.out.println("Enter Playlist name : ");
        String playlistName = sc.nextLine();

        try {
            PreparedStatement ps = connect.prepareStatement("INSERT INTO PlaylistTable (name) VALUES (?)");
            ps.setString(1,playlistName);
            ps.execute();
            System.out.println("-----------------------------------------------");
            System.out.println("Playlist "+playlistName+" created");
            System.out.println("-----------------------------------------------");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("All songs in the catalog:");
        System.out.println("-----------------------------------------------");
        Catalog ct = new Catalog();
        ct.viewList();

        try {
            PreparedStatement ps = connect.prepareStatement("SELECT MAX(id) AS last_id FROM SongTable"); //no. of songs in catalog
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lastId = rs.getInt("last_id");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement ps = connect.prepareStatement("SELECT id FROM PlaylistTable ORDER BY id DESC LIMIT 1");//last added id of playlist table
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                playListID = rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int[] songIDs = new int[lastId];
        int count = 0 ;
        for(int i =0;i< songIDs.length;i++){ //run loop upto lastid thta is .lenght()
            System.out.println("Please enter the song ID to add to your playlist:");
            int songID = sc.nextInt();
            songIDs[i]= songID;
            count++;
            System.out.println("Would u like to add more songs (y/n)?");
            String yn = sc.next();
            if (yn.equals("n")){
                break;
            }
        }
        for (int i =0 ; i < count ; i++) { //run loop upto amount of songs added in that particular PL
            try {
                PreparedStatement ps = connect.prepareStatement("INSERT INTO PlaylistSongTable (playlist_id, song_id) VALUES(?,?)");


                ps.setInt(1, playListID);
                ps.setInt(2, songIDs[i]);

                ps.execute();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }

    }


    }

