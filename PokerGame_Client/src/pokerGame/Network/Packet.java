package pokerGame.Network;

import pokerGame.Entities.Player;
import pokerGame.Events.PlayerAction;

public class Packet {
	private PlayerAction action;
	private Object eventObject;
	private Player player;
	
	public PlayerAction getAction() {
		return action;
	}
	public void setAction(PlayerAction action) {
		this.action = action;
	}
	public Object getEventObject() {
		return eventObject;
	}
	public void setEventObject(Object eventObject) {
		this.eventObject = eventObject;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
}
