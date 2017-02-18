package pokerGame.Events;

import pokerGame.Entities.Player;

public class PlayerEvent {
	private IEvent event;
	private Player player;
	private PlayerAction action;
	
	public IEvent getEvent() {
		return event;
	}
	public void setEvent(IEvent event) {
		this.event = event;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public PlayerAction getAction() {
		return action;
	}
	public void setAction(PlayerAction action) {
		this.action = action;
	}
	
}
