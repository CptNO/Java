package pokerGame.Controllers;

import java.net.URL;
import java.rmi.Naming;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pokerGame.Entities.User;
import pokerGame.Scenes.GameScenes;
import server.rmi.ILogin;
import server.rmi.LoginMessage;

public class RegisterPaneController extends BaseController {

	@FXML
	private Button registerButton;
	
	@FXML
	private Button backButton;
	
	@FXML
	private TextField usernameField;
	
	@FXML
	private PasswordField passwordField;
	
	@FXML
	private Label statusLable;
	
	private LoginController loginController;
	
	private User user;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void onRegisterButtonAction(ActionEvent e){
		user = new User();
		user.setUsername(usernameField.getText());
		user.setPassword(passwordField.getText());
		try{
			ILogin login = (ILogin)Naming.lookup("rmi://"+mainController.getSettings().getServerLocation()+"/login");
			LoginMessage msg = login.registerUser(user);
			checkIfRegistrationIsValid(msg);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void checkIfRegistrationIsValid(LoginMessage message){
		switch (message.getType()) {
			case REGISTRATION_PASSED:
				loginController.mainController.setUser(user);
				statusLable.setText("User successfully registered");
				break;
			case REGISTRATION_FAILED:
				statusLable.setText(message.getAdditionalInfo());
				break;
			case SERVER_ERROR:
				break;
			default:
				//Empty
				break;
		}
	}
	
	@FXML
	private void onBackButtonAction(ActionEvent e){
		loginController.switchPane(mainController.getScene(GameScenes.LOGIN_PANE));
	}
	
	public void setLoginController(Object loginController){
		this.loginController = (LoginController)loginController;
	}
	

}
