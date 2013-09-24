package com.vayabuzz.androidgames.kittypoker;

import com.vayabuzz.androidgames.framework.Screen;
import com.vayabuzz.androidgames.framework.impl.AndroidGame;

public class KittyPokerGame extends AndroidGame {
	
	// All we need to do is derive from AndroidGame and implement the getStartScreen() method
    //@Override
    public Screen getStartScreen() {
        return new LoadingScreen(this); 
    }
}