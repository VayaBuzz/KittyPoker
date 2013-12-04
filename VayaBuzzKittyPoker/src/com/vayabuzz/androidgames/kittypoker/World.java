package com.vayabuzz.androidgames.kittypoker;

// This class initializes KittyPoker's poker table

// Here are some new comments to test git!!! //

public final class World {
    public interface WorldListener {
        public void purr();
        public void stopPurr();
    }
    
    public WorldStateEnum state;   // TO-DO: making private causes errors. See if you can update this.
    private Deck deck; // Deck of 52 cards
    public Hand hand; // = new Hand(deck);    // abstract the hand (array of 5 cards) to a new class

    public int score = 0; // gotta give 'em some time to start with. Negative scores are allowed.
    int winAmount = 0; // how much is won in a particular round of cards
    
    private int bet = 1; // not used at this time

    public boolean gameOver = false;
    
    // 
    // Timekeeping stuff
    //
    private int timeMultiplier = 2; // used in update method for second round
    public float dealTime = 0; // experimental time accumulator variable for drawing out of cards.
    //static float tick = TICK_INITIAL; // time interval used to advance the timer.  // Depreated
    public float waitTime = 0.15f; //0.15f;  // play with this later if you like. Larger numbers increase time between cards drawn.
    //public float timeLeft = 60.0f;  // 60 seconds to finish the game, or else game over. But, if you win a hand you get more time // Deprecated
    
    public final WorldListener listener;
    //public World(WorldListener listener) {  // World Constructor
    public World(WorldListener listener) {  // World Constructor
        this.listener = listener;
        state = WorldStateEnum.AwaitingBet;
        deck = new Deck();
        deck.shuffle();
        hand = new Hand(deck);
    }

    public void update(float deltaTime) {
    	// this method responsible for updating the World and all the objects in it
    	// based on the delta time we pass to it. This method will be called each
    	// frame in the game screen so that the World is constantly updated.
        if (gameOver)
            return;

        dealTime += deltaTime;  // deal time starts at 0, then gets incremented by really small amount (deltaTime)
        
        if (state == WorldStateEnum.FirstRoundDealt){  // FirstRoundDealt means the user pressed Draw to draw the first 5 cards.
        	// Put a little pause of "waitTime" between drawing each of the cards
        	if ((dealTime > waitTime * 1) && (hand.showCard[0] == false)){
        		hand.showCard[0] = true;
    			if(Settings.soundEnabled)
    				Assets.fft.play(1);
        	}
        	if ((dealTime > waitTime * 2) && (hand.showCard[1] == false)){
        		hand.showCard[1] = true;
    			if(Settings.soundEnabled)
    				Assets.fft.play(1);
        	}
        	if ((dealTime > waitTime * 3) && (hand.showCard[2] == false)){
        		hand.showCard[2] = true;
    			if(Settings.soundEnabled)
    				Assets.fft.play(1);
        	}
        	if ((dealTime > waitTime * 4) && (hand.showCard[3] == false)){
        		hand.showCard[3] = true;
    			if(Settings.soundEnabled)
    				Assets.fft.play(1);
        	}
        	if ((dealTime > waitTime * 5) && (hand.showCard[4] == false)){
        		hand.showCard[4] = true;
    			if(Settings.soundEnabled)
    				Assets.fft.play(1);
        	}
        	if ((dealTime > waitTime * 6) && (hand.showCard[5] == false)){
        		hand.bestHand = hand.calculateBestHand();  // re-add this later if needed
        		//hand.calculateWinningCards(); // NEW! (the ones we want to see in yellow)
        		hand.setCurrentHandWorth(); // determine points for the hand.
        		hand.showCard[5] = true; // the "sixth" card here is the score we'll display over the hand
        		if (hand.getCurrentHandWorth() > 0){
        			if(Settings.soundEnabled){
        				Assets.meow2.play(1);
        				
        			}
        		}
        	}
        } // state == WorldStateEnum.FirstRoundDealt
        
        if (state == WorldStateEnum.SecondRoundDealt){  
        	// Slowly reveal the cards, with a pause  between each one.
        	// This was easy in the first round since there was a pause between all 5 cards.
        	// But now we only need to pause for cards that weren't held. Hence, we use
        	// a variable timeMultiplier rather than fixed timeMultipliers in the first round. 
        	
        	if ((dealTime > waitTime * timeMultiplier) && (hand.showCard[0] == false)){
        		hand.showCard[0] = true;
        		timeMultiplier++;
    			if(Settings.soundEnabled)
    				Assets.fft.play(1);
        	}
        	if ((dealTime > waitTime * timeMultiplier) && (hand.showCard[1] == false)){
        		hand.showCard[1] = true;
        		timeMultiplier++;
    			if(Settings.soundEnabled)
    				Assets.fft.play(1);
        	}
        	if ((dealTime > waitTime * timeMultiplier) && (hand.showCard[2] == false)){
        		hand.showCard[2] = true;
        		timeMultiplier++;
    			if(Settings.soundEnabled)
    				Assets.fft.play(1);
        	}
        	if ((dealTime > waitTime * timeMultiplier) && (hand.showCard[3] == false)){
        		hand.showCard[3] = true;
        		timeMultiplier++;
    			if(Settings.soundEnabled)
    				Assets.fft.play(1);
        	}
        	if ((dealTime > waitTime * timeMultiplier) && (hand.showCard[4] == false)){
        		hand.showCard[4] = true;
        		timeMultiplier++;
    			if(Settings.soundEnabled)
    				Assets.fft.play(1);
        	}
        	if ((dealTime > waitTime * timeMultiplier) && (hand.showCard[5] == false)){
        		hand.showCard[5] = true;
        		hand.bestHand = hand.calculateBestHand();  // re-add this later if needed
        		hand.calculateWinningCards(); // NEW! (the ones we want to see in yellow)
        		hand.setCurrentHandWorth(); // determine points for the hand.
        		if (hand.getCurrentHandWorth() > 0){
        			if(Settings.soundEnabled)
        				Assets.meow1.play(1);
        			    listener.purr();
        		}else{
        			if(Settings.soundEnabled)
        				listener.stopPurr();
        				Assets.hairballSound.play(1);
        		} //(hand.getCurrentHandWorth() > 0)
        		updateScore();
        	}
        	
        } // state == WorldStateEnum.SecondRoundDealt        
         
        // Detect a transition to a new round of playing. Build in a pause.
        if (state == WorldStateEnum.NewRound){
    	    deck.deckIndex = 0; 
    	    deck.shuffle();
    	    hand.resetHoldCards();
        	hand.drawCards();
        	
        	changeWorldState(); // change to FirstRoundDealt
        	// new
        	dealTime = 0.0f; // reset the dealTimeaccumulator
        }


           /*   // currently not used
           timeLeft -= tick; // code for our countdown timer. If it reaches zero, game over
            if (timeLeft <= 0.0f) {
            	timeLeft = 0.0f;
                gameOver = true;
                return;
            } 
            */
    }
    
    public void resetTimeMultiplier(){
    	timeMultiplier = 2;
    }
    
    public void increaseBet() {
    	if ((bet < 1001) && (score > 0)){ // limit max bet multiplier to 1000 2.1 billion is max int value in Java 
    		bet++;
    	}
    }
    
    public void decreaseBet() {
    	if (bet > 1){
    		bet--;
    	}
    }
    
    /*
    public void regulateBet() {  // keeps the user from trying to sneak through with a bet bigger than the total score.
    	if (bet > score){
    		bet = score;
    	}
    }
    */

    public void updateScore() {	
        //CardHandEnum myHand = hand.bestHand;
        //hand:                  // payoff:
    	//SLOP,                  // 0 x bet
    	//JACKSORBETTER,         // 1 x bet
    	//TWOPAIR,               // 2 x bet
    	//THREEOFAKIND,          // 3 x bet
    	//STRAIGHT,              // 4 x bet
    	//FLUSH,                 // 5 x bet
    	//FULLHOUSE,             // 7 x bet
    	//FOUROFAKIND,           // 50 x bet
    	//STRAIGHTFLUSH,         // 80 x bet
    	//ROYALFLUSH,            // 250 x bet
    	/*
		switch (myHand) {
		case SLOP:
			winAmount = -3 * bet;  // "bet" really means "bet multiplier". But for now, the bet multiplier is "1"
			break;
		case JACKSORBETTER:
			winAmount = 10 * bet;
			break;
		case TWOPAIR:
			winAmount = 20 * bet;
			break;
		case THREEOFAKIND:
			winAmount = 30 * bet;
			break;
		case STRAIGHT:
			winAmount = 40 * bet;
			break;
		case FLUSH:
			winAmount = 50 * bet;
			break;
		case FULLHOUSE:
			winAmount = 70 * bet;
			break;
		case FOUROFAKIND:
			winAmount = 500 * bet;
			break;
		case STRAIGHTFLUSH:
			winAmount = 800 * bet;
			break;
		case ROYALFLUSH:
			winAmount = 2500 * bet;
			break;
		default:
			winAmount = 0 * bet;
			break;
		}
		*/
		// Old version where the "time" is your score:
		// Make sure timeLeft never goes below zero.
		// Otherwise, assume the winAmount is a positive number and add it to timeLeft
		/*if (timeLeft + winAmount < 0){
			timeLeft = 0.0f;
		}else{
			timeLeft += winAmount;
		}
		if (timeLeft > score)  // increase score to show the max time left that has been achieved. Score only goes up, not down.
			score = (int) timeLeft;
    	*/
		// New version where score goes as high or as low as you like:
    	winAmount = hand.getCurrentHandWorth();
		score += winAmount;
    }
    
    public void changeWorldState(){  // state of the current round of Kitty Poker
    	if (state == WorldStateEnum.AwaitingBet) {
    	    state = WorldStateEnum.FirstRoundDealt;
    	    dealTime = 0.0f;
    	} else if (state == WorldStateEnum.FirstRoundDealt) {
    	    state = WorldStateEnum.SecondRoundDealt;
    	    dealTime = 0.0f;
    	} else if (state == WorldStateEnum.SecondRoundDealt) {
    	    state = WorldStateEnum.AwaitingBet;
    	    deck.deckIndex = 0;
    	    deck.shuffle();
    	    hand.resetHoldCards();
    	    hand.resetShowCards();
    	} else if (state == WorldStateEnum.NewRound) {
    	    state = WorldStateEnum.FirstRoundDealt;
    	} 
    }
    
    public void setWorldState(WorldStateEnum newState){
    	state = newState;
    }
    
    public WorldStateEnum getWorldState(){
    	return state;
    }
    
}
