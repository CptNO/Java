package pokerGame.Controllers;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import pokerGame.Entities.PlayerStatistics;
import pokerGame.Scenes.GameScenes;

public class StatisticsController extends BaseController {
	
	@FXML
	private Label userNameLable;
	@FXML
	private TextField hoursPlayedField;
	@FXML
	private TextField currentMoneyField;
	@FXML
	private TextField moneyLostField;
	@FXML
	private TextField moneyWonField;
	@FXML
	private Button returnButton;
	@FXML
	private AnchorPane mainPane;
	private PlayerStatistics statistics;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	@FXML
	private void onButtonReturnAction(ActionEvent e){
		mainController.setScene(GameScenes.MAIN_MENU);
	}
	
	@FXML
	private void onMouseEnteredAction(MouseEvent e){
		loadStatistics();
	}
	
	private void loadStatistics() {
		userNameLable.setText(mainController.getUser().getUsername());
		try {
			FileInputStream inputFileStream = new FileInputStream(mainController.getUser().getUsername() + ".ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(inputFileStream);
			statistics = (PlayerStatistics) objectInputStream.readObject();
			objectInputStream.close();
			inputFileStream.close();
			
			hoursPlayedField.setText(Double.toString(statistics.getHoursPlayed()));
			currentMoneyField.setText(Double.toString(statistics.getCurrentMoney()));
			moneyLostField.setText(Double.toString(statistics.getMoneyLost()));
			moneyWonField.setText(Double.toString(statistics.getMonneyWon()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
