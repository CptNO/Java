package pokerGame.Controllers;

import java.net.URL;
import java.rmi.Naming;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import pokerGame.Entities.User;
import pokerGame.Entities.Room;
import pokerGame.Scenes.GameScenes;
import server.rmi.ILobby;
import server.rmi.LobbyMessage.Type;

public class RoomController extends BaseController {

	@FXML
	private Button leaveRoomButton;

	@FXML
	private Button startGameButton;

	@FXML
	private Label roomNameLabel;

	@FXML
	private ListView<User> userList;

	private ListProperty<User> listProperty;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Empty
	}

	public void bindRoom(Room room) {
		listProperty = new SimpleListProperty<>();
		listProperty.set(FXCollections.observableList(room.getUserList()));
		userList.itemsProperty().bind(listProperty);
		roomNameLabel.setText(room.getRoomName());
	}

	@FXML
	private void onLeaveRoomButtonAction(ActionEvent e) {
		try {
			ILobby lobby = (ILobby) Naming
					.lookup("rmi://" + mainController.getSettings().getServerLocation() + "/lobby");
			if (mainController.getUser().getUsername().equals(mainController.getRoom().getRoomOwner().getUsername())) {
				if (lobby.deleteRoom(mainController.getUser(), mainController.getRoom())
						.getType() == Type.ACTION_SUCSESS) {
				}
			} else {
				lobby.leaveRoom(mainController.getUser(), mainController.getRoom());
			}
			((LobbyController) mainController.getController(GameScenes.LOBBY))
					.switchRoomsPane(mainController.getScene(GameScenes.ROOMS_PANE));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setStartGameButton(User user) {
		
	}

	@FXML
	private void onStartGameButtonAction(ActionEvent e) {
		try {
			ILobby lobby = (ILobby) Naming.lookup("rmi://" + mainController.getSettings().getServerLocation() + "/lobby");
			lobby.startGame(mainController.getUser(), mainController.getRoom());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
