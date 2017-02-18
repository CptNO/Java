package pokerGame.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import pokerGame.Scenes.GameScenes;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main{
	public static void main(String[] args) {
		Game game = new Game();
		game.startGame(args);
	}
}
