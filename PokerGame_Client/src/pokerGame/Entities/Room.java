package pokerGame.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
	
	private int id;
	private String roomName;
	private List<User> userList;
	private User roomOwner;

	public Room(String roomName){
		this.roomName = roomName;
		setUserList(new ArrayList<User>());
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	public void removeUser(User user){
		for(User userMember : userList){
			if(userMember.getUsername().equals(user.getUsername())){
				userList.remove(userMember);
				break;
			}
		}
	}
	
	public void addUser(User user){
		userList.add(user);
	}
	
	public User getUser(String username){
		for(User userMember : userList){
			if(userMember.getUsername().equals(username)){
				return userMember;
			}
		}
		return null;
	}

	public User getRoomOwner() {
		return roomOwner;
	}

	public void setRoomOwner(User roomOwner) {
		this.roomOwner = roomOwner;
	}
	
	public String getRoomOwnerName(){
		return roomOwner.getUsername();
	}
}
