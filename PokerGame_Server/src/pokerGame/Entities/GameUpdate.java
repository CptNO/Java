package pokerGame.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameUpdate implements Serializable {
	private Player player;
	private UpdateType updateType;
	private PlayerAction actionType;
	private double potSize;
	private double raiseAmount;
	private List<Card> cards;
	
	public GameUpdate(){
		setCards(new ArrayList<>());
		updateType = UpdateType.NONE;
		setActionType(PlayerAction.NONE);
	}
	
	public enum UpdateType{
		PLAYER_TURN,
		NEW_ROUND,
		NEW_CARD,
		PLAYER_WON,
		PLAYER_LEFT,
		PLAYER_ACTION,
		NONE;
	}
	
	public enum PlayerAction{
		RAISE,
		CALL,
		FOLD,
		CHECK,
		NONE;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public UpdateType getUpdateType() {
		return updateType;
	}

	public void setUpdateType(UpdateType updateType) {
		this.updateType = updateType;
	}

	public double getPotSize() {
		return potSize;
	}

	public void setPotSize(double potSize) {
		this.potSize = potSize;
	}

	public double getRaiseAmount() {
		return raiseAmount;
	}

	public void setRaiseAmount(double raiseAmount) {
		this.raiseAmount = raiseAmount;
	}

	public PlayerAction getActionType() {
		return actionType;
	}

	public void setActionType(PlayerAction actionType) {
		this.actionType = actionType;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
	public void addCard(Card card){
		cards.add(card);
	}
}
