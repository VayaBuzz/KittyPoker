package com.vayabuzz.androidgames.kittypoker;

import java.util.List;

import com.vayabuzz.androidgames.framework.Game;
import com.vayabuzz.androidgames.framework.Graphics;
import com.vayabuzz.androidgames.framework.Screen;
import com.vayabuzz.androidgames.framework.Input.TouchEvent;


// The HighScoreScreen draws the top five high scores we store in the Settings class,
// plus a fancy header telling the player that she is on the high scores screen,
// and a button left that will transition back to the main menu when pressed.
public class HighscoreScreen extends Screen {
    String lines[] = new String[5];

    public HighscoreScreen(Game game) {
        super(game);

        for (int i = 0; i < 5; i++) {
            lines[i] = "" + (i + 1) + ". " + Settings.highscores[i];
        }
        // As we want to stay friends with the garbage collector, we store strings
        // of the five high score lines in a string array member. We construct the strings
        // based on the Settings.highscores array in the constructor.
    }

    @Override
    public void update(float deltaTime) {
    	// Awe we do is check for whether a touch-up event hit the button in the bottom-left
    	// corner. If that's the case, we play the click sound and transition back to the 
    	// MainMenuScreen.
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 400 && event.y > 240) {
                    if(Settings.soundEnabled)
                        Assets.fft.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
    	// Render the background image first, as usual,
    	// followed by the "HIGHSCORES" portion of the Asssets.mainmenu image.
    	// We could have stored that in a separate file, but we
    	// reuse it to free up more memory.
    	// Next we loop through the five strings for each high score line we created in the 
    	// constructor. We draw each line with the drawText() method. The first line starts 
    	// at 20,100, the next at 20,150, and so on. We just increase the y coordinate for the text 
    	// rendering by 50 pixels for each line so that we have a nice vertical spacing between
    	// the lines. We finish the method off by drawing our button.
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.background, 0, 0); // draw background
        g.drawPixmap(Assets.mainMenu, 64, 20, 0, 37, 149, 37); // high scores text

        int y = 100;
        for (int i = 0; i < 5; i++) {
            drawText(g, lines[i], 20, y);
            y += 35;
        }
      g.drawPixmap(Assets.buttons, 410, 240, 64, 124, 64, 64); // back button

    }

    // We have two things at our disposal: the numbers.png image and Graphics.drawPixmap(),
    // which allows us to draw portions of an image to the screen.
    // Each digit has a width of 20 pixels, and the "dot" has a width of 10 pixels in the 
    // numbers.png image.
    // Given the coordinates of the upper left corner of our first character in the string,
    // we can loop through each character in the string, draw it, and increment the x-coordinate
    // for the next character to be drawn by either 20 or 10 pixels, depending on the character
    // we just drew.
    public void drawText(Graphics g, String line, int x, int y) {
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char character = line.charAt(i);
    
            if (character == ' ') {
                x += 20;
                continue;
            }
    
            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }
    
            g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
            // This method will of course blow up if our string contains anything
            // other than spaces, digits, or dots. Can you thing of a way to make
            // it work with any string?
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
