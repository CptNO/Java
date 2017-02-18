package pokerGame.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import pokerGame.Entities.Player;
import pokerGame.Scenes.GameScenes;

public class LoginController extends BaseController {

	@FXML
	public StackPane childrenPane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Empty
	}
	
	@Override
	public void injectController(Object controler) {
		this.mainController = (MainController) controler;
		//addChatScene(this.mainController.getScene(GameScenes.CHAT));
		switchPane(this.mainController.getScene(GameScenes.LOGIN_PANE));
		
		((LoginPaneController)(this.mainController.getController(GameScenes.LOGIN_PANE))).setLoginController(this);
		((RegisterPaneController)(this.mainController.getController(GameScenes.REGISTER_PANE))).setLoginController(this);
	}
	
	public void switchPane(Node pane){
		this.childrenPane.getChildren().clear();
		this.childrenPane.getChildren().add(pane);
	}
}
