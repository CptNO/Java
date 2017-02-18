package pokerGame.Events;

import java.util.List;

import pokerGame.Entities.Card;
import pokerGame.Entities.Player;

public class TableEvent {
	private List<Card> tableCards;
	private boolean playerWon;
	private Player player;
	
	public List<Card> getTableCards() {
		return tableCards;
	}
	public void setTableCards(List<Card> tableCards) {
		this.tableCards = tableCards;
	}
	public boolean isPlayerWon() {
		return playerWon;
	}
	public void setPlayerWon(boolean playerWon) {
		this.playerWon = playerWon;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
}
