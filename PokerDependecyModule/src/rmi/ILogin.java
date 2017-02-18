package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import objects.LoginMessage;
import objects.User;

public interface ILogin extends Remote {
	
	public LoginMessage authenticateUser(User user) throws RemoteException;
	public LoginMessage registerUser(User user) throws RemoteException;
	public LoginMessage logOutUser(User user) throws RemoteException;
}
