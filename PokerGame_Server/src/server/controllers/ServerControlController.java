package server.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ServerControlController extends BaseController {
    @FXML
    private Button startServerButton;
    @FXML
    private Button stopServerButton;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		stopServerButton.setDisable(true);
	}
	
    @FXML
    private void onStartServerButtonAction(ActionEvent e) {
    	setButtons(true);
		if(!mainController.startServer()){
			setButtons(false);
		}
    }

    @FXML
    private void onStopServerButtonAction(ActionEvent e) {
    	setButtons(false);
    	mainController.shutDownServer();
    }
    
    public void setButtons(Boolean serverRunning){
    	if(serverRunning == true){
    		stopServerButton.setDisable(true);
            startServerButton.setDisable(false);
    	}else{
        	stopServerButton.setDisable(true);
            startServerButton.setDisable(false);
    	}
    }
}
