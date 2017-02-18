package pokerGame.Controllers;

import java.net.ServerSocket;
import java.nio.channels.SocketChannel;
import java.rmi.Naming;
import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import objects.Room;
import objects.User;
import pokerGame.Application.Settings;
import pokerGame.Scenes.GameScenes;
import rmi.ILogin;

public class MainController extends StackPane {

    private HashMap<GameScenes, Node> scenes = new HashMap<>();
    
    private HashMap<GameScenes, BaseController> controllers = new HashMap<>();
    
    private Settings settings;
    
    private User player;
    
    private Room currentRoom;    
    
    public MainController(){
    	settings = new Settings();
    }
    
    public void addScene(GameScenes id, Node screen) {
        scenes.put(id, screen);
    }
    
    public void loadScene(GameScenes id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(GameScenes.getStateClass(id)));
            Parent parent = (Parent) loader.load();
            BaseController controller = (BaseController) loader.getController();
            controller.injectController(this);
            loadStyleSheet(parent, id);
            addScene(id, parent);
            addController(id, controller);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error occured while loading scene: " + id);
        }
    }
    
    private void loadStyleSheet(Parent p, GameScenes id){
    	try{
    		System.out.println("INFO->Loading Styles for: " + id);
    		
    		p.getStylesheets().add(getClass().getResource(settings.getStyleSheetLocation()).toExternalForm());
    	}catch(Exception ex){
    		System.out.println("ERROR->Could not load style for: " + id + "\n \tLocation: ");
    		ex.printStackTrace();
    	}
    }

    public void unloadScene(GameScenes id) {
        if (scenes.remove(id) == null) {
            System.out.println("Error while removing scene " + id);
        }
    }

    public void setScene(final GameScenes id) {
        final DoubleProperty opacity = opacityProperty();
        if (!getChildren().isEmpty()) {
            Timeline fade = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                    new KeyFrame(new Duration(1000),
                            t -> {
							    getChildren().remove(0);
							    getChildren().add(0, scenes.get(id));
							    Timeline fadeIn = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
							            new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
							    fadeIn.play();

							}, new KeyValue(opacity, 0.0)));
            fade.play();
        } else {
            setOpacity(0.0);
            getChildren().add(scenes.get(id));
            Timeline fadeIn = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                    new KeyFrame(new Duration(2500), new KeyValue(opacity, 1.0)));
            fadeIn.play();
        }
    }

    public void setSettings(Settings settings){
    	this.settings = settings;
    }
    
    public Settings getSettings(){
    	return this.settings;
    }
    
    public Node getScene(GameScenes id) {
        return scenes.get(id);
    }

    public BaseController getController(GameScenes type){
    	return controllers.get(type);
    }
    
    public void addController(GameScenes type, BaseController controller){
    	controllers.put(type, controller);
    }
    
    public void exitApplication(){
    	logOut();
    	((GameController)getController(GameScenes.PLAY)).freeReasurces();
		Platform.exit();
		System.exit(0);
    }
    
    private void logOut(){
    	((GameController)getController(GameScenes.PLAY)).freeReasurces();
    	try{
    		ILogin login = (ILogin)Naming.lookup("rmi://"+getSettings().getServerLocation()+"/login");
    		login.logOutUser(player);
    	}catch(Exception ex){
    		System.out.println("ERROR-> ");
    		ex.printStackTrace();
    	}
    }

	public User getUser() {
		return player;
	}

	public void setUser(User player) {
		this.player = player;
	}
	
	public void setRoom(Room room){
		this.currentRoom = room;
	}
	
	public Room getRoom(){
		return this.currentRoom;
	}
}
