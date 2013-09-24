package com.vayabuzz.androidgames.kittypoker;

import com.vayabuzz.androidgames.framework.Game;
import com.vayabuzz.androidgames.framework.Graphics;
import com.vayabuzz.androidgames.framework.Screen;
import com.vayabuzz.androidgames.framework.Graphics.PixmapFormat;
import com.vayabuzz.androidgames.kittypoker.Assets;

// This class loads all assets and the settings

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        // 
        // Images
        //
        Assets.background = g.newPixmap("background_kitty_poker.png", PixmapFormat.RGB565);
        Assets.aceTheCat = g.newPixmap("aceTheCat.png", PixmapFormat.ARGB4444);
        Assets.logo = g.newPixmap("logokittypokerpaws400.png", PixmapFormat.ARGB4444);
        Assets.logoWords = g.newPixmap("kittypokerwords.png", PixmapFormat.ARGB4444);
        Assets.buttons = g.newPixmap("buttonskittypoker.png", PixmapFormat.ARGB4444);
        Assets.mainMenu = g.newPixmap("mainmenukittypoker.png", PixmapFormat.ARGB4444);
        Assets.cards = g.newPixmap("kitty cards 52.png", PixmapFormat.ARGB4444);
        Assets.fabric = g.newPixmap("fabric.png", PixmapFormat.ARGB4444);
        Assets.hands = g.newPixmap("hands.png", PixmapFormat.ARGB4444);
        Assets.exampleHands = g.newPixmap("exampleHands.png", PixmapFormat.ARGB4444);
        Assets.buttonMinus = g.newPixmap("down.png", PixmapFormat.ARGB4444);
        Assets.buttonPlus = g.newPixmap("up.png", PixmapFormat.ARGB4444);
        Assets.hairball = g.newPixmap("sad kitty160w.png", PixmapFormat.ARGB4444);  //g.newPixmap("hairball.png", PixmapFormat.ARGB4444);
        Assets.pointer = g.newPixmap("pointer.png", PixmapFormat.ARGB4444);
        Assets.play = g.newPixmap("play.png", PixmapFormat.ARGB4444);
        Assets.playTextEn = g.newPixmap("play-text-en.png", PixmapFormat.ARGB4444);
        Assets.pauseButton = g.newPixmap("pause.png", PixmapFormat.ARGB4444);
        Assets.resumeExit = g.newPixmap("resume-exit.png", PixmapFormat.ARGB4444);
        Assets.timeWon = g.newPixmap("time won.png", PixmapFormat.ARGB4444);
        Assets.highlight = g.newPixmap("highlight.png", PixmapFormat.ARGB4444);
        Assets.highlightCard = g.newPixmap("highlight-card.png", PixmapFormat.ARGB4444);
        //Assets.time = g.newPixmap("time.png", PixmapFormat.ARGB4444);  // use in later version if needed
        //Assets.timeMultiplier = g.newPixmap("time multiplier.png", PixmapFormat.ARGB4444); // use in later version if needed
        //Assets.timeBest = g.newPixmap("best time.png", PixmapFormat.ARGB4444); // use in later version if needed
        //Assets.letterX = g.newPixmap("x.png", PixmapFormat.ARGB4444); // use in later version if needed

        Assets.helpTextA = g.newPixmap("helpTextA.png", PixmapFormat.ARGB4444);
        Assets.numbers = g.newPixmap("numbers.png", PixmapFormat.ARGB4444);
        Assets.numbersMinus = g.newPixmap("numbers-minus.png", PixmapFormat.ARGB4444);
        Assets.numbersPlus = g.newPixmap("numbers-plus.png", PixmapFormat.ARGB4444);
        Assets.winScore = g.newPixmap("winScore.png", PixmapFormat.ARGB4444);
        Assets.paws = g.newPixmap("paws.png", PixmapFormat.ARGB4444);
        //Assets.gameOver = g.newPixmap("gameover.png", PixmapFormat.ARGB4444);
        
        //
        // Sounds
        //
        Assets.fft = game.getAudio().newSound("fft.ogg");
        Assets.meow1 = game.getAudio().newSound("meow1.ogg");
        Assets.meow2 = game.getAudio().newSound("meow2.ogg");
        Assets.hairballSound = game.getAudio().newSound("hairball.ogg");
        
        //
        // Meow Jazz sounds
        //
        Assets.meowA = game.getAudio().newSound("meowA.ogg");
        Assets.meowB = game.getAudio().newSound("meowB.ogg");
        Assets.meowC = game.getAudio().newSound("meowC.ogg");
        Assets.meowD = game.getAudio().newSound("meowD.ogg");
        Assets.meowE = game.getAudio().newSound("meowE.ogg");
        
        Assets.meowF = game.getAudio().newSound("meowF.ogg");
        Assets.meowG = game.getAudio().newSound("meowG.ogg");
        Assets.meowH = game.getAudio().newSound("meowH.ogg");
        Assets.meowI = game.getAudio().newSound("meowI.ogg");
        Assets.meowJ = game.getAudio().newSound("meowJ.ogg");
        
        Assets.meowB1 = game.getAudio().newSound("meowB1.ogg");
        Assets.meowB2 = game.getAudio().newSound("meowB2.ogg");
        Assets.meowB3 = game.getAudio().newSound("meowB3.ogg");
        Assets.meowB4 = game.getAudio().newSound("meowB4.ogg");
        Assets.meowB5 = game.getAudio().newSound("meowB5.ogg");
        Assets.meowB6 = game.getAudio().newSound("meowB6.ogg");
        Assets.meowB7 = game.getAudio().newSound("meowB7.ogg");
        Assets.meowB8 = game.getAudio().newSound("meowB8.ogg");
        Assets.yeah =  game.getAudio().newSound("yeah1.ogg");
        
        Assets.music = game.getAudio().newMusic("Whiskers'_purr_edit.ogg");
        Assets.music.setLooping(true);
    
        Settings.load(game.getFileIO());
        // Initiate a transition to a Screen called MainMenuScreen, which will take over 
        // execution from this point on...
        game.setScreen(new MainMenuScreen(game));
    }

    // These other methods are just stubs and do not perform any actions. Since the update() 
    // method will immediately trigger a screen transition after all assets are loaded,
    // there's nothing more to do on this screen.
    
    @Override
    public void present(float deltaTime) {

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