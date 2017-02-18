package pokerGame.Controllers;

import java.net.URL;
import java.rmi.Naming;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import rmi.IChat;



public class ChatPaneController extends BaseController {

	@FXML
	private TextArea sendTextBox;
	@FXML
	private Button sendButton;
	@FXML
	private FlowPane messageArea;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	@FXML
	private void onSendButtonPress(ActionEvent event) {
		String text = sendTextBox.getText();
		try{
			IChat chat = (IChat)Naming.lookup("rmi://"+mainController.getSettings().getServerLocation()+"/chat");
			chat.sendMessage(mainController.getUser(), text);
		}catch(Exception ex){
			text = "Message not sent: \n" + text;
		}
		addMessage(text, mainController.getUser().getUsername(), true);
		sendTextBox.clear();
	}
	
	public void addMessage(String message, String user, Boolean sent){
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
        		Label messageLable = new Label();
        		if(sent){
        			messageLable.setTextFill(Color.web("#00529B"));
        			messageLable.setAlignment(Pos.CENTER_LEFT);
        		}else{
        			messageLable.setTextFill(Color.web("#9F6000"));
        			messageLable.setAlignment(Pos.CENTER_RIGHT);
        		}
        		
        		messageLable.prefWidthProperty().bind(messageArea.widthProperty());
        		messageLable.setText(String.format("%s: \n \t %s", user, message));
        		messageArea.getChildren().add(messageLable);
            }
        });
		
	}

	@Override
	public void injectController(Object controler) {
		this.mainController = (MainController)controler;
	}

}
