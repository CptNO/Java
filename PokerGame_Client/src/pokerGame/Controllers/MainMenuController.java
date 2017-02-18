package pokerGame.Controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;
import pokerGame.Scenes.GameScenes;

public class MainMenuController extends BaseController {

	@FXML
	private Button newGameButton;
	@FXML
	private Label clockLabel;
	@FXML
	private Button exitButton;
	@FXML
	private Button statisticsButton;
	@FXML
	private Label gameNameLabel;
	
	private double fontSize;
	private boolean isAnimationReversed;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fontSize = 10.00;
		isAnimationReversed = false;
	}
	
	@FXML
	private void onNewGameButtonPress(ActionEvent event){
		mainController.setScene(GameScenes.LOBBY);
	}
	
	@FXML
	private void onExitButtonPress(ActionEvent event){
		mainController.exitApplication();
	}
	
	@FXML
	private void onStatisticsButtonAction(ActionEvent e){
		mainController.setScene(GameScenes.STATISTICS);
	}

	@Override
	public void injectController(Object controller) {
		this.mainController = (MainController)controller;
		
	}
	
	public void startClock() {

		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(0), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				updateFontSize();
				Calendar time = Calendar.getInstance();
				String hour = "00";
				String minute = "00";
				String second = "00";
				if(time.get(Calendar.HOUR_OF_DAY) < 10){
					hour = "0"+time.get(Calendar.HOUR_OF_DAY);
				}else{
					hour = Integer.toString(time.get(Calendar.HOUR_OF_DAY));
				}
				if(time.get(Calendar.MINUTE) < 10){
					minute = "0"+time.get(Calendar.MINUTE);
				}else{
					minute = Integer.toString(time.get(Calendar.MINUTE));
				}
				if(time.get(Calendar.SECOND) < 10){
					second = "0"+time.get(Calendar.SECOND);
				}else{
					second = Integer.toString(time.get(Calendar.SECOND));
				}
				clockLabel.setAlignment(Pos.CENTER_RIGHT);
				clockLabel.setText(String.format("%s:%s:%s", hour, minute, second));
				gameNameLabel.setFont(new Font(fontSize));
			}
		}), new KeyFrame(Duration.millis(1)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	private void updateFontSize(){
		if(isAnimationReversed){
			fontSize += 0.01;
		}else
			fontSize -= 0.01;
		
		if(fontSize <= 10.00){
			isAnimationReversed = true;
		}else if(fontSize >= 20.00){
			isAnimationReversed = false;
		}
	}
}
