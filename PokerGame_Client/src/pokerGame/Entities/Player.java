package pokerGame.Entities;

import java.io.Serializable;
import java.util.List;

import server.rmi.IUserCallback;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String username;
	private List<Card> playerCards;
	private double monney;
	private boolean isCurrentTurn;
	
	public Integer getId() {
		return id;
	}
        
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public List<Card> getPlayerCards() {
		return playerCards;
	}

	public void setPlayerCards(List<Card> playerCards) {
		this.playerCards = playerCards;
	}
	
	public double getMonney() {
		return monney;
	}

	public void setMonney(double monney) {
		this.monney = monney;
	}

	public boolean isCurrentTurn() {
		return isCurrentTurn;
	}

	public void setCurrentTurn(boolean isCurrentTurn) {
		this.isCurrentTurn = isCurrentTurn;
	}
}
