package pokerGame.Entities;
import javafx.beans.property.*;

public class TableRoom {
	
	private StringProperty roomName;
	private StringProperty ownerName;
	private IntegerProperty userCount;
	
	public void setRoomName(String roomName){
		this.roomName = new SimpleStringProperty(roomName);
	}
	
	public String getRoomName(){
		return roomName.getValue();
	}
	
	public void setOwnerName(String ownerName){
		this.ownerName = new SimpleStringProperty(ownerName);
	}
	
	public String getOwnerName(){
		return ownerName.getValue();
	}
	
	public void setUserCount(Integer userCount){
		this.userCount = new SimpleIntegerProperty(userCount);
	}
	
	public Integer getUserCount(){
		return userCount.getValue();
	}
}
