package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import objects.User;

public interface IChat extends Remote {
	public void sendMessage(User user, String message) throws RemoteException;
}
