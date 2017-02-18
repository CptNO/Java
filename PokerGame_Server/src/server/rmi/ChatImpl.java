package server.rmi;

import java.rmi.RemoteException;

import pokerGame.Entities.User;


public class ChatImpl extends BaseService implements IChat {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ChatImpl() throws RemoteException {
		super();
	}
	
	@Override
	public void sendMessage(User user, String message) throws RemoteException {
		for(User userMember : mainController.getUserList()){
			if(!userMember.getUsername().equals(user.getUsername())){
				userMember.getCallback().messageRecived(userMember.getUsername(), message);
			}
		}
	}
}
