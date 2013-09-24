package com.vayabuzz.androidgames.kittypoker;

import java.util.List;

import com.vayabuzz.androidgames.framework.Game;
import com.vayabuzz.androidgames.framework.Graphics;
import com.vayabuzz.androidgames.framework.Input.TouchEvent;
import com.vayabuzz.androidgames.framework.Screen;
import com.vayabuzz.androidgames.kittypoker.Assets;

// The main menu screen is called by LoadingScreen.java. It's pretty dumb. It just renders 
// the logo, the main menu options, and the sound setting in the form of a toggle button.
// To implement this behavior we need to know two things:
//    where on the screen we render the images and what the touch areas are that will
//    either trigger a screen transition or toggle the sound settings.
// Target Resolution 480x320 pixels

public class MainMenuScreen extends Screen {
	// We let the class derive from Screen again and implement an adequate constructor for it.
    public MainMenuScreen(Game game) {
        super(game);               
    }   

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();   // We don't use the key events, but we fetch them anyway
        								// in order to clear the internal buffer (yes, that's a bit
        								// nasty, but let's make it a habit).
        
        // We then loop over all the TouchEvents until we find one with the type touchEvent.TOUCH_UP
        // In most UIs the Up event is use to indicate that a UI component was pressed.
        
        // Once we have a up touch event, we check whether it either hit the sound toggle button
        // or one of the menu entries, using the included inBounds() method.
        
        // If a sound toggle button is hit, we invert the Settings.soundEnabled boolean value.
        // In case any of the main menu entries are hit, we transition to the appropriate screen
        // by instancing it and setting it via Game.setScreen(). We can immediately return in
        // that case, as the MainMenuScreen doesn't have anythign to do anymore. We also play
        // the click sounds if either the toggle button or a main menu entry is hit and sound is
        // enabled.
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled)
                        Assets.meow2.play(1);
                }
                if(inBounds(event, 321, 168, 149, 38) ) {
                    game.setScreen(new HelpScreen(game));
                    if(Settings.soundEnabled)
                        Assets.fft.play(1);
                    return;
                }
                if(inBounds(event, 321, 168 + 38, 149, 38) ) {
                    game.setScreen(new HighscoreScreen(game));
                    if(Settings.soundEnabled)
                        Assets.fft.play(1);
                    return;
                }
                if(inBounds(event, 400, 240, 149, 68) ) {
                    game.setScreen(new GameScreen(game));
                    if(Settings.soundEnabled)
                        Assets.fft.play(1);
                    return;
                }
            }
        }
    }
    
    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
    	// Put in a touch event and a rectangle, and it tells you whether the 
    	// touch event's coordinates are inside the rectangle.
        if(event.x > x && event.x < x + width - 1 && 
           event.y > y && event.y < y + height - 1) 
            return true;
        else
            return false;
    }

    @Override   // Overrides the Screen class present()
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        
        // Render the background at 0,0, which will basically erase our frame buffer,
        //so no call to Graphics.clear() is needed. 
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.logo, 10, 32);
        g.drawPixmap(Assets.logoWords, 290, 5);
        g.drawPixmap(Assets.aceTheCat, 200,305);
        g.drawPixmap(Assets.mainMenu, 321,168);
        //
        // Draw Play (Draw) button
        //
        g.drawPixmap(Assets.play, 400,240);
        g.drawPixmap(Assets.playTextEn, 407,260);
        // Draw Sound Enabled/Disabled buttons:
        if(Settings.soundEnabled)
            g.drawPixmap(Assets.buttons, 2, 250, 0, 0, 63, 64);// g.drawPixmap(Assets.buttons, 0, 250, 0, 0, 64, 64);
        else
            g.drawPixmap(Assets.buttons, 2, 250, 62, 0, 64, 64);
    }

    @Override
    public void pause() {    
    	// Persist to external storage
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {
    	// Nothing to do on this screen
    }

    @Override
    public void dispose() {
    	// Nothing to do on this screen.
    }
}
