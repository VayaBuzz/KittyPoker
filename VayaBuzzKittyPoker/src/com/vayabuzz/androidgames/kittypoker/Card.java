package com.vayabuzz.androidgames.kittypoker;

public class Card {
//
// a single Playing card
//	
	private CardSuitEnum suit;
	private CardRankEnum rank;
	    
	    public Card(CardSuitEnum suit, CardRankEnum rank) {
	        this.suit = suit;
	        this.rank = rank;
	    }
	    
	    public CardSuitEnum getSuit(){
	    	return suit;
	    }
	    
	    public CardRankEnum getRank(){
	    	return rank;
	    }
	    
}    
