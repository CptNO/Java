package pokerGame.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import pokerGame.Scenes.GameScenes;

public class LobbyController extends BaseController{
	@FXML
	private Button backButton;
	@FXML
	private Button createRoomButton;
	@FXML
	private AnchorPane chatPane;
	@FXML
	private StackPane roomsPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	@Override
	public void injectController(Object controler) {
		this.mainController = (MainController) controler;
		setChatPane(this.mainController.getScene(GameScenes.CHAT));
		switchRoomsPane(this.mainController.getScene(GameScenes.ROOMS_PANE));
		
		((RoomsPaneController)(this.mainController.getController(GameScenes.ROOMS_PANE))).setLobbyController(this);
		((NewRoomPaneController)(this.mainController.getController(GameScenes.NEW_ROOM_PANE))).setLobbyController(this);
	}
	
	@FXML
	private void onBackButtonAction(ActionEvent e){
		mainController.setScene(GameScenes.MAIN_MENU);
	}

	private void setChatPane(Node chatPane){
		this.chatPane.getChildren().add(chatPane);
	}
	
	public void switchRoomsPane(Node pane){
		  Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
					roomsPane.getChildren().clear();
					roomsPane.getChildren().add(pane);
	            }
		  });
	}
}
