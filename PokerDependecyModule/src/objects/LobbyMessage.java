package objects;

import java.io.Serializable;

public class LobbyMessage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public enum Type{
		ROOM_JOIN,
		ROOM_LEAVE,
		ROOM_CREATE,
		ROOM_DELETE,
		ACTION_SUCSESS,
		ACTION_FAILED,
		GAME_START
	}
	private Type type;
	private String additionalInfo;
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
}
