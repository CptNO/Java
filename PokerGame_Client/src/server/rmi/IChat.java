package server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import pokerGame.Entities.User;

public interface IChat extends Remote {
	public void sendMessage(User user, String message) throws RemoteException;
}
