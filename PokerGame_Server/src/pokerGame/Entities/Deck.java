package pokerGame.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pokerGame.Entities.Card.Rank;
import pokerGame.Entities.Card.Suit;

public class Deck implements Serializable {
	private List<Card> cardList;

	private List<Card> cardsShown;
	
	public Deck(){
		cardsShown = new ArrayList<>();
		cardList = new ArrayList<>();
	}

	public void generateDeck() {
		cardList = new ArrayList<Card>();
		for (Suit suit : Suit.values()) {
			if(suit != Suit.DECK){
				for (Rank rank : Rank.values()) {
					if(rank != Rank.RED){
						cardList.add(new Card(rank, suit));
					}
				}
			}
		}
	}

	public void showNewCard() {
		Random rand = new Random();
		Integer index = rand.nextInt(cardList.size() - 1);

		cardsShown.add(cardList.get(index));
		cardList.remove(index);
	}

	public Card getCard() {
		Random rand = new Random();
		Integer index = rand.nextInt(getCardList().size() - 1);
		cardsShown.add(cardList.get(index));
		Card card = cardList.get(index);
		cardList.remove(index);
		return card;
	}

	public List<Card> getCardList() {
		return cardList;
	}

	public void setCardList(List<Card> cardList) {
		this.cardList = cardList;
	}
}
