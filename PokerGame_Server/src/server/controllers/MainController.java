package server.controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;

import application.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import objects.Room;
import objects.User;
import pokerGame.Entities.Game;
import server.controllers.LoggerController.LoggerLevel;
import server.scenes.ServerScenes;

public class MainController extends StackPane {
    	
    private HashMap<ServerScenes, Node> scenes = new HashMap<>();
    
    private HashMap<ServerScenes, BaseController> controllers = new HashMap<>();
    
    private List<User> userList;
    
    private List<Room> roomList;
    
    private List<Game> gameList;
    
    private Settings settings;
    
    private NamingEnumeration<?> enumeration;
    
    public MainController(){
    	userList = new ArrayList<User>();
    	roomList = new ArrayList<Room>();
    }
    
    public void loadScene(ServerScenes id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ServerScenes.getStateClass(id)));
            Parent parent = (Parent) loader.load();
            BaseController controller = (BaseController) loader.getController();
            controller.injectController(this);
            addScene(id, parent);
            addController(id, controller);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error occured while loading scene: " + id);
        }
    }
    
    public void addScene(ServerScenes id, Node screen) {
        scenes.put(id, screen);
    }

    public void addController(ServerScenes type, BaseController controller){
    	controllers.put(type, controller);
    }
    
    /**
     * I false is returned server initalization failed
     * @return
     */
	public boolean startServer(){
		try {
			if(initJndi()){
				gameList = new ArrayList<>();
				initRmi();
				initSocket();
				getLogger().log("Server Started", LoggerLevel.INFO);
				((ServerControlController) getController(ServerScenes.SERVER_CONTROL)).setButtons(true);
				return true;
			}else{
				getLogger().log("Could not initalize server, Stopping server", LoggerLevel.ERROR);
			}
			return false;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public void initSettings(String settingsLocation) throws NullPointerException{
		settings = new Settings(this.getLogger(), settingsLocation);
		if(settings == null){
			throw new NullPointerException();
		}
	}
	
	private void initRmi(){
		RmiDeployController rmiController = new RmiDeployController();
		rmiController.setMainController(this);
		rmiController.initRmi();
	}
	
	private void initSocket(){
    	getLogger().log("Initializing Game Socket", LoggerLevel.INFO);
    }
	
	private boolean initJndi(){
		try{
			getLogger().log("Initializing JNDI", LoggerLevel.INFO);
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
			env.put(Context.PROVIDER_URL, "file:" + getCurrentWorkingDirectory());
			Context ctxt = new InitialContext(env);
			enumeration = ctxt.listBindings("");
			
			File file = (File)ctxt.lookup("Settings.xml");
			initSettings(file.getPath());
			return true;
		}catch(Exception ex){
			getLogger().log("Error Initializing JNDI", LoggerLevel.WARNING);
			getLogger().log("Settings.xml not found", LoggerLevel.ERROR);
			ex.printStackTrace();
			return false;
		}
	}
	
	private String getCurrentWorkingDirectory(){
		URL location = MainController.class.getProtectionDomain().getCodeSource().getLocation();
		return location.getFile();
	}
    
    public void shutDownServer(){
    	try{
    		userList.clear();
    		roomList.clear();
    		gameList.clear();
    	}catch(Exception ex){
    		getLogger().log(ex.getMessage(), LoggerLevel.ERROR);
    	}
    }

	public void setSettings(Settings settings) {
		this.settings = settings;
	}


	public List<Room> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<Room> roomList) {
		this.roomList = roomList;
	}
	
    public void setScene(final ServerScenes id) {
        setOpacity(1.0);
        getChildren().add(scenes.get(id));
    }
    
    public Node getScene(ServerScenes id) {
        return scenes.get(id);
    }
    
    public BaseController getController(ServerScenes type){
    	return controllers.get(type);
    }
    
    public LoggerController getLogger(){
    	return (LoggerController)getController(ServerScenes.LOGGER);
    }
    
    public Settings getSettings(){
    	return settings;
    }

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	public void addGame(Game game){
		this.gameList.add(game);
	}
	
	public List<Game> getGameList(){
		return gameList;
	}
	
	public NamingEnumeration<?> getNamingEnumeration(){
		return this.enumeration;
	}
	
	public void setOnGameEnd(Room room){
		for(User user : userList){
			try{
				user.getCallback().roomDeleted(room);
			}catch(Exception ex){
				
			}
		}
		for(Room r : roomList){
			if(r.getRoomName().equals(room.getRoomName())){
				roomList.remove(r);
				break;
			}
		}
	}
}
