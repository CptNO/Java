package pokerGame.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pokerGame.Entities.Card.Rank;
import pokerGame.Entities.Card.Suit;

public class Deck implements Serializable {
	List<Card> cardList;
	
	List<Card> cardsShown;
	
	public void generateDeck(){
		cardList = new ArrayList<Card>();
		for(Suit suit : Suit.values()){
			for(Rank rank : Rank.values()){
				cardList.add(new Card(rank, suit));
			}
		}
	}
	
	public void showNewCard(){
		  Random rand = new Random();
		  Integer index = rand.nextInt(cardList.size() -1);
		  
		  cardsShown.add(cardList.get(index));
		  cardList.remove(index);
	}

	public List<Card> getCardList() {
		return cardList;
	}

	public void setCardList(List<Card> cardList) {
		this.cardList = cardList;
	}
}
