package com.vayabuzz.androidgames.kittypoker;

import java.util.List;

import com.vayabuzz.androidgames.framework.Game;
import com.vayabuzz.androidgames.framework.Graphics;
import com.vayabuzz.androidgames.framework.Screen;
import com.vayabuzz.androidgames.framework.Input.TouchEvent;

public class HelpScreen2 extends Screen {

    public HelpScreen2(Game game) {
        super(game);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        Graphics g = game.getGraphics();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > g.getWidth() - 64 && event.y > g.getHeight() - 64 ) {
                    //game.setScreen(new HelpScreen3(game));
                	game.setScreen(new MainMenuScreen(game));
                    if(Settings.soundEnabled)
                        Assets.fft.play(1);
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.exampleHands, 0, 0);
        g.drawPixmap(Assets.buttons, 410, 240, 0, 124, 64, 64);
        //g.drawPixmap(Assets.background, 0, 0);
        //g.drawPixmap(Assets.help2, 64, 100);
        //g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}