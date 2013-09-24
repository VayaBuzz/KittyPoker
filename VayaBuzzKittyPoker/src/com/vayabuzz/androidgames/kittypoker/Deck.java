package com.vayabuzz.androidgames.kittypoker;
// updateRunning contains the "C" of MVC
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// The deck... in code! // contains 52 cards, of course.

public class Deck {
    
	Card card;
	public List<Card> cards = new ArrayList<Card>();
	public int deckIndex = 0; // used to keep track of number of cards drawn from deck;
 
    public Deck() {        
    	// Constructor: Create 52 cards
    	addCardsToDeck(CardSuitEnum.CLUBS);
    	addCardsToDeck(CardSuitEnum.DIAMONDS);
    	addCardsToDeck(CardSuitEnum.HEARTS);
    	addCardsToDeck(CardSuitEnum.SPADES);
    }
    
    private void addCardsToDeck(CardSuitEnum suit) {
    	card = new Card(suit,CardRankEnum.ACE);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.DEUCE);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.THREE);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.FOUR);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.FIVE);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.SIX);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.SEVEN);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.EIGHT);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.NINE);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.TEN);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.JACK);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.QUEEN);
    	cards.add(card);
    	card = new Card(suit,CardRankEnum.KING);
    	cards.add(card);		
	}

	public void shuffle() {
    	Collections.shuffle(cards);
    	
    }
 
}
