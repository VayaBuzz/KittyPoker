package com.vayabuzz.androidgames.kittypoker;

import java.util.List;

import com.vayabuzz.androidgames.framework.Game;
import com.vayabuzz.androidgames.framework.Graphics;
import com.vayabuzz.androidgames.framework.Input.TouchEvent;
import com.vayabuzz.androidgames.framework.Screen;

// The first of three help screens

public class HelpScreen extends Screen {      
    public HelpScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
    	// This method simply checks if the button at the bottom was pressed.
    	// If that's the case, we play the click sound and transtion to HelpScreen2
    	//
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(inBounds(event, 400, 240, 68, 68) ) {
                    game.setScreen(new HelpScreen2(game));
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
        
    @Override
    public void present(float deltaTime) {
    	// Just renders the background again, followed by the help image and the button.
        Graphics g = game.getGraphics();      
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.helpTextA, 22, 22);
        g.drawPixmap(Assets.buttons, 410, 240, 0, 124, 64, 64);
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