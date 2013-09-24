package com.vayabuzz.androidgames.kittypoker;

import java.util.List;
import java.util.Random;


import com.vayabuzz.androidgames.framework.Game;
import com.vayabuzz.androidgames.framework.Graphics;
import com.vayabuzz.androidgames.framework.Input.TouchEvent;
import com.vayabuzz.androidgames.framework.Screen;
import com.vayabuzz.androidgames.kittypoker.WorldStateEnum;
import com.vayabuzz.androidgames.kittypoker.World.WorldListener;

public class GameScreen extends Screen {  // 480x320 size optimized. Yes, sorry, small. Will work on this.
    enum GameState {
        //Ready,
        Running,
        Paused,
        GameOver
    }
    
    GameState state = GameState.Running; 
    World world;
    WorldListener worldListener;
    WorldStateEnum worldState; //keeps track of world's current state
    
    // Numbers to keep track of during gameplay:
    int oldScore = 0;
    String score = "0";
    
    int oldBet = 0;
    String bet = "0";
    
    int oldWinAmount = 0;
    String winAmount = "0";
    
    int oldHandWorth = 0;   
    String handWorth = "0";
    
    //float oldTimeLeft = 0.0f;
    //String timeLeft = "0";
    
	Random r = new Random(); // for random x position of hairball image
	int hairX;  // the random x position of the hairball image
	int meowCounter = 0;  // counts meows in awaiting bet stage
	boolean meowJazz = true;  // keep this set to true as long as player touches cards in the correct order before initial round is drawn.
	
    //oldScore = world.score;
    //score = "" + oldScore;
    // The reason we have the last two members is that we don't want to constantly create
    // new strings from the World.score member each time we draw the score. Instead we will
    // cache the string and only create a new one when the score changes. That way we play
    // nice with the garbage collector.
    
    public GameScreen(Game game) {
        super(game);
        
        worldListener = new WorldListener() {  // an anonymous inner class it's registered with the 
			// World instance and will play back sound effects according to
			// the event that gets reported to it.
        	public void purr() {            
                if(Settings.soundEnabled)
                	Assets.music.setVolume(0.09f);
        	}
        	public void stopPurr() {            
                if(Settings.soundEnabled)
                	Assets.music.setVolume(0.0f);
        	}
                    
       	};
        
        world = new World(worldListener);
        
        //
        // Set background purring
        //
        Assets.music.setVolume(0.0f);  // only increase volume if winning hand is made
        if(Settings.soundEnabled)
        	 Assets.music.play();
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        //if(state == GameState.Ready)
        //    updateReady(touchEvents);
        if(state == GameState.Running)
            updateRunning(touchEvents, deltaTime);
        else if(state == GameState.Paused)
            updatePaused(touchEvents);
        else if(state == GameState.GameOver)
            updateGameOver(touchEvents);          
    }
    
    //private void updateReady(List<TouchEvent> touchEvents) {
    //    if(touchEvents.size() > 0)
    //        state = GameState.Running;
    //}
    
    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {   
    	// updateRunning contains the "C" of MVC
        int len = touchEvents.size();
        
        for(int i = 0; i < len; i++) {
        	//
        	// Check for Pause Button pressed
        	//
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 430 && event.x < 490 && event.y > 10 && event.y < 60) {
                    if(Settings.soundEnabled)
                        Assets.fft.play(1);  	
                    state = GameState.Paused;
                    return;
                }
            }
            // 
            // Detect player interaction other than pause
            //
            if(event.type == TouchEvent.TOUCH_DOWN) {

            	worldState = world.getWorldState();
            	
            	//
            	// "Draw button" detection   //pause
            	//
                if(event.x > 400 && event.x < 468 && event.y > 240 && event.y < 310) {  // "Draw" button

            		switch (worldState) {
            		case AwaitingBet:
            			world.hand.drawCards();
                		world.changeWorldState(); // change to first round dealt
                		break;
            		case FirstRoundDealt:
            			world.hand.drawCards();
                		world.changeWorldState(); // change to second round dealt
                		world.dealTime = 0;  // reset the clock to zero and start slowly dealing out the new cards if necessary
                		//world.waitTime = 0.15f;
                		world.hand.resetShowCardsForRound2();
                		world.resetTimeMultiplier();
                		
                   		// Update the x position of hairball
                		hairX = r.nextInt(300-100) + 40;
                		break;
                		// second round dealt code here:
            		case SecondRoundDealt:

                		world.setWorldState(WorldStateEnum.NewRound); // change to new round so we can have a little pause
                		break;
                		
            		default:
            			break;
               
            		}
                } // end "Draw" button detection
                
                // Increase and Decrease bet button is not used for now
                
                /*
            	//
            	// "Decrease bet" button detection
            	//  210,260
                if(event.x > 50 && event.x < 100 && event.y > 260 && event.y < 310) {	
            		switch (worldState) {
            		case AwaitingBet:
            			world.decreaseBet();
                		if(Settings.soundEnabled)
                			Assets.fft.play(1);
                		break;
            		case SecondRoundDealt:
            			world.decreaseBet();
            			if(Settings.soundEnabled)
            				Assets.fft.play(1);
            			world.changeWorldState();  // go to awaiting bet screen
            			break;                		
            		default:
            			break;
            		}
                } // end "Decrease bet" button detection
                
            	//
            	// "Increase bet" button detection
            	//  210,260
                if(event.x > 113 && event.x < 163 && event.y > 260 && event.y < 310) {
            		switch (worldState) {
            		case AwaitingBet:
            			world.increaseBet();
                		if(Settings.soundEnabled)
                			Assets.fft.play(1);
                		break;
            		case SecondRoundDealt:
            			world.increaseBet();
            			if(Settings.soundEnabled)
            				Assets.fft.play(1);
            			world.changeWorldState();  // go to awaiting bet screen
            			break;
            		default:
            			break;
            		}
                } // end "Increase bet" button detection
                */

                //
                // Detect if any of five cards is pressed to Hold it
                //
                int xStart = 10;
                int xEnd = 90;
                for(int h = 0; h < 5; h++) {
                	if(event.x > xStart && event.x < xEnd && event.y > 90 && event.y < 210) {	
                		if (worldState == WorldStateEnum.FirstRoundDealt){
                			if(Settings.soundEnabled)
                				Assets.fft.play(1);
                			world.hand.updateHoldCard(h);
                		}
                	}
                	xStart+=90;
                	xEnd+=90;
                } // end for - detect any of five cards pressed
                
                //
                // Detect if any of five cards is pressed to Play silly jazz meow.
                //
                if (worldState == WorldStateEnum.AwaitingBet){
                	if (event.y > 90 && event.y < 210) {
                		if(event.x > 10 && event.x < 90){	// Card 1
                			meowCounter++;
                			if(Settings.soundEnabled){
                				if (meowJazz == true){
                					switch (meowCounter){
                						case 1:
                						case 9:
                						case 25:
                						case 41:
                						case 49: 
                							Assets.meowA.play(1);
                							break;
                						case 17: 
                							Assets.meowF.play(1);
                							break;
                						case 33: 
                							Assets.meowB1.play(1);
                							break;
                						case 37: 
                							Assets.meowB5.play(1);
                							break;
                						case 50:
                							meowCounter = 0; // if they make it to 50, restart counter so they can start over.
                							Assets.yeah.play(1);
                							break;
                						default:
                							meowJazz = false;
                							Assets.hairballSound.play(1);
                							break;
                					}//switch
                				}else{
                					Assets.hairballSound.play(1);
                				}
                			}// soundEnabled
                		}//(event.x > 10 && event.x < 90){
            			if(event.x > 100 && event.x < 180){	// Card 2
                			meowCounter++;
                			if(Settings.soundEnabled){
                				if (meowJazz == true){
                					switch (meowCounter){
                						case 2:
                						case 8:
                						case 10:
                						case 16:
                						case 26:
                						case 32:
                						case 42:
                						case 48:
                							Assets.meowB.play(1);
                							break;
                						case 18:
                						case 24:
                							Assets.meowG.play(1);
                							break;
                						case 34: 
                							Assets.meowB2.play(1);
                							break;
                						case 38: 
                							Assets.meowB6.play(1);
                							break;
                						default:
                							meowJazz = false;
                							Assets.hairballSound.play(1);
                							break;
                					}//switch
                				}else{
                					Assets.hairballSound.play(1);
                				}
                			}// soundEnabled
            			}
        				if(event.x > 190 && event.x < 270){	// Card 3
                			meowCounter++;
                			if(Settings.soundEnabled){
                				if (meowJazz == true){
                					switch (meowCounter){
                						case 3:
                						case 7:
                						case 11:
                						case 15:
                						case 27:
                						case 31:
                						case 43:
                						case 47:
                							Assets.meowC.play(1);
                							break;
                						case 19:
                						case 23:
                							Assets.meowH.play(1);
                							break;
                						case 35: 
                							Assets.meowB3.play(1);
                							break;
                						case 39: 
                							Assets.meowB7.play(1);
                							break;
                						default:
                							meowJazz = false;
                							Assets.hairballSound.play(1);
                							break;
                					}//switch
                				}else{
                					Assets.hairballSound.play(1);
                				}
                			}// soundEnabled
        				}
    					if(event.x > 280 && event.x < 360){		// Card 4
                			meowCounter++;
                			if(Settings.soundEnabled){
                				if (meowJazz == true){
                					switch (meowCounter){
                						case 4:
                						case 6:
                						case 12:
                						case 14:
                						case 28:
                						case 30:
                						case 44:
                						case 46:
                							Assets.meowD.play(1);
                							break;
                						case 20:
                						case 22:
                							Assets.meowI.play(1);
                							break;
                						case 36: 
                							Assets.meowB4.play(1);
                							break;
                						case 40: 
                							Assets.meowB8.play(1);
                							break;
                						default:
                							meowJazz = false;
                							Assets.hairballSound.play(1);
                							break;
                					}//switch
                				}else{
                					Assets.hairballSound.play(1);
                				}
                			}// soundEnabled
    					}
    					if(event.x > 370 && event.x < 450){		// Card 5
                			meowCounter++;
                			if(Settings.soundEnabled){
                				if (meowJazz == true){
                					switch (meowCounter){
                						case 5:
                						case 13:
                						case 29:
                						case 45:
                							Assets.meowE.play(1);
                							break;
                						case 21:
                							Assets.meowJ.play(1);
                							break;
                						default:
                							meowJazz = false;
                							Assets.hairballSound.play(1);
                							break;
                					}//switch
                				}else{
                					Assets.hairballSound.play(1);
                				}
                			}// soundEnabled
    					}
                	} //if (event.y > 90 && event.y < 210) {
                }  // AwaitingBet
                
                
            } // end if (event.type == TouchEvent.TOUCH_DOWN) 
        } // end for(int i = 0; i < len; i++) {
        
        world.update(deltaTime);
        //if(world.gameOver) {
        //    if(Settings.soundEnabled)
        //        Assets.fft.play(1);
        //    state = GameState.GameOver;
        //}
        //
        // Update the numbers
        // 
        if(oldScore != world.score) {
            oldScore = world.score;
            score = "" + oldScore;
        }
        /*
        if(oldBet != world.bet) {
            oldBet = world.bet;
            bet = "" + oldBet;

        }
        */
        if(oldWinAmount != world.winAmount) {
        	oldWinAmount = world.winAmount;
            winAmount = "" + oldWinAmount;

        }
        
        if(oldHandWorth != world.hand.getCurrentHandWorth()) {
        	oldHandWorth = world.hand.getCurrentHandWorth();
            handWorth = "" + oldHandWorth;

        }
        
    }
    
    
    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 429 && event.x <= 479) {
                    if(event.y > 10 && event.y < 61) {
                        if(Settings.soundEnabled)
                            Assets.fft.play(1);
                        state = GameState.Running;
                        return;
                    }                    
                    if(event.y > 238 && event.y < 300) {
                        if(Settings.soundEnabled){
                            Assets.fft.play(1);
                            Assets.music.setVolume(0.0f);
                        }
                        // Add this score to the high scores queue before exiting the game.
                        Settings.addScore(world.score);
                        Settings.save(game.getFileIO());
                    	meowCounter = 0;  // counts meows in awaiting bet stage
                    	meowJazz = true;  // keep this set to true as long as player touches cards in right order.
                        game.setScreen(new MainMenuScreen(game));                        
                        return;
                    }
                }
            }
        }
    }
    /*
    private void updatePaused(List<TouchEvent> touchEvents) {
        //if(Settings.soundEnabled)
       	// Assets.music.pause();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	if(event.x > 430 && event.x < 480){
                    if(event.y > 10 && event.y < 61) {
                        if(Settings.soundEnabled){
                            Assets.fft.play(1);
                        	Assets.music.setVolume(0.2f);
                        }
                        state = GameState.Running;
                        return;
                    }                    
                }
            }
        }
    }
    */
    
    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                //if(event.x >= 62 && event.x <= 162 && event.y >= 100 && event.y <= 264) {
                if(event.x >= 62 && event.x <= 162 && event.y >= 0) {
                    if(Settings.soundEnabled)
                        Assets.fft.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }
    

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   

    @Override
    public void present(float deltaTime) {
        
        if(state == GameState.Running)
            drawRunningUI();  // if you comment this out the pause/resume screen won't show up.
        else if(state == GameState.Paused)
            drawPausedUI();
        //if(state == GameState.GameOver)
        //    drawGameOverUI();
        
    }
    
    //private void drawWorld(World world) {
    private void drawWorld() {
        Graphics g = game.getGraphics();
        int x,y;  // used for position of cards on screen
        x = 12; // xpos of card on screen-- increments by 90 at end of loop
        y = 92;
        
        g.drawPixmap(Assets.background, 0, 0);
        
        for(int c = 0; c < 5; c++) {
        	g.drawPixmap(Assets.fabric, x, y);
        	x+=90; // move 90 pixels right on the main screen 
        }
        //
        // Draw Plus/Minus buttons for increasing/decreasing time multiplier. (Hidden for now. Maybe bring back in later version).
        //
        /*
        if (world.state == WorldStateEnum.AwaitingBet || world.state == WorldStateEnum.SecondRoundDealt){ 
        	g.drawPixmap(Assets.buttonMinus, 50,260);
        	g.drawPixmap(Assets.buttonPlus, 113,260);
        }
        */
        
    	//
    	// Draw all 5 cards and the optional hold icons over each card
    	//
        if (world.state == WorldStateEnum.FirstRoundDealt || world.state == WorldStateEnum.SecondRoundDealt){ 
        	
        	//
        	// Draw "Ace" the Cat in the background
        	//
        	////g.drawPixmap(Assets.pointer, 135, -5);  // leave ace out for now.
        	
            int cardX = 0; // int positions inside the cards png file of the card we want to display.
            int cardY = 0; 
            x = 12; // xpos of card on screen-- increments by 90 at end of loop
            y = 92; // ypos of card on screen-- stays same
            
            int xHeldIcon = -10; //44; // xpos of "Held" icon on screen -- increments by 90 at end of loop
            int yHeldIcon = 160;//72; // ypos of card on screen-- stays same
            
    		CardRankEnum myRank; 
    		CardSuitEnum mySuit; 
    		
    		//
    		// Loop to draw all five cards on screen
    		//
        	for(int c = 0; c < 5; c++) {
        		
        		// After second round is dealt, draw a yellow highlight around each of the winning cards in the hand.
            	if ( (world.state == WorldStateEnum.SecondRoundDealt) && (world.hand.showCard[5] == true) ){
            			if (world.hand.winningCard[c] == true )
            				g.drawPixmap(Assets.highlightCard, x-5, y-5);

            	} // end if
        		
        		// Determine rank and suit of card. This determines the x,y coordinates of the card to grab from 
        		// the png file that contains an image of all 52 cards.
        		myRank = world.hand.getCard(c).getRank();
        		mySuit = world.hand.getCard(c).getSuit();
        		switch (myRank) {
        			// Determine X position of card inside the png
        			case DEUCE:
        				cardX = 0 * 80;
        				break;
        			case THREE:
        				cardX = 1 * 80;
        				break;
        			case FOUR:
        				cardX = 2 * 80;
        				break;
        			case FIVE:
        				cardX = 3 * 80;
        				break;
        			case SIX:
        				cardX = 4 * 80;
        				break;
        			case SEVEN:
        				cardX = 5 * 80;
        				break;
        			case EIGHT:
        				cardX = 6 * 80;
        				break;
        			case NINE:
        				cardX = 7 * 80;
        				break;
        			case TEN:
        				cardX = 8 * 80;
        				break;
        			case JACK:
        				cardX = 9 * 80;
        				break;
        			case QUEEN:
        				cardX = 10 * 80;
        				break;
        			case KING:
        				cardX = 11 * 80;
        				break;
        			case ACE:
        				cardX = 12 * 80; // X pos inside the cards png.  // 73 for all cards
        				break;
        			default:
        				break;
        		} // end switch
        		switch (mySuit) {
        			// Determine Y position of card inside the png
        			case SPADES:
        				cardY= 0 * 120;    //98 was old value
        				break;
        			case CLUBS:
        				cardY = 1 * 120;
        				break;
        			case HEARTS:
        				cardY = 2 * 120;
        				break;
        			case DIAMONDS:
        				cardY = 3 * 120;
        				break;
        			default:
        				break;
        		} // end switch
        		// Draw the card from above:
                ////g.drawPixmap(Assets.cards, x, y, cardX, cardY, 73, 98);
        		if (world.hand.showCard[c] == true)  // show card helps with the timing.
        			g.drawPixmap(Assets.cards, x, y, cardX, cardY, 82, 121);  //g.drawPixmap(Assets.cards, x, y, cardX, cardY, 82, 122);

                x+=90; // move 90 pixels right on the main screen
        		
        		// draw "held" icon over card
                //if (world.hand.holdCard[c] == true)
        		//	g.drawPixmap(Assets.held, xHeldIcon+54, yHeldIcon-88);
        		
        		// draw "held paw" icon over card
        		if (world.hand.holdCard[c] == true){
        			if (c == 0 || c == 2 || c == 4)
        				g.drawPixmap(Assets.paws, xHeldIcon, yHeldIcon,0,  0, 108, 181);
        			else
        				g.drawPixmap(Assets.paws, xHeldIcon, yHeldIcon,107,0, 108, 181);
        		}
        		xHeldIcon+=90;
        		
            } // for c = 0 to 5 of drawing cards
        
        	//
        	// Display Text telling you what hand you have:
        	//
        	CardHandEnum myHand = world.hand.bestHand;
        	if ( (world.hand.showCard[5] == true) || (world.state == WorldStateEnum.SecondRoundDealt) ){  
        		// 
        		// Draw "hairball" for losing hand or "good job!' for winning hand.
        		//
        		if ((world.state == WorldStateEnum.SecondRoundDealt) && (world.hand.showCard[5] == true)){ 
        			if(myHand == CardHandEnum.SLOP){
        				g.drawPixmap(Assets.hairball, hairX, 155);  //140
        			}else{
        	        	g.drawPixmap(Assets.winScore,  160, 233,0,0,80,40);
        	        	drawText(g, winAmount,  245,233);
        	        	// Draw happy kitty!
        	        	g.drawPixmap(Assets.pointer, 310, 4); 
        			}
        		}        		

            	switch (myHand) {
            		case SLOP:
            			cardY = 9 * 20 ;
            			break;
            		case JACKSORBETTER:
            			cardY = 8 * 20;
            			break;
            		case TWOPAIR:
            			cardY = 7 * 20;
            			break;
            		case THREEOFAKIND:
            			cardY = 6 * 20;
            			break;
            		case STRAIGHT:
            			cardY = 5 * 20;
            			break;
            		case FLUSH:
            			cardY = 4 * 20;
            			break;
            		case FULLHOUSE:
            			cardY = 3 * 20;
            			break;
            		case FOUROFAKIND:
            			cardY = 2 * 20;
            			break;
            		case STRAIGHTFLUSH:
            			cardY = 1 * 20;
            			break;
            		case ROYALFLUSH:
            			cardY = 0 * 20;
            			break;
            		default:
            			cardY = 9 * 20;
            			break;
            	}
        		// we're making sure to show the stuff in this block only after last card is shown in first hand
        		// or otherwise just update it at the last second after the second hand
        		
        		
        		// Display the best hand in middle, and next worst below, and next best above.
        		g.drawPixmap(Assets.hands, 20, 10, 0, cardY, 220, 60);
        		
        		// Draw circle around the best hand
        		g.drawPixmap(Assets.highlight, 5, 6);
        	
        		// Draw what hand is worth (to the right of it)
        		drawText(g, handWorth, 245, 21);  
        		if (oldHandWorth < 0)
        			g.drawPixmap(Assets.numbersMinus, 245, 22);
        		else
        			g.drawPixmap(Assets.numbersPlus, 215, 24);
        		

        	} // end if

        } // end if (world.state == WorldStateEnum.FirstRoundDealt || world.state == WorldStateEnum.SecondRoundDealt)
        
        
        //
        // Draw Pause button
        //
        g.drawPixmap(Assets.pauseButton, 430, 10); 
        
        //
        // Draw Play (Draw) button
        //
        g.drawPixmap(Assets.play, 400,240);
        
        //
        // Draw Score
        //
    	g.drawPixmap(Assets.winScore,  133,286,0,45,97,30);
    	drawText(g, score, 245,280);
		if (oldScore < 0)
			g.drawPixmap(Assets.numbersMinus, 245, 281);
    	
        
        // 
        // Draw Time Multiplier (hidden for now. Maybe in a later version)
        //
        /*
		g.drawPixmap(Assets.timeMultiplier,  170,  g.getHeight() - 52);
        g.drawPixmap(Assets.letterX, 173, g.getHeight() - 32);
        drawText(g, bet, 193, g.getHeight() - 42);
        */
        
        //
        // Draw Best Time ever
        //
        /*g.drawPixmap(Assets.timeBest,  20, 211);
        drawText(g, score, 100, 216);
        */
      
        // Draw Win Amount:
        //if ((world.state == WorldStateEnum.SecondRoundDealt) && (oldWinAmount > 0) && (world.hand.showCard[5] == true)){
       // 	g.drawPixmap(Assets.timeWon,  216, 220);
      //  	drawText(g, winAmount,  246,216);
        //}
        
    }  // end void drawWorld(World world)
    
    //private void drawReadyUI() {
    //    Graphics g = game.getGraphics();
    //    
        //g.drawPixmap(Assets.ready, 47, 100);
    //    g.drawLine(0, 416, 480, 416, Color.BLACK);
    //}
    
    private void drawRunningUI() {
        ////Graphics g = game.getGraphics();
        //drawWorld(world);                       
        drawWorld();  
    }
    
    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.exampleHands, 0, 0);
        g.drawPixmap(Assets.resumeExit, 419, 13);
    }

    /*
    private void drawGameOverUI() {  // Not used in this version
        Graphics g = game.getGraphics();
        
        //g.drawPixmap(Assets.gameOver, 62, 100);
        //g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }
    */
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
        }
    }
    
    @Override
    public void pause() {
    	if(Settings.soundEnabled)
    		Assets.music.setVolume(0.0f);
    	
        if(state == GameState.Running)
            state = GameState.Paused;
        
        if(world.gameOver) {
            Settings.addScore(world.score);
            Settings.save(game.getFileIO());
        }

    }

    @Override
    public void resume() {
        //if(Settings.soundEnabled)
        //	Assets.music.play(); 
    }

    @Override
    public void dispose() {
    	if(Settings.soundEnabled)
    		//Assets.music.dispose();
    		Assets.music.stop();
        
    }
}