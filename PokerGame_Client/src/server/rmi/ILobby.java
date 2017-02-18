package server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import pokerGame.Entities.Room;
import pokerGame.Entities.User;

public interface ILobby extends Remote{
	
	public Room joinRoom(User user, String roomId) throws RemoteException;
	public LobbyMessage leaveRoom(User user, Room room) throws RemoteException;
	public Room createRoom(User user, Room room) throws RemoteException;
	public LobbyMessage deleteRoom(User user, Room room) throws RemoteException;
	public LobbyMessage startGame(User user, Room room) throws RemoteException;
	public List<Room> getRoomList(User user) throws RemoteException;
	public void updateUserType(User user) throws RemoteException;
}
