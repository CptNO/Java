package server.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import server.scenes.ServerScenes;

public class DashboardController extends BaseController {
	
    @FXML
    private AnchorPane serverControlPane;
    @FXML
    private AnchorPane loggerPane;
    
	@Override
	public void initialize(URL location, ResourceBundle resources){
	}

	@Override
	public void injectController(Object controler) {
		this.mainController = (MainController) controler;
		loggerPane.getChildren().add(mainController.getScene(ServerScenes.LOGGER));
		serverControlPane.getChildren().add(mainController.getScene(ServerScenes.SERVER_CONTROL));
	}
}
