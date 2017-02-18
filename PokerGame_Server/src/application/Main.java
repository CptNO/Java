package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import server.controllers.MainController;
import server.scenes.ServerScenes;
import javafx.scene.Group;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			MainController mainController = new MainController();
			
			mainController.loadScene(ServerScenes.LOGGER);
			mainController.loadScene(ServerScenes.SERVER_CONTROL);
			mainController.loadScene(ServerScenes.DASHBOARD);
			
			mainController.setScene(ServerScenes.DASHBOARD);
			Group root = new Group();
			root.getChildren().addAll(mainController);
			Scene scene = new Scene(root);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
