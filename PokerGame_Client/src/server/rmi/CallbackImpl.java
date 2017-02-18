package server.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import objects.Card;
import objects.Room;
import objects.User;
import pokerGame.Controllers.ChatPaneController;
import pokerGame.Controllers.GameController;
import pokerGame.Controllers.LobbyController;
import pokerGame.Controllers.MainController;
import pokerGame.Controllers.RoomsPaneController;
import pokerGame.Scenes.GameScenes;
import rmi.IUserCallback;

public class CallbackImpl extends UnicastRemoteObject implements IUserCallback {

	private MainController mainController;
	
	public CallbackImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void userConnected(String userName) throws RemoteException {
		//Commit test
		// this is coomit for test 
	}

	@Override
	public void userDisconected(String userName) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void roomCreated(Room room) throws RemoteException {
		((RoomsPaneController)mainController.getController(GameScenes.ROOMS_PANE)).addTableRoom(room);
	}

	@Override
	public void roomDeleted(Room room) throws RemoteException {
		try{
			if(mainController.getRoom() != null && mainController.getRoom().getRoomName().equals(room.getRoomName())){
				((LobbyController)mainController.getController(GameScenes.LOBBY)).switchRoomsPane(mainController.getScene(GameScenes.ROOMS_PANE));
				mainController.setRoom(null);
			}
			((RoomsPaneController)mainController.getController(GameScenes.ROOMS_PANE)).removeTableRoom(room);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void userJoinedRoom(User user, Room room) throws RemoteException {
		System.out.println("User joined room " + user.getUsername() + " "+ room.getRoomName());
		if(mainController.getRoom() != null && mainController.getRoom().getRoomName().equals(room.getRoomName())){
			if(!user.getUsername().equals(mainController.getUser().getUsername())){
				mainController.getRoom().addUser(user);
			}
		}else{
			((RoomsPaneController)mainController.getController(GameScenes.ROOMS_PANE)).updateTableRoom(room);
		}
	}

	@Override
	public void userLeftRoom(User user, Room room) throws RemoteException {
		if(mainController.getRoom() != null && mainController.getRoom().getRoomName().equals(room.getRoomName())){
			mainController.getRoom().removeUser(user);
		}else{
			((RoomsPaneController)mainController.getController(GameScenes.ROOMS_PANE)).updateTableRoom(room);
		}
	}

	@Override
	public void messageRecived(String userName, String content) {
		try{
			((ChatPaneController)mainController.getController(GameScenes.CHAT)).addMessage(content, userName, false);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void setMainController(MainController mainController){
		this.mainController = mainController;
	}

	@Override
	public void gameStarted(String serverIp, int port) throws RemoteException {
		((GameController)mainController.getController(GameScenes.PLAY)).setupGame(port);
	}
}
