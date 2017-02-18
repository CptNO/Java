package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import objects.Room;
import objects.User;

public interface IUserCallback  extends Remote {
	
	public void userConnected(String userName) throws RemoteException;
	public void userDisconected(String userName) throws RemoteException;
	public void roomCreated(Room room) throws RemoteException;
	public void roomDeleted(Room room) throws RemoteException;
	public void userJoinedRoom(User user, Room room) throws RemoteException;
	public void userLeftRoom(User user,Room room) throws RemoteException;
	public void messageRecived(String userName, String content) throws RemoteException;
	public void gameStarted(String serverIp, int port) throws RemoteException;
}
