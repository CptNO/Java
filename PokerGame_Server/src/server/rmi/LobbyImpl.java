package server.rmi;

import java.rmi.RemoteException;
import java.util.List;

import pokerGame.Entities.Game;
import pokerGame.Entities.User;
import pokerGame.Entities.Room;
import server.controllers.LoggerController.LoggerLevel;
import server.rmi.LobbyMessage.Type;

public class LobbyImpl extends BaseService implements ILobby {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LobbyImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Room joinRoom(User user, String roomId) throws RemoteException {
		Room roomToJoin = null;
		for(Game game : mainController.getGameList()){
			if(game.getRoom().getRoomName().equals(roomId)){
				return null;
			}
		}
		for (Room r : mainController.getRoomList()) {
			if (r.getRoomName().equals(roomId)) {
				r.addUser(user);
				roomToJoin = r;
				break;
			}
		}
		for (User p : mainController.getUserList()) {
			if (p.getUsername() != user.getUsername()) {
				p.getCallback().userJoinedRoom(user, roomToJoin);
			}
		}
		mainController.getLogger().log(String.format("User [%s] joined Room[%s]", user.getUsername(), roomId),
				LoggerLevel.INFO);
		return roomToJoin;
	}

	@Override
	public Room createRoom(User user, Room room) throws RemoteException {
		for (Room r : mainController.getRoomList()) {
			if (r.getRoomName().equals(room.getRoomName())) {
				return null;
			}
		}
		room.setRoomOwner(user);
		mainController.getRoomList().add(room);
		mainController.getLogger().log(
				String.format("User[%s] created room with name[%s]", user.getUsername(), room.getRoomName()),
				LoggerLevel.INFO);
		for (User p : mainController.getUserList()) {
			p.getCallback().roomCreated(room);
		}
		// TO DO assign Room Id here
		return room;
	}

	@Override
	public LobbyMessage leaveRoom(User user, Room room) throws RemoteException {
		LobbyMessage msg = new LobbyMessage();
		for (User p : mainController.getUserList()) {
			p.getCallback().userLeftRoom(user, room);
		}
		mainController.getLogger().log(
				String.format("User [%s] left Room[%s]", user.getUsername(), room.getRoomName()), LoggerLevel.INFO);
		msg.setType(Type.ACTION_SUCSESS);
		return msg;
	}

	@Override
	public LobbyMessage deleteRoom(User user, Room room) throws RemoteException {
		LobbyMessage msg = new LobbyMessage();
		try {
			for (User p : mainController.getUserList()) {
				p.getCallback().roomDeleted(room);
			}
			for (Room r : mainController.getRoomList()) {
				if (r.getRoomName().equals(room.getRoomName())) {
					mainController.getRoomList().remove(r);
					break;
				}
			}
			msg.setType(Type.ACTION_SUCSESS);
		} catch (Exception ex) {
			mainController.getLogger().log("Error deleting room: " + room.getRoomName(), LoggerLevel.WARNING);
			msg.setType(Type.ACTION_FAILED);
		}
		mainController.getLogger().log(
				String.format("User[%s] deleted room with name[%s]", user.getUsername(), room.getRoomName()),
				LoggerLevel.INFO);
		return msg;
	}

	@Override
	public LobbyMessage startGame(User user, Room room) throws RemoteException {
		Game game = new Game();
		game.setMainController(mainController);
		game.setRoom(room);
		int port = game.setupServerPort();
		if (port != 0) {
			game.start();
			for (User p : room.getUserList()) {
				p.getCallback().gameStarted("", port);
			}
		}
		mainController.addGame(game);
		return null;
	}

	@Override
	public void updateUserType(User user) throws RemoteException {
		//Empty
	}

	@Override
	public List<Room> getRoomList(User user) throws RemoteException {
		mainController.getLogger().log(String.format("User[%s] requested room list, returning %s values",
				user.getUsername(), mainController.getRoomList().size()), LoggerLevel.INFO);
		return mainController.getRoomList();
	}

}
