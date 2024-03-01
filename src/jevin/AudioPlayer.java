package jevin;

import javax.sound.sampled.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AudioPlayer {
    private Clip clip;
    private boolean isPaused;
    private long clipTimePosition;
    DBConnection connectionCall;
    Connection connect;
    Scanner sc = new Scanner(System.in);
    String musicPath;
    MenuOP mo = new MenuOP();
    public AudioPlayer() {
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

    public AudioPlayer(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null && !clip.isRunning()) {
            if (isPaused) {
                clip.setMicrosecondPosition(clipTimePosition);
                isPaused = false;
            } else {
                clip.start();
            }
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            clipTimePosition = clip.getMicrosecondPosition();
            clip.stop();
            isPaused = true;
        }
    }

    public void resume() {
        if (clip != null && isPaused) {
            clip.start();
            isPaused = false;
        }
    }

    public void restart() {
        if (clip != null) {
            clip.setMicrosecondPosition(0);
            isPaused = false;
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clipTimePosition = 0;
            isPaused = false;
        }
    }

    public void cdPlayer(String location) {

        AudioPlayer player = new AudioPlayer(location);
        player.play();

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Enter your choice:");
            System.out.println("1 - Pause");
            System.out.println("2 - Resume");
            System.out.println("3 - Restart");
            System.out.println("4 - Stop player,Go to Main Menu");


            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    player.pause();
                    break;
                case 2:
                    player.resume();
                    break;
                case 3:
                    player.restart();
                    break;

                case 4:
                    isRunning = false;
                    player.stop();
                    // Stop the audio before exiting
                    mo.Menu();
                    break;
                default:
                    System.out.println("Invalid choice! Please enter a valid option.");
            }
        }

        scanner.close();
    }
    public void searchAndPlay(){
        Catalog ct = new Catalog();
        System.out.println("All songs in the catalog:");
        System.out.println("-----------------------------------------------");
        ct.viewList();
        System.out.println("Enter Search Category - Name , Artist , Genre");

        String CatSearch = sc.nextLine();
        if (CatSearch.equalsIgnoreCase("name")) {
            System.out.println("Enter Song Name from above given Catalog ");
            String songName = sc.nextLine();
            ct.searchByName(songName);
        } else if (CatSearch.equalsIgnoreCase("genre")) {
            System.out.println("Enter Genre from above given Catalog");
            String genreName = sc.nextLine();
            ct.searchByGenre(genreName);
        } else if (CatSearch.equalsIgnoreCase("artist")){
            System.out.println("Enter Artist's Name from above given Catalog");
            String artistName = sc.nextLine();
            ct.searchByArtist(artistName);
        }
        System.out.println("Enter Song ID to start play");
        int sid =sc.nextInt();
        try {
            PreparedStatement ps = connect.prepareStatement("SELECT songpath FROM SongTable WHERE id COLLATE utf8mb4_general_ci = ?");
            ps.setInt(1, sid);
            ResultSet rs =ps.executeQuery();
            if(rs.next()) {
                musicPath = rs.getString("songpath");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cdPlayer(musicPath);

    }

}
