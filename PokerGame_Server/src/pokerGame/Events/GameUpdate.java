package pokerGame.Events;

import java.io.Serializable;
import java.math.BigDecimal;

import pokerGame.Entities.Player;

public class GameUpdate implements Serializable {

	private Player player;
	private BigDecimal raise;
	private Boolean call;
	private Boolean pass;
	private Boolean fold;
	private Action action;
	
	public enum Action{
		FOLD,
		RAISE,
		PASS,
		CALL,
		PLAYER_WON
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public BigDecimal getRaise() {
		return raise;
	}
	public void setRaise(BigDecimal raise) {
		this.raise = raise;
	}
	public Boolean getCall() {
		return call;
	}
	public void setCall(Boolean call) {
		this.call = call;
	}
	public Boolean getPass() {
		return pass;
	}
	public void setPass(Boolean pass) {
		this.pass = pass;
	}
	public Boolean getFold() {
		return fold;
	}
	public void setFold(Boolean fold) {
		this.fold = fold;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	
}
