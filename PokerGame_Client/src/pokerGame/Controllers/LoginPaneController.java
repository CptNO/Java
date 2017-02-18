package pokerGame.Controllers;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.channels.SocketChannel;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pokerGame.Entities.User;
import pokerGame.Scenes.GameScenes;
import server.rmi.CallbackImpl;
import server.rmi.IChat;
import server.rmi.ILogin;
import server.rmi.LoginMessage;

public class LoginPaneController extends BaseController {

	@FXML
	private Button loginButton;
	
	@FXML
	private Button exitButton;
	
	@FXML
	private Button registerButton;
	
	@FXML
	private TextField usernameField;
	
	@FXML
	private PasswordField passwordField;
	
	@FXML
	private Label loginStatusLable;
	
	private LoginController loginController;
	
	private User user;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	@FXML
	private void onLoginButtonAction(ActionEvent e){
		user = new User();
		user.setUsername(usernameField.getText());
		user.setPassword(passwordField.getText());
		try{
			user.setCallback(new CallbackImpl());
			((CallbackImpl)user.getCallback()).setMainController(mainController);
			ILogin login = (ILogin)Naming.lookup("rmi://"+mainController.getSettings().getServerLocation()+"/login");
			LoginMessage msg = login.authenticateUser(user);
			checkIfLoginIsValid(msg);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	private void checkIfLoginIsValid(LoginMessage message){
		if(message == null){
			return;
		}
		switch (message.getType()) {
		case LOGIN_PASSED:
			loginController.mainController.setUser(user);
			((MainMenuController)mainController.getController(GameScenes.MAIN_MENU)).startClock();
			loginController.mainController.setScene(GameScenes.MAIN_MENU);
			break;
		case LOGIN_FAILED:
			loginStatusLable.setText(message.getAdditionalInfo());
			break;
		case SERVER_ERROR:
			break;
		default:
			//Empty
			break;
		}
	}
	
	@FXML
	private void onExitButtonAction(ActionEvent e){
		mainController.exitApplication();
	}
	
	@FXML
	private void onRegisterButtonAction(ActionEvent e){
		loginController.switchPane(mainController.getScene(GameScenes.REGISTER_PANE));
	}
	
	public void setLoginController(Object loginController){
		this.loginController = (LoginController)loginController;
	}

}
