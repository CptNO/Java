package pokerGame.Controllers;

import java.net.URL;
import java.rmi.Naming;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import objects.LoginMessage;
import objects.Room;
import pokerGame.Scenes.GameScenes;
import rmi.ILobby;

public class NewRoomPaneController extends BaseController {
	@FXML
	private Button backButton;
	
	@FXML
	private Button createRoomButton;
	
	@FXML
	private TextField roomNameField;
	
	@FXML
	private Label roomStatusLable;
	
	private LobbyController lobbyController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void onBackButtonAction(ActionEvent e){
		lobbyController.switchRoomsPane(mainController.getScene(GameScenes.ROOMS_PANE));
	}
	
	@FXML
	private void onCreateRoomButtonAction(ActionEvent e){
		try{
			ILobby lobby = (ILobby)Naming.lookup("rmi://"+mainController.getSettings().getServerLocation()+"/lobby");
			Room newRoom = new Room(roomNameField.getText());
			newRoom.setRoomOwner(mainController.getUser());
			newRoom.addUser(mainController.getUser());
			newRoom = lobby.createRoom(mainController.getUser(), newRoom);
			if(newRoom != null){
				roomStatusLable.setText("Room created");
				mainController.setRoom(newRoom);
				((RoomController)mainController.getController(GameScenes.ROOM)).bindRoom(newRoom);
				lobbyController.switchRoomsPane(mainController.getScene(GameScenes.ROOM));
			}else
				roomStatusLable.setText("Room Name not valid");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	@Override
	public void injectController(Object controller) {
		this.mainController = (MainController) controller;
	}
	
	public void setLobbyController(LobbyController controller){
		this.lobbyController = controller;
	}
	
}
