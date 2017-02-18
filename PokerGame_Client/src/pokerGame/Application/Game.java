package pokerGame.Application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pokerGame.Controllers.MainController;
import pokerGame.Entities.Player;
import pokerGame.Entities.Room;
import pokerGame.Scenes.GameScenes;

public class Game extends Application {
	
	public Game(){
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			MainController mainController = new MainController();
			
			mainController.loadScene(GameScenes.MAIN_MENU);
			
			mainController.loadScene(GameScenes.LOGIN_PANE);
			mainController.loadScene(GameScenes.REGISTER_PANE);
			mainController.loadScene(GameScenes.LOGIN);
			
			mainController.loadScene(GameScenes.CHAT);
			mainController.loadScene(GameScenes.ROOMS_PANE);
			mainController.loadScene(GameScenes.NEW_ROOM_PANE);
			mainController.loadScene(GameScenes.ROOM);
			mainController.loadScene(GameScenes.LOBBY);
			mainController.loadScene(GameScenes.PLAY);
			mainController.loadScene(GameScenes.STATISTICS);
			
			mainController.setScene(GameScenes.LOGIN);
			
			Group root = new Group();
			root.getChildren().addAll(mainController);
			
			Scene scene = new Scene(root);
			
			primaryStage.initStyle(StageStyle.UNIFIED);
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(event -> {
				mainController.exitApplication();
		      });
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startGame(String[] args){
		launch(args);
	}
	
	

}
