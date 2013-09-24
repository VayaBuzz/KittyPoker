package com.vayabuzz.androidgames.kittypoker;

import com.vayabuzz.androidgames.framework.Music;
import com.vayabuzz.androidgames.framework.Pixmap;
import com.vayabuzz.androidgames.framework.Sound;

public class Assets {
	//
	// Images
	//
	public static Pixmap aceTheCat;
    public static Pixmap background;
    public static Pixmap cards;
    public static Pixmap fabric;
    public static Pixmap logo;
	public static Pixmap logoWords;
    public static Pixmap mainMenu;
    public static Pixmap buttons;
    public static Pixmap helpTextA;
    public static Pixmap numbers;
    public static Pixmap numbersMinus;
    public static Pixmap numbersPlus;
    public static Pixmap pause;
    public static Pixmap resumeExit;
    //public static Pixmap gameOver;

    public static Pixmap hands;
    public static Pixmap exampleHands;
    public static Pixmap buttonPlus;
    public static Pixmap buttonMinus;
    public static Pixmap hairball;
    public static Pixmap pointer;
    public static Pixmap play;
    public static Pixmap playTextEn;
    public static Pixmap pauseButton;
    public static Pixmap paws;
    public static Pixmap timeWon;
    public static Pixmap highlight;
    public static Pixmap highlightCard;
    //public static Pixmap time;
    //public static Pixmap timeMultiplier;
    //public static Pixmap timeBest;
    //public static Pixmap letterX; 
    public static Pixmap winScore;
    public static Pixmap catHead;
    
    //
    // Sounds
    //
    public static Sound fft;
    public static Sound meow1;
    public static Sound meow2;
    public static Sound hairballSound;
    
    // meow jazz:
    public static Sound meowA;
    public static Sound meowB;
    public static Sound meowC;
    public static Sound meowD;
    public static Sound meowE;
    public static Sound meowF;
    public static Sound meowG;
    public static Sound meowH;
    public static Sound meowI;
    public static Sound meowJ;
    
    public static Sound meowB1;
    public static Sound meowB2;
    public static Sound meowB3;
    public static Sound meowB4;
    public static Sound meowB5;
    public static Sound meowB6;
    public static Sound meowB7;
    public static Sound meowB8;
    public static Sound yeah;
    
    public static Music music;

    
    // Now we have a static member for every image and sound we load from the assets.
    // If we want to use one of these assets, we can do something like this:
    //    game.getGraphics().drawPixmap(Assets.background,0,0);
    // or something like this:
    //   Assets.click.play(1);
    // A cleaner solution would hide the assets behind setters and getters in a so-called 
    // singleton class. We will stick to our poor man's asset manager, though.
}
