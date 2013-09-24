package com.vayabuzz.androidgames.kittypoker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.vayabuzz.androidgames.framework.FileIO;

// Stores our settings and loads/saves them
// LoadingScreen.java invokes this class

public class Settings {
    public static boolean soundEnabled = true; // whether sound effects are played back.
    public static int[] highscores = new int[] { 0, 0, 0, 0, 0 };  // five highest scores, sorted from highest to lowest.
    
    public static void load(FileIO files) {
    	// this assumes the sound setting and high score settings are in separate lines in the
    	// file ".kittypoker"
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    files.readFile(".kittypoker")));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            for (int i = 0; i < 5; i++) {
                highscores[i] = Integer.parseInt(in.readLine());
            }
        } catch (IOException e) {
            // :( It's ok we have defaults
        } catch (NumberFormatException e) {
            // :/ It's ok, defaults save our day
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    public static void save(FileIO files) {
    	// Takes the current settings and serializes them to the .kittypoker file on
    	// external storage (e.g., /sdcard/.kittypoker)
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    files.writeFile(".kittypoker")));
            out.write(Boolean.toString(soundEnabled));
            out.write("\n");
            for (int i = 0; i < 5; i++) {
                out.write(Integer.toString(highscores[i]));
                out.write("\n");
            }

        } catch (IOException e) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }

    public static void addScore(int score) {
    	// for convenience, we use this to add a new score to the high scores,
    	// automatically resorting them depending on the value we want to insert.
        for (int i = 0; i < 5; i++) {
            if (highscores[i] < score) {
                for (int j = 4; j > i; j--)
                    highscores[j] = highscores[j - 1];
                highscores[i] = score;
                break;
            }
        }
    }
}
