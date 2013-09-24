package com.vayabuzz.androidgames.kittypoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Hand {  // Represents a collection of five Card objects
	Deck deck;
	public boolean holdCard[]; // boolean to keep track of whether or not any of the five cards is "Held"
	public boolean winningCard[]; // boolean to keep track of whether a particular card is part of a winning hand.
	public boolean showCard[]; // used to track whether card should be shown on screen at the moment or not.
	public List<Card> hand; // the hand as it will be displayed on the board
	private List<Card> handAsc; // the hand above, but an internal copy in Ascending order by rank  
	private Comparator<Card> myComparator;  // used internally to compare if one card rank is higher or lower than another
	public CardHandEnum bestHand;
	public int currentHandWorth;  // number of game points the hand is worth (change these values later if needed)
	
	public Hand(Deck deck){  // constructor //
		this.deck = deck; // refer to the deck reference we passed in.
		holdCard = new boolean[5];
		winningCard = new boolean[5];
		showCard = new boolean[6];  // values 0-4 are for the five cards. value 5 is for the best hand and win amount display:
		hand = new ArrayList<Card>();
		
		/*
		// Test purposes only
		hand.add(new Card(CardSuitEnum.SPADES,CardRankEnum.TEN));
		hand.add(new Card(CardSuitEnum.SPADES,CardRankEnum.JACK));
		hand.add(new Card(CardSuitEnum.SPADES,CardRankEnum.ACE));
		hand.add(new Card(CardSuitEnum.SPADES,CardRankEnum.KING));
		hand.add(new Card(CardSuitEnum.HEARTS,CardRankEnum.QUEEN));
		// Test purposes only
		*/
		
		handAsc = new ArrayList<Card>();
		myComparator = new Comparator<Card>(){ 
			public int compare(Card o1, Card o2) {
				return o1.getRank().compareTo(o2.getRank());
        	}
		};
		
	} // end constructor

	private void sortCards(){  // orders the internal "handAsc" hand by rank, low to high. Does not affect the display order of game
		Collections.sort(handAsc, myComparator);
	}
	
	public void drawCards(){ // insert as many cards into the hand as needed
		// keep using deckIndex to keep track of cards drawn from the deck
		// Reset the deck index when new hand is drawn (do this in World)
		for (int y = 0; y < 5; y++) {  // deal necessary number of cards from deck
			if (holdCard[y] == false){
				showCard[y] = false;  // initialize card as not viewable. World will change it to viewable after a time limit passes by.
				deck.deckIndex++;
				// Check to see if hand position is empty. If so, then use "add" command"
				// If not empty, then use the "set" command to replace the existing card.
				int handSize = hand.size();
				if (handSize < (y+1)){
					hand.add(deck.cards.get(deck.deckIndex));
				}else{
					hand.set(y,deck.cards.get(deck.deckIndex));
				}
			}
		}
		showCard[5] = false;  // used for the best hand display. World sets this to true to each card as time goes by.
		
		// Clear out the handAsc list, then add all of hand's cards to handAsc.
		// Finally sort handAsc by rank  (A,2,3,4...10,J,Q,K...etc.)
		handAsc.clear();
		handAsc.addAll(hand);
		sortCards();  // orders the internal "handAsc" hand by rank, low to high. Does not affect the display order of game
	}
	
	public void updateHoldCard(int index){ // if told to hold this card, then hold it, dude! Or, un-hold it if it is already held
		// to do: check to make sure value is between 0 and 4, inclusive. throw exception if not (or better yet, just ignore
		if(index >= 0 && index <=4 ){
			if(holdCard[index] == false)
				holdCard[index] = true;
			else
				holdCard[index] = false;
		}
	}
	
	public int countHoldCards(){   // returns number of cards in hand that are held
		int cardsHeld = 0;
		for (int y = 0; y < 5; y++){
			if (holdCard[y] == true)
				cardsHeld++;
		}
		return cardsHeld;
	}
	
	public void resetHoldCards(){ // change value to false for all hold cards
		//make sure value is between 0 and 4, inclusive.
		for (int y = 0; y < 5; y++){
			holdCard[y] = false;
		}
	}
	
	public void resetShowCards(){ // change value to false for all hold cards
		//make sure value is between 0 and 5, inclusive. 
		for (int y = 0; y < 6; y++){
			showCard[y] = false;
		}
	}
	
	public void resetShowCardsForRound2(){ // change value to false for all hold cards
		for (int y = 0; y < 5; y++){
			if (holdCard[y] == false)
				showCard[y] = false;    // otherwise the holdCard is true and we will keep the showCard value as true as it was at the end end of round 1
		}
		showCard[5] = false;  // hard code this because there never is a holdCard[5]
	}

	
    public void setCurrentHandWorth(){  // This method is only called by drawCards() above
        CardHandEnum myHand = bestHand;
        //hand:                  // payoff:
    	//SLOP,                  // -10 x bet  ("bet" really means "bet multiplier")
    	//JACKSORBETTER,         // 10 x bet
    	//TWOPAIR,               // 20 x bet
    	//THREEOFAKIND,          // 30 x bet
    	//STRAIGHT,              // 40 x bet
    	//FLUSH,                 // 60 x bet
    	//FULLHOUSE,             // 90 x bet
    	//FOUROFAKIND,           // 250 x bet
    	//STRAIGHTFLUSH,         // 500 x bet
    	//ROYALFLUSH,            // 2500 x bet
		switch (myHand) {
		case SLOP:
			currentHandWorth = -10;
			break;
		case JACKSORBETTER:
			currentHandWorth = 10;
			break;
		case TWOPAIR:
			currentHandWorth = 20;
			break;
		case THREEOFAKIND:
			currentHandWorth = 30;
			break;
		case STRAIGHT:
			currentHandWorth = 40;
			break;
		case FLUSH:
			currentHandWorth = 60;
			break;
		case FULLHOUSE:
			currentHandWorth = 90;
			break;
		case FOUROFAKIND:
			currentHandWorth = 250;
			break;
		case STRAIGHTFLUSH:
			currentHandWorth = 500;
			break;
		case ROYALFLUSH:
			currentHandWorth = 2500;
			break;
		default:
			currentHandWorth = 0;
			break;
		}
    }
    
    public int getCurrentHandWorth(){
    	return currentHandWorth;
    }
    
    
	public void calculateWinningCards(){
    	// Set a boolean in the winningCard[5] array to true for each card that is part of a winning hand.
		// The "besthand' value in this class needs to be set prior to this function being called.
    	// example: a slop hand would set winningCard[] 0,1,2,3,4 all to false
		// example: a flush would set winningCard[] 0,1,2,3,4 all to true
		// example: jacks or better where 0 and 1 are jacks would setwinningCard[] 0=true,1=true, 2,3,4 to false.
    	CardRankEnum rankToFind = null; 
    	CardRankEnum secondRankToFind = null;
    	//CardRankEnum myRank;
    	//Card myCard;
    	int numAce =   countCardsOfRank(CardRankEnum.ACE);
    	int numDeuce = countCardsOfRank(CardRankEnum.DEUCE);
    	int numThree = countCardsOfRank(CardRankEnum.THREE);
    	int numFour =  countCardsOfRank(CardRankEnum.FOUR);
    	int numFive =  countCardsOfRank(CardRankEnum.FIVE);
    	int numSix =   countCardsOfRank(CardRankEnum.SIX);
    	int numSeven = countCardsOfRank(CardRankEnum.SEVEN);
    	int numEight = countCardsOfRank(CardRankEnum.EIGHT);
    	int numNine =  countCardsOfRank(CardRankEnum.NINE);
    	int numTen =   countCardsOfRank(CardRankEnum.TEN);
    	int numJack =  countCardsOfRank(CardRankEnum.JACK);
    	int numQueen = countCardsOfRank(CardRankEnum.QUEEN);
    	int numKing =  countCardsOfRank(CardRankEnum.KING);
    	
		for (int y = 0; y < 5; y++) {  // always initialize the array just to be safe
			winningCard[y] = false;
		}
		
		switch (bestHand) {
		case SLOP:  // do nothing and just break
			break;
		case STRAIGHTFLUSH:  // all of these hands contain 5 winning cards, so just set all the values to true:
		case ROYALFLUSH:
		case FULLHOUSE:
		case STRAIGHT:
		case FLUSH:
			for (int y = 0; y < 5; y++) {
				winningCard[y] = true;
			}
			break;			

		case JACKSORBETTER:
			if (numAce == 2)
				rankToFind = (CardRankEnum.ACE);
			else if (numJack == 2)
				rankToFind = (CardRankEnum.JACK);
			else if (numQueen == 2)
				rankToFind = (CardRankEnum.QUEEN);
			else if (numKing == 2)
				rankToFind = (CardRankEnum.KING);
			else
				rankToFind = (CardRankEnum.KING); // this should never happen, but just to be safe...
			
			for (int y = 0; y < 5; y++) {
				if (hand.get(y).getRank() == rankToFind) 
					winningCard[y] = true;
			} // end for
			
			break;

		case TWOPAIR: 
			boolean firstPairFound = false;

			if (numAce == 2){
				rankToFind = (CardRankEnum.ACE);
				firstPairFound = true;
			}
			if (numDeuce == 2){
 				if (firstPairFound == true){
					secondRankToFind = CardRankEnum.DEUCE;
				}else{
					rankToFind = CardRankEnum.DEUCE;
					firstPairFound = true;
				}
			}
			if (numThree == 2){
 				if (firstPairFound == true){
					secondRankToFind = CardRankEnum.THREE;
				}else{
					rankToFind = CardRankEnum.THREE;
					firstPairFound = true;
				}
			}
			if (numFour == 2){
 				if (firstPairFound == true){
					secondRankToFind = CardRankEnum.FOUR;
				}else{
					rankToFind = CardRankEnum.FOUR;
					firstPairFound = true;
				}
			}
			if (numFive == 2){
 				if (firstPairFound == true){
					secondRankToFind = CardRankEnum.FIVE;
				}else{
					rankToFind = CardRankEnum.FIVE;
					firstPairFound = true;
				}
			}
			if (numSix == 2){
  				if (firstPairFound == true){
					secondRankToFind = CardRankEnum.SIX;
				}else{
					rankToFind = CardRankEnum.SIX;
					firstPairFound = true;
				}
			}
			if (numSeven == 2){
 				if (firstPairFound == true){
					secondRankToFind = CardRankEnum.SEVEN;
				}else{
					rankToFind = CardRankEnum.SEVEN;
					firstPairFound = true;
				}
			}
			if (numEight == 2){
 				if (firstPairFound == true){
					secondRankToFind = CardRankEnum.EIGHT;
				}else{
					rankToFind = CardRankEnum.EIGHT;
					firstPairFound = true;
				}
			}
			if (numNine == 2){
 				if (firstPairFound == true){
					secondRankToFind = CardRankEnum.NINE;
				}else{
					rankToFind = CardRankEnum.NINE;
					firstPairFound = true;
				}
			}
			if (numTen == 2){
 				if (firstPairFound == true){
					secondRankToFind = CardRankEnum.TEN;
				}else{
					rankToFind = CardRankEnum.TEN;
					firstPairFound = true;
				}
			}
			if (numJack == 2){
 				if (firstPairFound == true){
					secondRankToFind = CardRankEnum.JACK;
				}else{
					rankToFind = CardRankEnum.JACK;
					firstPairFound = true;
				}
			}
			if (numQueen == 2){
 				if (firstPairFound == true){
					secondRankToFind = CardRankEnum.QUEEN;
				}else{
					rankToFind = CardRankEnum.QUEEN;
					firstPairFound = true;
				}
			}
			if (numKing == 2)
 				secondRankToFind = CardRankEnum.KING;  // at this point the first pair should have been found.

			for (int y = 0; y < 5; y++) {
				if ( (hand.get(y).getRank() == rankToFind) || (hand.get(y).getRank() == secondRankToFind) )
					winningCard[y] = true;
			} // end for			
			break;

		case THREEOFAKIND:
			if (numAce == 3)
				rankToFind = (CardRankEnum.ACE);
			else if (numDeuce == 3)
				rankToFind = (CardRankEnum.DEUCE);
			else if (numThree == 3)
				rankToFind = (CardRankEnum.THREE);
			else if (numFour == 3)
				rankToFind = (CardRankEnum.FOUR);
			else if (numFive == 3)
				rankToFind = (CardRankEnum.FIVE);
			else if (numSix == 3)
				rankToFind = (CardRankEnum.SIX);
			else if (numSeven == 3)
				rankToFind = (CardRankEnum.SEVEN);
			else if (numEight == 3)
				rankToFind = (CardRankEnum.EIGHT);
			else if (numNine == 3)
				rankToFind = (CardRankEnum.NINE);
			else if (numTen == 3)
				rankToFind = (CardRankEnum.TEN);
			else if (numJack == 3)
				rankToFind = (CardRankEnum.JACK);
			else if (numQueen == 3)
				rankToFind = (CardRankEnum.QUEEN);
			else if (numKing == 3)
				rankToFind = (CardRankEnum.KING);
			else
				rankToFind = (CardRankEnum.KING); // this should never happen, but just to be safe...
			
			for (int y = 0; y < 5; y++) {
				if (hand.get(y).getRank() == rankToFind) 
					winningCard[y] = true;
			} // end for			
			break;
		case FOUROFAKIND:
			if (numAce == 4)
				rankToFind = (CardRankEnum.ACE);
			else if (numDeuce == 4)
				rankToFind = (CardRankEnum.DEUCE);
			else if (numThree == 4)
				rankToFind = (CardRankEnum.THREE);
			else if (numFour == 4)
				rankToFind = (CardRankEnum.FOUR);
			else if (numFive == 4)
				rankToFind = (CardRankEnum.FIVE);
			else if (numSix == 4)
				rankToFind = (CardRankEnum.SIX);
			else if (numSeven == 4)
				rankToFind = (CardRankEnum.SEVEN);
			else if (numEight == 4)
				rankToFind = (CardRankEnum.EIGHT);
			else if (numNine == 4)
				rankToFind = (CardRankEnum.NINE);
			else if (numTen == 4)
				rankToFind = (CardRankEnum.TEN);
			else if (numJack == 4)
				rankToFind = (CardRankEnum.JACK);
			else if (numQueen == 4)
				rankToFind = (CardRankEnum.QUEEN);
			else if (numKing == 4)
				rankToFind = (CardRankEnum.KING);
			else
				rankToFind = (CardRankEnum.KING); // this should never happen, but just to be safe...
			
			for (int y = 0; y < 5; y++) {
				if (hand.get(y).getRank() == rankToFind) 
					winningCard[y] = true;
			} // end for
			break;
			
		default:
			// do nothing. Array was already initialized to false at beginning of function
			break;
		} // end switch    	
    } // end method  calculateWinningCards()
	
	
	public CardHandEnum calculateBestHand(){
		boolean checkResult;
    	int numAce =   countCardsOfRank(CardRankEnum.ACE);
    	int numDeuce = countCardsOfRank(CardRankEnum.DEUCE);
    	int numThree = countCardsOfRank(CardRankEnum.THREE);
    	int numFour =  countCardsOfRank(CardRankEnum.FOUR);
    	int numFive =  countCardsOfRank(CardRankEnum.FIVE);
    	int numSix =   countCardsOfRank(CardRankEnum.SIX);
    	int numSeven = countCardsOfRank(CardRankEnum.SEVEN);
    	int numEight = countCardsOfRank(CardRankEnum.EIGHT);
    	int numNine =  countCardsOfRank(CardRankEnum.NINE);
    	int numTen =   countCardsOfRank(CardRankEnum.TEN);
    	int numJack =  countCardsOfRank(CardRankEnum.JACK);
    	int numQueen = countCardsOfRank(CardRankEnum.QUEEN);
    	int numKing =  countCardsOfRank(CardRankEnum.KING);
 
    	checkResult = checkRoyalFlush();
    	if (checkResult == true)
    		return CardHandEnum.ROYALFLUSH; 

    	checkResult = checkStraightFlush();
    	if (checkResult == true)
    		return CardHandEnum.STRAIGHTFLUSH; 

    	checkResult = checkFourOfAKind(numAce,numDeuce,numThree,numFour,numFive,numSix,numSeven,numEight,numNine,numTen,numJack,numQueen,numKing);
    	if (checkResult == true)
    		return CardHandEnum.FOUROFAKIND;

    	checkResult = checkFullHouse(numAce,numDeuce,numThree,numFour,numFive,numSix,numSeven,numEight,numNine,numTen,numJack,numQueen,numKing);
    	if (checkResult == true)
    		return CardHandEnum.FULLHOUSE;

    	checkResult = checkFlush();
    	if (checkResult == true)
    		return CardHandEnum.FLUSH;

    	checkResult = checkStraight();
    	if (checkResult == true)
    		return CardHandEnum.STRAIGHT;

    	checkResult = checkThreeOfAKind(numAce,numDeuce,numThree,numFour,numFive,numSix,numSeven,numEight,numNine,numTen,numJack,numQueen,numKing);
    	if (checkResult == true)
    		return CardHandEnum.THREEOFAKIND;

    	checkResult = checkTwoPair(numAce,numDeuce,numThree,numFour,numFive,numSix,numSeven,numEight,numNine,numTen,numJack,numQueen,numKing);
    	if (checkResult == true)
    		return CardHandEnum.TWOPAIR;

    	checkResult = checkJacksOrBetter(numJack,numQueen,numKing,numAce);
    	if (checkResult == true)
    		return CardHandEnum.JACKSORBETTER;

		return CardHandEnum.SLOP;
    }
    
    private boolean checkJacksOrBetter(int numJack, int numQueen, int numKing, int numAce) {
    	//
    	// Detect Jacks or Better
    	//
		if (numAce == 2)
			return true;		
		if (numJack == 2)
			return true;
		if (numQueen == 2)
			return true;
		if (numKing == 2)
			return true;
		return false;
	}

	private boolean checkTwoPair(int numAce, int numDeuce, int numThree, int numFour, int numFive, int numSix, int numSeven, int numEight, int numNine, int numTen, int numJack, int numQueen, int numKing){
    	//
    	// Detect Two Pair
    	//
		int totalPairs = 0;
		if (numAce == 2)
			totalPairs++;
		if (numDeuce == 2)
			totalPairs++;
		if (numThree == 2)
			totalPairs++;
		if (numFour == 2)
			totalPairs++;
		if (numFive == 2)
			totalPairs++;
		if (numSix == 2)
			totalPairs++;
		if (numSeven == 2)
			totalPairs++;
		if (numEight == 2) 
			totalPairs++;
		if (numNine == 2)
			totalPairs++;
		if (numTen == 2)
			totalPairs++;
		if (numJack == 2)
			totalPairs++;
		if (numQueen == 2)
			totalPairs++;
		if (numKing == 2)
			totalPairs++;
		if (totalPairs > 1)
		  return true;
		return false;
	}
	
	private boolean checkOnePair(int numAce, int numDeuce, int numThree, int numFour, int numFive, int numSix, int numSeven, int numEight, int numNine, int numTen, int numJack, int numQueen, int numKing){
    	//
    	// Detect a pair of anything
    	//
		int totalPairs = 0;
		if (numAce == 2)
			totalPairs++;
		if (numDeuce == 2)
			totalPairs++;
		if (numThree == 2)
			totalPairs++;
		if (numFour == 2)
			totalPairs++;
		if (numFive == 2)
			totalPairs++;
		if (numSix == 2)
			totalPairs++;
		if (numSeven == 2)
			totalPairs++;
		if (numEight == 2) 
			totalPairs++;
		if (numNine == 2)
			totalPairs++;
		if (numTen == 2)
			totalPairs++;
		if (numJack == 2)
			totalPairs++;
		if (numQueen == 2)
			totalPairs++;
		if (numKing == 2)
			totalPairs++;
		if (totalPairs > 0)
		  return true;
		return false;
	}

	private boolean checkThreeOfAKind(int numAce, int numDeuce, int numThree, int numFour, int numFive, int numSix, int numSeven, int numEight, int numNine, int numTen, int numJack, int numQueen, int numKing) {
    	//
    	// Detect Three of a Kind
    	//
    	if (		(numAce == 3)
    			 || (numDeuce == 3) 
    			 || (numThree == 3) 
    			 || (numFour == 3)  
    			 || (numFive == 3)  
    			 || (numSix == 3)   
    			 || (numSeven == 3) 
    			 || (numEight == 3) 
    			 || (numNine == 3)  
    			 || (numTen == 3)   
    			 || (numJack == 3) 
    			 || (numQueen == 3)
    			 || (numKing == 3) ){
    		return true;
    	}
		return false;
	}

	private boolean checkStraight() {
    	//
    	// Detect Straight
    	//
		CardRankEnum rank0 = handAsc.get(0).getRank();
		CardRankEnum rank1 = handAsc.get(1).getRank();
		CardRankEnum rank2 = handAsc.get(2).getRank();
		CardRankEnum rank3 = handAsc.get(3).getRank();
		CardRankEnum rank4 = handAsc.get(4).getRank();
		// Detect 10 different straight possibilities: 5 high... Ace high, etc.
		if ((rank0 == CardRankEnum.ACE) && (rank1 == CardRankEnum.DEUCE) && (rank2 == CardRankEnum.THREE) && (rank3 == CardRankEnum.FOUR) && (rank4 == CardRankEnum.FIVE))
			return true;
		if ((rank0 == CardRankEnum.DEUCE) && (rank1 == CardRankEnum.THREE) && (rank2 == CardRankEnum.FOUR) && (rank3 == CardRankEnum.FIVE) && (rank4 == CardRankEnum.SIX))
			return true;
		if ((rank0 == CardRankEnum.THREE) && (rank1 == CardRankEnum.FOUR) && (rank2 == CardRankEnum.FIVE) && (rank3 == CardRankEnum.SIX) && (rank4 == CardRankEnum.SEVEN))
			return true;
		if ((rank0 == CardRankEnum.FOUR) && (rank1 == CardRankEnum.FIVE) && (rank2 == CardRankEnum.SIX) && (rank3 == CardRankEnum.SEVEN) && (rank4 == CardRankEnum.EIGHT))
			return true;
		if ((rank0 == CardRankEnum.FIVE) && (rank1 == CardRankEnum.SIX) && (rank2 == CardRankEnum.SEVEN) && (rank3 == CardRankEnum.EIGHT) && (rank4 == CardRankEnum.NINE))
			return true;
		if ((rank0 == CardRankEnum.SIX) && (rank1 == CardRankEnum.SEVEN) && (rank2 == CardRankEnum.EIGHT) && (rank3 == CardRankEnum.NINE) && (rank4 == CardRankEnum.TEN))
			return true;
		if ((rank0 == CardRankEnum.SEVEN) && (rank1 == CardRankEnum.EIGHT) && (rank2 == CardRankEnum.NINE) && (rank3 == CardRankEnum.TEN) && (rank4 == CardRankEnum.JACK))
			return true;
		if ((rank0 == CardRankEnum.EIGHT) && (rank1 == CardRankEnum.NINE) && (rank2 == CardRankEnum.TEN) && (rank3 == CardRankEnum.JACK) && (rank4 == CardRankEnum.QUEEN))
			return true;
		if ((rank0 == CardRankEnum.NINE) && (rank1 == CardRankEnum.TEN) && (rank2 == CardRankEnum.JACK) && (rank3 == CardRankEnum.QUEEN) && (rank4 == CardRankEnum.KING))
			return true;
		if ((rank0 == CardRankEnum.ACE) && (rank1 == CardRankEnum.TEN) && (rank2 == CardRankEnum.JACK) && (rank3 == CardRankEnum.QUEEN) && (rank4 == CardRankEnum.KING))
			return true;
		return false;
	}

	private boolean checkFlush() {
    	//
    	// Detect Flush
    	//
		CardSuitEnum suit0 = hand.get(0).getSuit();
		CardSuitEnum suit1 = hand.get(1).getSuit();
		CardSuitEnum suit2 = hand.get(2).getSuit();
		CardSuitEnum suit3 = hand.get(3).getSuit();
		CardSuitEnum suit4 = hand.get(4).getSuit();
		if ((suit0 == suit1) && (suit0 == suit2) && (suit0 == suit3) && (suit0 == suit4)){
			return true;
		}
		return false;
	}

	private boolean checkFullHouse(int numAce, int numDeuce, int numThree, int numFour, int numFive, int numSix, int numSeven, int numEight, int numNine, int numTen, int numJack, int numQueen, int numKing) {
    	//
    	// Detect Full House
    	//
		boolean hasPair = checkOnePair(numAce,numDeuce,numThree,numFour,numFive,numSix,numSeven,numEight,numNine,numTen,numJack,numQueen,numKing);
		boolean hasTrips = checkThreeOfAKind(numAce,numDeuce,numThree,numFour,numFive,numSix,numSeven,numEight,numNine,numTen,numJack,numQueen,numKing);
		if ((hasPair == true) && (hasTrips == true))
			return true;
		return false;
	}

	private boolean checkFourOfAKind(int numAce, int numDeuce, int numThree, int numFour, int numFive, int numSix, int numSeven, int numEight, int numNine, int numTen, int numJack, int numQueen, int numKing) {
    	//
    	// Detect Four of a Kind
    	//
    	if (		(numAce == 4)
    			 || (numDeuce == 4) 
    			 || (numThree == 4) 
    			 || (numFour == 4)  
    			 || (numFive == 4)  
    			 || (numSix == 4)   
    			 || (numSeven == 4) 
    			 || (numEight == 4) 
    			 || (numNine == 4)  
    			 || (numTen == 4)   
    			 || (numJack == 4) 
    			 || (numQueen == 4)
    			 || (numKing == 4) ){
    		return true;
    	}
		return false;
	}

	private boolean checkStraightFlush() {
    	//
    	// Detect Straight Flush
    	//
		if ((checkStraight()) && (checkFlush()))
			return true;
		return false;
	}

	private boolean checkRoyalFlush() {
    	//
    	// Detect Royal Flush
    	//
		boolean isFlush = checkFlush();
		boolean isRoyalStraight = false;
		int countTen = 0;
		int countJack = 0;
		int countQueen = 0;
		int countKing = 0;
		int countAce = 0;
		
		// count number of unique cards:
		countTen = countCardsOfRank(CardRankEnum.TEN);
		countJack = countCardsOfRank(CardRankEnum.JACK);
		countQueen = countCardsOfRank(CardRankEnum.QUEEN);
		countKing = countCardsOfRank(CardRankEnum.KING);
		countAce = countCardsOfRank(CardRankEnum.ACE);
		
		if ((countTen == 1) && (countJack == 1) && (countQueen == 1) && (countKing == 1) && (countAce == 1)){
			isRoyalStraight = true;
		}		 
		
		if (isFlush && isRoyalStraight){
			return true;
		}		
		return false;
	}

	private int countCardsOfRank(CardRankEnum myRank) {
		// given a particular rank, return how many cards of that rank are in the hand
		int cardsOfRank = 0;
	    for (int y = 0; y < 5; y++) {
	    	if (hand.get(y).getRank() == myRank){
	    		cardsOfRank++;
	        }
	    }
		return cardsOfRank;
	}

	
	@SuppressWarnings("unused")
	private int countCardsOfSuit(CardSuitEnum mySuit) {
		// given a particular suit, return how many cards of that rank are in the hand
		int cardsOfSuit = 0;
	    for (int y = 0; y < 5; y++) {
	    	if (handAsc.get(y).getSuit() == mySuit){
	    		cardsOfSuit++;
	        }
	    }
		return cardsOfSuit;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void keepCard(int y){ // mark card "y" as one we want to keep
    	holdCard[y] = true;   	
    }
    
    public void drawCard(int y, Card card){ // insert one fresh cards into the hand
    // TO-DO: This function maybe not needed after all.
    	    	
    }
    public Card getCard(int x){ // return card from index value x
		return hand.get(x);
    	
    }
    
    public void clearHand(){ // removes all cards from the hand  
        for (int y = 0; y < 5; y++) {
        	hand.set(y,null);
        	handAsc.set(y,null);
        }
        for (int y = 0; y < 5; y++) {
            holdCard[y] = false; // each value to be set to true later by end user
        }
    }
    
    public void clearOneCardFromHand(int c){ // removes one card from the hand  
        hand.set(c,null);
    }
}





