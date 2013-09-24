package com.vayabuzz.androidgames.framework;

public abstract class Screen {
    protected final Game game;

    public Screen(Game game) {
        this.game = game;
        // Having a reference to the game allows us:
        // 1) Access to low level modules of the Game to play back audio, draw to the 
        //    screen, get user input, and read/write files.
        // 2) to set a new current Screen by invoking Game.setScreen() when appropriate
        //    (e.g. when a button is pressed that triggers a transition to a new screen.
    }

    public abstract void update(float deltaTime);
        // updates screen state
    public abstract void present(float deltaTime);
        // presents the screen state
    public abstract void pause();
        // called when game is paused.
    public abstract void resume();
        // called when game is resumed
    public abstract void dispose();
        // This is called by the Game instance in case Game.setScreen() is called.
        // The Game instance will dispose of the current Screen via this method and
    	// thereby give the Screen an opportunity to release all its system resources
    	// (e.g., graphical assets stored in Pixmaps) to make room for the new screen's 
    	// resources in memory. The call to the Screen.dispose() method is also the last
    	// opportunity for a screen to make sure that any 
    	// information that needs persistence is saved.
}
