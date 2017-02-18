package pokerGame.Entities;

import java.util.ArrayList;
import java.util.List;

import objects.Card;
import objects.Entity;

public class Table extends Entity{
	private List<Card> deck;
	private List<Card> tableCards;
	private Pot pot;

	public Table(){
		deck = new ArrayList<>();
		tableCards = new ArrayList<>();
	}
	
	public Pot getPot() {
		return pot;
	}
	public void setPot(Pot pot) {
		this.pot = pot;
	}
	public List<Card> getDeck() {
		return deck;
	}
	public void setDeck(List<Card> cardlist) {
		this.deck = cardlist;
	}
	public void addCardToDeck(Card card){
		deck.add(card);
	}

	public List<Card> getTableCards() {
		return tableCards;
	}

	public void setTableCards(List<Card> tableCards) {
		this.tableCards = tableCards;
	}
	
	public void addCardToTable(Card card){
		tableCards.add(card);
	}
	
}
