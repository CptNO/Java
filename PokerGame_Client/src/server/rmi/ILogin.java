package server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import pokerGame.Entities.User;

public interface ILogin extends Remote {
	
	public LoginMessage authenticateUser(User user) throws RemoteException;
	public LoginMessage registerUser(User user) throws RemoteException;
	public LoginMessage logOutUser(User user) throws RemoteException;
}
