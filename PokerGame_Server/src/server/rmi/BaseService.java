package server.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import server.controllers.MainController;

public abstract class BaseService extends UnicastRemoteObject {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected MainController mainController;

	protected BaseService() throws RemoteException {
		super();
		//Empty
	}
	
	public void setMainController(MainController mainController){
		this.mainController = mainController;
	}
	
	public MainController getMainController(){
		return this.mainController;
	}
}
